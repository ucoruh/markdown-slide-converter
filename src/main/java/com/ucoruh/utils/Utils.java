package com.ucoruh.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.batik.apps.rasterizer.DestinationType;
import org.apache.batik.apps.rasterizer.SVGConverter;
import org.apache.batik.apps.rasterizer.SVGConverterException;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.sl.usermodel.PictureData.PictureType;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author ugur.coruh
 *
 */
public class Utils {

	// Logger JUL
	private final static Logger LOGGER = Logger.getLogger(Utils.class.getName());

	/**
	 * 
	 * @brief Calculates the Jaccard similarity between two strings. The Jaccard
	 *        similarity is a measure of similarity between two sets of elements,
	 *        defined as the ratio of the size of their intersection to the size of
	 *        their union. In this method, the two input strings are treated as sets
	 *        of characters, and their Jaccard similarity is calculated as the ratio
	 *        of the number of characters they have in common to the total number of
	 *        characters in both strings.
	 * @param str1 The first string to compare.
	 * @param str2 The second string to compare.
	 * @return The Jaccard similarity between the two strings, as a double value
	 *         between 0 and 1.
	 */
	public static double calculateJaccardSimilarity(String str1, String str2) {
		// Create sets of characters for each string
		Set<Character> set1 = new HashSet<Character>();
		Set<Character> set2 = new HashSet<Character>();
		// Add each character in both strings to their respective sets
		for (char c : str1.toCharArray()) {
			set1.add(c);
		}
		for (char c : str2.toCharArray()) {
			set2.add(c);
		}

		// Calculate the intersection and union of the two sets
		Set<Character> intersection = new HashSet<Character>(set1);
		intersection.retainAll(set2);
		Set<Character> union = new HashSet<Character>(set1);
		union.addAll(set2);

		// Calculate the Jaccard similarity as the ratio of intersection to union
		return (double) intersection.size() / union.size();
	}

	/**
	 * Checks if a given file path points to a file name that contains either
	 * "index.", "license.", or "tags.", and ends with the extension ".md".
	 *
	 * @param filePath The path to the file whose name should be checked.
	 * @return True if the file name matches the criteria, false otherwise.
	 */
	public static boolean isIgnoredFile(String filePath) {
		Path path = Paths.get(filePath);
		String filename = path.getFileName().toString();
		return (filename.contains("index.") || filename.contains("license.") || filename.contains("tags."))
				&& filename.endsWith(".md");
	}

	/**
	 * 
	 * Converts an array of objects to a string representation.
	 * 
	 * @param array       the array of objects to convert
	 * 
	 * @param concatenate a flag indicating whether to concatenate the elements
	 *                    without separating them or not
	 * 
	 * @return a string representation of the array
	 */
	public static String toString(Object[] array, Boolean concatenate) {
		if (array == null)
			return "null";

		int iMax = array.length - 1;
		if (iMax == -1) {
			if (concatenate) {
				return "";
			} else {
				return "[]";
			}
		}

		StringBuilder b = new StringBuilder();

		if (!concatenate) {
			b.append('[');
		}

		for (int i = 0;; i++) {
			b.append(String.valueOf(array[i]));
			if (i == iMax) {
				if (!concatenate) {
					return b.append(']').toString();
				} else {
					return b.toString();
				}
			}

			if (!concatenate) {
				b.append(", ");
			} else {
				b.append(" ");
			}

		}
	}

	/**
	 * 
	 * Converts an SVG file to a PDF file using Batik Rasterizer.
	 * 
	 * @param inputFilePath  the path to the input SVG file
	 * 
	 * @param outputFilePath the path to the output PDF file
	 * @throws SVGConverterException
	 */
	public static void convertSvgToPngBatikRasterizer(String inputFilePath, String outputFilePath)
			throws SVGConverterException {

		SVGConverter rasterizer = new SVGConverter();

		createFoldersForFilePathIfNotExist(outputFilePath);

		rasterizer.setBackgroundColor(Color.WHITE);
		rasterizer.setDestinationType(DestinationType.PNG);
		rasterizer.setExecuteOnload(true);
		rasterizer.setQuality(0.95f);
		rasterizer.setSources(new String[] { inputFilePath });
		rasterizer.setDst(new File(outputFilePath));
		rasterizer.execute();

	}

