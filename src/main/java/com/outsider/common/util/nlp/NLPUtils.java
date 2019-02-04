package com.outsider.common.util.nlp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.outsider.common.algorithm.dat.darts.DoubleArrayTrie;
import com.outsider.common.util.IOUtils;
import com.outsider.common.util.StringUtils;
/**
 * һЩ������NLP��ز����Ĺ�����
 * @author outsider
 *
 */
public class NLPUtils {
	
	/**
	 * ������ת��Ϊ����id
	 * @param words ��������
	 * @param dictionary �ֵ���
	 * @return ����id����
	 */
	public static int[] words2intId(String[] words, DoubleArrayTrie dictionary) {
		int[] intId = new int[words.length];
		for(int i = 0; i < intId.length; i++) {
			intId[i] = dictionary.intIdOf(words[i]);
		}
		return intId;
	}
	
	/**
	 * ���ַ��������ÿһ���ַ����е��ַ�ֱ��ת��ΪUnicode��
	 * @param strs �ַ�������
	 * @return Unicodeֵ
	 */
	public static List<int[]> strs2int(String[] strs) {
		List<int[]> res = new ArrayList<>(strs.length);
		for(int i = 0; i < strs.length;i++) {
			int[] O = new int[strs[i].length()];
			for(int j = 0; j < strs[i].length();j++) {
				O[j] = strs[i].charAt(j);
			}
			res.add(O);
		}
		return res;
	}
	
	
	/**
	 * �����·ָ�Ϊ����
	 * �Ժ��ٿ��Ƕ��̷ִ߳�
	 * @param article
	 * @return
	 */
	public static String[] cutSentences(String article) {
		//������.?!
		
		return null;
	}
	
	
	/**
	 * ���������ձ��������͵������е�ʵ��ת��Ϊ��ǩ��
	 * ʵ�����:��������������֯����
	 * �����ձ������е��������պ����Ƿֿ��ġ�
	 * nt�ǻ�������ns�ǵ�����nr������
	 * O,P_B,P_M,P_E,L_B,L_M,L_E,O_B,O_M,O_E
	 * @param text
	 * @return 
	 * O,P_B,P_M,P_E,L_B,L_M,L_E,O_B,O_M,O_E
	 * @deprecated û��Ū�̫꣬�鷳�ˣ�ʹ��΢�������о�Ժ������
	 */
	public static String[][] parseNERcorpus(String text) {
		Pattern p1 = Pattern.compile("\\[[^\\[\\]]+\\]\\w{1,5}\\s");
		char[] chs = text.toCharArray();
		Matcher m1 = p1.matcher(text);
		StringBuilder sb = new StringBuilder();
		int b = 0;
		int e = 0;
		while( m1.find()) {
			String s = m1.group();
			e = m1.start();
			//e-n�ǲ�����[]�Ĳ���
			//��������[]�Ĳ���
			String tmp1 = text.substring(b,e).trim();
			String[] tmp1s = tmp1.split("  ");
			
			for(int i = 0; i < tmp1s.length; i++) {
				tmp1s[i] = tmp1s[i].trim();
				if(tmp1s[i].equals("")) continue;
				String[] xy = tmp1s[i].split("/");
				if(xy.length != 2) {
					System.out.println(tmp1s[i]);
				}
				char[] xs = xy[0].toCharArray();
				if(xy[1].equals("nt")) {
					sb.append(xs[0]+"\tO_B\n");
					for(int k = 1; k < xs.length - 1; k++) {
						sb.append(xs[k]+"\tO_M\n");
					}
					sb.append(xs[xs.length - 1]+"\tO_E\n");
				} else if(xy[1].equals("ns")) {
					sb.append(xs[0]+"\tL_B\n");
					for(int k = 1; k < xs.length - 1; k++) {
						sb.append(xs[k]+"\tL_M\n");
					}
					sb.append(xs[xs.length - 1]+"\tL_E\n");
				} else if(xy[1].equals("nr")) {
					//�ϲ�����
					int nrb = i;
					while(i < tmp1s.length && tmp1s[i].split("/")[1].equals("nr")) i++;
					String name = "";
					for(int j = nrb; j < i; j++) {
						name += tmp1s[j].split("/")[0];
					}
					char[] nrc = name.toCharArray();
					sb.append(nrc[0]+"\tP_B\n");
					for(int k = 1; k < nrc.length - 1; k++) {
						sb.append(nrc[k]+"\t+P_M\n");
					}
					sb.append(nrc[nrc.length - 1]+"\tP_E\n");
					System.out.println("name:"+name);
					i--;
				} else {
					for(char c : xs) {
						sb.append(c+"\t"+"O\n");
					}
				}
			}
			
			b = m1.end();
			//�������[]�Ĳ���
			for(int i = e; i <= b; i++) {
			}
		}
		return null;
	}

	
	
