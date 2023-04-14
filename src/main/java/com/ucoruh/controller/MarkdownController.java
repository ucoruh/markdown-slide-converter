package com.ucoruh.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.batik.transcoder.TranscoderException;

import com.ucoruh.option.ControllerOptions;
import com.ucoruh.utils.Utils;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @class MarkdownController
 * @brief A controller class for managing Markdown files. This class provides
 *        functionality for clean, merge, deploy and generate of content.
 */
public class MarkdownController {

	/**
	 * 
	 * @brief A logger object used for logging messages in the MarkdownController
	 *        class.
	 */
	private final static Logger LOGGER = Logger.getLogger(MarkdownController.class.getName());

	/**
	 * 
	 * @brief A regular expression used to find the last index in a string
	 *        containing parentheses and digits.
	 */
	private static final String FIND_ALL_LAST_INDEXES = "\\((\\d+)\\)[^()]*$";

	/**
	 * 
	 * @brief The similarity threshold used when comparing strings for similarity.
	 */
	private static final double SIMILARITY_THRESHOLD = 0.999;

	/**
	 * 
	 * @brief The separator used to separate pages in a multi-page Markdown
	 *        document.
	 */
	private static final String PAGE_SEPERATOR = "---";

	/**
	 * 
	 * @brief The character used to indicate the start of a Markdown header.
	 */
	private static final String HEADER_CHAR = "#";

	/**
	 * 
	 * @brief The prefix used for image links in a Markdown document.
	 */
	private static final String IMAGE_LINK_PREFIX = "![";

	/**
	 * 
	 * Getter and setter for the command-line options used by the controller.
	 */
	@Getter
	@Setter
	private ControllerOptions options;

	/**
	 * 
	 * @brief The MarkdownController class manages the conversion of markdown text
	 *        into HTML, PDF, DOCX and also merge slides into single page. This
	 *        class provides a simple interface for converting markdown text
	 */
	public MarkdownController() {
		// Constructor code goes here
	}

	/**
	 * 
	 * Cleans generated files from Markdown.
	 * 
	 * @return {@code true} if the clean operation completed successfully,
	 *         {@code false} otherwise.
	 * 
	 * @throws IOException If there is an error accessing the files.
	 */
	public boolean clean() throws IOException {

		boolean result = false;

		LOGGER.info("Starting clean operation...");

		if (options.isFileSet()) {
			LOGGER.info("Cleaning selected file outputs...");
			result = cleanSelectedFileOutputs(options.getFileInputPath());
		}

		if (options.isFolderSet()) {
			LOGGER.info("Cleaning selected folder outputs...");
			result = cleanSelectedFolderOutputs(options.getFolderInputPath());
		}

		if (result) {
			LOGGER.info("Clean operation completed successfully.");
		} else {
			LOGGER.severe("Clean operation failed!");
		}

		return result;

	}

	/**
	 * 
	 * Cleans the outputs of all Markdown files within the specified folder.
	 * 
	 * @param inputFolderPath the path of the folder to clean the outputs of
	 * 
	 * @throws IOException if an I/O error occurs while cleaning outputs
	 * 
	 * @return true if all outputs were cleaned successfully, false otherwise
	 */
	private boolean cleanSelectedFolderOutputs(String inputFolderPath) throws IOException {

		/**
		 * Logs that the folder outputs are being cleaned.
		 */
		LOGGER.info("Cleaning folder outputs at " + inputFolderPath);
		boolean result = true;

		// Find all Markdown files within the input folder
		String[] extensions = { "md" };
		List<String> files = Utils.findFiles(Paths.get(inputFolderPath), extensions);

		// Clean each file within the folder
		for (String file : files) {
			boolean returnVal = cleanSelectedFileOutputs(file);
			if (result) {
				result = returnVal;
			}
		}

		// Log the results of the cleaning
		if (result) {
			LOGGER.info("Folder outputs cleaned successfully.");
		} else {
			LOGGER.severe("Folder outputs cleaning failed.");
		}

		return result;
	}

