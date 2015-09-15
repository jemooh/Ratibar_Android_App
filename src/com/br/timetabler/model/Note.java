package com.br.timetabler.model;

public class Note {  
    
    private long noteID;  
    private String noteText;  
     
    public void setNoteText(String userText){  
     noteText=userText;  
    }  
    public String getNoteText(){  
     return noteText;  
    }  
    public void setNoteID(long newID){  
     noteID=newID;  
    }  
    public long getNoteID(){  
     return noteID;  
    }  
    @Override  
    public String toString(){  
     return noteText;  
    }  
    
   
   }  