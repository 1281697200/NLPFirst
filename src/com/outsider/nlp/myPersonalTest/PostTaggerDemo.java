package com.outsider.nlp.myPersonalTest;

import org.junit.Test;

public class PostTaggerDemo {
	
	@Test
	public void testHMMTagger() {
		String path = "D:\\nlp����\\���Ա�ע\\train.txt";
		/**
		 * ��������ͳ��:
		 *����33��: ns,nt,nz,ws,a,c,d,mq,e,f,h,i,j,k,m,nhf,n,vd,o,p,q,r,u,nd,v,vl,w,nhs,x,nh,ni,nl,vu
		 *������:119280
		 */
	/*	long start = System.currentTimeMillis();
		String[] corpus = FileUtils.loadTextDataFromFile(path, "utf-8", " ");
		FirstOrderHMMPOSTagger tagger = new FirstOrderHMMPOSTagger(33, 119280);
		tagger.train(corpus);
		long end = System.currentTimeMillis();
		System.out.println("ѵ����ʱ:"+(end-start)/1000+"��");
		String testPath = "D:\\\\nlp����\\\\���Ա�ע\\\\dev.txt";
		String[] testCorpus = LoadCorpus.loadDataFromFile(testPath, "utf-8", " ");
		//���Ա�ע������������հ�ִʵĹ������ں����У����������ֻ��һ�����ԣ����߳���Ƶ����ߵĴ���ԶԶ���ڵڶ�λ�Ĵ��ԡ�
		tagger.output(new String[] {"��","���","��ô��"},true);*/
		//float accuray = tagger.accuray(testCorpus);
		//System.out.println("��������׼ȷ��:"+accuray);//׼ȷ��ֻ��60%����
		//float accuray2 = tagger.accuray(corpus);
		//���ڴ��� �ܲ�����
		//System.out.println("ѵ������׼ȷ��:"+accuray2);
	}
}
