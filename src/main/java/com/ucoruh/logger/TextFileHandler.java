package com.ucoruh.logger;

import java.io.IOException;
import java.util.logging.FileHandler;

/**
 * 
 * A custom FileHandler for logging to a text file. Used for logging.properties
 * to define multiple file handlers.
 * 
 * @see java.util.logging.FileHandler
 * @author ugur.coruh
 */
public class TextFileHandler extends FileHandler {

	public TextFileHandler() throws IOException, SecurityException {
		super();
	}

}