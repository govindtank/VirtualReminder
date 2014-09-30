package com.example.virtualreminder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ExpListActivity extends Activity {
	private ListView listView;
	private ReminderService reminderservice;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView (R.layout.explistactivity);

		//gesture
		ImageView tx1 = (ImageView) this.findViewById(R.id.viewforgesture1);
		tx1.setOnTouchListener(new OnGestureListener(this){


			@Override
			public void oneFingerDoubleClick(){

				Intent intent = new Intent();
				intent.setClass(ExpListActivity.this, NewActivity.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void oneFingerRight2Left(){

				Intent intent = new Intent();
				intent.setClass(ExpListActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});


		//Read database and show on the list view
		reminderservice = new ReminderService (this);				
		listView = (ListView) this.findViewById(R.id.listview1);
		//set up list view items on click method
		listView.setOnItemClickListener (new ItemClickListener());			
		//show the content of database on the list view
		show();
	}
	
	private void show() {
		List<Reminder> reminders = reminderservice.getScrollDataExpired(0, 100);
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>> ();
		for (Reminder reminder : reminders){
			HashMap<String, Object> item = new HashMap<String, Object> ();
			item.put("id", reminder.getEvent_id());
			item.put("text", reminder.getEvent_text());
			item.put("date", reminder.getDate());
			item.put("time", reminder.getTime());
			item.put("video", reminder.getVideo());
			item.put("recorder", reminder.getRecorder());
			item.put("time_int", reminder.getTime_int());
			data.add(item);
			
		}
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.item,
				new String[]{"text", "date", "time"}, new int[] {R.id.Textview_text,
			R.id.Textview_date, R.id.Textview_time} );
		
		listView.setAdapter(adapter);
	}
	
	
	private final class ItemClickListener implements OnItemClickListener{
		public void onItemClick (AdapterView<?> parent, View view, int position, long id){
			
			ListView lView = (ListView) parent;
			// openreminderActivity(view);
			   Intent intent = new Intent();
			//intent.putExtra("Item_ID",lView.getItemAtPosition(position).toString()); 
		       intent.setClass(ExpListActivity.this, ExistingActivity.class);
		       
		        Bundle bundle = new Bundle();
		        
		        HashMap<String, Object> item=(HashMap<String, Object>) parent.getItemAtPosition(position);   
		         
		        bundle.putString ("Text",(String) item.get("text"));
		       	bundle.putString ("Date",(String) item.get("date"));
		        bundle.putString ("Time",(String) item.get("time"));
		        bundle.putInt("ListID", (Integer) item.get("id"));
		        bundle.putInt("timeint", (Integer) item.get("time_int"));
		       	intent.putExtras(bundle);
		       
		        startActivity(intent);		  
		        String tuple=lView.getItemAtPosition(position).toString();
		        finish();
		        
		      //  Toast.makeText(ExpListActivity.this, lView.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();  
		}	
	}
	
	//Start a new activity for clicking on main button
	public void openmainActivity(View v) {		
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
	//Start a new activity for clicking on new button 
	public void opennewActivity(View v) {
		Intent intent = new Intent();
		intent.setClass(this, NewActivity.class);
		startActivity(intent);
		finish();
	}	

}