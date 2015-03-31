package dhbwka2015.labwbsys.imgfilters;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

/**
 * Created by Soeren Berken-Mersmann on 31.03.15.
 */
public class ResizeFilter implements ImageFilterIf {
    @Override
    public void filterImages(BufferedImage in, BufferedImage out, ArrayList<String> parameters) {
        WritableRaster inRaster = in.getRaster();
        WritableRaster outRaster = out.getRaster();

        ColorModel model = in.getColorModel();
        ColorModel outmodel = out.getColorModel();


        int width = in.getWidth();
        int height = in.getHeight();

        int rgb;

        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {

                if (i+1 >= width || j+1 >= height) {
                    rgb = 0;
                } else {
                    Object inPixTL = inRaster.getDataElements(i, j, null);
                    Object inPixTR = inRaster.getDataElements(i+1, j, null);
                    Object inPixBL = inRaster.getDataElements(i, j+1, null);
                    Object inPixBR = inRaster.getDataElements(i+1, j+1, null);

                    int rgbTL = model.getRGB(inPixTL);
                    int rgbTR = model.getRGB(inPixTR);
                    int rgbBL = model.getRGB(inPixBL);
                    int rgbBR = model.getRGB(inPixBR);

                    int r = ((rgbTL & 0x00ff0000) >> 16) + ((rgbTR & 0x00ff0000) >> 16) + ((rgbBL & 0x00ff0000) >> 16) + ((rgbBR & 0x00ff0000) >> 16);
                    r = ((r / 4) << 16) & 0x00ff0000;
                    int g = ((rgbTL & 0x0000ff00) >> 8) + ((rgbTR & 0x0000ff00) >> 8) + ((rgbBL & 0x0000ff00) >> 8) + ((rgbBR & 0x0000ff00) >> 8);
                    g = ((g / 4) << 8) & 0x0000ff00;
                    int b = (rgbTL & 0x000000ff) + (rgbTR & 0x000000ff) + (rgbBL & 0x000000ff) + (rgbBR & 0x000000ff);
                    b = (b / 4) & 0x000000ff;
                    int a = ((rgbTL & 0xff000000) >> 24) + ((rgbTR & 0xff000000) >> 24) + ((rgbBL & 0xff000000) >> 24) + ((rgbBR & 0xff000000) >> 24);
                    a = ((a / 4) << 24) & 0xff000000;

                    rgb = a | r | g | b;
                }



                outRaster.setDataElements(i/2, j/2, outmodel.getDataElements(rgb, null));

            }
        }
    }
}
