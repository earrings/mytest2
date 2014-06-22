package com.travelsky.movie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import com.travelsky.utils.ItemArrayAdapter;
import com.travelsky.utils.ListItem;

import android.view.View.OnClickListener;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnCompletionListener,
		OnErrorListener, OnInfoListener, OnPreparedListener,
		OnSeekCompleteListener, OnVideoSizeChangedListener,
		SurfaceHolder.Callback {
	private SurfaceView surfaceView;
	private MediaPlayer player;
	private SurfaceHolder holder;
	private String dataPath;
	private Display currDisplay;
	private int vWidth, vHeight;
	LinearLayout linearLayout;
	private Boolean movieDisplay = true;
	TextView movePosition, moveMax;
	ImageButton play = null,scale = null;
	private AtomicBoolean isPrepared = new AtomicBoolean(false);//��ֹ��Ƶû���ص�ʱ���ȥȡ��Ƶ����ʱ��;
	private SeekBar mProgress;
	private Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.movie_main);

		surfaceView = (SurfaceView) this.findViewById(R.id.video_surface);
		ListView listView = (ListView) this.findViewById(R.id.lv);
		linearLayout = (LinearLayout) this.findViewById(R.id.control_movie);
		movePosition = (TextView) this.findViewById(R.id.movePosition);
		moveMax = (TextView) this.findViewById(R.id.moveMax);
		play = (ImageButton)this.findViewById(R.id.play);
		scale = (ImageButton)this.findViewById(R.id.scale);
		mProgress = (SeekBar)this.findViewById(R.id.progress);
		mProgress.setEnabled(false);

		List<ListItem> items = getListItems();
		ItemArrayAdapter adapter = new ItemArrayAdapter(MainActivity.this,R.layout.sd_list,items);
		listView.setAdapter(adapter);
		
		mProgress.setOnSeekBarChangeListener(new  OnSeekBarChangeListener(){
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				System.out.println(progress);
				//�϶�����ʾ�ŵ���λ��
				movePosition.setText(getTimeString(progress));
				if (fromUser) {
				//if(isPrepared.get()){
					//int max = player.getDuration();
					//mProgress.setMax(max);
					
					//��Ƶ�϶�����Ҫ�ŵ�λ��
					player.seekTo(progress);
				//}else{
					
				//}
				}
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				Toast.makeText(getApplicationContext(), "onStartTrackingTouch" , 
                        Toast.LENGTH_SHORT).show(); 
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				 Toast.makeText(getApplicationContext(), "onStopTrackingTouch" , 
	                        Toast.LENGTH_SHORT).show(); 
			}		
		});
		
		// һ������ʱ�����Ƶ���ư�ť��������
		linearLayout.setVisibility(View.INVISIBLE);
		play.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				if(player.isPlaying()){
					play.setImageResource(R.drawable.play);
					player.pause();
				}else {
					play.setImageResource(R.drawable.pause);
					player.start();
					mHandler.sendEmptyMessage(2);
				}
			}
		});
		scale.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
		//		MainActivity.this.requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
				linearLayout.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN));
		//		MainActivity.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//ȥ����Ϣ��
				MainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
		});	
		surfaceView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {			
				if(isPrepared.get()){
					int position = player.getCurrentPosition();// ��Ƶ��ǰ��������ʱ��
					int max = player.getDuration();// ��Ƶ�����ʱ�䳤��
					if (movieDisplay) {
						System.out.println("�ҵ����surface");
						movePosition.setText(getTimeString(position));
						moveMax.setText(getTimeString(max));
						linearLayout.setVisibility(View.VISIBLE);
						movieDisplay = false;
					} else {
						linearLayout.setVisibility(View.INVISIBLE);
						movieDisplay = true;
					}
				}else{
					int position = 0;// ��Ƶ��ǰ��������ʱ��
					int max = 0;// ��Ƶ�����ʱ�䳤��
					if (movieDisplay) {
						System.out.println("�ҵ����surface+=+++");
						movePosition.setText(getTimeString(position));
						moveMax.setText(getTimeString(max));
						linearLayout.setVisibility(View.VISIBLE);
						movieDisplay = false;
					} else {
						linearLayout.setVisibility(View.INVISIBLE);
						movieDisplay = true;
					}
				}
			}
		});
		// listViewչʾ�����б�
