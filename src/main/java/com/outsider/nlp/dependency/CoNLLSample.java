package com.outsider.nlp.dependency;

import java.util.Arrays;

/**
 * һ������������2����֮�����ɵ�һ������
 * @author outsider
 *
 */
public class CoNLLSample {
	public String[] context;
	public CoNLLSample(String[] context) {
		this.context = context;
	}
	
	
	@Override
	public String toString() {
		return Arrays.toString(context);
	}
}
