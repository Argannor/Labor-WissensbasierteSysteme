package dhbwka2015.labwbsys.imgfilters;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

/**
 * Created by argannor on 07.04.15.
 */
public class ErodeFilter implements ImageFilterIf {

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
                result[i][j] = erode(i,j);
            }
        }

        process(outModel, outRaster, result);
    }

    private void process(ColorModel outModel, WritableRaster outRaster, boolean[][] result) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                outRaster.setDataElements(i, j, outModel.getDataElements(calcColor(result[i][j]), null));
            }
        }
    }

    private int calcColor(boolean b) {
        if(b)
            return 0xffffffff;
        return 0xff000000;
    }

    private boolean erode(int i, int j) {
        if(i < 1 || j < 1 || i >= width - 1 || j >= height - 1) {
            return false;
        }
        return isTrue(i, j) && isTrue(i - 1, j) && isTrue(i+1, j) && isTrue(i, j-1) && isTrue(i, j+1);
    }

    private boolean isTrue(int i, int j) {
        return calcBrightnessValue(inputRaster.getDataElements(i, j, null)) > 127;
    }

    /**
     * Calculates the Brightness of a given pixel
     * @param pix
     * @return
     */
    int calcBrightnessValue(Object pix) {
        int rgb = inputModel.getRGB(pix);

        int r = (rgb & 0x00ff0000) >> 16;
        int g = (rgb & 0x0000ff00) >> 8;
        int b = (rgb & 0x000000ff);

        return (r+g+b) / 3;
    }
}
