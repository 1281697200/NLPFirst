package com.outsider.nlp.segmenter;

import com.zhifac.crf4j.Tagger;

/**
 * ΪCrfpp�����һ���ִʹ�����
 * @author outsider
 *
 */
public class SegmentationUtils4Crfpp {
	/**
	 * �ִʷ�������ע���������BMES����д
	 * @param sentence ����
	 * @param tagger crfpp��ע��
	 * @return
	 */
	public static String[] seg(String sentence, Tagger tagger) {
		//���֮ǰ�Ĵ�Ԥ������
		tagger.clear();
		//��Ӵ�Ԥ������
		for(int i = 0; i < sentence.length(); i++) {
			tagger.add(sentence.charAt(i) + "");
		}
		//Ԥ��
		tagger.parse();
		//ת��Ϊchar���������
		char[] predict = new char[sentence.length()];
		for(int i = 0; i < predict.length; i++) {
			int yInt = tagger.y(i);
			char yName = tagger.yname(yInt).charAt(0);
			predict[i] = yName;
		}
		//����
		return SegmentationUtils.decode(predict, sentence);
	}
}
