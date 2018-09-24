package com.outsider.model.hmm;

import java.io.File;
import java.util.List;
import java.util.Observable;

import com.outsider.common.config.Config;
import com.outsider.common.util.IOUtils;
import com.outsider.common.util.MathUtils;

/**
 * ͨ�üලѧϰHMM�㷨��ʵ��
 * @author outsider
 * @version v1.0
 * @Date 2018.9.6
 *
 */
public class FirstOrderGeneralHMM extends AbstractHMM{
	/**��ʼ״̬����**/
	protected double[] pi;
	/**ת�Ƹ���**/
	protected double[][] transferProbability1;
	/**�������**/
	//TODO ��ʱû��ϡ��洢����
	protected double[][] emissionProbability;
	/**���������**/
	public static final double INFINITY = (double) -Math.pow(2, 31);
	/**
	 * ���캯��
	 * @param stateNum ״ֵ̬���ϴ�С
	 * @param observationNum �۲�ֵ���ϴ�С
	 */
	public FirstOrderGeneralHMM(int stateNum, int observationNum) {
		super(stateNum, observationNum);
		//��ʼ��pi��transferProbability1��emissionProbability
		this.pi = new double[stateNum];
		this.transferProbability1 = new double[stateNum][stateNum];
		this.emissionProbability = new double[stateNum][observationNum];
	}
	
	public FirstOrderGeneralHMM() {
		super();
	}

	/**
	 * ֱ�ӹ����Ѿ�ѵ���õ�ģ��
	 * @param stateNum ״̬���ϴ�С
	 * @param observationNum �۲⼯�ϴ�С
	 * @param pi ��ʼ״̬����
	 * @param transferProbability1 ת�Ƹ��ʾ���
	 * @param emissionProbability ������ʾ���
	 */
	public FirstOrderGeneralHMM(int stateNum, int observationNum, double[] pi, double[][] transferProbability1, double[][] emissionProbability) {
		super(stateNum, observationNum);
		this.pi = pi;
		this.transferProbability1 = transferProbability1;
		this.emissionProbability = emissionProbability;
	}
	
	/**
	 * ͳ��������
	 * @param nodes ���нڵ�
	 */
	@Override
	protected void solve(List<SequenceNode> nodes) {
		//�������п�ʼѵ��
		pi[nodes.get(0).getState()]++;
		emissionProbability[nodes.get(0).getState()][nodes.get(0).getNodeIndex()]++;
		for(int i = 1;i < nodes.size(); i++) {
			SequenceNode node = nodes.get(i);
			//״̬ͳ��
			pi[node.getState()]++;
			//״̬ת��ͳ��
			transferProbability1[nodes.get(i-1).getState()][node.getState()]++;
			//״̬�¹۲�ֲ�ͳ��
			emissionProbability[node.getState()][node.getNodeIndex()]++;
		}
	}
	/**
	 * ����ȡ����
	 */
	protected void logProbability() {
		double piSum  = 0;
		double[] aSum = new double[stateNum];
		double[] bSum = new double[stateNum];
		piSum = MathUtils.sum(pi);
		for(int i = 0; i < stateNum; i++) {
			//piSum += pi[i];
			/*for(int j = 0; j < stateNum; j++) {
				aSum[i] += transferProbability1[i][j];
			}*/
			//ȡ��ԭ������ͣ��������
			aSum[i] = MathUtils.sum(transferProbability1[i]);
			/*for(int k = 0; k < observationNum; k++) {
				bSum[i] += emissionProbability[i][k];
			}*/
			bSum[i] = MathUtils.sum(emissionProbability[i]);
		}
		for(int i = 0; i < stateNum; i++) {
			if(pi[i] != 0) {
				pi[i] = (double) (Math.log(pi[i]) - Math.log(piSum));
			} else {
				pi[i] = INFINITY;
			}
			for(int j = 0; j < stateNum; j++) {
				if(transferProbability1[i][j] !=0) {
					transferProbability1[i][j] = (double) (Math.log(transferProbability1[i][j]) - Math.log(aSum[i]));
				} else {
					transferProbability1[i][j] = INFINITY;
				}
			}
			for(int k = 0; k < observationNum; k++) {
				if(emissionProbability[i][k] !=0) {
					emissionProbability[i][k] = (double) (Math.log(emissionProbability[i][k]) - Math.log(bSum[i]));
				} else {
					emissionProbability[i][k] = INFINITY;
				}
			}
		}
	}
	
