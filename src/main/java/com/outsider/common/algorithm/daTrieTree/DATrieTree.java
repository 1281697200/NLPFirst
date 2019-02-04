package com.outsider.common.algorithm.daTrieTree;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.outsider.common.util.IOUtils;
/**
 * ʵ��˵��Double Array Trie Tree��
 * ��base[n] = -2��ʾҶ�ӽڵ�
 * ��base[0] = 1��ʾ���ڵ�
 * �ݹ鹹�����������
 * ����ֱ��ʹ���ַ���Unicodeֵ��û�н��ϡ������
 * �����ٶȽ���
 * û�ж���ǰ׺�Ĵ���tail����������һ��node�ڵ�
 * ֻ�ʺϹ�����̬��
 * @author outsider
 *
 */
public abstract class DATrieTree{
	protected int size = 655350;
	protected int[] check;
	protected int[] base;
	protected int arrMaxLen; //base����ж��
	protected int maxDepth;
	protected int maxCode;
	protected int minCode;
	protected int tokenSize;
	protected int rootBaseValue = 1;
	public DATrieTree() {
	}
	public DATrieTree(List<String> tokens) {
		build(tokens);
	}
	private void init() {
		size = (int) (arrMaxLen / 0.01);
		base = new int[size];
		base[0] = rootBaseValue;//���ڵ�
		check = new int[size];
	}
	public void build(List<String> tokens) {
		//���Ϊ1ʱ�����в��ظ��ڵ㣬Ҳ���Ǹ��ڵ�����Ľڵ�,�ʵĵ�һ���ַ�
		Set<Character> parentChars = new HashSet<>();
		tokenSize = tokens.size();
		for(int i = 0; i < tokenSize; i++) {
			parentChars.add(tokens.get(i).charAt(0));
			int len = tokens.get(i).length();
			arrMaxLen += len;
			maxDepth  = len > maxDepth ? len : maxDepth;
			String s = tokens.get(i);
			for(int j = 0; j < len; j++) {
				int u = code(s.charAt(j));
				maxCode = u > maxCode ? u : maxCode;
				minCode = u < minCode ? u: minCode;
			}
		}
		//���ݿռ�ʹ��������ʼ����С
		init();
		//�����ҵ�ԭ���ˣ�
		//��Ϊ��һ�㸸�ڵ��base����ֵ��ȷ���ģ����������Ϊռ�ã�
		//��ô���������ɺ��ӽڵ�ʱ��ռ�ã���͵���������
		////����Ը��ڵ�baseռλ
		for(char c : parentChars ) {
			base[base[0]+code(c)] = -2;
		}
		//�ݹ鹹��
		for(char c : parentChars ) {
			walk(tokens, 1, c, 0);
		}
	}
	/**
	 * 
	 * @param tokens
	 * @param depth ָ���ڵ����ڵ��������
	 * @param parentChar
	 * @param lastTransferBaseIndex ת�Ƶ����ڵ��ת�ƻ�����index
	 */
	public void walk(List<String> tokens, int depth, char parentChar, int lastTransferBaseIndex) {
		Set<Character> childChars = new TreeSet<Character>();
		//Ѱ�ҵ�ǰ�ַ��������ӽڵ�
		for(int i = 0; i < tokenSize; i++) {
			//�ʵĳ��ȱ������depth������ǰ׺��parentChar
			if(tokens.get(i).length() > depth && tokens.get(i).charAt(depth-1) == parentChar) {
				childChars.add(tokens.get(i).charAt(depth));
			}
		}
		int a = base[lastTransferBaseIndex];
		//�Ѿ���Ҷ�ӽڵ�����,�Ѿ���insert�����ֱ���ֵΪ-2
		//�����Ƿ�Ҷ�ӽڵ�����
		if(childChars.size() > 0) {
			insert(childChars, a + code(parentChar));
			depth++;
			for(char c : childChars) {
				walk(tokens, depth, c, a + code(parentChar));
			}
		}
	}
	
