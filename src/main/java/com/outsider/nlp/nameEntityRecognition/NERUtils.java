package com.outsider.nlp.nameEntityRecognition;

import java.util.ArrayList;
import java.util.List;

import com.zhifac.crf4j.Tagger;

public class NERUtils {
	
	/**
	 * ����ʵ���Ǻ����
	 * @param text
	 * @param predict
	 * @return
	 */
	public static List<Entity> decode(String text, String[] predict) {
		char[] x = text.toCharArray();
		if(x.length != predict.length) {
			System.err.println("x�������ǩ���Ȳ�ƥ��!");
		}
		List<Entity> ens = new ArrayList<>();
		for(int i = 0; i < predict.length; i++) {
			Entity entity = new Entity();
			entity.setStart(-1);
			if(predict[i].equals("B_P") || predict[i].equals("M_P")) {
				int b = i;
				while(i < x.length && !predict[i].equals("E_P")) i++;
				if(i == x.length) i--;
				entity.setStart(b);
				entity.setEnd(i);
				entity.setEntityType(EntityType.PERSON_NAME);
				entity.setEntity(new String(x, b , i-b+1));
			} else if(predict[i].equals("B_L") || predict[i].equals("M_L")) {
				int b = i;
				while(i < x.length && !predict[i].equals("E_L")) i++;
				if(i == x.length) i--;
				entity.setStart(b);
				entity.setEnd(i);
				entity.setEntityType(EntityType.LOCATION);
				entity.setEntity(new String(x, b , i-b+1));
			} else if(predict[i].equals("B_O") || predict[i].equals("M_O")) {
				int b = i;
				while(i < x.length && !predict[i].equals("E_O")) i++;
				if(i == x.length) i--;
				entity.setStart(b);
				entity.setEnd(i);
				entity.setEntityType(EntityType.ORGANIZATION);
				entity.setEntity(new String(x, b , i-b+1));
			} else if(predict[i].equals("W")) {
				entity.setStart(i);
				entity.setEnd(i);
				entity.setEntityType(EntityType.SINGLE);
				entity.setEntity(new String(x, i , 1));
			}
			if(entity.getStart() != -1) {
				ens.add(entity);
			}
		}
		return ens;
	}
	
	public static List<Entity> nerTag(Tagger tagger, String text){
		//���֮ǰ�Ĵ�Ԥ������
		tagger.clear();
		//��Ӵ�Ԥ������
		for(int i = 0; i < text.length(); i++) {
			tagger.add(text.charAt(i) + "");
		}
		//Ԥ��
		tagger.parse();
		//ת��Ϊchar���������
		String[] predict = new String[text.length()];
		for(int i = 0; i < predict.length; i++) {
			int yInt = tagger.y(i);
			String yName = tagger.yname(yInt);
			predict[i] = yName;
		}
		return decode(text, predict);
	}
	
	public static List<Entity> getSpecificEntityType(List<Entity> ens, String type){
		List<Entity> res = new ArrayList<>();
		for(Entity entity : ens) {
			if(entity.getEntityType().equals(type)) {
				res.add(entity);
			}
		}
		return res;
	}
	
	
	
}
