package com.outsider.zhifac.crf4j.extension;

import com.zhifac.crf4j.CrfLearn;
/**
 * ��CrfPP������ѵ��ģ�͵�CrfLearn.run��һ���򵥵İ�װ
 * �κ�Crf��ѵ���������˷���
 * ע��ѵ�����ݵĸ�ʽ���밴��Crf�ı�׼��ʽ������ά�����֮ǰ�Ի��з��ָ��֮�����Ʊ��\t�ָ�
 * @author outsider
 * 
 * ѵ�������������ֵ�ĺ���:
 *  iter: number of iterations processed
	terr: error rate with respect to tags. (# of error tags/# of all tag)
	serr: error rate with respect to sentences. (# of error sentences/# of all sentences)
	obj: current object value. When this value converges to a fixed point, CRF++ stops the iteration.
	diff: relative difference from the previous object value.
 *
 *
 *
 *����������
 *N-best outputs��
 *	With the -n option, you can obtain N-best results sorted by the conditional probability of CRF. With n-best output mode, CRF++ first gives one additional line like "# N prob", where N means that rank of the output starting from 0 and prob denotes 
 *	the conditional probability for the output.
 *verbose level��
 *	The -v option sets verbose level. default value is 0. By increasing the level, you can have an extra information from CRF++
	level 1��
		You can also have marginal probabilities for each tag (a kind of confidece measure for each output tag) and a conditional probably for the output (confidence measure for the entire output).
 	level 2��
		You can also have marginal probabilities for all other candidates.
 */
public class CrfppTrainer {
	/**
	 * ��CrfPP������ѵ��ģ�͵�CrfLearn.run��һ���򵥵İ�װ
	 * @param templateFilePath ģ���ļ�·��
	 * @param trainDataFilePath ѵ������·����ע��ѵ�����ݵĸ�ʽ���밴��Crf�ı�׼��ʽ������ά�����֮ǰ�Ի��з��ָ��֮�����Ʊ��\t�ָ�
	 * @param modelFile ģ�ͱ���·��
	 * @param options �����������
	 *  -f, �Cfreq=INTʹ�����Եĳ��ִ���������INT(Ĭ��Ϊ1)��������������1���ⶪ���������������ڴ���ѵ������ʱ���Լ������������ĸ���
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
	 */
	public static void run(String templateFilePath, String trainDataFilePath, String modelFile,
			String[] options) {
		String[] args = new String[] {templateFilePath, trainDataFilePath, modelFile};
		if(options != null && options.length > 0) {
			String[] newArgs = new String[args.length + options.length];
			System.arraycopy(args, 0, newArgs, 0, args.length);
			for(int i = 0; i < options.length; i++) {
				newArgs[i + args.length] = options[i];
			}
			args = newArgs;
		}
		CrfLearn.run(args);
	}
}
