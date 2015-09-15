package com.br.timetabler.src;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ListView;

import com.br.timetabler.model.Note;
import com.br.timetabler.util.NotesDataHelper;

import android.app.ListActivity;  
import android.content.DialogInterface;  
import android.content.Intent;
import android.view.Menu;  
import android.view.View;  
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;  
import android.widget.AdapterView.OnItemClickListener;  
import android.widget.ArrayAdapter;  

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;  
import android.app.ListActivity;  
import android.content.DialogInterface;  
import android.view.Menu;  
import android.view.View;  
import android.widget.AdapterView;  
import android.widget.AdapterView.OnItemClickListener;  
import android.widget.ArrayAdapter;  
import android.widget.EditText;  
import android.widget.ImageButton;
import android.widget.ListView; 

import org.json.JSONException;
import org.json.JSONObject;

import com.br.timetabler.R;
import com.br.timetabler.adapter.LessonNotesCursorAdapter;
import com.br.timetabler.adapter.NoteArrayAdapter;
import com.br.timetabler.listener.CommentClickListener;
import com.br.timetabler.util.DatabaseHandler_joe;
import com.br.timetabler.util.NotesDialogFragment;
import com.br.timetabler.util.ServerInteractions;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

			public class One_Lesson_Notes_Fragment extends ListFragment implements OnClickListener  {
			
				private NoteManager noteMan;  
			    private ListView theList;  
			    private int notePosn;  
				AlertDialog alertDialog;
				ImageButton btnSaveNotes, btnAddnotes,btnShareNotes;
				ListView listViewNotes;
				private static final int ENTER_DATA_REQUEST_CODE = 1;
				private static String KEY_SUCCESS = "success";
				private LessonNotesCursorAdapter customAdapter;
			    private NotesDataHelper databaseHelper;
			    private NoteArrayAdapter  notAdpter;
				//SaveNotesTask notesTsk;
				ServerInteractions userFunction;
				DatabaseHandler_joe db;
				JSONObject json_user;
			    JSONObject json;
			    String errorMsg, successMsg;
			    String res;
			    DecimalFormat df = new DecimalFormat("#.##");
				EditText edtcomment;
				String unit_id;
				
				
				@Override
			    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			    	View v=inflater.inflate(R.layout.one_lesson_notes, container, false);
			    	
					btnAddnotes = (ImageButton) v.findViewById(R.id.btnAddnotes);
					btnAddnotes.setOnClickListener(new OnClickListener() {			
						@Override
						public void onClick(View v) {
							startActivityForResult(new Intent(getActivity(), AddNoteActivity.class), ENTER_DATA_REQUEST_CODE);
							//showDialog();
						}
					});
					
					
			
					databaseHelper = new NotesDataHelper(getActivity());
					listViewNotes = (ListView) v.findViewById(android.R.id.list);
					//listViewNotes = (ListView) v.findViewById(R.id.lvnot);
			
					
					listViewNotes.setOnItemClickListener(new OnItemClickListener() {
			
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						
							
								Log.i( "clicked on item: " + position, "**");
								databaseHelper = new NotesDataHelper(getActivity());
								databaseHelper.remove(id);
								Log.i( "clicked on item: " + position, "**");
							}
					});
			 
			        // Database query can be a time consuming task ..
			        // so its safe to call database query in another thread
			        // Handler, will handle this stuff for you <img src="http://s0.wp.com/wp-includes/images/smilies/icon_smile.gif?m=1129645325g" alt=":)" class="wp-smiley">
			 
			        new Handler().post(new Runnable() {
			            @Override
			            public void run() {
			                customAdapter = new LessonNotesCursorAdapter(getActivity(), databaseHelper.getAllData());
			                listViewNotes.setAdapter(customAdapter);
			            }
			        });
					
				return v;	
					
				}
				
				
				/**
				* Creates a new instance of our dialog and displays it.
				
				private void showDialog() {
					DialogFragment newFragment = NotesDialogFragment.newInstance(1);
					newFragment.show(getFragmentManager(), "dialog");
				}*/
			    
				    @Override
				    public void onActivityResult(int requestCode, int resultCode, Intent data) {
				
				        super.onActivityResult(requestCode, resultCode, data);
				
				        if (data !=null ) {
				
				            databaseHelper.insertData(data.getExtras().getString("tag_time"), data.getExtras().getString("tag_unit"), data.getExtras().getString("tag_note"));
				            
				            customAdapter.changeCursor(databaseHelper.getAllData());
				        }else 
				        	 customAdapter.changeCursor(databaseHelper.getAllData());
				        
				        	
				        
				        
				    }
			
			
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					}
}
