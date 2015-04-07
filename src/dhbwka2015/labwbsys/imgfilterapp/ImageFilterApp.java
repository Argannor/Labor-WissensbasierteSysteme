package dhbwka2015.labwbsys.imgfilterapp;

import dhbwka2015.labwbsys.imgfilters.*;


public class ImageFilterApp {

	static String DefaultImageFile = "data/redeye1.jpg";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ImageFilterIf filter = null;
		
		filter = new ErodeFilter();
	
		FilteredImageWindow app = new FilteredImageWindow(filter, DefaultImageFile);
		app.setTitle("Image Filter Test Application");
	}

}

