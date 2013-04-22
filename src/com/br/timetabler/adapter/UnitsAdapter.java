package com.br.timetabler.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.br.timetabler.R;
import com.br.timetabler.model.Unit;

public class UnitsAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	List<Unit> units;
    public UnitsAdapter(Context context, List<Unit> units) {
    	this.units = units;
    	this.mInflater = LayoutInflater.from(context);        
    }

    public int getCount() {
    	Log.i("units.size() ", ""+units.size());
        return units.size();
    }

    public Object getItem(int position) {
        return units.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.single_item_unit, parent, false);
            holder = new ViewHolder();
            holder.label = (TextView) convertView.findViewById(R.id.label);
            holder.cbox = (CheckBox) convertView.findViewById(R.id.check);
            holder.cbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
	            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	            	Unit element = (Unit) holder.cbox.getTag();
	            	element.setSelected(buttonView.isChecked());	            	
	            }
	         });
			//convertView.setTag(viewHolder);
            holder.cbox.setTag(units.get(position));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Unit unit = units.get(position);
        holder.label.setText(unit.getUnitAcronyms() + " - " + unit.getUnitName());
        //holder.cbox.setChecked(true);
        holder.cbox.setChecked(units.get(position).isSelected());

        return convertView;
    }

    public class ViewHolder {
        TextView label;
        CheckBox cbox;
    }
}