	/**
	 * Export images (SVG, PNG, JPEG) for each page in a draw.io file using the
	 * draw.io desktop application.
	 *
	 * @param inputFilePath The path of the input draw.io file to be converted.
	 * @throws Exception If any error occurs while executing the command.
	 */
	public static boolean exportDrawioImages(String inputFilePath) throws Exception {

		boolean concatenate = true;

		////////// GET DRAWIO from PATH //////////////////////////
		// check drawio installed
		String drawio = "\"" + System.getenv("DRAWIO_PATH") + "\"";
//		String drawio = "\"C:\\Program Files\\draw.io\\draw.io.exe\"";
//      install with "choco install drawio -y"		
		//////////////////////////////////////////////////////////

		///////////// EXPORT to XML ////////////////////////////////
		String[] cmdxml = { drawio, "--export", "--format", "xml", "--uncompressed", inputFilePath };

		LOGGER.info("Converting Drawio to XML " + inputFilePath);
		LOGGER.info("Running Command " + Utils.toString(cmdxml, concatenate));

		Utils.executeCommandThread(cmdxml, null, true);

		LOGGER.info("Conversion Completed to XML " + inputFilePath);

		////////////////////////////////////////////////////////////

		////////// PARSE XML and GET PAGE NAMES ////////////////////

		// find <diagram id="-J4FnIjizJcJbLS0x7CH" name="processing_time"> entries

		LOGGER.info("Parsing XML " + inputFilePath);

		String xmlFilePath = Utils.generateFilePath(inputFilePath, "", "", "xml", false);

		ArrayList<String> names = new ArrayList<String>();

		try {
			File inputXmlFile = new File(xmlFilePath);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputXmlFile);

			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("diagram");

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					String name = ((org.w3c.dom.Element) node).getAttribute("name");

					LOGGER.info("Draw.io Pagename-" + i + " : [" + name + "]");

					names.add(name);
				}
			}

			// Delete XML File
			inputXmlFile.delete();

		} catch (Exception e) {
			throw e;
		}

		// Delete Generated XML

		///////////////////////////////////////////////////////////////////////////

		/////////// EXPORT IMAGES WITH PAGE_INDEX and NAME WITH PAGE-NAME ////////
		if (names.size() > 0) {
			int index = 0;
			int pageCount = names.size();

			// Create assets directory if it doesn't exist
			String assetsDirPath = FilenameUtils.getFullPath(inputFilePath) + "assets/";
			Path assetsDir = Paths.get(assetsDirPath);
			if (!Files.exists(assetsDir)) {
				Files.createDirectory(assetsDir);
			}

			File drawioFile = new File(inputFilePath);

			String drawioFileNameWithoutExtension = "";
			int extensionIndex = drawioFile.getName().lastIndexOf('.');
			if (extensionIndex > 0) {
				drawioFileNameWithoutExtension = drawioFile.getName().substring(0, extensionIndex);
			}

			String parentFolder = drawioFile.getParent();

			for (String name : names) {

				LOGGER.info("Exporting page-" + index + "/" + pageCount + " : " + name);

				// SVG Export
				String outputFile = "\"" + parentFolder + "\\assets\\" + drawioFileNameWithoutExtension + "-" + name
						+ ".svg" + "\"";

				String inputFile = "\"" + inputFilePath + "\"";

				LOGGER.info("Input File : " + inputFile);
				LOGGER.info("Output File : " + outputFile);

				String[] cmdSvg = { drawio, "--export", "--page-index", String.valueOf(index), "--format", "svg",
						"--output", outputFile, inputFile };

				LOGGER.info("Command : " + Utils.toString(cmdSvg, concatenate));

				Utils.executeCommandThread(cmdSvg, null, true);

				LOGGER.info("Exporting page-" + index + "/" + pageCount + " : " + name + " SVG Success");

				// PNG Export
				outputFile = "\"" + parentFolder + "\\assets\\" + drawioFileNameWithoutExtension + "-" + name + ".png"
						+ "\"";

				inputFile = "\"" + inputFilePath + "\"";

				LOGGER.info("Input File : " + inputFile);
				LOGGER.info("Output File : " + outputFile);

				String[] cmdpng = { drawio, "--export", "--page-index", String.valueOf(index), "--format", "png",
						"--output", outputFile, inputFile };

				LOGGER.info("Command : " + Utils.toString(cmdpng, concatenate));

				Utils.executeCommandThread(cmdpng, null, true);

				LOGGER.info("Exporting page-" + index + "/" + pageCount + " : " + name + " PNG Success");

				// JPEG Export
				outputFile = "\"" + parentFolder + "\\assets\\" + drawioFileNameWithoutExtension + "-" + name + ".jpeg"
						+ "\"";

				inputFile = "\"" + inputFilePath + "\"";

				LOGGER.info("Input File : " + inputFile);
				LOGGER.info("Output File : " + outputFile);

				String[] cmdjpg = { drawio, "--export", "--page-index", String.valueOf(index), "--format", "jpeg",
						"--output", outputFile, inputFile };

				LOGGER.info("Command : " + Utils.toString(cmdjpg, concatenate));

				Utils.executeCommandThread(cmdjpg, null, true);

				LOGGER.info("Exporting page-" + index + "/" + pageCount + " : " + name + " JPG Success");

				index++;
			}
		}
		/////////////////////////////////////////////////////////////////////////////

		return true;

	}

	/**
	 * 
	 * This method converts an image link in the Marp syntax format to Pandoc syntax
	 * format.
	 * 
	 * @param marpImageLink The image link in Marp syntax format.
	 * 
	 * @return An array containing two strings: the Pandoc image link and the
	 *         original image URL.
	 */
	public static String[] convertMarpToPandoc(String marpImageLink) {

		String[] result = new String[2];

		String pandocImageType = ".jpeg"; // replace svg with jpeg

		String pandocImageLink = "";
		int i = marpImageLink.lastIndexOf("(");

		String imageAttributes = "";
		String imageUrl = "";
		if (i > -1) {
			imageAttributes = marpImageLink.substring(0, i);
			imageUrl = marpImageLink.substring(i + 1, marpImageLink.length() - 1);
		} else {
			imageAttributes = marpImageLink;
			imageUrl = "http://www.plantuml.com/plantuml/png/SoWkIImgAStDuV9DB2fGqBLJ24ZCIybFLh1IyCjNyCnDJ4zLSCilpKlXSaZDIm6g0m00";
		}

		String altAttr = "";
		if (imageAttributes.contains("alt:")) {
			String altkeyword = "alt:\"";
			String endSeperator = "\"";
			altAttr = extractKeyValue(imageAttributes, altkeyword, endSeperator);
		}

		String widthAttr = "";
		if (imageAttributes.contains("width:")) {
			String widthkeyword = "width:";
			String endSeperator = " ";
			widthAttr = extractKeyValue(imageAttributes, widthkeyword, endSeperator);
		}

		if (imageAttributes.contains("w:")) {
			String widthkeyword = "w:";
			String endSeperator = " ";
			widthAttr = extractKeyValue(imageAttributes, widthkeyword, endSeperator);
		}

		String heightAttr = "";
		if (imageAttributes.contains("height:")) {
			String heightkeyword = "height:";
			String endSeperator = " ";
			heightAttr = extractKeyValue(imageAttributes, heightkeyword, endSeperator);
		}

		if (imageAttributes.contains("h:")) {
			String heightkeyword = "h:";
			String endSeperator = " ";
			heightAttr = extractKeyValue(imageAttributes, heightkeyword, endSeperator);
		}

		String attributesNew = "";
		if (!Utils.checkStringNullOrEmpty(widthAttr)) {
			attributesNew += "width=" + widthAttr + " ";
		}

		if (!Utils.checkStringNullOrEmpty(heightAttr)) {
			attributesNew += "height=" + heightAttr + " ";
		}

		if (imageAttributes.contains("center")) {
			attributesNew += "align=center ";
		}

//		if (!imageUrl.startsWith("http") && !imageUrl.startsWith("./")) {
//			imageUrl = "./" + imageUrl;
//		}

		if (!Utils.checkStringNullOrEmpty(altAttr)) {
			altAttr = "\"" + altAttr + "\"";
		}

		pandocImageLink = "![" + altAttr + "]" + "(" + imageUrl.replace(".svg", pandocImageType).strip() + ")" + "{"
				+ attributesNew.strip() + "}";

		result[0] = pandocImageLink;
		result[1] = imageUrl;

		return result;
	}

	/**
	 * Creates all parent folders for the given file path if they do not already
	 * exist.
	 * 
	 * @param filePath the path of the file to create
	 */
	public static void createFoldersForFilePathIfNotExist(String filePath) {
		File file = new File(filePath);
		File parent = file.getParentFile();
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}
	}

	/**
	 * 
	 * Extracts the value of a key in a string of image attributes.
	 * 
	 * @param imageAttributes the string of image attributes
	 * @param key             the key to extract the value of
	 * @param endSeparator    the end separator after the value
	 * @return the value of the key, or null if not found or end separator not found
	 */
	private static String extractKeyValue(String imageAttributes, String key, String endSeparator) {
		int startIndex = imageAttributes.indexOf(key);
		if (startIndex == -1) {
			return null; // Key not found
		}
		startIndex += key.length(); // Skip key
		int endIndex = imageAttributes.indexOf(endSeparator, startIndex);
		if (endIndex == -1) {
			endIndex = imageAttributes.indexOf("]", startIndex);
		}
		if (endIndex == -1) {
			return null; // End separator not found
		}
		return imageAttributes.substring(startIndex, endIndex);
	}

	/**
	 * Get the current working directory of the Jar or Class file (based on the
	 * Utils class).
	 *
	 * @return the current working directory as a string
	 * @throws URISyntaxException if there is an error with the file path
	 */
	public static String getWorkingDirectory() throws URISyntaxException {
		String jarFilePath = new File(Utils.class.getProtectionDomain().getCodeSource().getLocation().toURI())
				.getPath();
		File binDir = new File(jarFilePath);
		return Utils.ifNullThenUse(binDir.getParent(), "");
	}

	/**
	 * 
	 * Get the full path for a given file path, whether it is relative or absolute.
	 * 
	 * @param filePath the file path to get the full path for
	 * 
	 * @return the full path of the file
	 * 
	 * @throws URISyntaxException if there is an error in parsing the file path
	 */
	public static String getFullPath(String filePath) throws URISyntaxException {

		Path path = Paths.get(filePath);
		if (path.isAbsolute()) {
			return filePath;
		} else {
			return combinePaths(getWorkingDirectory(), filePath);
		}

	}

	/**
	 * Generates a file path with a prefix and/or postfix and an extension.
	 * 
	 * @param filePath  The original file path.
	 * @param prefix    The prefix to add.
	 * @param postfix   The postfix to add.
	 * @param extension The extension to use. If null or empty, it is extracted from
	 *                  the original file path.
	 * @return The full path of the file with the prefix and postfix added and the
	 *         new extension.
	 */
	public static String generateFilePath(String filePath, String prefix, String postfix, String extension,
			boolean wrap) {
		String newExtension = extension;
		if (Utils.checkStringNullOrEmpty(extension)) {
			int index = filePath.lastIndexOf('.');
			if (index > 0) {
				newExtension = filePath.substring(index + 1);
			}
		}

		File file = new File(filePath);

		String fileNameWithoutExt = file.getName().replaceFirst("[.][^.]+$", "");
		String parentDir = file.getParent() != null ? file.getParent() + File.separator : "";

		String newFileName = prefix + fileNameWithoutExt + postfix + "." + newExtension;

		String fullPath = parentDir + newFileName;

		fullPath = fullPath.replace('"', '\0');

		if (wrap) {
			fullPath = "\"" + fullPath + "\"";
		}

		return fullPath;
	}

	/**
	 * 
	 * Combine path strings into a single path.
	 * 
	 * @param paths List of path components to combine
	 * 
	 * @return Combined path
	 */
	public static String combinePaths(String... paths) {
		if (paths.length == 0) {
			return "";
		}

		File combined = new File(paths[0]);

		int i = 1;
		while (i < paths.length) {
			combined = new File(combined, paths[i]);
			++i;
		}

		return combined.getPath();
	}

	/**
	 * Check if String Null or Empty
	 * 
	 * @param val string value for control
	 * @return true if null or empty else false
	 */
	public static boolean checkStringNullOrEmpty(String val) {
		return (val == null || val.isEmpty() || val.trim().isEmpty());
	}

	/**
	 * Check if String Null then Use Default Value
	 * 
	 * @param fiIfNullCheckValue control value
	 * @param fiDefaultValue     default value if nullcheck value null
	 * @return default value if null else return current valeu
	 */
	public static String ifNullThenUse(String fiIfNullCheckValue, String fiDefaultValue) {
		if (fiIfNullCheckValue == null)
			return fiDefaultValue;
		return fiIfNullCheckValue;
	}

	/**
	 * 
	 * Get the version of the installed Java.
	 * 
	 * @return The version of the installed Java as an integer.
	 */
	public static int getJavaVersion() {
		String version = System.getProperty("java.version");
		if (version.startsWith("1.")) {
			version = version.substring(2, 3);
		} else {
			int dot = version.indexOf(".");
			if (dot != -1) {
				version = version.substring(0, dot);
			}
		}
		return Integer.parseInt(version);
	}

	/**
	 * Finds all files with the specified file extensions in the given directory
	 * path.
	 *
	 * @param path           The directory path to search in.
	 * @param fileExtensions The array of file extensions to look for.
	 * @return A list of strings representing the paths of the found files.
	 * @throws IOException              If an I/O error occurs while searching for
	 *                                  the files.
	 * @throws IllegalArgumentException If the specified path is not a directory.
	 */
	public static List<String> findFiles(Path path, String[] fileExtensions) throws IOException {
		if (!Files.isDirectory(path)) {
			throw new IllegalArgumentException("Path must be a directory!");
		}

		List<String> result;
		try (Stream<Path> walk = Files.walk(path, Integer.MAX_VALUE)) {
			result = walk.filter(p -> !Files.isDirectory(p))
					// convert path to string
					.map(p -> p.toString().toLowerCase()).filter(f -> isEndWith(f, fileExtensions))
					.collect(Collectors.toList());
		}
		return result;
	}

	/**
	 * Checks whether the given file ends with any of the specified file extensions.
	 *
	 * @param file           The file to check.
	 * @param fileExtensions The array of file extensions to compare with.
	 * @return true if the file ends with any of the file extensions, false
	 *         otherwise.
	 */
	private static boolean isEndWith(String file, String[] fileExtensions) {
		boolean result = false;
		for (String fileExtension : fileExtensions) {
			if (file.endsWith(fileExtension)) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * Converts an image string in a specific format to pandoc syntax. The image
	 * string format should be as follows:
	 *
	 * "![alt:\"insertion sort algorithm\" height:500px
	 * center](assets/ce100-week-1-intro-ins_sort_2.drawio.svg)"
	 *
	 * where alt is the alternate text for the image, height is the height of the
	 * image, and center is an optional boolean indicating whether the image should
	 * be centered or not.
	 *
	 * @param imageString The image string to convert.
	 * @return The pandoc syntax for the image.
	 */
	public static String convertImageStringToPandoc(String imageString) {
		String altText = "";
		String path = "";
		String height = "";
		String center = "";

		int altStart = imageString.indexOf("[");
		int altEnd = imageString.indexOf("]");
		if (altStart != -1 && altEnd != -1) {
			altText = imageString.substring(altStart + 1, altEnd);
		}

		int pathStart = imageString.indexOf("(");
		int pathEnd = imageString.indexOf(" ", pathStart);
		if (pathStart != -1 && pathEnd != -1) {
			path = imageString.substring(pathStart + 1, pathEnd);
		} else if (pathStart != -1) {
			path = imageString.substring(pathStart + 1, imageString.length() - 1);
		}

		String attributes = imageString.substring(pathEnd + 1, imageString.length() - 1);
		String[] attributeList = attributes.split(" ");

		for (int i = 0; i < attributeList.length; i++) {
			String[] attribute = attributeList[i].split(":");
			if (attribute[0].equals("height")) {
				height = attribute[1];
			} else if (attribute[0].equals("center")) {
				center = ".center";
			}
		}

		return String.format("![%s](%s){ height=%s%s }", altText, path, height, center);
	}

	/**
	 * 
	 * Executes a command in a separate thread and returns the output.
	 * 
	 * @param command       the command to execute as an array of strings
	 * @param workingFolder the folder in which to execute the command (can be null)
	 * @param waitFor       whether to wait for the process to finish before
	 *                      returning
	 * @return the output of the command as a string
	 */
	public static String executeCommandThread(String[] command, String workingFolder, boolean waitFor) {

		String commandCombined = String.join(" ", command);

		StringBuilder output = new StringBuilder();

		ProcessBuilder processBuilder = new ProcessBuilder(commandCombined.split("\\s+"));
		if (workingFolder != null && !workingFolder.isEmpty()) {
			processBuilder.directory(new File(workingFolder));
		}

		Process process;
		try {
			process = processBuilder.start();

			// Create a thread to read the output of the process
			Thread outputThread = new Thread(() -> {
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
					String line;
					while ((line = reader.readLine()) != null) {
						output.append(line).append('\n');
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			outputThread.start();

			if (waitFor) {
				// Wait for the process to finish and then join the output thread
				int exitCode = process.waitFor();
				outputThread.join();

				if (exitCode != 0) {
					throw new RuntimeException("Command failed with exit code " + exitCode);
				}
			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		return output.toString();
	}

	/**
	 * 
	 * Converts a PowerPoint file to Marp markdown format.
	 * 
	 * @param filePath The path to the PowerPoint file to convert.
	 * 
	 * @param pandoc   Whether to use pandoc-style image tags or not.
	 * 
	 * @return The Marp markdown string.
	 * 
	 * @throws IOException If an error occurs while reading or writing files.
	 */
	public static String convertPowerPointToMarp(String filePath, boolean pandoc) throws IOException {
		String assetsFolder = "assets/";
		XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(filePath));
		StringBuilder markdownBuilder = new StringBuilder();

		// Create assets directory if it doesn't exist
		String assetsDirPath = FilenameUtils.getFullPath(filePath) + assetsFolder;
		Path assetsDir = Paths.get(assetsDirPath);
		if (!Files.exists(assetsDir)) {
			Files.createDirectory(assetsDir);
		}

		for (XSLFSlide slide : ppt.getSlides()) {
			markdownBuilder.append("---\n\n");

			for (XSLFShape shape : slide.getShapes()) {
				if (shape instanceof XSLFTextShape) {
					XSLFTextShape textShape = (XSLFTextShape) shape;
					String text = textShape.getText();

					if (text != null && !text.isEmpty()) {
						switch (textShape.getTextType()) {
						case TITLE:
							markdownBuilder.append("# ").append(text).append("\n\n");
							break;
						case SUBTITLE:
							markdownBuilder.append("## ").append(text).append("\n\n");
							break;
						case BODY:
							markdownBuilder.append(text).append("\n\n");
							break;
						default:
							break;
						}
					}
				} else if (shape instanceof XSLFPictureShape) {
					XSLFPictureShape pictureShape = (XSLFPictureShape) shape;
					XSLFPictureData pictureData = pictureShape.getPictureData();
					int contentType = pictureData.getPictureType();
					PictureType pictureType = null;

					pictureType = xslf2pictureType(contentType, pictureType);

					// Only handle PNG and JPEG images
					if (pictureType == PictureType.PNG || pictureType == PictureType.JPEG) {
						String fileName = pictureData.getFileName();
						String fileExtension = FilenameUtils.getExtension(fileName);
						byte[] imageData = pictureData.getData();
						String base64Image = Base64.getEncoder().encodeToString(imageData);

						// Generate a unique file name for the image
						String uniqueFileName = generateUniqueFileName(assetsDirPath, fileExtension);
						String imagePath = assetsFolder + uniqueFileName;

						// Write the image data to a file
						File imageFile = new File(assetsDirPath + uniqueFileName);
						FileOutputStream fos = new FileOutputStream(imageFile);
						fos.write(imageData);
						fos.close();

						// Add a Marp Markdown image tag referencing the saved image file
						XSLFPictureShape pictureShapex = (XSLFPictureShape) pictureShape;
						Rectangle2D anchor = pictureShapex.getAnchor();
						double height = anchor.getHeight();
						double width = anchor.getWidth();
						double x = anchor.getX();
						double y = anchor.getY();
						Dimension imageSize = new Dimension((int) Math.round(width), (int) Math.round(height));
						double aspectRatio = imageSize.getWidth() / imageSize.getHeight();

						// Calculate the center coordinates
						double centerX = x + width / 2;
						double centerY = y + height / 2;
						int roundedCenterX = (int) Math.round(centerX);
						int roundedCenterY = (int) Math.round(centerY);

						String markdownImageTag = "";
						if (pandoc) {
							// Construct the Marp Markdown image tag with position and dimensions
							markdownImageTag = "![alt=\"image\" width:" + width + "px height:" + height + "px]("
									+ imagePath + "){position: absolute; top: " + roundedCenterY + "px; left: "
									+ roundedCenterX + "px}";
						} else {
							markdownImageTag = "![alt=\"image\" width:" + width + "px height:" + height + "px]("
									+ imagePath + ")";
						}

						markdownBuilder.append(markdownImageTag).append("\n\n");

					}
				}
			}
		}

		return markdownBuilder.toString();
	}

	/**
	 * 
	 * Converts a content type integer to a PictureType object.
	 * 
	 * @param contentType The content type integer to convert
	 * @param pictureType The default PictureType object to use if conversion fails
	 * @return The PictureType object corresponding to the content type integer, or
	 *         the default PictureType object if conversion fails
	 */
	private static PictureType xslf2pictureType(int contentType, PictureType pictureType) {
		switch (contentType) {
		case XSLFPictureData.PICTURE_TYPE_EMF:
			pictureType = PictureType.EMF;
			break;
		case XSLFPictureData.PICTURE_TYPE_WMF:
			pictureType = PictureType.WMF;
			break;
		case XSLFPictureData.PICTURE_TYPE_PICT:
			pictureType = PictureType.PICT;
			break;
		case XSLFPictureData.PICTURE_TYPE_JPEG:
			pictureType = PictureType.JPEG;
			break;
		case XSLFPictureData.PICTURE_TYPE_PNG:
			pictureType = PictureType.PNG;
			break;
		case XSLFPictureData.PICTURE_TYPE_DIB:
			pictureType = PictureType.DIB;
			break;
		case XSLFPictureData.PICTURE_TYPE_GIF:
			pictureType = PictureType.GIF;
			break;
		case XSLFPictureData.PICTURE_TYPE_TIFF:
			pictureType = PictureType.TIFF;
			break;
		case XSLFPictureData.PICTURE_TYPE_EPS:
			pictureType = PictureType.EPS;
			break;
		case XSLFPictureData.PICTURE_TYPE_BMP:
			pictureType = PictureType.BMP;
			break;
		case XSLFPictureData.PICTURE_TYPE_WPG:
			pictureType = PictureType.WPG;
			break;
		default:
			break;
		}
		return pictureType;
	}

	/**
	 * 
	 * Generates a unique file name for a file with a given extension in a given
	 * directory.
	 * 
	 * @param directoryPath The path of the directory to generate the file name for.
	 * @param fileExtension The extension of the file to generate the name for.
	 * @return A unique file name with the given extension.
	 */
	private static String generateUniqueFileName(String directoryPath, String fileExtension) {
		String fileName;
		File file;

		do {
			fileName = UUID.randomUUID().toString() + "." + fileExtension;
			file = new File(directoryPath + fileName);
		} while (file.exists());

		return fileName;
	}

}