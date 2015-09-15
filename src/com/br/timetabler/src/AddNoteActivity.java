package com.br.timetabler.src;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.br.timetabler.R;
import com.br.timetabler.util.UserFunctions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

		 
		public class AddNoteActivity extends ActionBarActivity {
		    
			ImageButton btnshare,btnsave,btncancel;
		    EditText editnote;
		    TextView error;
		    String note;
		    
		    
		  
		    // JSON Response node names
		    private static String KEY_SUCCESS = "success";
		 
		    @Override
		    public void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        setContentView(R.layout.note_lay);
		 
		        // Importing all assets like buttons, text fields
		       editnote = (EditText) findViewById(R.id.txtAddNotes);
		        
		        btnsave = (ImageButton) findViewById(R.id.btn_save);
		        btnshare = (ImageButton) findViewById(R.id.btn_share);
		        btncancel = (ImageButton) findViewById(R.id.btn_cancel);
		        
		       /*
		        Calendar c = Calendar.getInstance();
		        SimpleDateFormat currentDate = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss ");
		        String formatdate= currentDate.format(c.getTime());
		          */
		        // Register Button Click event
		        btnshare.setOnClickListener(new View.OnClickListener() {        
		            public void onClick(View view) {
		                  note = editnote.getText().toString();
		                  Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		                      sharingIntent.setType("text/plain");
		                      sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
		                      sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, note);
		                      startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.app_name)));
		          }
		            
		        });
		      
		    }
		   
		    public String giveDateTime() {
		       Calendar cal = Calendar.getInstance();
		       SimpleDateFormat currentDate = new SimpleDateFormat("MMM/dd/yyyy HH:mm ");
		       return currentDate.format(cal.getTime());
		    }
		    
		    
		    
		    public void onClickSave (View btn) {
		
					   	    String  note = editnote.getText().toString();
					   	    String  currentDateTime = giveDateTime();
					   	    String unit_id = SingleLessonActivity1.unit_id;
		
		       if ( note.length() != 0) {
				
				           Intent newIntent = getIntent();
				           newIntent.putExtra("tag_time", currentDateTime);
				           newIntent.putExtra("tag_unit", unit_id);
				           newIntent.putExtra("tag_note", note);
				
				           this.setResult(RESULT_OK, newIntent);
				           finish();
		       }
		    }
		
		    public void onClickCancel (View btn) {
		    	this.setResult(Activity.RESULT_CANCELED);
		    	finish();
		       }
}
		
