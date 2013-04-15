package com.br.timetabler.model;

import java.io.Serializable;
import java.util.List;

public class UnitLibrary implements Serializable {
	private static final long serialVersionUID = 1L;
	// The username of the owner of the library
    private String user;
    // A list of lessons that the user owns
    private List<Unit> units;
     
    public UnitLibrary(String user, List<Unit> units) {
        this.user = user;
        this.units = units;
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
    public List<Unit> getUnits() {
        return units;
    }
}