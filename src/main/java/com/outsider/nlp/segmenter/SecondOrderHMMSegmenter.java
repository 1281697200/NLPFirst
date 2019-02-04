package com.outsider.nlp.segmenter;

import java.util.ArrayList;
import java.util.List;

import com.outsider.model.data.DataConverter;
import com.outsider.model.data.SegmentationDataConverter;
import com.outsider.model.hmm.SecondOrderGeneralHMM;
import com.outsider.model.hmm.SequenceNode;

public class SecondOrderHMMSegmenter extends SecondOrderGeneralHMM implements Segmenter{

	public SecondOrderHMMSegmenter(int stateNum, int observationNum) {
		super(stateNum, observationNum);
	}
	public SecondOrderHMMSegmenter() {
		super(4, 65536);
	}
	public SecondOrderHMMSegmenter(int stateNum, int observationNum, double[] pi, double[][] transfer_probability1,
			double[][] emission_probability) {
		super(stateNum, observationNum, pi, transfer_probability1, emission_probability);
	}
	
	@Override
	public void train(String[] words) {
		//ʹ��Ĭ�ϵ�ת����
		DataConverter<String[], List<SequenceNode>> converter = new SegmentationDataConverter();
		List<SequenceNode> nodes = (List<SequenceNode>) converter.convert(words);
		super.train(nodes);
	}
	@Override
	public void train(ArrayList<String[]> corpuses) {
		String[] afterMerge = SegmentationUtils.mergeCorpus(corpuses);
		DataConverter<String[], List<SequenceNode>> converter = new SegmentationDataConverter();
		List<SequenceNode> nodes = converter.convert(afterMerge);
		super.train(nodes);
	}
	@Override
	public String[] seg(String text) {
		List<int[]> intids = SegmentationUtils.strs2int(new String[] {text});
		int[] charids = intids.get(0);
		int[] predi = this.predict(charids);
		return SegmentationUtils.decode(predi, text);
	}
	@Override
	public List<String[]> seg(String[] texts) {
		List<String[]> res = new ArrayList<>(texts.length);
		for(String text : texts) {
			res.add(seg(text));
		}
		return res;
	}
	/**
	 * [Ha, n, LP, ��, ��, һ, ϵ��, ģ��, ��, �㷨, ���, ��, Ja, v, a��, �߰�, ��, Ŀ��, ��, �ռ�, ��Ȼ, ����, ����, ��, ����, ����, ��, ��, Ӧ��, ��]
[��, ����, ��, ��, ǿ������, ��, ��, ��ɫ, ����, ��, ��, ����, ˮ, ��, ��, �Ҵ�, ��, ��, ��ԭ, ��, ����, ����, ����, ��, ˮ��, ����, ��, ������, ��, Ư�׼�, ��, ����, ���ռ�, ��, ������̼, ���Ƽ�, ��, ��]

	 */
}
