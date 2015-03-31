package dhbwka2015.labwbsys.imgfilters;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

public class CopyFilter implements ImageFilterIf {

	@Override
	public void filterImages(BufferedImage in, BufferedImage out, ArrayList<String> parameters) {
		WritableRaster inRaster = in.getRaster();
		WritableRaster outRaster = out.getRaster();

		ColorModel model = in.getColorModel();
		ColorModel outmodel = out.getColorModel();
		
		for (int i = 0; i < in.getWidth(); ++i) {
			for (int j = 0; j < in.getHeight(); ++j) {

				Object inPix = inRaster.getDataElements(i, j, null);
				
				int rgb = model.getRGB(inPix);
				
				outRaster.setDataElements(i, j, outmodel.getDataElements(rgb, null));
			}
		}
	}

}
