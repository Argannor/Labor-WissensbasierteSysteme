package dhbwka2015.labwbsys.imgfilters;

import java.awt.*;

/**
 * Created by argannor on 23.04.15.
 *
 */
public final class ColorHelper {
    private ColorHelper(){}

    public static int getRed(int rgb) {
        return (rgb & 0xff0000) >> 16;
    }

    public static int getGreen(int rgb) {
        return (rgb & 0x00ff00) >> 8;
    }

    public static int getBlue(int rgb) {
        return rgb & 0xff;
    }

    public static float[] getHSB(int rgb) {
        float[] result = new float[3];
        Color.RGBtoHSB(getRed(rgb), getGreen(rgb), getBlue(rgb), result);
        return result;
    }

    public static float getHue(int rgb) {
        return getHSB(rgb)[0];
    }
    public static float getSaturation(int rgb) {
        return getHSB(rgb)[1];
    }
    public static float getBrightness(int rgb) {
        return getHSB(rgb)[2];
    }

}
