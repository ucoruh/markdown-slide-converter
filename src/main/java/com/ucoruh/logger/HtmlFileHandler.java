package com.ucoruh.logger;

import java.io.IOException;
import java.util.logging.FileHandler;

/**
 * 
 * @brief A custom file handler for logging in HTML format.
 * 
 *        This class extends the built-in FileHandler class to create a custom
 *        file handler that writes log records in HTML format.
 * 
 *        It can be used in the logging.properties file to define multiple file
 *        handlers.
 * 
 * @note This class does not add any additional functionality to the FileHandler
 *       class, but only serves as a placeholder for defining
 * 
 *       a custom file handler.
 * 
 * @see FileHandler
 * 
 * @see LogManager
 * 
 * @par Example:
 * 
 * @code{.java}
 * 
 *              handlers= java.util.logging.ConsoleHandler,
 *              com.example.HtmlFileHandler
 * 
 *              com.example.HtmlFileHandler.level = ALL
 * 
 *              com.example.HtmlFileHandler.formatter =
 *              com.example.HtmlFormatter
 * 
 *              com.example.HtmlFileHandler.encoding = UTF-8
 * 
 *              com.example.HtmlFileHandler.limit = 10000000
 * 
 *              com.example.HtmlFileHandler.count = 3
 * 
 *              com.example.HtmlFileHandler.pattern = logs/log_%u.html
 * 
 * @endcode
 * 
 * @since 1.0
 */
public class HtmlFileHandler extends FileHandler {

	public HtmlFileHandler() throws IOException, SecurityException {
		super();
	}

}