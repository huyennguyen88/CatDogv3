package catDogNN;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class XuLyAnh {
	int m,n,wra,hra;
	public XuLyAnh(int m,int wra,int hra) //Anh xam
	{
		this.m = m;
		n= wra*hra;
		this.wra = wra;
		this.hra = hra;
	}

	public double[][] napX(List<BufferedImage> imgList)
	{
		double[][] X = new double[m][n+1];
		int i;
		for (i = 0; i < m; i++) {
			try {
				X[i]= Vectorize(imgList.get(i));
			}catch(Exception e)
			{
				System.out.println("m = "+ m);
				System.out.println("imgList = "+ imgList.size());
				System.out.println("i = "+i);
			}
			
		}
		return X;
	}
	public double[] Vectorize(BufferedImage img) //chuyen 1 img thanh 1 vecto feature
	{
		int color;
		double b,g,r;
		List<double[]> li = new ArrayList<double[]>();
		double[][] imgGray = new double[hra][wra];
		double[] vec = new double[n+1];
		double gray;
		double[] filter = {0,-1,0,
						  -1,5,-1,
						   0,-1,0}; 
		
		for(int i=0;i<hra;i++)
		{
			for(int j=0;j<wra;j++)
			{
				//Tinh tong
				color=img.getRGB(j, i);
				b = color & 0xff;
				g = (color >> 8) & 0xff;
				r = (color >> 16) & 0xff;
				gray = r * 0.299 + g * 0.587 + b * 0.114;
				imgGray[i][j] =gray; 
			}
		}
		//Filter
		double[][] imgTemp = new double[hra+2][wra+2];
		double[][] imgNew = new double[hra][wra];
		//Them vien cho img
		for (int i = 1; i < imgTemp.length-1; i++) {
			for (int j = 1; j < imgTemp[0].length-1; j++) {
				imgTemp[i][j] = imgGray[i-1][j-1];
			}
		}
		//Tich chap voi filter
		double tong=0;
		int f =0;
		int k=0;
		for (int i = 1; i < imgTemp.length -1; i++) 
		{
			for (int j = 1; j < imgTemp[0].length-1; j++) 
			{
				tong=0;
				f=0;
				for (int t = i-1; t < i-1+3; t++) 
				{
					for (int l = j-1; l < j-1+3; l++) 
					{
						tong+= imgTemp[t][l]*filter[f];
						f++;
					}
				}
				imgNew[i-1][j-1]= tong;
				vec[k]= tong/255;
				k++;
			}
		}
		//Chuyen thanh 1 vector
		vec[n] =1;
		return  vec;
	}
}
