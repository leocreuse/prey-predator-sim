package simulation.simpleServer;

import java.util.ArrayList;

public class VariousTestMain {

	public static void main(String[] args) {
		Simulation sim = new Simulation(0, 0, new Cst());
		System.out.println(sim.getElements());
		sim.update();
		sim.update();
		System.out.println(sim.getElements().get(0));
		System.out.println(sim.getElements().get(1));
		System.out.println(sim.getElements().size());

	}

}
