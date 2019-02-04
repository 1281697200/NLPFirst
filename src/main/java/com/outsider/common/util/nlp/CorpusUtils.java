package com.outsider.common.util.nlp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.outsider.common.util.IOUtils;

/**
 * ���ϲ�����صĹ�����
 * @author outsider
 *
 */
public class CorpusUtils {
	/**
	 * ����CRF��ʽ�ķִ����ϣ���ǩ��B/M/E/S
	 * @param words ��������
	 * @return ����õ����ݣ�
	 * ����: 
	 * ��		B
	 * ��		E
	 * ��		S	
	 */
	public static String makeCRFormatSegmentationCorpus(String[] words) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < words.length;i++) {
			words[i] = words[i].trim();
			if(words[i].length() == 0) {
				continue;
			}
			if(words[i].length() == 1) {
				sb.append(words[i]+"\t"+"S\n");
			} else {
				if(i == 0) {
					System.out.println(words[0]);
					System.out.println(words[0].length());
				}
				String s = words[i];
				sb.append(s.charAt(0)+"\t"+"B\n");
				for(int j = 1; j < s.length()-1;j++) {
					sb.append(s.charAt(j)+"\t"+"M\n");
				}
				sb.append(s.charAt(s.length()-1)+"\t"+"E\n");
			}
		}
		return sb.toString();
	}
	/**
	 * ����PKU2014�����ձ������Ͻ������еķִʽ���ʹ��Ա�ע���
	 * ��ʽ����:
	 *  ������/nz 1��1��/t Ѷ/ng ��/p ��/w [ŦԼ/nsf ʱ��/n]/nz ��
	 * @param corpus û�о����κδ����ԭʼ����
	 * @param splitChar �ָ���
	 * @return ���ؽ������������: 
	 * ���	d
	 * ��	ude
	 * ��	w
	 */
	public static StringBuilder makeCRFormatPOSTaggingCorpusWithPKUFormat(String corpus, String splitChar) {
		//��PKU�����ձ����зֱ�׼��Ҫ����
		StringBuilder result = new StringBuilder();
		Pattern pattern0 = Pattern.compile("\\[[^\\[\\]]+\\]/{0,1}\\w{1,5}\\s");
		Matcher matcher0 = pattern0.matcher(corpus);
		int start = 0;
		int lastEnd = 0;
		while(matcher0.find()) {
			String s = matcher0.group();
			start = matcher0.start();
			//������[]���ʱ��������ǰ��ķ�������������ٴ������������
			//lastEnd -> start
			dealWithoutBracketSituation(corpus.substring(lastEnd, start), splitChar, result);
			dealBracketSituation(matcher0.group(), splitChar, result);
			lastEnd = matcher0.end();
		}
		//�������һ��ƥ�䵽��[]���沿��
		if(lastEnd < corpus.length()) {
			dealWithoutBracketSituation(corpus.substring(lastEnd, corpus.length()), splitChar, result);
		}
		return result;
	}
	
	//�������������
	private static void dealBracketSituation(String s, String splitChar, StringBuilder result){
		int in = s.lastIndexOf("]");//�ҵ�[]�е�����
		//����[]�ڵ�
		String[] s2 = s.substring(1, in).split(splitChar);
		for(int j = 0; j < s2.length;j++) {
			//���ַ�ʽ���Խ�� //w��������ĳ���
			int index = s2[j].lastIndexOf("/");
			if(index == -1) {
				System.out.println(s.trim());
				continue;
			}
			String word = s2[j].substring(0, index).toLowerCase().trim();
			String wordNature = s2[j].substring(index+1, s2[j].length()).trim();
			result.append(word+"\t"+wordNature+"\n");
		}
	}
		
	private static void dealWithoutBracketSituation(String subCorpus, String splitChar, StringBuilder result){
		String[] words = subCorpus.split(splitChar);
		for(int i =0; i< words.length;i++) {
			//�����д���һЩû�б���Ǵ��ԵĴ���
			if(!words[i].contains("/")) {
				System.out.println(words[i].trim());
				continue;
			}
			int index = words[i].lastIndexOf("/");
			String word = words[i].substring(0, index).toLowerCase().trim();
			String wordNature = words[i].substring(index+1, words[i].length()).trim();
			result.append(word+"\t"+wordNature+"\n");
		}
	}
	
	public static void main(String[] args) {
		//�������о���У����Ȼ���ڴ�����Щ����û�б���ע����
		//String data = IOUtils.readText("D:\\nlp����\\���Ա�ע\\�����ձ����Ͽ�2014\\2014�����ձ��з�����6_utf8_�����ĺͷ��ŷֿ�.txt", "utf-8");
		String data = IOUtils.readText("D:\\nlp����\\���Ա�ע\\�����ձ����Ͽ�2014\\2014�����ձ��з�����6_utf8_�����ĺͷ��ŷֿ�.txt", "utf-8"); 
		StringBuilder result = makeCRFormatPOSTaggingCorpusWithPKUFormat(data, " ");
		//IOUtils.writeTextData2File(result.toString(), "D:\\nlp����\\���Ա�ע\\���Ա�ע_crf.txt", "utf-8");
	}
}
