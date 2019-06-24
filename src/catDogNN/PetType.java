package catDogNN;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class PetType {
	NN nn;
	File iF;
	public PetType(File iF)
	{
		this.iF = iF;
		nn = new NN();
		Predict();
		
	}
	public boolean Predict()
	{
		int targetWidth = 50;
		int targetHeight = 50;
		BufferedImage src;
		double out = -1;
		try {
			src = ImageIO.read(iF);
			BufferedImage img = Resize.resize(targetWidth,targetHeight,src); 
			out= nn.testOne(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(out<=0.5) return false; //cho
		return true;	//meo

	}


}
