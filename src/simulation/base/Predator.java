package simulation.base;
/**
 * This class describes the predators of the simulation. as all predators are animals, it extends Animal.
 * @author leo
 * 
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Predator extends Animal {
	private boolean hasPredated;

	private Predator closestPred;

	private Prey closestPrey;

	public Predator(double x, double y, double age, double hunger, Cst cst) {
		super(x, y, cst.PREDATOR_COLOR, cst.PREDATOR_HUNGER_RATE, cst.PREDATOR_REPRODUCTION_PROBABILITY, age, hunger,
				cst.PREDATOR_SPEED, cst.PREDATOR_REPRODUCTION_RADIUS, cst);
		hasPredated = true;
		closestPred = null;
		closestPrey = null;

	}

	/** generates a predator from given animal */
	public Predator(Animal original) {
		super(original);
		hasPredated = true;
		this.closestPred = null;
		this.closestPrey = null;
	}

	/**
	 * creates a new pedator from p1 and p2, this function does not check for
	 * reproduction conditions
	 */
	public static Predator reproduce(Predator p1, Predator p2, double rand) {
		Animal ani = Animal.reproduce(p1, p2, rand);
		if (ani == null) {
			return null;
		}
		return new Predator(ani);
	}

	/**
	 * this function will determine if the predator can eat the closest prey, and if
	 * it can change all the status accordingly
	 */
	public boolean predate() {
		if (closestPrey != null) {
			if (Animal.distance(this, closestPrey) < this.cst.PREDATION_RADIUS && !hasPredated) {
				// System.out.println("Eaten! Rawr!");
				this.setHunger(0.);
				this.setHasPredated(true);
				closestPrey.setDead();
				return true;
			}
		}
		return false;
	}

	public void setHasPredated(boolean state) {
		hasPredated = state;
	}

	/** this function finds the closest living prey */
	public void updateClosestPrey(ArrayList<Prey> list) {
		if (list.size() > 0) {
			closestPrey = Collections.min(list, Comparator.comparing(x -> Animal.deadComp(this, x)));
		} else {
			closestPrey = null;
		}
	}

	/**
	 * this function finds the closest living and capable of reproduction predator
	 */
	public void updateClosestPred(ArrayList<Predator> list) {
		if (list.size() > 1) {
			list.remove(this);
			closestPred = Collections.min(list, Comparator.comparing(x -> Animal.reprodComp(this, x)));
			list.add(this);
		} else {
			closestPred = null;
		}
	}

	public boolean getHasPredated() {
		return hasPredated;
	}

	public Predator getClosestPred() {
		return closestPred;
	}

	public Prey getClosestPrey() {
		return closestPrey;
	}

}