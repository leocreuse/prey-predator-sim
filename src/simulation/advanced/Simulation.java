package simulation.advanced;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Simulation {
	/**
	 * Some useful constants.
	 */
	public static final int SIZE = 700;

	public static final double TIME_STEP = 1. / 24.;

	public static final Color BACKGROUND = Color.WHITE;

	public static final int NB_STEPS = 60 * 24;

	private int step;

	private Random rand;
	/** predators of the simulation */
	private ArrayList<Predator> preds;
	/** preys of the simulation */
	private ArrayList<Prey> preys;

	// private interconnectionClient client;
	/**
	 * border handling.
	 */
	private DummyClient dummy;
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
		dummy = new DummyClient();
		//preys.add(new Prey(200,200,0,0,cst));
		//preds.add(new Predator(300,200,0,0,cst));
		/**
		 * All preys and predators generated randomly
		 */
		for (int i = 0; i < nbPrey; i++) {
			preys.add(new Prey(rand.nextDouble() * SIZE, rand.nextDouble() * SIZE, rand.nextInt(11), 0, cst));
		}
		for (int i = 0; i < nbPred; i++) {
			preds.add(new Predator(rand.nextDouble() * SIZE, rand.nextDouble() * SIZE, rand.nextInt(11), 0, cst));
		}
		// System.out.println("All ready to go!");
	}
	/**
	 * returns returns a list of circles, for displaying purposes.
	 *  */
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
		System.out.println("\rStep: " + step + "; Preys: " + preys.size() + " ; Predators: " + preds.size());
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
	/**
	 * movement is done acording to the deplacement set in each animal.
	 */
	private void move() {
		for (Prey p : preys) {
			p.updateClosestPred(preds);
			p.computeNextMovement(rand.nextDouble());
			p.move();
		}
		for (Predator p : preds) {
			p.updateClosestPrey(preys);
			p.computeNextMovement(rand.nextDouble());
			p.move();
		}
	}
	/**
	 * this function will check each animal for boundaries infractions, and if it is not within limits, the animal is sent to the client for boundaries management.
	 */
	private void manageBorders() {
		for (int i = preys.size() - 1; i >= 0; i--) {
			Prey p = preys.get(i);
			if (!p.isWithinBorders()) {
				preys.remove(i);
				dummy.addPrey(p);
			}
		}
		if (dummy.getPreys() != null) {
			preys.addAll(dummy.getPreys());
		}
		for (int i = preds.size() - 1; i >= 0; i--) {
			Predator p = preds.get(i);
			if (!p.isWithinBorders()) {
				preds.remove(i);
				dummy.addPred(p);
			}
		}
		if (dummy.getPreds() != null) {
			preds.addAll(dummy.getPreds());
		}
		dummy.clearAll();

	}
	/**
	 * gives a chance to each predator to predate. The function in class predator will determine if predation is indeed possible.
	 * each predator will only eat it's closest prey per simulation step.
	 */
	private void predate() {
		for (Predator p : preds) {
			p.updateClosestPrey(preys);
			p.predate(rand.nextDouble());
		}
	}
	/**
	 * checks for each animal if reproduction is possible, and if so, creates new baby.
	 * each animal can only reproduce once per simulation step.
	 */
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
	/**
	 * updates hunger, age and death status for each animal.
	 */
	private void updateStatus() {
		for (Prey p : preys) {
			p.updateStatus();
		}
		for (Predator p : preds) {
			p.updateStatus();
		}

	}
	/**
	 * Cleans up the list of animals, by removing all dead animals.
	 */
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
		return step > NB_STEPS || (preds.isEmpty() || preys.isEmpty());
	}

	public void exit() {
		System.out.println("Finishing simulation... See ya!");
		System.exit(0);
	}
	/***
	 * evaluation function for experimental gradient method to find optimal settings.
	 * not working reliably currently.
	 */
	public double eval() {
		for (int i = 0; i < 480; i++) {
			update();
		}
		double npr = preds.size();
		double npy = preys.size();
		return Math.abs(npr - npy + 20) + Math.abs(npr - 80) + Math.abs(npy - 100);
	}

}