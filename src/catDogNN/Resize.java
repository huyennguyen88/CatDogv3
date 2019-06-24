package catDogNN;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Resize {

	 public static BufferedImage resize(int targetWidth, int targetHeight,
	            BufferedImage src) 
	    {
	        BufferedImage result = new BufferedImage(
	        		 targetWidth,targetHeight, BufferedImage.TYPE_3BYTE_BGR);
	        Graphics2D g2d = result.createGraphics();
	        g2d.drawImage(src, 0, 0, targetWidth, targetHeight, null);
	        g2d.dispose();
	        return result;
	    }
//	public static void main(String[] args) {
//		String folderSrc = "D:/DoAn2/dogs-vs-cats/train/";
//		String folderDst = "D:/DoAn2/dogs-vs-cats/testH/";
//		int N = 10; //Cho: 12500, meo: 12500
//		BufferedImage imgSrc;
//		String pathSrc, pathDst;
//		File imgDst;
//		for(int i=0; i<12500;i++)
//		{
//			try {
//				pathSrc = folderSrc+"cat."+i+".jpg";
//				pathDst = folderDst+"cat."+i+".jpg";
//				imgSrc = ImageIO.read(new File(pathSrc));
//				imgDst = new File(pathDst);
//				ImageIO.write(resize(50, 50, imgSrc), "JPG", imgDst);
//				
//				pathSrc = folderSrc+"dog."+i+".jpg";
//				pathDst = folderDst+"dog."+i+".jpg";
//				imgSrc = ImageIO.read(new File(pathSrc));
//				imgDst = new File(pathDst);
//				ImageIO.write(resize(50, 50, imgSrc), "JPG", imgDst);
//			} 
//			catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}

}
