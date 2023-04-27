package com.ucoruh.mkdocs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.ucoruh.utils.Utils;

public class MkdocsBuilder {

	/**
	 * 
	 * @brief A logger object used for logging messages in the MkdocsBuilder class.
	 */
	private final static Logger LOGGER = Logger.getLogger(MkdocsBuilder.class.getName());

	/**
	 * Copies a resource folder from a JAR file to an external folder on the
	 * filesystem.
	 *
	 * @param inputFolder  the path to the resource folder relative to the root of
	 *                     the classpath.
	 * @param outputFolder the path to the external folder where the files will be
	 *                     copied.
	 * @return true if the copy operation was successful, false otherwise.
	 * @throws IOException if an I/O error occurs while reading or writing the
	 *                     files.
	 */
	public boolean copyResourceFolder(String inputFolder, String outputFolder) throws IOException {
		// Create the output folder if it doesn't exist
		Utils.createDirectories(outputFolder);

		// Get a list of all the files in the input folder
		URL inputUrl = getClass().getResource(inputFolder);
		if (inputUrl == null) {
			throw new FileNotFoundException("Resource folder " + inputFolder + " not found.");
		}
		File inputFile = new File(inputUrl.getFile());
		File[] files = inputFile.listFiles();

		// Copy each file to the output folder
		for (File file : files) {
			if (file.isDirectory()) {
				copyResourceFolder(inputFolder + "/" + file.getName(), outputFolder + "/" + file.getName());
			} else {
				InputStream inputStream = new FileInputStream(file);
				OutputStream outputStream = new FileOutputStream(new File(outputFolder + "/" + file.getName()));
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
				inputStream.close();
				outputStream.close();
			}
		}

		return true;
	}

