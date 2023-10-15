
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;


public class ImageDisplay {

	JFrame frame; // main application window
	JLabel lbIm1; // used to display image
	BufferedImage imgOne; // image buffer used to store the original laoded in image
	BufferedImage imgTwo; // image buffer used to store the scaled image
	BufferedImage overlayImg;
	int width = 1920; // default image width 
	int height = 1080; // default image height
	// int width = 1920*4; // default image width 
	// int height = 1080*4; // default image height
	private boolean controlPressed = false; //track whether the control key is opressed or not
	private int overlaySize; //determines the overlay size of the wxw window
	private boolean updateOverlay = false;

	/** Read Image RGB
	 *  Reads the image of given width and height at the given imgPath into the provided BufferedImage.
	 */
	private void readImageRGB(int width, int height, String imgPath, BufferedImage img)
	{
		try
		{
			int frameLength = width*height*3;

			File file = new File(imgPath); //opens the image path and reads the binary data in the rgb file
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			raf.seek(0);

			long len = frameLength;
			byte[] bytes = new byte[(int) len];

			raf.read(bytes);

			int ind = 0;
			//populating the Buffered Image with RGB pixel values
			for(int y = 0; y < height; y++)
			{
				for(int x = 0; x < width; x++)
				{
					byte r = bytes[ind];
					byte g = bytes[ind+height*width];
					byte b = bytes[ind+height*width*2]; 

					int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					img.setRGB(x,y,pix);
					ind++;
				}
			}
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	private BufferedImage applyAntiAliasing(BufferedImage img){
		// low pass filter to remove aliasing artifacts

		BufferedImage anti_aliasedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); //creating a new BufferedImage with the same dimensions as the imput image

		int radius = 1; //size of low-pass filter (radius of 1 = 3x3)
		int size = 2 * radius + 1; //size of kernal 

		float[] kernel = new float[size * size]; //simple averaging kernal
		for(int i = 0; i < kernel.length; i++) {
			kernel[i] = 1.0f / (size * size);
		}

		ConvolveOp conv = new ConvolveOp(new Kernel(size, size, kernel));

		anti_aliasedImg = conv.filter(img, anti_aliasedImg); //producing the antialiased image

		return anti_aliasedImg;
	}

	private BufferedImage applyScaling(BufferedImage img, float scale) {
		//calculate new dimensions
		// System.out.println("original W: " + img.getWidth());
		// System.out.println("original H: " + img.getHeight());
		int targetW = (int) (img.getWidth() * scale);
		int targetH = (int) (img.getHeight() * scale);
		// System.out.println("target W: " + targetW);
		// System.out.println("target H: " + targetH);

		BufferedImage scaledImg = new BufferedImage(targetW, targetH, BufferedImage.TYPE_INT_RGB); //create a new image

		AffineTransform transform = AffineTransform.getScaleInstance(scale, scale); //affline defines a 2D transformation
		AffineTransformOp scaleOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);

		scaledImg = scaleOp.filter(img, scaledImg); //applying the scaling transformation

		return scaledImg;
	}

	public void showIms(String[] args){

		String param1_ = args[1]; // S value (downscaling factor between 0-1) //convert to float
		Float param1 = Float.parseFloat(param1_);
		String param2_ = args[2]; // A value (aliasing boolean (0 or 1)) //convert to boolean
		Boolean param2 = Integer.parseInt(param2_) != 0;
		String param3_ = args[3]; // w value (square window for overlay)
		Integer param3 = Math.round(Float.parseFloat(param3_));

		System.out.println("The S value is: " + param1);
		System.out.println("The A value is: " + param2);
		System.out.println("The w value: " + param3);

		// Read in the specified image
		imgOne = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		readImageRGB(width, height, args[0], imgOne);
		//creates a new image which will have the antialiasing and scaling 

		if(param2){ // if A is true then apply antialiasing 
			imgTwo = applyAntiAliasing(imgOne);
		}
		else{
			imgTwo = imgOne;
		}

		if(param1 > 0 && param1 <= 1){
			imgTwo = applyScaling(imgTwo, param1);
		}
		else {
			// Display an error message and exit the method
			JOptionPane.showMessageDialog(frame, "Invalid S value. Please enter a value between 0 and 1.");
			return;
		}

		frame = new JFrame();
		GridBagLayout gLayout = new GridBagLayout();
		frame.getContentPane().setLayout(gLayout);

		lbIm1 = new JLabel(new ImageIcon(imgTwo));

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		frame.getContentPane().add(lbIm1, c);

		frame.pack();
		frame.setVisible(true); // displays it 

		//Control Key Pressed 
		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_CONTROL){
					controlPressed = true;
					updateOverlay = true;
				}
			}

			@Override
			public void keyReleased(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_CONTROL){
					controlPressed = false;
					updateOverlay = false; // Disable overlay update when control is released
					// Reset imgTwo to its original state
					imgTwo = applyScaling(imgOne, param1);
					lbIm1.setIcon(new ImageIcon(imgTwo));
				}
			}
		});

		lbIm1.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e){
				if(controlPressed && updateOverlay){
					//adjust the mouse position regards to scaling 
					float mouseX = e.getX() / param1;
					float mouseY = e.getY() / param1;

					//center the overlay around the mouse
					int overlayX = (int) (mouseX - overlaySize / 2);
					int overlayY = (int) (mouseY - overlaySize / 2);

					// Ensure the overlay stays within bounds
					overlayX = Math.max(0, Math.min(overlayX, width - overlaySize));
					overlayY = Math.max(0, Math.min(overlayY, height - overlaySize));
		
					// if(overlayX < 0){
					// 	overlayX = 0;
					// }
					// if(overlayY < 0){
					// 	overlayY = 0;
					// }
					// if(overlayX + overlayY > width){
					// 	overlayX = width - overlaySize;
					// }
					// if(overlayY + overlayY > height){
					// 	overlayY = height - overlaySize;
					// }

					//overlayImg = new BufferedImage(overlaySize, overlaySize, BufferedImage.TYPE_INT_RGB);
				
					// Graphics2D g = overlayImg.createGraphics();
					// //g.drawImage(imgOne, 0, 0, width, height, null);
					// g.drawImage(imgOne, 0, 0, overlaySize, overlaySize, overlayX, overlayY, overlayX + overlaySize, overlayY + overlaySize, null);
					// g.dispose();

					//update the pixels in imgTwo with the corresponding pixel from imgOne within the wxw area 
					// maps pixels from the wxw area to imgOne tp the corresponsoing positions in imgTwo
					for(int y = 0; y < overlaySize; y++){
						for(int x = 0; x < overlaySize; x++){
							int imgOneX = (overlayX + x);
							int imgOneY = (overlayY + y);
							int imgTwoX = (int) (x * param1);
							int imgTwoY = (int) (y * param1);

							if (imgOneX >= 0 && imgOneX < width && imgOneY >= 0 && imgOneY < height &&
                            	imgTwoX >= 0 && imgTwoX < imgTwo.getWidth() && imgTwoY >= 0 && imgTwoY < imgTwo.getHeight()) {
								int rgb = imgOne.getRGB(imgOneX, imgOneY);
								imgTwo.setRGB(imgTwoX, imgTwoY, rgb);
							}
						}
					}
		
					lbIm1.setIcon(new ImageIcon(imgTwo));
				}
			}
		});

		overlaySize = param3;

	}

	public static void main(String[] args) {
		ImageDisplay ren = new ImageDisplay();
		ren.showIms(args);
	}

}