	/**
	 * 
	 * Deletes generated files associated with the specified input file.
	 * 
	 * @param inputFilePath The path of the input file
	 * 
	 * @return true if all outputs were cleaned successfully, false otherwise
	 */
	private boolean cleanSelectedFileOutputs(String inputFilePath) {

		LOGGER.info("Cleaning Selected File Outputs if exists" + inputFilePath);

		File inputFile = new File(inputFilePath);

		String inputFileAbsolutePath = inputFile.getAbsolutePath();
		String pdfFilePath = Utils.generateFilePath(inputFileAbsolutePath, "", "_doc", "pdf", false);
		String docxFilePath = Utils.generateFilePath(inputFileAbsolutePath, "", "_word", "docx", false);
		String slidePdfFilePath = Utils.generateFilePath(inputFileAbsolutePath, "", "_slide", "pdf", false);
		String slideHtmlFilePath = Utils.generateFilePath(inputFileAbsolutePath, "", "_slide", "html", false);
		String pptxFilePath = Utils.generateFilePath(inputFileAbsolutePath, "", "_slide", "pptx", false);
		String wordPptxFilePath = Utils.generateFilePath(inputFileAbsolutePath, "", "_word", "pptx", false);

		File pdfFile = new File(pdfFilePath);
		File docxFile = new File(docxFilePath);
		File slidePdfFile = new File(slidePdfFilePath);
		File slideHtmlFile = new File(slideHtmlFilePath);
		File pptxFile = new File(pptxFilePath);
		File wordPptxFile = new File(wordPptxFilePath);

		boolean failure = false;
		boolean isDeleted = false;
		boolean exist = false;

		if (pdfFile.exists()) {
			exist = true;
			isDeleted = pdfFile.delete();
			if (isDeleted) {
				LOGGER.info(pdfFilePath + " deleted successfully.");
			} else {
				LOGGER.severe("Failed to delete " + pdfFilePath);
				failure = true;
			}
		}

		if (docxFile.exists()) {
			exist = true;
			isDeleted = docxFile.delete();
			if (isDeleted) {
				LOGGER.info(docxFilePath + " deleted successfully.");
			} else {
				LOGGER.severe("Failed to delete " + docxFilePath);
				failure = true;
			}
		}

		if (slidePdfFile.exists()) {
			exist = true;
			isDeleted = slidePdfFile.delete();
			if (isDeleted) {
				LOGGER.info(slidePdfFilePath + " deleted successfully.");
			} else {
				LOGGER.severe("Failed to delete " + slidePdfFilePath);
				failure = true;
			}
		}

		if (slideHtmlFile.exists()) {
			exist = true;
			isDeleted = slideHtmlFile.delete();
			if (isDeleted) {
				LOGGER.info(slideHtmlFilePath + " deleted successfully.");
			} else {
				LOGGER.severe("Failed to delete " + slideHtmlFilePath);
				failure = true;
			}
		}

		if (pptxFile.exists()) {
			exist = true;
			isDeleted = pptxFile.delete();
			if (isDeleted) {
				LOGGER.info(pptxFilePath + " deleted successfully.");
			} else {
				LOGGER.severe("Failed to delete " + pptxFilePath);
				failure = true;
			}
		}

		if (wordPptxFile.exists()) {
			exist = true;
			isDeleted = wordPptxFile.delete();
			if (isDeleted) {
				LOGGER.info(wordPptxFilePath + " deleted successfully.");
			} else {
				LOGGER.severe("Failed to delete " + wordPptxFilePath);
				failure = true;
			}
		}

		if (exist) {
			if (failure) {
				LOGGER.severe("Selected File Outputs Cleaning Failed!");
			} else {
				LOGGER.info("Selected File Outputs Cleaned Successfully");
			}
		} else {
			LOGGER.info("Selected File Outputs Not Found");
		}

		return !failure;

	}

