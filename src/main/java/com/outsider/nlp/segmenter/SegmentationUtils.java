package com.outsider.nlp.segmenter;

import java.util.ArrayList;
import java.util.List;

import com.outsider.common.dataStructure.Table;
import com.outsider.common.util.StringUtils;
import com.outsider.model.data.CRFSegmentationTableDataConverter;
import com.outsider.model.data.DataConverter;
import com.outsider.model.hmm.SequenceNode;

public class SegmentationUtils {
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
	
	public static int[] str2int(String str) {
		return strs2int(new String[] {str}).get(0);
	}
	/**
	 * ����Ԥ��������
	 * BEMS 0123
	 * @param predict Ԥ����
	 * @param sentence ����
	 * @return
	 */
	public static String[] decode(int[] predict, String sentence) {
		List<String> res = new ArrayList<>();
		char[] chars = sentence.toCharArray();
		for(int i = 0; i < predict.length;i++) {
			if(predict[i] == 0 || predict[i] == 1) {
				int a = i;
				while(predict[i] != 2) {
					i++;
					if(i == predict.length) {
						break;
					}
				}
				int b = i;
				if(b == predict.length) {
					b--;
				}
				res.add(new String(chars,a,b-a+1));
			} else {
				res.add(new String(chars,i,1));
			}
		}
		String[] s = new String[res.size()];
		return res.toArray(s);
	}
	
	/**
	 * �ϲ���ݷִ�����
	 * @param corpuses
	 * @return
	 */
	public static String[] mergeCorpus(List<String[]> corpuses) {
		String[] s = new String[0];
		for(int i = 0; i < corpuses.size(); i++) {
			String[] co = corpuses.get(i);
			s = StringUtils.concat(s, co);
		}
		return s;
	}
	
	/**
	 * ���ִ�����ץ��Ϊint״̬���飬BMES
	 * @param words ��������
	 * @param charLen �ַ�����
	 * @return
	 */
	public static int[] segmentationCorpus2state(String[] words, int charLen) {
		//otherParameters[0] ���Ȳ���
		int[] state = new int[charLen];
		int count = 0;
		for(int i = 0; i < words.length; i++) {
			/*if(words[i].trim().length()!=words[i].length()) {
				System.out.println("�����ܱ�trim���ַ�/"+words[i]);
			}*/
			words[i] = words[i].trim();
			if(words[i].length() == 1) { //���ֳɴ�
				state[count] = 3;
				count++;
			} else if(words[i].length() == 2) {//˫�ֳɴ�
				state[count] = 0;
				state[count+1] = 2;
				count+=2;
			} else if(words[i].length() >2){//����2���ַ��ɴ�
				int len = words[i].length();
				//֮ǰ����д����bug�����뿼��˳�򣬲��ܽ���λ��
				state[count] = 0;
				for(int j = count+1; j < count+len-1; j++) {
					state[j] = 1;
				}
				state[count+len-1] = 2;
				count+=len;
			}
		}
		return state;
	}
	/**
	 * CRF��ʽ�ķִ�����ת��Ϊ���н��
	 * @param table
	 * @param xColumnIndex
	 * @param yColumnIndex
	 * @return
	 */
	public List<SequenceNode> CRFSegmentationTable2SequenceNodes(Table table, int xColumnIndex, int yColumnIndex){
		DataConverter<Table, List<SequenceNode>> converter = new CRFSegmentationTableDataConverter(xColumnIndex, yColumnIndex);
		List<SequenceNode> nodes = converter.convert(table);
		return nodes;
	}
	
	/**
	 * ����Ԥ��������
	 * BEMS
	 * @param predict Ԥ����
	 * @param sentence ����
	 * @return
	 */
	public static String[] decode(char[] predict, String sentence) {
		List<String> res = new ArrayList<String>();
		char[] chars = sentence.toCharArray();
		for(int i = 0; i < predict.length;i++) {
			if(predict[i] == 'B' || predict[i] == 'M') {
				int a = i;
				while(predict[i] != 'E') {
					i++;
					if(i == predict.length) {
						break;
					}
				}
				int b = i;
				if(b == predict.length) {
					b--;
				}
				res.add(new String(chars,a,b-a+1));
			} else {
				res.add(new String(chars,i,1));
			}
		}
		String[] s = new String[res.size()];
		return res.toArray(s);
	}
	
	
}
