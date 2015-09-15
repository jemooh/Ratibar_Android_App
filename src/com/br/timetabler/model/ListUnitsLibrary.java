package com.br.timetabler.model;

import java.io.Serializable;
import java.util.List;

public class ListUnitsLibrary implements Serializable {
	private static final long serialVersionUID = 1L;
	// The username of the owner of the library
    private String user;
    // A list of lessons that the user owns
    private List<ListUnits> lessons;
     
    public ListUnitsLibrary(String user, List<ListUnits> lessons) {
        this.user = user;
        this.lessons = lessons;
    }
 
    /**
     * @return the user name
     */
    public String getUser() {
        return user;
    }
 
    /**
     * @return the lessons
     */
    public List<ListUnits> getLessons() {
        return lessons;
    }
}