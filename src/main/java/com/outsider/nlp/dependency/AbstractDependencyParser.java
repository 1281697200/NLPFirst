package com.outsider.nlp.dependency;

import com.outsider.nlp.postagger.POSTagger;
import com.outsider.nlp.postagger.POSTaggingPredictor;
import com.outsider.nlp.segmenter.SegmentationPredictor;

/**
 * �������������������Ҫ��Ϊ�˶��������ķִ����ʹ��Ա�ע��
 * @author outsider
 *
 */
public abstract class AbstractDependencyParser implements DependencyParser{
	/**
	 * �����ķִ���
	 */
	protected SegmentationPredictor segmenter;
	/**
	 * �����Ĵ��Ա�ע��
	 */
	protected POSTaggingPredictor posTagger;
	
	public void setSegmenter(SegmentationPredictor segmenter) {
		this.segmenter = segmenter;
	}
	public void setPosTagger(POSTagger posTagger) {
		this.posTagger = posTagger;
	}
	
}
