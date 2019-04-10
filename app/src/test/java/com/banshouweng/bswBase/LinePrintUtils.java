package com.banshouweng.bswBase;

/**
 * 日志行号获取
 * 
 * @author leiming
 *
 */
public class LinePrintUtils {
	/**
	 * 行号打印
	 * @param string
	 */
	public static void print(String string) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		// 具体目标元素，需要根据实际情况分析（当前方法中为[1]、外部第一层调用为[2]，以此类推）
		StackTraceElement targetElement = stackTrace[2];
		System.out.println("[(" + targetElement.getFileName() + ":" + targetElement.getLineNumber() + ")#"
				+ targetElement.getMethodName() + " ] " + string);
	}
}