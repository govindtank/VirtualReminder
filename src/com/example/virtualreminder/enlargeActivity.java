package com.example.virtualreminder;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class enlargeActivity extends Activity {  
    /** Called when the activity is first created. */  
        Bitmap bp=null;  
        ImageView imageview;  
        float scaleWidth;  
        float scaleHeight;           
        int h;  
        boolean num=false;  
        Matrix matrix;
        
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);   
        //make Image view to full screen
        this.getWindow().setFlags(
        		WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //dismiss the title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.enlarger); 
        //get the photo path
        Bundle bundle = this.getIntent().getExtras();
		String PhotoToEnlarge= bundle.getString("PhotoToEnlarge");  
        Display display=getWindowManager().getDefaultDisplay();  
                
        bp= BitmapFactory.decodeFile(PhotoToEnlarge);
        int width=bp.getWidth();  
        int height=bp.getHeight();  
        int w=getWindowManager().getDefaultDisplay().getWidth();  
        int h=getWindowManager().getDefaultDisplay().getHeight(); 
        scaleWidth=((float)w)/width;  
        scaleHeight=((float)h)/height;  
      //input scale value
      //matrix.postScale(scaleWidth, scaleHeight);
        
      //Bitmap newbp = Bitmap.createBitmap(bp, 0, 0, width, height, matrix, true);
        imageview=(ImageView)findViewById(R.id.RecordedPhoto); 
        imageview.setImageBitmap(bp);
      //imageview.setImageBitmap(newbp);  
        
      //gesture     
        ImageView iv = (ImageView) this.findViewById(R.id.RecordedPhoto);
        iv.setOnTouchListener(new OnGestureListener(this){
			
			@Override 
			public void oneFingerCross(){
        		finish();
				}
			
			@Override 
			public void oneFingerSingleClick(){
				finish();
				}			
		});        
    }  
   
}   