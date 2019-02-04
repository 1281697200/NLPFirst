package com.outsider.model.data;

public interface DataConverter<DataType,ConvertedType> {
	/**
	 * ��ԭʼ����ת��Ϊ
	 * @param rawData ԭʼ����
	 * @param otherParameters �����õ�����������
	 * @return
	 */
	ConvertedType convert(DataType rawData, Object...otherParameters);
}
