package com.outsider.model.crf.unsupervised;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.outsider.common.dataStructure.Table;
import com.outsider.common.util.MathUtils;
import com.outsider.model.crf.CRF;
import com.outsider.model.crf.FeatureFunction;
import com.outsider.model.crf.FeatureTemplate;
import com.outsider.model.hmm.SequenceNode;
import com.outsider.model.metric.Metric;
import com.outsider.nlp.segmenter.SegmentationPredictor;
import com.outsider.nlp.segmenter.SegmentationUtils;
import com.outsider.nlp.segmenter.SegmenterTest;
/**
 * 2018.12.7 
 * ����������һ�顶ͳ��ѧϰ�������е��Ƶ���׼��ʵ�������ᵽ�Ļ��ڸĽ��ĵ����߶��㷨��CRF
 * @author outsider
 * ����������:
 * 1.����������
 * ��ʽ��p(x,y)*f(x,y)��p(x,y)����ͳ�ģ���crf++�в�ͬ��һԪģ���Ӧ�˲�ͬ�ĸ��ʷֲ�������������
 * ��Ҫ��Ӧ��ģ�����������磺
 * (1) p(x[t],y[t]) 
 * (2) p(x[t-1],x[t],x[t+1],y[t])
 * (3) p(x[t-1],y[t])
 * (4) p(y[t-1],y[t])
 * ....
 * (1)(2)(3)(4)��Ӧ�˲�ͬ�ĸ��ʷֲ�����������������p(x,y){����ָ��ͳ��p(x,y)}���ʷֲ�������ʱ
 * Ӧ�����ֿ���
 * 
 * 2.ע��ģ��������
 * ��p(x,y)*f(x,y) = ��p(x) p(y|x) *f(x,y),����p(x)������������
 * �����p(x,y)��ͬ������ֲ��еģ������p(y,x)�е�xָ�������п���Ӱ��y���������x��
 * ��p(y|x)����ģ����Ҫ����ģ����unigram������p(yi|x)
 * �����bigram������ģ����Ҫ�������p(y[i-1],y[i]|x)
 */
public class UnsupervisedCRF extends CRF implements SegmentationPredictor{
	/**
	 * ѵ�����ݳ���
	 */
	private int T = 0;
	private static final long serialVersionUID = 1L;
	public UnsupervisedCRF() {
		super();
		reInitParameters();
	}

	public UnsupervisedCRF(int observationNum, int stateNum) {
		super(observationNum, stateNum);
		reInitParameters();
	}
	@Override
	public void train(List<SequenceNode> data) {
		train(getDefaultTemplate(), 0, data);
	}
	
