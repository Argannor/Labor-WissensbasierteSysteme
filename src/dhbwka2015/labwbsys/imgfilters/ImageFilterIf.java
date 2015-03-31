package dhbwka2015.labwbsys.imgfilters;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public interface ImageFilterIf {

	static public final ArrayList<String> EMPTYPARAMS = new ArrayList<String>(Arrays.asList("", "", "", "", "", ""));
	
	public void filterImages(BufferedImage in, BufferedImage out, ArrayList<String> parameters);

}

