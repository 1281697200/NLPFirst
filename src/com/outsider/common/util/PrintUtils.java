package com.outsider.common.util;

public class PrintUtils {
	
	/**
	 * ����bmes״̬�ķִʴ�ӡ��
	 * @param predict Ԥ����
	 * @param sequence ԭʼ����
	 */
	public static void segmenterPrinter(int[] predict,String sequence) {
		String data = sequence.trim();
		for(int i = 0; i < predict.length;i++) {
			if(predict[i] == 2 || predict[i] == 3) {
				System.out.print(data.charAt(i)+"|");
			} else {
				System.out.print(data.charAt(i));
			}
		}
	}
	
	public static void segmenterPrinter2(int[] predict,String sentence) {
		for(int i = 0; i < predict.length;i++) {
			if(predict[i] == 0 || predict[i] == 1) {
				int a = i;
				int b = i;
				while(predict[i] != 2) {
					i++;
					b++;
					if(i == predict.length) {
						b--;
						break;
					}
				}
				for(int j = a; j <= b;j++) {
					System.out.print(sentence.charAt(j));
				}
				System.out.print("|");
			} else if(predict[i] == 2 || predict[i]==3) {
				System.out.print(sentence.charAt(i)+"|");
			}
		}
	}
	
}
