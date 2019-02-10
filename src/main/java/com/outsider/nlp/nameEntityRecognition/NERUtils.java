package com.outsider.nlp.nameEntityRecognition;

import java.util.ArrayList;
import java.util.List;

import com.outsider.common.util.IOUtils;
import com.outsider.common.util.StringUtils;
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
	
	public static List<Entity> getSpecificEntityType(List<Entity> ens, char type){
		List<Entity> res = new ArrayList<>();
		for(Entity entity : ens) {
			if(entity.getEntityType() == type) {
				res.add(entity);
			}
		}
		return res;
	}
	
	/**
	 * ���ı��ж�ȡʵ�壬
	 * ��ʽ�������㣺
	 * ���磺
	 * �й���һ�����ƹ��ҡ�
	 * �й�/ns ��һ�����ƹ��ҡ�/o
	 * @param datas ����ָ�õ��ı����ݡ�����������Ӿ��� [�й�/ns][��һ�����ƹ��ҡ�/o]���ɵ�����
	 * @return
	 */
	public static List<Entity> extractEntityFromText(String[] datas){
		List<Entity> ens = new ArrayList<>();
		// nr���� ns���� nt������
		int start = 0,end = 0;
		for(String ent : datas) {
			ent = ent.trim();
			int i = ent.lastIndexOf('/');
			if(i == -1)
				continue;
			String entityC = ent.substring(0, i);
			end += entityC.length();
			start = end - entityC.length();
			String type = ent.substring(i + 1).toLowerCase();
			if(type.equals("o")) {
				continue;
			}
			Entity entity = new Entity();
			entity.setEntity(entityC);
			entity.setStart(start);
			entity.setEnd(end-1);
			if(type.equals("nr")) {
				entity.setEntityType(EntityType.PERSON_NAME);
			} else if (type.equals("ns")) {
				entity.setEntityType(EntityType.LOCATION);
			} else if(type.equals("nt")) {
				entity.setEntityType(EntityType.ORGANIZATION);
			} else {
				System.out.println("error...");
			}
			ens.add(entity);
		}
		return ens;
	}
	
	public static void main(String[] args) {
		// nr���� ns���� nt������
		String path = "./data/ner/testright.txt";
		String[] datas = IOUtils.loadSegmentionCorpus(path, "utf-8", " ");
		List<Entity> ens = extractEntityFromText(datas);
		int count = 0;
		for(Entity entity : ens) {
			System.out.println(entity.getEntity()+"("+entity.getStart()+","+entity.getEnd()+")");
			count++;
			if(count > 100) break;
		}
	}
}
