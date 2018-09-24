package com.outsider.model.data;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.outsider.common.algorithm.dat.darts.DoubleArrayTrie;
import com.outsider.model.hmm.SequenceNode;
import com.outsider.nlp.postagger.POSTagger;

public class POSTaggerDataConverter implements DataConverter<String, List<SequenceNode>>{
	private DoubleArrayTrie dictionary;
	public POSTaggerDataConverter(DoubleArrayTrie dictionary) {
		this.dictionary = dictionary;
	}
	@Override
	public List<SequenceNode> rawData2ConvertedData(String rawData, Object... otherParameters) {
		//��PKU�����ձ����зֱ�׼��Ҫ����
		String splitChar = (String) otherParameters[0];
		String corpus = rawData;
		Pattern pattern0 = Pattern.compile("\\[[^\\[\\]]{1,80}\\]/{0,1}\\w{1,5}\\s");
		Matcher matcher0 = pattern0.matcher(corpus);
		List<SequenceNode> result = new ArrayList<>();//���յĽ��
		int start = 0;
		int lastEnd = 0;
		while(matcher0.find()) {
			String s = matcher0.group();
			start = matcher0.start();
			//������[]���ʱ��������ǰ��ķ�������������ٴ������������
			//lastEnd -> start
			List<SequenceNode> nodes1 = dealWithoutBracketSituation(corpus.substring(lastEnd, start), splitChar);
			List<SequenceNode> nodes2 = dealBracketSituation(matcher0.group(), splitChar);
			result.addAll(nodes1);
			result.addAll(nodes2);
			lastEnd = matcher0.end();
		}
		//�������һ��ƥ�䵽��[]���沿��
		if(lastEnd < corpus.length()) {
			List<SequenceNode> nodes = dealWithoutBracketSituation(corpus.substring(lastEnd, corpus.length()), splitChar);
			result.addAll(nodes);
		}
		return result;
	}
	//�������������
	private List<SequenceNode> dealBracketSituation(String s, String splitChar){
		int in = s.lastIndexOf("]");//�ҵ�[]�е�����
		//����[]�ڵ�
		String[] s2 = s.substring(1, in).split(splitChar);
		List<SequenceNode> result = new ArrayList<>();
		for(int j = 0; j < s2.length;j++) {
			//���ַ�ʽ���Խ�� //w��������ĳ���
			int index = s2[j].lastIndexOf("/");
			String word = s2[j].substring(0, index);
			String wordNature = s2[j].substring(index+1, s2[j].length());
			SequenceNode node = new SequenceNode(dictionary.intIdOf(word), POSTagger.natureName2Int.get(wordNature));
			result.add(node);
		}
		//����[]���幹�ɵĴʵĴ���
		//TODO ��ʱֻ���Ȳ����������
		//String s4 = s.substring(end+1, s.length()-1);
		return result;
	}
	
	public List<SequenceNode> dealWithoutBracketSituation(String subCorpus, String splitChar){
		String[] words = subCorpus.split(splitChar);
		List<SequenceNode> result = new ArrayList<>();
		for(int i =0; i< words.length;i++) {
			int index = words[i].lastIndexOf("/");
			//�����д���һЩû�б���Ǵ��ԵĴ���
			if(index == -1) {
				continue;
			}
			String word = words[i].substring(0, index);
			//�����д���һЩû�б���Ǵ��ԵĴ���
			String wordNature = words[i].substring(index+1, words[i].length());
			SequenceNode node = new SequenceNode(dictionary.intIdOf(word), POSTagger.natureName2Int.get(wordNature));
			result.add(node);
		}
		return  result;
	}
}
