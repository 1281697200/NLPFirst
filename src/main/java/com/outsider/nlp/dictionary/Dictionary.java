package com.outsider.nlp.dictionary;

import java.util.Collections;
import java.util.List;

import com.outsider.common.algorithm.dat.darts.DoubleArrayTrie;
import com.outsider.common.util.Storable;
import com.outsider.common.util.StorageUtils;
/**
 * �ʵ�����
 * @author outsider
 *
 */
public class Dictionary implements Storable{
	//������
	protected int wordNum;
	//˫����洢��
	protected DoubleArrayTrie dat;
	public Dictionary() {
	}
	/**
	 * ����ʵ�
	 * @param words ������
	 * @param isSorted �Ƿ��Ѿ����д�����
	 * @param encoding ���� Ĭ��ʱutf-8
	 */
	public void build(List<String> words, boolean isSorted) {
		this.wordNum = words.size();
		if(!isSorted) {
			Collections.sort(words);
		}
		dat = new DoubleArrayTrie();
		dat.build(words);
	}
	
	public int getWordNum() {
		return wordNum;
	}
	
	public DoubleArrayTrie getDictionary() {
		return dat;
	}
	@Override
	public void save(String directory, String fileName) {
		StorageUtils.save(directory, fileName, this);
	}
	@Override
	public void open(String directory, String fileName) {
		StorageUtils.open(directory, fileName, this);
	}
	
}
