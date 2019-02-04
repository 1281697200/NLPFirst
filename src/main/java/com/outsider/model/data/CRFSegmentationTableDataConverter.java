package com.outsider.model.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.outsider.common.dataStructure.Table;
import com.outsider.model.hmm.SequenceNode;
/**
 * CRFѵ�����ݸ�ʽ�ķִ�����ת�������н��
 * ״̬ BMES/0123
 * �۲��ַ�Unicode��
 * @author outsider
 *
 */
public class CRFSegmentationTableDataConverter implements DataConverter<Table, List<SequenceNode>>{
	//ָ��xʹ��������
	private int xColumnIndex;
	//ָ��yʹ����ʹ������
	private int yColumnIndex;
	private Map<Character, Integer> state2Int = new HashMap<>();
	public CRFSegmentationTableDataConverter(int xColumnIndex, int yColumnIndex) {
		super();
		this.xColumnIndex = xColumnIndex;
		this.yColumnIndex = yColumnIndex;
		state2Int.put('B', 0);
		state2Int.put('M', 1);
		state2Int.put('E', 2);
		state2Int.put('S', 3);
	}
	
	@Override
	public List<SequenceNode> convert(Table table, Object... otherParameters) {
		List<SequenceNode> nodes = new ArrayList<>(table.getRowNum());
		for(int i = 0; i < table.getRowNum(); i++) {
			char word = table.get(i, xColumnIndex).charAt(0);
			char state = table.get(i, yColumnIndex).charAt(0);
			if(word == ' ' || state == ' ') {
				continue;
			}
			SequenceNode node = new SequenceNode((int)word, state2Int.get(state));
			nodes.add(node);
		}
		return nodes;
	}

}
