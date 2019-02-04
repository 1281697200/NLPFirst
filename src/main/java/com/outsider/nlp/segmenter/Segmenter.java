package com.outsider.nlp.segmenter;

import java.util.ArrayList;
import java.util.List;

import com.outsider.common.util.Storable;
import com.outsider.model.SequenceModel;
/**
 * �����ķִʽӿ�
 * ����̳�SequenceModel��Ϊ�˱�¶SequenceModel�еķ���
 * @author outsider
 *
 */
public interface Segmenter extends Storable,SequenceModel, SegmentationPredictor{
	/**
	 * ��������ѵ����
	 * @param words �Ѿ��ֺôʵĴ�����
	 */
	void train(String[] words);
	/**
	 * ��������ѵ����ָ���Զ��������ת����
	 * ���ָ����ת��������ô�ͱ���ָ����Ӧ�Ľ�����
	 * ���Դ˷���������
	 * �ϳ�
	 * @deprecated
	 * @param words
	 * @param converter
	 */
	//void train(String[] words, DataConverter converter);
	/**
	 * �������ѵ��
	 * ������SequenceModel�ִ�����ͬ����������ΪList<SequenceNode>������˳�ͻ
	 * ����ֻ����ʱ��ЭΪArrayList
	 * @param multiWords
	 */
	void train(ArrayList<String[]> corpuses);
	/**
	 * ������ϣ�ָ��ת����
	 * �ϳ�����ͬ��
	 * @deprecated
	 * @param multiWords
	 * @param converter 
	 */
	//void train(List<String[]> multiWords, DataConverter converter);
}
