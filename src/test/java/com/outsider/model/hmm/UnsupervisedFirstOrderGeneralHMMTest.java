package com.outsider.model.hmm;

import java.util.Arrays;
import java.util.List;

import com.outsider.common.util.IOUtils;
import com.outsider.model.data.DataConverter;
import com.outsider.model.data.SegmentationDataConverter;
import com.outsider.nlp.segmenter.SegmentationUtils;

public class UnsupervisedFirstOrderGeneralHMMTest {

	public static void main(String[] args) {
		/**
		 * �������ݽ��鴮��
  		�������ݣ���ʮ�򣬰����������ߵ�����ǿ�ҽ��鲢��ѵ��,�����Ǵ��еĺ�4������
		 */
		//testHMM();
		testParallelHMM();
	}
	
	/**
	 * ����
	 */
	public static void testHMM() {
		UnsupervisedFirstOrderGeneralHMM hmm = new UnsupervisedFirstOrderGeneralHMM(4, 65536);
		//�ر���־��ӡ
		//CONLPLogger.closeLogger(hmm.logger);
		String path = "./data/pku_training.splitBy2space.utf8";
		String data = IOUtils.readText(path, "utf-8");
		String[] words = data.split("  ");
		DataConverter<String[], List<SequenceNode>> converter = new SegmentationDataConverter();
		List<SequenceNode> nodes = converter.convert(words);
		nodes = nodes.subList(0, 10000);//ֻʹ��10000���ڵ�
		//ѵ��֮ǰ����������ʣ��������ã�EM�Գ�ʼֵ���У����������Ĭ��Ϊ��Ϊ0�����в�������һ����û������
		//���ֻ��������һЩ����������ֵ�����������ʼ����������������
		//hmm.randomInitA();
		//hmm.randomInitB();
		//hmm.randomInitPi();
		//hmm.randomInitAllParameters();
		//����������Ϣ�������ò���pi��A��B�е�һ��
		hmm.setPriorPi(new double[] {-1.138130826175848, -2.632826946498266, -1.138130826175848, -1.2472622308278396});
		hmm.setPriorTransferProbability1((double[][]) IOUtils.readObject("src/main/resources/A"));
		hmm.setPriorEmissionProbability((double[][]) IOUtils.readObject("src/main/resources/B"));
		hmm.train(nodes, -1, 0.5);
		String str = "ԭ���⣺��ý�ĵ����ֳ�����һĻ" + 
				"���ձ���������NNN��9��8�ձ�������ǰ���ձ������������������ս��֮һ��ֱ������ĸ���Ӻء������Ϻ�����ʱ��������й�����ս���ֽ����ټ��ӡ�" ; 
		//����ת��Ϊ��Ӧ��Unicode��
		int[] x = SegmentationUtils.str2int(str);
		int[] predict = hmm.predict(x);
		System.out.println(Arrays.toString(predict));
		String[] res = SegmentationUtils.decode(predict, str);
		System.out.println(Arrays.toString(res));
	}
	/**
	 * ����
	 */
	public static void testParallelHMM() {
		UnsupervisedFirstOrderGeneralHMM hmm = new UnsupervisedFirstOrderGeneralHMM(4, 65536);
		//CONLPLogger.closeLogger(hmm.logger);
		String basePath = "./data/";
		String path = "./data/pku_training.splitBy2space.utf8";
		String data = IOUtils.readText(path, "utf-8");
		String[] words = data.split("  ");
		DataConverter<String[], List<SequenceNode>> converter = new SegmentationDataConverter();
		List<SequenceNode> nodes = converter.convert(words);
		System.out.println("nodes.size:"+nodes.size());
		nodes = nodes.subList(0, 100000);//ʹ��500000���ڵ�
		hmm.setPriorPi(new double[] {-1.138130826175848, -2.632826946498266, -1.138130826175848, -1.2472622308278396});
		//hmm.setPriorTransferProbability1((double[][]) IOUtils.readObject("src/main/resources/A"));
		hmm.setPriorEmissionProbability((double[][]) IOUtils.readObject("src/main/resources/B"));
		hmm.parallelTrain(nodes, -1, 0.5);
		String str = "ԭ���⣺��ý�ĵ����ֳ�����һĻ" + 
				"���ձ���������NNN��9��8�ձ�������ǰ���ձ������������������ս��֮һ��ֱ������ĸ���Ӻء������Ϻ�����ʱ��������й�����ս���ֽ����ټ��ӡ�" ; 
		//����ת��Ϊ��Ӧ��Unicode��
		int[] x = SegmentationUtils.str2int(str);
		int[] predict = hmm.predict(x);
		System.out.println(Arrays.toString(predict));
		String[] res = SegmentationUtils.decode(predict, str);
		System.out.println(Arrays.toString(res));
	}
	
}