	/**
	 * 
	 * Merges the files needed for Mkdocs and Pandoc.
	 * 
	 * @throws IOException         If an I/O error occurs while attempting to merge
	 *                             files.
	 * 
	 * @throws TranscoderException If a transcoding error occurs while attempting to
	 *                             merge SVG files.
	 * 
	 * @return true if all files were successfully merged, false otherwise
	 */
	public boolean mergePages() throws IOException, TranscoderException {

		LOGGER.info("Merge Pages operation starting...");

		boolean result = true;

		if (options.isFileSet()) {
			LOGGER.info("Merging selected file...");
			result = mergeSelectedFile(options.getFileInputPath(), options.getFileOutputPath());
		}

		if (!result) {
			LOGGER.severe("Selected File Merge Operation Failed, Terminating Operation!!");
			return result;

		}

		if (options.isFolderSet()) {
			LOGGER.info("Merging selected folder files...");
			result = mergeSelectedFolderFiles();
		}

		if (!result) {
			LOGGER.severe("Selected Folder Merge Operation Failed, Terminating Operation!!");
			return result;

		}

		if (options.isRebuildIsSet()) {
			LOGGER.info("Rebuild Set, Cleaning generated files...");
			result = clean();

			if (!result) {
				LOGGER.severe("ReBuild Clean Operation Failed, Terminating Operation!!");
				return result;
			}
		}

		if (options.isBuildIsSet() || options.isRebuildIsSet()) {
			LOGGER.info("Building pages...");
			result = buildPages();

			if (!result) {
				LOGGER.severe("Build Operation Failed, Terminating Operation!!");
				return result;
			} else {
				LOGGER.info("Merge Operation with Build Successfully Completed");
			}

		} else {

			LOGGER.info("Merge Pages operation completed successfully.");
		}

		return result;

	}

	/**
	 * 
	 * Merges all Markdown files within the specified folder.
	 * 
	 * @throws IOException if an I/O error occurs while attempting to merge the
	 *                     files
	 * 
	 * @return true if all files were successfully merged, false otherwise
	 */
	private boolean mergeSelectedFolderFiles() throws IOException {

		LOGGER.info("Merging Selected Folder Files");

		boolean result = true;

		// Find all Markdown files within the input folder
		String[] extensions = { "md" };
		List<String> files = Utils.findFiles(Paths.get(options.getFolderInputPath()), extensions);

		// Clean each file within the folder
		for (String file : files) {

			File f = new File(file);
			if (!f.getName().startsWith(ControllerOptions.MKDOCS_WEB_PREFIX)) {
				try {
					boolean returnVal = mergeSelectedFile(file, null);
					if (result) {
						result = returnVal;
					}
					LOGGER.info("Merged file: " + file);
				} catch (IOException | TranscoderException e) {
					LOGGER.log(Level.SEVERE, "Merge Folder Operation Failed", e);
				}
			}
		}

		// Log cleaning results
		if (result) {
			LOGGER.info("Folder files merged successfully.");
		} else {
			LOGGER.severe("Folder files merge failed");
		}

		return result;

	}

