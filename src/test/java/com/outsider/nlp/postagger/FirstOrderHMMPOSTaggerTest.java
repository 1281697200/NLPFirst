package com.outsider.nlp.postagger;

import java.util.Arrays;
import java.util.List;

import com.outsider.common.util.IOUtils;
import com.outsider.constants.nlp.PathConstans;
import com.outsider.model.hmm.SequenceNode;
import com.outsider.model.metric.Metric;
import com.outsider.nlp.segmenter.CrfppSegmenter;

public class FirstOrderHMMPOSTaggerTest {
	public static void main(String[] args) {
		train();
		use();
		
	}
	
	public static void use() {
		CrfppSegmenter segmenter = new CrfppSegmenter();
		segmenter.open(PathConstans.CRFPP_SEGMENTER);
		String[] words = segmenter.seg("������һ����ֿ�İ����������ǰ");
		System.out.println(Arrays.toString(words));
		POSTagger tagger = new FirstOrderHMMPOSTagger();
		tagger.open(PathConstans.FIRST_ORDER_HMM_POSTAGGER, null);
		String[] tags = tagger.tag(words);
		System.out.println(Arrays.toString(tags));
		
	}
	
	public static void train() {
		FirstOrderHMMPOSTagger tagger = new FirstOrderHMMPOSTagger(POSTaggingUtils.getDefaultDictionary(), WordNatureMapping.getCoarseWordNatureMapping());
		List<SequenceNode> nodes = POSTaggingUtils.generateNodesWithCRFormatData("D:\\\\nlp����\\\\���Ա�ע\\\\���Ա�ע2014_crf_coarseNature.txt", "utf-8", 0, 1
				,tagger.getDictionary(), tagger.getWordNatureMapping());
		System.out.println("����������...");
		tagger.train(nodes);
		String[] words = new String[] {"��","���","����","����","��","��"};
		String[] res = tagger.tag(words);
		for(int i = 0; i < res.length; i++) {
			System.out.print(words[i]+"/"+res[i]);
		}
		System.out.println();
		String[] testData = IOUtils.readTextAndReturnLinesOfArray("D:\\\\nlp����\\\\���Ա�ע\\\\���Ա�ע@�����ձ�199801_crf.txt", "utf-8");
		int testLen = (int) (testData.length);
		String[] words1 = new String[testLen];
		String[] natures = new String[testLen];
		for(int i = 0; i < testLen; i++) {
			String[] s = testData[i].split("\t");
			words1[i] = s[0];
			natures[i] = s[1].substring(0, 1).toLowerCase();
		}
		String[] predict = tagger.tag(words1);
		for(int i = 0; i < predict.length; i++) {
			predict[i] = predict[i].substring(0, 1).toLowerCase();
		}
		float accuracy = Metric.accuracyScore(predict, natures);
		System.out.println("���Լ���׼ȷ��:"+accuracy);
		tagger.save(PathConstans.FIRST_ORDER_HMM_POSTAGGER, null);
		//ϸ���ȵĴ���:FOHMM:0.6641114
		//�����ȴ���:FOHMM:0.89177626
		//SOHMM:0.89011705
		//��Ȼ��ϸ���ȴ���ѵ����������������ֻ�������ȴ����Ƿ���ȷ����ô��ȷ��Ҳ���У�����0.8906333��Ҳ����˵��Ȼϸ���ȵĴ��Բ�����ȫ��ȷ��Ԥ�⵽�������´����ȵĴ��Ի�����ȷ��
		// ��/rr���/t����/m����/m��/y��/w
	}
}
