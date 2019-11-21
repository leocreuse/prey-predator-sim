package simulation.multiServer;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Simulation {
	/**
	 * Some useful constants.
	 */
	public static final int SIZE = 200;

	public static final double TIME_STEP = 1. / 3.;

	public static final Color BACKGROUND = Color.WHITE;

	public static final int NB_STEPS = 20 * 3;

	private int step;

	private Random rand;
	/** predators of the simulation */
	private ArrayList<Predator> preds;
	/** preys of the simulation */
	private ArrayList<Prey> preys;

	private interconnectionClient client;
	/**
	 * border handling.
	 */
	// private DummyClient client;
	/**
	 * holds all of the other constants of the simulation
	 */
	protected Cst cst;

	public Simulation(int nbPrey, int nbPred, Cst cst) {
		// System.out.println("Welcome to simulation super awsome over 9000!");
		// System.out.println("generating creatures...");
		this.cst = cst;
		preys = new ArrayList<Prey>();
		preds = new ArrayList<Predator>();
		rand = new Random(System.currentTimeMillis());
		step = 0;
		try {
			client = new interconnectionClient(cst);
		}
		catch(Exception e) {
			System.out.println("Error initializing client, exiting now.");
			System.exit(-1);
		}
		 //preys.add(new Prey(150,100,0,0,cst));
		 preys.add(new Prey(rand.nextInt(200),101,0,0,cst));
		/**
		 * All preys and predators generated randomly
		 */
		/*for (int i = 0; i < nbPrey; i++) {
			preys.add(new Prey(rand.nextDouble() * SIZE, rand.nextDouble() * SIZE, rand.nextInt(21), 0, cst));
		}
		for (int i = 0; i < nbPred; i++) {
			preds.add(new Predator(rand.nextDouble() * SIZE, rand.nextDouble() * SIZE, rand.nextInt(21), 0, cst));
		}*/
		// System.out.println("All ready to go!");
	}

	public ArrayList<Circle> getElements() {
		ArrayList<Circle> res = new ArrayList<>();
		res.addAll(preys);
		res.addAll(preds);
		return res;
	}

	/**
	 * preforms one step of the simulation with theses intermediary steps: - compute
	 * new position - manage animals outside bounds - change animal-intrisic
	 * properties (hunger, age, etc...) - predadors still alive predate - predators
	 * and preys still alive reproduce - remove all dead animals from simulation
	 */

	public void update() {
		System.out.println("\rStep: " + step + "; Preys: " + preys.size() + " ; Predators: " + preds.size()+"               ");
		// if(preys.size()<500 && preds.size()<500) {
		step++;
		move();
		manageBorders();
		updateStatus();
		predate();
		reproduce();
		cleanUp();
		// System.out.println("Step done.");
		// }
	}

	private void move() {
		for (Prey p : preys) {
			p.computeNextMovement(rand.nextDouble());
			p.move();
		}
		for (Predator p : preds) {
			p.computeNextMovement(rand.nextDouble());
			p.move();
		}
	}

	private void manageBorders() {
		for (int i = preys.size() - 1; i >= 0; i--) {
			Prey p = preys.get(i);
			if (!p.isWithinBorders()) {
				preys.remove(i);
				client.addPrey(p);
			}
		}
		for (int i = preds.size() - 1; i >= 0; i--) {
			Predator p = preds.get(i);
			if (!p.isWithinBorders()) {
				preds.remove(i);
				client.addPred(p);
			}
		}
		try {
			client.exchangeData(preys.size(), preds.size());
		}
		catch(Exception e){
			System.out.println("Some problem occured while exchanging data, here's the error: "+ e.getMessage());
			System.exit(-1);
		}
		if (client.newPreys()) {
			
			preys.addAll(client.getPreys());
		}
		if (client.newPreds()) {
			preds.addAll(client.getPreds());
		}
		client.clearAll();
		//System.out.println(preys);

	}

	private void predate() {
		for (Predator p : preds) {
			p.updateClosestPrey(preys);
			p.predate();
		}
	}

	private void reproduce() {
		// System.out.println("Trying to reproduce");
		Prey p;
		int nb = preys.size();
		for (int i = 0; i < nb; i++) {
			// System.out.println(i);
			p = preys.get(i);
			p.updateClosestPrey(preys);
			// System.out.println(p);
			// System.out.println(p.getClosestPrey());
			if (p.getClosestPrey() != null && Animal.canReproduce(p, p.getClosestPrey())) {
				// System.out.println("reproduction possible ;)");
				Prey baby = Prey.reproduce(p, p.getClosestPrey(), rand.nextDouble());
				if (baby != null) {
					preys.add(baby);
				}
			}
		}
		Predator p1;
		nb = preds.size();
		for (int i = 0; i < nb; i++) {
			p1 = preds.get(i);
			p1.updateClosestPred(preds);
			if (p1.getClosestPred() != null && Animal.canReproduce(p1, p1.getClosestPred())) {
				Predator baby = Predator.reproduce(p1, p1.getClosestPred(), rand.nextDouble());
				if (baby != null) {
					preds.add(baby);
				}
			}
		}
	}

	private void updateStatus() {
		for (Prey p : preys) {
			p.updateStatus();
		}
		for (Predator p : preds) {
			p.updateStatus();
		}

	}

	private void cleanUp() {
		for (int i = preys.size() - 1; i >= 0; i--) {
			if (preys.get(i).getHasDied()) {
				preys.remove(i);
			} else {
				preys.get(i).setHasReproduced(false);
			}
		}
		for (int i = preds.size() - 1; i >= 0; i--) {
			if (preds.get(i).getHasDied()) {
				preds.remove(i);
			} else {
				preds.get(i).setHasReproduced(false);
				preds.get(i).setHasPredated(false);
			}
		}
	}

	public boolean isTerminated() {
		return step > NB_STEPS || (preds.isEmpty() && preys.isEmpty());
	}

	public void exit() {
		System.out.println("\nFinishing simulation... See ya!");
		System.exit(0);
	}

	public double eval() {
		for (int i = 0; i < 480; i++) {
			update();
		}
		double npr = preds.size();
		double npy = preys.size();
		return Math.abs(npr - npy + 20) + Math.abs(npr - 80) + Math.abs(npy - 100);
	}

}