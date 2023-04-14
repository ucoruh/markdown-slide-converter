package com.ucoruh.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * 
 * @author ugur.coruh
 *
 *         Custom log formatter for HTML table output
 */
class LogHtmlFormatter extends Formatter {

	/**
	 * 
	 * Formats a log record into an HTML table row, with the log level, timestamp,
	 * 
	 * calling function name, and log message displayed in separate columns.
	 * 
	 * @param rec the log record to format
	 * 
	 * @return a string representation of the log record in HTML table row format
	 */
	@Override
	public String format(LogRecord rec) {

		String functionName = "";
		if (rec.getSourceClassName() != null) {
			try {
				StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
				if (stackTrace.length > 2) {
					// The top two elements are getStackTrace() and format(),
					// so we start iterating from the third element.
					for (int i = 2; i < stackTrace.length; i++) {
						StackTraceElement element = stackTrace[i];
						if (element.getClassName().equals(rec.getSourceClassName())) {
							functionName = "[" + element.getMethodName() + "] ";
							break;
						}
					}
				}
			} catch (Exception e) {
				// ignore
			}
		}

		StringBuffer buf = new StringBuffer(1000);
		buf.append("<tr>\n");

		if (rec.getLevel().intValue() > Level.WARNING.intValue()) {
			// colorize any levels > WARNING in red
			buf.append("\t<td style=\"color:red\">");
			buf.append("<b>");
			buf.append("[ERROR]");
			buf.append("</b>");
		} else if (rec.getLevel().intValue() == Level.WARNING.intValue()) {
			// colorize any levels == WARNING in yellow
			buf.append("\t<td style=\"color:yellow\">");
			buf.append("<b>");
			buf.append("[WARN]");
			buf.append("</b>");
		} else if (rec.getLevel().intValue() == Level.INFO.intValue()) {
			buf.append("\t<td style=\"color:green\">");
			buf.append("<b>");
			buf.append("[INFO]");
			buf.append("</b>");
		} else {
			buf.append("\t<td>");
			buf.append("<b>");
			buf.append("[DEBUG]");
			buf.append("</b>");
		}

		buf.append("</td>\n");
		buf.append("\t<td>");
		buf.append(calcDate(rec.getMillis()));
		buf.append("</td>\n");
		buf.append("\t<td>");
		buf.append(functionName);
		buf.append("</td>\n");
		buf.append("\t<td>");
		buf.append(formatMessage(rec));
		buf.append("</td>\n");
		buf.append("</tr>\n");

		return buf.toString();
	}

	/**
	 * 
	 * Calculates the date and time hour string from milliseconds.
	 * 
	 * @param millisecs the number of milliseconds since January 1, 1970, 00:00:00
	 *                  GMT
	 * @return the date and time hour string in the format of "MMM dd,yyyy HH:mm"
	 */
	private String calcDate(long millisecs) {
		SimpleDateFormat date_format = new SimpleDateFormat("MMM dd,yyyy HH:mm");
		Date resultdate = new Date(millisecs);
		return date_format.format(resultdate);
	}

	/**
	 * 
	 * This method is called just after the handler using this formatter is created.
	 * It returns a formatted HTML string to be used as the header of the log file.
	 * 
	 * @param h the handler using this formatter
	 * @return a formatted HTML string representing the header of the log file
	 */
	@Override
	public String getHead(Handler h) {
		return "<!DOCTYPE html>\n<head>\n<style>\n" + "table { width: 100%; }\n" + "th { font: bold 10pt Tahoma; }\n"
				+ "td { font: normal 10pt Tahoma; }\n" + "h1 { font: normal 11pt Tahoma; }\n"
				+ ".error { color: red; }\n" + ".warning { color: yellow; }\n" + ".info { color: green; }\n"
				+ "</style>\n" + "</head>\n" + "<body>\n" + "<h1>" + (new Date()) + "</h1>\n"
				+ "<table border=\"0\" cellpadding=\"5\" cellspacing=\"3\">\n" + "<tr align=\"left\">\n"
				+ "\t<th style=\"width:10%\">Loglevel</th>\n" + "\t<th style=\"width:15%\">Time</th>\n"
				+ "\t<th style=\"width:15%\">Executed Method</th>\n" + "\t<th style=\"width:60%\">Log Message</th>\n"
				+ "</tr>\n";
	}

	/**
	 * 
	 * This method is called just after the handler using this formatter is closed.
	 * It returns a formatted HTML string to be used as the footer of the log file.
	 * 
	 * @param h the handler using this formatter
	 * @return a formatted HTML string representing the footer of the log file
	 */
	@Override
	public String getTail(Handler h) {
		return "</table>\n</body>\n</html>";
	}
}
