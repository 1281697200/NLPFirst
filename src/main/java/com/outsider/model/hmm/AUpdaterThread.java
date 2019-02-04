package com.outsider.model.hmm;
/**
 * ����A�����߳�
 * @author outsider
 *
 */
public class AUpdaterThread implements Runnable{
	private UnsupervisedFirstOrderGeneralHMM hmm;
	private double[][] gamma;
	private double[][][] ksi;
	
	public AUpdaterThread(UnsupervisedFirstOrderGeneralHMM hmm, double[][] gamma, double[][][] ksi) {
		super();
		this.hmm = hmm;
		this.gamma = gamma;
		this.ksi = ksi;
	}

	@Override
	public void run() {
		hmm.updateA(ksi, gamma);
	}
	
}
