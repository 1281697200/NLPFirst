package com.outsider.common.util;

/**
 * ���ڶ������÷��䱣���������Ժͼ��ض��������
 * @author outsider
 *
 */
public interface Storable {
	/**
	 * �����������Ե�ָ����Ŀ¼
	 * ���ǵ��ö�������ɸ��࣬����ҲҪ���游������ԣ����ǲ�����ʵ�ֵĽӿ��еĳ���
	 * ����ָ���ļ�����Ĭ������
	 * @param directory Ŀ¼
	 * @param fileName �ļ���
	 */
	void save(String directory, String fileName);
	/**
	 * ��ĳ���ļ��м��ض�������Բ���ֵ���������࣬�������ӿ��е��ֶ�
	 * @param directory Ŀ¼
	 * @param fileName �ļ��������ļ���Ϊnullʹ�����ɵ�Ĭ���ļ���
	 */
	void open(String directory, String fileName);
	
}
