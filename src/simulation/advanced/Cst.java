package simulation.advanced;

import javafx.scene.paint.Color;

public class Cst {

	/**
	 * useful constants set at the beginning of the simulation
	 */

	/**
	 * Predator constants
	 */
	public final double PREDATION_RADIUS;
	public final Color PREDATOR_COLOR;
	public final double PREDATOR_SPEED;
	public final double PREDATOR_REPRODUCTION_RADIUS;
	public final double PREDATOR_REPRODUCTION_PROBABILITY;
	public final double PREDATOR_HUNGER_RATE;
	public final double PREDATOR_VIEW_DISTANCE;
	public final double PREDATOR_VIEW_ANGLE;

	/**
	 * Prey constants
	 */
	public final Color PREY_COLOR;
	public final double PREY_SPEED;
	public final double PREY_REPRODUCTION_RADIUS;
	public final double PREY_REPRODUCTION_PROBABILITY;
	public final double PREY_HUNGER_RATE;
	public final double PREY_VIEW_DISTANCE;
	public final double PREY_VIEW_ANGLE;
	/**
	 * Animal constants
	 */

	/** radius of the circle to be drawn */
	public final double RADIUS;

	/** default, "most stable" settings yet */
	public Cst() {
		RADIUS = 3.;
		PREY_HUNGER_RATE = 0.;
		PREY_REPRODUCTION_PROBABILITY = 0.70;
		PREY_REPRODUCTION_RADIUS = 6;
		PREY_SPEED = 140;
		PREY_COLOR = Color.LIGHTSKYBLUE;
		PREY_VIEW_DISTANCE = 30;
		PREY_VIEW_ANGLE = Math.PI;
		PREDATOR_HUNGER_RATE = 0.08;
		PREDATOR_REPRODUCTION_PROBABILITY = 0.5;
		PREDATOR_REPRODUCTION_RADIUS = 7;
		PREDATOR_SPEED = 110;
		PREDATOR_COLOR = Color.CRIMSON;
		PREDATION_RADIUS = 8;
		PREDATOR_VIEW_DISTANCE = 40;
		PREDATOR_VIEW_ANGLE = Math.PI/3;
	}

	public Cst(double pyhr, double pyrp, double pyrr, double pys, double prhr, double prrp, double prrr, double prs,
			double prpr, double pyvd, double prvd, double pyva, double prva) {
		RADIUS = 3.;
		PREY_HUNGER_RATE = pyhr;
		PREY_REPRODUCTION_PROBABILITY = pyrp;
		PREY_REPRODUCTION_RADIUS = pyrr;
		PREY_SPEED = pys;
		PREY_COLOR = Color.LIGHTSKYBLUE;
		PREDATOR_HUNGER_RATE = prhr;
		PREDATOR_REPRODUCTION_PROBABILITY = prrp;
		PREDATOR_REPRODUCTION_RADIUS = prrr;
		PREDATOR_SPEED = prs;
		PREDATOR_COLOR = Color.CRIMSON;
		PREDATION_RADIUS = prpr;
		PREY_VIEW_DISTANCE = pyvd;
		PREDATOR_VIEW_DISTANCE = prvd;
		PREY_VIEW_ANGLE = pyva;
		PREDATOR_VIEW_ANGLE = prva;
	}

}
