package dhbwka2015.labwbsys.imgfilters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soeren Berken-Mersmann on 31.03.15.
 */
public class RedEyeFilter implements ImageFilterIf {

    private WritableRaster inRaster;
    private WritableRaster outRaster;
    private List<Pixel> segment = new ArrayList<Pixel>();
    private List<Pixel> visited = new ArrayList<Pixel>();
    private float hueThreshold;
    private int nThreshold;
    private ColorModel model;
    private ColorModel outmodel;
    private int width;
    private int height;

    @Override
    public void filterImages(BufferedImage in, BufferedImage out, ArrayList<String> parameters) {
        inRaster = in.getRaster();
        outRaster = out.getRaster();

        model = in.getColorModel();
        outmodel = out.getColorModel();

        width = in.getWidth();
        height = in.getHeight();

        hueThreshold = Float.valueOf(parameters.get(0));
        nThreshold = Integer.valueOf(parameters.get(1));

        int rgb;

        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                segment.clear();
                visited.clear();
                grow(i, j);
                if (segment.size() > nThreshold) {
                    recolor();
                }

            }
        }
    }

    void grow(int i, int j) {
        if (i < 0 || j < 0 || i >= width || j >= height) {
            return;
        }
        Object inPix = inRaster.getDataElements(i, j, null);
        Pixel pix = new Pixel(inPix, i, j);
        if(visited.contains(pix)) {
            return;
        }
        visited.add(pix);
        float[] hsv = new float[3];
        int r = model.getRed(inPix);
        int g = model.getGreen(inPix);
        int b = model.getBlue(inPix);
        Color.RGBtoHSB(r, g, b, hsv);
        if(hsv[0] < hueThreshold && hsv[0] > -hueThreshold && hsv[1] > 0.5) {
            add(pix);
            grow(i-1,j);
            grow(i+1,j);
            grow(i,j-1);
            grow(i,j+1);
        }

    }

    void recolor() {
        int rgb = 0xffffffff;
        for(Pixel pix : segment) {
            outRaster.setDataElements(pix.i, pix.j, outmodel.getDataElements(rgb, null));
        }
    }

    void add(Pixel pixel) {
        segment.add(pixel);
    }
}
