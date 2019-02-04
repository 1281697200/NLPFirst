package com.outsider.nlp.postagger;

import com.outsider.common.util.Storable;
import com.outsider.model.SequenceModel;
import com.outsider.nlp.lexicalanalyzer.LexicalAnalysisResult;

/**
 * ���Ա�ע�ӿ�
 * @author outsider
 * �̳�Storable��SequenceModel��Ϊ�˱�¶�ӿڣ�����Ҳ����Ӧ��ȱ��
 */
public interface POSTagger extends Storable, SequenceModel, POSTaggingPredictor{
	/**
	 * ֱ�Ӵ��봦��õ����ݣ�LexicalAnalysisResult�з�װ�˴�������Ͷ�Ӧ�Ĵ�������
	 * @param result
	 */
	void train(LexicalAnalysisResult result);
}
