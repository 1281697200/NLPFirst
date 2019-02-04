package com.outsider.nlp.dictionary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.outsider.common.util.IOUtils;

/**
 * ���Ա�ע�ʵ�
 * δʹ��
 * @author outsider
 *
 */
public class POSTaggerDictionary extends Dictionary{
	//��������
	private int wordNatureNum;
	//�������ֵ�int��ӳ��
	private Map<String,Integer> natureName2Int;
	//int�����Ե�ӳ��
	private String[] int2NatureName;
	public POSTaggerDictionary() {
	}
	
	public int getWordNatureNum() {
		return wordNatureNum;
	}


	public Map<String, Integer> getNatureName2Int() {
		return natureName2Int;
	}


	public String[] getInt2NatureName() {
		return int2NatureName;
	}


	/*
	 * ��������ӳ��
	 */
	public void buildWordNature(String[] natures) {
		this.wordNatureNum = natures.length;
		natureName2Int = new HashMap<>();
		int2NatureName = new String[natures.length];
		for(int i = 0; i < natures.length; i++) {
			natureName2Int.put(natures[i], i);
			int2NatureName[i] = natures[i];
		}
	}
	/**
	 * ��ȡ��������int��ӳ��
	 * @return
	 */
	/*public Map<String,Integer> getNatureName2IntMapping() {
		return natureName2Int;
	}*/
	/**
	 * ��ȡint����������ӳ��
	 * @return
	 */
	/*public String[] getInt2NatureNameMapping() {
		return int2NatureName;
	}*/
	public static void main(String[] args) {
		List<String> words = IOUtils.readTextAndReturnLines("D:\\nlp����\\���Ա�ע\\dictionary2014&1998.txt", "utf-8");
		String[] natures = IOUtils.readTextAndReturnLinesOfArray("D:\\nlp����\\���Ա�ע\\wordNature.txt", "utf-8");
		//��������
		POSTaggerDictionary dictionary = new POSTaggerDictionary();
		//dictionary.build(words, true, null);
		//dictionary.buildWordNature(natures);
		//dictionary.save("D:\\nlp����\\���Ա�ע",null);
		dictionary.open("D:\\nlp����\\���Ա�ע", null);
		int a  = dictionary.getDictionary().intIdOf("���");
		System.out.println(a);
		//�������ˣ��������л�ȡ�ֶβ��ܻ�ȡ��������ֶΣ���Ҫ����
	}
}
