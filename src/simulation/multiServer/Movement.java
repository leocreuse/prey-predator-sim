package simulation.multiServer;

public class Movement {

	private double speed;

	private double direction;

	public double getDirection() {
		return direction;
	}

	public Movement(double speed, double direction) {
		this.speed = speed;
		this.direction = direction;
	}

	public double getSpeed() {
		return speed;
	}

	public void setMovement(double speed, double direction) {
		this.speed = speed;
		this.direction = direction;
	}

	public Movement(Movement original) {
		this.speed = original.speed;
		this.direction = original.direction;
	}

}