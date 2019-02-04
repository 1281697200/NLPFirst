package com.outsider.model.metric;

import java.util.ArrayList;
import java.util.List;

/**
 * �ִ�����
 * @author outsider
 * ���� 
 * 	�ƽ��׼:����� ���� ������
 *	�ִʽ��: ����� �� �� ������
 *	�Իƽ��׼�кͷִ����еĵ�ÿһ��������ע
 *	�ƽ��׼:(0,2),(3,4),(5,7)
 *	�ִ���:(0,2),(3),(4),(5,7)
 *	�Աȷ��ִַ����б�ע��ȷ����2��������ע����Ŀ�����Ϊ��1����Ҳ������Ϊ��2��
 *	������������㷨����Ϊ���Ԥ���ע����Ľ�һ���ʱ�ǳ�n������ô�����עҲ��n����������1��
 *	���Ԥ��������Ľ������ճ��һ����ô��ע�������Ϊ��1��
 *
 */
public class SegmenterEvaluation {
	/**
	 * �ٻ��ʣ���ȫ�ʣ�Խ��Խ��
	 */
	private double recallScore;
	/**
	 * ���ȣ�׼ȷ�ʣ�Խ��Խ��
	 */
	private double precisionScore;
	/**
	 * f�÷֣��ۺ�������Խ��Խ��
	 */
	private double fMeasureScore;
	/**
	 * �����ʣ�ԽСԽ��
	 */
	private double errorRate;
	public void score(String[] right, String[] predict) {
		int rightCount = rightCount(right, predict);
		this.recallScore = rightCount*1.0 / right.length;
		this.precisionScore = rightCount*1.0 / predict.length;
		this.fMeasureScore = (2*precisionScore*recallScore) / (precisionScore + recallScore);
		this.errorRate = (predict.length -rightCount*1.0) / right.length;
	}
	
	public void printScore() {
		System.out.println("�ٻ���:"+this.recallScore+",��׼��:"+this.precisionScore+",Fֵ:"+this.fMeasureScore+",������:"+this.errorRate);
	}
	
	/**
	 * ͳ�Ʒִʽ������ȷ�ĸ���
	 * @param right ��ȷ�ķִʽ��
	 * @param predict Ԥ��ķִʽ��
	 * @return
	 */
	public int rightCount(String[] right, String[] predict) {
		List<WordNode> rightNodes = buildNodes(right);
		List<WordNode> predictNodes = buildNodes(predict);
		//ͳ�Ʊ�ע��ȷ�ĸ���
		int count = 0;
		for(int i = 0; i < predictNodes.size(); i++) {
			if(rightNodes.contains(predictNodes.get(i))) {
				count++;
			}
		}
		return count;
	}
	
	public List<WordNode> buildNodes(String[] words){
		List<WordNode> nodes = new ArrayList<>();
		int last = 0;
		for(int i = 0; i < words.length; i++) {
			WordNode node = new WordNode(last, last + words[i].length() - 1);
			nodes.add(node);
			last = node.end + 1;
			
		}
		return nodes;
	}
	
	
	/**
	 * ����һ������һ���ִʽ�����е�λ��
	 * @author outsider
	 *
	 */
	public static class WordNode{
		//��ʼλ�úͽ�βλ�ö�����
		public int start;//�ʵĿ�ʼλ��
		public int end;//�ʵĽ�βλ��
		
		public WordNode(int start, int end) {
			super();
			this.start = start;
			this.end = end;
		}

		@Override
		public boolean equals(Object obj) {
			WordNode node = (WordNode) obj;
			if(this.start == node.start && this.end == node.end)
				return true;
			return false;
		}
		
		@Override
		public String toString() {
			return "("+start+","+end+")";
		}
	}


	public double getRecallScore() {
		return recallScore;
	}
	public double getPrecisionScore() {
		return precisionScore;
	}
	public double getfMeasureScore() {
		return fMeasureScore;
	}
	public double getErrorRate() {
		return errorRate;
	}
	
}
