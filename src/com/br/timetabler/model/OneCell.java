package com.br.timetabler.model;

import java.io.Serializable;

/**
 * This is the 'library' of all the users videos
 *
 * @author joe
 */
public class OneCell implements Serializable {
	private static final long serialVersionUID = 1L;
	private int gridId;
    private int gridPos;

     
    public OneCell(int gridId, int gridPos) {
        super();
        this.gridId = gridId;
        this.gridPos = gridPos;
        }
 
    /**
     * @return the identifier of this cell
     */
    public int getGridId(){
        return gridId;
    }
    
    /**
     * @return the title of the video
     */
    public int getGridPos(){
        return gridPos;
    }
}
