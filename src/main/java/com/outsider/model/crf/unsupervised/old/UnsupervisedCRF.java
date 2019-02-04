package com.outsider.model.crf.unsupervised.old;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.outsider.common.util.MathUtils;
import com.outsider.model.crf.CRF;
import com.outsider.model.crf.FeatureFunction;
import com.outsider.model.crf.FeatureTemplate;
import com.outsider.model.hmm.SequenceNode;

/**
 * �޼ල���Ǵ��������CRF
 * Ŀǰ�����˵�3���汾���ݶȼ��㣬��calcGradient3����û�м���Unigram���ݶ�
 * Ҳ����˵û�и�����Ӧ�Ĳ�������Ȼ�ܲ���ʲô�������׼��������
 * ��һ���汾������
 * 
 * @author outsider
 */
public abstract class UnsupervisedCRF extends CRF{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//ѵ�����ݵ����г���
	private int sequenceLen;
	public UnsupervisedCRF() {
		super();
		reInitParameters();
	}

	public UnsupervisedCRF(int observationNum, int stateNum) {
		super(observationNum, stateNum);
		reInitParameters();
	}

	/**
	 * p(x,y)��ͳ������ֲ�
	 * ����ȡ����
	 * ����x������y
	 */
	private double[][] priorXYDistribution;
	/**
	 * p(x)��ͳ������ֲ�
	 * ����ȡ����
	 */
	private double[] priorXDistribution;
	/**
	 * ��ǰλ���±�����Ӧ����������Ȩ���ۻ�
	 * ��ȡ����
	 * @param x
	 * @param y_i_1
	 * @param y_i
	 * @param i
	 * @return
	 */
	public double computeWeight(int[] x, int y_i_1, int y_i, int i) {
		List<FeatureFunction> funcs = getUnigramFeatureFunction(x, i);
		double w1 = computeUnigramWeight(funcs, y_i);
		double w2 = computeBigramWeight(y_i_1, y_i);
		return w1 + w2;
	}
	
	public double computeLogWeight(int[] x, int y_i_1, int y_i, int i) {
		List<FeatureFunction> funcs = getUnigramFeatureFunction(x, i);
		double w1 = computeUnigramWeight(funcs, y_i);
		double w2 = computeBigramWeight(y_i_1, y_i);
		double w = w1 + w2;
		if(w <= 0) return INFINITY;
		return Math.log(w);
	}
	/**
	 * ǰ���㷨�����ݵ�ǰ����������Ȩ��ֵ�������
	 * ���ڦ���������ȡ����
	 * @param nodes
	 * @return
	 */
	public double[][] calcAlpha(int[] x){
		double[][] alpha = new double[sequenceLen][stateNum];
		//alpha��ֵ�͵���exp(w*f(y_1,x));,ȡ�������ֻ��w*f(y_1,x)
		List<FeatureFunction> funcs = getUnigramFeatureFunction(x, 0);
		for(int i = 0; i < stateNum; i++) {
			alpha[0][i] = computeUnigramLogWeight(funcs, i);
		}
		double[] tmp = new double[stateNum];
		for(int t = 1;t < sequenceLen; t++) {
			for(int i = 0; i < stateNum; i++) {
				//alpha[t][i] = alpha[t-1][i] + computeWeight(x, y_i_1, y_i, i)
				//y_i_1�ǲ�ȷ����
				for(int j = 0; j < stateNum; j++) {
					tmp[j] = (alpha[t-1][j] + computeLogWeight(x, j, i, t));
				}
				alpha[t][i] = MathUtils.logSum(tmp);
			}
		}
		logger.info(Arrays.toString(alpha[sequenceLen-1]));
		return alpha;
	}
	/**
	 * �����㷨�����ݵ�ǰ����������Ȩ��ֵ�������
	 * ȡ����
	 * @param nodes
	 * @return
	 */
	public double[][] calcBeta(int[] x){
		double[][] beta = new double[sequenceLen][stateNum];
		List<FeatureFunction> funcs = getUnigramFeatureFunction(x, sequenceLen-1);
		//beta��ֵ
		for(int i = 0; i < stateNum; i++) {
			beta[sequenceLen-1][i] = computeUnigramLogWeight(funcs, i);
		}
		double[] tmp = new double[stateNum];
		for(int t = sequenceLen-2; t >= 0; t--) {
			for(int i = 0; i < stateNum; i++) {
				//y_i+1�ǲ�ȷ����
				for(int j = 0; j < stateNum; j++) {
					tmp[j] = beta[t+1][j] +computeLogWeight(x, i, j, t);
				}
				beta[t][i] = MathUtils.logSum(tmp);
			}
		}
		return beta;
	}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
	
