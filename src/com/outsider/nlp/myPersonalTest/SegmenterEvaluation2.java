package com.outsider.nlp.myPersonalTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.outsider.common.util.IOUtils;
import com.outsider.model.data.SegmenterDataConverter;
import com.outsider.model.evaluation.SegmentionEvaluation;
import com.outsider.nlp.segmenter.FirstOrderHMMSegmenter;
import com.outsider.nlp.segmenter.SecondOrderHMMSegmenter;
import com.outsider.nlp.segmenter.Segmenter;

//�ִ������κ�����
public class SegmenterEvaluation2 {
	static String basePath = "./data/";
	static String pku = basePath + "pku_training.splitBy2space.utf8";
	static String sku = basePath+"sku_train.utf8.splitBy2space.txt";
	static String ctb6 = basePath+"ctb6.train.seg.utf8.splitBy1space.txt";
	static String cityu = basePath+"cityu_training.utf8.splitBy1space.txt";
	static String as = basePath + "as_training.utf8.splitBy1spce.txt";
	static String msrTest = basePath+"msr_test_gold.utf8.txt";
	static String pkutest = basePath+"pku_test_gold.utf8";
	static String skuTest = basePath+"sku_test.txt";
	static String ctb6Test = basePath+"ctb6.test.seg.txt";
	static String asTest = basePath+"as_test_gold.utf8.txt";
	static String[] corpuses = new String[] {pku,sku,ctb6,cityu,as};
	static String[] corpusNames = new String[] {"pku","sku","ctb6","cityu","as"};
	static String[] splitChars = new String[] {"  ","  "," "," "," "};
	static String[] encodings = new String[] {"utf-8","utf-8","utf-8","utf-8","utf-8"};
	static String[] testSet = new String[] {msrTest,pkutest,skuTest,ctb6Test,asTest};
	static String[] testSetNames = new String[] {"msrT","pkuT","skuT","ctb6T","asT"};
	static String[] testSetSplitChats = new String[] {"  ","  ","  "," "," "};
	
	static String[] c1 = new String[] {"0","1","2","3","4"};
	static String[] c2 = new String[] {"0,1","0,2","0,3","0,4","1,2","1,3","1,4","2,3","2,4","3,4"};
	static String[] c3 = new String[] {"0,1,2","0,1,3","0,1,4","0,2,3","0,2,4","0,3,4","1,2,3","1,2,4","1,3,4","2,3,4"};
	static String[] c4 = new String[] {"0,1,2,3","0,1,2,4","0,1,3,4","1,2,3,4","0,2,3,4"};
	static String[] c5 = new String[] {"0,1,2,3,4"};
	static List<String[]> allCombination = new ArrayList<>();
	static String[] segmenterNames = new String[] {"1HMMSeg","2HMMSeg"};
	static {
		allCombination.add(c1);
		allCombination.add(c2);
		allCombination.add(c3);
		allCombination.add(c4);
		allCombination.add(c5);
	}
	public static void main(String[] args) {
		evaluation();
	}
	public static void evaluation() {
		
		Segmenter segmenter1 = new FirstOrderHMMSegmenter(4, 65536);
		Segmenter segmenter2 = new SecondOrderHMMSegmenter(4, 65536);
		List<Segmenter> segmenters = new ArrayList<>();
		segmenters.add(segmenter1);
		segmenters.add(segmenter2);
		//ֻʹ��һ������
		//ע��֮ǰû�����³�ʼ��ģ�Ͳ������²�׼ȷ
		for(int n = 0; n < 2;n++) {
			//��ͬ��ϵ�����
			for(int i = 1; i <= 5;i++) {
				String[] ss = allCombination.get(i-1);
				System.out.println("C5"+i);
				for(int c = 0; c < ss.length;c++) {
					String[] indexStr = ss[c].split(",");
					int[] indexs = new int[indexStr.length];
					for(int q = 0; q < indexStr.length;q++) {
						indexs[q] = Integer.parseInt(indexStr[q]);
					}
					List<String[]> parameter = getPathAndEncodingAndSpliChar(indexs);
					String[] trainData = IOUtils.loadMultiSegmentionCorpus(parameter.get(0), parameter.get(1), parameter.get(2));
					score(segmenters.get(n), trainData, n, indexs);
				}
			}
		}
	}
	
	public static void score(Segmenter segmenter,String[] data, int segmenterIndex,int[] indexs) {
		segmenter.initParameter();//ѵ��֮ǰ�������³�ʼ��ģ�͵Ĳ���
		segmenter.train(data);
	/*	List<String> te = new ArrayList<>();
		te.add("HanLP����һϵ��ģ�����㷨��ɵ�Java���߰���Ŀ�����ռ���Ȼ���Դ��������������е�Ӧ�á�");
		te.add("������أ�ǿ���������Ϻ�ɫ���壬������ˮ�����Ҵ�������ԭ����������������ˮ����������������Ư�׼����������ռ���������̼���Ƽ��ȡ�");
		te.add("��ҹ������ӡ�ͨ������ǳ�ݵ���Ů�ڰ�ҹ�������ӵ��龰,���������߶����������������");
		te.add("���������������������ι���ͳ�ƾֵ������");
		te.add("�㿴���º�������");
		te.add("����������[ί��]ǰ���Ǹ����̫�����ˣ�һ�㲻��Ʒ...@hankcs");
		te.add("���ӳ����ֻ��ܷ���ؼֲ�˹����̬��");
		for(int p = 0; p < te.size();p++) {
			String[] a = segmenter.predictAndReturnTerms(te.get(p));
			System.out.println(Arrays.toString(a));
		}*/
		//��ͬ���������
		float score_all = 0;
		for(int j = 0; j < 4; j++) {
			String[] test = IOUtils.loadSegmentionCorpus(testSet[j], encodings[j], testSetSplitChats[j]);
			StringBuilder sb = new StringBuilder();
			for(int k = 0; k < test.length;k++) {
				sb.append(test[k]);
			}
			String testData = sb.toString();
			int[] trueState = SegmenterDataConverter.getInstance().rawData2StateIndex(test,testData.length());
			int[] predict = segmenter.predict(testData);
			float accuract = SegmentionEvaluation.precision(predict, trueState);
			//System.out.println(segmenterNames[segmenterIndex]+" "+corpusNames[i]+" "+testSetNames[j]+" "+accuract);
			System.out.print(accuract+",");
			score_all+=accuract;
		}
		System.out.println();
		System.out.println(segmenterNames[segmenterIndex]+" "+Arrays.toString(indexs)+" "+score_all);
	}
	
	//����һ������·������������ֵ��������
	public static List<String[]> getPathAndEncodingAndSpliChar(int[] indexs) {
		String[] path = new String[indexs.length];
		String[] encodings = new String[indexs.length];
		String[] splitChar = new String[indexs.length];
		for(int i = 0; i < indexs.length;i++) {
			path[i] = corpuses[indexs[i]];
			encodings[i] = "utf-8";
			splitChar[i] = splitChars[indexs[i]];
		}
		List<String[]> arr = new ArrayList<>();
		arr.add(path);
		arr.add(encodings);
		arr.add(splitChar);
		return arr;
	}
	
	@Test
	public void checkCorpus() {
		String[] asData = IOUtils.loadSegmentionCorpus(as, "utf-8", " ");
		int count = 0;
		for(int i = 0; i < asData.length;i++) {
			//System.out.println(asData[i]);
			if(asData[i].trim().equals("")) {
				System.out.println("���ַ�:"+asData[i-1]+"/"+asData[i-2]);
				count++;
			}
		}
		System.out.println(count);
	}

}
