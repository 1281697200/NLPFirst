package com.outsider.nlp.nameEntityRecognition;

public class Entity {
	//ʵ�����
	private String entityType;
	//ʵ������
	private String entity;
	//��ʼλ��
	private int start;
	//����λ��
	private int end;
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	
	@Override
	public String toString() {
		return entity + ","+entityType +"("+start+","+end+")";
	}
	
}
