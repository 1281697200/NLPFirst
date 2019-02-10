package com.outsider.model.crf.unsupervised;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import com.outsider.common.util.IOUtils;
import com.outsider.model.data.SegmentationDataConverter;
import com.outsider.model.hmm.SequenceNode;

public class Test {
	public static void main(String[] args) {
		
		try {
			System.setErr(new PrintStream(new FileOutputStream("C:\\Users\\outsider\\Desktop\\iters.txt")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		UnsupervisedCRF crf  = new UnsupervisedCRF(65536, 4);
		String[] words = IOUtils.loadSegmentionCorpus("./data/segmentation/ctb6.train.seg.utf8.splitBy1space.txt", "utf-8", " ");
		SegmentationDataConverter dataConverter = new SegmentationDataConverter();
		List<SequenceNode> nodes = dataConverter.convert(words);
		crf.train(nodes);
		String test = "��Ȼ���Դ������˹������о��зǳ���Ҫ��һ������";
		String test2 =  "�㿴���º������ⲿ��Ӱ��";
		String test3 = "ԭ���⣺��ý�ĵ����ֳ�����һĻ�����ձ���������NNN��9��8�ձ�������ǰ���ձ������������������ս��֮һ��ֱ������ĸ���Ӻء������Ϻ�����ʱ��������й�����ս���ֽ����ټ��ӡ� ";
		String[] res = crf.seg(test);
		String[] res2 = crf.seg(test2);
		String[] res3 = crf.seg(test3);
		System.out.println(Arrays.toString(res));
		System.out.println(Arrays.toString(res2));
		System.out.println(Arrays.toString(res3));
		/**
			�ٻ���:0.8543504907694179,��׼��:0.8167286843894236,Fֵ:0.835116088700273,������:0.19171352914206582
			�ٻ���:0.8774084755343917,��׼��:0.9000009827913239,Fֵ:0.8885611434005103,������:0.09748876603654272
			�ܾ�ȷ��:1.7167296671807475,���ٻ���:1.7317589663038095,��f�÷�:1.7236772321007834
			
			iter 1:
			�ٻ���:0.8258025881186081,��׼��:0.7855172045499048,Fֵ:0.805156299178477,������:0.2254825821301919
			�ٻ���:0.8429352981192093,��׼��:0.8535255539601847,Fֵ:0.8481973708947346,������:0.144657040748867
			�ܾ�ȷ��:1.6390427585100895,���ٻ���:1.6687378862378175,��f�÷�:1.6533536700732117
			
			iter11:
			f-score:1.6748159294726115
			
			iter 20:
			�ٻ���:0.8258400157195924,��׼��:0.8099551248520221,Fֵ:0.8178204427312571,������:0.19377204719620483
			�ٻ���:0.8400226116449971,��׼��:0.8826093521920774,Fֵ:0.8607895692812191,������:0.11172643742035623
			�ܾ�ȷ��:1.6925644770440995,���ٻ���:1.6658626273645896,��f�÷�:1.6786100120124763
			
			iter22:
			�ٻ���:0.8260365106247602,��׼��:0.8102073218858123,Fֵ:0.8180453496668737,������:0.19350069708906834
			�ٻ���:0.8407124584415211,��׼��:0.8831119162640901,Fֵ:0.8613907554569505,������:0.11127612076151422
			�ܾ�ȷ��:1.6933192381499025,���ٻ���:1.6667489690662813,��f�÷�:1.6794361051238242
		 *
		 */
	}
}


/**
 * ����һ�ν��:
 *[ԭ, ����, ��, ��ý, ��, ��, ��, �ֳ�, ����, һ, Ļ, ��, ��, �ձ�, ������, ��, NNN��9��8��, ����, ��, ��ǰ, ��, �ձ�, ����, ������, ����, ���ս, ��֮, һ, ��, ֱ����, ��ĸ, ��, �Ӻ�, ��, ��, ��, �Ϻ�, ����, ʱ, ��, ���, ��, �й�, ����, ս��, �ֽ�, ����, ����, ��,  ]
 * 
**/