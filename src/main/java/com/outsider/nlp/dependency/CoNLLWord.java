package com.outsider.nlp.dependency;

public class CoNLLWord {
	private int ID;//��ǰ���ھ����е���ţ�����ʼ.
	private String LEMMA;//��ǰ������㣩��ԭ�ͻ�ʸɣ��������У�������FORM��ͬ
	private String CPOSTAG;//�����ȴ���
	private String POSTAG;//ϸ���ȴ���
	private int HEAD;//��ǰ��������Ĵ�,�������Ϊ2�Ĵʵ����Ĵ���4����ô2->4����һ�������
	private String DEPREL;//��ǰ���������Ĵʵ������ϵ
	
	//���ڵ�
	public static final CoNLLWord ROOT = new CoNLLWord(0, "ROOT_L","ROOT_CP" ,"ROOT_P", -1,"");
	// out of index POSTAG Խ���ϸ���ȴ���
	public static final String OOIPOSTAG = "PNULL";
	// out of index CPOSTAG Խ��Ĵ����ȴ���
	public static final String OOICPOSTAG = "CNULL";
	// null dependency relation label �������ϵ��ǩ
	public static final String NoneDEPREL = "DNULL";
	// the LEMMA of ROOT ���ڵ��LEMMA�ֶ�
	public static final String ROOT_LEMMA = "ROOT_L";
	// the CPOSTAG of ROOT ���ڵ�Ĵ����ȴ���
	public static final String ROOT_CPOSTAG = "ROOT_CP";
	//
	public static final String OOILEMMA = "LNULL";
	
	public CoNLLWord() {
	}
	
	public CoNLLWord(int iD, String lEMMA, String pOSTAG, int hEAD, String dEPREL) {
		super();
		ID = iD;
		LEMMA = lEMMA;
		POSTAG = pOSTAG;
		CPOSTAG = pOSTAG.substring(0, 1).toLowerCase();//�����ȴ���ȡϸ���ȴ��Եĵ�һ���ַ�
		HEAD = hEAD;
		DEPREL = dEPREL;
	}
	
	public CoNLLWord(int iD, String lEMMA, String cPOSTAG, String pOSTAG, int hEAD, String dEPREL) {
		super();
		ID = iD;
		LEMMA = lEMMA;
		CPOSTAG = cPOSTAG;
		POSTAG = pOSTAG;
		HEAD = hEAD;
		DEPREL = dEPREL;
	}

	public CoNLLWord(int iD, String lEMMA, String pOSTAG) {
		super();
		ID = iD;
		LEMMA = lEMMA;
		POSTAG = pOSTAG;
		CPOSTAG = pOSTAG.substring(0, 1).toLowerCase();//�����ȴ���ȡϸ���ȴ��Եĵ�һ���ַ�
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getLEMMA() {
		return LEMMA;
	}

	public void setLEMMA(String lEMMA) {
		LEMMA = lEMMA;
	}

	public String getCPOSTAG() {
		return CPOSTAG;
	}

	public void setCPOSTAG(String cPOSTAG) {
		CPOSTAG = cPOSTAG;
	}

	public String getPOSTAG() {
		return POSTAG;
	}

	public void setPOSTAG(String pOSTAG) {
		POSTAG = pOSTAG;
		CPOSTAG = pOSTAG.substring(0, 1).toLowerCase();
	}

	public int getHEAD() {
		return HEAD;
	}

	public void setHEAD(int hEAD) {
		HEAD = hEAD;
	}

	public String getDEPREL() {
		return DEPREL;
	}

	public void setDEPREL(String dEPREL) {
		DEPREL = dEPREL;
	}
	
}
