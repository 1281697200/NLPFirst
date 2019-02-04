package com.outsider.nlp.dependency;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.outsider.common.util.IOUtils;

/**
 * ������ģ��
 * ��������䷨��������
 * @author outsider
 *
 *
 *
id  ���� ���� ����(��) ����(ϸ) �䷨����   ���Ĵ�   ��ǰ�������ĴʵĹ�ϵ
1	���	���	a	ad	_	2	��ʽ	
2	����	����	v	v	_	0	���ĳɷ�	
3	̰��	̰��	v	v	_	7	�޶�	
4	��¸	��¸	n	n	_	3	��������	
5	��	��	u	udeng	_	3	��������	
6	����	����	n	n	_	7	�޶�	
7	����	����	v	vn	_	2	����	


����ģ��:
����һ�����ӵ�2�����ʣ�i��j������֮��Ĺ�ϵ���Ƿ������е�һ����𣬶������Ĳ��������������ģ�������
	i	j
  W(i)  W(j)
  P(i)  P(j)
 P(i+1) P(j+1)
 P(i+2) P(j+2)
 P(i-1) P(j-1)
 P(i-2) P(j-2)
 	Dis = i-j
W(i)+W(j) P(i)+P(j)
W(i)+W(j)+Dis P(i)+P(j)+Dis
P(i)+P(j)+P(i-1) P(i)+P(j)+P(i+1)
P(i)+P(j)+P(j-1) P(i)+P(j)+P(j+1)
��������:
W(i-2) W(j-2)
W(i-1) W(j-1)
W(i+1) W(j+1)
W(i+2) W(j+2)

����W�Ǵʱ���P�Ǵ��ԣ�Dis�Ǵʾ��룬������֮��
�Ӻű�ʾ�������
 */
