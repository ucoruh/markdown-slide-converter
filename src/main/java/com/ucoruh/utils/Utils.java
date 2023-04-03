package com.ucoruh.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
	public static String filePathWithPostFixOrPrefix(String filePath, String postfix, String prefix) {

		String extension = null;

		String prefixIn = prefix;
		String postFixIn = postfix;

		int index = filePath.lastIndexOf('.');
		if (index > 0) {
			extension = filePath.substring(index + 1);
		}

		File file = new File(filePath);

		String fileNameWithOutExt = file.getName().replaceFirst("[.][^.]+$", "");

		return file.getParent() + File.separator + prefixIn + fileNameWithOutExt + postFixIn + "." + extension;
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
//    public static List<String> findFiles(Path path, String[] fileExtensions) throws IOException {
//
//        if (!Files.isDirectory(path)) {
//            throw new IllegalArgumentException("Path must be a directory!");
//        }
//
//        List<String> result;
//        try (Stream<Path> walk = Files.walk(path, 1)) {
//            result = walk
//                    .filter(p -> !Files.isDirectory(p))
//                    // convert path to string
//                    .map(p -> p.toString().toLowerCase())
//                    .filter(f -> isEndWith(f, fileExtensions))
//                    .collect(Collectors.toList());
//        }
//        return result;
//
//    }
    
    
    public static List<String> findFiles(Path path, String[] fileExtensions) throws IOException {
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path must be a directory!");
        }

        List<String> result;
        try (Stream<Path> walk = Files.walk(path, Integer.MAX_VALUE)) {
            result = walk
                .filter(p -> !Files.isDirectory(p))
                // convert path to string
                .map(p -> p.toString().toLowerCase())
                .filter(f -> isEndWith(f, fileExtensions))
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

}