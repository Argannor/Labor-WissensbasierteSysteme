package dhbwka2015.labwbsys.imgfilters;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

public class MarkPointsFilter implements ImageFilterIf {

	@Override
	public void filterImages(BufferedImage in, BufferedImage out,
			ArrayList<String> parameters) {

		CsvCommentedReader reader = new CsvCommentedReader(parameters.get(2));
		ArrayList<int[]> pointList;
		if (reader.readFile()) {
			System.out.println(reader.toString());

			pointList = readPoints(reader.getFileContent());
		} else {
			System.out.println("Points in CSV could not be read!");
			return;
		}

		WritableRaster inRaster = in.getRaster();
		WritableRaster outRaster = out.getRaster();
		ColorModel inModel = in.getColorModel();
		ColorModel outModel = out.getColorModel();

		for (int i = 0; i < in.getWidth(); ++i) {
			for (int j = 0; j < in.getHeight(); ++j) {
				int px = inModel
						.getRGB(inRaster.getDataElements(i, j, null));

				Object pxData = outModel.getDataElements(px, null);
				outRaster.setDataElements(i, j, pxData);
			}
		}

		for (int[] p : pointList){
			System.out.println("Working on point " + p[0] + "/" + p[1]);
			
			int pxNew = 0xff000000 | (0xff << 8) | 0xff; 

			Object pxData = outModel.getDataElements(pxNew, null);
			outRaster.setDataElements(p[0], p[1], pxData);
		}
	}

	private ArrayList<int[]> readPoints(ArrayList<String[]> data) {
		ArrayList<int[]> res = new ArrayList<int[]>();

		for (String[] sa : data) {
			if (sa.length != 3) {
				continue;
			}

			int[] point = new int[2];
			try {
				int xpos = Integer.parseInt(sa[1].trim());
				int ypos = Integer.parseInt(sa[2].trim());

				point[0] = xpos;
				point[1] = ypos;
			} catch (Exception ex) {
				continue;
			}

			res.add(point);
		}

		return res;
	}

}
