package com.br.timetabler.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class MyDataHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;  
	public static final String DATABASE_NAME = "UserNotes1.db"; 
    public static class DBItem implements BaseColumns {  
    public static final String TABLE = "notes";  
    public static final String NOTE_COL = "note";  
    public static final String TIME_COL = "time";  
       }  
	
    
    private static final String CREATE_STR = "CREATE TABLE " +   
    		 DBItem.TABLE + " (" + DBItem._ID +   
    		 " INTEGER PRIMARY KEY AUTOINCREMENT, " +  
    		 DBItem.TIME_COL + " TEXT, " +  
    		 DBItem.NOTE_COL + " TEXT);"; 
	
	
    public MyDataHelper(Context context) {  
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  
       }  
    
    
    public void onCreate(SQLiteDatabase db) {  
        db.execSQL(CREATE_STR);  
       }  
    
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
        db.execSQL("DROP TABLE IF EXISTS " + DBItem.TABLE);  
        onCreate(db);  
       }  
       public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
        onUpgrade(db, oldVersion, newVersion);  
       }  
}
