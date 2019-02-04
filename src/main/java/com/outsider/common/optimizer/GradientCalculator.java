package com.outsider.common.optimizer;

/**
 *�ݶ��½��е��ݶȼ�����Ҫ�ⲿʵ�֣��������ﶨ��ӿ�
 * @author outsider
 *
 */
public interface GradientCalculator extends Calculator{
	/**
	 * �ݶȵļ���,��Ҫ�����һ���ݶȵ�ֵ
	 * @param parameters ��ǰ�Ĳ���ֵ
	 * @param x һ��������xֵ
	 * @param y ������yֵ
	 * @param lambda ������ĳ˷�ϵ��
	 * @param alpha ѧϰЧ��
	 * @return ���ؼ���õ�һ���ݶ�ֵ
	 */
	double[] calcGradient(double[] parameters, double[][] x, 
			double[] y, float lambda, float alpha);
	
}
