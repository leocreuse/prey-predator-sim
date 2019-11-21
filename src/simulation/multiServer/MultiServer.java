package simulation.multiServer;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * MultiServer for the Predators and Preys project.
 * 
 * 
 * 
 * 
 * @author Leo
 *
 */
public class MultiServer {
	private ArrayList<Socket> sockets;
	private ArrayList<DataInputStream> inputs;
	private ArrayList<DataOutputStream> outputs;
	private int extPreyLength;
	private int extPredLength;
	private int nb_clients;
	private int square_size;
	private int total_prey;
	private int total_pred;
	private int[] incomming_preys;
	private int[] incomming_preds;
	private class DataPacket{
		public double x;
		public double y;
		public byte[] ext;
		public int dest;
		public DataPacket(int dest, double x, double y, int extLength) {
			this.dest = dest;
			this.x = x;
			this.y = y;
			this.ext = new byte[extLength];
		}
	}
	
	private ArrayList<DataPacket> dataPrey;
	private ArrayList<DataPacket> dataPred;
	
	/**
	 * Creates a MultiServer.
	 * 
	 * @param n
	 *            The number of clients to wait for the simulation to begin. Lust be a square.
	 * @throws IOException
	 *             if an error occurs when opening or closing the server socket, or
	 *             when waiting for a connection, or when working with the streams
	 *             associated with the accepted connection (creation of the input
	 *             and output stream).
	 * 
	 */
	public MultiServer(int n) throws IOException {
		nb_clients = n;
		square_size = (int) Math.sqrt(n);
		//System.out.println(square_size);
		total_prey = 0;
		total_pred = 0;
		incomming_preys= new int[nb_clients];
		incomming_preds= new int[nb_clients];
		ServerSocket listenSocket = new ServerSocket(6789);
		sockets = new ArrayList<Socket>();
		outputs = new ArrayList<DataOutputStream>();
		inputs = new ArrayList<DataInputStream>();
		dataPrey = new ArrayList<DataPacket>();
		dataPred = new ArrayList<DataPacket>();
		for(int i = 0; i<n;i++) {
			sockets.add(listenSocket.accept());
			outputs.add(new DataOutputStream(sockets.get(i).getOutputStream()));
			inputs.add(new DataInputStream(sockets.get(i).getInputStream()));
			extPreyLength = inputs.get(i).readInt();
			extPredLength = inputs.get(i).readInt();
			outputs.get(i).writeInt(i);
			outputs.get(i).flush();
		}
		listenSocket.close();
	}

	public void recieve() throws IOException {
		for(int i = 0; i<nb_clients;i++) {
			total_prey+=inputs.get(i).readInt();
			total_pred+=inputs.get(i).readInt();
			int nb_prey = inputs.get(i).readInt();
			int nb_pred = inputs.get(i).readInt();
			for(int j=0;j<nb_prey; j++) {
				double x = inputs.get(i).readDouble();
				double y = inputs.get(i).readDouble();
				int dest = this.destination(i, x, y);
				incomming_preys[dest]+=1;
				x = x-Math.floor(x);
				y = y-Math.floor(y);
				DataPacket p = new DataPacket(dest,x,y,extPreyLength);
				inputs.get(i).read(p.ext, 0, extPreyLength);
				dataPrey.add(p);
			}
			//System.out.println("Hey!");
			
			for(int j=0;j<nb_pred; j++) {
				double x = inputs.get(i).readDouble();
				double y = inputs.get(i).readDouble();
				int dest = this.destination(i, x, y);
				//System.out.println(dest);
				incomming_preds[dest]+=1;
				x = x-Math.floor(x);
				y = y-Math.floor(y);
				DataPacket p = new DataPacket(dest,x,y,extPredLength);
				inputs.get(i).read(p.ext, 0, extPredLength);
				dataPred.add(p);
			}
			
		}
	}
	
	public void send() throws IOException {
		for(int i = 0; i<nb_clients; i++) {
			outputs.get(i).writeInt(incomming_preys[i]);
			outputs.get(i).writeInt(incomming_preds[i]);
			incomming_preys[i] = 0;
			incomming_preds[i] = 0;			
		}
		DataPacket p;
		for(int i = 0; i< dataPrey.size();i++) {
			p = dataPrey.get(i);
			outputs.get(p.dest).writeDouble(p.x);
			outputs.get(p.dest).writeDouble(p.y);
			outputs.get(p.dest).write(p.ext);
		}
		for(int i = 0; i< dataPred.size();i++) {
			p = dataPred.get(i);
			outputs.get(p.dest).writeDouble(p.x);
			outputs.get(p.dest).writeDouble(p.y);
			outputs.get(p.dest).write(p.ext);
		}
		for(int i = 0; i<nb_clients; i++) {
			outputs.get(i).flush();
		}
	}
	
	public void run() throws IOException {
			while(true) {
				total_prey = 0;
				total_pred = 0;
				recieve();
				System.out.print("\rRemaing Preys|Preds : "+total_prey+" | "+total_pred+"                ");
				send();
				dataPrey.clear();
				dataPred.clear();
			}
	}
	


	/**
	 * Sample main program. This creates a SimpleServer and runs it. Run this
	 * program and optionally provide the TCP port number as a command line argument
	 * (port 6789 will be used if no argument is provided), e.g:
	 * <p>
	 * java fr.isae.project2017.SimpleServer 6502
	 * <p>
	 * 
	 * @param args
	 *            the arguments on the command line. Should be either an empty array
	 *            of strings, or an array of a single string giving the decimal
	 *            representation of the port number. If the array is empty, default
	 *            value of 6789 will be used for the TCP port.
	 */
	public static void main(String[] args) {
		MultiServer server = null;
		try {
			int n = args.length == 1 ? Integer.parseInt(args[0]) : 1;
			System.out.println("Waiting for "+n+" clients...");
			server = new MultiServer(n);
			server.run();
		} catch (IOException e) {
			System.out.println("Bad server usage, try again");
		}
	}
	
	private int destination(int id, double x, double y) {
		int x0 = id % square_size;
		int y0 = id / square_size;
		x0 += (x>1) ? 1 : ((x<0)? -1 : 0) ;
		y0 += (y>1) ? 1 : ((y<0)? -1 : 0) ;
		y0 =(y0 + ((y0<0) ? square_size : 0))%square_size;
		x0 =(x0 + ((x0<0) ? square_size : 0))%square_size;
		return x0+square_size*y0;
	}
}