	/**
	 * Generates a license string for a given open source license type.
	 * 
	 * @param licenseType The type of the open source license.
	 * @param author      The name of the author who holds the copyright.
	 * @param yearBegin   The beginning year of the copyright.
	 * @param yearEnd     The ending year of the copyright.
	 * @param language    The language of the license string.en or tr
	 * @return A string containing the license information.
	 */
	public String generateLicenseString(LicenseTypes licenseType, String author, int yearBegin, int yearEnd,LanguageType language) {
		String licenseString = "";

		String copyright = "(c) " + yearBegin + "-" + yearEnd + " " + author;

		switch (licenseType) {
		case MIT:
			if (language == LanguageType.EN_US) {
				licenseString = "The MIT License (MIT)\n\n" + "Copyright " + copyright + "\n\n"
						+ "Permission is hereby granted, free of charge, to any person obtaining a copy\n"
						+ "of this software and associated documentation files (the \"Software\"), to deal\n"
						+ "in the Software without restriction, including without limitation the rights\n"
						+ "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n"
						+ "copies of the Software, and to permit persons to whom the Software is\n"
						+ "furnished to do so, subject to the following conditions:\n\n"
						+ "The above copyright notice and this permission notice shall be included in\n"
						+ "all copies or substantial portions of the Software.\n\n"
						+ "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n"
						+ "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n"
						+ "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n"
						+ "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n"
						+ "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n"
						+ "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN\n"
						+ "THE SOFTWARE.";
			} else if (language == LanguageType.TR_TR) {
				licenseString = "MIT Lisansı (MIT)\n\n" + "Telif Hakkı " + copyright + "\n\n"
						+ "İzin verilen: bu yazılım ve ilgili belgeler ("
						+ "Yazılım\") ücretsiz olarak herhangi bir kişi tarafından elde edilebilir."
						+ "Yazılımın kullanımı, kopyalanması, değiştirilmesi, birleştirilmesi,"
						+ "yayınlanması, dağıtılması, alt lisanslandırılması ve / veya"
						+ "kopyalarını satması ve yazılımı sağlayan kişilere izin verilir"
						+ "yazılım, aşağıdaki koşullara tabidir:\n\n"
						+ "Yukarıdaki telif hakkı bildirimi ve bu izin bildirimi tüm kopyalarda veya"
						+ "Yazılımın önemli bölümlerinde yer almalıdır.\n\n"
						+ "YAZILIM \"OLDUĞU GİBİ\" SAĞLANIR, HİÇBİR GARANTİ OLMAKSIZIN,"
						+ "BELİRLİ BİR AMACA UYGUNLUK VEYA İHLAL ETMEME GARANTİLERİ DAHİL OLMAK ÜZERE,"
						+ "SATILABİLİRLİK, ÖZEL AMACI VEYA İHLALİNE İLİŞKİN HERHANGİ BİR GARANTİ"
						+ "YAZILIM VEYA KULLANIMI NEDEN OLUŞTURAN DİĞER SORUMLULUKLAR,"
						+ "SÖZLEŞME, HAKSIZ FİİL VEYA DİĞER ŞEKİLLERDE, BU YAZILIMLA İLGİLİ,"
						+ "YAZILIMIN KULLANIMIYLA İLGİLİ VEYA DİĞER TİCARİDE,"
						+ "KULLANIM OLMASI İLE İLGİLİ HERHANGİ BİR SORUMLULUK,"
						+ "HİÇBİR DURUMDA YAZARLAR VEYA TELİF HAKKI SAHİPLERİNİN"
						+ "İDDİALAR, HASARLAR VEYA DİĞER YÜKÜMLÜLÜKLERLE İLGİLİ."
						+ "YAZILIM VEYA YAZILIMIN KULLANIMINDAN KAYNAKLANAN YA DA İLGİLİ HERHANGİ BİR İLİŞKİDE"
						+ "YAZILIM.";
			} else {
				licenseString = "Unknown license language";
			}

			break;

		case APACHE_2_0:
			if (language == LanguageType.EN_US) {
				licenseString = "Apache License\n" + "Version 2.0, January 2004\n"
						+ "http://www.apache.org/licenses/\n\n" + "Copyright " + copyright + "\n\n"
						+ "TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION\n\n" + "1. Definitions.\n\n"
						+ "   \"License\" shall mean the terms and conditions for use, reproduction,\n"
						+ "   and distribution as defined by Sections 1 through 9 of this document.\n\n"
						+ "   \"Licensor\" shall mean the copyright owner or entity authorized by\n"
						+ "   the copyright owner that is granting the License.\n\n"
						+ "   \"Legal Entity\" shall mean the union of the acting entity and all\n"
						+ "   other entities that control, are controlled by, or are under common\n"
						+ "   control with that entity. For the purposes of this definition,\n"
						+ "   \"control\" means (i) the power, direct or indirect, to cause the\n"
						+ "   direction or management of such entity, whether by contract or\n"
						+ "   otherwise, or (ii) ownership of fifty percent (50%) or more of the\n"
						+ "   outstanding shares, or (iii) beneficial ownership of such entity.";
			} else if (language == LanguageType.TR_TR) {
				licenseString = "Apache Lisansı\n" + "Sürüm 2.0, Ocak 2004\n" + "http://www.apache.org/licenses/\n\n"
						+ "Telif Hakkı " + copyright + "\n\n" + "Bu lisans, Apache Yazılım Vakfı (\"ASF\") tarafından"
						+ "sunulan yazılımın kullanımı, kopyalanması, değiştirilmesi, birleştirilmesi,"
						+ "yayınlanması, dağıtılması, alt lisanslandırılması ve / veya"
						+ "kopyalarını satması ve ASF'ye sağlayan kişilere izin verilir."
						+ "yazılım, aşağıdaki koşullara tabidir:\n\n"
						+ "Yukarıdaki telif hakkı bildirimi ve bu izin bildirimi tüm kopyalarda veya"
						+ "Yazılımın önemli bölümlerinde yer almalıdır.\n\n"
						+ "YAZILIM \"OLDUĞU GİBİ\" SAĞLANIR, HİÇBİR GARANTİ OLMAKSIZIN,"
						+ "BELİRLİ BİR AMACA UYGUNLUK VEYA İHLAL ETMEME GARANTİLERİ DAHİL OLMAK ÜZERE,"
						+ "SATILABİLİRLİK, ÖZEL AMACI VEYA İHLALİNE İLİŞKİN HERHANGİ BİR GARANTİ"
						+ "YAZILIM VEYA KULLANIMI NEDEN OLUŞTURAN DİĞER SORUMLULUKLAR,"
						+ "SÖZLEŞME, HAKSIZ FİİL VEYA DİĞER ŞEKİLLERDE, BU YAZILIMLA İLGİLİ,"
						+ "YAZILIMIN KULLANIMIYLA İLGİLİ VEYA DİĞER TİCARİDE,"
						+ "KULLANIM OLMASI İLE İLGİLİ HERHANGİ BİR SORUMLULUK,"
						+ "HİÇBİR DURUMDA YAZAR VEYA TELİF HAKKI SAHİBİ,"
						+ "ASF'NİN YASAL TEMSİLCİLERİ, ASF'YE KATILAN ŞİRKETLER,"
						+ "YAZAR VEYA TELİF HAKKI SAHİBİNİN KİŞİSEL BİLGİSAYARINDAKİ YAZILIM"
						+ "VEYA YAZILIMIN KULLANIMINDAN KAYNAKLANAN YA DA İLGİLİ HERHANGİ BİR İLİŞKİDE" + "YAZILIM.";
			} else {
				licenseString = "Unknown license language";
			}
			break;
		case ALL_RIGHT_RESERVED:

			if (language == LanguageType.EN_US) {
				licenseString = "All Rights Reserved\r\n" + "\r\n" + "Copyright (c) " + copyright + "\r\n"
						+ "Created by " + copyright + "\r\n" + "\r\n"
						+ "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\r\n"
						+ "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\r\n"
						+ "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\r\n"
						+ "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\r\n"
						+ "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\r\n"
						+ "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN\r\n"
						+ "THE SOFTWARE.";
			} else if (language == LanguageType.TR_TR) {
				licenseString = "All Rights Reserved\r\n" + "\r\n" + "Copyright (c) " + copyright + "\r\n"
						+ "Created by " + copyright + "\r\n" + "\r\n"
						+ "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\r\n"
						+ "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\r\n"
						+ "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\r\n"
						+ "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\r\n"
						+ "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\r\n"
						+ "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN\r\n"
						+ "THE SOFTWARE.";
			} else {
				licenseString = "Unknown license language";
			}

			break;
		// add more cases for other open source licenses here
		default:
			if (language == LanguageType.EN_US) {
				licenseString = "Unknown license type";
			} else if (language == LanguageType.TR_TR) {
				licenseString = "Geçersiz lisans türü";
			} else {
				licenseString = "Unknown license language";
			}

			break;
		}

		if (language == LanguageType.EN_US) {
			return "\n" + licenseString + "\n\n" + "Copyright Notice:\n" + copyright + "\n";
		} else if (language == LanguageType.TR_TR) {
			return "\n" + licenseString + "\n\n" + "Telif Hakkı Bildirimi:\n" + copyright + "\n";
		} else {
			return "Unsupported language";
		}

	}

