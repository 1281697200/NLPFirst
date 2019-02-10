package com.outsider.nlp.nameEntityRecognition;

public class Entity {
	//ʵ�����
	private char entityType;
	//ʵ������
	private String entity;
	//��ʼλ��
	private int start;
	//����λ��
	private int end;
	public char getEntityType() {
		return entityType;
	}
	public void setEntityType(char entityType) {
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
	
	@Override
	public boolean equals(Object obj) {
		Entity e2 = (Entity) obj;
		if(this.entity.equals(e2.entity) && this.start == e2.start && this.end == e2.end
				&& this.entityType == e2.entityType) {
			return true;
		}
		return false;
	}
	
}