public class CoNLLFeatureGenerator {
	//��ͬ����ά��֮��ķָ��
	public static final String splitChar = " ";
	/**
	 * ��������䷨������ѵ������ 
	 * @param lines CoNLL��ʽ����list
	 * @return ����List
	 */
	public static List<String> makeData(List<String> lines){
		//���ɾ���
		List<CoNLLSentence> sentences = makeSentence(lines);
		List<String> finalTrainData = new ArrayList<>();
		//�������������ģ����ȡ����������������ѵ������
		int count = 1;
		for(CoNLLSentence sentence : sentences) {
			//i��j�������е�����2�����ʣ�i��j֮������һ�����������û�������ϵֱ��ȡnull  
			//i�����Ĵ���j
			//��Ϊi=0���ýڵ�root�����������Ĵʣ�����i��1��ʼ
			System.out.println("sentence:"+(count++)+"/"+sentences.size());
			for(int i = 1; i < sentence.length(); i++) {
				CoNLLWord wordi = sentence.getWord(i);
				for(int j = 0; j < sentence.length(); j++) {
					if(i == j) continue;
					//���������ϵ��������
					String feature = null;
					if(j == wordi.getHEAD()) {
						feature = makeContextFeature(sentence, i, j, wordi.getDEPREL());
					} else { //�����������ϵ
						feature = makeContextFeature(sentence, i, j, CoNLLWord.NoneDEPREL);
					}
					finalTrainData.add(feature);
				}
			}
		}
		return finalTrainData;
	}
	
	
	public static String makeContextFeature(CoNLLSentence sentence, int i, int j, String DEPREL) {
		StringBuilder sb = new StringBuilder();
		//Ϊ�����ֲ�ͬά�ȵ���������Ҫ����������ʶ�����磬W(i)��W(j)�����ڲ�ͬά�ȵ�����������ֵ����ͬ
		//�����������֣�ѵ��ʱ�����������ͻ
		sb.append(sentence.LEMMA(i)+"i"+splitChar);//W(i)
		sb.append(sentence.LEMMA(j)+"j"+splitChar);//W(j)
		sb.append(sentence.CPOSTAG(i)+"i"+splitChar);//P(i)
		sb.append(sentence.CPOSTAG(j)+"j"+splitChar);//P(j)
		//P(i+1) P(j+1)
		 //P(i+2) P(j+2)
		sb.append(sentence.CPOSTAG(i+1)+"i+1"+splitChar);
		sb.append(sentence.CPOSTAG(j+1)+"j+1"+splitChar);
		sb.append(sentence.CPOSTAG(i+2)+"i+2"+splitChar);
		sb.append(sentence.CPOSTAG(j+2)+"j+2"+splitChar);
		//P(i-1) P(j-1)
		 //P(i-2) P(j-2)
		sb.append(sentence.CPOSTAG(i-1)+"i-1"+splitChar);//P(i)
		sb.append(sentence.CPOSTAG(j-1)+"j-1"+splitChar);//P(j)
		sb.append(sentence.CPOSTAG(i-2)+"i-2"+splitChar);//P(i)
		sb.append(sentence.CPOSTAG(j-2)+"j-2"+splitChar);//P(j)
		//Dis
		int Dis = i - j;
		sb.append(Dis+splitChar);
		//W(i)+W(j) P(i)+P(j)
		sb.append(sentence.LEMMA(i)+sentence.LEMMA(j)+"0"+splitChar);
		sb.append(sentence.CPOSTAG(i)+sentence.CPOSTAG(j)+"1"+splitChar);
		//W(i)+W(j)+Dis P(i)+P(j)+Dis
		sb.append(sentence.LEMMA(i)+sentence.LEMMA(j)+Dis+"2"+splitChar);
		sb.append(sentence.CPOSTAG(i)+sentence.CPOSTAG(j)+Dis+"3"+splitChar);
		//P(i)+P(j)+P(i-1) P(i)+P(j)+P(i+1)
		sb.append(sentence.CPOSTAG(i)+sentence.CPOSTAG(j)+sentence.CPOSTAG(i-1)+"4"+splitChar);
		sb.append(sentence.CPOSTAG(i)+sentence.CPOSTAG(j)+sentence.CPOSTAG(i+ 1)+"5"+splitChar);
		//P(i)+P(j)+P(j-1) P(i)+P(j)+P(j+1)
		sb.append(sentence.CPOSTAG(i)+sentence.CPOSTAG(j)+sentence.CPOSTAG(j-1)+"6"+splitChar);
		sb.append(sentence.CPOSTAG(i)+sentence.CPOSTAG(j)+sentence.CPOSTAG(j+ 1)+"7"+splitChar);
		//W(i+1) W(j+1)
		//W(i+2) W(j+2)
		sb.append(sentence.LEMMA(i+1)+"a"+splitChar);
		sb.append(sentence.LEMMA(j+1)+"b"+splitChar);
		sb.append(sentence.LEMMA(i+2)+"c"+splitChar);
		sb.append(sentence.LEMMA(j+2)+"d"+splitChar);
		//W(i-1) W(j-1)
		//W(i-2) W(j-2)
		sb.append(sentence.LEMMA(i-1)+"e"+splitChar);
		sb.append(sentence.LEMMA(j-1)+"f"+splitChar);
		sb.append(sentence.LEMMA(i-2)+"g"+splitChar);
		sb.append(sentence.LEMMA(j-2)+"h"+splitChar);
		//�����ϱ�ǩ
		sb.append(DEPREL);
		return sb.toString();
	}
	
	/**
	 * ��CoNLL�е��н���Ϊ������ӡ�
	 * @param lines
	 * @return
	 */
	public static List<CoNLLSentence> makeSentence(List<String> lines){
		
		List<ArrayList<String>> sentences = new ArrayList<>();
		ArrayList<String> sentence = new ArrayList<>();
		for(int i = 0; i < lines.size(); i++) {
			String line = lines.get(i).trim();
			if(line.equals("")) {
				//if(sentence.size() > 0) {
				sentences.add(sentence);
				//} else {
				//	System.out.println("���ֿ�sentence������");
				//}
				sentence = new ArrayList<>();
			} else {
				sentence.add(line);
			}
		}
		
		List<CoNLLSentence> data = new ArrayList<>();
		for(ArrayList<String> se : sentences) {
			data.add(new CoNLLSentence(se));
		}
		return data;
	}
	
	public static void main(String[] args) {
		List<String> lines = IOUtils.readTextAndReturnLines("D:\\nlp����\\����䷨����\\�������ѵ������_NLP&CC 2013\\THU\\train.conll", "utf-8");
		List<String> samples = makeData(lines);
		String savePath = "D:\\\\nlp����\\\\����䷨����\\\\�������ѵ������_NLP&CC 2013\\\\THU\\train_maxent.data";
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(savePath));
			for(String s : samples) {
				bufferedWriter.write(s);
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
