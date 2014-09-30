package com.example.virtualreminder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ReminderService {
	
	private final static String TABLE_NAME = "Reminder_table";
	public final static String FIELD_id = "event_id";
	public final static String FIELD_TEXT = "event_text";
	
	private DBOpenHelper dbOpenHelper;
	
	public ReminderService (Context context){
		
		this.dbOpenHelper = new DBOpenHelper(context);
	}
	
	//Add a new item into database
	public void save (Reminder reminder){
		
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into " + TABLE_NAME + "(event_text, date, time, " +
				"video, recorder, time_int) values (?,?,?,?,?,?)", new Object[] {reminder.getEvent_text(),
				reminder.getDate(),reminder.getTime(),reminder.getVideo(),reminder.getRecorder(),reminder.getTime_int()});
	}
	//delete an item from database
    public void delete (int event_id){
		
    	SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
    	db.execSQL("delete from " + TABLE_NAME + " where event_id=?",new Object[] {event_id});
	}
    
   public void delete1 (String recorder){
		
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from " + TABLE_NAME + " where recorder=?", new Object[] {recorder});
	}
   
    //edit an item from database
    public void update (Reminder reminder, int id){
		
    	SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
    	db.execSQL("update "+TABLE_NAME+" set event_text=?, date=?, time=?, " +
    			"video=?, recorder=?, time_int=? where event_id = "+id,new Object[] {reminder.getEvent_text(),
				reminder.getDate(),reminder.getTime(),reminder.getVideo(),reminder.getRecorder(), reminder.getTime_int()});
	}
    //find a item from the database
    public Reminder find (Integer id){
	  
    	SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
    	Cursor cursor = db.rawQuery("select * from "+TABLE_NAME+" where event_id=?", 
    			new String[]{id.toString()} );
    	if (cursor.moveToFirst()){
    		int event_id = cursor.getInt(cursor.getColumnIndex("event_id"));
    		String event_text = cursor.getString(cursor.getColumnIndex("event_text"));
    		String date = cursor.getString(cursor.getColumnIndex("date"));
    		String time = cursor.getString(cursor.getColumnIndex("time"));
    		String video = cursor.getString(cursor.getColumnIndex("video"));
    		String recorder = cursor.getString(cursor.getColumnIndex("recorder"));
    		int time_int = cursor.getInt(cursor.getColumnIndex("time_int"));
    		return new Reminder(event_id, event_text, date, time, video, recorder, time_int);
    	}
    	cursor.close();
    	return null;
    }

    public List<Reminder> getScrollData (int offset, int maxResult){
    	
    	List<Reminder> reminders = new ArrayList<Reminder>();
    	SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
    	Cursor cursor = db.rawQuery("select * from Reminder_table order by event_id " +
    			"asc limit ?, ?", new String [] {String.valueOf(offset), String.valueOf(maxResult)});
    	while (cursor.moveToNext()){
    		
    		int id = cursor.getInt(cursor.getColumnIndex("event_id"));
    		String text = cursor.getString(cursor.getColumnIndex("event_text"));
    		String date = cursor.getString(cursor.getColumnIndex("date"));
    		String time = cursor.getString(cursor.getColumnIndex("time"));
    		String video = cursor.getString(cursor.getColumnIndex("video"));
    		String recorder = cursor.getString(cursor.getColumnIndex("recorder"));
    		int time_int = cursor.getInt(cursor.getColumnIndex("time_int"));
    		
    		reminders.add(new Reminder(id, text, date, time, video, recorder, time_int));
    	}
    	
    	cursor.close();
    	return reminders;
    }
    
    
public List<Reminder> getScrollDatauptodate (int offset, int maxResult){
    	
	    //get the current time
	    Calendar datetime = Calendar.getInstance(Locale.CANADA);

	    
	    int mYear = datetime.get(Calendar.YEAR);
        int mMonth = datetime.get(Calendar.MONTH)+1;
        int mDay = datetime.get(Calendar.DAY_OF_MONTH);
        int mHour = datetime.get(Calendar.HOUR_OF_DAY);
        int mMinute = datetime.get(Calendar.MINUTE);
        
	    String timeLabel = null;
	    String dateLabel = null;
	    
	    if (mMonth<10 && mDay<10){
	    	dateLabel = mYear+"-0"+mMonth+"-0"+mDay;
	    }
	    else if (mMonth>10 && mDay<10){
	    	dateLabel = mYear+"-"+mMonth+"-0"+mDay;
	    }
	    else if (mMonth<10 && mDay>10){
	    	dateLabel = mYear+"-0"+mMonth+"-"+mDay;
	    }
	    else{
	    	 dateLabel = mYear+"-"+mMonth+"-"+mDay;
	    }
	    
	   
        if (mHour<10 && mMinute<10)
        {
        	timeLabel="0"+mHour+":"+"0"+mMinute;
        }
        
        else if (mHour<10 && mMinute>10)
        {
        	timeLabel="0"+mHour+":"+mMinute;
        }
        else if (mHour>10 && mMinute<10)
        {
        	timeLabel=mHour+":"+"0"+mMinute;
        }
        else 
        {
        	timeLabel=mHour+":"+mMinute;
        }
        
        
    	List<Reminder> reminders = new ArrayList<Reminder>();
    	SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
    	

    	String Mark = "mark";
    	String textMark = "mark";
    	String videoMark = "mark";
    	int timeintMark = mMinute+mHour*60+mDay*60*24+mMonth*60*24*31+mYear*60*24*31*12;
    	Reminder reminder =  new Reminder(textMark, dateLabel, timeLabel, videoMark, Mark, timeintMark);
    	save(reminder);
  
    	Cursor cursor = db.rawQuery("select * from Reminder_table where time_int = 0 or time_int >"+timeintMark +
    			" order by time_int asc limit ?, ?", new String [] {String.valueOf(offset), String.valueOf(maxResult)});
    	
    	/*
    	Cursor cursor = db.rawQuery("select * from Reminder_table where time_int >"+timeintMark +
    			" order by time_int desc", null);
    	*/
    	while (cursor.moveToNext()){
    		
    		int id = cursor.getInt(cursor.getColumnIndex("event_id"));
    		String text = cursor.getString(cursor.getColumnIndex("event_text"));
    		String date = cursor.getString(cursor.getColumnIndex("date"));
    		String time = cursor.getString(cursor.getColumnIndex("time"));
    		String video = cursor.getString(cursor.getColumnIndex("video"));
    		String recorder = cursor.getString(cursor.getColumnIndex("recorder"));
    		int time_int = cursor.getInt(cursor.getColumnIndex("time_int"));
    		
    		reminders.add(new Reminder(id, text, date, time, video, recorder, time_int));
    	}
    	
    	delete1(Mark);
    	cursor.close();
    	return reminders;
    }

public List<Reminder> getScrollDataExpired (int offset, int maxResult){
	
    //get the current time
    Calendar datetime = Calendar.getInstance(Locale.CANADA);

    
    int mYear = datetime.get(Calendar.YEAR);
    int mMonth = datetime.get(Calendar.MONTH)+1;
    int mDay = datetime.get(Calendar.DAY_OF_MONTH);
    int mHour = datetime.get(Calendar.HOUR_OF_DAY);
    int mMinute = datetime.get(Calendar.MINUTE);
    
    String timeLabel = null;
    
    String dateLabel = null;
    
    if (mMonth<10 && mDay<10){
    	dateLabel = mYear+"-0"+mMonth+"-0"+mDay;
    }
    else if (mMonth>10 && mDay<10){
    	dateLabel = mYear+"-"+mMonth+"-0"+mDay;
    }
    else if (mMonth<10 && mDay>10){
    	dateLabel = mYear+"-0"+mMonth+"-"+mDay;
    }
    else{
    	 dateLabel = mYear+"-"+mMonth+"-"+mDay;
    }
    
    
    if (mHour<10 && mMinute<10)
    {
    	timeLabel="0"+mHour+":"+"0"+mMinute;
    }
    
    else if (mHour<10 && mMinute>10)
    {
    	timeLabel="0"+mHour+":"+mMinute;
    }
    else if (mHour>10 && mMinute<10)
    {
    	timeLabel=mHour+":"+"0"+mMinute;
    }
    else 
    {
    	timeLabel=mHour+":"+mMinute;
    }
    
    
	List<Reminder> reminders = new ArrayList<Reminder>();
	SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
	

	String Mark = "mark";
	String textMark = "mark";
	String videoMark = "mark";
	int timeintMark = mMinute+mHour*60+mDay*60*24+mMonth*60*24*31+mYear*60*24*31*12+1;
	Reminder reminder =  new Reminder(textMark, dateLabel, timeLabel, videoMark, Mark, timeintMark);
	save(reminder);

	/*Cursor cursor = db.rawQuery("select * from Reminder_table where time_int >"+timeintMark +
			" order by time_int asc limit ?, ?", new String [] {String.valueOf(offset), String.valueOf(maxResult)});
	*/
	
	Cursor cursor = db.rawQuery("select * from Reminder_table where time_int>0 and time_int <"+timeintMark +
			" order by time_int desc", null);
	
	while (cursor.moveToNext()){
		
		int id = cursor.getInt(cursor.getColumnIndex("event_id"));
		String text = cursor.getString(cursor.getColumnIndex("event_text"));
		String date = cursor.getString(cursor.getColumnIndex("date"));
		String time = cursor.getString(cursor.getColumnIndex("time"));
		String video = cursor.getString(cursor.getColumnIndex("video"));
		String recorder = cursor.getString(cursor.getColumnIndex("recorder"));
		int time_int = cursor.getInt(cursor.getColumnIndex("time_int"));
		
		reminders.add(new Reminder(id, text, date, time, video, recorder, time_int));
	}
	
	delete1(Mark);
	cursor.close();
	return reminders;
}
 

public int getfirstrowtime (){
	
    //get the current time
    Calendar datetime = Calendar.getInstance(Locale.CANADA);

    
    int mYear = datetime.get(Calendar.YEAR);
    int mMonth = datetime.get(Calendar.MONTH)+1;
    int mDay = datetime.get(Calendar.DAY_OF_MONTH);
    int mHour = datetime.get(Calendar.HOUR_OF_DAY);
    int mMinute = datetime.get(Calendar.MINUTE);
    
    String timeLabel = null;
    String dateLabel = null;
    
    if (mMonth<10 && mDay<10){
    	dateLabel = mYear+"-0"+mMonth+"-0"+mDay;
    }
    else if (mMonth>10 && mDay<10){
    	dateLabel = mYear+"-"+mMonth+"-0"+mDay;
    }
    else if (mMonth<10 && mDay>10){
    	dateLabel = mYear+"-0"+mMonth+"-"+mDay;
    }
    else{
    	 dateLabel = mYear+"-"+mMonth+"-"+mDay;
    }
    
   
    if (mHour<10 && mMinute<10)
    {
    	timeLabel="0"+mHour+":"+"0"+mMinute;
    }
    
    else if (mHour<10 && mMinute>10)
    {
    	timeLabel="0"+mHour+":"+mMinute;
    }
    else if (mHour>10 && mMinute<10)
    {
    	timeLabel=mHour+":"+"0"+mMinute;
    }
    else 
    {
    	timeLabel=mHour+":"+mMinute;
    }
    
    
	List<Reminder> reminders = new ArrayList<Reminder>();
	SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
	

	String Mark = "mark";
	String textMark = "mark";
	String videoMark = "mark";
	int timeintMark = mMinute+mHour*60+mDay*60*24+mMonth*60*24*31+mYear*60*24*31*12;
	Reminder reminder =  new Reminder(textMark, dateLabel, timeLabel, videoMark, Mark, timeintMark);
	save(reminder);

	Cursor cursor = db.rawQuery("select * from Reminder_table where time_int >"+timeintMark +
			" order by time_int limit ?", new String [] {String.valueOf(1)});
	
	/*
	Cursor cursor = db.rawQuery("select * from Reminder_table where time_int >"+timeintMark +
			" order by time_int desc", null);
	*/
	
		if (cursor.moveToFirst())
		{
		   int time_int = cursor.getInt(cursor.getColumnIndex("time_int"));
		   delete1(Mark);
		   cursor.close();
		   return time_int;
		}
	
		else{
			delete1(Mark);
			cursor.close();
			return 0;
		}
	

}

    public long getCount (){
    	
    	SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
    	Cursor cursor = db.rawQuery("select count(*) from Reminder_table", null );
    	cursor.moveToFirst();
    	long result = cursor.getLong(0);
    	cursor.close();
    	
    	return result;
    }
	
	
	

}
