package com.example.hellodear;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class FloatingService extends Service{
	private int vWidth, vHeight;
	WindowManager wm = null;  
    WindowManager.LayoutParams wmParams = null;  
    View view;  
    final static int TIME = 2000;   //2000ms = 2s，快进和快退的步进都是2秒
    ImageButton play = null, pause = null, quit = null;
    ImageButton forward = null, backward = null,scale = null;
    TextView movePosition = null,moveMax;    
    int position,max;
    SeekBar progress;
    private Boolean playOrPause = true;//当为true时是播放图标
    
    LinearLayout linearLayout;
    String TAG = "android.intent.action.MainActivity";  //自定义的广播标签
    @Override  
    public void onCreate() {  
    	System.out.println("onCreate " + Thread.currentThread());
        Log.d("FloatingService", "onCreate");  
        super.onCreate();  
        view = LayoutInflater.from(this).inflate(R.layout.window, null);
        play  = (ImageButton)view.findViewById(R.id.play);
 //       pause = (ImageButton)view.findViewById(R.id.pause);
        scale = (ImageButton)view.findViewById(R.id.scale);
        movePosition = (TextView)view.findViewById(R.id.movePosition);
        moveMax = (TextView)view.findViewById(R.id.moveMax);
        
     //   quit  = (ImageButton)view.findViewById(R.id.quitthis);
        progress = (SeekBar)view.findViewById(R.id.progress);
      //   linearLayout = (LinearLayout)view.findViewById(R.id.linearLayout1);
     //   forward  = (ImageButton)view.findViewById(R.id.forward);
     //   backward  = (ImageButton)view.findViewById(R.id.backward);
        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setAction(TAG);
                intent.putExtra("flag", "change");    //改变进度条
                intent.putExtra("newpos", position);
         //       FloatingService.this.sendBroadcast(intent);   //发送广播
                view.setVisibility(View.GONE);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {
                // TODO Auto-generated method stub
                if(fromUser)
                    position = progress;
            }
        });
//        forward.setOnClickListener(new Button.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent();
//                intent.setAction(TAG);
//                if(position<=max-TIME)
//                    position+= TIME;
//                progress.setProgress(position);
//                intent.putExtra("flag", "forward");    //快进
//                intent.putExtra("newpos", position);
//                FloatingService.this.sendBroadcast(intent);   //发送广播
//                //view.setVisibility(View.GONE);
//            }
//        });
//            backward.setOnClickListener(new Button.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    Intent intent = new Intent();
//                    intent.setAction(TAG);
//                    if(position>=TIME)
//                        position-= TIME;
//                    progress.setProgress(position);
//                    intent.putExtra("flag", "backward");    //快退
//                    intent.putExtra("newpos", position);
//                    FloatingService.this.sendBroadcast(intent);   //发送广播
//                    //view.setVisibility(View.GONE);
//                }
//            });
        play.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
            	if(playOrPause == true){
            		System.out.println(Thread.currentThread());
	            	System.out.println("play + ~~~~~~~~~~~~~~~~~play");
		            play.setImageResource(R.drawable.pause);
	                Intent intent = new Intent();
	                intent.setAction(TAG);
	                intent.putExtra("flag", "play");    //播放
	                FloatingService.this.sendBroadcast(intent);   //发送广播
	               
	                //view.setVisibility(View.GONE);
	            }else if(playOrPause == false){
	            	System.out.println("pause + ~~~~~~~~pause~~~~~~~~~~~~");
	            	Intent intent = new Intent();
	                intent.setAction(TAG);
	                intent.putExtra("flag", "pause");    //暂停
	                FloatingService.this.sendBroadcast(intent);   //发送广播
	                play.setImageResource(R.drawable.play);
	                //view.setVisibility(View.GONE);
            	}
            }
        });
