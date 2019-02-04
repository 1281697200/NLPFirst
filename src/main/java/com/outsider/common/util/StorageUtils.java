package com.outsider.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
/**
 * ����洢����
 * ���������з�final�ֶα��浽һ���ļ���
 * ��������ģ�͵ı��棬��ֻ�ʺ�Сģ�͵ı���
 * @author outsider
 *
 */
public class StorageUtils {
	public static void open(String directory, String fileName, Object obj) {
		Class currentClass = obj.getClass();
		if(fileName == null || fileName.trim().equals("")) {
			fileName = currentClass.getSimpleName()+"_fields";
		}
		List<Object[]> allValues = (List<Object[]>) IOUtils.readObject(directory+"//"+fileName);
		int count = 0;
		while(!currentClass.getName().equals("java.lang.Object")) {
			Field[] fields = currentClass.getDeclaredFields();
			fields = filterFinalField(fields);
			if(fields.length == 0) {
				currentClass = currentClass.getSuperclass();
				continue;
			}
			Object[] objs = (Object[]) allValues.get(count);
			for(int i = 0; i < fields.length; i++) {
				try {
					fields[i].setAccessible(true);
					fields[i].set(obj, objs[i]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			currentClass = currentClass.getSuperclass();
			count++;
		}
	}
	
	/**
	 * ���˵�����final���ֶ�
	 * @param fields
	 * @return
	 */
	private static Field[] filterFinalField(Field[] fields) {
		List<Field> noFinal = new ArrayList<>();
		for(int i = 0; i < fields.length; i++) {
			int modifiers = fields[i].getModifiers();
			//ֻ����final�ֶ�
			if(!Modifier.isFinal(modifiers)) {
				noFinal.add(fields[i]);
			}
		}
		Field[] res = new Field[noFinal.size()];
		noFinal.toArray(res);
		return res;
	}
	
	/**
	 * �����������Ե�ָ����Ŀ¼
	 * ���ǵ��ö�������ɸ��࣬����ҲҪ���游������ԣ����ǲ�����ʵ�ֵĽӿ��еĳ���
	 * ����ָ���ļ�����Ĭ������
	 * @param directory Ŀ¼
	 * @param fileName �ļ���
	 */
	public static void save(String directory, String fileName, Object obj) {
		Class currentClass = obj.getClass();
		List<Object[]> allValues = new ArrayList<>();
		if(fileName == null || fileName.trim().equals("")) {
			fileName = currentClass.getSimpleName()+"_fields";
		}
		while(!currentClass.getName().equals("java.lang.Object")) {
			//�����ֶεı���
			Field[] fields = currentClass.getDeclaredFields();
			fields = filterFinalField(fields);
			if(fields.length == 0) {
				//����д����bug��û�ж�currentClass���и��£��������ѭ��
				currentClass = currentClass.getSuperclass();
				continue;
			}
			try {
				Object[] values = new Object[fields.length];
				for(int i = 0; i < values.length;i++) {
					fields[i].setAccessible(true);
					values[i] = fields[i].get(obj);
				}
				allValues.add(values);
			} catch (Exception e) {
				e.printStackTrace();
			}
			currentClass = currentClass.getSuperclass();
		}
		IOUtils.writeObject2File(directory+"//"+fileName, allValues);
	}
}
