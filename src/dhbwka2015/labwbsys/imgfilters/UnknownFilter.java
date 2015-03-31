package dhbwka2015.labwbsys.imgfilters;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

public class UnknownFilter implements ImageFilterIf {

	@Override
	public void filterImages(BufferedImage in, BufferedImage out,
			ArrayList<String> parameters) {
		WritableRaster inRaster = in.getRaster();
		WritableRaster outRaster = out.getRaster();

		ColorModel inModel = in.getColorModel();
		ColorModel outModel = out.getColorModel();

		int width = inRaster.getWidth();
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < inRaster.getHeight(); ++j) {

				Object inPix = inRaster.getDataElements(i, j, null);
				int rgb = inModel.getRGB(inPix);

				outRaster.setDataElements(width - i - 1, j,
						outModel.getDataElements(rgb, null));
			}
		}
	}
}
