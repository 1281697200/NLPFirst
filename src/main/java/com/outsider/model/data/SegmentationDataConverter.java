package com.outsider.model.data;

import java.util.ArrayList;
import java.util.List;

import com.outsider.model.hmm.SequenceNode;

public class SegmentationDataConverter implements DataConverter<String[], List<SequenceNode>>{
	@Override
	public List<SequenceNode> convert(String[] words, Object... otherParameters) {
		List<SequenceNode> nodes = new ArrayList<>();
		for(String token : words) {
			token = token.trim();
			if(token.length() == 1) { //���ֳɴ�
				SequenceNode node = new SequenceNode(token.charAt(0), 3);
				nodes.add(node);
			} else if(token.length() == 2) {//˫�ֳɴ�
				nodes.add(new SequenceNode(token.charAt(0), 0));
				nodes.add(new SequenceNode(token.charAt(1),  2));
			} else if(token.length() >2){//����2���ַ��ɴ�
				int len = token.length();
				//֮ǰ����д����bug�����뿼��˳�򣬲��ܽ���λ��
				nodes.add(new SequenceNode(token.charAt(0), 0));
				for(int i = 1; i < len-1;i++) {
					nodes.add(new SequenceNode(token.charAt(i), 1));
				}
				nodes.add(new SequenceNode(token.charAt(len-1), 2));
			}
		}
		return nodes;
	}
	
	public SegmentationDataConverter() {}
}
