package com.br.timetabler.model;

import java.io.Serializable;

public class Unit implements Serializable {
	private static final long serialVersionUID = 1L;
	private String unitId;
	private String unitName;
	private String unitAcronyms;
	private boolean selected;
     
    public Unit(String unitId, String unitName, String unitAcronyms, boolean selected) {
        super();
        this.unitId = unitId;
        this.unitName = unitName;
        this.unitAcronyms = unitAcronyms;
        this.selected = selected;
    }
 
    public String getUnitId(){
        return unitId;
    }

    public String getUnitName(){
        return unitName;
    }

    public String getUnitAcronyms(){
        return unitAcronyms;
    }
    public boolean isSelected() {
 		return selected;
 	}

 	public void setSelected(boolean selected) {
 		this.selected = selected;
 	}

}