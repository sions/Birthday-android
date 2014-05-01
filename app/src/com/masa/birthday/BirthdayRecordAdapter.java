package com.masa.birthday;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BirthdayRecordAdapter extends BaseAdapter {

	List<BirthdayRecord> birthdayRecords;
	Context context;

	BirthdayRecordAdapter(Context context, List<BirthdayRecord> birthdayRecords) {
		this.context = context;
		this.birthdayRecords = birthdayRecords;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return birthdayRecords.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position >= birthdayRecords.size())
			return null;
		return birthdayRecords.get(position).getName();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		// Allocate
	      if (convertView == null) {
	        convertView = new TextView(context);
	      }

	      // Populate
	      TextView nameView = (TextView)convertView;
	      nameView.setText(birthdayRecords.get(position).getName() + " " + birthdayRecords.get(position).getBirthday());
	      nameView.setPadding(40, 40, 40, 40);
	      nameView.setTextSize(20);
	      
	      return convertView;
	    }
}
