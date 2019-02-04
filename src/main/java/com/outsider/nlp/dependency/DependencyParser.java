package com.outsider.nlp.dependency;

import java.util.List;

public interface DependencyParser {
	/**
	 * ����䷨��������
	 * @param words ����
	 * @param natures ������
	 * @return ��������������ʽΪCoNLL��ʽ
	 */
	CoNLLSentence parse(String[] words, String[] natures);
	/**
	 * ����䷨����
	 * @param words �������
	 * @param natures ���������
	 * @return �������䷨�ִʽ������ʽΪCoNLL��ʽ
	 */
	CoNLLSentence[] parse(List<String[]> wordsOfSentences, List<String[]> naturesOfSentences);
	/**
	 * ����䷨�����������ִ����ʹ��Ա�ע��
	 * @param sentence δ����ľ���
	 * @return ���������� ����ʽΪCoNLL��ʽ
	 */
	CoNLLSentence parse(String sentence);
	/**
	 * ����䷨�����������ִ����ʹ��Ա�ע��
	 * @param sentences �������
	 * @return ���������� ����ʽΪCoNLL��ʽ
	 */
	CoNLLSentence[] parse(String[] sentences);
}
