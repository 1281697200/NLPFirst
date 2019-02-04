package com.outsider.model.crf.unsupervised.old;
/**
 * ĳ����������������ѵ�����е���Щλ��
 * ������Щλ��Ϊ�����ݶ�ʱʹ��
 * @author outsider
 *	
 */

import java.util.ArrayList;
import java.util.List;

public class PositionsOfFeatureFunctionShowing {
	private List<List<Integer>> positions;
	public PositionsOfFeatureFunctionShowing(int stateNum) {
		positions = new ArrayList<>(stateNum);
		for(int i = 0; i < stateNum; i++) {
			positions.add(new ArrayList<>());
		}
	}
	
	public void addIndex(int index, int state) {
		positions.get(state).add(index);
	}
	
	public boolean isAppearing(int index, int state) {
		if(positions.get(state).contains(index))
			return true;
		return false;
	}
	
	public List<Integer> getPositionsOfFeatureFunction(int state){
		return positions.get(state);
	}
}
