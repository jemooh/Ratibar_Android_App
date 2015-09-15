package com.br.timetabler.model;

import java.io.Serializable;

/**
 * This is the 'library' of all the users videos
 *
 * @author paul.blundell
 */
public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;
	private String lessonId;
	private String commentId;
    private String creator;
    private String datetime;
    private String comment;
    private String thumbUrl;
     
    public Comment(String lessonId, String commentId, String creator, String datetime, String comment,String thumbUrl) {
        super();
        this.lessonId = lessonId;
        this.commentId = commentId;
        this.creator = creator;
        this.datetime = datetime;
        this.comment = comment;
        this.thumbUrl = thumbUrl;
        }
 
    /**
     * @return the title of the video
     */
    public String getLessonId(){
        return lessonId;
    }
    
    public String getUrl(){
        return thumbUrl;
    }
    /**
     * @return the title of the video
     */
    public String getCommentId(){
        return commentId;
    }
 
    /**
     * @return the title of the video
     */
    public String getCreator(){
        return creator;
    }
 
    /**
     * @return the url to this video on youtube
     */
    public String getDatetime() {
        return datetime;
    }
 
    /**
     * @return the DescriptionActivity to this video on youtube
     */
    public String getComment() {
        return comment;
    }
 
    
}
