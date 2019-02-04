package com.outsider.common.algorithm.daTrieTree;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.outsider.common.util.IOUtils;

public class EncondingDATest extends DATrieTree implements EncodingDATrieTreeInterface{
	private Map<Integer, Integer> unicode2smallCode;
	private List<Character> smallCode2unicode;
	
	public EncondingDATest() {
		super();
	}
	@Override
	public int code(char c) {
		return unicode2smallCode.get((int)c);
	}
	@Override
	public void build(List<String> tokens) {
		init(tokens);
		super.build(tokens);
	}
	private void init(List<String> tokens) {
		Object[] objs = encodeFromZero(tokens);
		unicode2smallCode = (Map<Integer, Integer>) objs[0];
		smallCode2unicode = (List<Character>) objs[1];
	}
	public static void main(String[] args) {
		//����
		DATrieTree da = new EncondingDATest();
		List<String> words = IOUtils.readTextAndReturnLines("D:\\nlp����\\���Ĵʿ�\\data\\��ʮ�����ʿ�.txt", "utf-8");
		long start = System.currentTimeMillis();
		System.out.println("������:"+words.size());
		//words = words.subList(0, 5000);
		da.build(words);
		long end = System.currentTimeMillis();
		System.out.println("������ʱ:"+(end - start) / 1000.0 + "�룡");
		words.forEach((e)->{
			if(da.exist(e)) {
				count++;
			} else {
				System.out.println(e);
			}
		});
		if(count!=words.size()) {
			System.out.println("����ѵ�����ڴʵ����޷��ҵ���");
			System.out.println("���ҵ�:"+count+";�ܹ�:"+words.size());
		}
		System.out.println("idOf:"+da.idOf("����"));
		count = 0;
		//����id�Ƿ���ظ�
		Set<String> set = new TreeSet<>();
		words.forEach((e)->{
			set.add(da.idOf(e));
		});
		System.out.println("�ռ�ʹ����:"+da.spaceUsingRate());
		System.out.println(set.size());
		System.out.println(words.size());
		//�ɲ����Գ��Կ���n���߳���������n��ʾ��һ��Ľڵ㣬���Ҫ��Щ
	}
}
