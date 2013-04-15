package com.br.timetabler.model;

import java.io.Serializable;
import java.util.List;

public class SchoolLibrary implements Serializable {
	private static final long serialVersionUID = 1L;
	// The username of the owner of the library
    private String user;
    // A list of lessons that the user owns
    private List<School> schools;
     
    public SchoolLibrary(String user, List<School> schools) {
        this.user = user;
        this.schools = schools;
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
    public List<School> getSchools() {
        return schools;
    }
}