	/**
	 * ת��΢�������о�Ժ������ʵ��ʶ������ΪCRF��ʽ
	 * nt�ǻ�������ns�ǵ�����nr������
	 * O,P_B,P_M,P_E,L_B,L_M,L_E,O_B,O_M,O_E
	 * W_P,W_L,W_O ����ʵ��
	 * W����ʵ�壬��������ʲô����ʵ��
	 * @return ��ʽʾ����
	 * 	��	P_B
	 * 	��	P_E
	 */
	public static String parseNERCorpusOfMASR2crformat(String text) {
		text = StringUtils.fullWidthChar2HalfWidthChar(text.trim());
		String[] xys = text.split(" ");
		StringBuilder sb = new StringBuilder();
		for(String xy : xys) {
			int inde = xy.lastIndexOf("/");
			String[] xySplit = new String[2];
			xySplit[0] = xy.substring(0,inde);
			xySplit[1] = xy.substring(inde+1);
			char[] xchs = xySplit[0].toCharArray();
			int len = xchs.length;
			String[] labels = new String[3];
			boolean entity = true;
			if(xySplit[1].equals("o")) {
				for(char c : xchs) {
					sb.append(c+"\tO\n");
				}
				entity = false;
			} else if(xySplit[0].length() == 1) {
				sb.append(xchs[0]+"\tW\n");
				entity = false;
			} else if(xySplit[1].equals("nt")) {
				labels[0] = "B_O";
				labels[1] = "M_O";
				labels[2] = "E_O";
			} else if(xySplit[1].equals("ns")) {
				labels[0] = "B_L";
				labels[1] = "M_L";
				labels[2] = "E_L";
			}else if(xySplit[1].equals("nr")) {
				labels[0] = "B_P";
				labels[1] = "M_P";
				labels[2] = "E_P";
			} else {
				System.out.println("error...:");
			}
			if(entity) {
				sb.append(xchs[0]+"\t"+labels[0]+"\n");
				for(int k = 1; k < len - 1; k++) {
					sb.append(xchs[k]+"\t"+labels[1]+"\n");
				}
				sb.append(xchs[len-1]+"\t"+labels[2]+"\n");
			}
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		//String s = fullWidthChar2HalfWidthChar("19980101-01-001-016/m  лл/v  ��/w  ��/w  �»���/nt  ����/ns  ������/t  ������/t  ��/n  ��/w  ");
		//19980101-01-001-016/m  лл/v  ��/w  ��/w  �»���/nt  ����/ns  ������/t  ������/t  ��/n  ��/w  
		//System.out.println(s);
		String path = "D:\\nlp����\\����ʵ��ʶ��\\MSRA\\train1.txt";
		String text = IOUtils.readText(path, "utf-8");
		String res = parseNERCorpusOfMASR2crformat(text);
		IOUtils.writeTextData2File(res, "D:\\\\nlp����\\\\����ʵ��ʶ��\\\\MSRA\\\\train1_crf.txt", "utf-8");
	}
}
