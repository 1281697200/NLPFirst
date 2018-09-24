package com.outsider.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {
	/**
	 * ��ȡ�ı��ļ������������ַ��������������з���
	 * @param path �ļ�·��
	 * @param encoding ���룬����null���߿մ�ʹ��Ĭ�ϱ���
	 * @return
	 */
	public static String readText(String path, String encoding) {
		try {
			BufferedReader reader = null;
			if((!encoding.trim().equals(""))&&encoding!=null) {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(path),encoding));
			} else {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			}
			String s="";
			StringBuilder sb  = new StringBuilder();
			while((s=reader.readLine())!=null) {
				sb.append(s);
			}
			reader.close();
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ��ȡ�ı��ļ������������ַ��������������з���
	 * @param path �ļ�·��
	 * @param encoding ���룬����null���߿մ�ʹ��Ĭ�ϱ���
	 * @param addNewLine �Ƿ�ӻ��з�
	 * @return
	 */
	public static List<String> readTextAndReturnLinesCheckLineBreak(String path, String encoding, boolean addNewLine) {
		try {
			String lineBreak;
			if(addNewLine) {
				lineBreak = "\n";
			} else {
				lineBreak = "";
			}
			BufferedReader reader = null;
			if((!encoding.trim().equals(""))&&encoding!=null) {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(path),encoding));
			} else {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			}
			String s="";
			List<String> list = new ArrayList<>();
			while((s=reader.readLine())!=null) {
				list.add(s+lineBreak);
			}
			reader.close();
			return list;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<String> readTextAndReturnLines(String path, String encoding){
		return readTextAndReturnLinesCheckLineBreak(path, encoding, false);
	}
	
	/**
	 * д���ı��ļ�
	 * @param data
	 * @param path
	 * @param encoding
	 */
	public static void writeTextData2File(String data,String path,String encoding) {
		try {
			BufferedWriter writer = null;
			if((!encoding.trim().equals(""))&&encoding!=null) {
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path),encoding));
			} else {
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)));
			}
			writer.write(data);
			writer.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ���ض������
	 * @param paths
	 * @param encodings
	 * @param splits
	 * @return
	 */
	public static String[] loadMultiSegmentionCorpus(String[] paths, String[] encodings ,String[] splits) {
		String[] datas = new String[paths.length];
		for(int i = 0; i < datas.length; i++) {
			datas[i] = IOUtils.readText(paths[i],encodings[i]);
		}
		String[] last = new String[0] ;
		for(int i = 0; i < datas.length; i++) {
			String[] r = datas[i].split(splits[i]);
			last = StringUtils.concat(last, r);
		}
		return last;
	}
	/**
	 * ���ص�������
	 * @param path
	 * @param encoding
	 * @param split
	 * @return
	 */
	public static String[] loadSegmentionCorpus(String path, String encoding ,String split) {
		
		return loadMultiSegmentionCorpus(new String[] {path}, new String[] {encoding}, new String[]{split});
	}
	
	/**
	 * �Ѷ���д���ļ�
	 * @param path
	 * @param object
	 */
	public static void writeObject2File(String path, Object object) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
			out.writeObject(object);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	/**
	 * ��ȡ����
	 * @param path
	 * @return
	 */
	public static Object readObject(String path) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
			return in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; 
	}
	
}
