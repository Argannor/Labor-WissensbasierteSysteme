package dhbwka2015.labwbsys.imgfilters;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

/**
 * Created by Soeren Berken-Mersmann on 31.03.15.
 */
public class FrameFilter implements ImageFilterIf {

    @Override
    public void filterImages(BufferedImage in, BufferedImage out, ArrayList<String> parameters) {
        WritableRaster inRaster = in.getRaster();
        WritableRaster outRaster = out.getRaster();

        ColorModel model = in.getColorModel();
        ColorModel outmodel = out.getColorModel();

        int borderStrength = Integer.valueOf(parameters.get(0));
        int r = Integer.valueOf(parameters.get(1)) % 256;
        int g = Integer.valueOf(parameters.get(2)) % 256;
        int b = Integer.valueOf(parameters.get(3)) % 256;
        int a = Integer.valueOf(parameters.get(4)) % 256;
        int borderRGB = (a << 24 | r << 16 | g << 8 | b);

        int width = in.getWidth();
        int height = in.getHeight();

        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {

                Object inPix = inRaster.getDataElements(i, j, null);

                int rgb = model.getRGB(inPix);

                if (i < borderStrength || i > width - borderStrength - 1 || j < borderStrength || j > height - borderStrength - 1) {
                    rgb = borderRGB;
                }

                outRaster.setDataElements(i, j, outmodel.getDataElements(rgb, null));

            }
        }
    }
}
