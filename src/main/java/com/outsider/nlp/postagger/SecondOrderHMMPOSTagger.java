package com.outsider.nlp.postagger;

import java.util.List;

import com.outsider.common.algorithm.dat.darts.DoubleArrayTrie;
import com.outsider.model.data.DataConverter;
import com.outsider.model.data.POSTaggingDataConverter;
import com.outsider.model.hmm.SecondOrderGeneralHMM;
import com.outsider.model.hmm.SequenceNode;
import com.outsider.nlp.lexicalanalyzer.LexicalAnalysisResult;

/**
 * ����HMM�Ĵ��Ա�ע������һ��û�����
 * @author outsider
 *
 */
public class SecondOrderHMMPOSTagger extends SecondOrderGeneralHMM implements POSTagger{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static DoubleArrayTrie dictionary = POSTaggingUtils.getDefaultDictionary();
	private static WordNatureMapping wordNatureMapping = WordNatureMapping.getDefault();
	
	public SecondOrderHMMPOSTagger() {
		this(wordNatureMapping.getWordNatureNum(),dictionary.getKeySize());
	}
	

	public SecondOrderHMMPOSTagger(int stateNum, int observationNum, double[] pi, double[][] transferProbability1,
			double[][] emissionProbability) {
		super(stateNum, observationNum, pi, transferProbability1, emissionProbability);
	}

	public SecondOrderHMMPOSTagger(int stateNum, int observationNum) {
		super(stateNum, observationNum);
		reInitParameters();
	}
	
	public void setWordNatureMapping(WordNatureMapping wordNatureMapping) {
		this.wordNatureMapping = wordNatureMapping;
	}
	public DoubleArrayTrie getDictionary() {
		return dictionary;
	}
	public void setDictionary(DoubleArrayTrie dictionary) {
		this.dictionary = dictionary;
	}
	public WordNatureMapping getWordNatureMapping() {
		return wordNatureMapping;
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
	protected void solve(List<SequenceNode> nodes) {
		//�������п�ʼѵ��
		pi[nodes.get(0).getState()]++;
		emissionProbability[nodes.get(0).getState()][nodes.get(0).getNodeIndex()]++;
		for(int i = 1;i < nodes.size(); i++) {
			SequenceNode node = nodes.get(i);
			//��Ҫ����ʵ���û�еĴʵ������Ҳ����nodeIndex=-1
			if(node.getNodeIndex() == -1) {
				continue;
			}
			//״̬ͳ��
			pi[node.getState()]++;
			//״̬ת��ͳ��
			transferProbability1[nodes.get(i-1).getState()][node.getState()]++;
			//״̬�¹۲�ֲ�ͳ��
			emissionProbability[node.getState()][node.getNodeIndex()]++;
		}
	}
	
	@Override
	public int[] verterbi(int[] O) {
		double[][] deltas = new double[O.length][this.stateNum];
		//����deltas[t][i]��ֵ������һ���ĸ�״̬������
		int[][] states = new int[O.length][this.stateNum];
		//��ʼ��deltas[0][]
		for(int i = 0;i < this.stateNum; i++) {
			/**
			 * �������δ��½�ʣ���ô��Ϊ������ʱ�INFINITY��С������ȡ1.5����INFINITY
			 */
			if(O[0]==-1)
				deltas[0][i] = pi[i] + 1.5*INFINITY;
			else
				deltas[0][i] = pi[i] + emissionProbability[i][O[0]];
		}
		//����deltas
		for(int t = 1; t < O.length; t++) {
			for(int i = 0; i < this.stateNum; i++) {
				deltas[t][i] = deltas[t-1][0]+transferProbability1[0][i];
				for(int j = 1; j < this.stateNum; j++) {
					double tmp = deltas[t-1][j]+transferProbability1[j][i];
					if (tmp > deltas[t][i]) {
						deltas[t][i] = tmp;
						states[t][i] = j;
					}
				}
				/**
				 * �������δ��½�ʣ���ô��Ϊ������ʱ�INFINITY��С������ȡ1.5����INFINITY
				 */
				if(O[t]==-1)
					deltas[t][i] += 1.5*INFINITY;
				else
					deltas[t][i] += emissionProbability[i][O[t]];
			}
		}
		//�����ҵ�����·��
		int[] predict = new int[O.length];
		double max = deltas[O.length-1][0];
		for(int i = 1; i < this.stateNum; i++) {
			if(deltas[O.length-1][i] > max) {
				max = deltas[O.length-1][i];
				predict[O.length-1] = i;				
			}
		}
		for(int i = O.length-2;i >= 0;i-- ) {
			predict[i] = states[i+1][predict[i+1]];
		}
		return predict;
	}

	@Override
	public void train(LexicalAnalysisResult result) {
		DataConverter<LexicalAnalysisResult, List<SequenceNode>> converter = new POSTaggingDataConverter(dictionary);
		List<SequenceNode> nodes = converter.convert(result);
		super.train(nodes);
	}
	/**
	 * ����CRF��ʽ�����ݽ���ѵ��
	 * ��: ����\t����\n
	 * @param lines
	 * @param wordColumnIndex �������е�����
	 * @param natureColumnIndex ���������е�����
	 */
	public void trainWithCRFormatData(List<String> lines, int wordColumnIndex, int natureColumnIndex) {
		//��dataת��ΪLexicalAnalysisResult
		LexicalAnalysisResult result = new LexicalAnalysisResult();
		String[] words = new String[lines.size()];
		String[] natures = new String[lines.size()];
		for(int i = 0; i < lines.size(); i++) {
			String[] s = lines.get(i).split("\t");
			words[i] = s[wordColumnIndex];
			words[i] = s[natureColumnIndex];
		}
		result.setSegmentationResult(words);
		result.setPostaggingResult(natures);
		this.train(result);
	}

}
