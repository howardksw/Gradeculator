package edu.umd.cs.gradeculator.model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import edu.umd.cs.gradeculator.model.Work.Category;

import static edu.umd.cs.gradeculator.model.Course.Grade.A;
import static edu.umd.cs.gradeculator.model.Course.Grade.A_MINUS;
import static edu.umd.cs.gradeculator.model.Course.Grade.A_PLUS;
import static edu.umd.cs.gradeculator.model.Course.Grade.B;
import static edu.umd.cs.gradeculator.model.Course.Grade.B_MINUS;
import static edu.umd.cs.gradeculator.model.Course.Grade.B_PLUS;
import static edu.umd.cs.gradeculator.model.Course.Grade.C;
import static edu.umd.cs.gradeculator.model.Course.Grade.C_MINUS;
import static edu.umd.cs.gradeculator.model.Course.Grade.C_PLUS;
import static edu.umd.cs.gradeculator.model.Work.Category.ASSIGNMENT;
import static edu.umd.cs.gradeculator.model.Work.Category.EXAM;
import static edu.umd.cs.gradeculator.model.Work.Category.EXTRA;
import static edu.umd.cs.gradeculator.model.Work.Category.PROJECT;
import static edu.umd.cs.gradeculator.model.Work.Category.QUIZ;

public class Course implements Serializable {
	private String id;
	private String title;
	private String identifier;
	private ArrayList<Work> exams;
	private ArrayList<Work> quizzes;
	private ArrayList<Work> projects;
	private ArrayList<Work> assignments;
	private ArrayList<Work> extra;
	private double exam_weight;
	private double quiz_weight;
	private double project_weight;
	private double assignment_weight;
	private double extra_weight;
	private Grade grade = A; // default
	private double current_grade;
	private double desire_grade;
	private int credit;
	private Boolean equal_weight_exam,equal_weight_quiz,equal_weight_assignment,
			equal_weight_project,equal_weight_extra;

	private boolean setGrade = false;
	// check if we set grade yet, only after seting grade we can fill in desired grade

	public Course() {
		id = UUID.randomUUID().toString();
		exams = new ArrayList<>();
		quizzes = new ArrayList<>();
		projects = new ArrayList<>();
		assignments = new ArrayList<>();
		extra = new ArrayList<>();
		exam_weight = quiz_weight = assignment_weight = project_weight = extra_weight = 0.0;
		equal_weight_assignment = equal_weight_quiz = equal_weight_exam =equal_weight_project=equal_weight_extra = true;
	}

	public Course(String title, String identifier, Grade grade, int credit) {
		id = UUID.randomUUID().toString();
		this.title = title;
		this.identifier = identifier;
		this.grade = grade;
		this.credit = credit;
		exams = new ArrayList<>();
		quizzes = new ArrayList<>();
		projects = new ArrayList<>();
		assignments = new ArrayList<>();
		extra = new ArrayList<>();
		setDesire_grade(grade);
		exam_weight = quiz_weight = assignment_weight = project_weight = extra_weight = 0.0;
		equal_weight_assignment = equal_weight_quiz = equal_weight_exam =equal_weight_project=equal_weight_extra = true;
	}

	public void setEqual_weight_exam(boolean a){
		equal_weight_exam=a;
	}

	public void setEqual_weight_quiz(boolean a){
		equal_weight_quiz=a;
	}

	public void setEqual_weight_assignment(boolean a){
		equal_weight_assignment=a;
	}

	public void setEqual_weight_project(boolean a){
		equal_weight_project=a;
	}

	public void setEqual_weight_extra(boolean a){
		equal_weight_extra=a;
	}

	public Boolean getEqual_weight_exam(){
			return equal_weight_exam;
	}

	public Boolean getEqual_weight_quiz(){
		return equal_weight_quiz;
	}

	public Boolean getEqual_weight_assignment(){
		return equal_weight_assignment;
	}

	public Boolean getEqual_weight_project(){
		return equal_weight_project;
	}

	public Boolean getEqual_weight_extra(){
		return equal_weight_extra;
	}

	public double getExam_weight() {
		return exam_weight;
	}

	public double getQuiz_weight() {
		return quiz_weight;

	}

	public double getAssignment_weight() {
		return assignment_weight;
	}

	public double getProject_weight() {
		return project_weight;
	}

	public double getExtra_weight() {
		return extra_weight;
	}

	public void setExam_weight(double weight) {
		exam_weight = weight;
	}

	public void setQuiz_weight(double weight) {
		quiz_weight = weight;
	}

	public void setAssignments_weight(double weight) {
		assignment_weight = weight;
	}

	public void setProject_weight(double weight) {
		project_weight = weight;
	}

	public void setExtra_weight(double weight) {
		extra_weight = weight;
	}

	public ArrayList<Work> getExams() {
		return exams;
	}

	public ArrayList<Work> getQuzs() {
		return quizzes;
	}

