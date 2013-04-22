package com.br.timetabler.model;

import java.io.Serializable;
import java.util.List;

public class CourseLibrary implements Serializable {
	private static final long serialVersionUID = 1L;
	// The username of the owner of the library
    private String user;
    // A list of lessons that the user owns
    private List<Course> courses;
     
    public CourseLibrary(String user, List<Course> courses) {
        this.user = user;
        this.courses = courses;
    }
 
    /**
     * @return the user name
     */
    public String getUser() {
        return user;
    }
 
    /**
     * @return the courses
     */
    public List<Course> getCourses() {
        return courses;
    }

}
