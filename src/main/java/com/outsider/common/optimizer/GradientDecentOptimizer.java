package com.outsider.common.optimizer;
public class GradientDecentOptimizer implements Optimizer{
	/**
	 * �ݶ��½��Ż� ���߳�
	 * @param parameters
	 * @param x
	 * @param lambda
	 * @param alpha
	 */
	public double[] batchGradientDecent(GradientCalculator calculator, 
			double[] parameters, double[][] x, double[] y, 
			float lambda, float alpha, double epsilon, int maxIter) {
		int iter = 1;
		//��������������������<=0˵�������������������
		//��ô����������������ΪĬ�����޴�
		if(maxIter <= 0) {
			maxIter = Integer.MAX_VALUE;
		}
		while(iter <= maxIter) {
			//���²���
			double[] tmpParameter = parameters.clone();
			double[] gradients = calculator.calcGradient(tmpParameter, x, y,lambda, alpha);
			for(int i = 0; i < parameters.length; i++) {
				parameters[i] = parameters[i] - alpha * gradients[i];
				System.out.println(gradients[i]);
			}
			//֮ǰ������������ˣ�����ֱ���ж������ݶ�С��һ��ֵ����Ϊ����������С��
			//�ж��Ƿ�����
			boolean isConvergent = true;
			for(int i = 0;i < parameters.length; i++) {
				//ֻҪ��һ���ݶ��Ǵ���epsilon�ģ���ô��û������
				if(Math.abs(gradients[i]) > epsilon) {
					isConvergent = false;
					break;
				}
			}
			//��������ͷ������յĲ���
			if(isConvergent) {
				return parameters;
			}
			System.out.println("iter:"+iter);
			iter++;
		}
		//�ﵽ��������������û�з��ز���˵��������û��������
		System.err.println("�ﵽ����������,�������ʧ��!");
		System.err.println("solve parameters failed!");
		return null;
	}

	@Override
	public double[] optimize(GradientCalculator calculator, double[] parameters, double[][] x, double[] y, float lambda,
			float alpha, double epsilon, int maxIter) {
		return batchGradientDecent(calculator, parameters, x, y, lambda, alpha, epsilon, maxIter);
		
	}
}
