package com.outsider.model.data;

public interface DataConverter<DataType,ConvertedType> {
	/**
	 * ��ԭʼ����ת��Ϊ
	 * @param rawData ԭʼ����
	 * @param otherParameters �����õ�����������
	 * @return
	 */
	ConvertedType rawData2ConvertedData(DataType rawData, Object...otherParameters);
}
