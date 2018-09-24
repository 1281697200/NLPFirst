package com.outsider.model.hmm;

import java.util.List;

import com.outsider.model.Model;

/**
 * �����HMMģ������
 * ͳһ������ýӿڹ淶
 * @author outsider
 *
 */
public abstract class AbstractHMM implements Model<List<SequenceNode>, Object, int[], int[]>{
	/**״ֵ̬���ϵĴ�С**/
	protected int stateNum;
	/**�۲�ֵ���ϵĴ�С**/
	protected int observationNum;
	public AbstractHMM() {}
	/**
	 * ���캯��
	 * @param stateNum ��ͬ״̬���ϵĴ�С
	 * @param observationNum ���в��ظ��Ĺ۲⼯�ϵĴ�С
	 */
	public AbstractHMM(int stateNum, int observationNum) {
		this.stateNum = stateNum;
		this.observationNum = observationNum;
	}
	/**
	 * ά�ر��㷨
	 * @param O �۲����е�����λ������
	 * @return
	 */
	protected abstract int[] verterbi(int[] O);
	/**
	 * ��������ע�ⲻ��Ը��ʽ���ȡ����
	 * @param nodes ���нڵ㼯��
	 */
	protected abstract void solve(List<SequenceNode> nodes);
	/**
	 * ֱ�Ӹ�������õ����нڵ��ѵ��ģ��
	 * ��ײ��ѵ��������ֱ��ѵ���淶�������
	 * ����train�������ն�����÷���
	 * @param sequenceNodes ���нڵ�
	 */
	@Override
	public void train(List<SequenceNode> sequenceNodes, Object...otherParameters) {
		this.solve(sequenceNodes);
		this.logProbability();
		//����ģ�͵�Ĭ��·��
		/*if(savedModel == false) {
			saveModel(null);
		}*/
	}
	/**
	 * Ԥ�ⷽ����֮����д�������ֻ��Ϊ�˶������ͳһ�ӿڹ淶
	 * ��ײ��Ԥ�ⷽ����ֱ�Ӵ���淶������
	 * ����predict�������ն�����ø÷���
	 * @param O �۲�����
	 * @return ����Ԥ����
	 */
	@Override
	public int[] predict(int[] O, Object...otherParameters) {
		
		return this.verterbi(O);
	}
	/**
	 * ��ԭʼ����ת��Ϊ���нڵ�
	 * @param rawData ԭʼ����
	 * @param otherParameters �����õ�����������
	 * @return
	 */
	//protected abstract List<SequenceNode> rawData2SequenceNodes(Object rawData, Object...otherParameters);
	/**
	 * �����������
	 */
	protected abstract void logProbability();
	/**
	 * ���³�ʼ������
	 */
	public abstract void initParameter();
}