	/**
	 * ���ӽڵ���뵽���У���Ҫ������ȷ�����ڵ��baseֵ�Ƕ��ٺ���
	 * @param childs �ӽڵ�
	 * @param parentCharBaseIndex ���ڵ���base�����е�����
	 */
	private void insert(Set<Character> childs, int parentCharBaseIndex) {
		//��Ѱһ�����ʵ�baseֵʹ�����е��ӽڵ�δ��ռ��
		//��i=1��ʼѰ��ʱ����ڸ��ǵ����⣬��������
		boolean failed = false;
		for(int i = -minCode+1; i <= base.length-1- maxCode; i++) {
			failed = false;
			for(char c : childs) {
				//��bug����������ֻ�Ǽ����base[i + c]�Ƿ�ռ�ã����ǲ�û�����ռ�ñ�־�����ᵼ����һ����
				//����һ��ͬ�����ַ�������Ϊbae[i+c]û�б�ռ�ã���Ϊ�����ǵ���0
				if(base[i + code(c)] != 0) {
					failed = true;
					break;
				}
				
			}
			//�ɹ��ҵ�
			if(!failed) {
				base[parentCharBaseIndex] = i;
				Iterator<Character> its = childs.iterator();
				while(its.hasNext()) {
					//һ��Ҫ����ռ����Ϊ���ӽڵ��base�����Ѿ�ȷ����
					//�����ռ�ý����ֱ������ڵ�ռ�õ����
					char c = its.next();
					base[i + code(c)] = -2;
					check[i + code(c)] = parentCharBaseIndex;
				}
				return;
			}
		}
		System.out.println("baseֵȷ��ʧ�ܣ�");
		//�ռ䲻�����¼Ӵ�ռ�
		//resize(11);
		//insert(childs, parentCharBaseIndex);
	}
	
	/**
	 * ���Ѿ������õ�trie�����в���
	 * @param token
	 */
	public void insert(String token) {
		
	}
	/**
	 * ��������ʹ��
	 */
	/*public void print() {
		for(int i = 0;i < base.length; i++) {
			if(base[i] != 0) {
				System.out.println("base["+i+"]=:"+base[i]+",check["+i+"]="+check[i]);
			}
		}
	}
	*/
	public List<String> match(String tokenPrefix) {
		return null;
	}
	//v0:�����ֵı�����С��1~20902
	//v1:ȡ�����ַ�ʽ����Ϊò���е��ֵ�����Ӣ����ĸ
	public int code(char c) {
		return c/* - 19967*/;
	}
	
	/**
	 * �ع������С
	 */
	public void resize(int size) {
		
	}
	
	
	public boolean exist(String token) {
		int last = 0;
		for(int i = 0; i < token.length(); i++) {
			int a = base[last] + code(token.charAt(i));
			if(/* base[a] == 0 || */check[a] != last) {
				return false;
			}
			//�޷��ж��Ƿ��գ���Ϊ�����еĴ�ֱ����ĳЩ�ʵ�ǰ׺
			//�ʵ����һ���ַ��Ƿ���
			/*if(i == token.length() -1 && base[a]!=-2) {
				return false;
			}*/
			last = a;
		}
		return true;
	}
	
	public String idOf(String token) {
		int last = 0;
		StringBuilder sb  = new StringBuilder();
		for(int i = 0; i < token.length(); i++) {
			int a = base[last] + code(token.charAt(i));
			if(check[a] != last) {
				return null;
			}
			//ʹ������a������id
			sb.append(a+"");
			last = a;
		}
		return sb.toString();
	}
	
	public float spaceUsingRate() {
		int j = base.length;
		for(int i = base.length-1; i >= 0;i--) {
			if(base[i]!=0) {
				break;
			} else {
				j = i;
			}
		}
		System.out.println(j);
		int count = 0;
		for(int k = 0; k < j;k++) {
			if(base[k] != 0) {
				count++;
			}
		}
		return  (float) (1.0*count/(j+1));
	}
	
	static int count = 0;
	public static void main(String[] args) {
		//����
		DATrieTree da = new DATrieTree() {
		};
		List<String> words = IOUtils.readTextAndReturnLines("D:\\nlp����\\���Ĵʿ�\\data\\��ʮ�����ʿ�.txt", "utf-8");
		long start = System.currentTimeMillis();
		System.out.println("������:"+words.size());
		words = words.subList(0, 7000);
		da.build(words);
		long end = System.currentTimeMillis();
		System.out.println("������ʱ:"+(end - start) / 1000.0 + "�룡");
		words.forEach((e)->{
			if(da.exist(e)) {
				count++;
			} else {
				System.out.println(e);
			}
		});
		if(count!=words.size()) {
			System.out.println("����ѵ�����ڴʵ����޷��ҵ���");
			System.out.println("���ҵ�:"+count+";�ܹ�:"+words.size());
		}
		System.out.println("idOf:"+da.idOf("����"));
		count = 0;
		//����id�Ƿ���ظ�
		Set<String> set = new HashSet<>();
		words.forEach((e)->{
			set.add(da.idOf(e));
		});
		System.out.println("�ռ�ʹ����:"+da.spaceUsingRate());
		System.out.println(set.size());
		System.out.println(words.size());
		//�ɲ����Գ��Կ���n���߳���������n��ʾ��һ��Ľڵ㣬���Ҫ��Щ
	}
}
