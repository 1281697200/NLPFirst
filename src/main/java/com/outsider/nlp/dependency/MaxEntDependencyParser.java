package com.outsider.nlp.dependency;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.outsider.common.algorithm.MinimumSpanningTree;
import com.outsider.common.logger.CONLPLogger;
import com.outsider.common.util.Storable2;
import com.outsider.constants.nlp.PathConstans;

import opennlp.tools.ml.maxent.GISModel;
import opennlp.tools.ml.maxent.GISModelReader;
/**
 * ���������ģ�͵�����䷨����
 * ע��:��classֻ����Ϊʹ������ؾ䷨�����Ľӿڣ����ṩģ�͵�ѵ��
 * ģ�͵�ѵ��ʹ��opennlp���
 * 
 * 2019.1.22 ������ɣ��������⣺
 * ��1���Ƿ�CoNLLFeatureGenerator��CoNLLSampleGenerator��������ʱ�᲻һ�µ���Ԥ��������
 * ��2���Ƿ����ӵĴ����������������������ã�
 * 
 * @author outsider
 */
public class MaxEntDependencyParser extends AbstractDependencyParser implements Storable2{
	//������openlp�е������ģ��
	private GISModel model;
	private Logger logger = CONLPLogger.getLoggerOfAClass(MaxEntDependencyParser.class);
	public MaxEntDependencyParser() {
		logger.info("load model...");
		long start = System.currentTimeMillis();
		//����Ĭ��·����ģ��
		open(PathConstans.DEPENDENCY_PARSER_MAXENT);
		long end = System.currentTimeMillis();
		logger.info("done..."+((end - start) / 1000) + " seconds");
		/*logger.info("load default segmenter...");
		//this.segmenter = 
		logger.info("done...");
		logger.info("load default postagger...");
		//this.posTagger = 
		logger.info("done...");*/
		
	}
	
	
	@Override
	public CoNLLSentence parse(String[] words, String[] natures) {
		if(words.length != natures.length || words.length < 2) {
			return null;
		}
		//����root�ڵ�
		String[] wordsWithROOT = new String[words.length + 1];
		String[] naturesWithROOT = new String[natures.length + 1];
		wordsWithROOT[0] = CoNLLWord.ROOT_LEMMA;
		naturesWithROOT[0] = CoNLLWord.ROOT_CPOSTAG;
		System.arraycopy(words, 0, wordsWithROOT, 1, words.length);
		System.arraycopy(natures, 0, naturesWithROOT, 1, natures.length);
		//��������֮��ķ�������
		CoNLLSample[][] contexts = CoNLLSampleGenerator.generate(words, natures);
		//���๹���ڽӾ���Ϊ����С�������㷨׼��
		float[][] graph = new float[contexts.length][contexts.length];
		//����ߵ�һЩ������Ϣ�����������ϵ��
		Edge[][] edges = new Edge[contexts.length][contexts.length];
		for(int i = 0; i < contexts.length; i++) {
			for(int j = 0; j < contexts.length; j++) {
				//��ʼ��graph
				graph[i][j] = Float.MAX_VALUE;
				edges[i][j] = new Edge();
				if(contexts[i][j] == null)
					continue;
				String[] context = contexts[i][j].context;
				double[] prob = model.eval(context);
				String best = model.getBestOutcome(prob);
				int index = model.getIndex(best);
				double bestProb = prob[index];
				edges[i][j].deprelaLabel = index;
				//��������ϵԤ��ΪNULL��ѡ��ڶ��õ�һ�������ϵ��Ϊ�����
				if(best.equals(CoNLLWord.NoneDEPREL)) {
					prob[index] = 0;
					String secondBest = model.getBestOutcome(prob);
					int secondBestIndex = model.getIndex(secondBest);
					edges[i][j].deprelaLabel = secondBestIndex;
					bestProb = prob[secondBestIndex];
				}
				edges[i][j].proba = (float) bestProb;
				graph[i][j] = (float) Math.abs(Math.log(bestProb));
				
			}
		}
		//������ͼ���򻯣�ȡ���п��ܵķ�����Ϊ����ͼ�ı�
		//ֻ�����ڽӾ����һ��
		for(int i = 0; i < contexts.length; i++) {
			for(int j = 0; j < i; j++) {
				float best = 0;
				//��Ϊ����ȡ�˶����;���ֵ��ԽС�������Խ��
				if(graph[i][j] < graph[j][i]) {
					best = graph[i][j];
					edges[i][j].edgeDirection = true;
					edges[j][i].edgeDirection = false;
				} else {
					best = graph[j][i];
					edges[j][i].edgeDirection = true;
					edges[i][j].edgeDirection = false;
				}
				graph[i][j] = best;
				graph[j][i] = best;
			}
		}
		//�Ľ���ROOT�ڵ�����ֻ��һ�����ӣ����ҵ����п��ܵĺ��ӣ��ж�ROOT�������ڵ�ı߱���һ�����ӳ����������ĳɷ֣�
		float maxROOT = Float.MAX_VALUE;
		int bestChild = 0;
		for(int i = 1; i < graph.length; i++) {
			if(graph[i][0] < maxROOT) {
				maxROOT = graph[i][0];
				bestChild = i;
			}
		}
		//��������(�ڵ��ROOT֮��ĸ������ı�)�ж�
		for(int i = 1; i < graph.length; i++) {
			if(i != bestChild) {
				graph[i][0] = Float.MAX_VALUE;
				graph[0][i] = Float.MAX_VALUE;
			}
		}
		//�����ڽӾ���Ϊ���ܳ��Ľ��ʵ����������������Ľ����������ȡ��������ȡ����ֵ��ΪȨֵ�͵�����ķ�㷨��
		int[] mst = MinimumSpanningTree.primAlgorithm(graph);
		CoNLLWord[] coWords = new CoNLLWord[words.length];
		for(int i = 0; i < coWords.length; i++) {
			coWords[i] = new CoNLLWord();
			coWords[i].setID(i+1);
			coWords[i].setLEMMA(words[i]);
			coWords[i].setCPOSTAG(natures[i]);
			coWords[i].setPOSTAG(natures[i]);
		}
		for(int i = 0; i < mst.length; i++) {
			if(mst[i] != -1) {
				Edge edge = edges[mst[i]][i];
				int start = 0;
				int end = 0;
				if(edge.edgeDirection) {
					start = mst[i];
					end = i;
				} else {
					start = i;
					end = mst[i];
				}
				//���ܳ����еĽڵ�û��HEAD�����Ĵʣ������������
				//���ֵ�i������û�к��Ĵ� HEAD
				if(start != i) {
					start = i;//����֮ǰûд����������
					//�ҳ�����ܵ��������Ϊ���
					float maxProba = 0;
					for(int j = 1; j < edges.length; j++) {
						if(edges[start][j].proba > maxProba) {
							maxProba = edges[start][j].proba;
							end = j;
						}
					}
				}
				String DEPRELA = model.getOutcome(edges[start][end].deprelaLabel);
				coWords[start -1].setHEAD(end);
				coWords[start -1].setDEPREL(DEPRELA);
			}
		}
		CoNLLSentence sentence = new CoNLLSentence(coWords);
		return sentence;
	}

