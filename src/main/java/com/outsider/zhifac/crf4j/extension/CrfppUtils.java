package com.outsider.zhifac.crf4j.extension;

import com.zhifac.crf4j.ModelImpl;
import com.zhifac.crf4j.Tagger;

public class CrfppUtils {
	/**
	 * ����ģ���ļ�����tagger
	 * @param modelFile
	 * @param nbest
	 * @param vlevel
	 * @param costFactor
	 * @return
	 */
	public static Tagger open(String modelFile, int nbest, int vlevel, double costFactor) {
		ModelImpl model = new ModelImpl();
		model.open(modelFile, nbest, vlevel, costFactor);
		return model.createTagger();
	}
	
	/**
	 * crf++һ��ͨ�õı�ע�������������ǩ�����ַ�����ǩ���
	 * @param tagger
	 * @param sequence
	 * @return
	 */
	public static String[] tag(Tagger tagger, String[] sequence) {
		//���֮ǰ�Ĵ�Ԥ������
		tagger.clear();
		//��Ӵ�Ԥ������
		for(int i = 0; i < sequence.length; i++) {
			tagger.add(sequence[i]);
		}
		//Ԥ��
		tagger.parse();
		//ת��Ϊchar���������
		String[] predict = new String[sequence.length];
		for(int i = 0; i < predict.length; i++) {
			int yInt = tagger.y(i);
			String yName = tagger.yname(yInt);
			predict[i] = yName;
		}
		return predict;
	}
}
