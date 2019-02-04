package com.outsider.nlp.dependency;

import java.util.List;

public class CoNLLSentence {
	private CoNLLWord[] words;
	/**
	 * 
	 * @param lines CoNLL��һ�����Ӱ����������С�
	 */
	public CoNLLSentence(List<String> lines) {
		//����CoNLLSentence
		//id ���� ���� ����(��) ����(ϸ) �䷨���� ���Ĵ� ��ǰ�������ĴʵĹ�ϵ
		words = new CoNLLWord[lines.size() + 1];
		words[0] = CoNLLWord.ROOT;
		for(int i = 1; i <= lines.size(); i++) {
			String[] line = lines.get(i-1).split("\t");
			words[i] = new CoNLLWord();
			words[i].setID(Integer.parseInt(line[0])); //�����±��0��ʼ������������1��ƫ��
			words[i].setPOSTAG(line[4]);
			words[i].setLEMMA(line[2]);
			words[i].setDEPREL(line[7]);
			words[i].setHEAD(Integer.parseInt(line[6]));
		}
	}
	
	public CoNLLSentence(CoNLLWord[] words) {
		this.words = words;
	}
	
	public CoNLLWord getWord(int i) {
		return words[i];
	}
	public String LEMMA(int i) {
		if(i < 0 || i >= words.length)
			return CoNLLWord.OOILEMMA;
		return words[i].getLEMMA();
	}
	
	public String POSTAG(int i) {
		if(i < 0 || i >= words.length)
			return CoNLLWord.OOIPOSTAG;
		return words[i].getPOSTAG();
	}
	
	public String CPOSTAG(int i) {
		if(i < 0 || i >= words.length)
			return CoNLLWord.OOICPOSTAG;
		return words[i].getCPOSTAG();
	}
	
	public int length() {
		return words.length;
	}
	/**
	 * ����CoNLL��ʽ����
	 * @return
	 */
	public String[] generateCoNLLLines() {
		//7	����	����	v	vn	_	2	����	
		String[] result = new String[words.length];
		int count = 0;
		for(CoNLLWord word : words) {
			result[count++] = word.getID()+"\t"+word.getLEMMA()+"\t"+word.getLEMMA()+"\t"+word.getCPOSTAG()+"\t"+word.getPOSTAG()+"\t"
					+"_\t"+word.getHEAD()+"\t"+word.getDEPREL();
		}
		return result;
	}
	
	@Override
	public String toString() {
		String[] lines = generateCoNLLLines();
		StringBuilder sb = new StringBuilder();
		for(String line : lines) {
			sb.append(line+"\n");
		}
		return sb.toString();
	}
}
