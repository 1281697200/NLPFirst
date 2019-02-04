package com.outsider.nlp.nameEntityRecognition;

import java.util.List;
import java.util.Map;

import com.outsider.model.Model;
/**
 * ����ʵ��ʶ��ӿ�
 * @author outsider
 *
 */
public interface NER{
	/**
	 * ��ȡʵ�壬����һ��map��key��ʵ������value�Ǹ�����µ�ʵ��
	 * @param text
	 * @return
	 */
	public List<Entity> extractEntity(String text);
	/**
	 * ��ȡ����ʵ��
	 * @param text
	 * @return
	 */
	public List<Entity> getPersonNameEntity(String text);
	/**
	 * ��ȡ����ʵ��
	 * @param text
	 * @return
	 */
	public List<Entity> getLocationEntity(String text);
	
	/**
	 * ��ȡ��֯��
	 * @param text
	 * @return
	 */
	public List<Entity> getOrganizationEntity(String text);
	
	
}
