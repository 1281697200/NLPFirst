package com.outsider.zhifac.crf4j.extension;
/*
 * ���������дһЩ��crf4j����չ����װ
 * 
 *1.���в���˵����
 *   	-f, �Cfreq=INTʹ�����Եĳ��ִ���������INT(Ĭ��Ϊ1)��������������1���ⶪ���������������ڴ���ѵ������ʱ���Լ������������ĸ���
	 *	-m, �Cmaxiter=INT����INTΪLBFGS������������ (Ĭ��10k)
	 *	-c, �Ccost=FLOAT      ����FLOATΪ���۲���������������� (Ĭ��1.0)������ģ�͵�һ������Ҫ���������ƹ����
	 *	-e, �Ceta=FLOAT������ֹ��׼FLOAT(Ĭ��0.0001)
	 *	-C, �Cconvert���ı�ģʽתΪ������ģʽ
	 *	-t, �CtextmodelΪ���Խ����ı�ģ���ļ�
	 *	-a, �Calgorithm=(CRF|MIRA)
	 *	ѡ��ѵ���㷨��Ĭ��ΪCRF-L2
	 *	-p, �Cthread=INT�߳���(Ĭ��1)�����ö��CPU����ѵ��ʱ��
	 *	-H, �Cshrinking-size=INT
	 *	����INTΪ�����˵ĵ����������� (Ĭ��20)
	 *	-h, �Chelp��ʾ�������˳�
	 *
   
   2.ģ�ʹ򿪲���˵����
   	N-best outputs��
	 	With the -n option, you can obtain N-best results sorted by the conditional probability of CRF. With n-best output mode, CRF++ first gives one additional line like "# N prob", where N means that rank of the output starting from 0 and prob denotes 
	 	the conditional probability for the output.
 
 	verbose level��
	 	The -v option sets verbose level. default value is 0. By increasing the level, you can have an extra information from CRF++
		level 1��
			You can also have marginal probabilities for each tag (a kind of confidece measure for each output tag) and a conditional probably for the output (confidence measure for the entire output).
	 	level 2��
			You can also have marginal probabilities for all other candidates.
 *  cost-factor��
 *  	With this option, you can change the hyper-parameter for the CRFs.
 *  	With larger C value, CRF tends to overfit to the give training corpus. This parameter trades the balance between overfitting and underfitting. The results will significantly be influenced by this parameter. You can find an optimal value by using held-out data or more general model selection method such as cross validation.
 * 
 * 3.ģ��ѵ�������е�������壺
 *  iter: number of iterations processed
	terr: error rate with respect to tags. (# of error tags/# of all tag)
	serr: error rate with respect to sentences. (# of error sentences/# of all sentences)
	obj: current object value. When this value converges to a fixed point, CRF++ stops the iteration.
	diff: relative difference from the previous object value.
	
  4.ע��TaggerImpl����Щ������û��ʵ�֣�����add(String[] lines); parse(String str);
  
*/
