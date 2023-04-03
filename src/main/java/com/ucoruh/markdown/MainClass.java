package com.ucoruh.markdown;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.ucoruh.controller.MarkdownController;
import com.ucoruh.utils.Utils;

public class MainClass {

	
	
	// input file for signature
	private static Path inputFolderPath = null;
	
	// input file for signature
	private static File inputFile = null;

	// output file for signature
	private static File outputFile = null;

	// overwrite output file if exist
	private static boolean isOverwriteEnabled = false;

	// Logger JUL
	private final static Logger LOGGER = Logger.getLogger(MainClass.class.getName());

	public static void main(String[] args) {

		// Build Options
		LOGGER.info("Application started");

		Options options = buildOptions();

		LOGGER.info("Command line options builded");

		try {

			// Parse Command Line Arguments
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd = parser.parse(options, args);

			LOGGER.info("Command line options parsed");

			// Argument Controls
			if (cmd.hasOption("help")) {

				LOGGER.info("--help option used, printing help");
				displayHelp(options);

			} else if (cmd.hasOption("mergefile")) {

				LOGGER.info("--mergefile option used");

				if (cmd.hasOption("overwrite")) {
					isOverwriteEnabled = true;
					LOGGER.info("--mergefile option used with --overwrite to overwrite output markdown file");
				}

				String[] fileNames = cmd.getOptionValues("mergefile");

				if (fileNames.length == 1) {

					inputFile = new File(Utils.getFullPath(fileNames[0]));

					LOGGER.info("--mergefile option used with input file only, output will be include "
							+ MarkdownController.MERGED_FILE_POSTFIX + " postfix");

					LOGGER.info("--mergefile option inputfile value [" + fileNames[0] + "]");
					LOGGER.info("--mergefile option inputfile converted value [" + inputFile + "]");

				} else if (fileNames.length == 2) {

					inputFile = new File(Utils.getFullPath(fileNames[0]));
					outputFile = new File(Utils.getFullPath(fileNames[1]));

					LOGGER.info("--mergefile option used with input and output file");

					LOGGER.info("--mergefile option inputfile value [" + fileNames[0] + "]");
					LOGGER.info("--mergefile option inputfile converted value [" + inputFile + "]");

					LOGGER.info("--mergefile option outputFile value [" + fileNames[1] + "]");
					LOGGER.info("--mergefile option outputFile converted value [" + outputFile + "]");

					ifOutputFileExistLogic(options, outputFile);
					ifOutputFileNotExistLogic(options, outputFile);

				} else {

					LOGGER.severe("--mergefile option file value must be 1 or 2");

					if (fileNames.length == 0) {
						LOGGER.severe("--mergefile option file value doesn't exist");
					}

					for (String value : fileNames) {
						LOGGER.severe("--mergefile option file value [" + value + "]");
					}

					displayHelp(options);
					LOGGER.severe("Operation Terminated");
					System.exit(-1);
				}

				MarkdownController controller = null;

				try {

					controller = new MarkdownController();

					controller.setInputFilePath(inputFile.getPath());

					if (outputFile != null) {
						controller.setOutputFilePath(outputFile.getPath());
					}

					controller.mergeSingleFile();

					LOGGER.info("--mergefile option completed");

				} catch (IOException e) {

					LOGGER.log(Level.SEVERE, "--mergefile operation error", e);
					e.printStackTrace();
					displayHelp(options);
					LOGGER.severe("Operation Terminated");
					System.exit(-1);
				}

			} else if (cmd.hasOption("mergefolder")) {

				LOGGER.info("--mergefolder option used");

				if (cmd.hasOption("overwrite")) {
					isOverwriteEnabled = true;
					LOGGER.info("--mergefolder option used with --overwrite to overwrite output markdown file");
				}

				String[] folderPath = cmd.getOptionValues("mergefolder");

				if (folderPath.length == 1) {

//					inputFolder = new File(Utils.getFullPath(folderPath[0]));

					LOGGER.info("--mergefolder option used with input file only, output will be include "
							+ MarkdownController.MERGED_FILE_POSTFIX + " postfix");
					
					
					

					LOGGER.info("--mergefolder option input path value [" + folderPath[0] + "]");
					
					if(!Paths.get(folderPath[0]).isAbsolute())
					{
						inputFolderPath = Paths.get(Utils.getWorkingDirectory()).resolve(folderPath[0]).toAbsolutePath();
					}else {
						inputFolderPath = Paths.get(folderPath[0]);
					}
					
					LOGGER.info("--mergefolder option input path converted value [" + inputFolderPath.getFileName().toAbsolutePath().toString() + "]");

				} else {

					LOGGER.severe("--mergefolder option input path value must be 1");

					if (folderPath.length == 0) {
						LOGGER.severe("--mergefolder option input path value doesn't exist");
					}

					for (String value : folderPath) {
						LOGGER.severe("--mergefolder option input path value [" + value + "]");
					}

					displayHelp(options);
					LOGGER.severe("Operation Terminated");
					System.exit(-1);
				}

				MarkdownController controller = null;

				try {

					controller = new MarkdownController();

					controller.setInputFolderPath(inputFolderPath.getFileName().toAbsolutePath().toString());

					controller.mergeFolder();

					LOGGER.info("--mergefolder option completed");

				} catch (IOException e) {

					LOGGER.log(Level.SEVERE, "--mergefolder operation error", e);
					e.printStackTrace();
					displayHelp(options);
					LOGGER.severe("Operation Terminated");
					System.exit(-1);
				}

			} else {

				displayHelp(options);
				LOGGER.severe("Operation Terminated");
				System.exit(-1);
			}

		} catch (ParseException | URISyntaxException e) {

			displayHelp(options);
			LOGGER.log(Level.SEVERE, "Operation Terminated", e);
			System.exit(-1);
		}

	}