	@Override
	public CoNLLSentence[] parse(List<String[]> words, List<String[]> natures) {
		CoNLLSentence[] sentences = new CoNLLSentence[words.size()];
		for(int i = 0; i < sentences.length; i++) {
			sentences[i] = parse(words.get(i), natures.get(i));
		}
		return sentences;
	}

	@Override
	/**
	 * �÷�������Ҫʵ��
	 * ģ�Ͳ������ﱣ��
	 */
	@Deprecated
	public void save(String filePath) {
	}

	@Override
	public void open(String filePath) {
		GISModelReader reader = null;
		try {
			reader = new GISModelReader(new File(filePath));
			this.model = (GISModel) reader.getModel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//parsing�������õ�����������
	protected class Edge{
		//����ߵ�ʵ�ʱ߷���true��ʾi->j������,����j->i����
		//����:edge[1][0].edgeDirection = false,��ʾ1��0֮��Ľڵ�ʵ���ϵķ�����0->1
		public boolean edgeDirection = true;
		//�����ϵ��ֻ���������������String��ǩ��model��ȡ����
		public int deprelaLabel;
		//�����������
		public float proba;
	}
	
	
	public void test(String[] words, String[] natures) {
		CoNLLSentence sentence = parse(words, natures);
		System.out.println(sentence);
		System.out.println();
	}

	@Override
	public CoNLLSentence parse(String sentence) {
		String[] words = segmenter.seg(sentence);
		String[] tags = posTagger.tag(words);
		return this.parse(words, tags);
	}

	@Override
	public CoNLLSentence[] parse(String[] sentences) {
		CoNLLSentence[] result = new CoNLLSentence[sentences.length];
		for(int i = 0;i < sentences.length; i++) {
			result[i] = parse(sentences[i]);
		}
		return result;
	}
	
	//����CoNLLSampleGenerator��CoNLLFeatureGenerator�����������Ƿ�һ��
	/*public static void main(String[] args) {
		String[] words = new String[] {"���","����","̰��","��¸","��","����","����"};
		String[] natures = new String[] {"a","v","v","n","u","n","v"};
		CoNLLSample[][] samples = CoNLLSampleGenerator.generate(words, natures);
		List<String> lines = new ArrayList<String>();
		lines.add("1	���	���	a	ad	_	2	��ʽ");
		lines.add("2	����	����	v	v	_	0	���ĳɷ�");
		lines.add("3	̰��	̰��	v	v	_	7	�޶�");
		lines.add("4	��¸	��¸	n	n	_	3	��������");
		lines.add("5	��	��	u	udeng	_	3	��������");
		lines.add("6	����	����	n	n	_	7	�޶�");
		lines.add("7	����	����	v	vn	_	2	����");
		lines.add("");
		List<String> samples2 = CoNLLFeatureGenerator.makeData(lines);
		List<String> slines = new ArrayList<>();
		for(int i = 0; i < samples.length; i++) {
			for(int j = 0; j < samples.length; j++) {
				if(samples[i][j] != null) {
					String[] r = samples[i][j].context;
					StringBuilder sb = new StringBuilder();
					for(String s : r) {
						sb.append(s+" ");
					}
					slines.add(sb.toString().trim());
				}
			}
		}
		List<String> samples22 = new ArrayList<>();
		for(String s : samples2) {
			int index = s.lastIndexOf(" ");
			s = s.substring(0, index);
			samples22.add(s);
		}
		System.out.println(slines.size()+":"+samples2.size());
		int count = 0;
		for(int i = 0; i < slines.size(); i++) {
			if(slines.get(i).equals(samples22.get(i))) {
				count++;
			} else {
				System.out.println("CoNLLSampleGenerator:"+slines.get(i));
				System.out.println("CoNLLFeatureGenerator:"+samples22.get(i));
				System.out.println();
			}
			
		}
		System.out.println(count);
		System.out.println(count == slines.size());
	}*/
	
	/**
1	���	���	a	ad	_	2	��ʽ	
2	����	����	v	v	_	0	���ĳɷ�	
3	̰��	̰��	v	v	_	7	�޶�	
4	��¸	��¸	n	n	_	3	��������	
5	��	��	u	udeng	_	3	��������	
6	����	����	n	n	_	7	�޶�	
7	����	����	v	vn	_	2	����	
	 */
}
