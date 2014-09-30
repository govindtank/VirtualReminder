package com.example.virtualreminder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
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

public class NewActivity extends Activity {

	// Declare alarm manager
	AlarmManager aManager = null;   
	// Get date formatter object
	DateFormat fmtDateAndTime;
	// Define a TextView object
	EditText TextLabel = null;
	TextView dateLabel = null;
	TextView timeLabel = null;
	static String  photo_path = null;
	static String  audio_path = null;
	TextView recorder_path = null;
	static int timeint = 0;		
	//Get a calendar object
	Calendar dateAndTime = Calendar.getInstance(Locale.CANADA);
	// Create variables to read seted time and write to textview
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;
	//Create a recorder
	private MediaRecorder mRecorder = null;

	//On clicking the button of DatePickerDialog, invoke this method		
	DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener(){	
		@Override	
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {		
			//Modify year,month,date
			//Here, the value of year,monthOfYear,dayOfMonth is the same as the up-to-date value in DatePickerDialog
			dateAndTime.set(Calendar.YEAR, year);	
			dateAndTime.set(Calendar.MONTH, monthOfYear);	
			dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);  	
			//Update TextView to newest time
			updateLabel();          		
		}      		
	};

	TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
		//��DatePickerDialog����		
		@Override		
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {		
			dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);		
			dateAndTime.set(Calendar.MINUTE, minute);	
			updateLabel();		
		}		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView (R.layout.newactivity);

		//Gesture
		ImageView ev = (ImageView) this.findViewById(R.id.viewforgesture3);
		ev.setOnTouchListener(new OnGestureListener(this){

			@Override 
			public void oneFingerDoubleClick(){
				//Add new item into database
				String textlabel = TextLabel.getText().toString();
				String datelabel = dateLabel.getText().toString();
				String timelabel = timeLabel.getText().toString();
				//String saverecorderpath = recorder_path.getText().toString();
				ReminderService service = new ReminderService(NewActivity.this);
				Reminder reminder;
				if (datelabel=="" && timelabel==""){
					reminder =  new Reminder(textlabel, datelabel, timelabel, photo_path, audio_path, 0);
				}
				else{
					reminder =  new Reminder(textlabel, datelabel, timelabel, photo_path, audio_path,timeint);
				}
				service.save(reminder);

				//Return to main page
				Intent intent = new Intent();
				intent.setClass(NewActivity.this, MainActivity.class);
				startActivity(intent);

				//Set alarm 
				//Myalarm(dateAndTime);

				//clear time and date
				dateAndTime.set(Calendar.YEAR, 0);
				dateAndTime.set(Calendar.MONTH, 0);
				dateAndTime.set(Calendar.DAY_OF_MONTH, 0); 
				dateAndTime.set(Calendar.HOUR_OF_DAY, 0);
				dateAndTime.set(Calendar.MINUTE, 0);
				finish();
			}

			@Override
			public void oneFingerCross(){
				//System.out.println("oneFingerCross");
				// TODO Auto-generated method stub
				// show a message while loader is loading
				AlertDialog.Builder adb = new AlertDialog.Builder(NewActivity.this);
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

						//Return to main page
						Intent intent = new Intent();
						intent.setClass(NewActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					}  
				});
				adb.show();    
			}
			
			@Override
			public void twoFingersIncreaseDistance(){
				// TODO Auto-generated method stub
				photo_path = letCamera();
			}
			
			@Override
			public void twoFingersDecreaseDistance(){
				// TODO Auto-generated method stub
				letRecorder();	
			}
			
			 @Override
			    public void oneFingerLongPress(){
		
				 Toast.makeText(NewActivity.this, "Introduction to Gesture: 1. Cross for delete " +
				 "2. Double click for save 3. Spread for photo taken 4. Pinch for voice recorder", Toast.LENGTH_SHORT).show();
			    	}
		});


		aManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);		 
		dateAndTime.set(Calendar.YEAR, 0);
		dateAndTime.set(Calendar.MONTH, 0);
		dateAndTime.set(Calendar.DAY_OF_MONTH, 0); 
		dateAndTime.set(Calendar.HOUR_OF_DAY, 0);
		dateAndTime.set(Calendar.MINUTE, 0);

		//If click on camera button, letCamera() will be called
		ImageButton btnCamera = (ImageButton) findViewById(R.id.btnCamera);
		btnCamera.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				photo_path = letCamera();
			}
		});

		//If click on recorder button, letRecorder() will be called
		ImageButton btnRecorder = (ImageButton) findViewById(R.id.btnRecorder);        
		btnRecorder.setOnClickListener(new OnClickListener() {           
			public void onClick(View v) {
				// TODO Auto-generated method stub
				letRecorder();
			}            
		});

		//Use OnClickListener to control click on set time button
		Button dateBtn = (Button)findViewById(R.id.setDate);
		//set the on click listener for the button
		dateBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//generate a DatePickerDialog instance and show it. The DatePickerDialog can be used to specify year, month and date
				dateAndTime = Calendar.getInstance(Locale.CANADA);            	
				new DatePickerDialog(NewActivity.this,d,
						dateAndTime.get(Calendar.YEAR),
						dateAndTime.get(Calendar.MONTH),
						dateAndTime.get(Calendar.DAY_OF_MONTH)).show();              
			}
		});

		//Use OnClickListener to control click on set time button
		Button timeBtn = (Button)findViewById(R.id.setTime);  	
		timeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new TimePickerDialog(NewActivity.this,t,dateAndTime.get(Calendar.HOUR_OF_DAY),dateAndTime.get(Calendar.MINUTE),true).show();
			}
		});

		TextLabel=(EditText)findViewById(R.id.EditText);
		dateLabel=(TextView)findViewById(R.id.vdate); 
		timeLabel=(TextView)findViewById(R.id.vtime);

		Button SaveBtn = (Button)findViewById(R.id.SaveBtn);
		SaveBtn.setOnClickListener(new View.OnClickListener() {  
			@Override	
			public void onClick(View v){
				//Add new item into database
				String textlabel = TextLabel.getText().toString();
				String datelabel = dateLabel.getText().toString();
				String timelabel = timeLabel.getText().toString();

				//String saverecorderpath = recorder_path.getText().toString();
				ReminderService service = new ReminderService(NewActivity.this);
				Reminder reminder;
				if (datelabel=="" && timelabel==""){
					reminder =  new Reminder(textlabel, datelabel, timelabel, photo_path, audio_path,0);
				}
				else{
					reminder =  new Reminder(textlabel, datelabel, timelabel, photo_path, audio_path,timeint);
				}

				service.save(reminder);        		
				//Return to main page
				openmainActivity(v);        		
				//Set alarm 
				//Myalarm(dateAndTime);        		
				//clear time and date
				dateAndTime.set(Calendar.YEAR, 0);
				dateAndTime.set(Calendar.MONTH, 0);
				dateAndTime.set(Calendar.DAY_OF_MONTH, 0); 
				dateAndTime.set(Calendar.HOUR_OF_DAY, 0);
				dateAndTime.set(Calendar.MINUTE, 0);
				finish();
			}   	
		});

		Button DeleteBtn=(Button)findViewById(R.id.DeleteBtn);
		DeleteBtn.setOnClickListener(new View.OnClickListener() {  
			@Override	
			public void onClick(View v){   
				// TODO Auto-generated method stub
				// show a message while loader is loading
				AlertDialog.Builder adb = new AlertDialog.Builder(NewActivity.this);
				adb.setTitle("Delete?");
				adb.setMessage("Are you sure you want to delete ");
				adb.setNegativeButton("Cancel", null);
				adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int which) {
						//MyDataObject.remove(positionToRemove);
						//DatabaseHandler dBHandler = new DatabaseHandler(activity.getApplicationContext());
						//dBHandler.Delete_Contact(user_id);
						//Main_Screen.this.onResume();

						//Return to main page
						Intent intent = new Intent();
						intent.setClass(NewActivity.this, MainActivity.class);
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
	
	//The method to update TextView
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:// Take a photo
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
			}
			break;
		case 2://recording
			if (resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();  
				/*get the data from Bundle, pay attention to the key*/  
				audio_path = bundle.getString("recorderpath"); 
			}
		default:
			break;
		}
	}

	protected String letCamera() {
		// TODO Auto-generated method stub
		Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String strImgPath = Environment.getExternalStorageDirectory().toString() + "/dlion/";//folder to store photos
		Date dt=new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");      
		String fileName = df.format(dt) + ".jpg"; //Give a name for the photo(with time stamp)     
		String Photo_Path = strImgPath+fileName;   
		File out = new File(strImgPath);
		if (!out.exists()) {
			out.mkdirs();
		}
		out = new File(strImgPath, fileName);
		strImgPath = strImgPath + fileName;//The absolute path of the photo
		Uri uri = Uri.fromFile(out);
		imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(imageCaptureIntent, 1);     
		return Photo_Path;
	}

	protected void letRecorder(){
		//Start a new activity for clicking on record button
		Intent intent = new Intent();
		intent.setClass(this, AudioRecordTest.class);
		startActivityForResult(intent,2); 		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void Myalarm(Calendar c){   
		Intent intent = new Intent(NewActivity.this, MyAlarm.class);
		PendingIntent pi = PendingIntent.getActivity(NewActivity.this, 0, intent,0);
		aManager.set (AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
		/*   Toast.makeText(MainActivity.this,"hahaha", Toast.LENGTH_SHORT).show();
		 */		   
	}

}