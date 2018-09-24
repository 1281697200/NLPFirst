package com.outsider.common.algorithm.daTrieTree;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Double Array Trie Tree�ӿ�
 * @author outsider
 *
 */
public interface DATrieTreeInterface {
	/**
	 * ���ݴ���������
	 * @param tokens ���б�
	 */
	void build(List<String> tokens);
	/**
	 * ����һ���ʵ�����
	 * @param token ��
	 */
	void insert(String token);
	/**
	 * ƥ��ǰ׺��tokenPrefix�����д�
	 * @param tokenPrefix ǰ׺
	 * @return
	 */
	List<String> match(String tokenPrefix);
	/**
	 * �ַ�����
	 * @param c �ַ�
	 * @return ����
	 */
	int code(char c);
	/**
	 * �ع������С
	 * @param size ��С
	 */
	void resize(int size);
	/**
	 * �ж�һ�����Ƿ����
	 * @param token ��
	 * @return
	 */
	boolean exist(String token);
	/**
	 * ��ȡһ���ʵ�id
	 * @param token ��
	 * @return �ַ���id
	 */
	String idOf(String token);
	
}
