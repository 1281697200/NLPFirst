package com.outsider.model.hmm;

/**
 * ���������е�ÿһ���ڵ�:����+״̬
 * 
 * @author outsider
 * 
 */
public class SequenceNode{
	/**��emission_probability�����е�ǰ���нڵ������λ�û���˵���λ��**/
	private int nodeIndex;
	/**��ǰ�ڵ��״̬**/
	private int state;
	
	public int getNodeIndex() {
		return nodeIndex;
	}
	public void setNodeIndex(int nodeIndex) {
		this.nodeIndex = nodeIndex;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public SequenceNode(int nodeIndex, int state) {
		super();
		this.nodeIndex = nodeIndex;
		this.state = state;
	}
	
	public SequenceNode() {}
}