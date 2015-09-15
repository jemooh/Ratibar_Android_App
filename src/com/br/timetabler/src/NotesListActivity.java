package com.br.timetabler.src;
import java.util.List;  

import com.br.timetabler.R;
import com.br.timetabler.model.Note;
import com.br.timetabler.util.NotesDataHelper;

import android.app.AlertDialog;  
import android.app.ListActivity;  
import android.content.DialogInterface;  
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;  
import android.view.View;  
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;  
import android.widget.AdapterView.OnItemClickListener;  
import android.widget.ArrayAdapter;  
import android.widget.Button;
import android.widget.EditText;  
import android.widget.ListView;  
import android.app.AlertDialog;  
import android.app.ListActivity;  
import android.content.DialogInterface;  
import android.view.Menu;  
import android.view.View;  
import android.widget.AdapterView;  
import android.widget.AdapterView.OnItemClickListener;  
import android.widget.ArrayAdapter;  
import android.widget.EditText;  
import android.widget.ListView; 


		public class NotesListActivity extends ListActivity {
			
		    private NoteManager noteMan;  
		    private ListView theList;  
		    private int notePosn;  
			@Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.one_lesson_notes);
				
			    noteMan = new NoteManager(getApplicationContext());  
				//btnAddnotes = (Button) findViewById(R.id.btnAddnotes);
			    Button	btnAddnotes = (Button) findViewById(R.id.btnAddnotes);
				

				
			    theList = (ListView) findViewById(android.R.id.list);
			    List<Note> existingNotes = noteMan.getNotes();
			    ArrayAdapter<Note> noteAdapt = new ArrayAdapter<Note>(  
			    		 this, android.R.layout.simple_list_item_1, existingNotes); 
			    
			     setListAdapter(noteAdapt);
			     theList.setOnItemClickListener(new OnItemClickListener(){  
			         @Override  
			         public void onItemClick(AdapterView<?> arg0, View view,  
			          int position, long id) {  
			        	 
			        	 notePosn=position;
			        	 AlertDialog.Builder deleteAlert = new   
			        			 AlertDialog.Builder(NotesListActivity.this);  
			        			deleteAlert.setTitle("Delete Note");  
			        			deleteAlert.setMessage("Do you want to delete this note?");
			        			
			        			
			        		    deleteAlert.setPositiveButton("OK",   
			        		    	     new DialogInterface.OnClickListener() {  
			        		    	     public void onClick(DialogInterface dialog, int whichButton) {  
			        		    	      ArrayAdapter<Note> noteAdapt = (ArrayAdapter<Note>)  
			        		    	        NotesListActivity.this.getListAdapter();  
			        		    	      Note clickedNote = (Note)noteAdapt.getItem(notePosn);  
			        		    	      int noteDeleted = noteMan.deleteNote  
			        		    	       (clickedNote.getNoteID());  
			        		    	      noteAdapt.remove(clickedNote);  
			        		    	      noteAdapt.notifyDataSetChanged();  
			        		    	      setListAdapter(noteAdapt);  
			        		    	     }  
			        		    	    });  
			        		    
			        		    deleteAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {  
			        		        public void onClick(DialogInterface dialog, int which){  
			        		         dialog.cancel();  
			        		        }  
			        		       });  
			        		    
			        		    deleteAlert.show();  
			        }  
			        });  
			     
			 
			}
			
			public void onClickAdd (View v){  
				
				AlertDialog.Builder addAlert = new AlertDialog.Builder(this);  
				addAlert.setTitle("New Note");  
				addAlert.setMessage("Enter your note:"); 
				
				final EditText noteIn = new EditText(this);  
				addAlert.setView(noteIn);
				
				addAlert.setPositiveButton("OK",   
						 new DialogInterface.OnClickListener() {  
						 public void onClick(DialogInterface dialog, int whichButton) {  
						  String noteInput = noteIn.getText().toString();  
						  Note inputNote = new Note();  
						  inputNote.setNoteText(noteInput);  
						  long addedID = noteMan.addNewNote(inputNote);  
						  inputNote.setNoteID(addedID);  
						  ArrayAdapter<Note> noteAdapt = (ArrayAdapter<Note>)  
						    NotesListActivity.this.getListAdapter();  
						  noteAdapt.add(inputNote);  
						 }  
						}); 
				  
			    addAlert.setNegativeButton("Cancel",   
			    	     new DialogInterface.OnClickListener() {  
			    	     public void onClick(DialogInterface dialog, int which){  
			    	      dialog.cancel();  
			    	     }  
			    	    });  
			    addAlert.show();  
			} 
		
			
		}
		


