package simulation.advanced;

import java.util.ArrayList;

public class DummyClient {

	private ArrayList<Prey> preyList;

	private ArrayList<Predator> predList;

	public DummyClient() {
		preyList = new ArrayList<Prey>();
		predList = new ArrayList<Predator>();
	}

	public ArrayList<Prey> getPreys() {
		return preyList;
	}

	public ArrayList<Predator> getPreds() {
		return predList;
	}

	public void addPrey(Prey p) {
		p.setCenterX(p.getCenterX() % Simulation.SIZE + (p.getCenterX() < 0 ? +Simulation.SIZE : 0));
		p.setCenterY(p.getCenterY() % Simulation.SIZE + (p.getCenterY() < 0 ? +Simulation.SIZE : 0));
		preyList.add(p);
	}

	public void addPred(Predator p) {
		p.setCenterX(p.getCenterX() % Simulation.SIZE + (p.getCenterX() < 0 ? +Simulation.SIZE : 0));
		p.setCenterY(p.getCenterY() % Simulation.SIZE + (p.getCenterY() < 0 ? +Simulation.SIZE : 0));
		predList.add(p);
	}

	public void clearAll() {
		preyList.clear();
		predList.clear();
	}

}