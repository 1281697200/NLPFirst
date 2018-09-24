package com.outsider.model.hmm;

import java.io.File;
import java.util.List;

import com.outsider.common.config.Config;
import com.outsider.common.util.IOUtils;
import com.outsider.common.util.MathUtils;

public class SecondOrderGeneralHMM extends FirstOrderGeneralHMM{
	private double[][][] transferProbability2;
	public SecondOrderGeneralHMM(int stateNum, int observationNum) {
		super(stateNum, observationNum);
		this.transferProbability2 = new double[stateNum][stateNum][stateNum];
	}
	
	public SecondOrderGeneralHMM(int stateNum, int observationNum, double[] pi, double[][] transferProbability1,
			double[][] emissionProbability) {
		super(stateNum, observationNum, pi, transferProbability1, emissionProbability);
	}
	
	public SecondOrderGeneralHMM() {
		super();
	}

	@Override
	public void train(List<SequenceNode> sequenceNodes, Object... otherParameters) {
		//����HMM��Ҫ�õ�һ�׵Ĳ�������Ҫѵ������
		super.solve(sequenceNodes);//���������ø����train���ᵼ���ظ��ĸ��ʶ�����
		//ͳ�ƶ���HMM��ת�Ƹ��ʾ������
		for(int i = 2; i < sequenceNodes.size();i++) {
			transferProbability2[sequenceNodes.get(i-2).getState()]
					[sequenceNodes.get(i-1).getState()][sequenceNodes.get(i).getState()]++;
			
		}
		logProbability();
	}

	/**
	 * ����ά�ر��㷨
	 */
	@Override
	public int[] verterbi(int[] O) {
		//dp�����
		double[][][] deltas = new double[O.length-1][stateNum][stateNum];
		//���浱ǰ״̬������ֵ����һ���ĸ�״̬����
		int[][][] states = new int[O.length-1][stateNum][stateNum];
		////ǰt=2ʱ�ĵļ��㲻��ʹ��ͨ�õĵ���ʽ����������delta_t2,deltas[0][i][j]����delta_t2
		for(int i = 0; i < stateNum; i++) {
			for(int j = 0;j < stateNum; j++) {
				deltas[0][i][j] = pi[i]+emissionProbability[i][O[0]]
						+ transferProbability1[i][j] + emissionProbability[j][O[1]];
			}
		}
		//����ʣ���delta_t , t >= 3,����ĳ�ʼt=1�ʹ���������t=3
		for(int t = 1; t < O.length-1; t++) {
			for(int j = 0; j < stateNum; j++) {
				for(int k = 0; k < stateNum;k++) {
					deltas[t][j][k] = deltas[t-1][0][j]+transferProbability2[0][j][k];
					for(int i = 1; i < stateNum; i++) {
						double tmp = deltas[t-1][i][j]+transferProbability2[i][j][k];
						if(tmp > deltas[t][j][k]) {
							deltas[t][j][k] = tmp;
							states[t][j][k] = i;
						}
					}
					//��Ҫ�ر�ע�������O[t+1]�е���������t����Ϊ�����t��ʵ�ʵ�t��һ��1��ƫ����
					//֮ǰ��bug�ͳ����������Ч�����
					deltas[t][j][k] += emissionProbability[k][O[t+1]]; 
				}
			}
		}
		//���ҳ�����·��
		int[] predict = new int[O.length];
		double max = deltas[O.length-2][0][0];
		for(int i = 0; i < stateNum; i++) {
			for(int j = 0; j < stateNum;j++) {
				if(deltas[O.length - 2][i][j] > max) {
					max = deltas[O.length - 2][i][j];
					predict[O.length-2] = i;
					predict[O.length-1] = j;
				}
			}
		}
		//����
		for(int i = O.length - 3; i >= 0;i--) {
			predict[i] = states[i+1][predict[i+1]][predict[i+2]];
		}
		return predict;
	}
	

	@Override
	protected void logProbability() {
		super.logProbability();
		//��Ҫ���µ�ת�Ƹ��ʾ�����й�һ��
		//���
		double[][] sum = new double[this.stateNum][this.stateNum];
		for(int i = 0; i < this.stateNum;i++) {
			for(int j = 0; j < this.stateNum;j++) {
				/*for(int k = 0; k < this.stateNum;k++) {
					sum[i][j] += transferProbability2[i][j][k];
				}*/
				//������룬ȡ��ԭ����ѭ�����
				sum[i][j] = MathUtils.sum(transferProbability2[i][j]);
			}
		}
		//ȡ����
		for(int i = 0; i < this.stateNum;i++) {
			for(int j = 0; j < this.stateNum;j++) {
				for(int k = 0; k < this.stateNum;k++) {
					if(transferProbability2[i][j][k] == 0) {
						transferProbability2[i][j][k] = INFINITY;
					} else {
						transferProbability2[i][j][k] = Math.log(transferProbability2[i][j][k]) - Math.log(sum[i][j]);
					}
				}
			}
		}
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
		IOUtils.writeObject2File(directory+"transferProbability2", transferProbability2);
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
		transferProbability2 = (double[][][]) IOUtils.readObject(directory+"transferProbability2");
		return this;
	}
	
	@Override
	public void initParameter() {
		super.initParameter();
		this.transferProbability2 = new double[stateNum][stateNum][stateNum];
	}
}