	/**
	 * 
	 * This method generates two markdown files "index.en.md" and "index.tr.md" in
	 * the "docs/changelog/" directory.
	 * 
	 * It takes an ArrayList of strings containing changelog entries as input. Each
	 * entry should follow the format "version | date | description".
	 * 
	 * @param changelogEntries ArrayList of strings containing changelog entries
	 * 
	 * @param rootFolderPath   Root folder path of the project
	 * 
	 * @return True if the markdown files are successfully created, false otherwise
	 * 
	 * @throws IOException If an I/O error occurs while creating the markdown files
	 */
	public boolean createChangeLogMarkdownFiles(ArrayList<String> changelogEntries, String rootFolderPath)
			throws IOException {

		String indexEnMdFilePath = Utils.combinePaths(rootFolderPath, "docs", "changelog", "index.en.md");
		String indexTrMdFilePath = Utils.combinePaths(rootFolderPath, "docs", "changelog", "index.tr.md");

		Utils.createFoldersForFilePathIfNotExist(indexEnMdFilePath);
		Utils.createFoldersForFilePathIfNotExist(indexTrMdFilePath); // not necessary

		String markdown = "---\ntemplate: main.html\n---\n\n";
		markdown += "# Changelog\n\n";

		if (changelogEntries != null) {
			for (String entry : changelogEntries) {
				String[] parts = entry.split("\\|");
				String version = parts[0].trim();
				String date = parts[1].trim();
				String description = parts[2].trim();

				markdown += "### " + version + " <small>_ " + date + "</small> { id=\"" + version + "\" }\n\n";
				markdown += "- " + description + "\n";
			}
		}

		// Write the English version of the Markdown file
		Utils.createFileWithContent(indexEnMdFilePath, markdown);

		markdown = "template: main.html\n\n";
		markdown += "# Değişiklik Kayıtları\n\n";

		if (changelogEntries != null) {
			for (String entry : changelogEntries) {
				String[] parts = entry.split("\\|");
				String version = parts[0].trim();
				String date = parts[1].trim();
				String description = parts[2].trim();

				markdown += "### " + version + " <small>_ " + date + "</small> { id=\"" + version + "\" }\n\n";
				markdown += "- " + description + "\n";
			}
		}

		// Write the Turkish version of the Markdown file
		Utils.createFileWithContent(indexTrMdFilePath, markdown);

		return true;
	}

