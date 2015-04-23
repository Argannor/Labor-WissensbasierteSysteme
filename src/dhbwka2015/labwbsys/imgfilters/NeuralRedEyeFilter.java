package dhbwka2015.labwbsys.imgfilters;

import dhbwka2015.labwbsys.perceptron.ClassicLearner;
import dhbwka2015.labwbsys.perceptron.LearnInstance;
import dhbwka2015.labwbsys.perceptron.Perceptron;
import dhbwka2015.labwbsys.perceptron.PerceptronLearner;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by argannor on 23.04.15.
 *
 */
public class NeuralRedEyeFilter implements ImageFilterIf {

    private final static int PROPERTY_COUNT = 3;
    private BufferedImage in;
    private BufferedImage out;

    @Override
    public void filterImages(BufferedImage in, BufferedImage out, ArrayList<String> parameters) {
        this.in = in;
        this.out = out;

        // Read CSV
        CsvCommentedReader reader = new CsvCommentedReader(parameters.get(2));
        List<ClassifiedPixel> pointList;
        if (reader.readFile()) {
            System.out.println(reader.toString());

            pointList = readPoints(reader.getFileContent());
        } else {
            System.out.println("Points in CSV could not be read!");
            return;
        }

        Perceptron perceptron = createPerceptron(pointList);
        List<ClassifiedPixel> pixels = selectPixelsWith(perceptron);

        copyOldImage();
        recolorSelected(pixels);
    }

    private void copyOldImage() {
        for (int i = 0; i < in.getWidth(); ++i) {
            for (int j = 0; j < in.getHeight(); ++j) {
                out.setRGB(i, j, in.getRGB(i, j));
            }
        }
    }

    private void recolorSelected(List<ClassifiedPixel> pixels) {
        int x,y,rgb;
        for (ClassifiedPixel pixel : pixels) {
            x = pixel.xpos;
            y = pixel.ypos;
            out.setRGB(x, y, 0xff00ffff);
        }
    }

    private List<ClassifiedPixel> selectPixelsWith(Perceptron perceptron) {
        List<ClassifiedPixel> selectedPixels = new ArrayList<ClassifiedPixel>();

        double[] properties;
        for (int i = 0; i < in.getWidth(); ++i) {
            for (int j = 0; j < in.getHeight(); ++j) {
                ClassifiedPixel pixel = new ClassifiedPixel(i, j, false);

                properties = getPropertiesOf(pixel);
                double result = perceptron.calcStepResult(properties);

                if(result == 1) {
                    selectedPixels.add(pixel);
                }

            }
        }

        return selectedPixels;
    }

    private Perceptron createPerceptron(List<ClassifiedPixel> pointList) {
        Perceptron p = new Perceptron(PROPERTY_COUNT, new ClassicLearner());

        List<LearnInstance> samples = new ArrayList<LearnInstance>();

        int rgb = 0;
        for (ClassifiedPixel point : pointList) {

            double[] properties = getPropertiesOf(point);

            LearnInstance learnInstance = new LearnInstance(PROPERTY_COUNT);

            learnInstance.c = point.classification;
            learnInstance.setData(properties);

            samples.add(learnInstance);
        }
        p.learn((ArrayList<LearnInstance>) samples);
        return p;
    }

    private double[] getPropertiesOf(ClassifiedPixel point) {
        int rgb;
        rgb = in.getRGB(point.xpos, point.ypos);
        double[] properties = new double[PROPERTY_COUNT];
        properties[0] = ColorHelper.getHue(rgb);
        properties[1] = ColorHelper.getSaturation(rgb);
        properties[2] = ColorHelper.getBrightness(rgb);
        return properties;
    }

    private List<ClassifiedPixel> readPoints(ArrayList<String[]> data) {
        List<ClassifiedPixel> points = new ArrayList<ClassifiedPixel>();

        for (String[] sa : data) {
            if (sa.length != 3) {
                continue;
            }


            try {
                boolean classification = Boolean.parseBoolean(sa[0].trim());
                int xpos = Integer.parseInt(sa[1].trim());
                int ypos = Integer.parseInt(sa[2].trim());

                ClassifiedPixel point = new ClassifiedPixel(xpos, ypos, classification);
                points.add(point);
            } catch (Exception ex) {
                continue;
            }
        }

        return points;
    }

    private class ClassifiedPixel {
        public int xpos;
        public int ypos;
        public boolean classification;

        public ClassifiedPixel(int xpos, int ypos, boolean classification) {
            this.xpos = xpos;
            this.ypos = ypos;
            this.classification = classification;

        }

    }

}
