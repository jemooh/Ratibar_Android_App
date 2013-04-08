package com.br.timetabler.model;

import java.io.Serializable;
import java.util.List;

public class CommentLibrary implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// The username of the owner of the library
    private String user;
    // A list of lessons that the user owns
    private List<Comment> comments;
     
    public CommentLibrary(String user, List<Comment> comments) {
        this.user = user;
        this.comments = comments;
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
    public List<Comment> getComments() {
        return comments;
    }
}