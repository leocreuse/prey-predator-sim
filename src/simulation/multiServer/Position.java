package simulation.multiServer;

public class Position {

	private int x;

	private int y;

	public Position(int x, int y) {
	}

	public Position(Position original) {
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void move(Movement dep) {
	}

	public static double distance(Position p1, Position p2) {
		return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.x - p2.x));
	}

}