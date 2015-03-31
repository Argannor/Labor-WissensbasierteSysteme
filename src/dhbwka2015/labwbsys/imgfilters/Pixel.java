package dhbwka2015.labwbsys.imgfilters;

/**
 * Created by Soeren Berken-Mersmann on 31.03.15.
 */
public class Pixel {
    Object pixel;
    int i,j;

    public Pixel(Object pixel, int i, int j) {
        this.pixel = pixel;
        this.i = i;
        this.j = j;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Pixel)) {
            return false;
        }
        Pixel px = (Pixel) obj;
        if(px.i == i && px.j == j) {
            return true;
        }
        return false;
    }
}
