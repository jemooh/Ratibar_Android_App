package com.br.timetabler.widget;

import java.util.List;

import com.br.timetabler.adapter.UnitsAdapter;
import com.br.timetabler.model.Unit;
import com.br.timetabler.listener.UnitClickListener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;


public class UnitsListView extends ListView implements android.widget.AdapterView.OnItemClickListener {
	private List<Unit> units;
    private UnitClickListener unitClickListener;
    
	public UnitsListView(Context context) {
        super(context);
    }
	
	public UnitsListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
 
    public UnitsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    
 
    public void setUnits(List<Unit> units){
    	this.units = units;
    	UnitsAdapter adapter = new UnitsAdapter(getContext(), units);
        setAdapter(adapter);
        // When the topics are set we also set an item click listener to the list
        // this will callback to our custom list whenever an item it pressed
        // it will tell us what position in the list is pressed
        setOnItemClickListener(this);
    }
     
    // Calling this method sets a listener to the list
    // Whatever class is passed in will be notified when the list is pressed
    // (The class that is passed in just has to 'implement VideoClickListener'
    // meaning is has the methods available we want to call)
    public void setOnUnitClickListener(UnitClickListener l) {
    	unitClickListener = l;
    }
    
    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

    // When we receive a notification that a list item was pressed
    // we check to see if a video listener has been set
    // if it has we can then tell the listener 'hey a video has just been clicked' also passing the video
    @Override
	public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
    	if(unitClickListener != null){
    		unitClickListener.onUnitClicked(units.get(position));
        }
		
	}
}
