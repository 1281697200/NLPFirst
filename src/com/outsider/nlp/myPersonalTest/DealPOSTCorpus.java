package com.outsider.nlp.myPersonalTest;

import java.io.File;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.junit.Test;

import com.outsider.common.util.IOUtils;
import com.outsider.common.util.nlp.ChineseDictionarySortUtil;

public class DealPOSTCorpus {
	
	
	@Test
	public void t1() {
		String basepath = "D:\\nlp����\\�����ձ����Ͽ�2014\\2014";
		int start = 101;
		List<String> all = new ArrayList<>();
		for(int i = start; i <= 123;i++) {
			String dpath = basepath+"\\0"+ i;
			File file = new File(dpath);
			String[] files = file.list();
			for(int j = 0; j < files.length;j++) {
				List<String> lines = IOUtils.readTextAndReturnLinesCheckLineBreak(dpath+"\\"+files[j], "utf-8",true);
				all.addAll(lines);
			}
		}
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < all.size(); i++) {
			sb.append(all.get(i));
		}
		IOUtils.writeTextData2File(sb.toString(), "C:\\Users\\outsider\\Desktop\\2014�����ձ��з�����.txt", "utf-8");
	}
	
	@Test
	public void t4() {
		String path = "D:\\nlp����\\���Ա�ע\\�����ձ����Ͽ�2014\\2014�����ձ��з�����4_utf8_������С���ϲ�.txt";
		List<String> corpus = IOUtils.readTextAndReturnLinesCheckLineBreak(path, "utf-8", true);
		StringBuilder sb = new StringBuilder();
		for(String s : corpus) {
			String ss = s;
			for(int i = 0;i < 4;i++) {
				ss = ss.replaceAll("  ", " ");
			}
			//ss =ss.trim();
			sb.append(ss);
		}
		IOUtils.writeTextData2File(sb.toString(), "D:\\\\nlp����\\\\���Ա�ע\\\\�����ձ����Ͽ�2014\\\\2014�����ձ��з�����5_utf8_������С���ϲ�.txt", "utf-8");
		
	}
	@Test
	public void t2() {
		String path = "D:\\nlp����\\���Ա�ע\\�����ձ����Ͽ�2014\\2014�����ձ��з�����4_utf8_������С���ϲ�.txt";
		//String path = "D:\\nlp����\\���Ա�ע\\���Ա�ע@�����ձ�199801.txt";
		String splitChar = " ";
		String corpus = IOUtils.readText(path, "utf-8");
		Set<String> partOfSpeech = new HashSet<>();
		Pattern pattern0 = Pattern.compile("\\[[^\\[\\]]{1,80}\\]/{0,1}\\w{1,5}\\s");
		Matcher matcher0 = pattern0.matcher(corpus);
		int lastEnd = 0;
		while(matcher0.find()) {
			String s = matcher0.group();
			String regx2 = "\\[.+\\]";
			Pattern pattern2 = Pattern.compile(regx2);
			Matcher matcher2 = pattern2.matcher(s);
			matcher2.find();
			int start = matcher2.start();
			int end = matcher2.end();
			//����[]�ڵ�
			String[] s2 = s.substring(start+1, end-1).split(splitChar);
			for(int j = 0; j < s2.length;j++) {
				String[] s3 = s2[j].split("/");
				if(s3.length<2) {
					System.out.println(s);
				} else {
					//���ڴ���//w����
					if(s3.length > 2)
						partOfSpeech.add(s3[2]);
					else
						partOfSpeech.add(s3[1]);
				}
			}
			//����[]���幹�ɵĴʵĴ���
			String s4 = s.substring(end+1, s.length()-1);
			partOfSpeech.add(s4);
		}
		corpus = matcher0.replaceAll("");//�滻����[]/
		String[] words = corpus.split(splitChar);
		for(int i =0; i< words.length;i++) {
			if(words[i].trim().equals("")) {
				continue;
			}
			// [���/w��/d]/s ����˼[]�໹������Ϊ��һ���ʲ��Ҵ�����/s
			int in = words[i].lastIndexOf("/");
			partOfSpeech.add(words[i].substring(in+1, words[i].length()));
		}
		System.out.println("�����ԣ�"+partOfSpeech.size());
		for(String s : partOfSpeech) {
			System.out.println(s);
		}
	}
	
	
	
	
	@Test
	public void t5() {
		String path = "D:\\nlp����\\�����ձ����Ͽ�2014\\2014�����ձ��з�����2_utf8.txt";
		String splitChar = " ";
		//12/m ./w 88/m
		String corpus = IOUtils.readText(path, "utf-8");
		Pattern pattern = Pattern.compile("[0-9]+/m\\s\\./w\\s[0-9]+/m");
		Matcher matcher = pattern.matcher(corpus);
		/*while(matcher.find()) {
			System.out.println(matcher.group());
		}*/
		corpus = corpus.replaceAll("([0-9]+)/m\\s\\./w\\s([0-9]+)/m", "$1.$2/m");
		IOUtils.writeTextData2File(corpus, "D:\\nlp����\\�����ձ����Ͽ�2014\\2014�����ձ��з�����4_utf8.txt", "utf-8");
	}
	
	@Test
	public void t6() {
		String s = "1825/m ./w 1/m 289/m ./w 7/m";
		s = s.replaceAll("([0-9]+)/m\\s\\./w\\s([0-9]+)/m", "$1.$2/m");
		System.out.println(s);
	}
	
	
	@Test
	public void t7() {
		//��һ�δ��������еĴ���
		String path = "D:\\nlp����\\���Ա�ע\\�����ձ����Ͽ�2014\\2014�����ձ��з�����5_utf8_������С���ϲ�.txt";
		String splitChar = " ";
		String corpus = IOUtils.readText(path, "utf-8");
		/*Pattern pattern = Pattern.compile("[^a-zA-Z\u4e00-\u9fa5\\s/\\[\\]\\d\uFF10-\uFF19��-��-]{1,}[\u4e00-\u9fa5]+");
		Matcher matcher = pattern.matcher(corpus);
		while(matcher.find()) {
			//matcher.group();
			int start = matcher.start();
			int end = matcher.end();
		}*/
		String prefixReg = "([^a-zA-Z\u4e00-\u9fa5\\s/\\[\\]\\d\uFF10-\uFF19��-��-]{1,})([\u4e00-\u9fa5]+)";
		corpus = corpus.replaceAll(prefixReg, "$1 $2");
		String backfixReg = "([\u4e00-\u9fa5]+)([^a-zA-Z\u4e00-\u9fa5\\s/\\[\\]\\d\uFF10-\uFF19��-��-]{1,})";
		corpus = corpus.replaceAll(backfixReg, "$1 $2");
		IOUtils.writeTextData2File(corpus, "D:\\\\nlp����\\\\���Ա�ע\\\\�����ձ����Ͽ�2014\\\\2014�����ձ��з�����6_utf8_�����ĺͷ��ŷֿ�.txt", "utf-8");
	}
	
	
	@Test
	public void t8() {
		String basePath = "D:\\nlp����\\���Ա�ע\\";
		String d1998 = basePath+"dictionary1998.txt";
		String d2014 = basePath+"dictionary2014.txt";
		List<String> dic1998 = IOUtils.readTextAndReturnLines(d1998, "utf-8");
		List<String> dic2014 = IOUtils.readTextAndReturnLines(d2014, "utf-8");
		int count = 0;
		List<String> unconatain = new ArrayList<>();
		for(int i = 0; i < dic1998.size();i++) {
			if(!dic2014.contains(dic1998.get(i))){
				unconatain.add(dic1998.get(i));
				count++;
			}
		}
		System.out.println("2014�ʵ��в�����1998�еĴ��У�"+count+"��");
		dic2014.addAll(unconatain);
		//ȥ��Ӣ��
		List<String> newdic2014 = new ArrayList<>();
		for(int i = 0; i < dic2014.size();i++) {
			if(!dic2014.get(i).matches("[a-zA-Z]+")) {
				newdic2014.add(dic2014.get(i));
			}
		}
		
		ChineseDictionarySortUtil.sortEncodingUtf8(newdic2014);
		StringBuilder sb = new StringBuilder();
		newdic2014.forEach((e)->{
			sb.append(e+"\n");
		});
		IOUtils.writeTextData2File(sb.toString(), "D:\\nlp����\\���Ա�ע\\dictionary2014&1998.txt", "utf-8");
	}
}
