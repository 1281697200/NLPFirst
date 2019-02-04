package com.outsider.model.hmm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.outsider.common.logger.CONLPLogger;
import com.outsider.common.util.MathUtils;
/**
 * 
 * �޼ලѧϰ��HMMʵ��
 * �������ݽ��鴮��
 * �������ݣ���ʮ�򣬰����������ߵ�����ǿ�ҽ��鲢��ѵ��,�����Ǵ��еĺ�4������
 * @author outsider
 */
public class UnsupervisedFirstOrderGeneralHMM extends FirstOrderGeneralHMM{
	/**
	 * ѵ�����ݳ���
	 */
	private double precision = 1e-7;
	private int sequenceLen;
	public Logger logger = CONLPLogger.getLoggerOfAClass(UnsupervisedFirstOrderGeneralHMM.class);
	public UnsupervisedFirstOrderGeneralHMM() {
		super();
	}
	public UnsupervisedFirstOrderGeneralHMM(int stateNum, int observationNum, double[] pi,
			double[][] transferProbability1, double[][] emissionProbability) {
		super(stateNum, observationNum, pi, transferProbability1, emissionProbability);
	}
	public UnsupervisedFirstOrderGeneralHMM(int stateNum, int observationNum) {
		super(stateNum, observationNum);
	}
	/**
	 * ����HMM�������ܳ�
	 */
	@Override
	protected void solve(List<SequenceNode> nodes) {
		baumWelch(nodes, -1, 1e-4);
	}
	/**
	 * ѵ������
	 * @param nodes ѵ����������
	 * @param maxIter ����������
	 * @param precision ����
	 */
	public void train(List<SequenceNode> nodes, int maxIter, double precision) {
		this.sequenceLen = nodes.size();
		baumWelch(nodes, maxIter, precision);
	}
	
	@Override
	public void train(List<SequenceNode> nodes) {
		this.sequenceLen = nodes.size();
		solve(nodes);
		//�������ʹ�һ��
	}
	/**
	 * ����ѵ��
	 * @param nodes ѵ�����ݽ��list
	 * @param maxIter ����������
	 * @param precision ����
	 */
	public void parallelTrain(List<SequenceNode> nodes, int maxIter, double precision) {
		this.sequenceLen = nodes.size();
		parallelBaumWelch(nodes, maxIter, precision);
	}
	
	/**
	 * baumWelch�㷨�������
	 * ����ʱ���������������²�������һ�εĲ���������󣬵��ǵ�����������ֵ����������
	 * ���Ե�����ֹ��������2����
	 * 1.�ﵽ����������
	 * 2.����A��B��pi�е�ֵ�����һ�ε�������С��ĳ������ֵ����Ϊ����
	 * 3.��1�и��ľ���ֵ̫������ܵ����޷�����������������һ�������������ǰ������������һ�ε��������С��ĳ��ֵ���������1e-7����
	 * ������Ϊ�����ˡ�
	 * @param nodes �۲�����
	 * @param maxIter �������������������<=0������Ĭ��ΪInteger.MAX_VALUE,�൱�ڲ������Ͳ�����ѭ��
	 * @param precision �������ľ���С��precision����Ϊ����
	 */
	protected void baumWelch(List<SequenceNode> nodes, int maxIter, double precision) {
		int iter = 0;
		double oldMaxError = 0;
		if(maxIter <= 0) {
			maxIter = Integer.MAX_VALUE;
		}
		//��ʼ�����ֲ���
		double[][] alpha = new double[sequenceLen][stateNum];
		double[][] beta = new double[sequenceLen][stateNum];
		double[][] gamma  = new double[sequenceLen][stateNum];
		double[][][] ksi = new double[sequenceLen][stateNum][stateNum];
		while(iter < maxIter) {
			logger.info("\niter"+iter+"...");
			long start = System.currentTimeMillis();
			//������ֲ�����Ϊ����ģ�Ͳ�����׼������ӦEM�е�E��
			calcAlpha(nodes, alpha);
			calcBeta(nodes, beta);
			calcGamma(nodes, alpha, beta, gamma);
			calcKsi(nodes, alpha, beta, ksi);
			//���²�������ӦEM�е�M��
			double[][] oldA = generateOldA();
			//double[][] oldB = generateOldB();
			//double[] oldPi = pi.clone();
			updateLambda(nodes, gamma, ksi);
			//double maxError = calcError(oldA, oldPi, oldB);
			double maxError = calcError(oldA, null, null);
			logger.info("max_error:"+maxError);
			if(maxError < precision || (Math.abs(maxError-oldMaxError)) < this.precision) {
				logger.info("����������....");
				break;
			}
			oldMaxError = maxError;
			iter++;
			long end = System.currentTimeMillis();
			logger.info("���ε�������,��ʱ:"+(end - start)+"����");
		}
		logger.info("���ղ���:");
		logger.info("pi:"+Arrays.toString(pi));
		logger.info("A:");
		for(int i = 0; i < transferProbability1.length; i++) {
			logger.info(Arrays.toString(transferProbability1[i]));
		}
	}
	