	/**
	 * 
	 * Merges the input Markdown file into three different formats: mkdocs-web,
	 * pandoc-pdf, and pandoc-slide.
	 * 
	 * Any lines starting with "<!-- _backgroundColor:", "<!-- _color:", and "<!--
	 * paginate:" are excluded.
	 * 
	 * Lines starting with MarkdownController.HEADER_CHAR are checked for similarity
	 * and duplicates are removed.
	 * 
	 * Lines starting with MarkdownController.IMAGE_LINK_PREFIX are converted to
	 * pandoc format.
	 * 
	 * @param fileInputPath  The path of the input Markdown file
	 * 
	 * @param fileOutputPath The path of the output file
	 * 
	 * @throws IOException         If there is an error reading or writing the file.
	 * 
	 * @throws TranscoderException If there is an error converting the image format.
	 * 
	 * @return true if the file was successfully merged, false otherwise
	 */
	private boolean mergeSelectedFile(String fileInputPath, String fileOutputPath)
			throws IOException, TranscoderException {

		if (Utils.isIgnoredFile(fileInputPath)) {
			return true;
		}

		ArrayList<String> markdownlinesMarp = new ArrayList<String>();
		ArrayList<Integer> markdownExludedlines = new ArrayList<Integer>();

		ArrayList<String> markdownlinesMkdocsWeb = new ArrayList<String>();
		ArrayList<String> markdownlinesPandocPdf = new ArrayList<String>();
		ArrayList<String> markdownlinesPandocSlide = new ArrayList<String>();

		int lastSeperatorIndex = 0;
		int lastTitleIndex = 0;

		// Read the input Markdown file
		BufferedReader reader = new BufferedReader(new FileReader(fileInputPath));

		String line;

		while ((line = reader.readLine()) != null) {
			markdownlinesMarp.add(line);
		}
		reader.close();

		boolean repeatedItems = true;

		while (repeatedItems) {

			int counter = 0;
			boolean seperator = false;

			for (int i = 0; i < markdownlinesMarp.size(); i++) {

				if (markdownlinesMarp.get(i).contains("<!-- _backgroundColor:")
						|| markdownlinesMarp.get(i).contains("<!-- _color:")
						|| markdownlinesMarp.get(i).contains("<!-- paginate:")) {
					if (!markdownExludedlines.contains(i)) {
						markdownExludedlines.add(i);
					}
				}

				seperator = false;
				if (markdownlinesMarp.get(i).trim().toLowerCase().equals(MarkdownController.PAGE_SEPERATOR)) {
					seperator = true;
					counter++;
				}

				// first two seperator for configuration
				if (counter > 2 && seperator) {
					markdownExludedlines.add(i);
					lastSeperatorIndex = i;
				}

				if (markdownlinesMarp.get(i).trim().toLowerCase().startsWith(MarkdownController.HEADER_CHAR)) {
					if (lastSeperatorIndex > 0) {

						String prev = markdownlinesMarp.get(lastTitleIndex).replace(MarkdownController.HEADER_CHAR, "")
								.toLowerCase().trim().replaceFirst(FIND_ALL_LAST_INDEXES, "");
						String curr = markdownlinesMarp.get(i).replace(MarkdownController.HEADER_CHAR, "").toLowerCase()
								.trim().replaceFirst(FIND_ALL_LAST_INDEXES, "");

						double similarity = Utils.calculateJaccardSimilarity(prev, curr);
						if (similarity > SIMILARITY_THRESHOLD) {
							markdownExludedlines.add(i);
						}
					}

					lastTitleIndex = i;
				}

				repeatedItems = false;
				for (int j = lastTitleIndex + 1; j < markdownlinesMarp.size(); j++) {
					if (markdownlinesMarp.get(j).trim().toLowerCase().startsWith(MarkdownController.HEADER_CHAR)) {
						String prev = markdownlinesMarp.get(lastTitleIndex).replace(MarkdownController.HEADER_CHAR, "")
								.toLowerCase().trim().replaceFirst(FIND_ALL_LAST_INDEXES, "");
						String curr = markdownlinesMarp.get(j).replace(MarkdownController.HEADER_CHAR, "").toLowerCase()
								.trim().replaceFirst(FIND_ALL_LAST_INDEXES, "");
						double similarity = Utils.calculateJaccardSimilarity(prev, curr);
						if (similarity >= SIMILARITY_THRESHOLD) {
							repeatedItems = true;
							break;
						}
					}
				}
			}

			if (!repeatedItems)
				break;

		}

		for (int i = 0; i < markdownlinesMarp.size(); i++) {

			String markdownline = markdownlinesMarp.get(i);

			boolean converted = false;

			if (!markdownExludedlines.contains(i)) {

				if (markdownline.trim().startsWith(MarkdownController.HEADER_CHAR)) {
					markdownline = markdownline.replaceFirst(FIND_ALL_LAST_INDEXES, "");
				}

				markdownlinesMkdocsWeb.add(markdownline);

				if (markdownline.trim().startsWith(MarkdownController.IMAGE_LINK_PREFIX)) {
					String[] result;
					result = Utils.convertMarpToPandoc(markdownline);
					markdownline = result[0];

					converted = true;
				}

				markdownlinesPandocPdf.add(markdownline);

				// outputBuilder.append(markdownline).append(System.lineSeparator());
			}

			if (converted == false) {
				if (markdownline.trim().startsWith(MarkdownController.IMAGE_LINK_PREFIX)) {
					String[] result;
					result = Utils.convertMarpToPandoc(markdownline);
					markdownline = result[0];

				}
			}

			markdownlinesPandocSlide.add(markdownline);
		}

		StringBuilder outputBuilder = new StringBuilder();
		for (int i = 0; i < markdownlinesMkdocsWeb.size(); i++) {
			String markdownline = markdownlinesMkdocsWeb.get(i);
			outputBuilder.append(markdownline).append(System.lineSeparator());
		}

		String outputMarkdownMkdocsWeb = outputBuilder.toString();

		outputBuilder = new StringBuilder();
		for (int i = 0; i < markdownlinesPandocPdf.size(); i++) {
			String markdownline = markdownlinesPandocPdf.get(i);
			outputBuilder.append(markdownline).append(System.lineSeparator());
		}

		String outputMarkdownPandocPdf = outputBuilder.toString();

		outputBuilder = new StringBuilder();
		for (int i = 0; i < markdownlinesPandocSlide.size(); i++) {
			String markdownline = markdownlinesPandocSlide.get(i);
			outputBuilder.append(markdownline).append(System.lineSeparator());
		}

		String outputMarkdownPandocSlide = outputBuilder.toString();

		// Generate Output File Name with Path

		String outputFile = Utils.generateFilePath(fileInputPath, ControllerOptions.MKDOCS_WEB_PREFIX, "", "", false);
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8);
		osw.write(outputMarkdownMkdocsWeb);
		osw.close();

