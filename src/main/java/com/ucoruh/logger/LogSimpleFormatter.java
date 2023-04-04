package com.ucoruh.logger;

import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class LogSimpleFormatter extends SimpleFormatter {

	private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

	@Override
	public synchronized String format(LogRecord lr) {
		return String.format(format, new java.util.Date(lr.getMillis()), lr.getLevel().getLocalizedName(),
				lr.getMessage());
	}

}
