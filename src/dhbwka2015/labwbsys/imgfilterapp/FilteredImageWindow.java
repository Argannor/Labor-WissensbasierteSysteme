package dhbwka2015.labwbsys.imgfilterapp;

import dhbwka2015.labwbsys.imgfilters.ImageFilterIf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class FilteredImageWindow extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String inFileName;
	File inFile;

	JPanel pnlMainLayout, pnlControl, pnlImages, pnlImageLeft, pnlImageRight, pnlStatus;
	JScrollPane spnlImageLeft, spnlImageRight;
	JLabel labelLeft, labelRight, labelStatus;

	JTextField tfInputFile, tfParam0, tfParam1, tfParam2, tfParam3, tfParam4,
			tfParam5;
	JButton btnSelectFile, btnLoadFile, btnSelectParam2, btnSelectParam5;

	BufferedImage inImage = null;
	BufferedImage outImage = null;
	ImageIcon inIcon, outIcon;

	ImageFilterIf filterObject;

	public FilteredImageWindow(ImageFilterIf filter, String imageFileName) {
		filterObject = filter;

		pnlMainLayout = new JPanel();
		pnlMainLayout.setLayout(new BoxLayout(pnlMainLayout, BoxLayout.Y_AXIS));

		// Control elements area
		// --------------------------------------------------------------------

		pnlControl = new JPanel();
		pnlControl.setLayout(new BoxLayout(pnlControl, BoxLayout.Y_AXIS));

		Box imageFileBox = Box.createHorizontalBox();
		imageFileBox.add(new JLabel("Image file: "));
		tfInputFile = new JTextField(imageFileName, 30);
		tfInputFile.setMaximumSize(new Dimension(300, 30));
		imageFileBox.add(tfInputFile);

		btnSelectFile = new JButton("Select image");
		btnSelectFile.addActionListener(this);
		imageFileBox.add(btnSelectFile);
		imageFileBox.add(Box.createHorizontalGlue());
		pnlControl.add(imageFileBox);

		Box params0to2 = Box.createHorizontalBox();
		params0to2.add(new JLabel("Param 0: "));
		tfParam0 = new JTextField("", 30);
		tfParam0.setMaximumSize(new Dimension(20, 30));
		params0to2.add(tfParam0);
		params0to2.add(new JLabel("Param 1: "));
		tfParam1 = new JTextField("", 30);
		tfParam1.setMaximumSize(new Dimension(20, 30));
		params0to2.add(tfParam1);
		params0to2.add(new JLabel("Param 2: "));
		tfParam2 = new JTextField("", 30);
		tfParam2.setMaximumSize(new Dimension(20, 30));
		params0to2.add(tfParam2);
		btnSelectParam2 = new JButton("Select Param 2");
		btnSelectParam2.addActionListener(this);
		params0to2.add(btnSelectParam2);
		params0to2.add(Box.createHorizontalGlue());
		pnlControl.add(params0to2);

		Box params3to5 = Box.createHorizontalBox();
		params3to5.add(new JLabel("Param 3: "));
		tfParam3 = new JTextField("", 30);
		tfParam3.setMaximumSize(new Dimension(20, 30));
		params3to5.add(tfParam3);
		params3to5.add(new JLabel("Param 4: "));
		tfParam4 = new JTextField("", 30);
		tfParam4.setMaximumSize(new Dimension(20, 30));
		params3to5.add(tfParam4);
		params3to5.add(new JLabel("Param 5: "));
		tfParam5 = new JTextField("", 30);
		tfParam5.setMaximumSize(new Dimension(20, 30));
		params3to5.add(tfParam5);
		btnSelectParam5 = new JButton("Select Param 5");
		btnSelectParam5.addActionListener(this);
		params3to5.add(btnSelectParam5);
		params3to5.add(Box.createHorizontalGlue());
		pnlControl.add(params3to5);

		btnLoadFile = new JButton("(Re-)Load image");
		btnLoadFile.addActionListener(this);
		pnlControl.add(btnLoadFile);

		pnlMainLayout.add(pnlControl);

		inFileName = tfInputFile.getText();

		// Image (original and filtered) area
		// ----------------------------------------------------------
		labelLeft = new JLabel("No image loaded!");
		labelLeft.setBorder(new LineBorder(Color.WHITE));
		labelLeft.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e){
				showPixelStatus(e.getX(), e.getY());
			}

			public void mousePressed(MouseEvent e) {
				// unused event
			}

			public void mouseReleased(MouseEvent e) {
				// unused event
			}

			public void mouseEntered(MouseEvent e) {
				// unused event
			}

			public void mouseExited(MouseEvent e) {
				// unused event
			}
		});
		
		pnlImageLeft = new JPanel();
		pnlImageLeft.setLayout(new FlowLayout());
		pnlImageLeft.setBackground(Color.DARK_GRAY);
		pnlImageLeft.add(labelLeft);

		labelRight = new JLabel(outIcon);
		labelRight.setBorder(new LineBorder(Color.WHITE));

		pnlImageRight = new JPanel();
		pnlImageRight.setLayout(new FlowLayout());
		pnlImageRight.setBackground(Color.DARK_GRAY);
		pnlImageRight.add(labelRight);

		spnlImageLeft = new JScrollPane(pnlImageLeft);
		spnlImageRight = new JScrollPane(pnlImageRight);

		pnlImages = new JPanel();
		pnlImages.setLayout(new BoxLayout(pnlImages, BoxLayout.X_AXIS));

		pnlImages.add(spnlImageLeft);
		pnlImages.add(spnlImageRight);

		pnlMainLayout.add(pnlImages);


		// Status line area
		// ----------------------------------------------------------
		pnlStatus = new JPanel();
		pnlStatus.setLayout(new BoxLayout(pnlStatus, BoxLayout.X_AXIS));
		labelStatus = new JLabel(" ");
		pnlStatus.add(labelStatus);
		
		pnlMainLayout.add(pnlStatus);
		
		getContentPane().add(pnlMainLayout);
		setSize(2 * 300 + 20, 200 + 200);

		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void processImage() {

		ArrayList<String> params = new ArrayList<String>();
		params.add(tfParam0.getText());
		params.add(tfParam1.getText());
		params.add(tfParam2.getText());
		params.add(tfParam3.getText());
		params.add(tfParam4.getText());
		params.add(tfParam5.getText());

		filterObject.filterImages(inImage, outImage, params);
	}

	private void loadInImage() {
		try {
			inImage = ImageIO.read(new File(inFileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void showPixelStatus(int x, int y){
		int pixel = inImage.getRGB(x, y);
		int a = (pixel >> 24) & 0xff;
		int r = (pixel >> 16) & 0xff;
		int g = (pixel >> 8) & 0xff;
		int b = pixel & 0xff;
		
		labelStatus.setText("Position (X/Y): " + x + " / " + y + "       Farbe (A/R/G/B): " + a + " / " + r + " / " + g + " / " + b);
	}
	
	private void handleNewImages() {
		inFile = new File(inFileName);
		inImage = null;
		outImage = null;

		if (inFile.exists()) {
			loadInImage();
			inIcon = new ImageIcon(inImage);
			labelLeft.setText("");
			labelLeft.setIcon(inIcon);

			outImage = new BufferedImage(inImage.getWidth(),
					inImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
		} else {
			labelLeft.setIcon(null);
			labelLeft.setText("Could not find file!");
		}

		labelLeft.setBorder(new LineBorder(Color.WHITE));

		if (inImage != null) {
			processImage();

			outIcon = new ImageIcon(outImage);
	
			labelRight.setText("");
			labelRight.setIcon(outIcon);
			labelRight.setBorder(new LineBorder(Color.WHITE));
			setSize(2 * inImage.getWidth() + 20, inImage.getHeight() + 60);
		} else {
			labelRight.setIcon(null);
			labelRight.setBorder(new LineBorder(Color.WHITE));
		}
	}

	private void selectImageFile() {
		String file = selectFile();

		if (file != null) {
			tfInputFile.setText(file);
		}
	}

	private void selectFileParam2() {
		String file = selectFile();

		if (file != null) {
			tfParam2.setText(file);
		}
	}

	private void selectFileParam5() {
		String file = selectFile();

		if (file != null) {
			tfParam5.setText(file);
		}
	}

	private String selectFile() {
		String res = null;

		JFileChooser chooser = new JFileChooser("Verzeichnis wählen");
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

		chooser.setVisible(true);
		int result = chooser.showOpenDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			res = chooser.getSelectedFile().getAbsolutePath();
		}

		chooser.setVisible(false);

		return res;
	}

	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource() == this.btnSelectFile) {
			selectImageFile();
		} else if (ae.getSource() == this.btnSelectParam2) {
			selectFileParam2();
		} else if (ae.getSource() == this.btnSelectParam5) {
			selectFileParam5();
		} else if (ae.getSource() == this.btnLoadFile) {
			inFileName = tfInputFile.getText();
			handleNewImages();
		}
	}
}