	/**
	 * ά�رȽ���
	 * @param O �۲�����,������Ǿ������봦��ģ�������ԭʼ���ݣ�
	 * ���磬����������ַ�������ô���������һϵ�е��ַ��ı���������ַ�����
	 * @return ����Ԥ������
	 */
	public int[] verterbi(int[] O) {
		double[][] deltas = new double[O.length][this.stateNum];
		//����deltas[t][i]��ֵ������һ���ĸ�״̬������
		int[][] states = new int[O.length][this.stateNum];
		//��ʼ��deltas[0][]
		for(int i = 0;i < this.stateNum; i++) {
			//TODO
			if(O[0]!=-1)
				deltas[0][i] = pi[i] + emissionProbability[i][O[0]];
			else
				deltas[0][i] = pi[i];
		}
		//����deltas
		for(int t = 1; t < O.length; t++) {
			for(int i = 0; i < this.stateNum; i++) {
				deltas[t][i] = deltas[t-1][0]+transferProbability1[0][i];
				for(int j = 1; j < this.stateNum; j++) {
					double tmp = deltas[t-1][j]+transferProbability1[j][i];
					if (tmp > deltas[t][i]) {
						deltas[t][i] = tmp;
						states[t][i] = j;
					}
				}
				/**
				 * ������Ϊ����ʵĲ����ں�����O[t]����ʲôӰ�컹�д�����
				 */
				if(O[t]!=-1)
					deltas[t][i] += emissionProbability[i][O[t]];
			}
		}
		//�����ҵ�����·��
		int[] predict = new int[O.length];
		double max = deltas[O.length-1][0];
		for(int i = 1; i < this.stateNum; i++) {
			if(deltas[O.length-1][i] > max) {
				max = deltas[O.length-1][i];
				predict[O.length-1] = i;				
			}
		}
		for(int i = O.length-2;i >= 0;i-- ) {
			predict[i] = states[i+1][predict[i+1]];
		}
		return predict;
	}
	@Override
	public void saveModel(String directory) {
		if(directory == null || directory.trim().equals("")) {
			directory = Config.MODEL_BASE_PATH+this.getClass().getSimpleName()+"/";
			File file = new File(directory);
			if(!file.exists()) {
				file.mkdir();
			}
		}
		IOUtils.writeObject2File(directory+"pi", pi);
		IOUtils.writeObject2File(directory+"transferProbability1", transferProbability1);
		IOUtils.writeObject2File(directory+"emissionProbability", emissionProbability);
		IOUtils.writeObject2File(directory+"stateNum", stateNum);
		IOUtils.writeObject2File(directory+"observationNum", observationNum);
	}
	@Override
	public Object loadModel(String directory) {
		if(directory == null || directory.trim().equals("")) {
			directory = Config.MODEL_BASE_PATH+this.getClass().getSimpleName()+"/";
		}
		pi = (double[]) IOUtils.readObject(directory+"pi");
		transferProbability1 = (double[][]) IOUtils.readObject(directory+"transferProbability1");
		emissionProbability = (double[][]) IOUtils.readObject(directory+"emissionProbability");
		stateNum = (int) IOUtils.readObject(directory+"stateNum");
		observationNum = (int) IOUtils.readObject(directory+"observationNum");
		return this;
	}

	@Override
	public void initParameter() {
		this.pi = new double[stateNum];
		this.transferProbability1 = new double[stateNum][stateNum];
		this.emissionProbability = new double[stateNum][observationNum];
	}
}
