package dhbwka2015.labwbsys.perceptron;

import java.util.ArrayList;
import java.util.Random;

public class ClassicLearner implements PerceptronLearnerIf {

	@Override
	public boolean learnSolvable(Perceptron learnObject,
			ArrayList<LearnInstance> samples) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean learnUnknown(Perceptron learnObject,
			ArrayList<LearnInstance> samples) {
		Random rnd = new Random();

		for (int i = 0; i < learnObject.getInputCount() + 1; ++i) {
			learnObject.setWeight(i, rnd.nextDouble() * 0.1);
		}

		int MAX = 1000;
		while (!testAll(learnObject, samples) && MAX > 0) {
			--MAX;

			LearnInstance li = samples.get(rnd.nextInt(samples.size()));

			double res = learnObject.calcStepResult(li.data);

			if (li.c && res < 0.0) {
				learnObject.updateAddWeightVector(li.data);
			} else if (!li.c && res > 0.0) {
				learnObject.updateSubWeightVector(li.data);
			}
		}

		return MAX > 0;
	}

	public boolean testAll(Perceptron learnObject,
			ArrayList<LearnInstance> samples) {
		boolean res = true;

		for (LearnInstance li : samples) {
			if (li.c && learnObject.calcStepResult(li.data) < 0.0) {
				res = false;
				break;
			} else if (!li.c && learnObject.calcStepResult(li.data) > 0.0) {
				res = false;
				break;
			}
		}

		return res;
	}
}
