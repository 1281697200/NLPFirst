package com.outsider.common.util;

import java.util.Arrays;

import org.junit.Test;

public class StringUtils {
	/**
	 * ����2���ַ�������
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	public static String[] concat(String[] arr1, String[] arr2) {
        int strLen1 = arr1.length;// �����һ�����鳤��
        int strLen2 = arr2.length;// ����ڶ������鳤��
        arr1 = Arrays.copyOf(arr1, strLen1 + strLen2);// ����
        System.arraycopy(arr2, 0, arr1, strLen1, strLen2);// ���ڶ����������һ������ϲ�
        // System.out.println(Arrays.toString(arr1));// �������
        return arr1;
	}
}