		outputFile = Utils.generateFilePath(fileInputPath, ControllerOptions.PANDOCS_DOC_PREFIX, "", "", false);
		osw = new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8);
		osw.write(outputMarkdownPandocPdf);
		osw.close();

		outputFile = Utils.generateFilePath(fileInputPath, ControllerOptions.PANDOCS_PPT_PREFIX, "", "", false);
		osw = new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8);
		osw.write(outputMarkdownPandocSlide);
		osw.close();

		return true;

	}

	/**
	 * 
	 * Generates default pages with configuration.
	 * 
	 * This method generates default pages with configuration. If a folder is set in
	 * the options, it processes that folder and generates default pages for it.
	 * 
	 * @pre The options object must be initialized.
	 * 
	 * @post Default pages are generated for the configured folder, if any.
	 * 
	 * @return true if default pages were generated successfully, false otherwise.
	 */
	public boolean generatePages() {

		LOGGER.info("Generating default pages");

		boolean result = false;

		// Generate default pages with configuration
		if (options.isFolderSet()) {
			// process folder

			LOGGER.info("Processing folder: " + options.getFolderInputPath());

			result = generateDefaultPages(options.getFolderInputPath());
		}

		// Log cleaning results
		if (result) {
			LOGGER.info("Default pages generated successfully.");
		} else {
			LOGGER.severe("Default pages generation failed");
		}

		return result;
	}

	/**
	 * 
	 * Generates default pages for the given folder path.
	 * 
	 * @param folderInputPath The folder path for which to generate default pages.
	 * 
	 * @return true if default pages were generated successfully, false otherwise.
	 */
	private boolean generateDefaultPages(String folderInputPath) {

		// TODO: Generate YAML Configuration with Navigation
		// TODO: Generate Required Files
		// TODO: Generate Default MARP markdown files
		// TODO : Run in with Merge command and Build option...
		return false;
	}

	/**
	 * 
	 * Builds pages with Marp and Pandoc.
	 * 
	 * @return A boolean indicating whether the pages were successfully built or
	 *         not.
	 * 
	 * @throws IOException If an I/O error occurs while building pages.
	 */
	public boolean buildPages() throws IOException {

		boolean result = true;

		if (!options.isFileSet() && !options.isFolderSet()) {
			LOGGER.severe("There is no file or folder path configured for build operations, Terminating Operation");
			return false;
		}

		LOGGER.info("Building pages...");

		if (options.isFileSet()) {

			LOGGER.info("Building Selected File...");
			// process file
			result = buildSelectedFile(options.getFileInputPath());

			if (!result) {
				LOGGER.severe("Build Selected File Failed, Terminating Operation");
				return result;
			}

			LOGGER.info("Building Selected File Successfully Completed!!");

		}

		if (options.isFolderSet()) {

			LOGGER.info("Building Selected Folder Files...");
			// process folder
			result = buildSelectedFolderFiles(options.getFolderInputPath());
			if (!result) {
				LOGGER.severe("Build Selected Folder Files Failed, Terminating Operation");
				return result;
			}

			LOGGER.info("Building Selected Folder Files Successfully Completed!!");
		}

		LOGGER.info("Building Operation Completed with Success!!");

		return result;
	}

	/**
	 * 
	 * Builds all Markdown files within the given input folder path.
	 * 
	 * @param inputFolderPath The path of the folder containing Markdown files to be
	 *                        built.
	 * 
	 * @throws IOException If an I/O error occurs while reading or writing files.
	 * 
	 * @return true if all files are successfully built, false otherwise.
	 */
	private boolean buildSelectedFolderFiles(String inputFolderPath) throws IOException {

		LOGGER.info("Building Selected Folder Files");

		boolean result = true;

		String[] extensions = { "md" };

		List<String> files = Utils.findFiles(Paths.get(inputFolderPath), extensions);

		// Clean each file within the folder
		for (String file : files) {
			boolean returnVal = buildSelectedFile(file);
			if (!returnVal) {
				LOGGER.severe(file + " Building Failed!!");
				result = returnVal;
			} else {
				LOGGER.info(file + " Building Success..");
			}
		}

		// Log cleaning results
		if (result) {
			LOGGER.info("Building files completed successfully.");
		} else {
			LOGGER.severe("Building files failed");
		}

		return result;

	}

	/**
	 * 
	 * Builds the selected file using either Pandoc or Marp depending on the file
	 * type.
	 * 
	 * @param inputFilePath The path to the input file to be built.
	 * 
	 * @return Returns true if the file is built successfully, false otherwise.
	 */
	private boolean buildSelectedFile(String inputFilePath) {

		boolean concatenate = true;
		String pandocExec = "cmd /c start cmd.exe /c pandoc";
		String marpExec = "cmd /c start cmd.exe /c marp";

		File f = new File(inputFilePath);

		String inputFile = f.getAbsolutePath();
		String inputFileFolder = f.getParent();
		String outputFile = "";

		if (Utils.isIgnoredFile(inputFilePath)) {
			LOGGER.info("Build Ignored File : " + inputFilePath);
			return true;
		}

		// MARP HTML, SLIDE
		if (!f.getName().startsWith(ControllerOptions.MKDOCS_WEB_PREFIX)
				&& !f.getName().startsWith(ControllerOptions.PANDOCS_DOC_PREFIX)
				&& !f.getName().startsWith(ControllerOptions.PANDOCS_PPT_PREFIX)) {

			LOGGER.info("Building Marp Presentation PDF");

			LOGGER.info("Input File : " + inputFile);

			outputFile = Utils.generateFilePath(inputFile, "", "_slide", "pdf", true);

			LOGGER.info("Output File : " + outputFile);

			String[] marpSlidePdfCmd = { marpExec, inputFile, "--html", "--pdf", "-o", outputFile,
					"--allow-local-files" };

			LOGGER.info("Command : " + Utils.toString(marpSlidePdfCmd, concatenate));

			Utils.executeCommandThread(marpSlidePdfCmd, inputFileFolder, false);

			LOGGER.info("Building Marp Presentation HTML");

			LOGGER.info("Input File : " + inputFile);

			outputFile = Utils.generateFilePath(inputFile, "", "_slide", "html", true);

			LOGGER.info("Output File : " + outputFile);

			String[] marpSlideHtmlCmd = { marpExec, inputFile, "--html", "-o", outputFile, "--allow-local-files" };

			LOGGER.info("Command : " + Utils.toString(marpSlideHtmlCmd, concatenate));

			Utils.executeCommandThread(marpSlideHtmlCmd, inputFileFolder, false);

			LOGGER.info("Building Marp Presentation PPTX");

			LOGGER.info("Input File : " + inputFile);

			outputFile = Utils.generateFilePath(inputFile, "", "_slide", "pptx", true);

			LOGGER.info("Output File : " + outputFile);

			String[] marpSlidePptxCmd = { marpExec, inputFile, "--pptx", "-o", outputFile, "--allow-local-files" };

			LOGGER.info("Command : " + Utils.toString(marpSlidePptxCmd, concatenate));

			Utils.executeCommandThread(marpSlidePptxCmd, inputFileFolder, true);

		}

		// PANDOC SLIDE
		if (f.getName().startsWith(ControllerOptions.PANDOCS_PPT_PREFIX)) {

			LOGGER.info("Building Pandoc Presentation PPTX");

			LOGGER.info("Input File : " + inputFile);

			outputFile = Utils.generateFilePath(inputFile, "", "_word", "pptx", true);

			LOGGER.info("Output File : " + outputFile);

			String[] pandocDocPptxCmd = { pandocExec, "--reference-doc=custom-reference.pptx", "-o", outputFile, "-f",
					"markdown", "-t", "pptx", inputFile };

			LOGGER.info("Command : " + Utils.toString(pandocDocPptxCmd, concatenate));

			Utils.executeCommandThread(pandocDocPptxCmd, inputFileFolder, true);
		}

		// PANDOC DOC
		if (f.getName().startsWith(ControllerOptions.PANDOCS_DOC_PREFIX)) {

			LOGGER.info("Building Pandoc Document PDF");

			LOGGER.info("Input File : " + inputFile);

			outputFile = Utils.generateFilePath(inputFile, "", "_doc", "pdf", true);

			LOGGER.info("Output File : " + outputFile);

			String[] pandocDocPdfCmd = { pandocExec, inputFile, "--pdf-engine=xelatex", "-f",
					"markdown-implicit_figures", "-V", "colorlinks", "-V", "urlcolor=NavyBlue", "-V", "toccolor=Red",
					"--toc", "-N", "-o", outputFile };

			LOGGER.info("Command : " + Utils.toString(pandocDocPdfCmd, concatenate));

			Utils.executeCommandThread(pandocDocPdfCmd, inputFileFolder, false);

			LOGGER.info("Building Pandoc Document DOCX");

			LOGGER.info("Input File : " + inputFile);

			outputFile = Utils.generateFilePath(inputFile, "", "_word", "docx", true);

			LOGGER.info("Output File : " + outputFile);

			String[] pandocDocDocxCmd = { pandocExec, "-o", outputFile, "-f", "markdown", "-t", "docx", inputFile };

			LOGGER.info("Command : " + Utils.toString(pandocDocDocxCmd, concatenate));

			Utils.executeCommandThread(pandocDocDocxCmd, inputFileFolder, true);
		}

		return true;

	}

	/**
	 * 
	 * Deploy the pages to GitHub Pages using the MkDocs gh-deploy command.
	 * 
	 * @return Returns true if the deployment was successful, false otherwise.
	 */
	public boolean deployPages() {

		boolean result = true;

		if (options.isFolderSet()) {
			result = deploySelectedFolder(options.getFolderInputPath());
		} else {
			LOGGER.severe("Deployment Configuration File Not Set, Terminating Operation");
			return false;
		}

		if (result) {
			LOGGER.info("Deployment to GitHub Pages completed successfully.");
		} else {
			LOGGER.severe("Deployment to GitHub Pages failed!");
		}

		return result;
	}

	/**
	 * 
	 * Deploy the selected folder to GitHub Pages using the MkDocs gh-deploy
	 * command.
	 * 
	 * @param folderInputPath The path of the folder to deploy.
	 * 
	 * @return Returns true if the deployment was successful, false otherwise.
	 */
	private boolean deploySelectedFolder(String folderInputPath) {

		String[] pandocDocPptxCmd = { "cmd /c start cmd.exe /c", "mkdocs", "gh-deploy", "--force" };

		Utils.executeCommandThread(pandocDocPptxCmd, folderInputPath, false);
		LOGGER.info("Folder " + folderInputPath + " successfully deployed to GitHub Pages.");

		return true;

	}

	/**
	 * Export Draw.IO images from all .drawio files found in the specified folder
	 * path.
	 * 
	 * @throws IOException        if there is an I/O error when reading or writing
	 *                            the files
	 * 
	 * @throws URISyntaxException if there is a syntax error in the specified folder
	 *                            path URI
	 * 
	 * @return true if the image export was successful for all files, false
	 *         otherwise
	 */
	public boolean drawioExportImages() throws IOException, URISyntaxException {

		boolean result = true;

		// Find all .drawio files in the specified folder path
		String[] extensions = { "drawio" };
		List<String> files = Utils.findFiles(Paths.get(options.getFolderInputPath()), extensions);

		// Export images for each .drawio file found
		for (String file : files) {

			LOGGER.info(file + " Drawio Exporting...");

			try {
				result = Utils.exportDrawioImages(file);
				if (!result) {
					LOGGER.severe(file + " Drawio Export Failed, Terminating Operaiton!!!");
					return result;
				}
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, file + " Drawio Export Failed, Terminating Operaiton!!!", e);
				return false;
			}
		}

		LOGGER.info("Draw.IO image export successful for all files.");

		return true;
	}

}
