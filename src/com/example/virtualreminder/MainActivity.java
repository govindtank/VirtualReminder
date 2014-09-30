package com.example.virtualreminder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	private ListView listView;
	private ReminderService reminderservice;
	private ReminderService reminderservicealarm;
	// Declare alarm manager
    AlarmManager aManager = null;
    // Get a calendar instance
  	Calendar dateAndTimesetalarm = Calendar.getInstance(Locale.CANADA);  
  	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//gesture
		  ImageView tx1 = (ImageView) this.findViewById(R.id.viewforgesture);
		  tx1.setOnTouchListener(new OnGestureListener(this){	    
			    @Override
			    public void oneFingerDoubleClick(){
			    
			    Intent intent = new Intent();
				intent.setClass(MainActivity.this, NewActivity.class);
				startActivity(intent);
        		finish();
			    }
			    
			    @Override
			    public void oneFingerLeft2Right(){
			    
			    Intent intent = new Intent();
				intent.setClass(MainActivity.this, ExpListActivity.class);
				startActivity(intent);
        		finish();
			    }
			    
			    @Override
			    public void oneFingerLongPress(){
			    	System.out.println("oneFingerLongPress");
			    	}
      });

		
		aManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);		
		//Read database and show on the list view
		reminderservice = new ReminderService (this);		
		listView = (ListView) this.findViewById(R.id.listview);
		//set up list view items on click method
		listView.setOnItemClickListener (new ItemClickListener());		
		//show the content of database on the list view
		show();		
		//set alarm
		reminderservicealarm = new ReminderService (this);
		int alarmtime = reminderservicealarm.getfirstrowtime();
		if (alarmtime!=0){			
		  int alarmYear = alarmtime/(60*24*31*12);
		  int alarmMonth = (alarmtime- alarmYear*60*24*31*12)/(60*24*31);
		  int alarmDay = (alarmtime-alarmYear*60*24*31*12-alarmMonth*60*24*31)/(60*24);
		  int alarmHour =(alarmtime-alarmYear*60*24*31*12-alarmMonth*60*24*31-alarmDay*60*24)/60;
		  int alarmMinute =alarmtime-alarmYear*60*24*31*12-alarmMonth*60*24*31-alarmDay*60*24-60*alarmHour;
		
		  dateAndTimesetalarm.set(Calendar.YEAR, alarmYear);
		  dateAndTimesetalarm.set(Calendar.MONTH, alarmMonth-1);
		  dateAndTimesetalarm.set(Calendar.DAY_OF_MONTH, alarmDay);
		  dateAndTimesetalarm.set(Calendar.HOUR_OF_DAY, alarmHour);
		  dateAndTimesetalarm.set(Calendar.MINUTE, alarmMinute);
		 	
		  Myalarm(dateAndTimesetalarm);
		}		  
	}

	private final class ItemClickListener implements OnItemClickListener{
		public void onItemClick (AdapterView<?> parent, View view, int position, long id){			
			     ListView lView = (ListView) parent;
			   //openreminderActivity(view);
			     Intent intent = new Intent();
			   //intent.putExtra("Item_ID",lView.getItemAtPosition(position).toString()); 
		         intent.setClass(MainActivity.this, ExistingActivity.class);		       
		         Bundle bundle = new Bundle();	       	 
		         HashMap<String, Object> item=(HashMap<String, Object>) parent.getItemAtPosition(position);   	         
		         bundle.putString ("Text",(String) item.get("text"));
		       	 bundle.putString ("Date",(String) item.get("date"));
		         bundle.putString ("Time",(String) item.get("time"));
		         bundle.putInt("ListID", (Integer) item.get("id"));
		         bundle.putString ("PhotoPath",(String) item.get("video"));
		         bundle.putString ("AudioPath",(String) item.get("recorder"));
		         bundle.putInt("timeint", (Integer) item.get("time_int"));
		       	 
		       	 intent.putExtras(bundle);
		       
		         startActivity(intent);		  
		         String tuple=lView.getItemAtPosition(position).toString();
		         finish();
		      // Toast.makeText(MainActivity.this, lView.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show(); 		     
		}		
	}
	
	//Activity to open and update the reminder item
	public void openreminderActivity(View v) {
			Intent intent = new Intent();
			intent.setClass(this, ExistingActivity.class);
			startActivity(intent);
			finish();
    }
	
	private void show() {
		//List<Reminder> reminders = reminderservice.getScrollData(0, 100);
		List<Reminder> reminders = reminderservice.getScrollDatauptodate(0, 100);
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
	    /*
		String[] from=new String[]{"video","text", "date", "time"};
		int[]      to=new int[] {R.id.RecordedPhoto, R.id.Textview_text, R.id.Textview_date, R.id.Textview_time};
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.item, from, to);
		MyViewBinder ViewBinder=new MyViewBinder();
		adapter.setViewBinder(ViewBinder);
		//ImageView recordedphoto=(ImageView)findViewById (R.id.RecordedPhoto);
	    //adapter.setViewImage(recordedphoto,data["video"]);
		*/
		String[] from=new String[]{"text", "date", "time"};
		int[]      to=new int[] {R.id.Textview_text, R.id.Textview_date, R.id.Textview_time};
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.item, from, to);	
		listView.setAdapter(adapter);
	}
	
	
	class MyViewBinder implements SimpleAdapter.ViewBinder {
	    @Override
	    public boolean setViewValue(View view, Object data, String textRepresentation) {
	        if (view instanceof ImageView && data instanceof Bitmap) {
	            ImageView v = (ImageView)view;
	            Bitmap bitmap=(Bitmap)data;
	            v.setImageBitmap(bitmap);
	            /*
	            bitmap=null;
	            bitmap.recycle();
	    		System.gc();
	    		*/
	            // return true to signal that bind was successful
	            return true;
	        }
	        return false;
	    }
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
    //Start a new activity for clicking on "Expired" button
	public void openexpActivity(View v) {		
		Intent intent = new Intent();
		intent.setClass(this, ExpListActivity.class);
		startActivity(intent);
	}
    //Start a new activity for clicking on "New" button 
    public void opennewActivity(View v) {
		Intent intent = new Intent();
		intent.setClass(this, NewActivity.class);
		startActivity(intent);
		finish();
	}
    
    public void Myalarm(Calendar c){   
		   Intent intent = new Intent(MainActivity.this, MyAlarm.class);
		   PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, intent,0);
		   aManager.set (AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
		//   Toast.makeText(MainActivity.this,"hahaha", Toast.LENGTH_SHORT).show();		   	   
	}
}
