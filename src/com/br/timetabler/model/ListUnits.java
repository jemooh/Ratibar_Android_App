package com.br.timetabler.model;

import java.io.Serializable;

/**
 * This is the 'library' of all the users videos
 *
 * @author paul.blundell
 */
public class ListUnits implements Serializable {
	/**
	 * code, title, teacher, starttime, endtime, location,
	 */
	private static final long serialVersionUID = 1L;
	private String lessonId;
    private String code;
    private String colorband;
    private String title;
  
     
    public ListUnits(String lessonId, String code,String colorband, String title) {
        super();
        this.lessonId = lessonId;
        this.code = code;
        this.colorband = colorband;
        this.title = title;
       
    }
 
    /**
     * @return the title of the video
     */
    public String getLessonId(){
        return lessonId;
    }
    
    /**
     * @return the title of the video
     */
    public String getCode(){
        return code;
    }
    
    public String getColorband(){
        return colorband;
    }
 
    public String getTitle(){
        return title;
    }
 
   
 

}
