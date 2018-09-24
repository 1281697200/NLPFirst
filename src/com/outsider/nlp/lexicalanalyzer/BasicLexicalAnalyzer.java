package com.outsider.nlp.lexicalanalyzer;

import com.outsider.nlp.postagger.POSTagger;
import com.outsider.nlp.postagger.StaticPOSTagger;
import com.outsider.nlp.segmenter.Segmenter;
import com.outsider.nlp.segmenter.StaticSegmenter;

/**
 * �����ʷ��ִ���
 * ʹ��һ��HMM�ִʺ�һ��HMM���Ա�ע
 * @author outsider
 *
 */
public class BasicLexicalAnalyzer {
	private static Segmenter segmenter = StaticSegmenter.getSegmenter();
	private static POSTagger posTagger = StaticPOSTagger.getPOSTagger();
	
	public static String[] analyze(String sentence) {
		String[] words = segmenter.predictAndReturnTerms(sentence);
		String[] result = posTagger.predictAndReturnStr(words);
		return result;
	}
	
	
}
