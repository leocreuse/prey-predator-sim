package simulation.multiServer;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * This class describes an animal, which can be either a Prey or a Predator in
 * the simulation. It extends circle so it can be drawn more easily by the
 * graphical front-end.
 * 
 * 
 * @author leo
 *
 */

public class Animal extends Circle {

	private double age;

	private double hunger;

	private double hungerRate;

	private double reproductionProbability;
	/**
	 * used to know if the creature has already reproduced this cycle; reset at end
	 * of cycle.
	 */
	private boolean hasReproduced;
	/**
	 * used to know if this creature has already died during this cycle; reset at
	 * end of cycle.
	 */
	private boolean hasDied;
	/** holds the direction and speed of the creature */
	private Movement dep;
	/** maximal distance for two animals to reproduce */
	private final double reproductionRadius;

	protected final Cst cst;

	public Animal(double centerX, double centerY, Color col, double hungerRate, double reproProb, double age,
			double hunger, double speed, double repRad, Cst cst) {
		super(centerX, centerY, cst.RADIUS, col);
		this.cst = cst;
		this.hungerRate = hungerRate;
		this.reproductionProbability = reproProb;
		this.hunger = hunger;
		this.age = age;
		this.hasDied = false;
		this.hasReproduced = false;
		this.dep = new Movement(speed, 0);
		this.reproductionRadius = repRad;
	}

	/** copy constructor */
	public Animal(Animal original) {
		super(original.getCenterX(), original.getCenterY(), original.getRadius(), original.getFill());
		this.cst = original.cst;
		this.hungerRate = original.hungerRate;
		this.reproductionProbability = original.reproductionProbability;
		this.hunger = 0;
		this.age = 0;
		this.hasDied = false;
		this.hasReproduced = false;
		this.dep = new Movement(original.dep);
		this.reproductionRadius = original.reproductionRadius;
	}

	/**
	 * indicates whether two creatures a1 and a2 satisfy all conditions to reproduce
	 * together
	 */
	public static boolean canReproduce(Animal a1, Animal a2) {
		// System.out.println("Is this gonna work?");
		return !a2.hasReproduced && !a1.hasReproduced && a1.age > 2 && !a1.hasDied && a2.age > 2 && !a2.hasDied
				&& distance(a1, a2) < a1.reproductionRadius && a1.hunger < 0.5 && a2.hunger < 0.5;
	}

	/** changes the movement of the animal with given speed and direction */
	public void setMovement(double speed, double direction) {
		(this.dep).setMovement(speed, direction);
	}

	/**
	 * returns a new animal on the same position than a1, or null depending on rand.
	 * rand should be a random double between 0 and 1, to emulate reproduction
	 * probability this function doesn't check for reproduction conditions, this
	 * should be taken care with the function "canReproduce"
	 */
	public static Animal reproduce(Animal a1, Animal a2, double rand) {
		// System.out.println("New baby animal! awwwww...");
		a1.setHasReproduced(true);
		a2.setHasReproduced(true);
		if (rand < a1.reproductionProbability) {
			return new Animal(a1);
		} else {
			return null;
		}
	}

	public double getHunger() {
		return hunger;
	}

	public void setHunger(double hunger) {
		this.hunger = hunger;
	}

	/**
	 * updates the status of the animal: increase hunger and age, and make the die
	 * accordingly
	 */
	public void updateStatus() {
		age += Simulation.TIME_STEP;
		hunger += hungerRate * Simulation.TIME_STEP;
		if (age > 40 || hunger > 1) {
			// System.out.println("too old or too hungry");
			hasDied = true;
		}
	}

	/**
	 * moves the position of the animal according to it's speed and direction. This
	 * takes into account the duration of the simulation step.
	 */
	public void move() {
		double dx = (Simulation.TIME_STEP) * dep.getSpeed() * Math.cos(dep.getDirection());
		double dy = (Simulation.TIME_STEP) * dep.getSpeed() * Math.sin(dep.getDirection());
		this.setCenterX(this.getCenterX() + dx);
		this.setCenterY(this.getCenterY() + dy);
	}

	public void setDead() {
		hasDied = true;
	}

	public void setHasReproduced(boolean state) {
		hasReproduced = state;
	}

	public static double distance(Animal a1, Animal a2) {
		return Math.sqrt((a1.getCenterX() - a2.getCenterX()) * (a1.getCenterX() - a2.getCenterX())
				+ (a1.getCenterY() - a2.getCenterY()) * (a1.getCenterY() - a2.getCenterY()));
	}

	public boolean getHasDied() {
		return hasDied;
	}

	/**
	 * in this implementation the next movement is generated with the same speed and
	 * a random direction, if random is a double randomly generated beween 0 and 1
	 */
	public void computeNextMovement(double random) {
		this.setMovement(this.dep.getSpeed(), 2 * Math.PI * random);
	}

	public boolean isWithinBorders() {
		return this.getCenterX() >= 0 && this.getCenterX() <= Simulation.SIZE && this.getCenterY() >= 0
				&& this.getCenterY() <= Simulation.SIZE;
	}

	public String toString() {
		return super.toString() + "Dep[speed=" + dep.getSpeed() + ", dir=" + dep.getDirection() + "]" + "Dead="
				+ hasDied + ", rep=" + hasReproduced;
	}

	/**
	 * this function returns the "living distance" between orig and other: this
	 * distance is used to find the closes living animal.
	 */
	public static double deadComp(Animal orig, Animal other) {
		double res = 0;
		res = other.hasDied ? 2 * Simulation.SIZE : 0;
		res += distance(orig, other);
		return res;
	}

	/**
	 * this function returns the "reproduction prey distance" between orig and
	 * other: this distance is used to find the closes living and capable of
	 * reproduction animal.
	 */
	public static double reprodComp(Animal orig, Animal other) {
		double res = 0;
		res = other.hasReproduced || other.hasDied ? 2 * Simulation.SIZE : 0;
		res += distance(orig, other);
		return res;
	}

	public double getAge() {
		return age;
	}

	public Movement getDep() {
		return dep;
	}

}