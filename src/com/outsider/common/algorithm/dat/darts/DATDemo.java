package com.outsider.common.algorithm.dat.darts;

import java.io.IOException;

public class DATDemo {
	/**
	 * ��������ʹ��
	 * @param args
	 * @throws IOException
	 */
	@Deprecated
	public static void main(String[] args) throws IOException {
		/**
		 * �����ʵ䣬���밴���ֵ���
		 */
		/*List<String> words = new ArrayList<>();
		words = IOUtils.readTextAndReturnLines("D:\\nlp����\\���Ա�ע\\dictionary2014&1998.txt", "utf-8");
		ChineseDictionarySortUtil.sortEncodingUtf8(words);
		System.out.println("�ֵ������" + words.size());
		DoubleArrayTrie dat = new DoubleArrayTrie();
        System.out.println("�Ƿ����: " + dat.build(words));
        //�Բۣ����밴���ֵ��򣬲�Ȼ����
        Set<Integer> ids = new HashSet<>();
        for(String word : words) {
        	ids.add(dat.exactMatchSearch(word));
        }
        dat.save("./model/dictionary/dic2014&1998dat");
        System.out.println(dat.exactMatchSearch("��"));
        System.out.println("��������:"+words.size());
        System.out.println("id����:"+ids.size());*/
		/**
		 * ���شʵ�
		 */
		/*DoubleArrayTrie dat = new DoubleArrayTrie();
		dat.open("./model/dictionary/dic2014&1998dat");*/
	}
}
