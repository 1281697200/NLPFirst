package com.outsider.common.optimizer;

public class GradientDecentOptimizerTest {
	public static void main(String[] args) {
		/**
		 * ���Գɹ���֮ǰ��Ϊ����̫�󣬵��²������𵴶�û������
		 */
		//����һ��a+bx^2������,�趨a=0��b=1
		double[] params = new double[] {0,0};
		//����һ��x �� yֵ
		double[][] x = new double[100][1];
		double[] y = new double[100];
		int j = -50;
		for(int i = 0; i < 100;i++ ) {
			x[i][0]  = j;
			y[i] = 0+1*j*j;
			j++;
		}
		float alpha = (float) 1e-7;
		float lambda = 0;
		double epsilon = 1e-3;
		GradientDecentOptimizer optimizer = new GradientDecentOptimizer();
		double[] resultParams = optimizer.batchGradientDecent(new MyGradientCalculator(), params, x, y, lambda, alpha,epsilon,-1);
		System.out.println("����a:"+resultParams[0]);
		System.out.println("����b:"+resultParams[1]);
	}
	
	public static class MyGradientCalculator implements GradientCalculator{
		private float frac_1_m = (float) (1.0/100);
		@Override
		public double[] calcGradient(double[] paramaters, 
				 double[][] x, double[] y,
				float lambda, float alpha) {
			double[] gradients = new double[paramaters.length];
			int xSize = x.length;
			double sum1 = 0;
			double sum2 = 0;
			for(int i= 0; i < xSize;i++) {
				double tmp = (paramaters[0] + paramaters[1]*x[i][0]*x[i][0] - y[i]);
				sum1+=(tmp*x[i][0]*x[i][0]);
				sum2+=tmp;
			}
			gradients[0] = sum2*frac_1_m;
			gradients[1] = sum1*frac_1_m;
			return gradients;
		}
	}
}
