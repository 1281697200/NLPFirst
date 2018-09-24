package com.outsider.nlp.postagger;

import java.util.List;

import com.outsider.model.hmm.FirstOrderGeneralHMM;
import com.outsider.model.hmm.SequenceNode;

public class FirstOrderHMMPOSTagger extends FirstOrderGeneralHMM implements POSTagger{

	public FirstOrderHMMPOSTagger() {
		super();
	}

	public FirstOrderHMMPOSTagger(int stateNum, int observationNum, double[] pi, double[][] transfer_probability1,
			double[][] emission_probability) {
		super(stateNum, observationNum, pi, transfer_probability1, emission_probability);
	}

	public FirstOrderHMMPOSTagger(int stateNum, int observationNum) {
		super(stateNum, observationNum);
	}
	
	@Override
	public int[] predict(int[] O, Object... otherParameters) {
		/**
		 * ��ȡ�ӿ�POSTagger�д����Ĵ�Ԥ��Ĵ�����
		 */
		String[] words = (String[]) otherParameters;
		//ԭʼԤ��
		int[] rawPredict = super.predict(O, otherParameters);
		/**
		 * ʹ������������ԭ��
		 * �����к������֣���ô��עΪm
		 * �����к���Ӣ�Ļ���������(����������ʱ������)����עΪx
		 * ��ʱ�����ǣ�
		 * ���Ժ������URL�ȵı�ע
		 */
		String numberReg = "[0-9]+";//û�п���ȫ�ǵ�����
		String otherLanguageReg = "[a-zA-Z]+";
		for(int i = 0; i < words.length; i++) {
			if(words[i].matches(numberReg)) {
				rawPredict[i] = natureName2Int.get("m");
			} else if(words[i].matches(otherLanguageReg)) {
				rawPredict[i] = natureName2Int.get("x");
			}
		}
		return rawPredict;
	}
	
	@Override
	protected void solve(List<SequenceNode> nodes) {
		//�������п�ʼѵ��
		pi[nodes.get(0).getState()]++;
		emissionProbability[nodes.get(0).getState()][nodes.get(0).getNodeIndex()]++;
		for(int i = 1;i < nodes.size(); i++) {
			SequenceNode node = nodes.get(i);
			//״̬ͳ��
			pi[node.getState()]++;
			//״̬ת��ͳ��
			transferProbability1[nodes.get(i-1).getState()][node.getState()]++;
			//��Ҫ����ʵ���û�еĴʵ������Ҳ����nodeIndex=-1
			if(node.getNodeIndex() != -1) {
				//״̬�¹۲�ֲ�ͳ��
				emissionProbability[node.getState()][node.getNodeIndex()]++;
			}
		}
	}
}
