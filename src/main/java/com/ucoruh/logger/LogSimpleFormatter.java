package com.ucoruh.logger;

import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class LogSimpleFormatter extends SimpleFormatter {

	/**
	 * 
	 * The format string used for formatting log records.
	 */
	private static final String format = "[%1$tF %1$tT] [%2$-7s] %4$s - %3$s %n";

	/**
	 * 
	 * Formats a given log record into a formatted string.
	 * 
	 * @param lr the log record to format
	 * @return a formatted string representing the log record
	 */
	@Override
	public synchronized String format(LogRecord lr) {
		String functionName = "";
		if (lr.getSourceClassName() != null) {
			try {
				StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
				if (stackTrace.length > 2) {
					// The top two elements are getStackTrace() and format(),
					// so we start iterating from the third element.
					for (int i = 2; i < stackTrace.length; i++) {
						StackTraceElement element = stackTrace[i];
						if (element.getClassName().equals(lr.getSourceClassName())) {
							functionName = "[" + element.getMethodName() + "] ";
							break;
						}
					}
				}
			} catch (Exception e) {
				// ignore
			}
		}
		return String.format(format, new java.util.Date(lr.getMillis()), lr.getLevel().getLocalizedName(),
				lr.getMessage(), functionName);
	}

}
