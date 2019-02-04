package com.outsider.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.junit.Test;

import com.outsider.common.util.IOUtils;
import com.outsider.model.crf.CRF;

public class SomeTest {

	@Test
	public void test() {
		String path = "D:\\nlp����\\���Ա�ע\\���Ա�ע_crf.txt";
		String[] lines = IOUtils.readTextAndReturnLinesOfArray(path, "utf-8");
		Set<String> coarseNature =  new HashSet<>();
		StringBuilder sb = new StringBuilder();
		for(String line : lines) {
			String[] s = line.split("\t");
			String nat = s[1].substring(0, 1).toLowerCase();
			sb.append(s[0]+"\t"+nat+"\n");
		}
		IOUtils.writeTextData2File(sb.toString(), "D:\\\\nlp����\\\\���Ա�ע\\\\���Ա�ע_crf_coarseNature.txt", "utf-8");
	}
	
	@Test
	public void test2() {
		String dir = "C:\\Users\\outsider\\AppData\\Roaming\\nltk_data\\corpora\\ctb8add";
		File fdir = new File(dir);
		File[] files = fdir.listFiles();
		for(File file : files) {
			BufferedReader reader = null;
			StringBuilder sb = new StringBuilder();
			String line = "";
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
				while((line = reader.readLine()) != null) {
					line = new String(line.getBytes("gbk"),"gbk");
					sb.append(line+"\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				while(true) {
					if(line == null) {
						file.delete();
						break;
					}
				}
				
			}
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "gbk"));
				writer.write(sb.toString());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//break;
		}
	}
	
	
	@Test
	public void test3() {
		String dir = "C:\\Users\\outsider\\AppData\\Roaming\\nltk_data\\corpora\\ctb8add";
		File fdir = new File(dir);
		File[] files = fdir.listFiles();
		for(File file : files) {
			BufferedReader reader = null;
			StringBuilder sb = new StringBuilder();
			String line = "";
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				while((line = reader.readLine()) != null) {
					sb.append(line+"\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				while(true) {
					if(line == null) {
						file.delete();
						break;
					}
				}
				
			}
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
				String s = sb.toString().replaceAll("</su>", "");
				s = s.replaceAll("<su\\s{1,3}id=.{1,50}>", "");
				
				if(s.contains("su")) {
					System.out.println(file.getName());
				}
				writer.write(s);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {//5188
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	@Test
	public void test4() {
		List<String> lines = IOUtils.readTextAndReturnLines("D:\\nlp����\\ctb8.0\\parse_result\\pos\\train.tsv", "utf-8");
		Set<String> natures = new TreeSet<>();
		for(String line : lines) {
			if(line.trim().equals(""))	continue;
			String[] s = line.split("\t");
			natures.add(s[1]);
		}
		System.out.println("����:"+natures.size());
		for(String na : natures) {
			System.out.println(na);
		}
	}
	
	
	@Test
	public void test5() {
		String s = "���i-2 adi-2 ����i-1 vi-1 ̰��i0 vi0 ��¸i1 ni1 ��i2 udengi2 ##�հ�##j-2 nullj-2 ##�հ�##j-1 nullj-1 ##����##j0 rootj0 ���j1 adj1 ����j2 vj2 ̰�ۡ�##����## v��root ̰�ۡ�##����##3 v��root3 ����@̰�ۡ�##����## ̰�ۡ�##�հ�##@##����## v@v��root v��null@root null";
		String s2 = "##�հ�##i-2 nulli-2 ##�հ�##i-1 nulli-1 ##����##i0 rooti0 ���i1 adi1 ����i2 vi2 ##�հ�##j-2 nullj-2 ##����##j-1 rootj-1 ���j0 adj0 ����j1 vj1 ̰��j2 vj2 ##����##����� root��ad ##����##�����-1 root��ad-1 ##�հ�##@##����##����� ##����##��##����##@��� null@root��ad root��root@ad null";
		System.out.println(s.split(" ").length);
		System.out.println(s2.split(" ").length);
	}
	
	@Test
	public void t44() {
		System.out.println(Math.log(Math.exp(CRF.INFINITY)));
	}
	
}


