package com.outsider.nlp.nameEntityRecognition;

import java.util.List;

import com.outsider.common.dataStructure.Table;
import com.outsider.common.util.IOUtils;
import com.outsider.constants.nlp.PathConstans;
import com.outsider.model.data.NERCRFDataConverter;
import com.outsider.model.hmm.SequenceNode;

public class FirstOrderHMMNERTest {
	public static void main(String[] args) {
		//train();
		use();
	}
	
	public static void train() {
		String dataPath = "./data/ner/train_crf.txt";
		String srcData = IOUtils.readTextWithLineCheckBreak(dataPath, "utf-8");
		Table table = Table.generateTable(srcData, "\t");
		NERCRFDataConverter converter = new NERCRFDataConverter(0, 1);
		List<SequenceNode> nodes = converter.convert(table);
		FirstOrderHMMNER ner = new FirstOrderHMMNER();
		ner.train(nodes);
		//ner.save("./model/ner/hmm", null);
	}
	
	public static void use() {
		FirstOrderHMMNER ner = StaticNER.getFirstOrderHMMNER();
		long start = System.currentTimeMillis();
		/*ner.open(PathConstans.FIRST_ORDER_HMM_NER, null);*/
		long end = System.currentTimeMillis();
		System.out.println("done...in "+((end - start) / 1000)+"seconds");
		System.out.println("done...");
		List<Entity> ents = ner.extractEntity("�й�������1949��10��1�գ��������й����׶����������������λ������ġ�");
		for(Entity entity : ents) {
			System.out.println(entity);
		}
		//NERTaggerTest.score(ner);
	}
	
}
