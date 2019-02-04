package com.outsider.model.crf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.outsider.common.algorithm.dat.darts.DoubleArrayTrie;
import com.outsider.common.dataStructure.Table;
import com.outsider.common.logger.CONLPLogger;
import com.outsider.common.util.Storable;
import com.outsider.common.util.StorageUtils;
import com.outsider.model.SequenceModel;
import com.outsider.model.hmm.SequenceNode;

/**
 * CRF�������������ģ�͵�ʵ��
 * @author outsider
 * 
 */
public abstract class CRF implements SequenceModel, Storable, Serializable{
	public Logger logger = CONLPLogger.getLoggerOfAClass(CRF.class);
	/**
	 * ģ���е������ƫ����
	 * ����ʱΪ����Ҫ��������۲�ʱ����������Χ�ķ���id����Ҫȷ�����ƫ�ƶ���
	 * ����: _B+4
	 * ��ǰĬ��ģ����2
	 * ��ʱ�������ַ�ʽ�����ַǳ�ռ���ڴ�
	 */
	@Deprecated
	protected int maxOffsetOfTemplate = 0;
	
	/**
	 * Bigramģ����ת�ƾ���Ȩ��
	 * ת������������idѡȡ����λ�õ��ַ���ƴ��
	 */
	protected double[][] transferProbabilityWeights;
	/**
	 * ��չ:���¸�������CRF++�е�����ģ�����˼
	 */
	//���ǵ����������ָ���ĺ�����DAT�洢
	//������Unigram
	protected DoubleArrayTrie<FeatureFunction> featureFunctionTrie;
	/**
	 * ��������ģ��
	 */
	protected List<FeatureTemplate> featureTemplates;
	
	/**
	 * �۲��������ظ���
	 */
	protected int observationNum;
	/**
	 * ״̬��
	 */
	protected int stateNum;
	//�����
	public static final double INFINITY =-1e31;
	public CRF() {
	}
	
	public double[][] getTransferProbabilityWeights() {
		return transferProbabilityWeights;
	}
	
	public CRF(int observationNum, int stateNum) {
		this.observationNum = observationNum;
		this.stateNum = stateNum;
		reInitParameters();
	}
	public void setStateNumAndObservationNum(int stateNum, int observationNum) {
		this.observationNum = observationNum;
		this.stateNum = stateNum;
		reInitParameters();
	}
	public int getObservationNum() {
		return observationNum;
	}
	public int getStateNum() {
		return stateNum;
	}
	/**
	 * �������������ֵ���
	 */
	protected void buildFeatureFunctionDAT(TreeMap<String,FeatureFunction> funcs) {
		logger.info("��ʼ������������DAT...");
		featureFunctionTrie = new DoubleArrayTrie<>();
		featureFunctionTrie.build(funcs);
		logger.info("�������...");
	}
	