	/**
	 * @brief Creates Markdown files for the given open source license type in
	 *        English and Turkish languages.
	 *
	 * @param licenseType    The type of the open source license.
	 * @param authorEn       The name of the author who holds the copyright in
	 *                       English.
	 * @param authorTR       The name of the author who holds the copyright in
	 *                       Turkish.
	 * @param yearBegin      The beginning year of the copyright.
	 * @param yearEnd        The ending year of the copyright.
	 * @param rootFolderPath The root folder path where the Markdown files will be
	 *                       created.
	 *
	 * @return true if the Markdown files were successfully created, false
	 *         otherwise.
	 * @throws IOException if there was an error while writing the Markdown files.
	 */
	public boolean createLicenseMarkdownFiles(LicenseTypes licenseType, String authorEn, String authorTR, int yearBegin,
			int yearEnd, String rootFolderPath) throws IOException {

		String indexEnMdFilePath = Utils.combinePaths(rootFolderPath, "docs", "license.en.md");
		String indexTrMdFilePath = Utils.combinePaths(rootFolderPath, "docs", "license.tr.md");

		Utils.createFoldersForFilePathIfNotExist(indexEnMdFilePath);
		Utils.createFoldersForFilePathIfNotExist(indexTrMdFilePath); // not necessary

		String markdown = "---\ntemplate: main.html\n---\n\n";

		markdown += "# License\n\n";

		markdown += "**" + licenseType + " License**\n\n";

		markdown += generateLicenseString(licenseType, authorEn, yearBegin, yearEnd, LanguageType.EN_US);

		// Write the English version of the Markdown file
		BufferedWriter writerEn = new BufferedWriter(new FileWriter(indexEnMdFilePath));
		writerEn.write(markdown);
		writerEn.close();

		markdown = "---\ntemplate: main.html\n---\n\n";

		markdown += "# Lisans\n\n";

		markdown += "**" + licenseType + " Lisans**\n\n";

		markdown += generateLicenseString(licenseType, authorTR, yearBegin, yearEnd, LanguageType.TR_TR);

		// Write the Turkish version of the Markdown file
		BufferedWriter writerTr = new BufferedWriter(new FileWriter(indexTrMdFilePath));
		writerTr.write(markdown);
		writerTr.close();

		return true;
	}

	/**
	 * 
	 * Generates the "docs/tags.md" file containing a global index of all tags used
	 * on the pages.
	 * 
	 * @param rootFolderPath the root folder path of the project.
	 * 
	 * @return true if the file was successfully created, false otherwise.
	 * 
	 * @throws IOException if there was an I/O error while creating the file.
	 */
	public boolean createTagsMarkdownFile(String rootFolderPath) throws IOException {

		String tagMdFilePath = Utils.combinePaths(rootFolderPath, "docs", "tags.md");

		Utils.createFoldersForFilePathIfNotExist(tagMdFilePath);

		String markdown = "# Tags\n\n";
		markdown += "This file contains a global index of all tags used on the pages.\n\n";
		markdown += "[TAGS]\n";

		// Write of the Markdown file
		BufferedWriter writerTags = new BufferedWriter(new FileWriter(tagMdFilePath));
		writerTags.write(markdown);
		writerTags.close();

		return true;
	}

