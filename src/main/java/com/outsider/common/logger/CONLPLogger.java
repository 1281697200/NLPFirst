package com.outsider.common.logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CONLPLogger{
	/**
	 * CONLPȫ����־��
	 */
	public static Logger logger = Logger.getLogger("CONLP");
	/**
	 * �ر�ȫ����־��
	 */
	public static void closeGlobalLogger() {
		logger.setLevel(Level.OFF);
	}
	/**
	 * ��ȫ����־��
	 */
	public static void openGlobalAllLevelLogger() {
		logger.setLevel(Level.ALL);
	}
	/**
	 * �ر�һ��ָ������־��
	 * @param logger
	 */
	public static void closeLogger(Logger logger) {
		logger.setLevel(Level.OFF);
	}
	/**
	 * ��һ��ָ������־��
	 * @param logger
	 */
	public static void openLogger(Logger logger) {
		logger.setLevel(Level.ALL);
	}
	/**
	 * ��ȡһ����ר���Ĵ�ӡ��
	 * ע�⴫��class��ͬ����ȡ��ͬһ��logger
	 * ʹ��Ĭ�ϵĸ�ʽ��:CONLPLogFormatter
	 * @param clazz
	 * @return
	 */
	public static Logger getLoggerOfAClass(Class clazz) {
		Handler handler = new ConsoleHandler();
		Logger logger =Logger.getLogger(clazz.getName());
		handler.setFormatter(new CONLPLogFormatter());
		logger.setUseParentHandlers(false);
		logger.addHandler(handler);
		return logger;
	}
	/**
	 * ��ȡһ�����ר����־��
	 * ע�⴫��class��ͬ����ȡ��ͬһ��logger
	 * ָ����ʽ������
	 * @param clazz
	 * @param formatter ��ʽ������ 
	 * @return
	 */
	public static Logger getLoggerOfAClass(Class clazz, Formatter formatter) {
		Handler handler = new ConsoleHandler();
		Logger logger =Logger.getLogger(clazz.getName());
		handler.setFormatter(formatter);
		logger.setUseParentHandlers(false);
		logger.addHandler(handler);
		return logger;
	}
	
}
