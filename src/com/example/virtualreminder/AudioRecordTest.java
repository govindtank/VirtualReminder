package com.example.virtualreminder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


import android.R.layout;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class AudioRecordTest extends Activity {	
	private static final String LOG_TAG = "AudioRecordTest"; 
    private String mFileName = null; 
    //Button for recorder  
    private RecordButton mRecordButton = null; 
    private MediaRecorder mRecorder = null; 
    //Button for replay 
    private PlayButton   mPlayButton = null; 
    private MediaPlayer   mPlayer = null; 
    //Save Button for recorded clips
    private SaveButton   mSaveButton=null;
 
    //When the recorder button is pressed, invoke this method, start or stop recording 
    private void onRecord(boolean start) { 
        if (start) { 
            startRecording(); 
        } else { 
            stopRecording(); 
        } 
    } 
 
    //  
    private void onPlay(boolean start) { 
        if (start) { 
            startPlaying(); 
        } else { 
            stopPlaying(); 
        } 
    } 
 
    private void startPlaying() { 
        mPlayer = new MediaPlayer(); 
        //Set the audio file to play
        try { 
            mPlayer.setDataSource(mFileName); 
            mPlayer.prepare(); 
            mPlayer.start(); //play the audio file
        } catch (IOException e) { 
            Log.e(LOG_TAG, "prepare() failed"); 
        } 
    } 
 
    //Stop playing
    private void stopPlaying() { 
        mPlayer.release(); 
        mPlayer = null; 
    } 
 
    private void startRecording() { 
        mRecorder = new MediaRecorder(); 
        //Set the audio source as Micphone  
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); 
        //Set the format
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); 
        mRecorder.setOutputFile(mFileName); 
        //Set the encoding format
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); 
 
        try { 
            mRecorder.prepare(); 
        } catch (IOException e) { 
            Log.e(LOG_TAG, "prepare() failed"); 
        } 
 
        mRecorder.start(); 
    } 
 
    private void stopRecording() { 
        mRecorder.stop(); 
        mRecorder.release(); 
        mRecorder = null; 
    } 
 
    //Define the Recorder Button 
    class RecordButton extends Button { 
        boolean mStartRecording = true; 
 
        OnClickListener clicker = new OnClickListener() { 
            public void onClick(View v) { 
                onRecord(mStartRecording); 
                if (mStartRecording) { 
                    setText("Stop recording"); 
                } else { 
                    setText("Start recording"); 
                } 
                mStartRecording = !mStartRecording; 
            } 
        }; 
 
        public RecordButton(Context ctx) { 
            super(ctx); 
            setText("Start recording"); 
            setOnClickListener(clicker); 
        } 
    } 
 
    //Define Play Button
    class PlayButton extends Button { 
        boolean mStartPlaying = true; 
 
        OnClickListener clicker = new OnClickListener() { 
            public void onClick(View v) { 
                onPlay(mStartPlaying); 
                if (mStartPlaying) { 
                    setText("Stop playing"); 
                } else { 
                    setText("Start playing"); 
                } 
                mStartPlaying = !mStartPlaying; 
            } 
        }; 
 
        public PlayButton(Context ctx) { 
            super(ctx); 
            setText("Start playing"); 
            setOnClickListener(clicker); 
        } 
    } 
 
    class SaveButton extends Button{
    	public SaveButton(Context ctx) {
			super(ctx);
			setText("Save");
			setOnClickListener(clicker); 
			// TODO Auto-generated constructor stub
		}

    	OnClickListener clicker = new OnClickListener(){
    		public void onClick(View v){
    			//Return to Newactivity page   			
    			Intent intent = new Intent(); 
    			intent.setClass(AudioRecordTest.this, NewActivity.class);
    			Bundle bundle = new Bundle();
    			bundle.putString("recorderpath", getmFileName()); 
    			intent.putExtras(bundle); 
    			AudioRecordTest.this.setResult(RESULT_OK, intent);
    			AudioRecordTest.this.finish();
    		}			
    	};    	   	    	
    }
      

    public AudioRecordTest() { 
        mFileName = Environment.getExternalStorageDirectory()
                .toString() + "/dlion/";// location to store recorder
        
        Date dt=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        mFileName += df.format(dt) + ".3gp"; 
       
    } 
       
    public String getmFileName() {
		return mFileName;
	}

	public void setmFileName(String mFileName) {
		this.mFileName = mFileName;
	}

	@Override 
    public void onCreate(Bundle icicle) { 
        super.onCreate(icicle); 
        
      //Construct an interface 
        LinearLayout ll = new LinearLayout(this); 
      //LayoutParams linLayoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT); 
        mRecordButton = new RecordButton(this); 
      //mRecordButton.setBackgroundColor(Color.GREEN);
      //mRecordButton.setLeft(100);
        
      //ll.setPadding(100, 800, 100, 50);
        ll.setMinimumHeight(120);
        ll.setMinimumWidth(150);
        ll.addView(mRecordButton, 
            new LinearLayout.LayoutParams( 
                ViewGroup.LayoutParams.WRAP_CONTENT, 
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0)); 
        mPlayButton = new PlayButton(this);
        
        ll.addView(mPlayButton, 
            new LinearLayout.LayoutParams( 
                ViewGroup.LayoutParams.WRAP_CONTENT, 
                ViewGroup.LayoutParams.WRAP_CONTENT, 
                0));
        
        mSaveButton= new SaveButton(this);
        ll.addView(mSaveButton, 
            new LinearLayout.LayoutParams( 
                ViewGroup.LayoutParams.WRAP_CONTENT, 
                ViewGroup.LayoutParams.WRAP_CONTENT, 
                0)); 
        setContentView(ll); 
        /*
        setContentView (R.layout.recording);
        mRecordButton = (RecordButton)findViewById(R.id.btnRecord);
        */
    } 
 
    @Override 
    public void onPause() { 
        super.onPause(); 
        //When Activity Pause, release recorder and player object
        if (mRecorder != null) { 
            mRecorder.release(); 
            mRecorder = null; 
        } 
 
        if (mPlayer != null) { 
            mPlayer.release(); 
            mPlayer = null; 
        } 
    } 
}
