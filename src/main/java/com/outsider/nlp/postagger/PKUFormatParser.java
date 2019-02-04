package com.outsider.nlp.postagger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.outsider.common.util.IOUtils;
import com.outsider.common.util.nlp.NLPUtils;
import com.outsider.nlp.lexicalanalyzer.LexicalAnalysisResult;

/**
 * ����PKU��ʽ�Ĵ��Ա�ע����
 * @author outsider
 *
 */
public class PKUFormatParser {
	public static LexicalAnalysisResult  parse(String rawData, String splitCharr) {
		//��PKU�����ձ����зֱ�׼����
		LexicalAnalysisResult result = new LexicalAnalysisResult();
		List<String> words = new ArrayList<>();
		List<String> natures = new ArrayList<>();
		String splitChar = splitCharr;
		String corpus = rawData;
		Pattern pattern0 = Pattern.compile("\\[[^\\[\\]]+\\]/{0,1}\\w{1,5}\\s");
		Matcher matcher0 = pattern0.matcher(corpus);
		int start = 0;
		int lastEnd = 0;
		while(matcher0.find()) {
			String s = matcher0.group();
			start = matcher0.start();
			//������[]���ʱ��������ǰ��ķ�������������ٴ������������
			//lastEnd -> start
			dealWithoutBracketSituation(corpus.substring(lastEnd, start), splitChar, words, natures);
			dealBracketSituation(matcher0.group(), splitChar, words, natures);
			lastEnd = matcher0.end();
		}
		//�������һ��ƥ�䵽��[]���沿��
		if(lastEnd < corpus.length()) {
			 dealWithoutBracketSituation(corpus.substring(lastEnd, corpus.length()), splitChar, words ,natures);
		}
		String[] wordsArr = new String[words.size()];
		String[] natureArr = new String[natures.size()];
		words.toArray(wordsArr);
		natures.toArray(natureArr);
		result.setSegmentationResult(wordsArr);
		result.setPostaggingResult(natureArr);
		return result;
	}
	//�������������
	private static void dealBracketSituation(String s, String splitChar, List<String> words, List<String> natures){
		
		int in = s.lastIndexOf("]");//�ҵ�[]�е�����
		//����[]�ڵ�
		String[] s2 = s.substring(1, in).split(splitChar);
		for(int j = 0; j < s2.length;j++) {
			//���ַ�ʽ���Խ�� //w��������ĳ���
			int index = s2[j].lastIndexOf("/");
			if(index == -1) {
				System.out.println(s);
				continue;
			}
			String word = s2[j].substring(0, index);
			String wordNature = s2[j].substring(index+1, s2[j].length());
			words.add(word);
			natures.add(wordNature);
		}
	}
	
	public static void dealWithoutBracketSituation(String subCorpus, String splitChar, List<String> words, List<String> natures){
		if(subCorpus.trim().equals("")) {
			return;
		}
		String[] s = subCorpus.split(splitChar);
		for(int i =0; i< s.length;i++) {
			//�����д���һЩû�б���Ǵ��ԵĴ���
			/*if(!s[i].contains("/")) {
				System.out.println("dddd:"+subCorpus);
				continue;
			}*/
			int index = s[i].lastIndexOf("/");
			String word = s[i].substring(0, index);
			String wordNature = s[i].substring(index+1, s[i].length());
			words.add(word);
			natures.add(wordNature);
		}
	}
	public static void main(String[] args) {
		String path = "D:\\nlp����\\���Ա�ע\\���Ա�ע@�����ձ�199801.txt";
		String data = IOUtils.readText(path, "utf-8");
		LexicalAnalysisResult result = parse(data, "  ");
		String[] natures = result.getPostaggingResult();
		String[] words = result.getSegmentationResult();
		StringBuilder sb = new StringBuilder();
		List<String> mis = new ArrayList<>();
		mis.add("Ag");
		mis.add("Vg");
		mis.add("Tg");
		mis.add("Ng");
		mis.add("Dg");
		mis.add("Bg");
		mis.add("Yg");
		mis.add("Vg");
		mis.add("Vg");
		for(int i = 0; i < words.length; i++) {
			if(mis.contains(natures[i])) {
				natures[i] = natures[i].toLowerCase();
			}
			sb.append(words[i]+"\t"+natures[i]+"\n");
		}
		IOUtils.writeTextData2File(sb.toString(), "D:\\\\nlp����\\\\���Ա�ע\\\\���Ա�ע@�����ձ�199801_crf.txt", "utf-8");
	}
}
