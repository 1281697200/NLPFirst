package com.outsider.model.crf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;

import com.outsider.common.util.Storable;
import com.outsider.common.util.StorageUtils;

/**
 * ����ģ������
 * ע��ֻ����ÿһ��
 * ���磺
 * 					U1:%x[0,0]\n"
				   "U2:%x[-1,0]\n"
				   "U3:%x[-1,0]\n"
				   "U4:%x[0,0]/%x[-1,0]/%x[1,0]
	����ÿһ�б���Ϊһ������ģ��
 * @author outsider
 *
 */
public class FeatureTemplate implements Serializable, Storable{
	//����ǰ׺Uxx
	//private String prefix;
	/**
	 * ����ģ���������ʽ
	 */
	public static final String regx = "%x\\[(-{0,1}\\d),(\\d)\\]";
	/**
	 * ���� ��ƫ�ƺ���λ��
	 * һ��int[] int[0] ������ƫ�ƣ�int[1]������λ��
	 */
	private List<int[]> offsetList;
	/**
	 * ��������ģ��
	 */
	private String template;
	/**
	 * ����ģ��ı��
	 */
	private short templateNumber;
	
	public static final Pattern PATTERN = Pattern.compile(regx);
	public FeatureTemplate(String template) {
		this.template = template ;
	}
	public FeatureTemplate() {
	}
	public List<int[]> getOffsetList() {
		return offsetList;
	}

	public void setOffsetList(List<int[]> offsetList) {
		this.offsetList = offsetList;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
	
	public void setTemplateNumber(short templateNumber) {
		this.templateNumber = templateNumber;
	}
	public short getTemplateNumber() {
		return templateNumber;
	}
	/**
	 * ��������ģ��
	 * @param template
	 * @return
	 */
	public static FeatureTemplate createFeatureTemplate(String template) {
		FeatureTemplate featureTemplate = new FeatureTemplate();
		featureTemplate.offsetList = new ArrayList<>();
		featureTemplate.template = template;
		Matcher matcher = PATTERN.matcher(template);
		int i = 0;
		while(matcher.find()) {
			if(i == 0 ) {
				int start = matcher.start();
				//����ð��
				featureTemplate.templateNumber = Short.parseShort(template.substring(1, start-1));
				i++;
			} 
			int[] t = new int[] {Integer.parseInt(matcher.group(1)),Integer.parseInt(matcher.group(2)) };
			featureTemplate.offsetList.add(t);
		}
		/**
		 * ������Bigram��B��ƥ�䲻��
		 */
		if(featureTemplate.offsetList.size() == 0) {
			return null;
		}
		return featureTemplate;
	}
	
	@Override
	public String toString() {
		return "FeatureTemplate:"+template;
	}
	
	
	
	public static void main(String[] args) {
		String template = "U1:%x[0,0]\n"+
				   "U2:%x[-1,0]\n"+
				   "U3:%x[-1,0]\n"+
				   "U4:%x[0,0]/%x[-1,0]/%x[1,0]";
		System.out.println(Arrays.toString(template.split("\n")));
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