	/**
	 * 
	 * Creates the home page index Markdown files for English and Turkish languages.
	 * 
	 * @param rootFolderPath the root folder path of the project
	 * @return true if the Markdown files are successfully created, false otherwise
	 * @throws IOException if there is an error while creating the Markdown files
	 */
	public boolean createHomePageIndexMarkdownFile(String rootFolderPath) throws IOException {

		String indexEnMdFilePath = Utils.combinePaths(rootFolderPath, "docs", "index.en.md");
		String indexTrMdFilePath = Utils.combinePaths(rootFolderPath, "docs", "index.tr.md");

		Utils.createDirectories(indexEnMdFilePath);
		Utils.createDirectories(indexTrMdFilePath);

		String markdown = "---\n" + "template: main.html\n" + "title: Course Material\n" + "---\n\n"
				+ "Course Material\n";

		// Write of the Markdown file
		BufferedWriter writerIndexEn = new BufferedWriter(new FileWriter(indexEnMdFilePath));
		writerIndexEn.write(markdown);
		writerIndexEn.close();

		markdown = "---\n" + "template: main.html\n" + "title: Ders Materyali \n" + "---\n\n" + "Ders Materyali \n";

		// Write of the Markdown file
		BufferedWriter writerIndexTr = new BufferedWriter(new FileWriter(indexTrMdFilePath));
		writerIndexTr.write(markdown);
		writerIndexTr.close();

		return true;
	}

	/**
	 * 
	 * Copies the override and image resources to the specified root folder path.
	 * 
	 * @param rootFolderPath The root folder path to which the resources will be
	 *                       copied.
	 * 
	 * @return Returns true if the operation is successful.
	 * 
	 * @throws IOException If an I/O error occurs while copying the resources.
	 */
	public boolean copyOverridesAndImages(String rootFolderPath) throws IOException {

		String sourcePath = "/docs/overrides";
		String targetPath = Utils.combinePaths(rootFolderPath, "docs", "overrides");

		this.copyResourceFolder(sourcePath, targetPath);

		sourcePath = "/docs/images";
		targetPath = Utils.combinePaths(rootFolderPath, "docs", "images");

		this.copyResourceFolder(sourcePath, targetPath);

		sourcePath = "/docs/img";
		targetPath = Utils.combinePaths(rootFolderPath, "docs", "img");

		this.copyResourceFolder(sourcePath, targetPath);

		sourcePath = "/docs/javascripts";
		targetPath = Utils.combinePaths(rootFolderPath, "docs", "javascripts");

		this.copyResourceFolder(sourcePath, targetPath);

		sourcePath = "/docs/overrides";
		targetPath = Utils.combinePaths(rootFolderPath, "docs", "overrides");

		this.copyResourceFolder(sourcePath, targetPath);

		return true;

	}

	/**
	 * 
	 * Copies the override and image resources to the specified root folder path and
	 * creates batch files to open local and remote web browsers.
	 * 
	 * @param rootFolderPath The root folder path to which the resources and batch
	 *                       files will be copied.
	 * 
	 * @param gitRepoName    The name of the Git repository.
	 * 
	 * @param gitUserName    The username of the Git account.
	 * 
	 * @return Returns true if the operation is successful.
	 * 
	 * @throws IOException If an I/O error occurs while copying the resources or
	 *                     creating the batch files.
	 */
	public boolean copyBatchScriptsAndRootFiles(String rootFolderPath, String gitRepoName, String gitUserName)
			throws IOException {

		String sourcePath = "/docs/batch";
		String targetPath = rootFolderPath;

		this.copyResourceFolder(sourcePath, targetPath);

		String fileName = Utils.combinePaths(targetPath, "8_open_local_web_browser.bat");
		String content = "@echo off\n";
		content += "@setlocal enableextensions\n";
		content += "@cd /d \"%~dp0\"\n";
		content += "start \"\" \"http://127.0.0.1:8000/" + gitRepoName + "/\"\n";

		Utils.createFileWithContent(fileName, content);

		fileName = Utils.combinePaths(targetPath, "10_open_remote_web_browser.bat");
		content = "start \"\" \"https://" + gitUserName + ".github.io/" + gitRepoName + "/\"";

		Utils.createFileWithContent(fileName, content);

		return true;

	}

