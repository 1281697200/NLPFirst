package com.outsider.nlp.postagger;

import java.util.List;

import com.outsider.common.algorithm.dat.darts.DoubleArrayTrie;
import com.outsider.common.dataStructure.Table;
import com.outsider.common.util.IOUtils;
import com.outsider.model.crf.FeatureFunction;
import com.outsider.model.crf.SupervisedCRF;
import com.outsider.model.data.DataConverter;
import com.outsider.model.data.POSTaggingDataConverter;
import com.outsider.model.hmm.SequenceNode;
import com.outsider.model.metric.Metric;
import com.outsider.nlp.lexicalanalyzer.LexicalAnalysisResult;

public class CrfPostagger extends SupervisedCRF implements POSTagger{
	private static DoubleArrayTrie dictionary = POSTaggingUtils.getDefaultDictionary();
	private static WordNatureMapping wordNatureMapping = WordNatureMapping.getDefault();
	
	public CrfPostagger() {
		super(dictionary.getKeySize() , wordNatureMapping.getWordNatureNum());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DoubleArrayTrie getDictionary() {
		return dictionary;
	}
	public WordNatureMapping getWordNatureMapping() {
		return wordNatureMapping;
	}
	@Override
	public void train(LexicalAnalysisResult result) {
		DataConverter<LexicalAnalysisResult, List<SequenceNode>> converter = new POSTaggingDataConverter(dictionary);
		List<SequenceNode> nodes = converter.convert(result);
		super.train(nodes);
		
	}

	@Override
	public String[] tag(String[] words) {
		int[] wordIntIds = POSTaggingUtils.words2intId(words, dictionary);
		/**
		 * ��ȡ�ӿ�POSTagger�д����Ĵ�Ԥ��Ĵ�����
		 */
		//ԭʼԤ��
		int[] rawPredict = super.predict(wordIntIds);
		/**
		 * ʹ������������ԭ��
		 * �����к������֣���ô��עΪm
		 * �����к���Ӣ�Ļ���������(����������ʱ������)����עΪx
		 * ��ʱ�����ǣ�
		 * ���Ժ������URL�ȵı�ע
		 */
		String numberReg = "[0-9]+";//û�п���ȫ�ǵ�����
		String otherLanguageReg = "[a-zA-Z]+";
		String[] res = new String[rawPredict.length];
		for(int i = 0; i < words.length; i++) {
			if(words[i].matches(numberReg)) {
				res[i] = "m";
			} else if(words[i].matches(otherLanguageReg)) {
				res[i] = "x";
			} else {
				res[i] = wordNatureMapping.int2natureName(rawPredict[i]);
			}
		}
		return res;
	}

	@Override
	public void train(Table table, int xColumnIndex, int yColumnIndex) {
		POSTaggingDataConverter converter = new POSTaggingDataConverter(dictionary);
		String[] words = table.getDataOfOneColumn(xColumnIndex);
		String[] natures = table.getDataOfOneColumn(yColumnIndex);
		LexicalAnalysisResult result = new LexicalAnalysisResult();
		result.setPostaggingResult(natures);
		result.setSegmentationResult(words);
		List<SequenceNode> nodes = converter.convert(result);
		this.train(nodes);
	}

	/**
	 * û��ʵ��
	 */
	@Override
	public String generateModelTemplate() {
		return null;
	}
	
	@Override
	public int[] veterbi(int[] observations) {
		int xLen = observations.length;
		//���浱ǰ���״̬��ǰһ��״̬����һ��״̬�õ���
		int[][] psi = new int[xLen][stateNum];
		double[][] deltas = new double[xLen][stateNum];
		//��ʼ��delta,��һ�μ���wk*f(x,y_0) ��ʱû��Bigram
		List<FeatureFunction> funcs = getUnigramFeatureFunction(observations, 0 );
		for(int i = 0; i  < stateNum; i++) {
			double weight = computeUnigramWeight(funcs, i);
			deltas[0][i] = weight;
		}
		//DP��������delta
		for(int t = 1; t < xLen; t++) {
			//��ȡ��ǰλ�õ���������list
			List<FeatureFunction> curFuncs = getUnigramFeatureFunction(observations, t );
			for(int i = 0; i < stateNum; i++) {
				//�ҵ���õ�ǰ��״̬
				deltas[t][i] = deltas[t-1][0] + computeBigramWeight(0, i);
				for(int j = 1; j < stateNum; j++) {
					double tmp = deltas[t-1][j] + computeBigramWeight(j, i);
					if(tmp > deltas[t][i]) {
						deltas[t][i] = tmp;
						psi[t][i] = j;//���浱ǰ���״̬����һ��ǰ��״̬����
					}
				}
				//���ϵ�ǰUnigram���ۻ�Ȩ��ֵ, �������������ر��ʱ
				deltas[t][i] += computeUnigramWeight(curFuncs, i);
			}
		}
		//�ҵ����һ���۲�����״̬������
		int[] best = new int[xLen];//��������Ԥ������
		double max = deltas[xLen-1][0];
		for(int i = 1; i < stateNum; i++) {
			if(deltas[xLen-1][i] > max) {
				max = deltas[xLen-1][i];
				best[xLen-1] = i;
			}
		}
		//����
		for(int i = xLen - 2; i >=0; i--) {
			best[i] = psi[i+1][best[i+1]];
		}
		return best;
	}
	
	@Override
	public String getDefaultTemplate() {
		return "# Unigram\n" +
	            "U0:%x[-1,0]\n" +
	            "U1:%x[0,0]\n" +
	            "U2:%x[1,0]\n" +
	            "U3:%x[-2,0]%x[-1,0]\n" +
	            "U4:%x[-1,0]%x[0,0]\n" +
	            "U5:%x[0,0]%x[1,0]\n" +
	            "U6:%x[1,0]%x[2,0]\n" +
	            "\n" +
	            "# Bigram\n" +
	            "B";
	}
	public static void main(String[] args) {
		CrfPostagger tagger = new CrfPostagger();
		List<SequenceNode> nodes = POSTaggingUtils.generateNodesWithCRFormatData("D:\\\\nlp����\\\\���Ա�ע\\\\���Ա�ע_crf.txt", "utf-8", 0, 1
				,tagger.getDictionary(), tagger.getWordNatureMapping());
		System.out.println("����������...");
		System.out.println("nodes.size:"+nodes.size());
		nodes = nodes.subList(0, 2100000);
		tagger.train(nodes);
		//D:\nlp����\���Ա�ע
		String[] words = new String[] {"��","���","����","����","��","��"};
		String[] res = tagger.tag(words);
		for(int i = 0; i < res.length; i++) {
			System.out.print(words[i]+"/"+res[i]);
		}
		System.out.println();
		String[] testData = IOUtils.readTextAndReturnLinesOfArray("D:\\\\nlp����\\\\���Ա�ע\\\\���Ա�ע@�����ձ�199801_crf.txt", "utf-8");
		int testLen = (int) (testData.length * 0.5);
		String[] words1 = new String[testLen];
		String[] natures = new String[testLen];
		for(int i = 0; i < testLen; i++) {
			String[] s = testData[i].split("\t");
			words1[i] = s[0];
			natures[i] = s[1];
		}
		String[] predict = tagger.tag(words1);
		float accuracy = Metric.accuracyScore(predict, natures);
		System.out.println("���Լ���׼ȷ��:"+accuracy);
		//crf:0.62375253
		//һ��HMM:0.6641114
	}
	
}
