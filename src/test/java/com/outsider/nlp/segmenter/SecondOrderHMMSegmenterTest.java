package com.outsider.nlp.segmenter;

import java.util.Arrays;

import com.outsider.common.util.IOUtils;
import com.outsider.constants.nlp.PathConstans;

public class SecondOrderHMMSegmenterTest {
	public static void main(String[] args) {
		//ʹ��ʾ��
		use();
		//ѵ��ʾ��
		//train();
		//[ԭ����, ��, ��ý, �ĵ�, ��, �ֳ�, ����, һĻ, ��, �ձ�, ������, ��, NNN, ��, 9��, 8��, ����, ��, ��ǰ, ��, �ձ�, ����, ������, ����, ���, ս��, ֮, һ, ��, ֱ��, ����ĸ, ��, �Ӻ�, ��, ��, ��, �Ϻ�, ����, ʱ, ��, ��, ��, ��, �й�, ����, ս��, �ֽ�, ����, ����, ��]
	}
	public static void use() {
		Segmenter segmenter = new SecondOrderHMMSegmenter();
		segmenter.open(PathConstans.SEOND_ORDER_HMM_SEGMENTER, null);
		TestSeg.testSeg(segmenter);
	}
	/**
	 * ѵ��ʾ��
	 */
	public static void train() {
		Segmenter segmenter = new SecondOrderHMMSegmenter();
		String basePath = "./data/segmentation";
		String pku = basePath + "pku_training.splitBy2space.utf8";
		String sku = basePath+"sku_train.utf8.splitBy2space.txt";
		String ctb6 = basePath+"ctb6.train.seg.utf8.splitBy1space.txt";
		String cityu = basePath+"cityu_training.utf8.splitBy1space.txt";
		String[] words = IOUtils.loadMultiSegmentionCorpus(new String[] {pku,sku,ctb6,cityu}, new String[] {"utf-8","utf-8","utf-8","utf-8"}, new String[] {"  ","  "," "," "});
		segmenter.train(words);
		segmenter.save("./model/SecondOrderHMMSegmenter", null);
		String s = "HanLP����һϵ��ģ�����㷨��ɵ�Java���߰���Ŀ�����ռ���Ȼ���Դ��������������е�Ӧ�á�";
		String s2 = "������أ�ǿ���������Ϻ�ɫ���壬������ˮ�����Ҵ�������ԭ����������������ˮ����������������Ư�׼����������ռ���������̼���Ƽ��ȡ�";
		String str = "ԭ���⣺��ý�ĵ����ֳ�����һĻ" + 
				"���ձ���������NNN��9��8�ձ�������ǰ���ձ������������������ս��֮һ��ֱ������ĸ���Ӻء������Ϻ�����ʱ��������й�����ս���ֽ����ټ��ӡ�" ; 
		
		SegmenterTest.score(segmenter, "SOHMMSegmenter");
		String[] res = segmenter.seg(s);
		System.out.println(Arrays.toString(res));
		String[] res2 = segmenter.seg(s2);
		System.out.println(Arrays.toString(res2));
		String[] res3 = segmenter.seg(str);
		System.out.println(Arrays.toString(res3));
	}
}