	/**
	 * �ӷ�����Ϊtrain����
	 * ���ݵ�ǰ������������λ�ò�����������
	 * ע��˷�����ֻ�ǲ�������������������Ȩ�ص�ͳ��
	 * �ලѧϰCRF�е���Ҫ��Ȩ�ص�ͳ��
	 * @param nodes ����ǩ�Ĺ۲�����
	 * @param i ����λ��
	 * @param funcs �Ѿ���������������map
	 */
	protected void generateFeatureFunction(List<SequenceNode> nodes, int i, TreeMap<String,FeatureFunction> funcs) {
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
				//��ʼȨ��Ϊ
				weight[nodes.get(i).getState()]++;
				featureFunction.setWeight(weight);
				funcs.put(funcId, featureFunction);
			} else {
				//����
				old.getWeight()[nodes.get(i).getState()]++;
			}
		}
	}
	
	/**
	 * �����ַ���������ģ��Ϊһ����FeatureTemplate
	 * @param template ģ��
	 * @param splitChar ÿһ�еķָ���
	 */
	public void parseStr2FeatureTemplate(String template, String splitChar) {
		String[] lines = template.split(splitChar);
		this.featureTemplates = new ArrayList<>();
		for(String line : lines) {
			//��ʱû�п��Ǽ����Ե�����
			//��������ģ�壬B01:%x[0,0]/%x[-1,0]
			FeatureTemplate ft = FeatureTemplate.createFeatureTemplate(line);
			if(ft != null)
				this.featureTemplates.add(ft);
		}
	}
	/**
	 * ѵ������ΪTable��ָ��x����������y��������
	 * @param table
	 * @param xColumnIndex
	 * @param yColumnIndex
	 */
	public abstract void train(Table table, int xColumnIndex, int yColumnIndex);
	
	@Override
	public int[] predict(int[] x) {
		return veterbi(x);
	}
	
	/**
	 * ��֪��������������ĳ��״̬(��ǩ)�ĵ÷�
	 * @param observations ����۲�x
	 * @param currentIndex ���뵱ǰ������λ��
	 * @param state ������Ҫ�����״̬y
	 * @return ����Ȩ�ص��ۻ����
	 */
	public double computeUnigramWeight(List<FeatureFunction> funcs, int state) {
		double weight = 0;
		for(int i = 0; i < funcs.size(); i++) {
			//if(funcs.get(i).getWeight()[state] != INFINITY)
			weight += funcs.get(i).getWeight()[state];
		}
		return weight;
	}
	
	/**
	 * ��ȡ��ǰ�۲��µ���������
	 * ֮ǰ���ݹ۲�ȡ����������ֱ���ù۲����ȶԣ�Ч�ʷǳ���������ֱ�ӹ��캯��idû��trie��ȡ����������
	 * @param observations �۲�
	 * @param currentIndex ��ǰ�Ĺ۲�����
	 * @return
	 */
	protected List<FeatureFunction> getUnigramFeatureFunction(int[] observations, int currentIndex){
		//Unigram  ����������wk*f(x,y_i)
		List<FeatureFunction> funcs = featureFunctionTrie.getValues();
		List<FeatureFunction> r_funcs = new ArrayList<>();
		outer:for(FeatureTemplate featureTemplate : featureTemplates) {
			List<int[]> offsetList = featureTemplate.getOffsetList();
			int x[] = new int[offsetList.size()];
			for(int i = 0; i < offsetList.size(); i++) {
				int[] offset = offsetList.get(i);
				int raw = currentIndex + offset[0];
				//����Խ��
				if(raw < 0 || raw >= observations.length) {
					continue outer;
				}
				x[i] = observations[raw];
			}
			String funcId = FeatureFunction.generateFeatureFuncStrId(x, featureTemplate.getTemplateNumber());
			FeatureFunction featureFunction = featureFunctionTrie.getValue(funcId);
			if(featureFunction != null) r_funcs.add(featureFunction);
		}
		return r_funcs;
	}
	/**
	 * Unigram��������������
	 * ʵ�ʲ�û�б���ֱ��ȡ����
	 * @param y_i_1 y_(i-1)
	 * @param y_i y_(i)
	 * @return
	 */
	protected double computeBigramWeight(int y_i_1,int y_i) {
		//Bigram ����������wk*f(x,y_(i-1),y_i)
		//if(transferProbabilityWeights[y_i_1][y_i] == INFINITY) return 0;
		return transferProbabilityWeights[y_i_1][y_i]/* == INFINITY ? 0 : transferProbabilityWeights[y_i_1][y_i]*/;
	}
	/**
	 * �����㷨ά�ر�
	 * @param observations �۲�����
	 * @return ���Ԥ������
	 */
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
	
	/**
	 * ��ȡĬ�ϵ�����
	 * @return
	 */
	public String getDefaultTemplate() {
		return "U00:%x[-2,0]\n" + 
				"U01:%x[-1,0]\n" + 
				"U02:%x[0,0]\n" + 
				"U03:%x[1,0]\n" + 
				"U04:%x[2,0]\n" +
				"U05:%x[-2,0]/%x[-1,0]/%x[0,0]\n" + 
				"U06:%x[-1,0]/%x[0,0]/%x[1,0]\n" + 
				"U07:%x[0,0]/%x[1,0]/%x[2,0]\n" + 
				"U08:%x[-1,0]/%x[0,0]\n" + 
				"U09:%x[0,0]/%x[1,0]\n" + 
				"#Bigram\n"+
				"B";
	}
	
	public DoubleArrayTrie<FeatureFunction> getFeatureFunctionTrie() {
		return featureFunctionTrie;
	}
	
	@Override
	public void reInitParameters() {
		transferProbabilityWeights = new double[stateNum][stateNum];
		featureFunctionTrie = null;
	}
	
	/**
	 * ����CRFģ��ģ���ļ�
	 * @return
	 */
	public abstract String generateModelTemplate();
	/**
	 * ָ��ģ����ѵ��
	 * @param template ģ��
	 * @param maxOffsetOfTemplate ģ��������ƫ����
	 * @param nodes ѵ������
	 */
	public abstract void train(String template, int maxOffsetOfTemplate ,List<SequenceNode> nodes);
	/**
	 * Ϊѵ��������ƫ�ƽڵ�
	 * idĬ��Ϊ�������� -1 ��idΪ-1
	 * ���󳬳������� 1 ��idΪobservationNum,����2��idΪobservationNum+1
	 * @param nodes
	 * @deprecated
	 */
	/*protected void addOffsetNodes2trainData(List<SequenceNode> nodes) {
		for(int i = 0; i < maxOffsetOfTemplate; i++) {
			nodes.add(0, new SequenceNode(-(i+1)));
			nodes.add(new SequenceNode(this.observationNum+i));
		}
	}*/
	
	
	@Override
	public void open(String directory, String fileName) {
		DataInputStream  in = null;
		try {
			if(fileName == null || fileName.trim().equals("")) {
				fileName = this.getClass().getSimpleName();
			}
			in = new DataInputStream(new BufferedInputStream(
					new  FileInputStream(directory+"/"+fileName)));
			this.featureFunctionTrie = new DoubleArrayTrie<>();
			this.featureFunctionTrie.open(directory+"/"+fileName+"_featureFunctionTire");
			this.stateNum = in.readInt();
			this.observationNum = in.readInt();
			this.transferProbabilityWeights = new double[stateNum][stateNum];
			for(int i = 0; i < stateNum; i++) {
				for(int j = 0; j < stateNum; j++) {
					transferProbabilityWeights[i][j] = in.readDouble();
				}
			}
			this.featureTemplates = new ArrayList<>();
			StorageUtils.open(directory, fileName+"_featureTemplates", this.featureTemplates);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void save(String directory, String fileName) {
		System.out.println("crf����...");
		DataOutputStream  out = null;
		try {
			if(fileName == null || fileName.trim().equals("")) {
				fileName = this.getClass().getSimpleName();
			}
			out = new DataOutputStream(new BufferedOutputStream(
					new  FileOutputStream(directory+"/"+fileName)));
			this.featureFunctionTrie.save(directory+"/"+fileName+"_featureFunctionTire");
			out.writeInt(this.stateNum);
			out.writeInt(this.observationNum);
			for(int i = 0; i < stateNum; i++) {
				for(int j = 0; j < stateNum; j++) {
					out.writeDouble(transferProbabilityWeights[i][j]);
				}
			}
			StorageUtils.save(directory, fileName+"_featureTemplates", this.featureTemplates);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
