package edu.umd.cs.gradeculator.model;

import java.io.Serializable;
import java.util.Date;

import static edu.umd.cs.gradeculator.model.Work.Category.*;

/**
 * Created by howardksw1 on 4/5/17.
 */

public class Work implements Serializable {
    private Category category;
    private String title;
    private double weight;
    private double earned_points;
    private double possible_points;
    private double grade;
    private boolean completeness = false;
    private Date due_Date;
    public Work(String title) {
        this.title = title;
    }

    public double getGrade(){
        if(completeness){
            grade=earned_points/possible_points;
            return grade;
        }else{
            return -1;
        }
    }

    // getter of title
    public Date getDueDate() { return due_Date; }

    // setter of title
    public void setDueDate(Date due_Date) { this.due_Date = due_Date; }

    // getter of title
    public String getTitle() { return title; }

    // setter of title
    public void setTitle(String title) { this.title = title; }

    // setter of category
    public void setCategory(Category category) { this.category = category; }

    // getter of category
    public Category getCategory() { return category; }

    // setter of weight
    public void setWeight(double weight) { this.weight = weight; }

    // getter of weight
    public double getWeight() { return weight; }

    // setter of earned points
    public void setEarned_points(double earned_points) { this.earned_points = earned_points; }

    // getter of earned points
    public double getEarned_points() { return earned_points; }

    // setter of possible points
    public void setPossible_points(double possible_points) { this.possible_points = possible_points; }

    // getter of possible points
    public double getPossible_points() { return possible_points; }

    public void setCompleteness(boolean completeness) { this.completeness = completeness;}

    public Boolean getCompleteness() { return completeness; }

    public static String toCategory_String(Category category) {
        String c = "";
        switch (category) {
            case EXAM:
                c = "EXAM";
                break;
            case ASSIGNMENT:
                c = "ASSIGNMENT";
                break;
            case QUIZ:
                c = "QUIZ";
                break;
            case PROJECT:
                c = "PROJECT";
                break;
            case EXTRA:
                c = "EXTRA";
                break;
        }
        return c;
    }

    public static Category toString_Category(String s) {
        Category c = null;
        switch (s) {
            case "EXAM":
                c = EXAM;
                break;
            case "ASSIGNMENT":
                c = ASSIGNMENT;
                break;
            case "QUIZ":
                c = QUIZ;
                break;
            case "PROJECT":
                c = PROJECT;
                break;
            case "EXTRA":
                c = EXTRA;
                break;
        }
        return c;
    }

    public void setCompleteness() {
        completeness = true;
    }

    public enum Category {
        EXAM, ASSIGNMENT, QUIZ, PROJECT, EXTRA
    }


}
