package com.outsider.nlp.segmenter;

import java.util.ArrayList;
import java.util.List;

import com.outsider.common.util.StringUtils;
import com.outsider.model.Model;
import com.outsider.model.data.DataConverter;
import com.outsider.model.data.SegmenterDataConverter;
import com.outsider.model.hmm.SequenceNode;

/**
 * ͳһ�ķִ����ӿڣ�Ϊ�˷����Ժ�дcrf�ִ���չ
 * �Լ������ṩͳһ�Ľӿڷ������
 * @author outsider
 *
 */
public interface Segmenter extends Model<List<SequenceNode>, Object, int[], int[]>{
	final DataConverter<String[], List<SequenceNode>> converter = SegmenterDataConverter.getInstance();
	/**
	 * ���������ת��Ϊ�۲⼯���е����������ǰ��ַ�ת��Ϊ����
	 * @param sentences ������Ӽ���
	 * @return
	 */
	default List<int[]> sentence2ObservationIndex(String[] sentences) {
		List<int[]> res = new ArrayList<>();
		for(int i = 0; i < sentences.length;i++) {
			int[] O = new int[sentences[i].length()];
			for(int j = 0; j < sentences[i].length();j++) {
				O[j] = sentences[i].charAt(j);
			}
			res.add(O);
		}
		return res;
	}
	/**
	 * Ԥ����ӵ�״̬����
	 * @param sentence ����
	 * @return ״̬����
	 */
	default int[] predict(String sentence) {
		int[] O = sentence2ObservationIndex(new String[] {sentence}).get(0);
		return predict(O);
	}
	/**
	 * �������ֱ�ӷ��طִʽ��
	 * @param sentence
	 * @return
	 */
	default String[] predictAndReturnTerms(String sentence) {
		int[] predict = predict(sentence);
		return decode(predict, sentence);
	}
	/**
	 * ����������
	 * @param sentences
	 * @return ����Ԥ����0123 bmes
	 */
	default List<int[]> predict(String[] sentences){
		List<int[]> predictions = new ArrayList<>();
		List<int[]> observations = sentence2ObservationIndex(sentences);
		for(int i = 0; i< observations.size(); i++) {
			predictions.add(predict(observations.get(i)));
		}
		return predictions;
	}
	/**
	 * ���������ӷ��ؽ��
	 * @param sentences
	 * @return
	 */
	default List<String[]> predictAndReturnTerms(String[] sentences){
		List<int[]> predictions = predict(sentences);
		List<String[]> res = new ArrayList<>();
		for(int i =0 ; i < predictions.size(); i++) {
			res.add(decode(predictions.get(i), sentences[i]));
		}
		return res;
	}
	
	/**
	 * 
	 * @param terms
	 */
	default void train(String[] terms) {
		List<SequenceNode> nodes = converter.rawData2ConvertedData(terms);
		train(nodes);
	}
	/**
	 * ֱ�Ӵ���ѵ������
	 * @param corpus ����
	 * @param splitChar �ָ��ַ�
	 */
	default void train(String corpus, String splitChar) {
		List<SequenceNode> nodes = converter.rawData2ConvertedData(corpus.split(splitChar));
		train(nodes);
	}
	
	/**
	 * ����������
	 * @param corpuses �������
	 * @param splitChars �ָ��ַ�
	 */
	default void train(String[] corpuses, String[] splitChars) {
		String[] s = new String[0];
		for(int i = 0; i < corpuses.length; i++) {
			String[] co = corpuses[i].split(splitChars[i]);
			StringUtils.concat(s, co);
		}
		List<SequenceNode> nodes = converter.rawData2ConvertedData(s);
		train(nodes);//�ײ�ѵ��������Ҫʵ��
	}
	/*default List<SequenceNode> corpus2SequenceNodes(Object object, int segmentionMode, String splitChar){
		if(CorpusMode.SEGMENTION_ARTICLE == segmentionMode) {
			String article = (String) object;
			return converter.rawData2ConvertedData(article.split(splitChar));
		} else if (CorpusMode.SEGMENTION_SENTENCES == segmentionMode) {
			String[] sentences = (String[]) object;
			//�������ˣ���������ץ������Ϊ�����ӳ�һ�����У�������Ҫ����ͳ�Ʋ���
		}
		return null;
	}*/
	/**
	 * ��Ԥ�����;��ӽ��н����һ������
	 * @param code
	 * @param sentence
	 * @return
	 */
	default String[] decode(int[] predict, String sentence) {
		List<String> res = new ArrayList<>();
		char[] chars = sentence.toCharArray();
		for(int i = 0; i < predict.length;i++) {
			if(predict[i] == 0 || predict[i] == 1) {
				int a = i;
				while(predict[i] != 2) {
					i++;
					if(i == predict.length) {
						break;
					}
				}
				int b = i;
				if(b == predict.length) {
					b--;
				}
				res.add(new String(chars,a,b-a+1));
			} else {
				res.add(new String(chars,i,1));
			}
		}
		String[] s = new String[res.size()];
		return res.toArray(s);
	}
	
	public void initParameter();
}
