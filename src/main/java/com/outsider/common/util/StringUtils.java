package com.outsider.common.util;

import java.util.Arrays;

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
	
	
	/**
	 * ȫ�Ƿ���ת��Ϊ��Ƿ���
	 * @param fullWidthStr
	 * @return
	 */
	public static String fullWidthChar2HalfWidthChar(String fullWidthStr) {
		if (null == fullWidthStr || fullWidthStr.length() <= 0) {
            return "";
        }
        char[] charArray = fullWidthStr.toCharArray();
        //��ȫ���ַ�ת����char�������
        for (int i = 0; i < charArray.length; ++i) {
            int charIntValue = (int) charArray[i];
            //�������ת����ϵ,����Ӧ�±�֮�����ƫ����65248;����ǿո�Ļ�,ֱ����ת��
            if (charIntValue >= 65281 && charIntValue <= 65374) {
                charArray[i] = (char) (charIntValue - 65248);
            } else if (charIntValue == 12288) {
                charArray[i] = (char) 32;
            }
        }
        return new String(charArray);
	}
	
	public static void main(String[] args) {
		String[] s1 = new String[] {"1"};
		String[] s2 = new String[] {"2"};
		s1 = concat(s1, s2);
		System.out.println(Arrays.toString(s1));
	}
}
