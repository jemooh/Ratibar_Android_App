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
    private String dayId;
    private String yPos;
     
    public Lesson(String lessonId, String code, String title, String teacher, 
    		String starttime, String endtime, String location, String dayId, String yPos) {
        super();
        this.lessonId = lessonId;
        this.code = code;
        this.title = title;
        this.teacher = teacher;
        this.starttime = starttime;
        this.endtime = endtime;
        this.location = location;
        this.dayId = dayId;
        this.yPos = yPos;
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
 
    public String getTitle(){
        return title;
    }
 
    public String getTeacher() {
        return teacher;
    }
 
    public String getStarttime() {
        return starttime;
    }
 
    public String getEndtime() {
        return endtime;
    }
    
    public String getLocation() {
        return location;
    }
    
    public String getDayId() {
        return dayId;
    }
    
    public String GetyPos() {
    	return yPos;
    }
}
