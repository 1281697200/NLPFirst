package com.outsider.nlp.lexicalanalyzer;

/**
 * ��װ�ʷ������Ľ��
 * @author outsider
 */
public class LexicalAnalysisResult {
	//�ִʽ��
	private String[] segmentationResult;
	//���Ա�ע���
	private String[] postaggingResult;
	public String[] getSegmentationResult() {
		return segmentationResult;
	}
	public void setSegmentationResult(String[] segmentationResult) {
		this.segmentationResult = segmentationResult;
	}
	public String[] getPostaggingResult() {
		return postaggingResult;
	}
	public void setPostaggingResult(String[] postaggingResult) {
		this.postaggingResult = postaggingResult;
	}
	public LexicalAnalysisResult(String[] segmentationResult, String[] postaggingResult) {
		super();
		this.segmentationResult = segmentationResult;
		this.postaggingResult = postaggingResult;
	}
	public LexicalAnalysisResult() {}
}
