package com.canvas;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.inputmethodservice.InputMethodService;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.google.tts.TTS;

public class Canvas1 extends Activity implements OnClickListener,OnLongClickListener{
	private TTS myTts;
	CanvasClass canClass;
	private LinearLayout main;
	private LinearLayout cenerLayout;
	private LinearLayout topLayout;
	private LinearLayout subLayout;
	private LinearLayout tvLayout;
	private LinearLayout SecondLayout;
	private ImageView Exit;
	private ScrollView scrllView;
	private HorizontalScrollView HorizontalSV;
	private TextView[] TV=new TextView[1];
	private TextView[] TViews=new TextView[5];
	public final static int mButtonHeight = 220;
	public final static int mButtonWidth = 80;
	String inst[]=new String[5]; ;
	String InstTemp;
	int flag=0;
	int c0flag=0;
	int c1flag=0;
	int c2flag=0;
	int c3flag=0;
	int SpaceSelFlag = 1;
	String newStr="";
	int height;
	public static int width;
	Vibrator v;
	/** Called when the activity is first created. */
	CanvasClass canvasClass = null;
	Canvas1 conObj = null;

	public final int MY_DATA_CHECK_CODE = 1;
	private TextToSpeech mTts = null;
	public ProgressDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
		width = display.getWidth(); 
		height = display.getHeight(); 

		v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		AssetInstaller assetInstaller = new AssetInstaller(getApplicationContext(), "projects");
		try {
			assetInstaller.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		conObj = this;
		canvasClass = new CanvasClass(this,conObj);
		main = new LinearLayout(this);
		main.setOrientation(LinearLayout.VERTICAL);
		main.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
				LinearLayout.LayoutParams.FILL_PARENT));

		topLayout = new LinearLayout(this);
		topLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 
				((height/2)+130)));
		topLayout.setOrientation(LinearLayout.VERTICAL);
		topLayout.setBackgroundColor(0xffffffcc);
		topLayout.addView(canvasClass);

		cenerLayout = new LinearLayout(this);
		cenerLayout.setOrientation(LinearLayout.VERTICAL);
		cenerLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		cenerLayout.setBackgroundColor(Color.WHITE);

		for(int i = 0; i < TV.length; i++) {
			TV[i] = new TextView(this);
			TV[i].setTextColor(Color.BLUE);
			TV[i].setTextSize(40);
			TV[i].setGravity(Gravity.CENTER_HORIZONTAL);
			TV[i].setTypeface(null, Typeface.BOLD);
			TV[i].setHeight(height/8);
			TV[i].setPadding(5, 0, 0, 0);
			TV[i].setScroller(new Scroller(this));
			TV[i].setVerticalScrollBarEnabled(true);
			TV[i].setMovementMethod(ScrollingMovementMethod.getInstance());
			cenerLayout.addView(TV[i]);
		}

		SecondLayout = new LinearLayout(this);
		SecondLayout.setOrientation(LinearLayout.HORIZONTAL);
		SecondLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				70));
		SecondLayout.setBackgroundColor(0xff26466D);
		tvLayout = new LinearLayout(this);
		tvLayout.setOrientation(LinearLayout.HORIZONTAL);
		tvLayout.setLayoutParams(new LinearLayout.LayoutParams(
				(width-20),
				((height/8)-30)));
		for(int i = 0; i < TViews.length; i++) {
			TViews[i] = new TextView(this);
			TViews[i].setTextColor(Color.WHITE);
			TViews[0].setText("Suggested words..");
			TViews[0].setTextColor(0xffE8E8E8);
			TViews[i].setTextSize(15);
			TViews[i].setGravity(Gravity.CENTER);
			TViews[i].setPadding(10, 0, 0, 0);
			TViews[i].setHeight(((height/8)-30));
			TViews[i].setOnClickListener(this);
			tvLayout.addView(TViews[i]);
		}
		HorizontalSV=new HorizontalScrollView(this);
		HorizontalSV.setLayoutParams(new LinearLayout.LayoutParams(
				(width-125),
				LinearLayout.LayoutParams.WRAP_CONTENT));
		HorizontalSV.addView(tvLayout,new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		
		SecondLayout.addView(HorizontalSV);
		
		subLayout = new LinearLayout(this);
		subLayout.setOrientation(LinearLayout.HORIZONTAL);
		subLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				((height/8)-16)));
		subLayout.setGravity(Gravity.BOTTOM);
		Exit = new ImageView(this);

		
		Exit.setImageResource(R.drawable.exit);
		Exit.setPadding(30, 0, 0, 0);

		Exit.setOnClickListener(this);

		subLayout.addView(Exit);

		main.addView(cenerLayout);
		main.addView(topLayout);
		main.addView(subLayout);
		setContentView(main);

		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);

	}
	protected void onActivityResult(
			int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// success, create the TTS instance
				mTts = new TextToSpeech(this, new OnInitListener() {
					public void onInit(int status) {
						// TODO Auto-generated method stub

						//mTts.speak("Hello World", TextToSpeech.QUEUE_FLUSH, null);

					}
				});

			} else {
				// missing data, install it
				Intent installIntent = new Intent();
				installIntent.setAction(
						TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}

	public void onClick(View v) {
		
		if(v==Exit){
			int pid = android.os.Process.myPid(); 
			android.os.Process.killProcess(pid);
		}
	}


	/* This method will handle the swipe across the edge. Calls Freepad once the
	   touch area reaches the right end of screen */
	public void ClearCanvas(){
		if(canvasClass != null){
			topLayout.removeView(canvasClass);
			canvasClass = new CanvasClass(this,conObj);
			topLayout.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 
					((height/2)+130)));
			topLayout.addView(canvasClass);
		}
	}
	class ProgressdialogClass extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... unsued) {
			canvasClass.addStroke(canvasClass._currentStroke);
			return null;
		}
		@Override
		protected void onPostExecute(String sResponse) {
			dialog.dismiss();
			FreePadCall();
		}
		@Override
		protected void onPreExecute(){
			dialog = ProgressDialog.show(Canvas1.this, "Processing","Please wait...", true);
			
		}
	}
	public void CallingMethod(){
		ProgressdialogClass ObjAsy=new ProgressdialogClass();
		ObjAsy.execute();
	}
	public void FreePadCall(){
		
		HorizontalSV.setVisibility(View.VISIBLE);
		if(canvasClass != null){
			topLayout.removeView(canvasClass);
			canvasClass.destroyDrawingCache();
			canvasClass = new CanvasClass(this,conObj);
			topLayout.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 
					((height/2)+130)));
			topLayout.addView(canvasClass);
		}
		TV[0].setText(canvasClass.character[0]);
		String str1 = TV[0].getText().toString();
		mTts.speak(str1, 0, null);
	}

	public boolean onLongClick(View v) {
		System.out.println("-----long click------");		
		return true;
	}
	
	int curr_indx = 0;
	public void SpeakOutChoices(){
		if(canvasClass != null){
			topLayout.removeView(canvasClass);
			canvasClass = new CanvasClass(this,conObj);
			topLayout.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 
					((height/2)+130)));
			topLayout.addView(canvasClass);
		}
		System.out.println("index"+curr_indx +"---"+ CanvasClass.StrokeResultCount);
		if(curr_indx < CanvasClass.StrokeResultCount){
			TV[0].setText(CanvasClass.character[curr_indx]);
			String Choice1=CanvasClass.character[curr_indx];
			mTts.speak(Choice1, 0, null);
			curr_indx++;
			if(curr_indx == CanvasClass.StrokeResultCount)
				curr_indx = 0;
			
		}
	}
}