	public String[] seg(String text) {
		int[] x = SegmentationUtils.str2int(text);
		int[] predict = this.veterbi(x);
		return SegmentationUtils.decode(predict, text);
	}
	@Override
	public void train(Table table, int xColumnIndex, int yColumnIndex) {
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
	@Override
	public String generateModelTemplate() {
		return null;
	}
	@Override
	public void reInitParameters() {
		transferProbabilityWeights = new double[stateNum][stateNum];
		featureFunctionTrie = null;
	}
	@Override
	public void train(String template, int maxOffsetOfTemplate, List<SequenceNode> nodes) {
		//��¼��ͬ�����������ֵĴ���
		this.T = nodes.size();
		//һ������ģ���е���(����:U03:%x[-2,0]%x[-1,0])��Ӧ��һ�����ʷֲ���ͳ�����ڸ÷ֲ��ķ�����Ƶ��
		//key:������ģ����ַ���id��value�Ƕ�Ӧ��count
		Map<Short, Integer> featureTemplateCount = new HashMap<>();
		int bigramFeatureCount = 0;//bigram��������
		//�ȶ��������ѵ�����
		//1. ͳ������ֲ�p(x,y) ��������������
		TreeMap<String,FeatureFunction> funcs = new TreeMap<>();
		parseStr2FeatureTemplate(template, "\n");
		//��ʼ��
		for(FeatureTemplate featureTemplate : featureTemplates) {
			Short fid = featureTemplate.getTemplateNumber();
			featureTemplateCount.put(fid, 0);
		}
		for(int i = 0; i < nodes.size() -1 ; i++ ) {
			SequenceNode node = nodes.get(i);
			//����ģ�������������
			//ֻ���Unigram
			generateFeatureFunction(nodes, i, funcs);
			//���Bigram ״̬ת��
			transferProbabilityWeights[node.getState()][nodes.get(i+1).getState()]++;
		}
		transferProbabilityWeights[nodes.get(T-2).getState()][nodes.get(T-1).getState()]++;
		generateFeatureFunction(nodes, T - 1, funcs);
		//����featureTemplateCount
		for(FeatureFunction featureFunction : funcs.values()) {
			Short templeId = featureFunction.getFeatureTemplateNum();
			int count = featureTemplateCount.get(templeId);
			for(double w : featureFunction.getWeight()) {
				count += w;
			}
			featureTemplateCount.replace(templeId, count);
		}
		for(double[] i : transferProbabilityWeights) {
			bigramFeatureCount += MathUtils.sum(i);
		}
		/**
		 *  put:1826448
			put:1826449
			put:1826448
			put:1826447
			put:1826448
			put:1826448
			put:1826447
		 */
		//��������������
		//�������õ�������������ΪDAT
		buildFeatureFunctionDAT(funcs);
		int[] x = new int[T];
		int[] y = new int[T];
		for(int i = 0; i < T; i++) {
			x[i] = nodes.get(i).getNodeIndex();
			y[i] = nodes.get(i).getState();
		}
		//��ʼ��������������������
		//���������������ӣ����ٵ�������
		int allFeatureNum = 20;
		//�Ľ��ĵ����߶��Ż�
		IIS(1E-4, -1, allFeatureNum, x, y, featureTemplateCount, bigramFeatureCount);
	}
	/**
	 * ��ʼ��Ȩ��
	 * ����֮ǰͳ����Ϊ0��Ȩ�س�ʼ��Ϊ��������ֱ�ӳ�ʼ��Ϊ0�����������⣬����Ϊ�е�Ȩ�ؿ���Ϊ����������Ϊ0��Ȩ��
	 * �������󣬶Խ������Ӱ��
	 * @return ���ֵ������ܺ�
	 */
	private void initWeights() {
		List<FeatureFunction> funcs = featureFunctionTrie.getValues();
		for(FeatureFunction featureFunction : funcs) {
			for(int i = 0; i < stateNum; i++) {
				if(featureFunction.getWeight()[i] == 0)
					featureFunction.getWeight()[i] = INFINITY;
				else
					featureFunction.getWeight()[i] = 0;
			}
		}
		for(int i = 0; i < stateNum; i++) {
			for(int j = 0; j < stateNum; j++) {
				if(transferProbabilityWeights[i][j] == 0)
					transferProbabilityWeights[i][j] = INFINITY;
				else
					transferProbabilityWeights[i][j] = 0;
			}
		}
	}
	/**
	 * ��ѵ�����ݲ�����������
	 * @param nodes
	 * @param i
	 * @param funcs
	 * @param unigramFeaturePositions
	 */
	protected void generateFeatureFunction(List<SequenceNode> nodes, int i, 
			TreeMap<String,FeatureFunction> funcs) {
		SequenceNode node = nodes.get(i);
		outer:for(FeatureTemplate featureTemplate : featureTemplates) {
			List<int[]> offsetList = featureTemplate.getOffsetList();
			//�������������Ĺ۲�x
			int[] x = new int[offsetList.size()];
			for(int j = 0; j < offsetList.size(); j++) {
				int[] offset = offsetList.get(j);
				//�ڿ�ʼ���߽�βʱ���ܳ���offset������Χ�����
				int row = i+offset[0];
				if(row < 0 || row >= nodes.size()) {
					continue outer;
				}
				SequenceNode offsetNode = nodes.get(row);
				x[j] = offsetNode.getNodeIndex();
			}
			//������������id�������Ƿ��Ѿ������Ժ��new
			String funcId = FeatureFunction.generateFeatureFuncStrId(x, featureTemplate.getTemplateNumber());
			FeatureFunction oldFunc = funcs.get(funcId);
			int cy = nodes.get(i).getState();
			if(oldFunc == null ) {
				//ע�⵱ǰ��״̬yֻ�ǵ�ǰ�еģ���ƫ�Ƶ���һ���޹�
				FeatureFunction featureFunction = new FeatureFunction(funcId, featureTemplate.getTemplateNumber());
				featureFunction.setX(x);
				double[] weight = new double[stateNum];
				weight[cy]++;
				featureFunction.setWeight(weight);
				funcs.put(funcId, featureFunction);
			} else {
				//����
				oldFunc.getWeight()[cy]++;
			}
		}
	}
	
	/**
	 * IIS(�Ľ������߶�)�Ż��㷨
	 * @param epsilon ���ȣ������в�����֮ǰ�����Ĳ�ֵ��С�ڸþ��ȣ�������ֹ
	 * @param maxIter ����������,Ĭ��Ϊ100000
	 * @param allFeatureNum ���������ܺͣ����ݵ�ǰ��ѵ�����ݿɵã���Ӧ��ͳ��ѧϰ�������е�T(x,y)
	 * @param unigramFeaturePositions ��Ԫ���������������г��ֵ�λ��
	 * @param bigarmFeaturePosition һԪ���������������г��ֵ�λ�ã�key����������id��value��λ��list
	 * @param x �۲�����
	 * @param y ״̬����
	 */
	public void IIS(double epsilon, int maxIter, int allFeatureNum, int[] x, int[] y, 
			Map<Short, Integer> featureTemplateCount,
			int bigramFeatureCount) {
		if(maxIter <= 0) 
			maxIter = 100000;
		//ֻ����һ����������
		double[][] priorExpectation = priorExpectation(x, y, featureTemplateCount, bigramFeatureCount);
		//��ʼ��Ȩ��
		initWeights();
		for(int i = 0; i < maxIter; i++) {
			System.out.println("iter..."+i);
			//����delta
			double[][] delta = calcDelta(x, y, allFeatureNum, priorExpectation, featureTemplateCount, bigramFeatureCount);
			//���²���
			//unigram
			double[][] oldWeights = new double[featureFunctionTrie.getKeySize() + transferProbabilityWeights.length][stateNum];
			for(int j = 0; j < featureFunctionTrie.getKeySize(); j++) {
				FeatureFunction featureFunction = featureFunctionTrie.getValue(j);
				for(int k = 0; k < stateNum; k++) {
					oldWeights[j][k] = featureFunction.getWeight()[k];
					featureFunction.getWeight()[k] = featureFunction.getWeight()[k] + delta[j][k];
				}
			}
			//bigram
			int offset = delta.length - transferProbabilityWeights.length;
			for(int j = 0; j < stateNum; j++) {
				for(int k = 0; k < stateNum; k++) {
					oldWeights[j + offset][k] = transferProbabilityWeights[j][k];
					transferProbabilityWeights[j][k] = transferProbabilityWeights[j][k] + delta[j+offset][k];
				}
			}
			
			/*System.out.println("״̬ת������");
			System.out.println("\tB\tM\tE\tS");
			System.out.println("B\t"+Arrays.toString(transferProbabilityWeights[0]));
			System.out.println("M\t"+Arrays.toString(transferProbabilityWeights[1]));
			System.out.println("E\t"+Arrays.toString(transferProbabilityWeights[2]));
			System.out.println("S\t"+Arrays.toString(transferProbabilityWeights[3]));*/
			String[] test = new String[] {
			"ԭ���⣺��ý�ĵ����ֳ�����һĻ�����ձ���������NNN��9��8�ձ�������ǰ���ձ������������������ս��֮һ��ֱ������ĸ���Ӻء������Ϻ�����ʱ��������й�����ս���ֽ����ټ��ӡ� ",
			"HanLP����һϵ��ģ�����㷨��ɵ�Java���߰���Ŀ�����ռ���Ȼ���Դ��������������е�Ӧ�á�",
			"������أ�ǿ���������Ϻ�ɫ���壬������ˮ�����Ҵ�������ԭ����������������ˮ����������������Ư�׼����������ռ���������̼���Ƽ��ȡ�",
			"��ҹ������ӡ�ͨ������ǳ�ݵ���Ů�ڰ�ҹ�������ӵ��龰,���������߶����������������",
			"����������[ί��]ǰ���Ǹ����̫�����ˣ�һ�㲻��Ʒ...@hankcs",
			"��̩���С��һ��ζ��Ҳû��...ÿ����������...�����ģ�����ʳ��2A�ĺô�",
			"����˹���ȡ����޶�˵�������Ҳ��ǻ��衣��ȫ�Ҷ��Ȱ����֣���Ҳ����������ô����",
			"����APPS��Sago Mini Toolbox�������Ӷ�������",
			"���������������������ι���ͳ�ƾֵ������",
			"2.34������Ȣ1.53��Ů��˿ �Ʒ�������û���⡣",
			"�㿴���º�������",
			"���ӳ����ֻ��ܷ���ؼֲ�˹����̬��"
			};
			for(String te : test) {
				System.out.println(Arrays.toString(seg(te)));
			}
			//SegmenterTest.score(this, "crf");
			//����Ƿ�ﵽ����Ҫ��
			boolean isConvergent = isConvergent(epsilon, oldWeights);
			int[] py = veterbi(x);
			float accuracy = Metric.accuracyScore(py, y);
			System.err.println(i);
			System.err.println("acc:"+accuracy);
			if(isConvergent) {
				System.out.println("�ﵽ����Ҫ��...");
				break;
			}
		}
	}
	
	/**
	 * ���Ȩ�ز����Ƿ�����
	 * @param epsilong ����Ҫ��
	 * @param oldWeights ��Ȩ��
	 * @return
	 */
	public boolean isConvergent(double epsilong, double[][] oldWeights) {
		for(int i = 0; i < oldWeights.length;i++) {
			FeatureFunction featureFunction = featureFunctionTrie.getValue(i);
			for(int j = 0; j < stateNum; j++) {
				if(Math.abs(featureFunction.getWeight()[j] - oldWeights[i][j]) > epsilong) {
					return false;
				}
			}
		}
		int offset = oldWeights.length - transferProbabilityWeights.length;
		for(int i = 0; i < transferProbabilityWeights.length; i++) {
			for(int j = 0; j < stateNum; j++) {
				if(Math.abs(transferProbabilityWeights[i][j] - oldWeights[offset + i][j]) > epsilong) {
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * ������²����õ��Ĳ�������delta
	 * ��  = (1/T) * (E[����ֲ�] / E[ģ�Ͳ���])
	 */
	public double[][] calcDelta(int[] x, int[] y, int allFeatureNum,
			double[][] priorExpectation,
			Map<Short, Integer> featureTemplateCount,
			int bigramFeatureCount) {
		double[][] delta = new double[featureFunctionTrie.getKeySize() + transferProbabilityWeights.length][stateNum];
		//����M(x)����
		double[][][] m_matrix = calcMmartix(x);
		//ǰ��
		double[][] alpha = forward(x, m_matrix);
		//����
		double[][] beta = backward(x, m_matrix);
		//ģ������
		double[][] expectation = modelExpectation(x, alpha, beta, m_matrix, featureTemplateCount, bigramFeatureCount);
		
		for(int i = 0; i < expectation.length; i++) {
			for(int j = 0; j < stateNum; j++) {
				delta[i][j] = (priorExpectation[i][j] - expectation[i][j]) / allFeatureNum;
			}
		}
		return delta;
	}
	@Override
	protected double computeBigramWeight(int y_i_1,int y_i) {
		return transferProbabilityWeights[y_i_1][y_i] == INFINITY ? 0 : transferProbabilityWeights[y_i_1][y_i];
	}
	/**
	 * ֮ǰ����ͳ��������Ϊ�����˼ලcrf�е�computeUnigramWeight����ռȨ�ص���������ʼ��ΪINFINITY
	 * �������жϣ����ۻ���Ȩ�ص�ʱ�����INFINITY�ᵼ�¾������⣿
	 * ����Ȩ����󲢲�����Ϊһ��logֵ�洢��������Ӧ�ý���ռȨ�ص�����������Ȩ��ֵ��ʼ��Ϊ0
	 * ���Ǵ洢Ϊlogֵ����������Ӧ�ø���
	 */
	@Override
	public double computeUnigramWeight(List<FeatureFunction> funcs, int state) {
		double weight = 0;
		for(int i = 0; i < funcs.size(); i++) {
			if(funcs.get(i).getWeight()[state] != INFINITY)
				weight += funcs.get(i).getWeight()[state];
		}
		return weight;
	}
	
	/**
	 * ����ȡ�������M(y[i-1],y[i] | x)����
	 * ע������û��ȡexp�������൱����ȡ��log���M����
	 * @param x
	 * @return ����M����һ����ά���飬M[i][y_i_1][y_i]
	 */
	public double[][][] calcMmartix(int[] x){
		double[][][] m_matrix = new double[T][stateNum][stateNum];
		//1.��ʼ��ʱ��û��y_i_1��û��ת�����������˻�Ϊ����
		List<FeatureFunction> funcs = getUnigramFeatureFunction(x, 0);
		for(int i = 0; i < stateNum; i++) {
			m_matrix[0][0][i] = computeUnigramWeight(funcs, i);
		}
		//1��t��λ��
		for(int t = 1; t < T; t++) {
			List<FeatureFunction> funcs2 = getUnigramFeatureFunction(x, t);
			for(int y_i_1 = 0; y_i_1 < stateNum; y_i_1++) {
				for(int y_i = 0; y_i < stateNum; y_i++) {
					m_matrix[t][y_i_1][y_i] = computeUnigramWeight(funcs2, y_i) + 
							computeBigramWeight(y_i_1, y_i);
				}
			}
		}
		return m_matrix;
	}
	
	/**
	 * ֵΪȡlog���ֵ
	 * ǰ���㷨
	 * @param x �۲�����x
	 * @param M(y[i-1],y[i])����
	 */
	public double[][] forward(int[] x, double[][][] m_matrix) {
		double[][] alpha = new double[T][stateNum];
		//��ʼt = 0
		for(int i = 0; i < stateNum; i++) {
			//Log( ��[0][i] ) = Log( M(Y0|x) )
			alpha[0][i] = m_matrix[0][0][i];
		}
		double[] tmp = new double[stateNum];
		for(int t = 1; t < T; t++) {
			for(int y_i = 0; y_i < stateNum; y_i++) {
				for(int y_i_1 = 0; y_i_1 < stateNum; y_i_1++) {
					tmp[y_i_1] = (alpha[t-1][y_i_1] + m_matrix[t][y_i_1][y_i]);
				}
				alpha[t][y_i] = logSumExp(tmp);
			}
		}
		return alpha;
	}
	
	/**
	 * �����㷨
	 * @param x �۲�����x
	 * @param M(y[i-1],y[i])����
	 */
	public double[][] backward(int[] x, double[][][] m_matrix) {
		double[][] beta = new double[T][stateNum];
		List<FeatureFunction> funcs = getUnigramFeatureFunction(x, T - 1);
		//��ʼû��y[i+1]
		for(int i = 0; i < stateNum; i++) {
			beta[T - 1][i] = computeUnigramWeight(funcs, i);
		}
		double[] tmp = new double[stateNum];
		for(int t = T - 2; t >= 0; t--) {
			for(int y_i = 0; y_i < stateNum; y_i++) {
				for(int y_ip1 = 0; y_ip1 < stateNum; y_ip1++) {
					tmp[y_ip1] = (beta[t + 1][y_ip1] + m_matrix[t + 1][y_i][y_ip1]);
				}
				beta[t][y_i] = logSumExp(tmp);
			}
		}
		return beta;
	}
	
	
	/**
	 * ���������ģ����������
	 * ����logֵ
	 * ֻ��p(x)����ֲ�����������
	 * ������������p(x,y)������
	 * p(x,y) = p_(x) * p(y|x)
	 * p_(x)�Ǿ���ֲ�
	 * @param x
	 * @param alpha
	 * @param beta
	 * @param unigramFeaturePositions ��IIS˵��
	 * @param bigarmFeaturePosition  ��IIS˵��
	 * @param m_matrix M(y[i-1],y[i]|x)����
	 */
	public double[][] modelExpectation(int[] x, 
			double[][] alpha, double[][] beta, 
			double[][][] m_matrix,
			Map<Short, Integer> featureTemplateCount,
			int bigramFeatureCount) {
		double logZ = logSumExp(alpha[T-1]);
		//double logZ_beta = logSumExp(beta[0]); ����alpha�����logZ��һ����ֵ
		double[][] expectations = new double[featureFunctionTrie.getKeySize() + transferProbabilityWeights.length][stateNum];
		//unigram
		for(int t = 0; t < x.length; t++) {
			outer:for(FeatureTemplate template : featureTemplates) {
				List<int[]> offsetList = template.getOffsetList();
				int featureCount = featureTemplateCount.get(template.getTemplateNumber());
				int[] xx = new int[offsetList.size()];
				for(int i = 0; i < offsetList.size(); i++) {
					int[] offset = offsetList.get(i);
					int p = t + offset[0];
					if(p < 0 || p >= x.length)
						continue outer;
					xx[i] = x[p];
				}
				String funcId = FeatureFunction.generateFeatureFuncStrId(xx, template.getTemplateNumber());
				FeatureFunction featureFunction = featureFunctionTrie.getValue(funcId);
				int intId = featureFunctionTrie.intIdOf(funcId);
				double[] w = featureFunction.getWeight();
				for(int y = 0; y < stateNum; y++) {
					//Ȩ�س�ʼ��ΪINFINITY��������������Ȩ��Ϊ0
					if(w[y] != INFINITY) {
						//ģ�͹��� p(y|x) = 
						double log_pyx = alpha[t][y] + beta[t][y] - logZ;
						expectations[intId][y] += (Math.exp(log_pyx -Math.log(featureCount)));
					} else { 
						expectations[intId][y] = INFINITY;
					}
				}
			}
		}
		int offset = expectations.length - transferProbabilityWeights.length;
		//bigram
		for(int t = 1; t < x.length; t++) {
			for(int y_i_1 = 0; y_i_1 < stateNum; y_i_1++) {
				for(int y_i = 0; y_i < stateNum; y_i++) {
					//ģ�͹��� p(y[t-1],y[t]|x)
					if(transferProbabilityWeights[y_i_1][y_i] != INFINITY) {
						double log_py = alpha[t-1][y_i_1] + m_matrix[t][y_i_1][y_i] + beta[t][y_i] - logZ;
						expectations[offset+y_i_1][y_i] += (Math.exp(log_py - Math.log(bigramFeatureCount) )); 
						//System.out.println("bigram["+y_i_1+","+y_i+"]"+"_modelExpectation:"+(Math.exp(log_py - Math.log(bigramFeatureCount) )));
						//System.out.println("bigram["+y_i_1+","+y_i+"]"+"_logModelExpecta:"+(log_py - Math.log(bigramFeatureCount)));
					} else {
						expectations[offset+y_i_1][y_i] = INFINITY;
					}
				}
			}
		}
		//ȡ����
		for(int i = 0; i < expectations.length; i++) {
			for(int j = 0; j < stateNum; j++) {
				if(expectations[i][j] != INFINITY)
					expectations[i][j] = Math.log(expectations[i][j]);
			}
		}
		return expectations;
	}
	
	/**
	 * ���������ģ�͵���������
	 * ����������logֵ����������ֵδ0.ֵΪinfinity
	 * ������������ֲ�p(x,y)������
	 * ����������:
	 * ��ʽ�Ц�p(x,y)*f(x,y)��p(x,y)����ͳ�ģ���crf++�в�ͬ��һԪģ���Ӧ�˲�ͬ�ĸ��ʷֲ�������������
	 * ��Ҫ��Ӧ��ģ�����������磺
	 * (1) p(x[t],y[t]) 
	 * (2) p(x[t-1],x[t],x[t+1],y[t])
	 * (3) p(x[t-1],y[t])
	 * (4) p(y[t-1],y[t])
	 * ....
	 * (1)(2)(3)(4)��Ӧ�˲�ͬ�ĸ��ʷֲ�����������������p(x,y){����ָ��ͳ��p(x,y)}���ʷֲ�������ʱ
	 * Ӧ�����ֿ���
	 * @param x
	 * @param y
	 * @param featureTemplateCount
	 * @param bigramFeatureCount
	 * @return
	 */
	public double[][] priorExpectation(int[] x, int[] y,
			Map<Short, Integer> featureTemplateCount,
			int bigramFeatureCount){
		//������������������ģ��������������Щδ���ֵ�����
		//��������������Stridӳ��Ϊintֵ��Ϊ�����±�
		//��������ŵ���bigram��������������
		double[][] expectations = new double[featureFunctionTrie.getKeySize() + transferProbabilityWeights.length][stateNum];
		//unigram
		List<FeatureFunction> featureFunctions = featureFunctionTrie.getValues();
		for(FeatureFunction featureFunction : featureFunctions) {
			int count = featureTemplateCount.get(featureFunction.getFeatureTemplateNum());
			//��ȡ
			double[] expectaionArr = expectations[featureFunctionTrie.intIdOf(featureFunction.getStrId())];
			double[] w = featureFunction.getWeight();
			for(int i = 0; i < stateNum; i++) {
				if(w[i] == 0) {
					expectaionArr[i] = INFINITY;
				} else {
					expectaionArr[i] = Math.log(w[i]) - Math.log(count);
				}
			}
		}
		//bigram
		int offset = expectations.length - transferProbabilityWeights.length;
		for(int i = 0; i < stateNum; i++) {
			for(int j = 0; j < stateNum; j++) {
				if(transferProbabilityWeights[i][j] == 0) {
					expectations[offset + i][j] = INFINITY;
				} else {
					expectations[offset + i][j] = Math.log(transferProbabilityWeights[i][j]) - Math.log(bigramFeatureCount);
				}
			}
		}
		return expectations;
	}
	/**
	 * logSumExp���㼼��
	 * @param arr
	 * @return
	 */
	public double logSumExp(double[] arr) {
		if(arr.length == 0) return INFINITY;
		double max = MathUtils.max(arr);
		double res = 0;
		for(int i = 0; i < arr.length; i++) {
			res += Math.exp(arr[i] - max);
		}
		return max + Math.log(res);
	}

	@Override
	public List<String[]> seg(String[] texts) {
		return null;
	}
}
