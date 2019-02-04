package com.outsider.nlp.segmenter;

import java.util.Arrays;
import java.util.List;

import com.outsider.common.util.IOUtils;

public class TestSeg {
	public static void testSeg(SegmentationPredictor segmenter) {
		List<String> sentences = IOUtils.readTextAndReturnLines("./src/test/resources/sentences4segmentation.txt", "gbk");
		long time = 0;//4.3953996
		long len = 0;
		for(String sentence : sentences) {
			long start = System.currentTimeMillis();
			String[] res = segmenter.seg(sentence);
			long end = System.currentTimeMillis();
			time += (end - start);
			len += sentence.length();
			System.out.println(Arrays.toString(res));
		}
		System.out.println("��ʱ:"+time+"����");
	}
	
	public static void testSeg2(Segmenter segmenter) {
		String s1 = "������أ�ǿ���������Ϻ�ɫ���壬������ˮ�����Ҵ�������ԭ����������������ˮ����������������Ư�׼����������ռ���������̼���Ƽ��ȡ�";
		String s2 = "��ҹ������ӡ�ͨ������ǳ�ݵ���Ů�ڰ�ҹ�������ӵ��龰,���������߶���������������С�";
		String s3 = "���������������������ι���ͳ�ƾֵ�����ǡ�";
		String[] ss = new String[] {s1,s2,s3};
		List<String[]> result = segmenter.seg(ss);
		for(String[] res : result) {
			for(String word : res) {
				System.out.print(word+"|");
			}
			System.out.println();
		}
	}
}
