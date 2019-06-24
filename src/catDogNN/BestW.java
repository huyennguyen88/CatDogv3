package catDogNN;

public class BestW {
	double[][] w1;
	double[] w2;
	double percent;
	public BestW()
	{

	}
	public void saochep(double[][] w1, double[] w2, double p)
	{
		this.w1 = new double[w1.length][];
		this.w2 = w2.clone();
		for (int i = 0; i < w1.length; i++) {
			this.w1[i] = w1[i].clone();
		}
		this.percent = p;
	}
	public double[][] getW1()
	{
		double[][] w = new double[w1.length][];
		for (int i = 0; i < w.length; i++) {
			w[i] = w1[i].clone();
		}
		return w;
	}
	public double[] getW2()
	{
		double[] w;
		w = w2.clone();
		return w;
	}
}
