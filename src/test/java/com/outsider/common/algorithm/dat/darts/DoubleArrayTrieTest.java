package com.outsider.common.algorithm.dat.darts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.outsider.common.util.IOUtils;

public class DoubleArrayTrieTest {
	public static void main(String[] args) {
		/**
		 * �����ʵ䣬���밴���ֵ���
		 */
		List<String> words = new ArrayList<String>();
		words = IOUtils.readTextAndReturnLines("D:\\nlp����\\���Ա�ע\\dictionary2014&1998.txt", "utf-8");
		for(int i = 0; i < words.size(); i++) {
			words.set(i, words.get(i).trim());
		}
		//186089
		Collections.sort(words);
		StringBuilder sb = new StringBuilder();
		for(String word : words) {
			sb.append(word+"\n");
		}
		IOUtils.writeTextData2File(sb.toString(), "D:\\\\nlp����\\\\���Ա�ע\\\\dic2014&1998.txt", "utf-8");
		System.out.println(words.get(0));
		System.out.println(words.get(words.size() - 1));
		System.out.println("�ֵ������" + words.size());
		DoubleArrayTrie dat = new DoubleArrayTrie();
        System.out.println("�Ƿ����: " + dat.build(words));
        //�Բۣ����밴���ֵ��򣬲�Ȼ����
        System.out.println("ǰ׺��������:");
        String s = "������";
        List<Integer> intids = dat.commonPrefixSearch(s);
        System.out.println("������ǰ׺��"+"\""+s+"\""+"����:"+intids.size());
        for(int i= 0; i < intids.size();i++) {
        	System.out.println(intids.get(i));
        }
        Set<Integer> ids = new HashSet<>();
        for(String word : words) {
        	ids.add(dat.exactMatchSearch(word));
        }
        
        //dat.save("C:\\Users\\outsider\\Desktop\\a");
        System.out.println("��������:"+words.size());
        System.out.println("id����:"+ids.size());
        System.out.println("getSize():"+dat.getKeySize());
        System.out.println(dat.intIdOf(words.get(0)));
        System.out.println(dat.intIdOf(words.get(words.size() - 1)));
		/**
		 * ���شʵ�
		 */
		/*DoubleArrayTrie dat = new DoubleArrayTrie();
		dat.open("./model/dictionary/dic2014&1998dat");*/
	}
}