	/**
	 * 
	 * Copies the override and image resources to the specified root folder path and
	 * creates batch files to open local and remote web browsers.
	 * 
	 * @param rootFolderPath The root folder path to which the resources and batch
	 *                       files will be copied.
	 * 
	 * @param gitRepoName    The name of the Git repository.
	 * 
	 * @param gitUserName    The username of the Git account.
	 * 
	 * @return Returns true if the operation is successful.
	 * 
	 * @throws IOException If an I/O error occurs while copying the resources or
	 *                     creating the batch files.
	 */
	public boolean generateDefaultSyllabusFiles(String rootFolderPath, String gitRepoName, String gitUserName,
			String courseCode, String courseName, String bolognaTrUrl, String bolognaEnUrl) throws IOException {

		String sourcePath = "/docs/syllabus";
		String targetPath = rootFolderPath;

		String courseNameUpdated = courseName.toLowerCase().strip().replace(" ", "-");

		this.copyResourceFolder(sourcePath, targetPath);

		// create open-ce100-bologna-tr.bat
		// start ""
		// "http://ogrenci.erdogan.edu.tr/BLGNDersBilgiPaketi/DersBilgileri?mufDersID=29761&dersGrubuDersID=0&dersID=16077&programID=1221&dilID=1"
		String fileName = Utils.combinePaths(targetPath, "docs", "syllabus",
				"open-" + courseCode + "-" + courseNameUpdated + "-bologna-tr.bat");
		String content = "@echo off\n";
		content += "@setlocal enableextensions\n";
		content += "@cd /d \"%~dp0\"\n";
		content += "start \"\" \"" + bolognaTrUrl + "\"\n";

		Utils.createFileWithContent(fileName, content);

		// create open-ce100-bologna-en.bat
		// start ""
		// "http://ogrenci.erdogan.edu.tr/BLGNDersBilgiPaketi/DersBilgileri?mufDersID=29761&dersGrubuDersID=0&dersID=16077&yilID=null&programID=1221&mufredatID=null&dilID=2"
		fileName = Utils.combinePaths(targetPath, "docs", "syllabus",
				"open-" + courseCode + "-" + courseNameUpdated + "-bologna-en.bat");
		content = "@echo off\n";
		content += "@setlocal enableextensions\n";
		content += "@cd /d \"%~dp0\"\n";
		content += "start \"\" \"" + bolognaEnUrl + "\"\n";

		Utils.createFileWithContent(fileName, content);

		// create download-ce100-bologna-pdf.bat
		// curl -o "ce100-course-bologna-tr.pdf"
		// "http://ogrenci.erdogan.edu.tr/BLGNDersBilgiPaketi/PDFTumu?mufDersID=29761&dersGrubuDersID=0&dersID=16077&yilID=null&programID=1221&mufredatID=null&dilID=1"
		// curl -o "ce100-course-bologna-en.pdf"
		// "http://ogrenci.erdogan.edu.tr/BLGNDersBilgiPaketi/PDFTumu?mufDersID=29761&dersGrubuDersID=0&dersID=16077&yilID=null&programID=1221&mufredatID=null&dilID=2"
		fileName = Utils.combinePaths(targetPath, "docs", "syllabus",
				"download-" + courseCode + "-" + courseNameUpdated + "-bologna-pdf.bat");
		content = "@echo off\n";
		content += "@setlocal enableextensions\n";
		content += "@cd /d \"%~dp0\"\n";
		content += "curl -o \"" + courseCode + "-" + courseNameUpdated + "-bologna-tr.pdf\" \"" + bolognaTrUrl + "\"\n";
		content += "curl -o \"" + courseCode + "-" + courseNameUpdated + "-bologna-en.pdf\" \"" + bolognaEnUrl + "\"\n";

		Utils.createFileWithContent(fileName, content);

		fileName = Utils.combinePaths(targetPath, "docs", "syllabus",
				"download-" + courseCode + "-" + courseNameUpdated + "-bologna-pdf.bat");

		return true;

	}

	
}
