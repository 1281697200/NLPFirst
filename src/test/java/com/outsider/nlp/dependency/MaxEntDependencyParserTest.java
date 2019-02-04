package com.outsider.nlp.dependency;
/**
 * ע�⣺������Ҫ�ô����ľ��Ӳ���
 * ��Ϊѵ�������в�û�д���㣬���������ָ�ܿ���ʱ����߼������Ķ̾���
 * ������ɶ���̾�ֻ��һ�����ĳɷ֣���Ϊ�㷨�д���Ϊֻ����һ�����ĳɷ�
 * ���Կ��ǽ����ӷָ�ɶ���̾��ӣ��ֱ������
 * @author outsider
 *
 */
public class MaxEntDependencyParserTest {
	
	public static void main(String[] args) {
		DependencyParser parser = StaticDependencyParser.getMaxEntDependencyParser();
		CoNLLSentence sentence1 = parser.parse("��ÿ�춼��д����");
		System.out.println(sentence1);
		System.out.println();
		CoNLLSentence sentence2 = parser.parse("ժ�������͸���");
		System.out.println(sentence2);
		System.out.println();
		CoNLLSentence sentence3 = parser.parse("��Ŀǰ��ħ��ĳŮУѧϰ����");
		System.out.println(sentence3);
		System.out.println();
		CoNLLSentence sentence4 = parser.parse("�Ұ���");
		System.out.println(sentence4);
		System.out.println();
		CoNLLSentence sentence5 = parser.parse("������һ����ֿ�İ����������ǰ");
		System.out.println(sentence5);
		System.out.println();
		CoNLLSentence sentence6 = parser.parse("������һ����ֿ�İ����������ǰ����û����ϧ��ֱ��ʧȥ��׷��Ī����");
		System.out.println(sentence6);
		System.out.println();
		CoNLLSentence sentence7 = parser.parse("��û����ϧ");
		System.out.println(sentence7);
		System.out.println();
		CoNLLSentence sentence8 = parser.parse("ֱ��ʧȥ��׷��Ī��");
		System.out.println(sentence8);
		System.out.println();
		CoNLLSentence sentence9 = parser.parse("�������ʲô�ó���");
		System.out.println(sentence9);
		System.out.println();
		CoNLLSentence sentence10 = parser.parse("���г����÷��еĵȼ۽���ԭ�����뵳������͹��һ���������");
		System.out.println(sentence10);
		System.out.println();
		
		
		
		//Ŀǰ���ֵ����⣿���Ϸ��������֣�׼ȷ��Ӧ�ò����ߡ�
		//��hancks���ı������о�Ҫ��һЩ�����ܵ�ԭ��1������ģ�岻һ����2����С������������������⣿
		//�ղ������£�hancks����������ά����28��ά�ȣ�ȥ����ǩ���ҵ���21��
	}
	
	/**

11	��	��	r	r	_	5	ʩ��
2	ÿ��	ÿ��	r	r	_	5	ʩ��
3	��	��	d	d	_	5	�̶�
4	��	��	p	p	_	5	�������
5	д	д	v	v	_	0	���ĳɷ�
6	����	����	n	n	_	5	����


1	ժ��	ժ��	v	v	_	2	�޶�
2	����	����	n	n	_	3	����
3	�͸�	�͸�	v	v	_	0	���ĳɷ�
4	��	��	r	r	_	3	����


1	��	��	r	r	_	7	ʩ��
2	Ŀǰ	Ŀǰ	t	t	_	7	ʱ��
3	��	��	p	p	_	6	�������
4	ħ��	ħ��	n	n	_	6	�޶�
5	ĳ	ĳ	r	r	_	6	�޶�
6	ŮУ	ŮУ	n	n	_	7	ʩ��
7	ѧϰ	ѧϰ	v	v	_	0	���ĳɷ�
8	����	����	n	n	_	7	����


1	���	���	a	a	_	2	��ʽ
2	����	����	v	v	_	0	���ĳɷ�
3	̰��	̰��	v	v	_	7	�޶�
4	��¸	��¸	n	n	_	3	��������
5	��	��	u	u	_	3	��������
6	����	����	n	n	_	7	�޶�
7	����	����	v	v	_	2	����

1	��	��	r	r	_	2	������
2	��	��	v	v	_	0	���ĳɷ�
3	��	��	r	r	_	2	Ŀ��

1	��	��	r	r	_	2	������
2	��	��	v	v	_	0	���ĳɷ�
3	��	��	r	r	_	2	Ŀ��
4	��	��	w	w	_	2	����


1	����	����	d	d	_	2	ʱ��
2	��	��	v	v	_	0	���ĳɷ�
3	һ��	һ��	n	n	_	6	�޶�
4	��ֿ	��ֿ	a	a	_	6	�޶�
5	��	��	u	u	_	2	���ġ�������
6	����	����	n	n	_	7	ʩ��
7	��	��	v	v	_	2	����
8	��	��	p	p	_	10	�������
9	��	��	r	r	_	10	�޶�
10	��ǰ	��ǰ	f	f	_	7	����

	 */
}
