package com.outsider.model.metric;

public class SegmenterEvaluationTest {
	public static void main(String[] args) {
		String[] right = new String[] {"�����","����","������"};
		String[] predict = new String[] {"�����","��","��","������"};
		SegmenterEvaluation evaluation = new SegmenterEvaluation();
		evaluation.score(right, predict);
		evaluation.printScore();
	}
}
