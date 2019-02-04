package com.outsider.common.algorithm;


/**
 * ��С������
 * @author outsider
 * 
 */

public class MinimumSpanningTree {
	/**
	 * �㷨ʵ��
	 * @param adjacentMatrix �ڽӾ���Ҫ�󲻴��ڵı�Ȩ����ΪFloat.MAX_VALUE
	 * @return һ�����飬�±��ʾ�յ㣬��Ӧ��ֵ��ʾ��㣬��Щ�����յ㹹�ɵı߼�������С�������ı߼����������������ô�����е�
	 * ֵΪ-1
	 */
	public static int[] primAlgorithm(float[][] adjacentMatrix){
		int vn = adjacentMatrix.length;
		float[] cost = new float[vn];//��¼�Ѿ����뵽mst�еĵ㵽������֮��ľݻ��ѣ��ᱻ����
		int[] mst = new int[vn];//��¼cost�е�����
		boolean[] V = new boolean[vn];//��¼�ڵ��Ƿ��Ѿ���mst��
		//Ĭ��ѡ��ڵ�0��Ϊ���
		//��ʼ��
		for(int i = 0; i < vn; i++) {
			if(adjacentMatrix[0][i] == Float.MAX_VALUE) {
				cost[i] = Float.MAX_VALUE;
				mst[i] = -1;
			} else {
				cost[i] = adjacentMatrix[0][i];
				mst[i] = 0;
			}
		}
		V[0] = true;
		//vn - 1��ѭ����ÿ���ҳ�һ���㵽mst��
		for(int i = 0; i < vn - 1; i++) {
			//�ҳ�Ȩ����С�ĵ���뵽v����
			int min = -1;
			float minCost = Float.MAX_VALUE;
			for(int j = 1; j < vn; j++) {
				if(!V[j]) {
					if(cost[j] < minCost) {
						minCost = cost[j];
						min = j;
					}
				}
			}
			//����V��
			V[min] = true;
			//����cost���¼��뵽V���еĵ���ܴ��ڸ��̵�·���ﵽδ���뵽v���е���Щ��
			for(int j = 1; j < vn; j++) {
				if(!V[j] && adjacentMatrix[min][j] < cost[j]) {
					cost[j] = adjacentMatrix[min][j];
					mst[j] = min;
				}
			}
		}
		return mst;
	}
	
}
