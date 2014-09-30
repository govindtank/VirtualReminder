package com.example.virtualreminder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.media.MediaPlayer;
import android.os.Bundle;

public class MyAlarm extends Activity {
	

    MediaPlayer alarmMusic; 
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.alarm);
         //load music
         alarmMusic = MediaPlayer.create(this, R.raw.alarm);
         alarmMusic.setLooping(true);
         alarmMusic.start();
        new AlertDialog.Builder(MyAlarm.this).setTitle("Go Go Go!!!") 
        .setMessage("Don't Miss It!!!") 
        .setPositiveButton("OK", new OnClickListener()
        {
        	@Override
        	public void onClick (DialogInterface dialog, int which)
        	{
        		alarmMusic.stop();       		       		
        		MyAlarm.this.finish();
      
        		Intent intent = new Intent();
        		intent.setClass(MyAlarm.this, ExpListActivity.class);
        		startActivity(intent);
        		
        	}
        }).show();
    }


}