	/**
	 * BaumWelch����ѵ��
	 * @param nodes
	 * @param maxIter
	 * @param precision
	 */
	protected void parallelBaumWelch(List<SequenceNode> nodes, int maxIter, double precision) {
		int iter = 0;
		double oldMaxError = 0;
		if(maxIter <= 0) {
			maxIter = Integer.MAX_VALUE;
		}
		//��ʼ�����ֲ���
		double[][] alpha = new double[sequenceLen][stateNum];
		double[][] beta = new double[sequenceLen][stateNum];
		double[][] gamma  = new double[sequenceLen][stateNum];
		double[][][] ksi = new double[sequenceLen][stateNum][stateNum];
		while(iter < maxIter) {
			logger.info("\niter"+iter+"...");
			long start = System.currentTimeMillis();
			//������ֲ�����Ϊ����ģ�Ͳ�����׼������ӦEM�е�E��
			//alpha��beta�ļ�����Բ���
			Thread alphathe =  new Thread(new AlphaCalculatorThread(this, nodes, alpha));
			Thread betathe = new Thread(new BetaCalculatorThread(this, nodes, beta));
			alphathe.start();
			betathe.start();
			//���̵߳ȴ�alpha��beta������ɲ���������
			try {
				alphathe.join();
				betathe.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//gamma��ksi�ļ�����Բ��У����Ǳ���ȴ�alpha��beta�ļ������
			Thread gammathe = new Thread(new GammaCalculatorThread(this, nodes, alpha, beta, gamma));
			Thread ksithe = new Thread(new KsiCalculatorThread(this, nodes, alpha, beta, ksi));
			gammathe.start();
			ksithe.start();
			try {
				gammathe.join();
				ksithe.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//���²�������ӦEM�е�M��
			double[][] oldA = generateOldA();
			//double[][] oldB = generateOldB();
			//double[] oldPi = pi.clone();
			parallelUpdateLambda(nodes, gamma, ksi);
			//double maxError = calcError(oldA, oldPi, oldB);
			double maxError = calcError(oldA, null, null);
			logger.info("max_error:"+maxError);
			if(maxError < precision || (Math.abs(maxError-oldMaxError)) < this.precision) {
				logger.info("����������....");
				break;
			}
			oldMaxError = maxError;
			iter++;
			long end = System.currentTimeMillis();
			logger.info("���ε�������,��ʱ:"+(end - start)+"����");
		}
		logger.info("���ղ���:");
		logger.info("pi:"+Arrays.toString(pi));
		logger.info("A:");
		for(int i = 0; i < transferProbability1.length; i++) {
			logger.info(Arrays.toString(transferProbability1[i]));
		}
	}
	/**
	 * ����ɵĲ���A
	 * @return
	 */
	protected double[][] generateOldA() {
		double[][] oldA = new double[stateNum][stateNum];
		for(int i = 0; i < stateNum; i++) {
			for(int j = 0; j < stateNum; j++) {
				oldA[i][j] = transferProbability1[i][j];
			}
		}
		return oldA;
	}
	/**
	 * ����ɵĲ���B
	 * @return
	 */
	protected double[][] generateOldB() {
		double[][] oldB = new double[stateNum][observationNum];
		for(int i = 0; i < stateNum; i++) {
			for(int j = 0; j < observationNum; j++) {
				oldB[i][j] = emissionProbability[i][j];
			}
		}
		return oldB;
	}
	/**
	 * ��ʱֻ�������A�����
	 * ���ּ���B��pi�ᷢ�ֲ������Խ��Խ������󣬻�����������
	 * @param old
	 * @return
	 */
	protected double calcError(double[][] oldA, double[] oldPi, double[][] oldB) {
		double maxError = 0;
		for(int i =0 ; i < stateNum; i++) {
			/*double tmp1 = Math.abs(pi[i] - oldPi[i]);
			maxError = tmp1 > maxError ? tmp1 : maxError;*/
			for(int j =0; j < stateNum; j++) {
				double tmp = Math.abs(oldA[i][j] - transferProbability1[i][j]);
				maxError = tmp > maxError ? tmp : maxError;
			}
			/*for(int k =0; k < observationNum; k++) {
				double tmp2 = Math.abs(emissionProbability[i][k] - oldB[i][k]);
				maxError = tmp2 > maxError ? tmp2 : maxError;
			}*/
		}
		return maxError;
	}
	/**
	 * ���ʳ�ʼ��Ϊ0
	 */
	@Override
	public void reInitParameters() {
		//��ʼ���������ʼ��
		super.reInitParameters();
		//���ʳ�ʼ��Ϊ0
		for(int i = 0; i < stateNum; i++) {
			pi[i] = INFINITY;
			for(int j = 0; j < stateNum; j++) {
				transferProbability1[i][j] = INFINITY;
			}
			for(int k = 0; k < observationNum; k++) {
				emissionProbability[i][k] = INFINITY;
			}
		}
	}
	/**
	 * �����ʼ������PI
	 */
	public void randomInitPi() {
		for(int i = 0; i < stateNum; i++) {
			pi[i] = Math.random() * 100;
		}
		//log��һ��
		double sum = Math.log(MathUtils.sum(pi));
		for(int i =0; i < stateNum; i++) {
			if(pi[i] == 0) {
				pi[i] = INFINITY;
				continue;
			}
			pi[i] = Math.log(pi[i]) - sum;
		}
	}
	/**
	 * �����ʼ������A
	 */
	public void randomInitA() {
		for(int i = 0; i < stateNum; i++) {
			for(int j = 0; j < stateNum; j++) {
				transferProbability1[i][j] = Math.random()*100;;
			}
			double sum = Math.log(MathUtils.sum(transferProbability1[i]));
			for(int k = 0; k < stateNum; k++) {
				if(transferProbability1[i][k] == 0) {
					transferProbability1[i][k] = INFINITY;
					continue;
				}
				transferProbability1[i][k]  = Math.log(transferProbability1[i][k]) - sum;
			}
		}
	}
	/**
	 * �����ʼ������B
	 */
	public void randomInitB() {
		for(int i = 0; i < stateNum; i++) {
			for(int j = 0; j < observationNum; j++) {
				emissionProbability[i][j] = Math.random()*100;;
			}
			double sum = Math.log(MathUtils.sum(emissionProbability[i]));
			for(int k = 0; k < observationNum; k++) {
				if(emissionProbability[i][k] == 0) {
					emissionProbability[i][k] = INFINITY;
					continue;
				}
				emissionProbability[i][k]  = Math.log(emissionProbability[i][k]) - sum;
			}
		}
	}
	
	/**
	 * �����ʼ�����в���
	 */
	public void randomInitAllParameters() {
		randomInitA();
		randomInitB();
		randomInitPi();
	}
	
	/**
	 * ǰ���㷨�����ݵ�ǰ�����˼����
	 * ����һ�����г���*״̬���ȵľ���
	 * �Ѽ�⣬Ӧ��û������
	 */
	protected void calcAlpha(List<SequenceNode> nodes, double[][] alpha) {
		logger.info("����alpha...");
		long start = System.currentTimeMillis();
		//double[][] alpha = new double[sequenceLen][stateNum];
		//alpha t=0��ʼֵ
		for(int i = 0; i < stateNum; i++) {
			alpha[0][i] = pi[i] + emissionProbability[i][nodes.get(0).getNodeIndex()];
		}
		double[] logProbaArr = new double[stateNum];
		for(int t = 1; t < sequenceLen; t++) {
			for(int i = 0; i < stateNum; i++) {
				for(int j = 0; j < stateNum; j++) {
					logProbaArr[j]	= (alpha[t -1][j] + transferProbability1[j][i]);
				}
				alpha[t][i] = logSum(logProbaArr) + emissionProbability[i][nodes.get(t).getNodeIndex()];
			}
		}
		long end = System.currentTimeMillis();
		logger.info("�������...��ʱ:"+ (end - start) +"����");
		//return alpha;
	}
	/**
	 * �����㷨�����ݵ�ǰ�����˼����
	 * 
	 * @param nodes
	 */
	protected void calcBeta(List<SequenceNode> nodes, double[][] beta) {
		logger.info("����beta...");
		long start = System.currentTimeMillis();
		//double[][] beta = new double[sequenceLen][stateNum];
		//��ʼ����beta[T][i] = 1
		for(int i = 0; i < stateNum; i++) {
			beta[sequenceLen-1][i] = 1;
		}
		double[] logProbaArr = new double[stateNum];
		for(int t = sequenceLen -2; t >= 0; t--) {
			for(int i = 0; i < stateNum; i++) {
				for(int j = 0; j < stateNum; j++) {
					logProbaArr[j] = transferProbability1[i][j] + 
							emissionProbability[j][nodes.get(t+1).getNodeIndex()] +
							beta[t + 1][j];
				}
				beta[t][i] = logSum(logProbaArr);
			}
		}
		long end = System.currentTimeMillis();
		logger.info("�������...��ʱ:"+ (end - start) +"����");
		//return beta;
	}
	
	/**
	 * ���ݵ�ǰ�����˼����
	 * @param nodes �۲���
	 * @param alpha ǰ�����
	 * @param beta �������
	 */
	protected void calcKsi(List<SequenceNode> nodes, double[][] alpha, double[][] beta, double[][][] ksi) {
		logger.info("����ksi...");
		long start = System.currentTimeMillis();
		//double[][][] ksi = new double[sequenceLen][stateNum][stateNum];
		double[] logProbaArr = new double[stateNum * stateNum];
		for(int t = 0; t < sequenceLen -1; t++) {
			int k = 0;
			for(int i = 0; i < stateNum; i++) {
				for(int j = 0; j < stateNum; j++) {
					ksi[t][i][j] = alpha[t][i] + transferProbability1[i][j] +
							emissionProbability[j][nodes.get(t+1).getNodeIndex()]+beta[t+1][j];
					logProbaArr[k++] = ksi[t][i][j];
				}
			}
			double logSum = logSum(logProbaArr);//��ĸ
			for(int i = 0; i < stateNum; i++) {
				for(int j = 0; j < stateNum; j++) {
					ksi[t][i][j] -= logSum;//���ӳ���ĸ
				}
			}
		}
		long end = System.currentTimeMillis();
		logger.info("�������...��ʱ:"+ (end - start) +"����");
		//return ksi;
	}
	
	/**
	 * ���ݵ�ǰ�����ˣ������
	 * @param nodes
	 */
	protected void calcGamma(List<SequenceNode> nodes, double[][] alpha, double[][] beta, double[][] gamma) {
		logger.info("����gamma...");
		long start = System.currentTimeMillis();
		//double[][] gamma  = new double[sequenceLen][stateNum];
		for(int t = 0; t < sequenceLen; t++) {
			//��ĸ��Ҫ��LogSum
			for(int i = 0; i < stateNum; i++) {
				gamma[t][i] = alpha[t][i] + beta[t][i];
			}
			double logSum = logSum(gamma[t]);//��ĸ����
			for(int j = 0; j < stateNum; j++) {
				gamma[t][j] = gamma[t][j] - logSum;
			}
		}
		long end = System.currentTimeMillis();
		logger.info("�������...��ʱ:"+ (end - start) +"����");
		//return gamma;
	}
	
	/**
	 * ���²���
	 */
	protected void updateLambda(List<SequenceNode> nodes ,double[][] gamma, double[][][] ksi) {
		//˳����Եߵ�
		updatePi(gamma);
		updateA(ksi, gamma);
		updateB(nodes, gamma);
	}
	
	/**
	 * ���²���pi
	 * @param gamma
	 */
	public void updatePi(double[][] gamma) {
		//����HMM�еĲ���pi
		for(int i = 0; i < stateNum; i++) {
			pi[i] = gamma[0][i];
		}
	}
	/**
	 * ���²���A
	 * @param ksi
	 * @param gamma
	 */
	protected void updateA(double[][][] ksi, double[][] gamma) {
		logger.info("���²���ת�Ƹ���A...");
		////�����ڸ���A��Ҫ�õ��Բ�ͬ״̬��ǰT-1��gammaֵ��ͣ�������������
		double[] gammaSum = new double[stateNum];
		double[] tmp = new double[sequenceLen -1];
		for(int i = 0; i < stateNum; i++) {
			for(int t = 0; t < sequenceLen -1; t++) {
				tmp[t] = gamma[t][i];
			}
			gammaSum[i]  = logSum(tmp);
		}
		long start1 = System.currentTimeMillis();
		//����HMM�еĲ���A
		double[] ksiLogProbArr = new double[sequenceLen - 1];
		for(int i = 0; i < stateNum; i++) {
			for(int j = 0; j < stateNum; j++) {
				for(int t = 0; t < sequenceLen -1; t++) {
					ksiLogProbArr[t] = ksi[t][i][j];
				}
				transferProbability1[i][j] = logSum(ksiLogProbArr) - gammaSum[i];
			}
		}
		long end1 = System.currentTimeMillis();
		logger.info("�������...��ʱ:"+(end1 - start1)+"����");
	}
	/**
	 * ���²���B
	 * @param nodes
	 * @param gamma
	 */
	protected void updateB(List<SequenceNode> nodes, double[][] gamma) {
		//������Ҫ�õ�gamma���Ϊ�˼����ظ����㣬����ֱ���ȼ���
		//�����ڸ���Bʱ��Ҫ�õ��Բ�ͬ״̬������gammaֵ��ͣ�������������
		double[] gammaSum2 = new double[stateNum];
		double[] tmp2 = new double[sequenceLen];
		for(int i = 0; i < stateNum; i++) {
			for(int t = 0; t < sequenceLen; t++) {
				tmp2[t] = gamma[t][i];
			}
			gammaSum2[i]  = logSum(tmp2);
		}
		logger.info("����״̬�·ֲ�����B...");
		long start2 = System.currentTimeMillis();
		ArrayList<Double> valid = new ArrayList<Double>();
		for(int i = 0; i < stateNum; i++) {
			for(int k = 0; k < observationNum; k++) {
				valid.clear();//��������û�г�ʼ������˼�����������
				for(int t = 0; t < sequenceLen; t++) {
					if(nodes.get(t).getNodeIndex() == k) {
						valid.add(gamma[t][i]);
					}
				}
				//B[i][k]��i״̬��k�ķֲ�Ϊ����0��
				if(valid.size() == 0) {
					emissionProbability[i][k] = INFINITY;
					continue;
				}
				//�Է�����logSum
				double[] validArr = new double[valid.size()];
				for(int q = 0; q < valid.size(); q++) {
					validArr[q] = valid.get(q);
				}
				double validSum = logSum(validArr);
				//��ĸ��logSum�Ѿ�����������
				emissionProbability[i][k] = validSum - gammaSum2[i];
			}
		}
		long end2 = System.currentTimeMillis();
		logger.info("�������...��ʱ:"+(end2 - start2)+"����");
	}
	
	/**
	 * ����B�ĸ���ʱ���ʱ�ģ����Բ��ò��еķ�ʽ
	 * @param nodes
	 * @param gamma
	 */
	protected void parallelUpdateB(List<SequenceNode> nodes, double[][] gamma){
		//������Ҫ�õ�gamma���Ϊ�˼����ظ����㣬����ֱ���ȼ���
		//�����ڸ���Bʱ��Ҫ�õ��Բ�ͬ״̬������gammaֵ��ͣ�������������
		double[] gammaSum2 = new double[stateNum];
		double[] tmp2 = new double[sequenceLen];
		for(int i = 0; i < stateNum; i++) {
			for(int t = 0; t < sequenceLen; t++) {
				tmp2[t] = gamma[t][i];
			}
			gammaSum2[i]  = logSum(tmp2);
		}
		logger.info("����״̬�·ֲ�����B...");
		long start2 = System.currentTimeMillis();
		List<Thread> threads = new ArrayList<>();
		for(int i = 0; i < stateNum; i++) {
			Thread thread = new Thread(new BUpdaterThreadInDifferentState(this, nodes, gamma, i, gammaSum2));
			//����
			thread.start();
			threads.add(thread);
		}
		for(int i = 0; i < stateNum; i++) {
			try {
				threads.get(i).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		long end2 = System.currentTimeMillis();
		logger.info("�������...��ʱ:"+(end2 - start2)+"����");
	}
	/**
	 * parallelUpdateB���м��㲿��
	 * @param nodes
	 * @param gamma
	 * @param i
	 * @param gammaSum2
	 */
	protected void updateBinSpecificState(List<SequenceNode> nodes, double[][] gamma, int i, double[] gammaSum2) {
		List<Double> valid = new ArrayList<>();
		for(int k = 0; k < observationNum; k++) {
			valid.clear();//��������û�г�ʼ������˼�����������
			for(int t = 0; t < sequenceLen; t++) {
				if(nodes.get(t).getNodeIndex() == k) {
					valid.add(gamma[t][i]);
				}
			}
			//B[i][k]��i״̬��k�ķֲ�Ϊ����0��
			if(valid.size() == 0) {
				emissionProbability[i][k] = INFINITY;
				continue;
			}
			//�Է�����logSum
			double[] validArr = new double[valid.size()];
			for(int q = 0; q < valid.size(); q++) {
				validArr[q] = valid.get(q);
			}
			double validSum = logSum(validArr);
			//��ĸ��logSum�Ѿ�����������
			emissionProbability[i][k] = validSum - gammaSum2[i];
		}
	}
	/**
	 * ���и��²���HMM����
	 * @param nodes
	 * @param gamma
	 * @param ksi
	 */
	protected void parallelUpdateLambda(List<SequenceNode> nodes ,double[][] gamma, double[][][] ksi) {
		updatePi(gamma);
		Thread thread = new Thread(new AUpdaterThread(this, gamma, ksi));
		thread.start();
		//updateA(ksi, gamma);
		parallelUpdateB(nodes, gamma);
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * logSum���㼼��
	 * @param tmp
	 * @return
	 */
	public double logSum(double[] logProbaArr) {
		if(logProbaArr.length == 0) {
			return INFINITY;
		}
		double max = MathUtils.max(logProbaArr);
		double result = 0;
		for(int i = 0; i < logProbaArr.length; i++) {
			result += Math.exp(logProbaArr[i] - max);
		}
		return max + Math.log(result);
	}
	/**
	 * �����������pi
	 * ���봫��ȡ������ĸ���
	 * @param pi
	 */
	public void setPriorPi(double[] pi){
		this.pi = pi;
	}
	/**
	 * ��������ת�Ƹ���A
	 * ���봫��ȡ�����ĸ���
	 * @param trtransferProbability1
	 */
	public void setPriorTransferProbability1(double[][] trtransferProbability1){
		this.transferProbability1 = trtransferProbability1;
	}
	/**
	 * ��������״̬�µĹ۲�ֲ����ʣ�B
	 * ���봫��ȡ�����ĸ���
	 * @param emissionProbability
	 */
	public void setPriorEmissionProbability(double[][] emissionProbability) {
		this.emissionProbability = emissionProbability;
	}
}		