//        pause.setOnClickListener(new Button.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//            	System.out.println("play + ~~~~~~~~~~~~~~~~~~~~@@@");
//                // TODO Auto-generated method stub
//                Intent intent = new Intent();
//                intent.setAction(TAG);
//                intent.putExtra("flag", "pause");    //暂停
//                FloatingService.this.sendBroadcast(intent);   //发送广播
//                view.setVisibility(View.GONE);
//            }
//        });
//        quit.setOnClickListener(new Button.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent();
//                intent.setAction(TAG);
//                intent.putExtra("flag", "quit");    //退出
//                FloatingService.this.sendBroadcast(intent);   //发送广播
//                view.setVisibility(View.GONE);
//            }
//        });
        
        System.out.println("createView  createView");
             
        
        
        
    }  
    private void createView() {  
        // 获取WindowManager   
        wm = (WindowManager) getApplicationContext().getSystemService("window");
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
       
        System.out.println(density + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        // 设置LayoutParams(全局变量）相关参数   
        wmParams = new WindowManager.LayoutParams();  
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;//2002;
        wmParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;//8;  
        wmParams.gravity = Gravity.TOP | Gravity.LEFT;  
        // 以屏幕左上角为原点，设置x、y初始值   
        wmParams.x = 0;  
        wmParams.y = vHeight - (int)(30*density + 0.5f); 
        System.out.println(wmParams.y + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        // 设置悬浮窗口长宽数据   
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;  
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        System.out.println(vWidth+ "************************************");
        System.out.println(vHeight+ "************************************");
//        wmParams.width = 50;  
//        wmParams.height = 100;  
        wmParams.format = 1;  
        wm.addView(view, wmParams);  
        view.setOnTouchListener(new OnTouchListener() {  
            public boolean onTouch(View v, MotionEvent event) {  
                // 获取相对屏幕的坐标，即以屏幕左上角为原点
                view.setVisibility(View.GONE);
                return true;  
            }  
        });          
    }  
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	System.out.println("onStartCommand" + Thread.currentThread());
        // TODO Auto-generated method stub
        position = intent.getIntExtra("position", 0);
        max = intent.getIntExtra("max", 0);
        //视频总长度和当前播放的长度转换成时分秒
        String positionTime = getTimeString(position);
        String maxTime = getTimeString(max);
        
        boolean visable = intent.getBooleanExtra("visable", false);
        progress.setMax(max);
        movePosition.setText(positionTime);
        moveMax.setText("/"+maxTime);
        
        playOrPause = intent.getBooleanExtra("playOrPause", false);
        System.out.println(playOrPause+ "&*&*&*&*&*&*&*&*&*&*&*&*&***");
        
        
        vWidth = intent.getIntExtra("vWidth", 0);
		vHeight = intent.getIntExtra("vHeight", 0);
        progress.setProgress(position);
       
        if(visable)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.GONE);
        createView();
        return super.onStartCommand(intent, flags, startId);
    }
    @Override  
    public void onDestroy() {     
        Log.d("FloatingService", "onDestroy");  
        wm.removeView(view);  
        super.onDestroy();  
    }  
    @Override  
    public IBinder onBind(Intent intent) {  
        return null;  
    }
    
    
    
    public String getTimeString(long time){
//    	long hour = time / (60 * 60 * 1000);
		long minute = (time ) / (60 * 1000);
		long second = (time - minute * 60 * 1000) / 1000;
		if (second >= 60) {
			second = second % 60;
			minute += second / 60;
		}	
//			minute = minute % 60;
//			hour += minute / 60;
//		String sh = "";
		String sm = "";
		String ss = "";
//		if (hour < 10) {
//			sh = "0" + String.valueOf(hour);
//		} else {
//			sh = String.valueOf(hour);
//		}
		if (minute < 10) {
			sm = "0" + String.valueOf(minute);
		} else {
			sm = String.valueOf(minute);
		}
		if (second < 10) {
			ss = "0" + String.valueOf(second);
		} else {
			ss = String.valueOf(second);
		}
		return (sm+":"+ss);
    }
}