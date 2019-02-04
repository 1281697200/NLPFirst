package com.outsider.zhifac.crf4j.extension;

import com.zhifac.crf4j.ModelImpl;
import com.zhifac.crf4j.Tagger;

/**
 * һ��ʹ��crf++ģ�͵ı�׼ģ��
 * ʹ��crf++��չ����Ӧ�ÿ���ֱ�Ӽ̳����ģ��
 * @author outsider
 *
 */
public class CrfppModelTemplate {
	protected Tagger tagger;
	/**
	 * �޲ι�����Ҫѵ�����ߴ�ģ���ļ�����ģ��
	 */
	public CrfppModelTemplate() {
	}
	
	/**
	 * ֱ�ӹ����ʱ������ģ��
	 * @param modelFile
	 * @param nbest
	 * @param vlevel
	 * @param costFactor
	 */
	public CrfppModelTemplate(String modelFile, int nbest, int vlevel, double costFactor) {
		this.tagger = CrfppUtils.open(modelFile, nbest, vlevel, costFactor);
	}
	/**
	 * ���ڴ�ģ��
	 * @param modelFile
	 * @param nbest
	 * @param vlevel
	 * @param costFactor
	 */
	public void open(String modelFile, int nbest, int vlevel, double costFactor) {
		ModelImpl model = new ModelImpl();
		model.open(modelFile, nbest, vlevel, costFactor);
		this.tagger =  model.createTagger();
	}
	public void open(String modelFile) {
		ModelImpl model = new ModelImpl();
		model.open(modelFile, 0, 0, 1);
		this.tagger =  model.createTagger();
	}
	
	/**
	 * ѵ���ӿ�
	 * @param templateFilePath
	 * @param trainDataFilePath
	 * @param modelFile
	 * @param options
	 */
	public void train(String templateFilePath, String trainDataFilePath, String modelFile,
			String[] options) {
		CrfppTrainer.run(templateFilePath, trainDataFilePath, modelFile, options);
	}
}
