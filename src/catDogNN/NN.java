package catDogNN;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class NN 
{
	int n; //So feature dau vao, ko tinh bias
	int wra;
	int hra;
	double percentOld;
	double w1[][]; 
	double w2[]; 
	int unit =2;
	public NN()
	{
	}
	public void Xuly()
	{
		//readWeightFile("D:/DoAn2/weight12.txt");
		initWeight();
		int m = 500; //So anh muon train
		int batchNum = 1;
		int loop = 3000;
		double alpha = 0.15;
		double J=9999;
		double pc=0;
		double percentNew = 0;
		int unit=2;

		String Folder = "D:/DoAn2/dogs-vs-cats/trainH2/";
		List<BufferedImage> imgList ;
		
		double X[][]; //Ma tran data 
		int Y[] = new int[m]; //Vecto label
		double[] Hieu = new double[m];
		double[][] dw1 = new double[n+1][unit];
		double[] dw2 = new double[unit+1];
		double[] A2 = new double[m];
		double A1[][] = new double[m][unit+1];
		//Xu ly theo batch
		BestW bestw = new BestW();
		bestw.saochep(w1,w2,percentOld);
		while(batchNum-- >0) //Lap 4 lan, moi lan 1000 tam
		{	
			imgList = readListImg(Folder,m,Y);
			//Chuyen tap anh thanh 1 matrix
			XuLyAnh xla = new XuLyAnh(m,wra,hra);
			X = xla.napX(imgList);
			System.out.println("batch "+ batchNum+"-------------------");	
			//System.out.println("weight0 now: " + weight[0] + "Bias now : " + weight[n]);
			for(int l =0; l< loop; l++)
			{
				for (int j = 0; j < m; j++) //Tinh output toan bo m anh
				{
					for(int i=0; i<unit ;i++) //TinhA1
					{
						A1[j][i] = outPutUnit(X[j], w1[i]);
					}
					A1[j][unit]=1;
					A2[j] = outPutL2(A1[j],w2);
				}
				//Tinh vecto hieu A - Y 
				//m anh
				for (int j = 0; j < m; j++) 
				{
					Hieu[j] = A2[j]- Y[j];
				}
				//tinh dw1
				//m anh
				double tong =0;
				for (int i = 0; i < dw1.length; i++) 
				{
					for (int j = 0; j < dw1[0].length; j++) 
					{
						tong=0;
						for (int t = 0; t < m; t++) 
						{
							tong+= X[t][i]*A1[t][j]*(1-A1[t][j])*w2[j]*Hieu[t];
						}
						dw1[i][j]= tong/m;
					}
				}
				//tinh dw2
				//m anh
				for (int i = 0; i < dw2.length; i++) 
				{
					tong=0;
					for (int j = 0; j < m; j++) 
					{
						tong += (A1[j][i]*Hieu[j]);
					}
					dw2[i] = tong/m;
				}
				//Cap nhat W1
				for (int i = 0; i < w1.length; i++) 
				{
					for (int j = 0; j < w1[0].length; j++) 
					{
						w1[i][j] -= dw1[j][i]*alpha;
					}
				}
				//Cap nhat w2
				for (int i = 0; i < w2.length; i++) 
				{
					w2[i] -= dw2[i]*alpha;
				}
				J = Loss(Y, A2, m) ;
				//Kiem tra do chinh xac cua trong so nay trong tap train
				
				pc = testTrain(m,X,Y,w1,w2);
				System.out.printf("Cost train loop %d : %.3f | train accuracy : %.3f \n",l,J,pc);
			}
		
			//J = Loss(Y, A2, m) ;
			//pc = testTrain(m,X,Y,w1,w2);
			System.out.printf("Cost train batch %d : %.3f | train accuracy : %.3f------------------ \n",batchNum,J,pc);
			//System.out.println("In train batch weight0 now: " + weight[0] + "Bias now : " + weight[n]);
			//Kiem tra trong tap test random
			percentNew =testRandom(50); 
			
			System.out.printf("Test accuracy : %f \n",percentNew);
			if(percentNew>bestw.percent) 
			{
				 bestw.saochep(w1,w2, percentNew);
			}
			else {
				w1 = bestw.getW1();
				w2 = bestw.getW2();
			}
			System.out.println("Best percent : "+bestw.percent);
			System.out.println("best weight0 now: " + bestw.w1[0][0] + " | Best Bias now : " + bestw.w2[2]);
			
		}///-------------finish batch
		writeWeightfile(wra,hra,n,bestw.percent,w1,w2);
	}
	public double testOne(BufferedImage img)
	{
		readWeightFile("D:/DoAn2/weight13.txt");
		//BufferedImage img;
		double[] vec;
		XuLyAnh xla = new XuLyAnh(1,wra,hra);
		double out=-1;
		try {
			//img = ImageIO.read(iF);
			vec = xla.Vectorize(img);
			//Bat dau test
			
			int count=0;
			out = outPutNet(vec,w1,w2);
			if(out<=0.5) System.out.println("Out: "+out+" --> Dog");
			else System.out.println("Out: "+out+" --> Cat");	
		} catch (Exception e) {}
		return out;
	
	}
	public double testTrain(int m, double[][] X, int[] Y,double[][] w1, double[] w2)
	{
		//Bat dau test
		
		double out;
		int count=0;
		for (int i = 0; i < m; i++) 
		{
			out = outPutNet(X[i],w1,w2);
			if((out>0.5 && Y[i]==1) || (out<=0.5 && Y[i]==0)) count++;
		}
		double percent = (count*100.0)/m;
		return percent;
	}
	//1 sample
	public double outPutNet(double[] X, double[][] w1, double[] w2  )
	{
		int n = X.length;
		double[] a1 = new double[n];
		double a2;
		for(int i=0; i<unit ;i++) //TinhA1
		{
			a1[i] = outPutUnit(X, w1[i]);
		}
		a2 = outPutL2(a1,w2);
		return a2;
	}
	public double testRandom(int m)
	{
		List<BufferedImage> imgList = new ArrayList<BufferedImage>();
		String Folder;
		Folder = "D:/DoAn2/dogs-vs-cats/trainH2/";
		int[] Y = new int[m];
		double[][] X ;
		imgList= readListImg(Folder,m,Y);
		XuLyAnh xla = new XuLyAnh(m,wra,hra);
		X = xla.napX(imgList);
		//Bat dau test
		double out;
		int count=0;
		for (int i = 0; i < m; i++) {
			out = outPutNet(X[i],w1,w2);
			if((out>0.5 && Y[i]==1) || (out<=0.5 && Y[i]==0)) count++;
		}
		double percent = (count*100.0)/m;
		return percent;
	}

	//Random ma tran weight
	public void readWeightFile(String path)
	{		
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			percentOld = Double.parseDouble(br.readLine());
			wra = Integer.parseInt(br.readLine());
			hra = Integer.parseInt(br.readLine());
			n = Integer.parseInt(br.readLine());
			w1 = new double[unit][n+1];
			w2 = new double[unit+1];
			for (int i = 0; i < w1.length; i++) {
				for (int j = 0; j < w1[0].length; j++) {
					w1[i][j]= Double.parseDouble(br.readLine()); 
				}
			}
			for (int i = 0; i < w2.length; i++) {
				w2[i]= Double.parseDouble(br.readLine()); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public List<BufferedImage> readListImg(String Folder, int m,int[] Y) //Chon random m anh
	{
		String path;
		List<BufferedImage> imgList = new ArrayList<BufferedImage>();
		BufferedImage img;
		Random rd = new Random();
	    String chon;
		for (int i = 0; i < m; i++) 
		{
			path = Folder;
			int hinh= rd.nextInt(8000);
			if(rd.nextBoolean()) 
			{
				chon = "cat."+hinh+".jpg";
				path+= chon;
				Y[i]=1;
				//System.out.println("path: "+path+" : "+Y[i]);
			}
			else {
				chon = "dog."+hinh+".jpg";
				path+= chon;
				Y[i]=0;
				//System.out.println("path: "+path+" : "+Y[i]);
				
			}
			
			try {
				img = ImageIO.read(new File(path));
				imgList.add(img);
			}catch (Exception e) {
				System.out.println("path: "+path);
				System.out.println("Not_add");
			}
		}
		return imgList;
	
	}
	public void initWeight()
	{
		wra=50; //thu nho anh -> 50x50 pixel
		hra=50;
		n = wra*hra;

		//Khoi tao w1
		w1 = new double[unit][n+1];  //+bias
		w2 = new double[unit+1]; //bias
		percentOld = 0;
		Random rd = new Random();
		for(int i=0;i<w1.length;i++)
		{
			for (int j = 0; j < w1[0].length; j++) {
				w1[i][j] = rd.nextDouble()-0.5;
			}
		}
		//khoi tao w2
		for (int i = 0; i < w2.length; i++) {
			w2[i] = rd.nextDouble()-0.5;
		}
		System.out.println("Khoi tao xong");
	}
	//
	//1 sample
	public double outPutL2(double a1[], double[] w2)
	{
		double a2;
		a2 = outPutUnit(a1, w2);
		return a2;
	}
	//Output cua 1 sample trong 1 unit
	public double outPutUnit(double vec[], double[] weight) //co bias
	{
		//Ham tong
		int i;
		int n = weight.length;
		double b=weight[n-1];
		double z=0;
		double a;
		z+=b;
		for(i=0;i<n;i++)
		{
			z+= weight[i]*vec[i];
		}
		//Activate
		a = 1.0/(1.0+Math.exp(-z));
		return a;
	}
	public double Loss(int[] Y, double[] A,int m ) 
	{
		double J=0;
		for (int i = 0; i < m; i++) //Anh thu 1
		{
			J += (Y[i]*Math.log(A[i]) + (1-Y[i])*Math.log(1-A[i]));
		}
		return -1.0*J/m;
	}
	public void writeWeightfile(int wra, int hra, int n, double percent, double[][] w1, double[] w2)
	{
		String path = "D:/DoAn2/weight13.txt";
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(path));
			bw.write(String.valueOf(percent));
			bw.newLine();
			bw.write(String.valueOf(wra));
			bw.newLine();
			bw.write(String.valueOf(hra));
			bw.newLine();
			bw.write(String.valueOf(n));
			bw.newLine();
			for (int i = 0; i < w1.length; i++) {
				//double w = Math.round(weight[i]*100000.0)/100000.0;
				for (int j = 0; j < w1[0].length; j++) {
					double w = w1[i][j];
					bw.write(String.valueOf(w));
					bw.newLine();
				}
			}
			for (int i = 0; i < w2.length; i++) {
				double w = w2[i];
				bw.write(String.valueOf(w));
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void xuat1(double[] a)
	{
		for (int j = 0; j < a.length; j++) {
			System.out.printf("%.2f ",a[j]);
		}
		System.out.println();
	}
	public static void main(String[] args) {
		NN nn = new NN();
		nn.Xuly();

	}

}
