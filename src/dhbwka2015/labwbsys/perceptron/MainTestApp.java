package dhbwka2015.labwbsys.perceptron;

import java.util.ArrayList;

public class MainTestApp {

	// Hardcoded perceptron similar to example on slides.
	private static void testHardcodedPerceptron() {
		Perceptron p = new Perceptron(2, null);
		p.setThreshold(-.1);
		p.setWeight(1, 0.5);
		p.setWeight(2, 0.5);
		System.out.println(p);

		double in[] = new double[2];

		in[0] = -1.0;
		in[1] = -1.0;
		System.out.println("0 || 0  => " + p.calculateRawResult(in) + "  ==>  "
				+ p.calcStepResult(in));

		in[0] = 1.0;
		in[1] = -1.0;
		System.out.println("0 || 1  => " + p.calculateRawResult(in) + "  ==>  "
				+ p.calcStepResult(in));

		in[0] = -1.0;
		in[1] = 1.0;
		System.out.println("1 || 0  => " + p.calculateRawResult(in) + "  ==>  "
				+ p.calcStepResult(in));

		in[0] = 1.0;
		in[1] = 1.0;
		System.out.println("1 || 1  => " + p.calculateRawResult(in) + "  ==>  "
				+ p.calcStepResult(in));
	}

	private static void testPerceptronLearning() {

		System.out.println(" Test complete learning algorithm");

		Perceptron p = new Perceptron(2, new RandomWeightSetter());
		p.name = "OR-Operator";
		System.out.println(p);
		ArrayList<LearnInstance> samples = new ArrayList<LearnInstance>();

		samples.add(new LearnInstance(false, new double[] { 0.0, 0.0 }));
		samples.add(new LearnInstance(true, new double[] { 0.0, 1.0 }));
		samples.add(new LearnInstance(true, new double[] { 1.0, 0.0 }));
		samples.add(new LearnInstance(true, new double[] { 1.0, 1.0 }));

		p.learn(samples);

		System.out.println("Final Perceptron:");
		System.out.println(p);

		double in[] = new double[2];

		in[0] = 0;
		in[1] = 0;
		System.out.println("0 || 0  => " + p.calculateRawResult(in) + "  ==>  "
				+ p.calcStepResult(in));

		in[0] = 1.0;
		in[1] = 0;
		System.out.println("0 || 1  => " + p.calculateRawResult(in) + "  ==>  "
				+ p.calcStepResult(in));

		in[0] = 0;
		in[1] = 1.0;
		System.out.println("1 || 0  => " + p.calculateRawResult(in) + "  ==>  "
				+ p.calcStepResult(in));

		in[0] = 1.0;
		in[1] = 1.0;
		System.out.println("1 || 1  => " + p.calculateRawResult(in) + "  ==>  "
				+ p.calcStepResult(in));
	}

    private static void testAndPerceptron() {
        Perceptron p = new Perceptron(2, null);
        p.name = "AND-Perceptron";
        p.setThreshold(0.8f);
        p.setWeight(1,0.5f);
        p.setWeight(2,0.5f);

        System.out.println("AND Perceptron:");
        System.out.println(p);

        double in[] = new double[2];

        in[0] = -1;
        in[1] = -1;
        System.out.println("0 & 0  => " + p.calculateRawResult(in) + "  ==>  "
                + p.calcStepResult(in));

        in[0] = 1.0;
        in[1] = -1;
        System.out.println("0 & 1  => " + p.calculateRawResult(in) + "  ==>  "
                + p.calcStepResult(in));

        in[0] = -1;
        in[1] = 1.0;
        System.out.println("1 & 0  => " + p.calculateRawResult(in) + "  ==>  "
                + p.calcStepResult(in));

        in[0] = 1.0;
        in[1] = 1.0;
        System.out.println("1 & 1  => " + p.calculateRawResult(in) + "  ==>  "
                + p.calcStepResult(in));

    }

    public static void testLearnedAndPerceptron() {
        System.out.println("Test complete learning algorithm");

        Perceptron p = new Perceptron(2, new PerceptronLearner());
        p.name = "AND-Operator";
        System.out.println(p);
        ArrayList<LearnInstance> samples = new ArrayList<LearnInstance>();

        samples.add(new LearnInstance(false, new double[] { 0, 0 }));
        samples.add(new LearnInstance(false, new double[] { 0 , 1.0 }));
        samples.add(new LearnInstance(false, new double[] { 1.0, 0 }));
        samples.add(new LearnInstance(true, new double[] { 1.0, 1.0 }));

        p.learn(samples);

        System.out.println("AND Perceptron:");
        System.out.println(p);

        double in[] = new double[2];

        in[0] = -1;
        in[1] = -1;
        System.out.println("0 & 0  => " + p.calculateRawResult(in) + "  ==>  "
                + p.calcStepResult(in));

        in[0] = 1.0;
        in[1] = 0;
        System.out.println("0 & 1  => " + p.calculateRawResult(in) + "  ==>  "
                + p.calcStepResult(in));

        in[0] = -1;
        in[1] = 1.0;
        System.out.println("1 & 0  => " + p.calculateRawResult(in) + "  ==>  "
                + p.calcStepResult(in));

        in[0] = 1.0;
        in[1] = 1.0;
        System.out.println("1 & 1  => " + p.calculateRawResult(in) + "  ==>  "
                + p.calcStepResult(in));
    }

    public static void testXORPerceptron() {
        Perceptron p1 = new Perceptron(2, null);
        p1.name = "only x1";
        // 1 x1 - 0,5 x2 > 0.55 => 11 = 0.5, -11 = -1.5, 1-1=1.5, -1-1=-0.5
        p1.setWeight(1, 1);
        p1.setWeight(2, -0.5);
        p1.setThreshold(0.55);

        Perceptron p2 = new Perceptron(2, null);
        p2.name = "only x2";
        // -0,5 x1 + 1 x2 > 0.55
        p1.setWeight(1, -0.5);
        p1.setWeight(2, 1);
        p1.setThreshold(0.55);

        Perceptron p3 = new Perceptron(2, null);
        p3.name = "or";
        p3.setThreshold(-.1);
        p3.setWeight(1, 0.5);
        p3.setWeight(2, 0.5);

        System.out.println("XOR Net");

        double[] in = new double[2];
        double[] in2 = new double[2];

        in[0] = -1; in[1] = -1;
        in2[0] = p1.calcStepResult(in);
        in2[1] = p2.calcStepResult(in);
        System.out.println("-1 XOR -1  => " + p3.calculateRawResult(in2) + "  ==>  "
                + p3.calcStepResult(in2));

        in[0] = -1; in[1] = 1;
        in2[0] = p1.calcStepResult(in);
        in2[1] = p2.calcStepResult(in);
        System.out.println("-1 XOR 1  => " + p3.calculateRawResult(in2) + "  ==>  "
                + p3.calcStepResult(in2));

        in[0] = 1; in[1] = -1;
        in2[0] = p1.calcStepResult(in);
        in2[1] = p2.calcStepResult(in);
        System.out.println("1 XOR -1  => " + p3.calculateRawResult(in2) + "  ==>  "
                + p3.calcStepResult(in));

        in[0] = 1; in[1] = 1;
        in2[0] = p1.calcStepResult(in);
        in2[1] = p2.calcStepResult(in);
        System.out.println("1 XOR 1  => " + p3.calculateRawResult(in2) + "  ==>  "
                + p3.calcStepResult(in2));

    }

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testHardcodedPerceptron();

		testPerceptronLearning();

        testAndPerceptron();

        testXORPerceptron();

        testLearnedAndPerceptron();
	}
}
