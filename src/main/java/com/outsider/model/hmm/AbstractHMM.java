package com.outsider.model.hmm;

import java.io.Serializable;
import java.util.List;

import com.outsider.common.util.Storable;
import com.outsider.common.util.StorageUtils;
import com.outsider.model.Model;
import com.outsider.model.SequenceModel;

/**
 * 抽象层HMM模型描述
 * 统一对外调用接口规范
 * @author outsider
 *
 */
public abstract class AbstractHMM implements SequenceModel,Storable,Serializable{
	/**状态值集合的大小**/
	protected int stateNum;
	/**观测值集合的大小**/
	protected int observationNum;
	public AbstractHMM() {}
	/**
	 * 构造函数
	 * @param stateNum 不同状态集合的大小
	 * @param observationNum 所有不重复的观测集合的大小
	 */
	public AbstractHMM(int stateNum, int observationNum) {
		this.stateNum = stateNum;
		this.observationNum = observationNum;
	}
	/**
	 * 维特比算法
	 * @param O 观测序列的索引位置数组
	 * @return
	 */
	protected abstract int[] verterbi(int[] O);
	/**
	 * 求解参数，注意不会对概率进行取对数
	 * @param nodes 序列节点集合
	 */
	protected abstract void solve(List<SequenceNode> nodes);
	/**
	 * 直接给出处理好的序列节点后训练模型
	 * 最底层的训练方法，直接训练规范后的数据
	 * 所有train方法最终都会调用方法
	 * @param sequenceNodes 序列节点
	 */
	@Override
	public void train(List<SequenceNode> sequenceNodes) {
		this.solve(sequenceNodes);
		this.logProbability();
	}
	/**
	 * 预测方法，之所以写这个方法只是为了对外调用统一接口规范
	 * 最底层的预测方法，直接处理规范的数据
	 * 所有predict方法最终都会调用该方法
	 * @param O 观测序列
	 * @return 返回预测结果
	 */
	@Override
	public int[] predict(int[] O) {
		return this.verterbi(O);
	}
	/**
	 * 计算对数概率
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
