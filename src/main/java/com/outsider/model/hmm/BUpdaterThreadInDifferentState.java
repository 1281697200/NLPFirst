package com.outsider.model.hmm;

import java.util.List;
/**
 * ���²���B��ʱ���ر��ʱ��
 * ��Ϊ����B�Ǹ��²�ͬ״̬�µĹ۲�ֲ�
 * �����Ǹ���ָ��״̬�µĹ۲�ֲ�
 * @author outsider
 *
 */
public class BUpdaterThreadInDifferentState implements Runnable{
	private UnsupervisedFirstOrderGeneralHMM hmm;
	private List<SequenceNode> nodes;
	private double[][] gamma;
	private int i;
	private double[] gammaSum2;
	
	public BUpdaterThreadInDifferentState(UnsupervisedFirstOrderGeneralHMM hmm, List<SequenceNode> nodes,
			double[][] gamma, int i, double[] gammaSum2) {
		super();
		this.hmm = hmm;
		this.nodes = nodes;
		this.gamma = gamma;
		this.i = i;
		this.gammaSum2 = gammaSum2;
	}

	@Override
	public void run() {
		hmm.updateBinSpecificState( nodes, gamma, i, gammaSum2);
	}

}
