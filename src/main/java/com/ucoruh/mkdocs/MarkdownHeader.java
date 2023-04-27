package com.ucoruh.mkdocs;

import org.jline.reader.impl.history.DefaultHistory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarkdownHeader {

	/**
	 * Example : Week-1 (Introduction to Analysis of Algorithms)
	 */
	private String header;
	/**
	 * Example : RTEU CE100 Week-1
	 */
	private String footer;
	/**
	 * Example : Asst. Prof. Dr. Uğur CORUH
	 */
	private String author;
	/**
	 * Example : WTFPL
	 */
	private LicenseTypes license;
	/**
	 * Example : en-US
	 */
	private LanguageType language;
	/**
	 * Spring Semester, 2022-2023
	 */
	private String semester;
	/**
	 * CE100 Algorithms and Programming II
	 */
	private String course;
	/**
	 * Recep Tayyip Erdogan University
	 */
	private String university;
	/**
	 * Faculty of Engineering and Architecture
	 */
	private String faculty;
	/**
	 * Computer Engineering Dept.
	 */
	private String department;

	/**
	 * Generates a Markdown header with the given metadata.
	 *
	 * @param markdownHeader the metadata for the Markdown header
	 * @return the Markdown header as a String
	 */
	public String generateMarkdownHeader() {

//		String header = "Week-1 (Introduction to Analysis of Algorithms)";
//		String footer = "RTEU CE100 Week-1";
//		String author = "Asst. Prof. Dr. Uğur CORUH";
//		String license = "WTFPL";
//		String language = "en-US";
//		String semester = "Spring Semester, 2022-2023";
//
//		String course = "CE100 Algorithms and Programming II";
//		String university = "Recep Tayyip Erdogan University";
//		String faculty = "Faculty of Engineering and Architecture";
//		String department = "Computer Engineering Dept.";

		StringBuilder markdownBuilder = new StringBuilder();

		// Add metadata
		markdownBuilder.append("marp: true\n");
		markdownBuilder.append("theme: default\n");
		markdownBuilder.append("style: |\n");
		markdownBuilder.append("    img[alt~=\"center\"] {\n");
		markdownBuilder.append("      display: block;\n");
		markdownBuilder.append("      margin: 0 auto;\n");
		markdownBuilder.append("      background-color: transparent!important;\n");
		markdownBuilder.append("    }\n");
		markdownBuilder.append("_class: lead\n");
		markdownBuilder.append("paginate: true\n");
		markdownBuilder.append("backgroundColor: #fff\n");
		markdownBuilder.append("backgroundImage: url('../images/hero-background.svg')\n");
		markdownBuilder.append("header: '" + this.course + "'\n");
		markdownBuilder.append("footer: '![height:50px](../images/rteu_logo.jpg) " + this.footer + "'\n");
		markdownBuilder.append("title: \"" + this.course + "\"\n");
		markdownBuilder.append("author: \"Author: " + this.author + "\"\n");
		markdownBuilder.append("date:\n");
		markdownBuilder.append("subtitle: \"" + this.header + "\"\n");
		markdownBuilder.append("geometry: \"left=2.54cm,right=2.54cm,top=1.91cm,bottom=1.91cm\"\n");
		markdownBuilder.append("titlepage: true\n");
		markdownBuilder.append("titlepage-color: \"FFFFFF\"\n");
		markdownBuilder.append("titlepage-text-color: \"000000\"\n");
		markdownBuilder.append("titlepage-rule-color: \"CCCCCC\"\n");
		markdownBuilder.append("titlepage-rule-height: 4\n");
		markdownBuilder.append("logo: \"../images/rteu_logo.jpg\"\n");
		markdownBuilder.append("logo-width: 100 \n");
		markdownBuilder.append("page-background:\n");
		markdownBuilder.append("page-background-opacity:\n");
		markdownBuilder.append("links-as-notes: true\n");
		markdownBuilder.append("lot: true\n");
		markdownBuilder.append("lof: true\n");
		markdownBuilder.append("listings-disable-line-numbers: true\n");
		markdownBuilder.append("listings-no-page-break: false\n");
		markdownBuilder.append("disable-header-and-footer: false\n");
		markdownBuilder.append("header-left:\n");
		markdownBuilder.append("header-center:\n");
		markdownBuilder.append("header-right:\n");
		markdownBuilder.append("footer-left: \"© " + this.author + "\"\n");
		markdownBuilder.append("footer-center: \"License: " + this.license + "\"\n");
		markdownBuilder.append("footer-right:\n");
		markdownBuilder.append("subparagraph: true\n");
		markdownBuilder.append("lang: " + this.language + "\n");
		markdownBuilder.append("\n");

		// Add Markdown content
		markdownBuilder.append("math: katex\n");
		markdownBuilder.append("\n");
		markdownBuilder.append("---\n");
		markdownBuilder.append("\n");
		markdownBuilder.append("<!-- _backgroundColor: aqua -->\n");
		markdownBuilder.append("\n");
		markdownBuilder.append("<!-- _color: orange -->\n");
		markdownBuilder.append("\n");
		markdownBuilder.append("<!-- paginate: false -->\n");
		markdownBuilder.append("\n");
		markdownBuilder.append("## " + this.university + "\n");
		markdownBuilder.append("\n");
		markdownBuilder.append("## " + this.faculty + "\n");
		markdownBuilder.append("\n");
		markdownBuilder.append("## " + this.department + "\n");
		markdownBuilder.append("\n");
		markdownBuilder.append("## " + this.course + "\n");
		markdownBuilder.append("\n");
		markdownBuilder.append("## " + this.header + "\n");
		markdownBuilder.append("\n");
		markdownBuilder.append("#### " + this.semester + "\n");
		markdownBuilder.append("\n");
		markdownBuilder.append("---\n");
		markdownBuilder.append("\n");
		markdownBuilder.append("<!-- paginate: true -->\n");

		// put rest of documents...

		return markdownBuilder.toString();

	}

}
