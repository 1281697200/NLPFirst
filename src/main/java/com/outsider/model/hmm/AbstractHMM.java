package com.outsider.model.hmm;

import java.io.Serializable;
import java.util.List;

import com.outsider.common.util.Storable;
import com.outsider.common.util.StorageUtils;
import com.outsider.model.Model;
import com.outsider.model.SequenceModel;

/**
 * �����HMMģ������
 * ͳһ������ýӿڹ淶
 * @author outsider
 *
 */
public abstract class AbstractHMM implements SequenceModel,Storable,Serializable{
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
	public void train(List<SequenceNode> sequenceNodes) {
		this.solve(sequenceNodes);
		this.logProbability();
	}
	/**
	 * Ԥ�ⷽ����֮����д�������ֻ��Ϊ�˶������ͳһ�ӿڹ淶
	 * ��ײ��Ԥ�ⷽ����ֱ�Ӵ���淶������
	 * ����predict�������ն�����ø÷���
	 * @param O �۲�����
	 * @return ����Ԥ����
	 */
	@Override
	public int[] predict(int[] O) {
		return this.verterbi(O);
	}
	/**
	 * �����������
	 */
	protected abstract void logProbability();
	
	@Override
	public void open(String directory, String fileName) {
		StorageUtils.open(directory, fileName, this);
	}
	
	@Override
	public void save(String directory, String fileName) {
		StorageUtils.save(directory, fileName, this);
	}
}
