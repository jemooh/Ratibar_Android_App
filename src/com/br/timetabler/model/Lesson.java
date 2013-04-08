package com.br.timetabler.model;

import java.io.Serializable;

/**
 * This is the 'library' of all the users videos
 *
 * @author paul.blundell
 */
public class Lesson implements Serializable {
	/**
	 * code, title, teacher, starttime, endtime, location,
	 */
	private static final long serialVersionUID = 1L;
	private String lessonId;
    private String code;
    private String title;
    private String teacher;
    private String starttime, endtime;
    private String location;
     
    public Lesson(String lessonId, String code, String title, String teacher, String starttime, String endtime, String location) {
        super();
        this.lessonId = lessonId;
        this.code = code;
        this.title = title;
        this.teacher = teacher;
        this.starttime = starttime;
        this.endtime = endtime;
        this.location = location;
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
 
    /**
     * @return the title of the video
     */
    public String getTitle(){
        return title;
    }
 
    /**
     * @return the url to this video on youtube
     */
    public String getTeacher() {
        return teacher;
    }
 
    /**
     * @return the DescriptionActivity to this video on youtube
     */
    public String getStarttime() {
        return starttime;
    }
 
    /**
     * @return the Duration to this video on youtube
     */
    public String getEndtime() {
        return endtime;
    }
 
    /**
     * @return the thumbUrl of a still image representation of this video
     */
    public String getLocation() {
        return location;
    }
}
