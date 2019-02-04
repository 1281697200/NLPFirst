package com.outsider.nlp.segmenter;

import java.util.Arrays;
import java.util.List;

import com.outsider.common.dataStructure.Table;
import com.outsider.common.util.IOUtils;
import com.outsider.common.util.nlp.CorpusUtils;
import com.outsider.common.util.nlp.NLPUtils;
import com.outsider.constants.nlp.PathConstans;
import com.outsider.model.data.CRFSegmentationTableDataConverter;
import com.outsider.model.data.SegmentationDataConverter;
import com.outsider.model.hmm.SequenceNode;
/**
 * ʹ��Ĭ��ģ��
 * ֻ����һ��������ã�4.2907094316046495 ���� 2 ��������875035
 * �ܾ�ȷ��:4.286389518392307,���ٻ���:4.296123341643277,��f�÷�:4.2907094316046495
 * ȫģ�壺�������� 2323339
 * �ܾ�ȷ��:4.31152655184218,���ٻ���:4.30433444739526,��f�÷�:4.307312639515478 �ִ�ֱ�۸��ܻ�����
 * 
 * ʹ��2��������ã�4.337030776105888  ����:[2, 3] �������� 1869221
 * �ܾ�ȷ��:4.339109999777037,���ٻ���:4.336089665088207,��f�÷�:4.337030776105888
 * 
 * ʹ��3�����ϣ�4.347382274041837 �������:[1, 2, 3] �������� 2107239
 * �ܾ�ȷ��:4.347279501022128,���ٻ���:4.348538072250367,��f�÷�:4.347382274041837
	��������û��̫��������̫���ڴ���
	
ctb8:
�ܾ�ȷ��:4.371331921870376,���ٻ���:4.360915860640292,��f�÷�:4.365571586390583

ctb8+sku:
�ܾ�ȷ��:4.382265856799716,���ٻ���:4.359842697453232,��f�÷�:4.3704761002059325

	��ctb6�����м����˲��������еĵ�һ�䣬��������Ȼ���Դ������һ����
 * @author outsider
 *
 */
public class SupervisedCRFSegmenterTest {

	public static void main(String[] args) {
		//train();
		train2();
		use();
		//7587206
	}
	
	/**
	 * ʹ��ʾ��
	 */
	public static void use() {
		Segmenter segmenter = new SupervisedCRFSegmenter();
		long start = System.currentTimeMillis();
		segmenter.open(PathConstans.SUPERVISED_CRF_SEGMENTER, null);
		long end = System.currentTimeMillis();
		System.out.println("��ʱ:"+(end - start)+"����!");
		TestSeg.testSeg(segmenter);
		SegmenterTest.score(segmenter, "SCRF");
	}
	/**
	 * ѵ��ʾ��
	 */
	public static void train() {
		//����
		SupervisedCRFSegmenter segmenter = new SupervisedCRFSegmenter();
		String basePath = "./data/segmentation";
		String pku = basePath + "pku_training.splitBy2space.utf8";
		String sku = basePath+"sku_train.utf8.splitBy2space.txt";
		String ctb6 = basePath+"ctb6.train.seg.utf8.splitBy1space.txt";
		String cityu = basePath+"cityu_training.utf8.splitBy1space.txt";
		// 1 2 3
		//String[] words = IOUtils.loadMultiSegmentionCorpus(new String[] {sku,ctb6,cityu}, new String[] {"utf-8","utf-8","utf-8"}, new String[] {"  "," "," "});
		// 2
		//String[] words = IOUtils.loadMultiSegmentionCorpus(new String[] {ctb6}, new String[] {"utf-8"}, new String[] {" "});
		String[] words = IOUtils.loadMultiSegmentionCorpus(new String[] {/*pku,*/cityu}, new String[] {/*"utf-8",*/"utf-8"}, new String[] {/*"  ",*/" "});
		// 2 3
		//String[] words = IOUtils.loadMultiSegmentionCorpus(new String[] {ctb6/*, cityu*/}, new String[] {"utf-8"/*, "utf-8"*/}, new String[] {" "/*, " "*/});
		SegmentationDataConverter converter = new SegmentationDataConverter();
		
		String ctb8 = "D:\\nlp����\\ctb8.0\\parse_result\\seg\\train.tsv";
		String srcData = IOUtils.readTextWithLineCheckBreak(ctb8, "utf-8");
		CRFSegmentationTableDataConverter converter2 = new CRFSegmentationTableDataConverter(0, 1);
		Table table = Table.generateTable(srcData, "\t");
		List<SequenceNode> nodes = converter2.convert(table);
		
		List<SequenceNode> nodes2 = converter.convert(words);
		nodes.addAll(nodes2);
		segmenter.train(nodes);
		String test3 = "ԭ���⣺��ý�ĵ����ֳ�����һĻ�����ձ���������NNN��9��8�ձ�������ǰ���ձ������������������ս��֮һ��ֱ������ĸ���Ӻء������Ϻ�����ʱ��������й�����ս���ֽ����ټ��ӡ� ";
		String[] res = segmenter.seg(test3);
		System.out.println(Arrays.toString(res));
		//String s1 = segmenter.generateModelTemplate();
		for(int i = 0; i < 4; i++) {
			System.out.println(Arrays.toString(segmenter.getTransferProbabilityWeights()[i]));
		}
		//IOUtils.writeTextData2File(s1, "C:\\Users\\outsider\\Desktop\\crf.model", "utf-8");
		//segmenter.save(PathConstans.SUPERVISED_CRF_SEGMENTER, null);
		TestSeg.testSeg(segmenter);
		SegmenterTest.score(segmenter, "SCRF");
	}
	
	public static void train2() {
		//����
		SupervisedCRFSegmenter segmenter = new SupervisedCRFSegmenter();
		String basePath = "./data/segmentation/";
		String pku = basePath + "pku_training.splitBy2space.utf8";
		String[] words = IOUtils.loadMultiSegmentionCorpus(new String[] {pku}, new String[] {"utf-8"}, new String[] {"  "});
		SegmentationDataConverter converter = new SegmentationDataConverter();
		List<SequenceNode> nodes = converter.convert(words);
		segmenter.train(nodes);
		String test3 = "ԭ���⣺��ý�ĵ����ֳ�����һĻ�����ձ���������NNN��9��8�ձ�������ǰ���ձ������������������ս��֮һ��ֱ������ĸ���Ӻء������Ϻ�����ʱ��������й�����ս���ֽ����ټ��ӡ� ";
		String[] res = segmenter.seg(test3);
		System.out.println(Arrays.toString(res));
		//String s1 = segmenter.generateModelTemplate();
		for(int i = 0; i < 4; i++) {
			System.out.println(Arrays.toString(segmenter.getTransferProbabilityWeights()[i]));
		}
		//IOUtils.writeTextData2File(s1, "C:\\Users\\outsider\\Desktop\\crf.model", "utf-8");
		//segmenter.save(PathConstans.SUPERVISED_CRF_SEGMENTER, null);
		TestSeg.testSeg(segmenter);
		SegmenterTest.score(segmenter, "SCRF");
	}
	
}
