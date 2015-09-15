package com.br.timetabler.model;

import java.io.Serializable;
import java.util.List;

public class DownloadLibrary implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// The username of the owner of the library
    private String user;
    // A list of lessons that the user owns
    private List<Downloads> downloads;
     
    public DownloadLibrary(String user, List<Downloads> downloads) {
        this.user = user;
        this.downloads = downloads;
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
    public List<Downloads> getDownloads() {
        return downloads;
    }
}