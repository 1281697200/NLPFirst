package com.outsider.nlp.postagger;

public interface POSTaggingPredictor {
	/**
	 * ��ע����
	 * @param words ��������
	 * @return
	 */
	String[] tag(String[] words);
}
