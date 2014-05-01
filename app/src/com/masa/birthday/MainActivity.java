package com.masa.birthday;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	ListView listView;
	List<BirthdayRecord> birthdays;
	BirthdayRecordAdapter adapter;
	
	  public static class IntentBuilder {
	    private Intent intent;

	    public IntentBuilder(Context context, Class<?> activityClass) {
	      intent = new Intent(context, activityClass);
	    }
	    public void setName(String value) {
	      intent.putExtra("name", value);
	    }
	    public void setBirthday(Date value) {
		      intent.putExtra("birthday", value);
		    }
	    public void setPhoto(String value) {
		      intent.putExtra("photo", value);
		    }
	    public Intent build() {
	      return intent;
	    }
	  }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		TextView currentDate = (TextView) findViewById(R.id.today);
		String currentDateTimeString = DateFormat.getDateTimeInstance().format(
				new Date());
		currentDate.setText(currentDateTimeString);

		listView = (ListView) findViewById(R.id.birthdays);
		BirthdayRecord sions =
				new BirthdayRecord(
						"sions",
						new GregorianCalendar(1984,2,6).getTime(),
						"placeholdeer");
		BirthdayRecord miris =
				new BirthdayRecord(
						"miris",
						new GregorianCalendar(1984,2,14).getTime(),
						"placeholdeer");
		
		birthdays = new ArrayList<BirthdayRecord>();
		birthdays.add(miris);
		birthdays.add(sions);
		adapter = new BirthdayRecordAdapter(this,  birthdays);
		listView.setAdapter(adapter);
		
		// To register the button with context menu.
		registerForContextMenu(listView);
		//listView.setOnItemClickListener((OnItemClickListener) this);
		//openContextMenu(listView);
	}
	private void openBirthdayEditor(BirthdayRecord record) {
		IntentBuilder builder = new IntentBuilder(MainActivity.this, EditBirthdayActivity.class);
		if (record != null) {
			builder.setName(record.getName());
			builder.setBirthday(record.getBirthday());
			builder.setPhoto(record.getPhoto());
		}
		Intent intent = builder.build();
		startActivity(intent);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		
		super.onCreateContextMenu(menu, v, menuInfo);  
        MenuInflater m = getMenuInflater();  
        m.inflate(R.menu.birthday_list_menu, menu); 
        

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();  
        int position =  (int) info.position;  
        		
		switch (item.getItemId()) {
		case R.id.send: {
			//TODO(sions): Do you magic.
		}
			break;
		case R.id.edit: {
			// Edit Action
			openBirthdayEditor(birthdays.get(position));
		}
			break;
		case R.id.delete: {
			
            birthdays.remove(position);  
            this.adapter.notifyDataSetChanged();  
		}
			break;
		}

		return super.onContextItemSelected(item);
	}
	public static final int MENU_ADD = Menu.FIRST;
	    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

	    menu.add(Menu.NONE, MENU_ADD, Menu.NONE, "Add");

		/*// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);*/
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
        {
            case MENU_ADD:
            	openBirthdayEditor(null /* No current record to edit. */);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
		
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		/*int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);*/
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
}
