package dhbwka2015.labwbsys.imgfilters;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

/**
 * Created by argannor on 07.04.15.
 */
public class DilateFilter implements ImageFilterIf {

    private int width;
    private int height;
    private ColorModel inputModel;
    private WritableRaster inputRaster;

    @Override
    public void filterImages(BufferedImage in, BufferedImage out, ArrayList<String> parameters) {
        inputRaster = in.getRaster();
        WritableRaster outRaster = out.getRaster();

        inputModel = in.getColorModel();
        ColorModel outModel = out.getColorModel();

        width = in.getWidth();
        height = in.getHeight();

        boolean[][] result = new boolean[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                result[i][j] = false;
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (dilate(i, j)) {
                    result[i - 1][j] = true;
                    result[i + 1][j] = true;
                    result[i][j - 1] = true;
                    result[i][j + 1] = true;
                }
            }
        }

        process(outModel, outRaster, result);
    }

    /**
     * Processes the result and therefore generates the output image
     * @param outModel
     * @param outRaster
     * @param result
     */
    private void process(ColorModel outModel, WritableRaster outRaster, boolean[][] result) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                outRaster.setDataElements(i, j, outModel.getDataElements(calcColor(result[i][j]), null));
            }
        }
    }

    /**
     * Calculate the output color for a given boolean value
     * @param b
     * @return
     */
    private int calcColor(boolean b) {
        if(!b)
            return 0xffffffff;
        return 0xff000000;
    }

    /**
     * Calculates whether the surrounding pixels should be set to true
     * @param i component of the pixels position
     * @param j component of the pixels position
     * @return result of dilation
     */
    private boolean dilate(int i, int j) {
        if(i < 1 || j < 1 || i >= width - 1 || j >= height - 1) {
            return false;
        }
        return isTrue(i,j);
    }

    /**
     * Calculates the binary value of a given pixel based on it's brightness
     * @param i component of the pixels position
     * @param j component of the pixels position
     * @return binary value
     */
    private boolean isTrue(int i, int j) {
        return calcBrightnessValue(inputRaster.getDataElements(i, j, null)) < 127;
    }

    /**
     * Calculates the Brightness of a given pixel
     * @param pixel
     * @return brightness
     */
    int calcBrightnessValue(Object pixel) {
        int rgb = inputModel.getRGB(pixel);

        int r = (rgb & 0x00ff0000) >> 16;
        int g = (rgb & 0x0000ff00) >> 8;
        int b = (rgb & 0x000000ff);

        return (r+g+b) / 3;
    }
}
