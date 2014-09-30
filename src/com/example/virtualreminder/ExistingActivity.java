package com.example.virtualreminder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ExistingActivity extends Activity {
	ReminderService dbhandler=new ReminderService(this);
	int Item_ID;

	private ReminderService reminderservice;
	private MediaPlayer   mPlayer = null;
	private static final String LOG_TAG = "AudioRecordTest";
	// Declare alarm manager
	AlarmManager aManager = null;  
	// Get a date format object
	DateFormat fmtDateAndTime;	
	// Define a TextView object
	EditText TextLabel = null;
	TextView dateLabel = null;
	TextView timeLabel = null;
	static String  photo_path = null;
	static String  audio_path;
	// Get a calendar object
	Calendar dateAndTime = Calendar.getInstance(Locale.CANADA);        

	// Create variables to read specified time and write to TextView
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;

	static int timeint = 0;
	//On clicking the configuration button of DatePickerDialog 
	DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener(){
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			//Modify year,month,day
			//The value of year,monthOfYear,dayOfMonth is identical as the up-to-date value in DatePickerDialog
			dateAndTime.set(Calendar.YEAR, year);
			dateAndTime.set(Calendar.MONTH, monthOfYear);
			dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);  
			//Update TextView as the newest time
			updateLabel();          
		}      
	};

	TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
		//Same as DatePickerDialog
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
			dateAndTime.set(Calendar.MINUTE, minute);
			updateLabel();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView (R.layout.edititems);
		reminderservice = new ReminderService (this);
		Bundle bundle = this.getIntent().getExtras();
		String Text = bundle.getString("Text");
		String Date = bundle.getString("Date");
		String Time = bundle.getString("Time");
		String PhotoPath = bundle.getString("PhotoPath");
		String AudioPath = bundle.getString("AudioPath");
		timeint = bundle.getInt("timeint");
		//EnlagePhotoPath=PhotoPath;
		//read the photo
		ImageButton mImageButton = (ImageButton) findViewById (R.id.btnPhoto);
		Bitmap bm = BitmapFactory.decodeFile(PhotoPath);
		mImageButton.setImageBitmap(bm);

		photo_path=PhotoPath;
		audio_path=AudioPath;
		final ArrayList<String> enlargephotopath = new ArrayList<String>();
		enlargephotopath.add(PhotoPath);		
		mImageButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Click to enlarge the photo
				String EnlagePhotoPath=enlargephotopath.get(enlargephotopath.size()-1);
				if(EnlagePhotoPath!=null){
				Intent intent = new Intent();
				intent.setClass(ExistingActivity.this, enlargeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString ("PhotoToEnlarge",EnlagePhotoPath);
				//enlargephotopath.clear();
				intent.putExtras(bundle);
				startActivity(intent);
				}else{
					Toast toast=Toast.makeText(ExistingActivity.this, "No photo!", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP|Gravity.LEFT,145,413);
					toast.show();
				}
			}
		});
		
		//read the audio
		final ArrayList<String> playaudiopath = new ArrayList<String>();
		playaudiopath.add(AudioPath);
		ImageButton mImageButton1 = (ImageButton) findViewById (R.id.btnAudio);
		mImageButton1.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String PlayPhotoPath=playaudiopath.get(playaudiopath.size()-1);
				mPlayer = new MediaPlayer(); 
				if(PlayPhotoPath!=null){
					try { 
						//Set the photo to show  
						mPlayer.setDataSource(PlayPhotoPath); 
						mPlayer.prepare(); 
						//Show the photo  
						mPlayer.start(); 
					} catch (IOException e) { 
						Log.e(LOG_TAG, "prepare() failed"); 
					} 
				}else{
					Toast toast=Toast.makeText(ExistingActivity.this, "No voice message!", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP|Gravity.LEFT,345,413);
					toast.show();
				}
			}   					
		});

		//Bitmap bm1 = BitmapFactory.decodeFile(AudioPath);
		//mImageButton.setImageBitmap(bm1);

		mYear = timeint/(60*24*31*12);
		mMonth = (timeint- mYear*60*24*31*12)/(60*24*31);
		mDay = (timeint-mYear*60*24*31*12-mMonth*60*24*31)/(60*24);
		mHour =(timeint-mYear*60*24*31*12-mMonth*60*24*31-mDay*60*24)/60;
		mMinute =timeint-mYear*60*24*31*12-mMonth*60*24*31-mDay*60*24-60*mHour;

		dateAndTime.set(Calendar.YEAR, mYear);
		dateAndTime.set(Calendar.MONTH, mMonth-1);
		dateAndTime.set(Calendar.DAY_OF_MONTH, mDay);  
		dateAndTime.set(Calendar.HOUR_OF_DAY, mHour);
		dateAndTime.set(Calendar.MINUTE, mMinute);

		final int event_id = bundle.getInt("ListID");

		TextView tv1 = (TextView) findViewById(R.id.EditTextEdit);
		tv1.setText(Text);

		TextView tv2 = (TextView) findViewById(R.id.vdateEdit);
		tv2.setText(Date);

		TextView tv3 = (TextView) findViewById(R.id.vtimeEdit);
		tv3.setText(Time);

		aManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);		

		//gesture
		ImageView ev = (ImageView) this.findViewById(R.id.viewforgesture4);
		ev.setOnTouchListener(new OnGestureListener(this){					
			@Override 
			public void oneFingerDoubleClick(){
				//Add new item into database
				String textlabel = TextLabel.getText().toString();
				String datelabel = dateLabel.getText().toString();
				String timelabel = timeLabel.getText().toString();
				ReminderService service = new ReminderService(ExistingActivity.this);

				Reminder reminder;
				if (datelabel=="" && timelabel==""){
					reminder =  new Reminder(textlabel, datelabel, timelabel, photo_path, audio_path ,0);
				}
				else{
					reminder =  new Reminder(textlabel, datelabel, timelabel, photo_path, audio_path ,timeint);
				}

				service.update(reminder, event_id);

				//Return to main page
				Intent intent = new Intent();
				intent.setClass(ExistingActivity.this, MainActivity.class);
				startActivity(intent);

				dateAndTime.set(Calendar.YEAR, 0);
				dateAndTime.set(Calendar.MONTH, 0);
				dateAndTime.set(Calendar.DAY_OF_MONTH, 0); 
				dateAndTime.set(Calendar.HOUR_OF_DAY, 0);
				dateAndTime.set(Calendar.MINUTE, 0);
				finish();
			}

			@Override
			public void oneFingerCross(){
				// TODO Auto-generated method stub
				// show a message while loader is loading
				AlertDialog.Builder adb = new AlertDialog.Builder(ExistingActivity.this);
				adb.setTitle("Delete?");
				adb.setMessage("Are you sure you want to delete ");
				//final int user_id = Integer.parseInt(v.getTag().toString());
				adb.setNegativeButton("Cancel", null);
				adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int which) {
						// MyDataObject.remove(positionToRemove);
						//DatabaseHandler dBHandler = new DatabaseHandler(activity.getApplicationContext());
						//dBHandler.Delete_Contact(user_id);
						//Main_Screen.this.onResume();
						//delete from database       		    	
						reminderservice.delete(event_id);
						//Return to main page
						Intent intent = new Intent();
						intent.setClass(ExistingActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					}  
				});
				adb.show();    		
			}
		});

		//Use OnClickListener to control click on set time button
		Button dateBtn = (Button)findViewById(R.id.setDateEdit);
		//Set a button click listener 
		dateBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Create a DatePickerDialog object and show it. DatePickerDialog can be used to specify year, date and month
				new DatePickerDialog(ExistingActivity.this,	d,
						dateAndTime.get(Calendar.YEAR),
						dateAndTime.get(Calendar.MONTH),
						dateAndTime.get(Calendar.DAY_OF_MONTH)).show();              
			}
		});

		//Use OnClickListener to control click on set time button
		Button timeBtn = (Button)findViewById(R.id.setTimeEdit);
		timeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new TimePickerDialog(ExistingActivity.this,t,dateAndTime.get(Calendar.HOUR_OF_DAY),dateAndTime.get(Calendar.MINUTE),true).show();
			}
		});

		TextLabel=(EditText)findViewById(R.id.EditTextEdit);
		dateLabel=(TextView)findViewById(R.id.vdateEdit); 
		timeLabel=(TextView)findViewById(R.id.vtimeEdit);

		Button EditBtn = (Button)findViewById(R.id.EditBtnedit);
		EditBtn.setOnClickListener(new View.OnClickListener() {  
			@Override
			public void onClick(View v){
				//Add new item into database
				String textlabel = TextLabel.getText().toString();
				String datelabel = dateLabel.getText().toString();
				String timelabel = timeLabel.getText().toString();
				ReminderService service = new ReminderService(ExistingActivity.this);				
				Reminder reminder;				
				if (datelabel=="" && timelabel==""){
					reminder =  new Reminder(textlabel, datelabel, timelabel, photo_path, audio_path, 0);
				}
				else{
					reminder =  new Reminder(textlabel, datelabel, timelabel, photo_path, audio_path,timeint);
				}

				service.update(reminder, event_id);

				//Return to main page
				Intent intent = new Intent();
				intent.setClass(ExistingActivity.this, MainActivity.class);
				startActivity(intent);

				//Set alarm 
				//Myalarm(dateAndTime);

				dateAndTime.set(Calendar.YEAR, 0);
				dateAndTime.set(Calendar.MONTH, 0);
				dateAndTime.set(Calendar.DAY_OF_MONTH, 0); 
				dateAndTime.set(Calendar.HOUR_OF_DAY, 0);
				dateAndTime.set(Calendar.MINUTE, 0);
				finish();
			}   	
		});

		Button delete = (Button) findViewById(R.id.DeleteBtnedit);

		delete.setOnClickListener(new View.OnClickListener() {  
			@Override	
			public void onClick(View v){   
				// TODO Auto-generated method stub
				// show a message while loader is loading
				AlertDialog.Builder adb = new AlertDialog.Builder(ExistingActivity.this);
				adb.setTitle("Delete?");
				adb.setMessage("Are you sure you want to delete ");
				//final int user_id = Integer.parseInt(v.getTag().toString());
				adb.setNegativeButton("Cancel", null);
				adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int which) {
						// MyDataObject.remove(positionToRemove);
						//DatabaseHandler dBHandler = new DatabaseHandler(activity.getApplicationContext());
						//dBHandler.Delete_Contact(user_id);
						//Main_Screen.this.onResume();

						//delete from database       		    	

						reminderservice.delete(event_id);

						//Return to main page
						Intent intent = new Intent();
						intent.setClass(ExistingActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					}  
				});
				adb.show();    		
			}   	
		});

	}

	//Activity to return to main page
	public void openmainActivity(View v) {
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		startActivity(intent);
	}
	
	//The TextView method to update interface 
	private void updateLabel() {
		// Get the date and time which is set by the user
		mYear = dateAndTime.get(Calendar.YEAR);
		mMonth = dateAndTime.get(Calendar.MONTH)+1;
		mDay = dateAndTime.get(Calendar.DAY_OF_MONTH);
		mHour = dateAndTime.get(Calendar.HOUR_OF_DAY);
		mMinute = dateAndTime.get(Calendar.MINUTE);

		timeint = mMinute+mHour*60+mDay*60*24+mMonth*60*24*31+mYear*60*24*31*12;

		if (mMonth<10 && mDay<10){
			dateLabel.setText(mYear+"-0"+mMonth+"-0"+mDay);
		}
		else if (mMonth>=10 && mDay<10){
			dateLabel.setText(mYear+"-"+mMonth+"-0"+mDay);
		}
		else if (mMonth<10 && mDay>=10){
			dateLabel.setText(mYear+"-0"+mMonth+"-"+mDay);
		}
		else{
			dateLabel.setText(mYear+"-"+mMonth+"-"+mDay);
		}

		if (mHour<10 && mMinute<10)
		{
			timeLabel.setText("0"+mHour+":"+"0"+mMinute);
		}

		else if (mHour<10 && mMinute>=10)
		{
			timeLabel.setText("0"+mHour+":"+mMinute);
		}
		else if (mHour>=10 && mMinute<10)
		{
			timeLabel.setText(mHour+":"+"0"+mMinute);
		}
		else 
		{
			timeLabel.setText(mHour+":"+mMinute);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void Myalarm(Calendar c){
		Intent intent = new Intent(ExistingActivity.this, MyAlarm.class);
		PendingIntent pi = PendingIntent.getActivity(ExistingActivity.this, 0, intent,0);
		aManager.set (AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
	}

	//Start a new activity for clicking on record button
	public void AudioRecord(View v) {
		Intent intent = new Intent();
		intent.setClass(this, AudioRecordTest.class);
		startActivity(intent);
	}
}
