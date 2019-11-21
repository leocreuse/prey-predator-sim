package simulation.simpleServer;

public class Gradient {

	public static double fun(double pyhr, double pyrp, double pyrr, double pys, double prhr, double prrp, double prrr,
			double prs, double prpr) {
		Simulation sim = new Simulation(100, 80, new Cst(pyhr, pyrp, pyrr, pys, prhr, prrp, prrr, prs, prpr));
		return sim.eval();
	}

	public static double[] grad(double pyhr, double pyrp, double pyrr, double pys, double prhr, double prrp,
			double prrr, double prs, double prpr, double dx) {
		double[] res = new double[9];
		double r0 = fun(pyhr, pyrp, pyrr, pys, prhr, prrp, prrr, prs, prpr);
		res[0] = (r0 - fun(pyhr + dx * 0.02, pyrp, pyrr, pys, prhr, prrp, prrr, prs, prpr)) / (dx * 0.02);
		res[1] = (r0 - fun(pyhr, pyrp + dx * 0.02, pyrr, pys, prhr, prrp, prrr, prs, prpr)) / (dx * 0.02);
		res[2] = (r0 - fun(pyhr, pyrp, pyrr + dx * 0.1, pys, prhr, prrp, prrr, prs, prpr)) / (dx * 0.1);
		res[3] = (r0 - fun(pyhr, pyrp, pyrr, pys + dx, prhr, prrp, prrr, prs, prpr)) / dx;
		res[4] = (r0 - fun(pyhr, pyrp, pyrr, pys, prhr + dx * 0.02, prrp, prrr, prs, prpr)) / (dx * 0.02);
		res[5] = (r0 - fun(pyhr, pyrp, pyrr, pys, prhr, prrp + dx * 0.02, prrr, prs, prpr)) / (dx * 0.02);
		res[6] = (r0 - fun(pyhr, pyrp, pyrr, pys, prhr, prrp, prrr + dx * 0.1, prs, prpr)) / (dx * 0.1);
		res[7] = (r0 - fun(pyhr, pyrp, pyrr, pys, prhr, prrp, prrr, prs + dx, prpr)) / dx;
		res[8] = (r0 - fun(pyhr, pyrp, pyrr, pys, prhr, prrp, prrr, prs, prpr + dx * 0.1)) / (dx * 0.1);
		return res;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Cst cst = new Cst();
		double pyhr = cst.PREY_HUNGER_RATE;
		double pyrp = cst.PREY_REPRODUCTION_PROBABILITY;
		double pyrr = cst.PREY_REPRODUCTION_RADIUS;
		double pys = cst.PREDATOR_SPEED;
		double prhr = cst.PREDATOR_HUNGER_RATE;
		double prrp = cst.PREDATOR_REPRODUCTION_PROBABILITY;
		double prrr = cst.PREDATOR_REPRODUCTION_RADIUS;
		double prs = cst.PREDATOR_SPEED;
		double prpr = cst.PREDATION_RADIUS;
		double dx = 0.5;
		double epsilon = 10;
		double old = 0;
		double norm = 1;
		double current = fun(pyhr, pyrp, pyrr, pys, prhr, prrp, prrr, prs, prpr);
		// System.out.println(current);
		// System.out.println(grad(pyhr,pyrp,pyrr,pys,prhr,prrp,prrr,prs,prpr,1)[0]);
		int i = 0;
		double aplha = 0.05;
		while (i < 100 && (Math.abs(current - old) > epsilon || current > 40)) {
			double[] gradient = grad(pyhr, pyrp, pyrr, pys, prhr, prrp, prrr, prs, prpr, dx);
			old = current;
			norm = 0;
			for (double g : gradient) {
				norm += g * g;
			}
			norm = Math.sqrt(norm);
			System.out.println(gradient[0] + "; " + gradient[1] + "; " + gradient[2] + "; " + gradient[3] + "; "
					+ gradient[4] + "; " + gradient[5] + "; " + gradient[6] + "; " + gradient[7] + "; " + gradient[8]);
			System.out.println("pyhr,pyrp,pyrr,pys,prhr,prrp,prrr,prs,prpr");
			System.out.println(pyhr + ";" + pyrp + ";" + pyrr + ";" + pys + ";" + prhr + ";" + prrp + ";" + prrr + ";"
					+ prs + ";" + prpr);
			pyhr -= 0;// aplha*gradient[0]/norm;
			pyrp -= aplha * gradient[1] / norm;
			pyrr -= aplha * gradient[2] / norm;
			pys -= aplha * gradient[3] / norm;
			prhr -= aplha * gradient[4] / norm;
			prrp -= aplha * gradient[5] / norm;
			prrr -= aplha * gradient[6] / norm;
			prs -= aplha * gradient[7] / norm;
			prpr -= aplha * gradient[8] / norm;
			current = fun(pyhr, pyrp, pyrr, pys, prhr, prrp, prrr, prs, prpr);
			System.out.println(i + ": " + current);
			i++;
		}
		System.out.println("pyhr,pyrp,pyrr,pys,prhr,prrp,prrr,prs,prpr");
		System.out.println(pyhr + ";" + pyrp + ";" + pyrr + ";" + pys + ";" + prhr + ";" + prrp + ";" + prrr + ";" + prs
				+ ";" + prpr);
		System.out.println(current);
	}

}
