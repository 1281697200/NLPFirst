package com.outsider.model.hmm;

import java.util.List;
/**
 * alpha�����߳�
 * @author outsider
 *
 */
public class AlphaCalculatorThread implements Runnable{
	private UnsupervisedFirstOrderGeneralHMM hmm;
	private List<SequenceNode> nodes;
	private double[][] alpha;
	
	public AlphaCalculatorThread(UnsupervisedFirstOrderGeneralHMM hmm, List<SequenceNode> nodes, double[][] alpha) {
		super();
		this.hmm = hmm;
		this.nodes = nodes;
		this.alpha = alpha;
	}

	@Override
	public void run() {
		hmm.calcAlpha(nodes, alpha);
	}
}
