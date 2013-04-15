package com.br.timetabler.model;

import java.io.Serializable;

public class Unit implements Serializable {
	private static final long serialVersionUID = 1L;
	private String unitId;
	private String unitName;
     
    public Unit(String unitId, String unitName) {
        super();
        this.unitId = unitId;
        this.unitName = unitName;
    }
 
    /**
     * @return the title of the video
     */
    public String getUnitId(){
        return unitId;
    }
    
    /**
     * @return the title of the video
     */
    public String getUnitName(){
        return unitName;
    }

}