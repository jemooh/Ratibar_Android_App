package com.br.timetabler.model;

import java.io.Serializable;

/**
 * This is the 'library' of all the users videos
 *
 * @author paul.blundell
 */
public class Notes implements Serializable {
	private static final long serialVersionUID = 1L;
	private String noteId;
	private String timeId;
    private String noteText;
    private String timeText;
     
    public Notes(String noteId, String timeId, String noteText, String timeText) {
        super();
        this.noteId = noteId;
        this.timeId = timeId;
        this.noteText = noteText;
        this.timeText = timeText;
        }
 
    /**
     * @return the title of the video
     */
    public String getNoteId(){
        return noteId;
    }
    
    /**
     * @return the title of the video
     */
    public String getTimeId(){
        return timeId;
    }
 
    /**
     * @return the title of the video
     */
    public String getNoteText(){
        return noteText;
    }
 
    /**
     * @return the url to this video on youtube
     */
    public String getNoteTime() {
        return timeText;
    }
 
    
    
}
