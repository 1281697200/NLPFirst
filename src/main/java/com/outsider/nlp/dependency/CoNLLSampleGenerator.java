package com.outsider.nlp.dependency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ���ݴ��Ժʹ����ɴ�Ԥ������
 * �͵�ģ����������
 * @author outsider
 * 
 */
public class CoNLLSampleGenerator {
	/**
	 * ������������Ԥ������
	 * @param words �����а�˳�����еĴ�
	 * @param natures ��Ӧ�Ĵ���
	 * @return һ����ά�������飬����Context[0][1]��ʾROOT�ڵ�͵�һ����֮�����ɵ������
	 */
	public static CoNLLSample[][] generate(String[] words, String[] natures) {
		int len = words.length + 1;
		String[] wordsWithRoot = new String[len];
		String[] naturesWithRoot = new String[len];
		System.arraycopy(words, 0, wordsWithRoot, 1, words.length);
		System.arraycopy(natures, 0, naturesWithRoot, 1, natures.length);
		wordsWithRoot[0] = CoNLLWord.ROOT_LEMMA;
		naturesWithRoot[0] = CoNLLWord.ROOT_CPOSTAG;
		CoNLLSample[][] allSamples = new CoNLLSample[len][len];
		for(int i = 1; i < len; i++) {
			for(int j = 0; j < len; j++) {
				//һ������:���ڸ��ڵ㲻Ӧ��ֻ��ĳ���ڵ�ָ��������������ָ�������ڵ㡣
				if(i == j) continue;
				String[] context = makeContextFeature(wordsWithRoot, naturesWithRoot, i, j);
				allSamples[i][j] = new CoNLLSample(context);
			}
		}
		return allSamples;
	}
	
	public static List<CoNLLSample[][]> generate(List<String[]> wordsOfSentences, List<String[]> naturesOfSentences) {
		List<CoNLLSample[][]> contexts = new ArrayList<>(wordsOfSentences.size());
		for(int i = 0; i < wordsOfSentences.size(); i++) {
			CoNLLSample[][] context = generate(wordsOfSentences.get(i), naturesOfSentences.get(i));
			contexts.add(context);
		}
		return contexts;
	}
	
	
	public static String[] makeContextFeature(String[] words, String[] natures, int i, int j) {
		int len = words.length;
		String[] context = new String[29];
		//Ϊ�����ֲ�ͬά�ȵ���������Ҫ����������ʶ�����磬W(i)��W(j)�����ڲ�ͬά�ȵ�����������ֵ����ͬ
		//�����������֣�ѵ��ʱ�����������ͻ
		context[0] = words[i] + "i";//W(i)
		context[1] = words[j] + "j";//W(j)
		context[2] = natures[i]+"i";//P(i)
		context[3] = natures[j]+"j";//P(j)
		//P(i+1) P(j+1)
		 //P(i+2) P(j+2)
		context[4] = i + 1 >= len ? CoNLLWord.OOICPOSTAG+"i+1" : natures[i+1]+"i+1";
		context[5] = j + 1 >= len ? CoNLLWord.OOICPOSTAG+"j+1" : natures[j+1]+"j+1";
		context[6] = i + 2 >= len ? CoNLLWord.OOICPOSTAG+"i+2": natures[i+2]+"i+2";
		context[7] = j + 2 >= len ? CoNLLWord.OOICPOSTAG+"j+2" : natures[j+2]+"j+2";
		//P(i-1) P(j-1)
		 //P(i-2) P(j-2)
		context[8] = i - 1 < 0? CoNLLWord.OOICPOSTAG+"i-1" : natures[i-1]+"i-1";
		context[9] = j - 1 < 0? CoNLLWord.OOICPOSTAG+"j-1" : natures[j-1]+"j-1";
		context[10] = i - 2 < 0? CoNLLWord.OOICPOSTAG+"i-2" : natures[i-2]+"i-2";
		context[11] = j - 2 < 0? CoNLLWord.OOICPOSTAG+"j-2" : natures[j-2]+"j-2";
		//Dis
		int Dis = i - j;
		context[12] = Dis+"";
		//W(i)+W(j) P(i)+P(j)
		context[13] = words[i] + words[j] + "0";
		context[14] = natures[i] + natures[j] + "1";
		//W(i)+W(j)+Dis P(i)+P(j)+Dis
		context[15] = words[i] + words[j] + Dis+"2";
		context[16] = natures[i] + natures[j] + Dis+"3";
		//P(i)+P(j)+P(i-1) P(i)+P(j)+P(i+1)
		context[17] = natures[i] + natures[j] + (i - 1 < 0? CoNLLWord.OOICPOSTAG : natures[i - 1])+"4";
		context[18] = natures[i] + natures[j] + (i + 1 >= len? CoNLLWord.OOICPOSTAG : natures[i + 1])+"5";
		//P(i)+P(j)+P(j-1) P(i)+P(j)+P(j+1)
		context[19] = natures[i] + natures[j] + (j - 1 < 0? CoNLLWord.OOICPOSTAG : natures[j - 1])+"6";
		context[20] = natures[i] + natures[j] + (j + 1 >= len? CoNLLWord.OOICPOSTAG : natures[j + 1])+"7";
		//W(i+1) W(j+1)
		//W(i+2) W(j+2)
		context[21] = i+1 >= len? CoNLLWord.OOILEMMA+"a" : words[i+1]+"a";
		context[22] = j+1 >= len? CoNLLWord.OOILEMMA+"b" : words[j+1]+"b";
		context[23] = i+2 >= len? CoNLLWord.OOILEMMA+"c" : words[i+2]+"c";
		context[24] = j+2 >= len? CoNLLWord.OOILEMMA+"d" : words[j+2]+"d";
		//W(i-1) W(j-1)
		//W(i-2) W(j-2)
		context[25] = i-1 < 0? CoNLLWord.OOILEMMA+"e" : words[i-1]+"e";
		context[26] = j-1 < 0? CoNLLWord.OOILEMMA+"f" : words[j-1]+"f";
		context[27] = i-2 < 0? CoNLLWord.OOILEMMA+"g" : words[i-2]+"g";
		context[28] = j-2 < 0? CoNLLWord.OOILEMMA+"h" : words[j-2]+"h";
		//�����ϱ�ǩ
		return context;
	}
	
	public static void main(String[] args) {
		CoNLLSample[][] context = generate(new String[] {"���","����","̰��","��¸","��","����","����"}, new String[] {"a","v","v","n","u","n","v"});
		System.out.println("���������:");
		//����j��i�����Ĵ�Ҳ���Ǳߵ�ָ����i-->j
		for(int i = 0; i < context.length; i++) {
			for(int j = 0; j < context.length; j++) {
				System.out.println("�����,"+i+"->"+j+":");
				System.out.println(context[i][j]);
			}
		}
	}
}

/**
 * 
 * 
1	���	���	a	ad	_	2	��ʽ	
2	����	����	v	v	_	0	���ĳɷ�	
3	̰��	̰��	v	v	_	7	�޶�	
4	��¸	��¸	n	n	_	3	��������	
5	��	��	u	udeng	_	3	��������	
6	����	����	n	n	_	7	�޶�	
7	����	����	v	vn	_	2	����	
 * 
 */
