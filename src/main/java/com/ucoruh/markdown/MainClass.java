package com.ucoruh.markdown;

import java.io.File;
import java.io.IOException;
import java.lang.ModuleLayer.Controller;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

//import com.theokanning.openai.completion.CompletionRequest;
//import com.theokanning.openai.completion.chat.ChatCompletionRequest;
//import com.theokanning.openai.completion.chat.ChatMessage;
//import com.theokanning.openai.completion.chat.ChatMessageRole;
//import com.theokanning.openai.image.CreateImageRequest;
//import com.theokanning.openai.service.OpenAiService;
import com.ucoruh.controller.MarkdownController;
import com.ucoruh.logger.LogController;
import com.ucoruh.option.ControllerOptions;
import com.ucoruh.option.OptionType;
import com.ucoruh.utils.Utils;

/**
 * 
 * Main class of the application.
 * 
 * Initializes the logging system and sets up the application logger.
 * 
 * @see LogController
 * 
 * @see java.util.logging.Logger
 */
public class MainClass {

	static {
		try {
			LogController.setup();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * The logger instance for the MainClass.
	 */
	private final static Logger LOGGER = Logger.getLogger(MainClass.class.getName());

	/**
	 * 
	 * Main method of the application.
	 * 
	 * Parses the command-line arguments and starts the application.
	 * 
	 * @param args the command-line arguments
	 * 
	 * @throws IOException if an I/O error occurs
	 */
	public static void main(String[] args) throws IOException {

		// Build Options
		LOGGER.info("Application started");

		Options options = buildOptions();

		ControllerOptions controllerOptions = new ControllerOptions();

		LOGGER.info("Command line options builded");

		try {
			// Parse Command Line Arguments
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd = parser.parse(options, args);

			LOGGER.info("Command line options parsed");

			// Detect Current Main Option
			controllerOptions.setOptionType(OptionType.NONE);

			for (OptionType optionType : OptionType.values()) {

				if (cmd.hasOption(optionType.getCommandName())) {

					if (controllerOptions.getOptionType() != OptionType.NONE) {

						displayHelp(options);
						LOGGER.log(Level.SEVERE, "Multiple Option Inserted, Operation Terminated!");
						System.exit(-1);

					} else {

						controllerOptions.setOptionType(optionType);
						LOGGER.info("--" + optionType.getCommandName() + " main option used");
					}

				}
			}

			// Get Current Option Argument and Other Related Options
			if (cmd.hasOption(ControllerOptions.MKDOCS_OPTION)) {
				controllerOptions.setMkdocsIsSet(true);
				// TODO: Get mkdocs prefix or postfix
				LOGGER.info("--" + controllerOptions.getOptionType().getCommandName() + " option used with "
						+ ControllerOptions.MKDOCS_OPTION);
			}

			if (cmd.hasOption(ControllerOptions.PANDOC_OPTION)) {
				controllerOptions.setPandocIsSet(true);
				// TODO: Get pandocs prefix or postfix
				LOGGER.info("--" + controllerOptions.getOptionType().getCommandName() + " option used with "
						+ ControllerOptions.PANDOC_OPTION);
			}

			if (cmd.hasOption(ControllerOptions.OVERWRITE_OPTION)) {
				controllerOptions.setOverwriteIsSet(true);
				LOGGER.info("--" + controllerOptions.getOptionType().getCommandName() + " option used with "
						+ ControllerOptions.OVERWRITE_OPTION + "to overwrite output files");
			}

			if (cmd.hasOption(ControllerOptions.BUILD_OPTION)) {
				controllerOptions.setBuildIsSet(true);
				LOGGER.info("--" + controllerOptions.getOptionType().getCommandName() + " option used with "
						+ ControllerOptions.BUILD_OPTION);
			}

			if (cmd.hasOption(ControllerOptions.REBUILD_OPTION)) {
				controllerOptions.setRebuildIsSet(true);
				LOGGER.info("--" + controllerOptions.getOptionType().getCommandName() + " option used with "
						+ ControllerOptions.REBUILD_OPTION);
				if (controllerOptions.isBuildIsSet()) {
					LOGGER.log(Level.WARNING,
							ControllerOptions.BUILD_OPTION + " override via " + ControllerOptions.REBUILD_OPTION);
				}
			}

			if (cmd.hasOption(ControllerOptions.LANGUAGE_OPTION)) {
				// TODO: Get languages en tr fr etc...
				LOGGER.info("--" + controllerOptions.getOptionType().getCommandName() + " option used with "
						+ ControllerOptions.LANGUAGE_OPTION);

				String[] languages = cmd.getOptionValues(ControllerOptions.LANGUAGE_OPTION);
				for (String value : languages) {
					LOGGER.severe(ControllerOptions.LANGUAGE_OPTION + " option value [" + value + "]");
					controllerOptions.getLanguages().add(value);
				}

			}

			if (cmd.hasOption(ControllerOptions.FILE_OPTION)) {
				// Get input and output file with overwrite control
				LOGGER.info("--" + controllerOptions.getOptionType().getCommandName() + " option used with "
						+ ControllerOptions.FILE_OPTION);

				String[] fileNames = cmd.getOptionValues(ControllerOptions.FILE_OPTION);
				if (fileNames.length == 0 || fileNames.length > 2) {
					LOGGER.severe("--" + controllerOptions.getOptionType().getCommandName() + " command "
							+ ControllerOptions.FILE_OPTION + " option file value must be 1 or 2");

					if (fileNames.length == 0) {
						LOGGER.severe("--" + controllerOptions.getOptionType().getCommandName() + " command "
								+ ControllerOptions.FILE_OPTION + " option file value doesn't exist");
					}

					for (String value : fileNames) {
						LOGGER.severe("--" + controllerOptions.getOptionType().getCommandName() + " command "
								+ ControllerOptions.FILE_OPTION + " option file value [" + value + "]");
					}

					displayHelp(options);
					LOGGER.severe("Operation Terminated");
					System.exit(-1);
				}

				if (fileNames.length == 1) {

					controllerOptions.setFileInputPath(fileNames[0]);

					LOGGER.info("--" + controllerOptions.getOptionType().getCommandName() + " command "
							+ ControllerOptions.FILE_OPTION
							+ " option used with input file only, output will be include "
							+ ControllerOptions.MKDOCS_WEB_PREFIX + " prefix");

					LOGGER.info("--" + controllerOptions.getOptionType().getCommandName() + " command "
							+ ControllerOptions.FILE_OPTION + " option inputfile value ["
							+ controllerOptions.getFileInputPath() + "]");
					LOGGER.info("--" + controllerOptions.getOptionType().getCommandName() + " command "
							+ ControllerOptions.FILE_OPTION + " option inputfile converted value ["
							+ Utils.getFullPath(controllerOptions.getFileInputPath()) + "]");

				} else if (fileNames.length == 2) {

					controllerOptions.setFileInputPath(fileNames[0]);
					controllerOptions.setFileOutputPath(fileNames[1]);

					LOGGER.info("--" + controllerOptions.getOptionType().getCommandName() + " command "
							+ ControllerOptions.FILE_OPTION + " option used with input and output file");

					LOGGER.info("--" + controllerOptions.getOptionType().getCommandName() + " command "
							+ ControllerOptions.FILE_OPTION + " option inputfile value ["
							+ controllerOptions.getFileInputPath() + "]");
					LOGGER.info("--" + controllerOptions.getOptionType().getCommandName() + " command "
							+ ControllerOptions.FILE_OPTION + " option inputfile converted value ["
							+ Utils.getFullPath(controllerOptions.getFileInputPath()) + "]");

					LOGGER.info("--" + controllerOptions.getOptionType().getCommandName() + " command "
							+ ControllerOptions.FILE_OPTION + " option outputfile value ["
							+ controllerOptions.getFileOutputPath() + "]");
					LOGGER.info("--" + controllerOptions.getOptionType().getCommandName() + " command "
							+ ControllerOptions.FILE_OPTION + " option outputfile converted value ["
							+ Utils.getFullPath(controllerOptions.getFileOutputPath()) + "]");

					ifOutputFileExistLogic(options, controllerOptions);

					ifOutputFileNotExistLogic(options, controllerOptions);
				}
			}

			if (cmd.hasOption(ControllerOptions.FOLDER_OPTION)) {

				LOGGER.info("--" + controllerOptions.getOptionType().getCommandName() + " option used with "
						+ ControllerOptions.FOLDER_OPTION);

				String[] folderPaths = cmd.getOptionValues(ControllerOptions.FOLDER_OPTION);
				if (folderPaths.length == 0 || folderPaths.length > 2) {
					LOGGER.severe("--" + controllerOptions.getOptionType().getCommandName() + " command "
							+ ControllerOptions.FOLDER_OPTION + " option folder value must be 1 or 2");

					if (folderPaths.length == 0) {
						LOGGER.severe("--" + controllerOptions.getOptionType().getCommandName() + " command "
								+ ControllerOptions.FOLDER_OPTION + " option folder value doesn't exist");
					}

					for (String value : folderPaths) {
						LOGGER.severe("--" + controllerOptions.getOptionType().getCommandName() + " command "
								+ ControllerOptions.FOLDER_OPTION + " option folder value [" + value + "]");
					}

					displayHelp(options);
					LOGGER.severe("Operation Terminated");
					System.exit(-1);
				}

				if (folderPaths.length == 1) {

					if (!Paths.get(folderPaths[0]).isAbsolute()) {
						controllerOptions.setFolderInputPath(Paths.get(Utils.getWorkingDirectory())
								.resolve(folderPaths[0]).toAbsolutePath().toString());
					} else {
						controllerOptions.setFolderInputPath(Paths.get(folderPaths[0]).toString());
					}

					LOGGER.info("--" + controllerOptions.getOptionType().getCommandName() + " command "
							+ ControllerOptions.FOLDER_OPTION
							+ " option used with input folder only, output will be include "
							+ ControllerOptions.MKDOCS_WEB_PREFIX + "," + ControllerOptions.PANDOCS_DOC_PREFIX + ","
							+ ControllerOptions.PANDOCS_PPT_PREFIX + " prefixes also"
							+ ControllerOptions.MARP_HTML_POSTFIX + "," + ControllerOptions.MARP_PDF_POSTFIX + ","
							+ ControllerOptions.MARP_PPTX_POSTFIX + "," + ControllerOptions.PANDOC_DOCX_POSTFIX + ","
							+ ControllerOptions.PANDOC_PDF_POSTFIX + " postfixes...");

					LOGGER.info("--" + controllerOptions.getOptionType().getCommandName() + " command "
							+ ControllerOptions.FOLDER_OPTION + " option inputfolder value [" + folderPaths[0] + "]");
					LOGGER.info("--" + controllerOptions.getOptionType().getCommandName() + " command "
							+ ControllerOptions.FOLDER_OPTION + " option inputfolder converted value ["
							+ Utils.getFullPath(controllerOptions.getFolderInputPath()) + "]");

				} else if (folderPaths.length == 2) {

					controllerOptions.setFileInputPath(folderPaths[0]);
					controllerOptions.setFileOutputPath(folderPaths[1]);

					if (!Paths.get(folderPaths[0]).isAbsolute()) {
						controllerOptions.setFolderInputPath(Paths.get(Utils.getWorkingDirectory())
								.resolve(folderPaths[0]).toAbsolutePath().toString());
					} else {
						controllerOptions.setFolderInputPath(Paths.get(folderPaths[0]).toString());
					}

					if (!Paths.get(folderPaths[1]).isAbsolute()) {
						controllerOptions.setFolderOutputPath(Paths.get(Utils.getWorkingDirectory())
								.resolve(folderPaths[1]).toAbsolutePath().toString());
					} else {
						controllerOptions.setFolderOutputPath(Paths.get(folderPaths[1]).toString());
					}

					LOGGER.info("--" + controllerOptions.getOptionType().getCommandName() + " command "
							+ ControllerOptions.FOLDER_OPTION + " option used with input and output folders");

					LOGGER.info("--" + controllerOptions.getOptionType().getCommandName() + " command "
							+ ControllerOptions.FOLDER_OPTION + " option inputfolder value [" + folderPaths[0] + "]");
					LOGGER.info("--" + controllerOptions.getOptionType().getCommandName() + " command "
							+ ControllerOptions.FOLDER_OPTION + " option inputfolder converted value ["
							+ Utils.getFullPath(controllerOptions.getFolderInputPath()) + "]");

					LOGGER.info("--" + controllerOptions.getOptionType().getCommandName() + " command "
							+ ControllerOptions.FOLDER_OPTION + " option outputfolder value [" + folderPaths[1] + "]");
					LOGGER.info("--" + controllerOptions.getOptionType().getCommandName() + " command "
							+ ControllerOptions.FOLDER_OPTION + " option outputfolder converted value ["
							+ Utils.getFullPath(controllerOptions.getFolderOutputPath()) + "]");

					// ifOutputFileExistLogic(options, controllerOptions);
					// ifOutputFileNotExistLogic(options, controllerOptions);
				}

			}

		} catch (ParseException | URISyntaxException e) {

			displayHelp(options);
			LOGGER.log(Level.SEVERE, "Operation Terminated", e);
			System.exit(-1);
		}

		// Options Need File or Folder Inputs/Outputs
		if (controllerOptions.getOptionType() == OptionType.GENERATEPAGES
				|| controllerOptions.getOptionType() == OptionType.BUILDPAGES
				|| controllerOptions.getOptionType() == OptionType.CLEANPAGES
				|| controllerOptions.getOptionType() == OptionType.MERGEPAGES) {

			if (!options.hasOption(ControllerOptions.FILE_OPTION)
					&& !options.hasOption(ControllerOptions.FOLDER_OPTION)) {
				displayHelp(options);
				LOGGER.log(Level.SEVERE, "File or Folder Not Defined, Operation Terminated");
				System.exit(-1);
			}
		}

		MarkdownController controller = new MarkdownController();

		controller.setOptions(controllerOptions);

		try {

			boolean result = false;

			// Markdown Controller Processing
			if (controllerOptions.getOptionType() == OptionType.HELP) {
				LOGGER.info("Display Help Information...");
				// Help Processing
				displayHelp(options);
				LOGGER.info("Exiting with Success...");
				System.exit(0);
			} else if (controllerOptions.getOptionType() == OptionType.CLEANPAGES) {
				// Clean Pages Processing
				result = controller.clean();
			} else if (controllerOptions.getOptionType() == OptionType.MERGEPAGES) {
				// Merge Pages Processing
				result = controller.mergePages();
			} else if (controllerOptions.getOptionType() == OptionType.BUILDPAGES) {
				// Build Pages Processing
				result = controller.buildPages();
			} else if (controllerOptions.getOptionType() == OptionType.DEPLOYPAGES) {
				// Deploy Pages Processing
				result = controller.deployPages();
			} else if (controllerOptions.getOptionType() == OptionType.GENERATEPAGES) {
				// Generate Pages Processing
				result = controller.generatePages();
			} else if (controllerOptions.getOptionType() == OptionType.DRAWIOEXPORT) {
				// Generate Pages Processing
				result = controller.drawioExportImages();
			}

			if (result) {
				LOGGER.severe("Operation Successfully Completed!!");
			} else {
				LOGGER.severe("Operation Failed!!");
			}

		} catch (IOException | TranscoderException | URISyntaxException e) {

			LOGGER.log(Level.SEVERE, " Operation error", e);
			e.printStackTrace();
			displayHelp(options);
			LOGGER.severe("Operation Terminated");
			System.exit(-1);
		}

	}

	/**
	 * 
	 * Checks if the output file exists and if it has write permissions. If not,
	 * 
	 * displays an error message and terminates the application.
	 * 
	 * @param options           The command line options passed to the application.
	 * 
	 * @param controllerOptions The controller options parsed from the command line
	 *                          options.
	 * 
	 * @throws URISyntaxException If the URI syntax of the output file is invalid.
	 */
	private static void ifOutputFileNotExistLogic(Options options, ControllerOptions controllerOptions)
			throws URISyntaxException {

		try {

			File outputFile = new File(Utils.getFullPath(controllerOptions.getFileOutputPath()));

			LOGGER.info("Trying to create and delete output file [" + outputFile + "], for access permissions");

			outputFile.createNewFile();
			outputFile.delete();

			LOGGER.info("Access permissions valid for output file [" + outputFile + "]");

		} catch (IOException | URISyntaxException e) {

			LOGGER.severe("Access permissions required for output file ["
					+ Utils.getFullPath(controllerOptions.getFileOutputPath()) + "]");

			e.printStackTrace();
			displayHelp(options);
			LOGGER.severe("Operation Terminated");
			System.exit(-1);
		}
	}

	/**
	 * Check if the output file already exists, if it does, handle it according to the user's preferences
	 *
	 * @param options           [input] Input Options
	 * @param controllerOptions [input] Controller Options
	 * @throws URISyntaxException When there is an error with the file path
	 */
	private static void ifOutputFileExistLogic(Options options, ControllerOptions controllerOptions)
			throws URISyntaxException {

		File outputFile = new File(Utils.getFullPath(controllerOptions.getFileOutputPath()));

		if (outputFile.exists() && !controllerOptions.isOverwriteIsSet()) {
			LOGGER.severe(
					"output file [" + outputFile + "] already exist, please control your file and delete or rename it");
			displayHelp(options);
			LOGGER.severe("Operation Terminated");
			System.exit(-1);
		}

		if (outputFile.exists() && controllerOptions.isOverwriteIsSet()) {

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
	 * 
	 * Build Options for Apache Commons CLI used for argument parsing and options
	 * 
	 * visit examples for
	 * 
	 * https://www.tutorialspoint.com/commons_cli/commons_cli_quick_guide.htm
	 * 
	 * @return Options object for Command Parsing
	 */
	private static Options buildOptions() {

		Options options = new Options();

		// HELP_OPTION Option Added
		Option option = Option.builder().longOpt(ControllerOptions.HELP_OPTION).hasArg(false).desc("Display help")
				.build();
		options.addOption(option);

		// HELP_OPTION Option Added
		option = Option.builder().longOpt(ControllerOptions.DRAWIOEXPORT_OPTION).hasArg(false)
				.desc("Export Drawio Images SVG, PNG, JPG").build();
		options.addOption(option);

		// MERGEPAGES_OPTION Option Added
		option = Option.builder().longOpt(ControllerOptions.MERGEPAGES_OPTION).hasArg(false).desc(
				"Merge markdown slide/newpage seperators and remove dublicated titles for Pandoc PDF and Mkdocs Web")
				.build();
		options.addOption(option);

		// BUILDPAGES_OPTION Option Added
		option = Option.builder().longOpt(ControllerOptions.BUILDPAGES_OPTION).hasArg(false)
				.desc("Generate html,pdf and docx files").build();
		options.addOption(option);

		// CLEANPAGES_OPTION Option Added
		option = Option.builder().longOpt(ControllerOptions.CLEANPAGES_OPTION).hasArg(false)
				.desc("Clean html,pdf and docx files and mkdocs and pandoc generated files").build();
		options.addOption(option);

		// GENERATEPAGES_OPTION Option Added
		option = Option.builder().longOpt(ControllerOptions.GENERATEPAGES_OPTION).hasArg(false)
				.desc("Generate default webpage markdown files for selected config").build();
		options.addOption(option);

		// DEPLOYPAGES_OPTION Option Added
		option = Option.builder().longOpt(ControllerOptions.DEPLOYPAGES_OPTION).hasArg(false)
				.desc("Run mkdocs deploy command").build();
		options.addOption(option);

		// OVERWRITE_OPTION Option Added
		option = Option.builder().longOpt(ControllerOptions.OVERWRITE_OPTION).hasArg(false)
				.desc("overwrite flag for outputs").build();
		options.addOption(option);

		// BUILD_OPTION Option Added
		option = Option.builder().longOpt(ControllerOptions.BUILD_OPTION).hasArg(false).desc("build flag for outputs")
				.build();
		options.addOption(option);

		// REBUILD_OPTION Option Added
		option = Option.builder().longOpt(ControllerOptions.REBUILD_OPTION).hasArg(false)
				.desc("rebuild flag for outputs").build();
		options.addOption(option);

		// LANGUAGE_OPTION Option Added
		option = Option.builder().longOpt(ControllerOptions.LANGUAGE_OPTION).argName("en> <fr> <tr").hasArgs()
				.desc("language options").build();
		options.addOption(option);

		// MKDOCS_OPTION Option Added
		option = Option.builder().longOpt(ControllerOptions.MKDOCS_OPTION).hasArg(false).desc("mkdocs tasks flag")
				.build();
		options.addOption(option);

		// PANDOC_OPTION Option Added
		option = Option.builder().longOpt(ControllerOptions.PANDOC_OPTION).hasArg(false).desc("pandoc tasks flag")
				.build();
		options.addOption(option);

		// FILE_OPTION Option Added
		option = Option.builder().longOpt(ControllerOptions.FILE_OPTION)
				.argName("file input path> < (optional) file output path").hasArgs()
				.desc("file input and output option for required tasks").build();
		options.addOption(option);

		// FOLDER_OPTION Option Added
		option = Option.builder().longOpt(ControllerOptions.FOLDER_OPTION)
				.argName("folder input path> < (optional) folder output path").hasArgs()
				.desc("folder input and output option for required tasks").build();
		options.addOption(option);

		return options;
	}

	/**
	 * Display the help information about the command-line options.
	 *
	 * @param options [input] The input options to be displayed in the help message.
	 */
	private static void displayHelp(Options options) {
		// display help
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("MARKDOWN MERGER", options);
	}

//	@SuppressWarnings("unused")
//	/**
//	 * This need paid service for this reason we parked this solution.
//	 */
//	private static void OpenAiApiUsageExample() {
//		//////////////////////////////////////////////////////////////////////////
//
////		https://openai.com/blog/introducing-chatgpt-and-whisper-apis
////		https://platform.openai.com/docs/models/gpt-4
////		https://platform.openai.com/docs/libraries/community-libraries
////		https://github.com/TheoKanning/openai-java
////		https://github.com/TheoKanning/openai-java/blob/main/example/src/main/java/example/OpenAiApiExample.java
////		https://platform.openai.com/account/billing/overview
////		https://mvnrepository.com/artifact/com.theokanning.openai-gpt3-java/service/0.12.0
//
//		String token = System.getenv("OPENAI_TOKEN");
//		OpenAiService service = new OpenAiService(token);
//
//		System.out.println("\nCreating completion...");
//		CompletionRequest completionRequest = CompletionRequest.builder().model("ada")
//				.prompt("Merhaba Dünyayı İngilizceye Çevirir misin?").echo(true).user("testing").n(3).build();
//		service.createCompletion(completionRequest).getChoices().forEach(System.out::println);
//
//		System.out.println("\nCreating Image...");
//		CreateImageRequest request = CreateImageRequest.builder().prompt("A cow breakdancing with a turtle").build();
//
//		System.out.println("\nImage is located at:");
//		System.out.println(service.createImage(request).getData().get(0).getUrl());
//
//		System.out.println("Streaming chat completion...");
//		final List<ChatMessage> messages = new ArrayList<>();
//		final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(),
//				"You are a dog and will speak as such.");
//		messages.add(systemMessage);
//		ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder().model("gpt-3.5-turbo")
//				.messages(messages).n(1).maxTokens(50).logitBias(new HashMap<>()).build();
//
//		service.streamChatCompletion(chatCompletionRequest).doOnError(Throwable::printStackTrace)
//				.blockingForEach(System.out::println);
//
//		service.shutdownExecutor();
//
//		/////////////////////////////////////////////////////////////////////////
//	}

}
