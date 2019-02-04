package com.outsider.test;

import java.util.ArrayList;
import java.util.List;

public class ArrayTest {
	public static void main(String[] args) {
		List<String> strList = new ArrayList<>();
		for(int i = 0; i < 5000000; i++) {
			strList.add(Math.random()*100+"");
		}
		toArray1(strList);
		toArray2(strList);
		//ʮ���������ѭ�����Ƹ���
		//�ϰ��򼶱�ѭ������Ҫ��һЩ
		//���Խ�� һǧ���������ѭ�����Ʒ�������
		
	}
	
	public static void toArray1(List<String> strList) {
		long start = System.currentTimeMillis();
		String[] arr = new String[strList.size()];
		strList.toArray(arr);
		long end = System.currentTimeMillis();
		System.out.println("ʹ��List�Դ��ĸ������飬��ʱ:"+(end - start)+"����");
	}
	public static void toArray2(List<String> strList) {
		long start = System.currentTimeMillis();
		String[] arr = new String[strList.size()];
		for(int i = 0; i < strList.size(); i++) {
			arr[i] = strList.get(i);
		}
		long end = System.currentTimeMillis();
		System.out.println("ѭ���������飬��ʱ:"+(end - start)+"����");
	}
}
