package com.outsider.nlp.segmenter;

import com.outsider.constants.nlp.PathConstans;
import com.outsider.zhifac.crf4j.extension.CrfppTrainer;
/**
 * crfpp�÷֣�
 * 
 * ctb6��
 * �ܾ�ȷ��:4.420024034248117,���ٻ���:4.463402966448713,��f�÷�:4.44099658851161
 * 
 * pku:
 * �ܾ�ȷ��:4.435005985946301,���ٻ���:4.434995239677223,��f�÷�:4.4344368523197915

   ctb8:
   �ܾ�ȷ��:4.435005985946301,���ٻ���:4.434995239677223,��f�÷�:4.4344368523197915
   
   ctb8+sku:
   �ܾ�ȷ��:4.533188119773194,���ٻ���:4.588728141143836,��f�÷�:4.560181879960174
 * @author outsider
 *
 */
public class CrfppSegmenterTest {
	public static CrfppSegmenter segmenter;
	
	
	public static void loadModel(String modelFile) {
		segmenter = new CrfppSegmenter(modelFile, 0, 0, 1);
	}
	/**
	 * crfppѵ�������ĸ�ģ�͵÷�:
	 * crfpp_ctb6.m:
	 * �ܾ�ȷ��:4.420024034248117,���ٻ���:4.463402966448713,��f�÷�:4.44099658851161
	 * crfSeg_pku.m:
	 * �ܾ�ȷ��:4.435005985946301,���ٻ���:4.434995239677223,��f�÷�:4.4344368523197915
	 */
	
	public static void train(int maxIter) {
		String ctb8_sku = "./data/ctb8_sku_crf.utf8";
		String template = "./model/crfpp/segmentationTemplate";
		String[] options = new String[] {"-m", maxIter+"","-t"};
		CrfppTrainer.run(template, ctb8_sku, "./model/crfpp/crfSeg_ctb8_sku_train.m", options);
	}
	public static void main(String[] args) {
		//ѵ��
		//train(1000);
		//ʹ��
		loadModel(PathConstans.CRFPP_SEGMENTER);
		TestSeg.testSeg(segmenter);
		SegmenterTest.score(segmenter, "crfpp");
	}
	
}
