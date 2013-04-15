package com.br.timetabler.model;

import java.io.Serializable;

/**
 * This is the 'library' of all the users videos
 *
 * @author paul.blundell
 */
public class School implements Serializable {
	private static final long serialVersionUID = 1L;
	private String schoolId;
	private String schoolName;
     
    public School(String schoolId, String schoolName) {
        super();
        this.schoolId = schoolId;
        this.schoolName = schoolName;
    }
 
    /**
     * @return the title of the video
     */
    public String getSchoolId(){
        return schoolId;
    }
    
    /**
     * @return the title of the video
     */
    public String getSchoolName(){
        return schoolName;
    }

}
