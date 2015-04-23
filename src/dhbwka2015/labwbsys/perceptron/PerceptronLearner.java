package dhbwka2015.labwbsys.perceptron;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by argannor on 16.04.15.
 */
public class PerceptronLearner implements PerceptronLearnerIf {

    private Random rn = new Random();
    private int t = 0;

    public boolean learn(Perceptron learnObject, ArrayList<LearnInstance> samples) {
        
        ArrayList<LearnInstance> instances = new ArrayList<LearnInstance>(samples);
        int limit = instances.size() * learnObject.getInputCount() * 2000;
        int count = 0;

        t = 0;
        for(int i = 0; i <= learnObject.getInputCount(); i++) {
            learnObject.setWeight(i, rn.nextDouble()*0.1);
        }
        
        while(count < limit) {
            count++;

            int index = rn.nextInt(instances.size());
            LearnInstance x = instances.get(index);
            if(x.c && learnObject.calculateRawResult(x.data) > 0)
                continue;
            if(x.c && learnObject.calculateRawResult(x.data) <= 0) {
                add(learnObject, x);
                continue;
            }
            if(!x.c && learnObject.calculateRawResult(x.data) < 0)
                continue;
            if(!x.c && learnObject.calculateRawResult(x.data) >= 0) {
                subtract(learnObject, x);
                continue;
            }
        }
        return true;
    }

    private void subtract(Perceptron learnObject, LearnInstance x) {
        learnObject.setThreshold(-learnObject.getThreshold() + 1);
        for(int i = 0; i < learnObject.getInputCount(); i++) {
            learnObject.setWeight(i+1, learnObject.getWeight(i+1) - x.data[i]);
            t++;
        }
    }

    private void add(Perceptron learnObject, LearnInstance x) {
        learnObject.setThreshold(-learnObject.getThreshold() - 1);
        for(int i = 0; i < learnObject.getInputCount(); i++) {
            learnObject.setWeight(i+1, learnObject.getWeight(i+1) + x.data[i]);
            t++;
        }
    }


    @Override
    public boolean learnSolvable(Perceptron learnObject, ArrayList<LearnInstance> samples) {
        return false;
    }

    @Override
    public boolean learnUnknown(Perceptron learnObject, ArrayList<LearnInstance> samples) {
        return false;
    }
}
