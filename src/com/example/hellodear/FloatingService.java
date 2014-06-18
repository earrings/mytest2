package com.example.hellodear;


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
import android.widget.SeekBar;

public class FloatingService extends Service{
	private int vWidth, vHeight;
	WindowManager wm = null;  
    WindowManager.LayoutParams wmParams = null;  
    View view;  
    final static int TIME = 2000;   //2000ms = 2s������Ϳ��˵Ĳ�������2��
    ImageButton play = null, pause = null, quit = null;
    ImageButton forward = null, backward = null;
    int position,max;
    SeekBar progress;
    LinearLayout linearLayout;
    String TAG = "android.intent.action.myvideoplayer";  //�Զ���Ĺ㲥��ǩ
    @Override  
    public void onCreate() {  
        Log.d("FloatingService", "onCreate");  
        super.onCreate();  
        view = LayoutInflater.from(this).inflate(R.layout.window, null);
       
        play  = (ImageButton)view.findViewById(R.id.play);
        pause = (ImageButton)view.findViewById(R.id.pause);
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
                intent.putExtra("flag", "change");    //�ı������
                intent.putExtra("newpos", position);
         //       FloatingService.this.sendBroadcast(intent);   //���͹㲥
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
//                intent.putExtra("flag", "forward");    //���
//                intent.putExtra("newpos", position);
//                FloatingService.this.sendBroadcast(intent);   //���͹㲥
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
//                    intent.putExtra("flag", "backward");    //����
//                    intent.putExtra("newpos", position);
//                    FloatingService.this.sendBroadcast(intent);   //���͹㲥
//                    //view.setVisibility(View.GONE);
//                }
//            });
        play.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setAction(TAG);
                intent.putExtra("flag", "play");    //����
             //   FloatingService.this.sendBroadcast(intent);   //���͹㲥
                view.setVisibility(View.GONE);
            }
        });
        pause.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setAction(TAG);
                intent.putExtra("flag", "pause");    //��ͣ
          //      FloatingService.this.sendBroadcast(intent);   //���͹㲥
                view.setVisibility(View.GONE);
            }
        });
//        quit.setOnClickListener(new Button.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent();
//                intent.setAction(TAG);
//                intent.putExtra("flag", "quit");    //�˳�
//                FloatingService.this.sendBroadcast(intent);   //���͹㲥
//                view.setVisibility(View.GONE);
//            }
//        });
        
        System.out.println("createView  createView");
        createView();     
        
        
        
    }  
    private void createView() {  
        // ��ȡWindowManager   
        wm = (WindowManager) getApplicationContext().getSystemService("window");  
        // ����LayoutParams(ȫ�ֱ�������ز���   
        wmParams = new WindowManager.LayoutParams();  
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;//2002;
        wmParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;//8;  
        wmParams.gravity = Gravity.CENTER | Gravity.CENTER;  
        // ����Ļ���Ͻ�Ϊԭ�㣬����x��y��ʼֵ   
        wmParams.x = 0;  
        wmParams.y = 0;  
        // �����������ڳ�������   
//        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;  
//        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        System.out.println(vWidth+ "************************************");
        System.out.println(vHeight+ "************************************");
        wmParams.width = 300;  
        wmParams.height = 300;  
        wmParams.format = 1;  
        wm.addView(view, wmParams);  
        view.setOnTouchListener(new OnTouchListener() {  
            public boolean onTouch(View v, MotionEvent event) {  
                // ��ȡ�����Ļ�����꣬������Ļ���Ͻ�Ϊԭ��
                view.setVisibility(View.GONE);
                return true;  
            }  
        });          
    }  
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        position = intent.getIntExtra("position", 0);
        max = intent.getIntExtra("max", 0);
        boolean visable = intent.getBooleanExtra("visable", false);
        progress.setMax(max);
        vWidth = intent.getIntExtra("vWidth", 0);
		System.out.println(vWidth);
		vHeight = intent.getIntExtra("vHeight", 0);
		System.out.println(vHeight);
        progress.setProgress(position);
       
        if(visable)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.GONE);
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
    
    
}