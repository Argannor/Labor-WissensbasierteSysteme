package dhbwka2015.labwbsys.imgfilters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.*;
import java.util.List;

/**
 * Created by Soeren Berken-Mersmann on 31.03.15.
 */
public class RedEyeFilter implements ImageFilterIf {

    // Empfohlene Parameter für Beispielbild: Threshold [Param0] = 0.25, X [Param1] = 144, Y [Param2] = 310

    private WritableRaster inRaster;
    private WritableRaster outRaster;
    private List<Pixel> segment = new ArrayList<Pixel>();
    private float distanceThreshold;
    private ColorModel inputModel;
    private ColorModel outputModel;
    private int width;
    private int height;
    private Object[][] pixels;

    /**
     * Queue of possible segment candidates.
     */
    private Queue<Pixel> selected = new ArrayDeque<Pixel>();
    private float[] hsvOrigin;


    @Override
    public void filterImages(BufferedImage in, BufferedImage out, ArrayList<String> parameters) {
        inRaster = in.getRaster();
        outRaster = out.getRaster();

        inputModel = in.getColorModel();
        outputModel = out.getColorModel();

        width = in.getWidth();
        height = in.getHeight();

        distanceThreshold = Float.valueOf(parameters.get(0));

        int x = Integer.valueOf(parameters.get(1));
        int y = Integer.valueOf(parameters.get(2));

        // Create an array of all pixels, so we only have to visit each pixel once.
        // Fills the output with the original
        int rgb;
        pixels = new Object[width][height];

        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                pixels[i][j] = inRaster.getDataElements(i, j, null);
                rgb = inputModel.getRGB(pixels[i][j]);
                outRaster.setDataElements(i, j, outputModel.getDataElements(rgb, null));
            }
        }

        // Select seed and calculate hsv, add it to the selection and start growing
        Pixel startPix = new Pixel(pixels[x][y],x,y);
        hsvOrigin = calcHsv(startPix.pixel);

        selected.add(startPix);
        Pixel pix;
        while(!selected.isEmpty()) {
            pix = selected.poll();
            grow(pix.i,pix.j);
        }

        // recolor all pixels within the segment
        recolor();
    }

    /**
     * Adds the pixel to the segment and selects the 4 neighbors, iff it lies within the hue-saturation-distance threshold.
     * @param i
     * @param j
     */
    void grow(int i, int j) {
        if (i < 1 || j < 1 || i >= width - 1 || j >= height - 1 || pixels[i][j] == null) {
            return;
        }
        Object inPix = pixels[i][j];
        pixels[i][j] = null;
        float[] hsv = calcHsv(inPix);
        selected.remove(new Pixel(inPix, i, j));

        // if the hue-saturation distance to the seed's hue and saturation doesn't exceed the threshold add the pixel and continue growing
        if (calcDistance(hsv, hsvOrigin) < distanceThreshold) {
            add(new Pixel(inPix,i,j));
            if(pixels[i-1][j] != null) {
                selected.add(new Pixel(inPix, i - 1, j));
            }
            if(pixels[i+1][j] != null) {
                selected.add(new Pixel(inPix, i + 1, j));
            }
            if(pixels[i][j-1] != null) {
                selected.add(new Pixel(inPix, i, j - 1));
            }
            if(pixels[i][j+1] != null) {
                selected.add(new Pixel(inPix, i, j + 1));
            }
        }

    }

    /**
     * Calculates the hue-saturation distance between two hsv colors
     * @param hsv1
     * @param hsv2
     * @return distance
     */
    private float calcDistance(float [] hsv1, float [] hsv2) {
        float a = hsv1[0] - hsv2[0];
        float b = hsv1[1] - hsv2[1];
        return (float) Math.sqrt(a*a+b*b);
    }

    /**
     * Calculates the hsv color of a pixel
     * @param inPix
     * @return
     */
    private float[] calcHsv(Object inPix) {
        float[] hsv = new float[3];
        int r = inputModel.getRed(inPix);
        int g = inputModel.getGreen(inPix);
        int b = inputModel.getBlue(inPix);
        Color.RGBtoHSB(r, g, b, hsv);
        return hsv;
    }

    /**
     * Recolors all selected pixels.
     * Uses calcGrayScale
     */
    void recolor() {
        for(Pixel pix : segment) {
            outRaster.setDataElements(pix.i, pix.j, outputModel.getDataElements(calcGrayScale(pix), null));
        }
    }

    /**
     * Calculates the gray scale of a given pixel
     * @param pix
     * @return
     */
    int calcGrayScale(Pixel pix) {
        int rgb = inputModel.getRGB(pix.pixel);

        int a = rgb & 0xff000000;
        int r = (rgb & 0x00ff0000) >> 16;
        int g = (rgb & 0x0000ff00) >> 8;
        int b = (rgb & 0x000000ff);

        int val = (r+g+b) / 3;
        return a | val << 16 | val << 8 | val;
    }

    /**
     * Adds the pixel to the segment
     * @param pixel
     */
    void add(Pixel pixel) {
        segment.add(pixel);
    }
}
