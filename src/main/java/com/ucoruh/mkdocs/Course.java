package com.ucoruh.mkdocs;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

//Course course = new Course("Physics", "PHY101", new String[]{"Monday", "Wednesday"}, new int[]{9, 11}, new String[]{"Tuesday", "Thursday"}, new int[]{13, 15});
@Data
@Accessors(fluent = true)
public class Course {

	private String university = "Recep Tayyip Erdogan University";
	private String faculty = "Faculty of Engineering and Architecture";
	private String department = "Unknown";
	private String lecturerTitleNameSurname = "Unknown";
	private String lecturerContactEmail = "Unknown";
	private String lecturerOffice = "Unknown";
	private String[] lecturerOfficeDays = new String[] { "Unknown" };
	private int[] lecturerOfficeHours = new int[] { -1 };
	private String lecturerOfficeDaysDescription = "Meetings will be scheduled over Google Meet or "
			+ "Microsoft Teams with your university account and email and performed via demand emails. "
			+ "Please send emails with the subject starting with *[<Course Code>]* tag for the fast response"
			+ " and write formal, clear, and short emails";
	private String googleClassroomCode = "Unknown";
	private String microsoftTeamsCode = "Unknown";

	private String courseGithubUserName = "Unknown";
	private String courseGithubRepoName = "UnKnown";
	
	private String courseName = "Unknown";
	private String courseCode = "Unknown";
	private String courseClassroomLocation = "Unknown";
	private SemesterType courseSemester = SemesterType.NONE;
	private int courseYear = -1;

	boolean theoryExist = false;
	private String[] theoryDays = new String[] { "Unknown" };
	private int[] theoryHours = new int[] { -1 };

	boolean labExist = false;
	private String[] labDays = new String[] { "Unknown" };
	private int[] labHours = new int[] { -1 };

	private LanguageType courselanguage = LanguageType.EN_US;
	private int courseCredit = -1;
	private String[] prerequisite = new String[] { "Not Exist" };
	private String[] corequisite = new String[] { "Not Exist" };
	private String[] requirement = new String[] { "Not Exist" };

	private String courseDescription = "Unknown";

	private String[] courseLearningOutcomes = new String[] { "Unknown" };

	private String[] courseTopics = new String[] { "Not Exist" };

	private String[] courseTextbooks = new String[] { "This course does not require a coursebook." };

	private String[] courseHardwareEquipments = new String[] {
			"This course does not require additional hardware or equipment." };

	private String courseGradingSystem = "Unknown";

	private String courseInstructionalMethods = "The basic teaching method of this course will be planned to be face-to-face in the classroom, and support resources, home works, and announcements will be shared over google classroom. In unexpected situations course will be planned for online for disaster scenarios. Students are expected to be in the university if face-to-face method selected. This responsibility is very important to complete this course with success. If pandemic situation changes and distance education is required during this course, this course will be done using synchronous and asynchronous distance education methods. In this scenario, students are expected to be in the online platform, zoom, or meet at the time specified in the course schedule. Attendance will be taken.";

	private String courseLateHomeworkPolicy = "Throughout the semester, assignments must be submitted as specified by the announced deadline."
			+ " Overdue assignments will not be accepted. "
			+ "Unexpected situations must be reported to the instructor for late home works by students.";

	private String coursePlatformAndCommunication = "Google Classroom and Microsoft Teams will be used as a course learning management system. "
			+ "All electronic resources and announcements about the course will be shared on this platform. "
			+ "It is very important to check the course page daily, access the necessary resources and announcements, "
			+ "and communicate with the instructor as you need course skills  to complete the course with success";

	private String coursePlagiarismPolicy = "Academic Integrity is one of the most important principles of our University. "
			+ "Anyone who breaches the principles of academic honesty is severely punished. "
			+ "It is natural to interact with classmates and others t.\"study together\". "
			+ "It may also be the case where a student asks to help from someone else, "
			+ "paid or unpaid, better understand a difficult topic or a whole course. "
			+ "However, what is the borderline between \"studying together\" " + "or \"taking private lessons\" "
			+ "and \"academic dishonesty\"? " + "When is it plagiarism, when is it cheating? "
			+ "It is obvious that looking at another student's paper or any source other than "
			+ "what is allowed during the exam is cheating and will be punished. "
			+ "However, it is known that many students come to university with "
			+ "very little experience concerning what is acceptable and "
			+ "what counts as \"copying,\"\" especially for assignments."
			+ "The following are attempted as guidelines for "
			+ "the Faculty of Engineering and Architecture students to highlight "
			+ "the philosophy of academic honesty for assignments for " + "which the student will be graded. "
			+ "Should a situation arise which is not described below, "
			+ "the student is advised to ask the instructor or assistant of "
			+ "the course whether what they intend to do would remain within "
			+ "the framework of academic honesty or not.";

	private String[] courseAcceptableAssignmentActions = new String[] {
			"Communicating with classmates about the assignment to understand it better",
			"Putting ideas, quotes, paragraphs, small pieces of code (snippets) that you find online or elsewhere into your assignment, provided that these are not themselves the whole solution to the assignment you cite the origins of these",
			"Asking sources for help in guiding you for the English language content of your assignment.",
			"Sharing small pieces of your assignment in the classroom to create a class discussion on some controversial topics.",
			"Turning to the web or elsewhere for instructions, references, and solutions to technical difficulties, but not for direct answers to the assignment",
			"Discuss solutions to assignments with others using diagrams or summarized statements but not actual text or code.",
			"Working with (and even paying) a tutor to help you with the course, provided the tutor does not do your assignment for you."};

	private String[] courseRestrictedAssignmentActions = new String[] { 
			"Ask a classmate to see their solution to a problem before submitting your own.", 
			"Failing to cite the origins of any text (or code for programming courses) that you discover outside of the course's lessons and integrate into your work", 
			"You are giving or showing a classmate your solution to a problem when the classmate is struggling to solve it." };

	private String courseExpectationFromStudents = "You are expected to attend classes on time by completing weekly course requirements"
			+ " (readings and assignments) during the semester. "
			+ "The main communication channel between the instructor and the students email emailed. "
			+ "Please send your questions to the instructor's email address about "
			+ "the course via the email address provided to you by the university. "
			+ "***Ensure that you include the course name in the subject field of your message and your name in the text field***. "
			+ "In addition, the instructor will contact you via email if necessary. "
			+ "For this reason, it is very important to check your email address every day for healthy communication.";

	private String courseContentUpdatePolicy = "If deemed necessary, changes in the lecture content or course schedule can be made. "
			+ "If any changes are made in the scope of this document, " + "the instructor will inform you about this.";

	private String courseBolognaEnWebUrl;

	private String courseBolognaTrWebUrl;

	private Date courseStartDate;

	private Date midtermStartDate;

	private Date midtermEndDate;

	private Date finalStartDate;

	private Date finalEndDate;

	private Date[] holidays;

	public String getTheorySchedule() {
		return String.format("%s theory lectures are on %s from %d AM to %d AM.", this.courseName,
				String.join(", ", this.theoryDays), this.theoryHours[0], this.theoryHours[1]);
	}

	public String getLabSchedule() {
		return String.format("%s lab sessions are on %s from %d PM to %d PM.", this.courseName,
				String.join(", ", this.labDays), this.labHours[0], this.labHours[1]);
	}

}
