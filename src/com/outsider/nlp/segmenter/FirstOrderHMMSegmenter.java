package com.outsider.nlp.segmenter;

import com.outsider.model.hmm.FirstOrderGeneralHMM;
/**
 * �ලѧϰ��bgram/һ��HMM���ķִ�
 * �ַ����ܵ�״̬:
 * begin middle end single (b m e s) 0 1 2 3
 * @author outsider
 *
 */
public class FirstOrderHMMSegmenter extends FirstOrderGeneralHMM implements Segmenter{

	public FirstOrderHMMSegmenter(int stateNum, int observationNum) {
		super(stateNum, observationNum);
	}
	
	public FirstOrderHMMSegmenter() {
		super();
	}

	public FirstOrderHMMSegmenter(int stateNum, int observationNum, double[] pi, double[][] transfer_probability1,
			double[][] emission_probability) {
		super(stateNum, observationNum, pi, transfer_probability1, emission_probability);
	}

	
}
