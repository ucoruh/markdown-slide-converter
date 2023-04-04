package com.ucoruh.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ucoruh.utils.Utils;

public class MarkdownController {

	private final static Logger LOGGER = Logger.getLogger(MarkdownController.class.getName());

	// If output file not present, this postfix appened end of file ****_single.md
	public static final String MERGED_FILE_PREFIX_MKDOCS = "mkdocs_";
	public static final String MERGED_FILE_PREFIX_PANDOC = "pandoc_";

//	public static final String DELETE_MARK = "@-DELETE-@";

	private static final String FIND_ALL_LAST_INDEXES = "\\((\\d+)\\)[^()]*$";

	private double similarityThreshold = 0.999;

	private String inputFolderPath = null;

	private String inputFilePath = null;

	private String outputFilePath = null;

	/**
	 * Constructor
	 */
	public MarkdownController() {

	}

	/**
	 * 
	 * @return
	 */
	public String getInputFolderPath() {
		return inputFolderPath;
	}

	/**
	 * 
	 * @param inputFolderPath
	 */
	public void setInputFolderPath(String inputFolderPath) {
		this.inputFolderPath = inputFolderPath;
	}

	/**
	 * Return input file
	 * 
	 * @return input file path
	 */
	public String getInputFilePath() {
		return inputFilePath;
	}

	/**
	 * Set input file
	 * 
	 * @param inputFilePath [input] input file
	 */
	public void setInputFilePath(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}

	/**
	 * Return output file path
	 * 
	 * @return output file path
	 */
	public String getOutputFilePath() {
		return outputFilePath;
	}

	/**
	 * Set output file path
	 * 
	 * @param outputFilePath [input] output file path
	 */
	public void setOutputFilePath(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}

	/**
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	private double calculateJaccardSimilarity(String str1, String str2) {
		Set<Character> set1 = new HashSet<Character>();
		Set<Character> set2 = new HashSet<Character>();

		// add each character in both strings to their respective sets
		for (char c : str1.toCharArray()) {
			set1.add(c);
		}
		for (char c : str2.toCharArray()) {
			set2.add(c);
		}

		// calculate the intersection and union of the two sets
		Set<Character> intersection = new HashSet<Character>(set1);
		intersection.retainAll(set2);
		Set<Character> union = new HashSet<Character>(set1);
		union.addAll(set2);

		// calculate the Jaccard Similarity as the ratio of intersection to union
		return (double) intersection.size() / union.size();
	}

	/**
	 * 
	 * @throws IOException
	 */
	public void mergeSingleFile() throws IOException {
		mergePages(this.inputFilePath, this.outputFilePath);
	}

	/**
	 * 
	 * @throws IOException
	 */
	public void mergeFolder() throws IOException {

		String[] extensions = { "md" };
		List<String> files = Utils.findFiles(Paths.get(this.inputFolderPath), extensions);
		files.forEach(x -> {
			try {

				File f = new File(x);
				if (!f.getName().startsWith(MERGED_FILE_PREFIX_MKDOCS)) {
					mergePages(x, null);
				}

			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Merge File Error", e);
			}
		});

	}

	/**
	 * 
	 * @throws IOException
	 */
	public void buildFolder() throws IOException {

		String[] extensions = { "md" };
		List<String> files = Utils.findFiles(Paths.get(this.inputFolderPath), extensions);
		files.forEach(x -> {
			File f = new File(x);
			if (f.getName().startsWith(MERGED_FILE_PREFIX_MKDOCS)) {
				// this is single page this can be docx or pdf

				String inputFile = f.getAbsolutePath();
				String outputFile = Utils.filePathWithPostFixOrPrefix(inputFile, "", "_doc", "pdf");

				String[] pandocDocPdfCmd = {"cmd /c start cmd.exe /c pandoc", inputFile, "--pdf-engine=xelatex", "-f",
						"markdown-implicit_figures", "-V", "colorlinks", "-V", "urlcolor=NavyBlue", "-V",
						"toccolor=Red", "--toc", "-N", "-o", outputFile };

				LOGGER.info("Building Pandoc PDF");

				LOGGER.info("Input File " + inputFile);
				LOGGER.info("Output File " + inputFile);

				Utils.executeCommandThread(pandocDocPdfCmd);

				outputFile = Utils.filePathWithPostFixOrPrefix(inputFile, "", "_word", "docx");

				String[] pandocDocDocxCmd = {"cmd /c start cmd.exe /c pandoc", "-o", outputFile, "-f", "markdown", "-t",
						"docx", inputFile };
				Utils.executeCommandThread(pandocDocDocxCmd);

			} else {
				// this is presentation

				String inputFile = f.getAbsolutePath();
				String outputFile = Utils.filePathWithPostFixOrPrefix(inputFile, "", "_slide", "pdf");

				String[] marpSlidePdfCmd = { "cmd /c start cmd.exe /c marp", inputFile, "--html", "--pdf", "-o", outputFile,
						"--allow-local-files" };
				Utils.executeCommandThread(marpSlidePdfCmd);

				outputFile = Utils.filePathWithPostFixOrPrefix(inputFile, "", "_slide", "html");

				String[] marpSlideHtmlCmd = { "cmd /c start cmd.exe /c marp", inputFile, "--html", "-o", outputFile,
						"--allow-local-files" };
				Utils.executeCommandThread(marpSlideHtmlCmd);

				outputFile = Utils.filePathWithPostFixOrPrefix(inputFile, "", "_slide", "pptx");

				String[] marpSlidePptxCmd = { "cmd /c start cmd.exe /c marp", inputFile, "--pptx", "-o", outputFile,
						"--allow-local-files" };

				Utils.executeCommandThread(marpSlidePptxCmd);

				outputFile = Utils.filePathWithPostFixOrPrefix(inputFile, "", "_word", "pptx");

				String[] pandocDocPptxCmd = {"cmd /c start cmd.exe /c pandoc", "-o", outputFile, "-f", "markdown", "-t",
						"pptx", inputFile };

				Utils.executeCommandThread(pandocDocPptxCmd);
			}
		});
	}

