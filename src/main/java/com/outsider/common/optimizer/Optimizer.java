package com.outsider.common.optimizer;

/**
 * �Ż����ӿ�
 * @author outsider
 *
 */
public interface Optimizer {
	/**
	 * �Ż�������
	 * @param calculator
	 * @param parameters
	 * @param x
	 * @param y
	 * @param lambda
	 * @param alpha
	 * @param epsilon
	 * @param maxIter
	 * @return ���ز������
	 */
	double[] optimize(GradientCalculator calculator, 
			double[] parameters, double[][] x, double[] y, 
			float lambda, float alpha, double epsilon, int maxIter);
}
