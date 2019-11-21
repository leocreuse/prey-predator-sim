package simulation.advanced;
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
				cst.PREDATOR_SPEED, cst.PREDATOR_REPRODUCTION_RADIUS,cst.PREDATOR_VIEW_DISTANCE,cst.PREDATOR_VIEW_ANGLE ,cst);
		hasPredated = true;
		closestPred = null;
		closestPrey = null;

	}

	/** generates a baby predator from given animal */
	public Predator(Animal original,double rand) {
		super(original,rand);
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
		return new Predator(ani,rand);
	}

	/**
	 * this function will determine if the predator can eat the closest prey, and if
	 * it can, changes all the status accordingly
	 */
	public boolean predate(double random) {
		if (closestPrey != null) {
			if (Animal.distance(this, closestPrey) < this.cst.PREDATION_RADIUS && !hasPredated) {
				// System.out.println("Eaten! Rawr!");
				this.setHunger(0.);
				this.setHasPredated(true);
				closestPrey.setDead();
				this.setMovement(this.getDep().getSpeed(), 2*Math.PI*random);
				return true;
			}
		}
		return false;
	}

	public void setHasPredated(boolean state) {
		hasPredated = state;
	}

	/** this function finds the closest living prey in field of view */
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
	
	/**Determines what the next movement of the predator will be:\n 
	 * if there is a rey within line of sight the predator sprints towards it\n
	 * if not, it continues with a straight movement.\n
	 * Speed proportional to hunger state */
	public void computeNextMovement(double random) {
		if(closestPrey != null && Animal.distance(this, closestPrey)<this.getViewingDistance()) {
			double direction = Math.atan2(closestPrey.getCenterY()-this.getCenterY(),closestPrey.getCenterX()-this.getCenterX() );
			double speed = 2*(0.1 + (1-this.getHunger())*0.9) * cst.PREDATOR_SPEED;
			this.setMovement(speed, direction);
		}
		else {
			double direction = this.getDep().getDirection();
			double speed = (0.1 + (1-this.getHunger())*0.9) * cst.PREDATOR_SPEED;
			this.setMovement(speed, direction);
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