package com.outsider.model.crf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.outsider.common.util.MathUtils;
import com.outsider.model.hmm.SequenceNode;

/**
 * �ලѧϰ��CRF
 * @author outsider
 *
 */
public abstract class SupervisedCRF extends CRF{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SupervisedCRF() {
		super();
	}

	public SupervisedCRF(int observationNum, int stateNum) {
		super(observationNum, stateNum);
	}

	/**
	 * �޼ලѧϰ��Ҫ��һ������
	 */
	public void logProb() {
		//ת�Ƹ��ʹ�һ��
		for(int i = 0; i < stateNum; i++) {
			double logSum = MathUtils.sum(transferProbabilityWeights[i]);
			logSum = Math.log(logSum);
			for(int j = 0; j  < stateNum; j++) {
				if(transferProbabilityWeights[i][j] != 0) {
					transferProbabilityWeights[i][j] = 
					Math.log(transferProbabilityWeights[i][j]) - logSum;
				} else {
					transferProbabilityWeights[i][j]  = INFINITY;
				}
			}
		}
		List<FeatureFunction> functions = featureFunctionTrie.getValues();
		//���������������ҳ���Щ��ͬһ������ģ�������������������һ��һ��״̬�µĹ۲����
		int funcAmount = 0;
		Map<Short, List<FeatureFunction>> funcsGroupByTemple = new HashMap<>();
		//������������Ϊ��ͬģ���µ���������
		for(FeatureTemplate template : featureTemplates) {
			funcsGroupByTemple.put(template.getTemplateNumber(), new ArrayList<>());
		}
		for(FeatureFunction func : functions) {
			funcsGroupByTemple.get(func.getFeatureTemplateNum()).add(func);
		}
		//��һ��
		Set<Entry<Short, List<FeatureFunction>>> entrys = funcsGroupByTemple.entrySet();
		for(Entry<Short, List<FeatureFunction>> entry : entrys) {
			List<FeatureFunction> funcs = entry.getValue();
			//��ͬһģ���µĲ�ͬ״̬������������һ��
			//Ҳ���ǹ�һ����ͬ״̬�µĹ۲�ֲ�
			//��Ӻ�
			double[] logSum = new double[stateNum];
			for(int i = 0; i < funcs.size(); i++) {
				for(int j = 0; j < stateNum;j++) {
					logSum[j] += funcs.get(i).getWeight()[j];
				}
			}
			for(int i = 0; i < stateNum;i++) {
				logSum[i] = Math.log(logSum[i]); 
			}
			//��һ��
			for(int i = 0; i < funcs.size(); i++) {
				FeatureFunction f = funcs.get(i);
				double[] weight = f.getWeight();
				for(int j = 0; j < stateNum; j++) {
					if(weight[j] != 0) {
						weight[j] = Math.log(weight[j]) - logSum[j];
					} else {
						weight[j] = INFINITY;
					}
				}
			}
			funcAmount += funcs.size();
			logger.info("�������ΪU"+entry.getKey()+"������������:"+funcs.size()+"��");
		}
		logger.info("�ܵ�Unigram������������:"+funcAmount);
	}
	
	@Override
	public void train(List<SequenceNode> nodes) {
		this.train(getDefaultTemplate(), 2, nodes);
		
	}
	
	@Override
	public String getDefaultTemplate() {
		/*return "# Unigram\n" +
		        "U0:%x[-1,0]\n" +
		        "U1:%x[0,0]\n" +
		        "U2:%x[1,0]\n" +
		        "U3:%x[-2,0]%x[-1,0]\n" +
		        "U4:%x[-1,0]%x[0,0]\n" +
		        "U5:%x[0,0]%x[1,0]\n" +
		        "U6:%x[1,0]%x[2,0]\n" +
		        "\n" +
		        "# Bigram\n" +
		        "B";*/
		return super.getDefaultTemplate();
	}
	
	/**
	 * �ලѧϰCRF������������ʱ��Ҫͳ�ƣ�ѧϰȨ�ز��������ǼලCRF����Ҫ
	 */
	@Override
	protected void generateFeatureFunction(List<SequenceNode> nodes, int i, TreeMap<String, FeatureFunction> funcs) {
		SequenceNode node = nodes.get(i);
		outer:for(FeatureTemplate featureTemplate : featureTemplates) {
			List<int[]> offsetList = featureTemplate.getOffsetList();
			//�������������Ĺ۲�x
			int[] x = new int[offsetList.size()];
			for(int j = 0; j < offsetList.size(); j++) {
				int[] offset = offsetList.get(j);
				//�ڿ�ʼ���߽�βʱ���ܳ���offset������Χ�����
				//�������ֱ������Ӧ�û���ЩӰ�죬�����Ҫ��������
				//������������ ��/_b+1,��Ҫ��_b+1������Χ�ķ���һ������id
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
			if(old != null ) {
				//������������Ѿ����ڣ���Ҫ����Ȩ��++
				old.getWeight()[node.getState()]++;
			} else {
				//ע�⵱ǰ��״̬yֻ�ǵ�ǰ�еģ���ƫ�Ƶ���һ���޹�
				FeatureFunction featureFunction = new FeatureFunction(funcId, featureTemplate.getTemplateNumber());
				featureFunction.setX(x);
				double[] weight = new double[stateNum];
				weight[node.getState()]++;
				featureFunction.setWeight(weight);
				funcs.put(funcId, featureFunction);
			}
		}
	}
	
	
	@Override
	public void train(String template, int maxOffsetOfTemplate, List<SequenceNode> nodes) {
		//this.maxOffsetOfTemplate = maxOffsetOfTemplate;
		//Ϊnodes����ƫ�ƽڵ�
		//addOffsetNodes2trainData(nodes);
		logger.info("�ලѧϰCRFѵ����ʼ...");
		long start = System.currentTimeMillis();
		//�ȶ��������ѵ�����
		//1. ͳ������ֲ�p(x,y) ��������������
		TreeMap<String,FeatureFunction> funcs = new TreeMap<>();
		parseStr2FeatureTemplate(template, "\n");
		for(int i = 0; i < nodes.size() -1 ; i++ ) {
			SequenceNode node = nodes.get(i);
			//����ģ�������������
			//ֻ���Unigram
			generateFeatureFunction(nodes, i, funcs);
			//���Bigram ״̬ת��
			transferProbabilityWeights[node.getState()][nodes.get(i+1).getState()]++;
		}
		//���һ��ѭ��������������
		generateFeatureFunction(nodes, nodes.size() -1, funcs);
		//�������õ�������������ΪDAT
		buildFeatureFunctionDAT(funcs);
		long end = System.currentTimeMillis();
		//���ʹ�һ��
		logger.info("���ʹ�һ����ʼ...");
		logProb();
		logger.info("���ʹ�һ������...");
		logger.info("�ලѧϰCRFѵ������...��ʱ:"+(end - start) / 1000.0 +"��");
	}
}
