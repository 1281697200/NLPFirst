package com.outsider.common.algorithm.daTrieTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.omg.CORBA.INTERNAL;

/**
 * ����Double Array Trie Tree�ӿ�
 * @author outsider
 *
 */
public interface EncodingDATrieTreeInterface extends DATrieTreeInterface{
	/**
	 * ��0��ʼ��ÿ����һ�����ַ����б���
	 * @param tokens
	 */
	default Object[] encodeFromZero(List<String> tokens) {
		Set<Integer> chars = new HashSet<>();
		int size = tokens.size();
		for(int i = 0; i < size; i++) {
			String s = tokens.get(i);
			int len = s.length();
			for(int j = 0;j < len; j++) {
				chars.add((int) s.charAt(j));
			}
		}
		//���������ֻ�洢unicode���򵥱����ӳ�䣬��ôʵ��ƥ��ǰ׺ʱ�������鷳
		Map<Integer, Integer> unicode2smallCode = new HashMap<>();
		List<Integer> smallCode2unicode = new ArrayList<>(chars.size());
		int count = 0;
		for(int c : chars) {
			unicode2smallCode.put(c, count);
			smallCode2unicode.add(c);
			count++;
		}
		return new Object[] {unicode2smallCode,smallCode2unicode};
	}
}