	/**
	 * 
	 * @param fileInputPath
	 * @param fileOutputPath
	 * @throws IOException
	 */
	private void mergePages(String fileInputPath, String fileOutputPath) throws IOException {

		ArrayList<String> markdownlines = new ArrayList<String>();
		ArrayList<Integer> markdownExludedlines = new ArrayList<Integer>();
		int lastSeperatorIndex = 0;
		int lastTitleIndex = 0;

		// Read the input Markdown file
		BufferedReader reader = new BufferedReader(new FileReader(fileInputPath));

		String line;

		while ((line = reader.readLine()) != null) {
			markdownlines.add(line);
		}
		reader.close();

		boolean repeatedItems = true;

		while (repeatedItems) {

			int counter = 0;
			boolean seperator = false;

			for (int i = 0; i < markdownlines.size(); i++) {
				seperator = false;
				if (markdownlines.get(i).trim().toLowerCase().equals("---")) {
					seperator = true;
					counter++;
				}

				// first two seperator for configuration
				if (counter > 2 && seperator) {
					markdownExludedlines.add(i);
					lastSeperatorIndex = i;
				}

				if (markdownlines.get(i).trim().toLowerCase().startsWith("#")) {
					if (lastSeperatorIndex > 0) {

						String prev = markdownlines.get(lastTitleIndex).replace("#", "").toLowerCase().trim()
								.replaceFirst(FIND_ALL_LAST_INDEXES, "");
						String curr = markdownlines.get(i).replace("#", "").toLowerCase().trim()
								.replaceFirst(FIND_ALL_LAST_INDEXES, "");

						double similarity = calculateJaccardSimilarity(prev, curr);
						if (similarity > similarityThreshold) {
							markdownExludedlines.add(i);
						}
					}

					lastTitleIndex = i;
				}

				repeatedItems = false;
				for (int j = lastTitleIndex + 1; j < markdownlines.size(); j++) {
					if (markdownlines.get(j).trim().toLowerCase().startsWith("#")) {
						String prev = markdownlines.get(lastTitleIndex).replace("#", "").toLowerCase().trim()
								.replaceFirst(FIND_ALL_LAST_INDEXES, "");
						String curr = markdownlines.get(j).replace("#", "").toLowerCase().trim()
								.replaceFirst(FIND_ALL_LAST_INDEXES, "");
						double similarity = calculateJaccardSimilarity(prev, curr);
						if (similarity >= similarityThreshold) {
							repeatedItems = true;
							break;
						}
					}
				}
			}

			if (!repeatedItems)
				break;

		}

		StringBuilder outputBuilder = new StringBuilder();
		for (int i = 0; i < markdownlines.size(); i++) {
			if (!markdownExludedlines.contains(i)) {

				String markdownline = markdownlines.get(i);

				if (markdownline.startsWith("#")) {
					markdownline = markdownline.replaceFirst(FIND_ALL_LAST_INDEXES, "");
				}

				outputBuilder.append(markdownline).append(System.lineSeparator());
			}
		}

		String outputMarkdown = outputBuilder.toString();

		// Generate Output File Name with Path
		FileWriter writer = null;

		String defaultFileOutputPath = Utils.filePathWithPostFixOrPrefix(fileInputPath, MERGED_FILE_PREFIX_MKDOCS, "", "");

		// if output file null then use prefix/postfix filename
		if (Utils.checkStringNullOrEmpty(outputFilePath)) {
			LOGGER.info("Default File Used [" + defaultFileOutputPath + "]");
			writer = new FileWriter(new File(defaultFileOutputPath));
		} else {
			LOGGER.info("Output File Used[" + outputFilePath + "]");
			writer = new FileWriter(new File(outputFilePath));
		}

		writer.write(outputMarkdown);
		writer.close();
	}

}
