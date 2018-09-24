package com.outsider.nlp.postagger;

import java.util.List;
import java.util.Map;

import com.outsider.common.algorithm.dat.darts.DoubleArrayTrie;
import com.outsider.model.Model;
import com.outsider.model.data.POSTaggerDataConverter;
import com.outsider.model.hmm.SequenceNode;
/**
 * 
 * @author outsider
 *
 */
public interface POSTagger extends Model<List<SequenceNode>, Object, int[], int[]>{
	DoubleArrayTrie dictionary = PKUCorpusDictionary.getDictionary();
	POSTaggerDataConverter dataConverter = new POSTaggerDataConverter(dictionary);
	static Map<String,Integer> natureName2Int = WordNatureMapping.getNatureName2IntMapping(null);
	static String[] int2NatureName = WordNatureMapping.getInt2NatureNameMapping(null);
	/*
	 * ���Ա�עʱ����������ֱ�ӱ�ע������Ҫ�������б�ע��������Ӣ��ֱ�ӱ�ע
	 */
	default void train(String data, String splitChar) {
		List<SequenceNode> nodes = dataConverter.rawData2ConvertedData(data, splitChar);
		this.train(nodes);
	}
	/**
	 * ����ת��Ϊ����ID
	 * @param words
	 * @return
	 */
	default int[] observation2Int(String[] words) {
		int[] intId = new int[words.length];
		for(int i = 0; i < intId.length; i++) {
			intId[i] = dictionary.intIdOf(words[i]);
		}
		return intId;
	}
	
	/*default String[] wordNatureIntId2Str(int[] wordNatureIntIds) {
		String[] 
	}*/
	/**
	 * Ԥ����ֱ�ӷ������飬�����ں��ڴ���Ԫ�����磺 ��/z
	 * @param words ��ҪԤ��ĵ�������
	 * @return
	 */
	default String[] predictAndReturnStr(String[] words) {
		int[] intIds = observation2Int(words);
		/**
		 * ���ﴫ��words��Ϊ����Ҫ����������ֵ��������ͺ�Ӣ�����͵ı�ע����
		 * ԭʼtrain��������Tagger����д
		 */
		int[] result = this.predict(intIds, words);
		for(int i = 0; i < words.length; i++) {
			try {
				String s = int2NatureName[result[i]];
			} catch (Exception e) {
				System.out.println("result[i]:"+result[i]);
				System.out.println(int2NatureName.length);
				System.exit(0);
			}
			words[i] = words[i]+"/"+ int2NatureName[result[i]];
		}
		return words;
	}
	/**
	 * ֱ�ӷ���Ԥ��ڵ����飬������ڴ���
	 * @param words
	 * @return
	 */
	default TaggingResultNode[] predictAndReturnNodes(String[] words) {
		int[] intIds = observation2Int(words);
		/**
		 * ���ﴫ��words��Ϊ����Ҫ����������ֵ��������ͺ�Ӣ�����͵ı�ע����
		 * ԭʼtrain��������Tagger����д
		 */
		int[] result = this.predict(intIds, words);
		TaggingResultNode[] nodes = new TaggingResultNode[intIds.length];
		for(int i = 0; i < words.length; i++) {
			nodes[i] = new TaggingResultNode(words[i], int2NatureName[result[i]]);
		}
		return nodes;
	}
}