	public double computeUnigramLogWeight(List<FeatureFunction> funcs, int state) {
		double w = computeUnigramWeight(funcs, state);
		if(w <= 0) return INFINITY;
		return Math.log(w);
	}
	
	@Override
	public void train(String template, int maxOffsetOfTemplate, List<SequenceNode> nodes) {
		//��������������ѵ�������г��ֵ�λ�ã�keyʱ��������id
		//��ǰֻ������Unigram�����ĳ���λ�ã���û�б���Bigram���ֵ�λ��
		//���׵�Bigram��ʱ��������
		Map<String, PositionsOfFeatureFunctionShowing> positionsOfFuncShowing = new HashMap<>();
		//��¼��ͬ�����������ֵĴ���
		this.sequenceLen = nodes.size();
		//�ȶ��������ѵ�����
		//1. ͳ������ֲ�p(x,y) ��������������
		TreeMap<String,FeatureFunction> funcs = new TreeMap<>();
		parseStr2FeatureTemplate(template, "\n");
		for(int i = 0; i < nodes.size() -1 ; i++ ) {
			SequenceNode node = nodes.get(i);
			//ͳ������ֲ�
			priorXDistribution[nodes.get(i).getNodeIndex()]++;
			priorXYDistribution[node.getNodeIndex()][node.getState()]++;
			//����ģ�������������
			//ֻ���Unigram
			generateAndReturnFeatureFunction(nodes, i, funcs, positionsOfFuncShowing);
			//���Bigram ״̬ת��
			transferProbabilityWeights[node.getState()][nodes.get(i+1).getState()] = 0.01;
		}
		//�������һ��ѭ��
		priorXDistribution[nodes.get(sequenceLen-1).getNodeIndex()]++;
		priorXYDistribution[nodes.get(sequenceLen-1).getNodeIndex()][nodes.get(sequenceLen-1).getState()]++;
		logProbaOfprioriDistribution();
		generateAndReturnFeatureFunction(nodes, nodes.size()-1, funcs, positionsOfFuncShowing);
		//��������������
		
		//�������õ�������������ΪDAT
		buildFeatureFunctionDAT(funcs);
		//TODO ֻȡ10��ڵ�����
		sequenceLen = 100000;
		int[] observations = new int[sequenceLen];
		for(int i = 0; i < sequenceLen; i++) {
			observations[i] = nodes.get(i).getNodeIndex();
		}
		logger.info("��ʼ�ݶ��½�....");
		//2. ִ���ݶ��½�
			//�ݶ��½��������漰���������㣬ǰ������㷨���������������ȵ�
		//batchGradientDecent(0, 0.00001, 0.001, 100, observations);
		batchGradientDecent2(0, 0.01, 0.001, 20, observations, positionsOfFuncShowing);
	}
	