//		SimpleAdapter adapter = new SimpleAdapter(this, gtPlayList(),
//				R.layout.sd_list, new String[] { "name" },
//				new int[] { R.id.mp4 });
	//	listView.setAdapter(adapter);
		// listViewÿ����¼����������¼�
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				isPrepared.set(false);
				System.out.println( " onclick + ++++++++");
				try {
					System.out.println("reset+ ***************");
					//����������Դ��ʱ�򲻹��ǲ����ڲ����У�ֱ��reset�����Ų������
					player.reset();
					player.setAudioStreamType(AudioManager.STREAM_MUSIC);
					player.setDisplay(holder);
					System.out.println("*****"+ dataPath);
					player.setDataSource(dataPath);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					System.out.println("��Ƶ׼��������");
					player.prepareAsync();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}			
				//player.start();
			//	System.out.println("��Ƶ׼��������");
			}
		});
		// ��SurfaceView���CallBack����
		holder = surfaceView.getHolder();
		holder.addCallback(this);
		// Ϊ�˿��Բ�����Ƶ����ʹ��CameraԤ����������Ҫָ����Buffer����
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// ���濪ʼʵ����MediaPlayer����
		player = new MediaPlayer();
		player.setOnCompletionListener(this);
		player.setOnErrorListener(this);
		player.setOnInfoListener(this);
		player.setOnPreparedListener(this);
		player.setOnSeekCompleteListener(this);
		player.setOnVideoSizeChangedListener(this);
		// Ȼ��ָ����Ҫ�����ļ���·������ʼ��MediaPlayer
		dataPath = "http://116.211.111.250/share.php?method=Share.download&cqid=1acc93049fd469acbe349ff025131a51&dt=53.e3050a26cec2eb1422c3ae24ecf9f4b2&e=1403588169&fhash=eae8797988d66421a32798437ae6674e537c29f8&fname=MOV008.mp4&fsize=15497693&nid=14029320224978335&scid=53&st=8d3a50ee2c47b243b68415a28835cdf8&xqid=198731395";		
