package com.ucoruh.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * @author ugur.coruh
 *
 */
public class Utils {

//	private final static Logger LOGGER = Logger.getLogger(Utils.class.getName());

	/**
	 * Return Current Working Directory of Jar or Class File (Based on Utils Class)
	 * 
	 * @return
	 * @throws URISyntaxException
	 */
	public static String getWorkingDirectory() throws URISyntaxException {

		String jarFilePath = new File(Utils.class.getProtectionDomain().getCodeSource().getLocation().toURI())
				.getPath();
		File binDir = new File(jarFilePath);
		return Utils.ifNullThenUse(binDir.getParent(), "");

//		String classPath = Utils.getClassPath();
//		File binDir = new File(classPath);
//		return Utils.ifNullThenUse(binDir.getParent(), "");
	}

	/**
	 * Get Full Path for Relative or Absolute Path Inputs
	 * 
	 * @param filePath
	 * @return
	 * @throws URISyntaxException
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
	 * Generate Filepath with postfix or prefix of file
	 * 
	 * @param filePath filepath
	 * @param postfix  postfix of file
	 * @param prefix   prefix of file
	 * @return full path of file with postfix and prefix
	 */
	public static String filePathWithPostFixOrPrefix(String fiFilePath, String fiPrefix, String fiPostfix,
			String fiExtension) {

		String extension = null;

		String prefixIn = Utils.ifNullThenUse(fiPrefix, "");
		String postFixIn = Utils.ifNullThenUse(fiPostfix, "");

		if (Utils.checkStringNullOrEmpty(fiExtension)) {
			int index = fiFilePath.lastIndexOf('.');
			if (index > 0) {
				extension = fiFilePath.substring(index + 1);
			}

		} else {
			extension = fiExtension;
		}

		File file = new File(fiFilePath);

		String fileNameWithOutExt = file.getName().replaceFirst("[.][^.]+$", "");

		String parentFile = "";
		if (file.getParent() != null)
			parentFile = file.getParent() + File.separator;

		return parentFile + prefixIn + fileNameWithOutExt + postFixIn + "." + extension;
	}

	/**
	 * Combine Path Strings
	 * 
	 * @param paths
	 * @return combined paths
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
	 * Get Java Installed Version
	 * 
	 * @return
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
	 * 
	 * @param path
	 * @param fileExtensions
	 * @return
	 * @throws IOException
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
	 * 
	 * @param file
	 * @param fileExtensions
	 * @return
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
	 * String imageString = "![alt:\"insertion sort algorithm\" height:500px center](assets/ce100-week-1-intro-ins_sort_2.drawio.svg)";
	 * String pandocSyntax = convertImageStringToPandoc(imageString);
	 * System.out.println(pandocSyntax);
	 * @param imageString
	 * @return
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

	

	public static void executeCommand(String[] command) {
		try {

			ProcessBuilder builder = new ProcessBuilder(command);
			// builder.directory(new File(System.getProperty("user.dir")));
			builder.redirectErrorStream(true);
			Process process = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			int exitCode = process.waitFor();
			System.out.println("Command execution completed with exit code: " + exitCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String executeCommandThread(String[] command) {
		
		String commandCombined = String.join(" ", command);
		
	    StringBuilder output = new StringBuilder();

	    Process process;
	    try {
	        process = Runtime.getRuntime().exec(commandCombined);

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

	        // Wait for the process to finish and then join the output thread
	        int exitCode = process.waitFor();
	        outputThread.join();

	        if (exitCode != 0) {
	            throw new RuntimeException("Command failed with exit code " + exitCode);
	        }

	    } catch (IOException | InterruptedException e) {
	        e.printStackTrace();
	    }

	    return output.toString();
	}

	
//    public static String[] executeCommand(String[] command){
//        
//        
//        String s = null;
//        
//        String[] outList = new String[2];
//
//        try {
//        	
//        	String commandString = "";
//        	
//        	for(String cmd : command)
//        	{
//        		commandString+=" "+cmd;
//        	}
//        	
//        	commandString = commandString.substring(1,commandString.length());
//        	
//            
//            Process p = Runtime.getRuntime().exec(commandString);
//            
//            BufferedReader stdInput = new BufferedReader(new 
//                 InputStreamReader(p.getInputStream()));
//
//            BufferedReader stdError = new BufferedReader(new 
//                 InputStreamReader(p.getErrorStream()));
//
//            // read the output from the command
//            System.out.println("Standard output of the command:\n");
//            while ((s = stdInput.readLine()) != null) {
//                outList[0]+=s+"\r\n";
//                System.out.println(s);
//            }
//            
//            // read any errors from the attempted command
//            System.out.println("Standard error of the command (if any):\n");
//            while ((s = stdError.readLine()) != null) {
//                outList[1]+=s+"\r\n";
//                System.out.println(s);
//            }
//        }
//        catch (IOException e) {
//            System.out.println("Exception happened: ");
//            e.printStackTrace();
//        }
//        
//        return outList;
//    }

}