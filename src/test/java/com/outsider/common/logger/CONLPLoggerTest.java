package com.outsider.common.logger;

import java.util.logging.Logger;

public class CONLPLoggerTest {
	public static void main(String[] args) {
		//��ȫ����־�����༶�����־��
		Logger logger = CONLPLogger.getLoggerOfAClass(CONLPLogger.class);
		logger.info("test Logger!");
		System.out.println();
		//ȫ����־��
		Logger globalLogger = CONLPLogger.logger;
		globalLogger.info("test gloabl logger!");
	}
}
