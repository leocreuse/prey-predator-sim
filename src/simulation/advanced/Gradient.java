package simulation.advanced;
/**
 * Experimental 13 degree of freedom gradient based optimization function for all the parameters.
 * Not working reliably for the moment due to non-deterministic function being minimized.
 * Since evaluating fun takes a long time, and the gradients takes 14 times longer to evaluate, the overall process is rather slow.
 * 
 * A different approach for finding optimal settings could have been selecting random settings, and trying with luck
 * @author leo
 *
 */
public class Gradient {
	/**
	 * function to minimize. 
	 * Parameters are the simulation constants.
	 */
	public static double fun(double pyhr, double pyrp, double pyrr, double pys, double prhr, double prrp, double prrr,
			double prs, double prpr, double pyvd, double prvd, double pyva, double prva) {
		Simulation sim = new Simulation(100, 80, new Cst(pyhr, pyrp, pyrr, pys, prhr, prrp, prrr, prs, prpr,pyvd,prvd,pyva,prva));
		return sim.eval();
	}
	/**
	 * gradient approximation of fun, same parameters, with dx the differential step.
	 * 
	 */
	public static double[] grad(double pyhr, double pyrp, double pyrr, double pys, double prhr, double prrp,
			double prrr, double prs, double prpr,double pyvd, double prvd,double pyva, double prva ,double dx) {
		double[] res = new double[13];
		double r0 = fun(pyhr, pyrp, pyrr, pys, prhr, prrp, prrr, prs, prpr,pyvd,prvd,pyva,prva);
		res[0] = (r0 - fun(pyhr + dx * 0.02, pyrp, pyrr, pys, prhr, prrp, prrr, prs, prpr,pyvd,prvd,pyva,prva)) / (dx * 0.02);
		res[1] = (r0 - fun(pyhr, pyrp + dx * 0.02, pyrr, pys, prhr, prrp, prrr, prs, prpr,pyvd,prvd,pyva,prva)) / (dx * 0.02);
		res[2] = (r0 - fun(pyhr, pyrp, pyrr + dx * 0.1, pys, prhr, prrp, prrr, prs, prpr,pyvd,prvd,pyva,prva)) / (dx * 0.1);
		res[3] = (r0 - fun(pyhr, pyrp, pyrr, pys + dx, prhr, prrp, prrr, prs, prpr,pyvd,prvd,pyva,prva)) / dx;
		res[4] = (r0 - fun(pyhr, pyrp, pyrr, pys, prhr + dx * 0.02, prrp, prrr, prs, prpr,pyvd,prvd,pyva,prva)) / (dx * 0.02);
		res[5] = (r0 - fun(pyhr, pyrp, pyrr, pys, prhr, prrp + dx * 0.02, prrr, prs, prpr,pyvd,prvd,pyva,prva)) / (dx * 0.02);
		res[6] = (r0 - fun(pyhr, pyrp, pyrr, pys, prhr, prrp, prrr + dx * 0.1, prs, prpr,pyvd,prvd,pyva,prva)) / (dx * 0.1);
		res[7] = (r0 - fun(pyhr, pyrp, pyrr, pys, prhr, prrp, prrr, prs + dx, prpr,pyvd,prvd,pyva,prva)) / dx;
		res[8] = (r0 - fun(pyhr, pyrp, pyrr, pys, prhr, prrp, prrr, prs, prpr + dx * 0.1,pyvd,prvd,pyva,prva)) / (dx * 0.1);
		res[9] = (r0 - fun(pyhr, pyrp, pyrr, pys, prhr, prrp, prrr, prs + dx, prpr,pyvd+dx,prvd,pyva,prva)) / dx;
		res[10] = (r0 - fun(pyhr, pyrp, pyrr, pys, prhr, prrp, prrr, prs + dx, prpr,pyvd,prvd+dx,pyva,prva)) / dx;
		res[11] = (r0 - fun(pyhr, pyrp, pyrr, pys, prhr, prrp, prrr, prs + dx, prpr,pyvd,prvd,pyva+dx,prva)) / dx;
		res[12] = (r0 - fun(pyhr, pyrp, pyrr, pys, prhr, prrp, prrr, prs + dx, prpr,pyvd,prvd,pyva,prva+dx)) / dx;
		return res;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Cst cst = new Cst();
		//Start with default (most reliable yet) settings.
		double pyhr = cst.PREY_HUNGER_RATE;
		double pyrp = cst.PREY_REPRODUCTION_PROBABILITY;
		double pyrr = cst.PREY_REPRODUCTION_RADIUS;
		double pys = cst.PREDATOR_SPEED;
		double prhr = cst.PREDATOR_HUNGER_RATE;
		double prrp = cst.PREDATOR_REPRODUCTION_PROBABILITY;
		double prrr = cst.PREDATOR_REPRODUCTION_RADIUS;
		double prs = cst.PREDATOR_SPEED;
		double prpr = cst.PREDATION_RADIUS;
		double pyvd = cst.PREY_VIEW_DISTANCE;
		double prvd = cst.PREDATOR_VIEW_DISTANCE;
		double pyva = cst.PREY_VIEW_ANGLE;
		double prva = cst.PREDATOR_VIEW_ANGLE;
		//some constants used for the optimization.
		double dx = 0.5;
		double epsilon = 10;
		double old = 0;
		double norm = 1;
		double current = fun(pyhr, pyrp, pyrr, pys, prhr, prrp, prrr, prs, prpr,pyvd,prvd,pyva,prva);
		// System.out.println(current);
		// System.out.println(grad(pyhr,pyrp,pyrr,pys,prhr,prrp,prrr,prs,prpr,1)[0]);
		int i = 0;
		double aplha = 0.05;
		while (i < 100 && (Math.abs(current - old) > epsilon || current > 40)) {
			double[] gradient = grad(pyhr, pyrp, pyrr, pys, prhr, prrp, prrr, prs, prpr,pyvd,prvd,pyva,prva, dx);
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
			pyvd -= aplha * gradient[9] / norm;
			prvd -= aplha * gradient[10] / norm;
			pyva -= aplha * gradient[11] / norm;
			prva -= aplha * gradient[12] / norm;
			current = fun(pyhr, pyrp, pyrr, pys, prhr, prrp, prrr, prs, prpr,pyvd,prvd,pyva,prva);
			System.out.println(i + ": " + current);
			i++;
		}
		System.out.println("pyhr,pyrp,pyrr,pys,prhr,prrp,prrr,prs,prpr");
		System.out.println(pyhr + ";" + pyrp + ";" + pyrr + ";" + pys + ";" + prhr + ";" + prrp + ";" + prrr + ";" + prs
				+ ";" + prpr);
		System.out.println(current);
	}

}
