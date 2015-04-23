package dhbwka2015.labwbsys.perceptron;

import java.util.ArrayList;

public interface PerceptronLearnerIf {
	public boolean learnSolvable(Perceptron learnObject, ArrayList<LearnInstance> samples);
	public boolean learnUnknown(Perceptron learnObject, ArrayList<LearnInstance> samples);

}
