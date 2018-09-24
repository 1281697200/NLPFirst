package com.outsider.demo.nlp;

import java.util.Arrays;

import com.outsider.nlp.lexicalanalyzer.BasicLexicalAnalyzer;

public class BasicLexicalAnalyzerDemo {
	public static void main(String[] args) {
		String str = "2018��9��24�գ�����������ڣ�������ʲô�أ�";
		String[] r1 = BasicLexicalAnalyzer.analyze(str);
		System.out.println(Arrays.toString(r1));
		String str1 = "ԭ���⣺��ý�ĵ����ֳ�����һĻ" + 
				"���ձ���������NNN��9��8�ձ�������ǰ���ձ������������������ս��֮һ��ֱ������ĸ���Ӻء������Ϻ�����ʱ��������й�����ս���ֽ����ټ��ӡ�" ; 
		String[] r2 = BasicLexicalAnalyzer.analyze(str1);
		System.out.println(Arrays.toString(r2));
	}
}
