package com.outsider.nlp.segmenter;

import java.util.List;

public interface SegmentationPredictor {
	/**
	 * �������ӷִ�
	 * @param text
	 * @return
	 */
	String[] seg(String text);
	/**
	 * ������ӷִ�
	 * @param texts
	 * @return
	 */
	List<String[]> seg(String[] texts);
}
