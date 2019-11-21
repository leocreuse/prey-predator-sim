package simulation.simpleServer;

import java.util.ArrayList;

import simulation.base.Simulation;

import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

public class interconnectionClient {

	private ArrayList<Prey> preyList;

	private ArrayList<Predator> predList;

	private Socket socket;
	
	private DataInputStream input;
	
	private DataOutputStream output;
	
	private int extPreyData = 2*8;
	
	private int extPredData = 2*8;
	
	private Cst cst;
	
	public interconnectionClient(Cst cst) throws IOException {
		this.cst = cst;
		socket = new Socket(InetAddress.getLoopbackAddress(),6789);
		preyList = new ArrayList<Prey>();
		predList = new ArrayList<Predator>();
		input = new DataInputStream((InputStream) socket.getInputStream());
		output = new DataOutputStream((OutputStream) socket.getOutputStream());
		//System.out.println("Simulation id: "+ input.readInt());
		output.writeInt(extPredData);
		output.writeInt(extPreyData);
		output.flush();
	}

	public void exchangeData(int nb_preys, int nb_preds) throws IOException {
		//System.out.print("\r"+nb_preys+" "+nb_preds);
		output.writeInt(nb_preys);
		output.writeInt(nb_preds);
		output.writeInt(preyList.size());
		output.writeInt(predList.size());
		for(Prey p: preyList) {
			output.writeDouble(p.getCenterX());
			output.writeDouble(p.getCenterY());
			output.writeDouble(p.getAge());
			output.writeDouble(p.getHunger());
		}
		for(Predator p: predList) {
			output.writeDouble(p.getCenterX());
			output.writeDouble(p.getCenterY());
			output.writeDouble(p.getAge());
			output.writeDouble(p.getHunger());
		}
		output.flush();
		clearAll();
		int n_inpy = input.readInt();
		int n_inpr = input.readInt();
		//System.out.println("");
		//System.out.println(n_inpy);
		//System.out.println(n_inpr);
		for(int i = 0; i< n_inpy; i++) {
			preyList.add(new Prey(input.readDouble(),input.readDouble(),input.readDouble(),input.readDouble(),cst));
			//System.out.println(preyList.get(preyList.size()-1).getCenterX()*Simulation.SIZE);
		}
		for(int i = 0; i< n_inpr; i++) {
			predList.add(new Predator(input.readDouble(),input.readDouble(),input.readDouble(),input.readDouble(),cst));
		}
		//System.out.println(preyList);
		//System.out.println(predList);
	}

	public ArrayList<Prey> getPreys() {
		for(int i = 0; i<preyList.size();i++) {
			Prey p = preyList.get(i);
			p.setCenterX(p.getCenterX() *Simulation.SIZE);
			p.setCenterY(p.getCenterY() *Simulation.SIZE);
		}
		return preyList;
	}

	public ArrayList<Predator> getPreds() {
		for(Predator p : predList) {
			p.setCenterX(p.getCenterX() *Simulation.SIZE);
			p.setCenterY(p.getCenterY() *Simulation.SIZE);
		}
		return predList;
	}

	public void addPrey(Prey p) {
		p.setCenterX(p.getCenterX() /(double)Simulation.SIZE);
		p.setCenterY(p.getCenterY() /(double)Simulation.SIZE);
		preyList.add(p);
	}

	public void addPred(Predator p) {
		p.setCenterX(p.getCenterX() /(double)Simulation.SIZE);
		p.setCenterY(p.getCenterY() /(double)Simulation.SIZE);
		predList.add(p);
	}

	public void clearAll() {
		preyList.clear();
		predList.clear();
	}
	public boolean newPreys() {
		return preyList.size()>0;
	}
	public boolean newPreds() {
		return predList.size()>0;
	}

}