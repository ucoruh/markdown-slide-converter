package com.ucoruh.logger;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 
 * @author ugur.coruh
 *
 */
public class LogController {

	private final static Logger LOGGER = Logger.getLogger(LogController.class.getName());

//	static {
//		// must set before the Logger
//		// loads logging.properties from the classpath
//		String path = LogController.class.getClassLoader().getResource("logging.properties").getFile();
//		System.setProperty("java.util.logging.config.file", path);
//
//	}

	static private FileHandler fileTxt;
//	static private SimpleFormatter formatterTxt;
	static private ConsoleHandler consoleHandler;

	static private FileHandler fileHTML;
	static private Formatter formatterHTML;
	static private LogSimpleFormatter simpleFormatter;


	/**
	 * Logging Operations Used With Property File
	 * @throws IOException
	 */
	static public void setup() throws IOException {

		// suppress the logging output to the console
		Logger rootLogger = Logger.getLogger("");
		Handler[] handlers = rootLogger.getHandlers();

		if (handlers.length != 0) {
			if (handlers[0] instanceof ConsoleHandler) {
				rootLogger.removeHandler(handlers[0]);
			}

		}

		LOGGER.setLevel(Level.INFO);
		fileTxt = new FileHandler("markdown-cli-log.txt");
		fileHTML = new FileHandler("markdown-cli-log.html");
		consoleHandler = new ConsoleHandler();
		
		// create a TXT formatter
		simpleFormatter = new LogSimpleFormatter();
		fileTxt.setFormatter(simpleFormatter);
		rootLogger.addHandler(fileTxt);

		// create an HTML formatter
		formatterHTML = new LogHtmlFormatter();
		fileHTML.setFormatter(formatterHTML);
		rootLogger.addHandler(fileHTML);
		
		
		// create an Console formatter
		simpleFormatter = new LogSimpleFormatter();
		consoleHandler.setFormatter(simpleFormatter);
		rootLogger.addHandler(consoleHandler);

	}
}
