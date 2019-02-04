package com.outsider.model.crf;

import java.io.Serializable;

import com.outsider.common.util.Storable;
import com.outsider.common.util.StorageUtils;

/**
 * ������������
 * ����ע�⣺����ֻ��Ӧһ����������
 * ���磺U01:%x[0,0]
 * 		�����ǰɨ�������(�ִʾ���): ��    S
 * 		��ô����������������
 * 		if(x=='��'&&y==0) return 1 else 0;
 * 		if(x=='��'&&y==1)....
 * 		if(x=='��'&&y==2)....
 * 		if(x=='��'&&y==3)....
 * ����������������������Ǳ�ǩ��ͬ��
 * ��������������������ձ�ǩ��id˳�򱣴�һ��Ȩ��
 * ��δ洢�洢�����������ҿ����������������HanLP�����ֵ����洢��
 * �����������Ŀ���ֵ���ֻ�ܴ洢�ַ�����������Ҫ����toString����
 * Ϊ��ʹÿ�����������Ĳ������ַ�����ͬ�������ֵ����洢��
 * ����ֱ��ƴ�ӹ۲��intֵ��Ϊ�ַ�����ֵ
 * 
 * err��
 *  U:%x[-2,0]��U:%x[0,0]��2��������������۲�ֵ�Ƿ���ͬ������⡣��
 *  Ϊ�����֣�equals�л���Ҫ�Ƚϸ�����������������һ������ģ�壬Ҳ���� Uxx
 *  
 *  
 *  ����û�и���hashCode��������������ͬ����������ȴ��Ϊ�ǲ�ͬ��
 * @author outsider
 *
 */
public class FeatureFunction implements Storable,Comparable<FeatureFunction>,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//����������Ӧ�ڱ�ǩ��id
	private double[] weight;
	/**
	 * ��Ӧ�۲����е���������id����
	 * ��������Ϊ����U01"%x[0,0]/%x[-1,0]��������ж���۲�
	 */
	private int[] x;
	//�����������ַ�id���������ֵ����д洢
	private String strId;
	//�����������ĸ�����ģ�壬Ҳ����ǰ׺Uxx
	//private String featureTemplatePrefix;
	//����ģ��ı����Ҫ������������ʱ��Ҫ�õ�
	//�������ǰ���featureTemplatePrefix������������ǰ�
	private short featureTemplateNum;
	/*public FeatureFunction(short templateNumber) {
		this.featureTemplateNum = templateNumber;
	}*/
	public short getFeatureTemplateNum() {
		return featureTemplateNum;
	}
	
	public String getStrId() {
		return strId;
	}
	public FeatureFunction(int[] x, String funcId, short templateNumber) {
		this.x = x;
		this.strId = funcId;
		this.featureTemplateNum = templateNumber;
		//�����ַ�id
	}
	public FeatureFunction(int[] x, short templateNumber) {
		this.featureTemplateNum = templateNumber;
		this.strId = generateFeatureFuncStrId(x, templateNumber);
	}
	public FeatureFunction(String funcId, short templateNumber) {
		this.strId = funcId;
		this.featureTemplateNum = templateNumber;
	}
	
	@Override
	public String toString() {
		return strId;
	}
	public double[] getWeight() {
		return weight;
	}
	public int[] getX() {
		return x;
	}
	public void setX(int[] x) {
		this.x = x;
	}
	public void setWeight(double[] weight) {
		this.weight = weight;
	}
	@Override
	public boolean equals(Object obj) {
		FeatureFunction f2 = (FeatureFunction) obj;
		if(this.strId.equals(f2.strId))
			return true;
		return false;
	}

	@Override
	public int compareTo(FeatureFunction o) {
		FeatureFunction f2 = o;
		if(this.strId.equals(f2.strId))
			return 0;
		return -1;
	}
	@Override
	public int hashCode() {
		int hash = 0;
		for(int i = 0; i < x.length;i++) {
			hash += x[i];
		}
		return hash + strId.hashCode();
	}
	
	/**
	 * ��������ģ��ź͹۲�x��������������id
	 * @param x �۲�
	 * @param templateNumber ����ģ��� 
	 * @return str Id
	 */
	public static String generateFeatureFuncStrId(int[] x, short templateNumber) {
		StringBuilder sb = new StringBuilder();
		//��Ҫ����ǰ׺Uxx:�ı�Ų�Ȼ�޷�����
		sb.append(templateNumber+":");
		for(int i = 0;i < x.length;i++) {
			sb.append(x[i]);
		}
		return sb.toString();
	}

	@Override
	public void save(String directory, String fileName) {
		StorageUtils.save(directory, fileName, this);
	}

	@Override
	public void open(String directory, String fileName) {
		StorageUtils.open(directory, fileName, this);
	}
	
}
