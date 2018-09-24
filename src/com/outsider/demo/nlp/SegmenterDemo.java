package com.outsider.demo.nlp;

import java.util.Arrays;

import com.outsider.nlp.segmenter.FirstOrderHMMSegmenter;
import com.outsider.nlp.segmenter.Segmenter;
import com.outsider.nlp.segmenter.StaticSegmenter;

public class SegmenterDemo {
	public static void demo() {
	}
	
	public static void main(String[] args) {
		//һ��ģ��
		Segmenter segmenter = StaticSegmenter.getSegmenter();
		String test = "ԭ���⣺��ý�ĵ����ֳ�����һĻ" + 
				"���ձ���������NNN��9��8�ձ�������ǰ���ձ������������������ս��֮һ��ֱ������ĸ���Ӻء������Ϻ�����ʱ��������й�����ս���ֽ����ټ��ӡ�" ; 
		String[] terms = segmenter.predictAndReturnTerms(test);
		System.out.println(Arrays.toString(terms));
		//����ģ��
		Segmenter seondOrderHMMSegmenter = StaticSegmenter.getSecondOrderHMMSegmenter();
		String[] result = seondOrderHMMSegmenter.predictAndReturnTerms(test);
		System.out.println(Arrays.toString(result));
		//���Ա�ע�����£�Ӧ��Ҫ���˶��
		
		String ss = "ũ����߼��ߣ����ǽ���Ӧ�����յ�500�������/ƽ���ס�";
		String[] s = seondOrderHMMSegmenter.predictAndReturnTerms(ss);
		System.out.println(Arrays.toString(s));
		
		String[] s2 = segmenter.predictAndReturnTerms(ss);
		System.out.println(Arrays.toString(s2));
	}
}