	protected void generateAndReturnFeatureFunction(List<SequenceNode> nodes, int i, TreeMap<String,FeatureFunction> funcs
			,Map<String, PositionsOfFeatureFunctionShowing> positionsOfFuncShowing) {
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
			FeatureFunction old = funcs.get(funcId);
			if(old == null ) {
				//ע�⵱ǰ��״̬yֻ�ǵ�ǰ�еģ���ƫ�Ƶ���һ���޹�
				FeatureFunction featureFunction = new FeatureFunction(funcId, featureTemplate.getTemplateNumber());
				featureFunction.setX(x);
				double[] weight = new double[stateNum];
				//��ʼȨ��Ϊ0.001
				weight[nodes.get(i).getState()] = 0.001;
				featureFunction.setWeight(weight);
				funcs.put(funcId, featureFunction);
				//����PositionsOfFeatureFunctionShowing������λ��
				PositionsOfFeatureFunctionShowing indexs = new PositionsOfFeatureFunctionShowing(stateNum);
				if(i < 100000)
					indexs.addIndex(i, nodes.get(i).getState());
				positionsOfFuncShowing.put(funcId, indexs);
			} else {
				//����
				//old.getWeight()[nodes.get(i).getState()]++;
				//����λ��
				PositionsOfFeatureFunctionShowing indexs = positionsOfFuncShowing.get(funcId);
				if(i < 100000)
					indexs.addIndex(i, nodes.get(i).getState());
			}
		}
	}
	
	public void train(List<SequenceNode> nodes) {
		train(getDefaultTemplate(), 2, nodes);
	}
	
	/**
	 * ��ȡCRF�ɵ�Ȩ�ز���
	 * @return
	 */
	public List<Double> getCRFeatureFunctionWeights() {
		List<Double> weights = new ArrayList<>();
		for(FeatureFunction featureFunction: featureFunctionTrie.getValues()) {
			for(int i = 0; i < stateNum; i++) {
				if(featureFunction.getWeight()[i] != 0) {
					weights.add(featureFunction.getWeight()[i]);
				}
			}
		}
		for(int i = 0; i < stateNum; i++) {
			for(int j = 0; j < stateNum; j++) {
				weights.add(transferProbabilityWeights[i][j]);
			}
		}
		return weights;
	}
	
	public void updateCRFeatureFunctionWeights(List<Double> weights) {
		int count = 0;
		for(FeatureFunction featureFunction: featureFunctionTrie.getValues()) {
			for(int i = 0; i < stateNum; i++) {
				if(featureFunction.getWeight()[i] != 0) {
					featureFunction.getWeight()[i] = weights.get(count++);
				}
			}
		}
		for(int i = 0; i < stateNum; i++) {
			for(int j = 0; j < stateNum; j++) {
				transferProbabilityWeights[i][j] = weights.get(count++);
			}
		}
		logger.info("updateCRFeatureFunctionWeights:");
		logger.info("count == weights.size:"+(count == weights.size()));
	}
	

	/**
	 * CRF�����ݶ��½����
	 * @param lambda ����ϵ��
	 * @param alpha ѧϰ��
	 * @param epsilon ����
	 * @param maxIter ����������������<=0 ��������Ϊ����������������
	 * @param observations �۲�����
	 */
	public void batchGradientDecent(double lambda, double alpha, double epsilon, int maxIter,
			int[] observations) {
		int iter = 1;
		//��������������������<=0˵�������������������
		//��ô����������������ΪĬ�����޴�
		if(maxIter <= 0) {
			maxIter = Integer.MAX_VALUE;
		}
		while(iter <= maxIter) {
			//���²���
			List<Double> oldParameter = getCRFeatureFunctionWeights();
			//1.����alpha
			logger.info("����alpha...");
			double[][] crfAlpha = calcAlpha(observations);
			logger.info("�������...");
			//2.����beta
			logger.info("����beta...");
			double[][] crfBeta = calcBeta(observations);
			logger.info("�������...");
			//3.�����ݶ�
			logger.info("�����ݶ�...");
			double gradient = calcGradient2(crfAlpha, crfBeta, observations);
			logger.info("�������...");
			//4.���²���
			for(int i = 0; i < oldParameter.size(); i++) {
				double tmp = oldParameter.get(i) - alpha * gradient;
				oldParameter.set(i, tmp);
			}
			updateCRFeatureFunctionWeights(oldParameter);
			logger.info("ת����������:");
			for(int i = 0; i < stateNum; i++) {
				logger.info(Arrays.toString(transferProbabilityWeights[i]));
			}
			//֮ǰ������������ˣ�����ֱ���ж������ݶ�С��һ��ֵ����Ϊ����������С��
			//�ж��Ƿ�����
			logger.info("�ݶ�ֵ:"+gradient);
			if(Math.abs(gradient) <= epsilon) {
				//�����������ѭ��������ѵ��
				return;
			}
			logger.info("iter:"+iter);
			iter++;
		}
		//�ﵽ��������������û�з��ز���˵��������û��������
		System.err.println("�ﵽ����������,�������ʧ��!");
		System.err.println("solve parameters failed!");
	}
	public void updateUnigramWeights(Map<String, double[]> gradietns, double alpha) {
		Set<Entry<String, double[]>> entrys = gradietns.entrySet();
		for(Entry<String, double[]> entry : entrys) {
			FeatureFunction func = featureFunctionTrie.getValue(entry.getKey());
			double[] gradient = entry.getValue();
			double[] weights = func.getWeight();
			for(int i = 0; i < stateNum; i++) {
				if(weights[i] != 0) {
					weights[i] = weights[i] - alpha * gradient[i];
					/*if(gradient[i] == 0) {
						System.out.println("�ݶȳ�����0���Ƿ񲻸ñ����µĲ�����������???");
					}*/
				}
			}
		}
	}
	
	public void batchGradientDecent2(double lambda, double alpha, double epsilon, int maxIter,
			int[] observations, Map<String, PositionsOfFeatureFunctionShowing> positionsOfFuncShowing) {
		int iter = 1;
		//��������������������<=0˵�������������������
		//��ô����������������ΪĬ�����޴�
		if(maxIter <= 0) {
			maxIter = Integer.MAX_VALUE;
		}
		while(iter <= maxIter) {
			//1.����alpha
			logger.info("����alpha...");
			double[][] crfAlpha = calcAlpha(observations);
			logger.info("�������...");
			//2.����beta
			logger.info("����beta...");
			double[][] crfBeta = calcBeta(observations);
			logger.info("�������...");
			//3.�����ݶ�
			logger.info("�����ݶ�...");
			Map<String, double[]> gradients = calcGradient3(crfAlpha, crfBeta, observations, positionsOfFuncShowing);
			logger.info("�������...");
			//4.���²���
			updateUnigramWeights(gradients, alpha);
			logger.info("ת����������:");
			for(int i = 0; i < stateNum; i++) {
				logger.info(Arrays.toString(transferProbabilityWeights[i]));
			}
			//֮ǰ������������ˣ�����ֱ���ж������ݶ�С��һ��ֵ����Ϊ����������С��
			//�ж��Ƿ�����
			//logger.info("�ݶ�ֵ:"+gradient);
			//�ж��Ƿ�����
			Collection<double[]> values = gradients.values();
			Iterator<double[]> iterator = values.iterator();
			boolean isConvergent = true;
			int count = 0;
			outer:while(iterator.hasNext()) {
				double[] cgradient = iterator.next();
				if(count == 0) {
					logger.info("Unigramǰ4���ݶ�ֵ:"+Arrays.toString(cgradient));
				}
				for(int i = 0; i < stateNum; i++) {
					//ֻҪ��һ���ݶ��Ǵ���epsilon�ģ���ô��û������
					if(cgradient[i] != 0 && Math.abs(cgradient[i]) > epsilon) {
						isConvergent = false;
						break outer;
					}
				}
			}
			//����������˳�
			if(isConvergent) {
				return;
			}
			logger.info("iter:"+iter);
			iter++;
		}
		//�ﵽ��������������û�з��ز���˵��������û��������
		System.err.println("�ﵽ����������,�������ʧ��!");
		System.err.println("solve parameters failed!");
	}
	//����ֲ�����ȡ����
	private void logProbaOfprioriDistribution() {
		//������x�ķֲ�����x��y�ķֲ����һ�������еĳ��ȣ��������ﲻ������һ��
		//x����ֲ�ȡ����
		double sum =Math.log( sequenceLen );
		for(int i =0; i < observationNum; i++) {
			if(priorXDistribution[i] == 0) {
				priorXDistribution[i] = INFINITY;
			} else {
				priorXDistribution[i] = Math.log(priorXDistribution[i]) - sum;
			}
		}
		//xy�ֲ�����ȡ����
		for(int i = 0; i < observationNum; i++) {
			for(int j = 0; j < stateNum; j++) {
				if(priorXYDistribution[i][j] == 0) {
					priorXYDistribution[i][j] = INFINITY;
				} else {
					priorXYDistribution[i][j] = Math.log(priorXYDistribution[i][j]) - sum;
				}
			}
		}
	}
	
	public double calcGradient2(double[][] alpha, double[][] beta, int[] observations) {
		double gradient  = 0;
		//1.�����ݶ��еĵ�һ����
		//������㦲p(y|x)*f(x,y)�ļ�����
		double[] pyGivenXMultiFuncNum = new double[sequenceLen + 1];
		double[] funcExceptionsUnderPXY = new double[sequenceLen];
		double[] tmp = new double[stateNum];
		double[] tmp5 = new double[stateNum];
		double zx = MathUtils.logSum(alpha[sequenceLen - 1]);
		List<FeatureFunction> startfuncs = getUnigramFeatureFunction(observations, 0);
		//��ʼ��i = 1ʱ��yi_1 = -1;
		for(int i = 0; i < stateNum; i++) {
			double logCount = Math.log(countFeatureFunction(startfuncs, i));
			double logWeight = 0;
			
			//�ݶȵ�1����
			tmp[i] = logCount
					+ 0 + computeUnigramLogWeight(startfuncs, i)
					+ beta[0][i] - zx;
			//�ݶȵ�2����
			tmp5[i] = priorXYDistribution[observations[0]][i] + logCount;
		}
		funcExceptionsUnderPXY[0] = MathUtils.logSum(tmp5);
		pyGivenXMultiFuncNum[0] = MathUtils.logSum(tmp);
		//��βi = T��������yT,ֻ��YT_1ʱ�Ϸ���
		List<FeatureFunction> endFuncs = getUnigramFeatureFunction(observations, sequenceLen - 1);
		for(int i = 0; i < stateNum; i++) {
			tmp[i] = Math.log(countFeatureFunction(endFuncs, i))
					+ alpha[sequenceLen -1][i] + computeUnigramLogWeight(endFuncs, i)
					+ 0 - zx;
		}
		pyGivenXMultiFuncNum[sequenceLen] = MathUtils.logSum(tmp);
		//�����м䲿��
		for(int i = 1; i < sequenceLen; i++) {
			List<FeatureFunction> funcs = getUnigramFeatureFunction(observations, i);
			double[] tmp2 = new double[stateNum];
			for(int yi = 0; yi < stateNum; yi++) {
				//+1����Ϊת��������������Ҫȡ����
				double[] tmp1 = new double[stateNum];
				double funcNum = Math.log(countFeatureFunction(funcs, yi) + 1);
				//�ݶȵ�2����
				tmp5[yi] = funcNum + priorXYDistribution[observations[i]][yi];
				//�ݶȵ�2���ֽ���
				for(int yi_1 = 0; yi_1 < stateNum; yi_1++) {
					tmp1[yi_1] = funcNum + alpha[i - 1][yi_1] + computeLogWeight(observations, yi_1, yi, i)
					+ beta[i][yi] - zx;
				}
				tmp2[yi] = MathUtils.logSum(tmp1);
			}
			funcExceptionsUnderPXY[i] = MathUtils.logSum(tmp5);
			pyGivenXMultiFuncNum[i] = MathUtils.logSum(tmp2);
		}
		//�����������
		double[] tmp4 = new double[sequenceLen];
		double a = MathUtils.logSum(pyGivenXMultiFuncNum);
		for(int i = 0; i < sequenceLen; i++) {
			tmp4[i] = priorXDistribution[observations[i]] + a;
		}
		double gradietnPart1 = MathUtils.logSum(tmp4);
		gradietnPart1 = Math.exp(gradietnPart1);//ȡexp��ԭ
		double gradientPart2 = MathUtils.logSum(funcExceptionsUnderPXY);
		gradientPart2 = Math.exp(gradientPart2);
		//2.�����ݶȵĵ�2����
		gradient = gradietnPart1 - gradientPart2;
		return gradient;
	}
	
	/**
	 * ����ˣ�ÿ�������������ݶȲ���һ��
	 * ��ʱֻ����Unigram���������Ĳ�������
	 * @param alpha
	 * @param beta
	 * @param observations
	 * @return
	 */
	public Map<String, double[]> calcGradient3(double[][] alpha, double[][] beta, int[] observations, Map<String, PositionsOfFeatureFunctionShowing> positions) {
		//��2������
		//1.�����ݶȵĵ�һ���� p(Yi=yi,Yi-1=yi-1|x)��ͨ�õģ��ȶԴ˽��м���
		
		//3.�����ݶȷֵڶ����֣������p(x,y)����������Ϣ�ͺܺü��㣬ֱ���õ���һ����ͳ�Ƶ�����������ѵ��������
		//���ֵĸ���
		return calcUnigramGradient(alpha, beta, observations, positions);
	}
	public double[][] calcBigramGradient(double[][] alpha, double[][] beta, int[] observations){
		
		return null;
	}
	public Map<String, double[]> calcUnigramGradient(double[][] alpha, double[][] beta, int[] observations, Map<String, PositionsOfFeatureFunctionShowing> positions){
		double zx = MathUtils.logSum(alpha[sequenceLen - 1]);
		Map<String, double[]> gradients = new HashMap<>();
		for(FeatureFunction function : featureFunctionTrie.getValues()) {
			PositionsOfFeatureFunctionShowing indexs = positions.get(function.getStrId());
			double[] gradient = new double[stateNum];
			gradients.put(function.getStrId(), gradient);
			for(int i = 0; i < stateNum; i++) {
				//Ȩ�س�ʼ��Ϊ0��˵�����ڸ���������
				if(function.getWeight()[i] != 0) {
					List<Integer> indexss = indexs.getPositionsOfFeatureFunction(i);
					//����p(x)*p(yi|x)
					//�����ݶȵ�һ����
					double[] tmp = new double[indexss.size()];
					//�����ݶȵڶ�����p(x,y)
					double[] tmp2 = new double[indexss.size()];
					for(int j = 0; j < tmp.length; j++) {
						tmp[j] = (alpha[indexss.get(j)][i] + beta[indexss.get(j)][i] 
								+priorXDistribution[observations[indexss.get(j)]]- zx);
						tmp2[j] = priorXYDistribution[observations[indexss.get(j)]][i];
					}
					//֮ǰȡ�˶�������Ҫȡexp����
					double part1Exp = Math.exp(MathUtils.logSum(tmp));
					double part2Exp = Math.exp(MathUtils.logSum(tmp2));
					gradient[i] = part1Exp - part2Exp;
				}
			}//-0.004027248159597632
			//System.out.println("gradient:"+Arrays.toString(gradient));
		}
		return gradients;
	}   
	/**
	 * ��һ��ָ��������������ѵ�����г��ֵĴ���
	 * ��������ڲ�������������ʱ���Ѿ�������
	 * ����Ҫ���ظ�����,����ֱ��ʹ����������������ͳ��
	 * @param observations
	 * @param y
	 */
	/*public Map<String, int[]> countFeatureFunction(int[] observations, int[] y) {
		Map<String, int[]> funcCounts = new HashMap<>();
		for(FeatureFunction featureFunction : featureFunctionTrie.getValues()) {
			int[] count = new int[stateNum];
			double[] weight = featureFunction.getWeight();
			for(int i = 0; i < stateNum; i++) {
				count[i] = (int) weight[i];
			}
			funcCounts.put(featureFunction.getStrId(), count);
		}
		return funcCounts;
	}*/
	
	/**
	 * ����ĳ��״̬�µ�������������
	 * ���Ȩ��Ϊ0����Ϊ����������������
	 * @param funcs
	 * @param y
	 * @return
	 */
	public int countFeatureFunction(List<FeatureFunction> funcs , int y) {
		int count = 0;
		for(FeatureFunction featureFunction : funcs) {
			if(featureFunction.getWeight()[y] != 0) {
				count++;
			}
		}
		return count;
	}
	
	@Override
	public void reInitParameters() {
		transferProbabilityWeights = new double[stateNum][stateNum];
		featureFunctionTrie = null;
		priorXDistribution = new double[observationNum];
		priorXYDistribution = new double[observationNum][stateNum];
	}
}