	public ArrayList<Work> getProjs() {
		return projects;
	}

	public ArrayList<Work> getAssigs() {
		return assignments;
	}

	public ArrayList<Work> getExtra() {
		return extra;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public int getCredit() {
		return credit;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public int getGradePosition() {
		switch (grade) {
			case A_PLUS:
				return 0;
			case A:
				return 1;
			case A_MINUS:
				return 2;
			case B_PLUS:
				return 3;
			case B:
				return 4;
			case B_MINUS:
				return 5;
			case C_PLUS:
				return 6;
			case C:
				return 7;
			case C_MINUS:
				return 8;
			default:
				return 1;
		}
	}

	public void setGrade(int position) {
		switch (position) {
			case 0:
				this.grade = grade.A_PLUS;
				break;
			case 1:
				this.grade = A;
				break;
			case 2:
				this.grade = grade.A_MINUS;
				break;
			case 3:
				this.grade = grade.B_PLUS;
				break;
			case 4:
				this.grade = grade.B;
				break;
			case 5:
				this.grade = grade.B_MINUS;
				break;
			case 6:
				this.grade = grade.C_PLUS;
				break;
			case 7:
				this.grade = grade.C;
				break;
			case 8:
				this.grade = grade.C_MINUS;
				break;
			default:
				this.grade = A;
				break;
		}
	}


	public void setCurrent_grade(double grade) {
		this.current_grade = grade;
	}

	public double getDesire_grade() {
		return desire_grade;
	}

	public Grade getDesire_grade_inLetter(double grade) {
		Grade g = A;
		if(grade >= 97) {
			g = A_PLUS;
		}else if(grade >= 93) {
			g = A;
		}else if(grade >= 90) {
			g = A_MINUS;
		}else if(grade >= 87) {
			g = B_PLUS;
		}else if(grade >= 83) {
			g = B;
		}else if(grade >= 80) {
			g = B_MINUS;
		}else if(grade >= 77) {
			g = C_PLUS;
		}else if(grade >= 73) {
			g = C;
		}else if(grade >= 70) {
			g = C_MINUS;
		}
		return g;
	}
	public void setDesire_grade(Grade grade) {
		switch (grade) {
			case A_PLUS:
				this.desire_grade = 97;
			case A:
				this.desire_grade = 93;
			case A_MINUS:
				this.desire_grade = 90;
			case B_PLUS:
				this.desire_grade = 87;
			case B:
				this.desire_grade = 83;
			case B_MINUS:
				this.desire_grade = 80;
			case C_PLUS:
				this.desire_grade = 77;
			case C:
				this.desire_grade = 73;
			case C_MINUS:
				this.desire_grade = 70;
			default:
				this.desire_grade = 93;
		}
	}

	public double getCurrent_grade() {
		return current_grade;
	}

	// check if the work element exists in the works data structure
	private Work containsWork(ArrayList<Work> works, String title) {
		for (Work w : works) {
			if (w.getTitle().equals(title)) {
				return w;
			}
		}
		return null;
	}

	// add a work into the data structure depending on the categories
	public boolean add(Work work) {
		switch (work.getCategory()) {
			case EXAM:
				if (containsWork(exams, work.getTitle()) == null) {
					exams.add(work);
					return true;
				}
				break;
			case ASSIGNMENT:
				if (containsWork(assignments, work.getTitle()) == null) {
					assignments.add(work);
					return true;
				}
				break;
			case QUIZ:
				if (containsWork(quizzes, work.getTitle()) == null) {
					quizzes.add(work);
					return true;
				}
				break;
			case PROJECT:
				if (containsWork(projects, work.getTitle()) == null) {
					projects.add(work);
					return true;
				}
				break;
			case EXTRA:
				if (containsWork(extra, work.getTitle()) == null) {
					extra.add(work);
					return true;
				}
				break;
			default:
				break;
		}
		return false;
	}

	// remove helper function to iterate through the data structure and locate the position
	private ArrayList<Work> removeWork(ArrayList<Work> works, String title) {
		for (int i = 0; i < works.size(); i++) {
			if (works.get(i).getTitle().equals(title)) {
				works.remove(works.get(i));
			}
		}
		return works;
	}

	// remove the item by the type of categories
	public void remove(Work.Category work, String target) {
		switch (work) {
			case EXAM:
				exams = removeWork(exams, target);
				break;
			case ASSIGNMENT:
				assignments = removeWork(assignments, target);
				break;
			case QUIZ:
				quizzes = removeWork(quizzes, target);
				break;
			case PROJECT:
				projects = removeWork(projects, target);
				break;
			case EXTRA:
				extra = removeWork(extra, target);
				break;
			default:
				break;
		}
	}

	public String toString() {
		String out = "";
		out += "	Assigs\n";
		for (Work work : this.getAssigs()) {
			out += "			";
			out += work;
			out += "\n";
		}
		out += "	Exams\n";
		for (Work work : this.getExams()) {
			out += "			";
			out += work;
			out += "\n";
		}
		out += "	Projs\n";
		for (Work work : this.getProjs()) {
			out += "			";
			out += work;
			out += "\n";
		}
		out += "	Quzs\n";
		for (Work work : this.getQuzs()) {
			out += "			";
			out += work;
			out += "\n";
		}
		out += "	Extras\n";
		for (Work work : this.getExtra()) {
			out += "			";
			out += work;
			out += "\n";
		}
		return out;
	}

	public double getCtGrade(Category cate,double totalweight) {
		double sum = 0;
		double tempGrade = 0;
		double allGrade = 0;
		int count=0;
		Boolean ifEqualW=null;
		ArrayList<Work> sumList=new ArrayList<Work>();
		switch (cate) {
			case ASSIGNMENT:
				sumList=getAssigs();
				ifEqualW=equal_weight_assignment;
				break;
			case EXAM:
				sumList=getExams();
				ifEqualW=equal_weight_exam;
				break;
			case PROJECT:
				sumList=getProjs();
				ifEqualW=equal_weight_project;
				break;
			case QUIZ:
				sumList=getQuzs();
				ifEqualW=equal_weight_quiz;
				break;
			case EXTRA:
				sumList=getExtra();
				ifEqualW=equal_weight_extra;
				break;
		}
		for (Work work : sumList){
			if(Double.compare(work.getGrade(), 0.0)>=0){
				count++;
			}
		}
		if(count==0){
			return 0.0;

		}
		if (ifEqualW) {
			for (Work work : sumList) {
				if (Double.compare(work.getGrade(), 0.0) >= 0) {
					tempGrade += work.getGrade()*(totalweight/sumList.size());
				}
			}
			Log.d("nana",Double.toString(tempGrade));
			return (tempGrade);
		}else{
			for(Work work : sumList){
				if(Double.compare(work.getGrade(), 0.0)>=0){

					tempGrade+=(work.getGrade()*work.getWeight());
				}
			}
			return tempGrade;
		}

	}

	public double getOverAll(){
		double currentWeight=0;
		currentWeight=getWeight(exams,equal_weight_exam,exam_weight,false)+getWeight(quizzes,equal_weight_quiz,quiz_weight,false)
				+getWeight(projects,equal_weight_project,project_weight,false)+getWeight(assignments,equal_weight_assignment,assignment_weight,false);
		return 100-(currentWeight-(getCtGrade(EXAM,exam_weight)+getCtGrade(ASSIGNMENT,assignment_weight)
				+getCtGrade(QUIZ,quiz_weight)
				+getCtGrade(PROJECT,project_weight))+getCtGrade(EXTRA,extra_weight));
	}
	public double soFarGrade(){
		double currentWeight=0;
		currentWeight=getWeight(exams,equal_weight_exam,exam_weight,false)+getWeight(quizzes,equal_weight_quiz,quiz_weight,false)
				+getWeight(projects,equal_weight_project,project_weight,false)+getWeight(assignments,equal_weight_assignment,assignment_weight,false);

		if(Double.compare(currentWeight, 0.0) != 1){
			return -1;
		}
		return 100*(getCtGrade(EXAM,exam_weight)+getCtGrade(ASSIGNMENT,assignment_weight)
                +getCtGrade(QUIZ,quiz_weight)
                +getCtGrade(PROJECT,project_weight))/currentWeight+getCtGrade(EXTRA,extra_weight);
    }
    public  boolean ckeckWeight(String cat,double d){
		switch(cat) {
			case "Exam":
				return Double.compare(getWeight(exams, equal_weight_exam, exam_weight,true) + d,exam_weight)<=0;
			case "Quiz":
				return Double.compare(getWeight(quizzes, equal_weight_quiz, quiz_weight,true) + d,quiz_weight)<=0;
			case "Assignment":
				return Double.compare(getWeight(assignments, equal_weight_assignment, assignment_weight,true) + d,assignment_weight)<=0;
			case "Project":
				return Double.compare(getWeight(projects, equal_weight_project, project_weight,true) + d,project_weight)<=0;
			case "Extra":
				return Double.compare(getWeight(extra, equal_weight_extra, extra_weight,true) + d,extra_weight)<=0;
		}
		return  false;
	}
    public double getWeight(ArrayList<Work> list,Boolean eq,Double w,boolean check){
		double weight= 0.0;
		for (Work work: list){
			if(check){
				weight+=work.getWeight();
			}else {
				if (work.getCompleteness()) {
					if (eq) {
						weight += w / (list.size());
					} else {
						weight += work.getWeight();
					}
				}
			}
		}
		return weight;
	}

	public enum Grade {
		A_PLUS, A, A_MINUS, B_PLUS, B, B_MINUS, C_PLUS, C, C_MINUS ;
	}
}

