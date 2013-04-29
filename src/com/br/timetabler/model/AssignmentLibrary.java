package com.br.timetabler.model;

import java.io.Serializable;
import java.util.List;

public class AssignmentLibrary implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// The username of the owner of the library
    private String user;
    // A list of lessons that the user owns
    private List<Assignment> assignments;
     
    public AssignmentLibrary(String user, List<Assignment> assignments) {
        this.user = user;
        this.assignments = assignments;
    }
 
    /**
     * @return the user name
     */
    public String getUser() {
        return user;
    }
 
    /**
     * @return the comments
     */
    public List<Assignment> getAssignments() {
        return assignments;
    }
}