package com.br.timetabler.model;

import java.io.Serializable;

public class Course implements Serializable {
	private static final long serialVersionUID = 1L;
	private String courseId;
	private String courseAcronyms;
	private String courseName;
     
    public Course(String courseId, String courseAcronyms, String courseName) {
        super();
        this.courseId = courseId;
        this.courseAcronyms = courseAcronyms;
        this.courseName = courseName;
        }
 
    /**
     * @return the title of the video
     */
    public String getCourseId(){
        return courseId;
    }
    
    public String getCourseAcronyms(){
        return courseAcronyms;
    }
    
    public String getCourseName(){
        return courseName;
    }

}