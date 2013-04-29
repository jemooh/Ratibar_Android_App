package com.br.timetabler.model;

import java.io.Serializable;

/**
 * This is the 'library' of all the assignments
 *
 * @author paul.blundell
 */
public class Assignment implements Serializable {
	private static final long serialVersionUID = 1L;
	private String unitId, unitCode;
	private String dateCreated;
    private String dateDue;
    private String description;
    boolean status;
     
    public Assignment(String unitId, String unitCode, String dateCreated, String dateDue, String description, boolean status) {
        super();
        this.unitId = unitId;
        this.dateCreated = dateCreated;
        this.unitCode = unitCode;
        this.dateDue = dateDue;
        this.description = description;
    }
 
    /**
     * @return the UnitId of the assignment
     */
    public String getUnitId(){
        return unitId;
    }
    
    /**
     * @return the UnitId of the assignment
     */
    public String getUnitCode(){
        return unitCode;
    }
    
    /**
     * @return the Date the assignment was Created
     */
    public String getDateCreated(){
        return dateCreated;
    }
 
    /**
     * @return the Date the assignment is due
     */
    public String getDateDue(){
        return dateDue;
    }
 
    /**
     * @return the description to this assignment
     */
    public String getDescription() {
        return description;
    }
 
    /**
     * @return the status to this assignment, whether completed or not
     */
    public boolean getStatus() {
        return status;
    }
 
    
 
    
}
