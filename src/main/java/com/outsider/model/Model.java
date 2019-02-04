package com.outsider.model;
/**
 * ����ģ�͵Ľӿ�
 * @author outsider
 *
 * @param <TrainDataType> ѵ����������
 * @param <PredictionDataType> Ԥ����������
 * @param <PredictionResultType> Ԥ��������
 */
public interface Model<TrainDataType,PredictionDataType,PredictionResultType> {
	/**
	 * ����ģ�͵Ļ���ѵ������
	 * @param data
	 */
	void train(TrainDataType data);
	/**
	 * ����ģ�͵Ļ���Ԥ�ⷽ��
	 * @param predictionData
	 * @return
	 */
	PredictionResultType predict(PredictionDataType predictionData);
	/**
	 * ���³�ʼ��ģ�͵Ĳ�����
	 * �ڲ���ģ��ʱ�����õ�
	 */
	void reInitParameters();
}
