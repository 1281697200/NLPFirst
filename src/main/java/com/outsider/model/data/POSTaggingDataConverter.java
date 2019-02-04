package com.outsider.model.data;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.outsider.common.algorithm.dat.darts.DoubleArrayTrie;
import com.outsider.model.hmm.SequenceNode;
import com.outsider.nlp.lexicalanalyzer.LexicalAnalysisResult;
import com.outsider.nlp.postagger.WordNatureMapping;
/**
 * 
 * @author outsider
 *
 */
public class POSTaggingDataConverter implements DataConverter<LexicalAnalysisResult, List<SequenceNode>>{
	private DoubleArrayTrie dictionary;
	private WordNatureMapping mapping = WordNatureMapping.getDefault();
	public POSTaggingDataConverter(DoubleArrayTrie dictionary) {
		this.dictionary = dictionary;
	}
	//׼���ϳ����ַ�ʽ��convert��ֻ���ʺʹ��Ե�id��ת��������Ĳ���LexicalAnalysisResult
	//����Ŀǰ�����������ǽ����ϴ���ΪLexicalAnalysisResultֱ�ӱ��ڴ���
	@Override
	public List<SequenceNode> convert(LexicalAnalysisResult rawData, Object... otherParameters) {
		String[] words = rawData.getSegmentationResult();
		String[] tagging = rawData.getPostaggingResult();
		List<SequenceNode> nodes = new ArrayList<>();
		for(int i = 0 ;i < words.length; i++) {
			SequenceNode node = new SequenceNode();
			node.setNodeIndex(dictionary.intIdOf(words[i]));
			node.setState(mapping.natureName2int(tagging[i]));
			nodes.add(node);
		}
		return nodes;
	}
	
}