	/**
	 * Output file not exist logic
	 * 
	 * @param fiOptions    [input] Options object for input argument control
	 * @param fiOutputFile [input] Output file for control
	 */
	private static void ifOutputFileNotExistLogic(Options fiOptions, File fiOutputFile) {

		try {

			LOGGER.info("Trying to create and delete output file [" + fiOutputFile + "], for access permissions");

			fiOutputFile.createNewFile();
			fiOutputFile.delete();

			LOGGER.info("Access permissions valid for output file [" + fiOutputFile + "]");

		} catch (IOException e) {

			LOGGER.severe("Access permissions required for output file [" + fiOutputFile + "]");

			e.printStackTrace();
			displayHelp(fiOptions);
			LOGGER.severe("Operation Terminated");
			System.exit(-1);
		}
	}

	/**
	 * Output File Existing Logic
	 * 
	 * @param options    [input] Options object for input argument control
	 * @param outputFile [input] Output file for control
	 */
	private static void ifOutputFileExistLogic(Options options, File outputFile) {

		if (outputFile.exists() && !isOverwriteEnabled) {
			LOGGER.severe(
					"output file [" + outputFile + "] already exist, please control your file and delete or rename it");
			displayHelp(options);
			LOGGER.severe("Operation Terminated");
			System.exit(-1);
		}

		if (outputFile.exists() && isOverwriteEnabled) {

			LOGGER.warning("Output file " + outputFile + " already exist, DELETING..");

			outputFile.delete();
			if (outputFile.exists()) {
				LOGGER.severe("Output file [" + outputFile
						+ "] deletion failed,check your permissions, please control your file and delete or rename it");
				displayHelp(options);
				LOGGER.severe("Operation Terminated");
				System.exit(-1);
			}
		}
	}

	/**
	 * Build Options for Apache Commons CLI used for argument parsing and options
	 * visit examples for
	 * https://www.tutorialspoint.com/commons_cli/commons_cli_quick_guide.htm
	 * 
	 * @return Options object for Command Parsing
	 */
	private static Options buildOptions() {

		Options options = new Options();

		// Help Option Added
		Option helpOption = Option.builder().longOpt("help").hasArg(false).desc("Display help").build();
		options.addOption(helpOption);

		// Override Option Added
		Option overrideOption = Option.builder().longOpt("overwrite").hasArg(false)
				.desc("Overwrite output markdown file if exist").build();
		options.addOption(overrideOption);

		// Merge File Option Added
		Option mergeFileOption = Option.builder().longOpt("mergefile")
				.argName("file input path> < (optional) file output path").hasArgs()
				.desc("Merge markdown slide/newpage seperators and remove dublicated titles").build();
		options.addOption(mergeFileOption);

		// Merge File Option Added
		Option mergeFolderOption = Option.builder().longOpt("mergefolder").argName("folder input path>").hasArgs()
				.desc("Merge markdown slide/newpage seperators and remove dublicated titles for folder").build();
		options.addOption(mergeFolderOption);

		return options;
	}

	/**
	 * Display Help
	 * 
	 * @param options [input] Input Options
	 */
	private static void displayHelp(Options options) {
		// display help
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("MARKDOWN MERGER", options);
	}

}
