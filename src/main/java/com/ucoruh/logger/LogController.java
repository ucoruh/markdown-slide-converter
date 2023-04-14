package com.ucoruh.logger;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The LogController class is responsible for setting up logging operations with
 * a property file.
 * 
 * This class suppresses the logging output to the console and sets up handlers
 * for logging to a text file and an HTML file.
 * 
 * To use this class, call the static method `setup()` to set up the logging
 * configuration.
 * 
 * Example usage:
 * 
 * <pre>
 * {@code
 * LogController.setup();
 * Logger LOGGER = Logger.getLogger(LogController.class.getName());
 * LOGGER.info("Logging is working!");
 * }
 * </pre>
 * 
 * This class requires the LogSimpleFormatter and LogHtmlFormatter classes to
 * format the log messages.
 * 
 * @see LogSimpleFormatter
 * @see LogHtmlFormatter
 * @author ugur.coruh
 *
 */
public class LogController {

	private final static Logger LOGGER = Logger.getLogger(LogController.class.getName());

	// static initializer blocks are executed once when the class is loaded
	static private FileHandler fileTxt;
	static private ConsoleHandler consoleHandler;
	static private FileHandler fileHTML;
	static private Formatter formatterHTML;
	static private LogSimpleFormatter simpleFormatter;

	/**
	 * The setup() method sets up the logging configuration.
	 * 
	 * This method sets the log level to INFO and suppresses the logging output to
	 * the console. It also creates handlers for logging to a text file and an HTML
	 * file, and sets the appropriate formatters for each handler.
	 * 
	 * @throws IOException if there is an I/O error when creating the file handlers
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

		// set the log level to INFO
		LOGGER.setLevel(Level.INFO);

		// create a text file handler
		fileTxt = new FileHandler("markdown-cli-log.txt");

		// create a console handler
		consoleHandler = new ConsoleHandler();

		// create an HTML file handler
		fileHTML = new FileHandler("markdown-cli-log.html");

		// create a simple formatter for both the text file and console handlers
		simpleFormatter = new LogSimpleFormatter();
		fileTxt.setFormatter(simpleFormatter);
		consoleHandler.setFormatter(simpleFormatter);

		// add the handlers to the root logger
		rootLogger.addHandler(fileTxt);
		rootLogger.addHandler(consoleHandler);

		// create an HTML formatter and add the HTML file handler to the root logger
		formatterHTML = new LogHtmlFormatter();
		fileHTML.setFormatter(formatterHTML);
		rootLogger.addHandler(fileHTML);
	}
}
