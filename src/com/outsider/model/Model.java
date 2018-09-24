package com.outsider.model;

/**
 * Ϊ�˷����Ժ���չ
 * ����ģ�Ͷ����ڴ�ģ�ͽӿ�
 * @author outsider
 *
 */
public interface Model<TrainDataType,ParameterType,ReturnType,PredictDataType> {
	/**
	 * �ײ��train����������ģ�ͱ���ͨ���˷�������ѵ��
	 * @param data
	 * @param otherParameters
	 */
	void train(TrainDataType data, ParameterType...otherParameters);
	/**
	 * �ײ�predict�ķ���������ģ�ͱ���ͨ���˷�������Ԥ��
	 * @param data
	 * @param otherParameters
	 * @return
	 */
	ReturnType predict(PredictDataType data, ParameterType...otherParameters);
	/**
	 * ����ģ��
	 * @param path
	 */
	void saveModel(String directory);
	/**
	 * ����ģ��
	 * @param path
	 */
	Object loadModel(String directory);
}