//		try {
//			player.setDataSource(dataPath);
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (IllegalStateException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		// Ȼ������ȡ�õ�ǰDisplay����
		currDisplay = this.getWindowManager().getDefaultDisplay();
		
		mHandler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	        	System.out.println("ִ���߳�+++++++++++++++++++++++++++++++");
	            int pos;
	            switch (msg.what) {
	                case 1:
	                    break;
	                case 2:
	                    pos = setProgress();
	           //         if (!mDragging && mShowing && player.isPlaying()) {
	                    if (player.isPlaying()) {
	                        msg = obtainMessage(2);
	                        sendMessageDelayed(msg, 1000 - (pos % 1000));
	                    }
	                    break;
	            }
	        }
	    };

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// ��Surface�ߴ�Ȳ����ı�ʱ����
		Log.v("Surface Change:::", "surfaceChanged called");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// ���˳���Ƶ��ʱ�򴥷�
		Log.v("Surface Destory:::", "surfaceDestroyed called");
	}

	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		// ��video��С�ı�ʱ����
		// �������������player��source�����ٴ���һ��
		Log.v("Video Size Change", "onVideoSizeChanged called");
	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {
		// seek�������ʱ����
		Log.v("Seek Completion", "onSeekComplete called");
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// ��prepare��ɺ󣬸÷������������������ǲ�����Ƶ
		isPrepared.set(true);
		mProgress.setEnabled(true);
		mProgress.setMax(mp.getDuration());
		moveMax.setText(getTimeString(mp.getDuration()));
		Log.v(" onPrepared:::", "onPrepared called");
		// ����ȡ��video�Ŀ�͸�
		vWidth = player.getVideoWidth();
		vHeight = player.getVideoHeight();

		if (vWidth > currDisplay.getWidth()
				|| vHeight > currDisplay.getHeight()) {
			// ���video�Ŀ���߸߳����˵�ǰ��Ļ�Ĵ�С����Ҫ��������
			float wRatio = (float) vWidth / (float) currDisplay.getWidth();
			float hRatio = (float) vHeight / (float) currDisplay.getHeight();
			// ѡ����һ����������
			float ratio = Math.max(wRatio, hRatio);
			vWidth = (int) Math.ceil((float) vWidth / ratio);
			vHeight = (int) Math.ceil((float) vHeight / ratio);
			// ����surfaceView�Ĳ��ֲ���
			surfaceView.setLayoutParams(new LinearLayout.LayoutParams(vWidth,vHeight));
			// Ȼ��ʼ������Ƶ
			System.out.println("����11111111111111");
			player.start();
			mHandler.sendEmptyMessage(2);
			System.out.println("����11111111111111");
		}
	}
	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		// ��һЩ�ض���Ϣ���ֻ��߾���ʱ����
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
			break;
		case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
			break;
		case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
			break;
		case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
			break;
		}
		return false;
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Log.v("Play Error:::", "onError called");
		switch (what) {
		case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
			Log.v("Play Error:::", "MEDIA_ERROR_SERVER_DIED");
			break;
		case MediaPlayer.MEDIA_ERROR_UNKNOWN:
			Log.v("Play Error:::", "MEDIA_ERROR_UNKNOWN");
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// ��MediaPlayer������ɺ󴥷�
		Log.v("Play onCompletion:::", "onComletion called");
		if(player.isPlaying()){
			player.stop();
		}
		// this.finish();
	}

	// �õ���Ƶ�Ĳ����б�
	private List<Map<String, String>> gtPlayList() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map1 = new HashMap<String, String>();
		Map<String, String> map2 = new HashMap<String, String>();
		Map<String, String> map3 = new HashMap<String, String>();
		map1.put("name", "��Ƶ1");
		map2.put("name", "��Ƶ2");
		map3.put("name", "��Ƶ3");
		list.add(map1);
		list.add(map2);
		list.add(map3);
		return list;
	}

	// ͨ������õ���Ƶ�ķ�����������
	public static String getTimeString(long time) {
		// long hour = time / (60 * 60 * 1000);
		long minute = (time) / (60 * 1000);
		long second = (time - minute * 60 * 1000) / 1000;
		if (second >= 60) {
			second = second % 60;
			minute += second / 60;
		}
		// minute = minute % 60;
		// hour += minute / 60;
		// String sh = "";
		String sm = "";
		String ss = "";
		// if (hour < 10) {
		// sh = "0" + String.valueOf(hour);
		// } else {
		// sh = String.valueOf(hour);
		// }
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
		return (sm + ":" + ss);
	}
	
	//
	private List<ListItem> getListItems() {
		List<ListItem> items = new ArrayList<ListItem>();

		items.add(new ListItem(R.drawable.publicity,"������̬԰�쵼�Ӳ�","2014-06-20",R.drawable.pictureplay));
		items.add(new ListItem(R.drawable.publicity,"������̬԰�쵼�Ӳ�","2014-06-21",R.drawable.pictureplay));
		items.add(new ListItem(R.drawable.publicity,"������̬԰�쵼�Ӳ�","2014-06-22",R.drawable.pictureplay));
		items.add(new ListItem(R.drawable.publicity,"������̬԰�쵼�Ӳ�","2014-06-23",R.drawable.pictureplay));
		items.add(new ListItem(R.drawable.publicity,"������̬԰�쵼�Ӳ�","2014-06-24",R.drawable.pictureplay));
		return items;
	}
	
	
    
    
    private int setProgress() {
      //  if (player == null || mDragging) {
    	if(player == null){
            return 0;
        }
        int position = player.getCurrentPosition();
        int duration = player.getDuration();
        if (mProgress != null) {
            if (duration > 0) {
                // use long to avoid overflow
                mProgress.setProgress(position);
            }
            //int percent = 2;
            //mProgress.setSecondaryProgress(percent * 10);
        }
//        if (moveMax != null)
//        	moveMax.setText(getTimeString(duration));
        if (movePosition != null)
        	movePosition.setText(getTimeString(position));
        return position;
    }

}