package simulation.base;

/**
 * 
 * This class describes the behavior of a prey.
 * Many methods resemble closely those of predator, check that class for more information on the related methods.
 * 
 * @author leo
 * 
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Prey extends Animal {

	private Prey closestPrey;

	private Predator closestPred;

	public Prey(double x, double y, double age, double hunger, Cst cst) {
		super(x, y, cst.PREY_COLOR, 0., cst.PREY_REPRODUCTION_PROBABILITY, age, hunger, cst.PREY_SPEED,
				cst.PREY_REPRODUCTION_RADIUS, cst);
		closestPrey = null;
		closestPred = null;
	}

	public Prey(Animal original) {
		super(original);
		closestPrey = null;
		closestPred = null;
	}

	public static Prey reproduce(Prey p1, Prey p2, double rand) {
		Animal ani = Animal.reproduce(p1, p2, rand);
		if (ani == null) {
			return null;
		}
		return new Prey(ani);
	}

	public void updateClosestPrey(ArrayList<Prey> list) {
		if (list.size() > 1) {
			list.remove(this);
			closestPrey = Collections.min(list, Comparator.comparing(x -> Animal.reprodComp(this, x)));
			list.add(this);
		} else {
			closestPrey = null;
		}
	}

	public void updateClosestPred(ArrayList<Predator> list) {
		if (list.size() > 0) {
			closestPred = Collections.min(list, Comparator.comparing(x -> Animal.deadComp(this, x)));
		} else {
			closestPred = null;
		}
	}

	public Prey getClosestPrey() {
		return closestPrey;
	}

	public Predator getClosestPred() {
		return closestPred;
	}

}