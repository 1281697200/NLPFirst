package com.outsider.nlp.postagger;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.outsider.common.util.IOUtils;
import com.outsider.common.util.Storable;
import com.outsider.common.util.StorageUtils;
import com.outsider.constants.nlp.PathConstans;

/**
 * ������һ������Ҫ�Ķ���
 * HMM��״̬�͹۲�ļ������ֻ��ӳ��Ϊһ����0��n����һ��ֵ��
 * ��Ϊ�������ܺ�����ת�Ƹ��ʺ�״̬�����ȵ�
 * @author outsider
 *
 */
public class WordNatureMapping implements Storable, Serializable{
	private  Map<String,Integer> natureName2Int;
	private  String[] int2NatureName;
	private  int wordNatureNum  = 110;
	public WordNatureMapping() {
	}
	public Map<String, Integer> getNatureName2Int() {
		return natureName2Int;
	}
	public String[] getInt2NatureName() {
		return int2NatureName;
	}
	public void setNatureName2Int(Map<String, Integer> natureName2Int) {
		this.natureName2Int = natureName2Int;
	}
	public void setInt2NatureName(String[] int2NatureName) {
		this.int2NatureName = int2NatureName;
	}
	public WordNatureMapping(Map<String, Integer> natureName2Int, String[] int2NatureName, int wordNatureNum) {
		super();
		this.natureName2Int = natureName2Int;
		this.int2NatureName = int2NatureName;
		this.wordNatureNum = wordNatureNum;
	}
	
	public String int2natureName(int intId) {
		return int2NatureName[intId];
	}
	public Integer natureName2int(String natureName) {
		return natureName2Int.get(natureName);
	}
	public void setWordNatureNum(int wordNatureNum) {
		this.wordNatureNum = wordNatureNum;
	}
	public int getWordNatureNum() {
		return wordNatureNum;
	}
	/**
	 * @param natureNames
	 * @return
	 */
	public static WordNatureMapping generateMapping(String[] natureNames) {
		Map<String, Integer> map = new HashMap<>();
		for(int i = 0; i < natureNames.length; i++) {
			map.put(natureNames[i], i);
		}
		WordNatureMapping mapping = new WordNatureMapping(map, natureNames, natureNames.length);
		return mapping;
	}
	/**
	 * ��ȡĬ��·���µ�WordNatureMapping
	 * @return
	 */
	public static WordNatureMapping getDefault() {
		WordNatureMapping res = new WordNatureMapping();
		res.open(PathConstans.WORD_NATURE_MAPPING, null);
		return res;
	}
	
	public static WordNatureMapping getCoarseWordNatureMapping() {
		WordNatureMapping re = new WordNatureMapping();
		re.open(PathConstans.WORD_NATURE_MAPPING, "coarseWordNature");
		return re;
	}
	/**
	 * ����2�����Ժ�int���໥ӳ�����
	 */
	public static void main(String[] args) {
		List<String> natures = IOUtils.readTextAndReturnLines("D:\\nlp����\\���Ա�ע\\wordNature_2014_coarse.txt","utf-8");
		String[] natureNames = new String[natures.size()];
		natures.toArray(natureNames);
		WordNatureMapping mapping = WordNatureMapping.generateMapping(natureNames);
		mapping.save("./model/dictionary", "coarseWordNature");
		//WordNatureMapping mapping = WordNatureMapping.getDefault();
		//System.out.println(mapping.wordNatureNum);
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
