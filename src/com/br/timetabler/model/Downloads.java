package com.br.timetabler.model;

import java.io.Serializable;

/**
 * This is the 'library' of all the users videos
 *
 * @author paul.blundell
 */
public class Downloads implements Serializable {
	private static final long serialVersionUID = 1L;
	private String downloadId;
	private String title;
    private String link;
     
    public Downloads(String downloadId, String title, String link) {
        super();
        this.downloadId = downloadId;
        this.title = title;
        this.link = link;
        }
 
    /**
     * @return the title of the video
     */
    public String getDownloadId(){
        return downloadId;
    }
    
    /**
     * @return the title of the video
     */
    public String getTitle(){
        return title;
    }
 
    /**
     * @return the title of the video
     */
    public String getLink(){
        return link;
    }
 
   
    
}
