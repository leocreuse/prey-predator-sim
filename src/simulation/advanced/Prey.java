package simulation.advanced;

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
				cst.PREY_REPRODUCTION_RADIUS,cst.PREDATOR_VIEW_DISTANCE,cst.PREY_VIEW_DISTANCE ,cst);
		closestPrey = null;
		closestPred = null;
	}

	public Prey(Animal original,double rand) {
		super(original,rand);
		closestPrey = null;
		closestPred = null;
	}

	public static Prey reproduce(Prey p1, Prey p2, double rand) {
		Animal ani = Animal.reproduce(p1, p2, rand);
		if (ani == null) {
			return null;
		}
		return new Prey(ani,rand);
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
	/**
	 * For a Prey, if there's a predator within line of sight, the prey will flee it while sprinting, 
	 * otherwise it will randomly roam the fields.
	 * 
	 */
	public void computeNextMovement(double rand) {
		if(closestPred != null && Animal.distance(this, closestPred)<this.getViewingDistance()) {
			double direction = Math.atan2(this.getCenterY()-closestPred.getCenterY(),this.getCenterX()-closestPred.getCenterX() );
			double speed = 2* cst.PREY_SPEED;
			this.setMovement(speed, direction);
		}
		else {
			double direction = 2*Math.PI * rand;
			double speed = cst.PREDATOR_SPEED;
			this.setMovement(speed, direction);
		}
	}
	
	public Prey getClosestPrey() {
		return closestPrey;
	}

	public Predator getClosestPred() {
		return closestPred;
	}

}