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
	private AtomicBoolean isPrepared = new AtomicBoolean(false);//防止视频没加载的时候就去取视频最大的时间;
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
				//拖动后显示放到的位置
				movePosition.setText(getTimeString(progress));
				if (fromUser) {
				//if(isPrepared.get()){
					//int max = player.getDuration();
					//mProgress.setMax(max);
					
					//视频拖动后需要放的位置
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
		
		// 一进来的时候把视频控制按钮给隐藏了
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
		//		MainActivity.this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
				linearLayout.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN));
		//		MainActivity.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
				MainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
		});	
		surfaceView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {			
				if(isPrepared.get()){
					int position = player.getCurrentPosition();// 视频当前播放所在时间
					int max = player.getDuration();// 视频的最大时间长度
					if (movieDisplay) {
						System.out.println("我点击了surface");
						movePosition.setText(getTimeString(position));
						moveMax.setText(getTimeString(max));
						linearLayout.setVisibility(View.VISIBLE);
						movieDisplay = false;
					} else {
						linearLayout.setVisibility(View.INVISIBLE);
						movieDisplay = true;
					}
				}else{
					int position = 0;// 视频当前播放所在时间
					int max = 0;// 视频的最大时间长度
					if (movieDisplay) {
						System.out.println("我点击了surface+=+++");
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
		// listView展示播放列表
//		SimpleAdapter adapter = new SimpleAdapter(this, gtPlayList(),
//				R.layout.sd_list, new String[] { "name" },
//				new int[] { R.id.mp4 });
	//	listView.setAdapter(adapter);
		// listView每条记录点击产生的事件
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				isPrepared.set(false);
				System.out.println( " onclick + ++++++++");
				try {
					System.out.println("reset+ ***************");
					//重新设置资源的时候不管是不是在播放中，直接reset这样才不会出错
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
					System.out.println("视频准备播放中");
					player.prepareAsync();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}			
				//player.start();
			//	System.out.println("视频准备播放中");
			}
		});
		// 给SurfaceView添加CallBack监听
		holder = surfaceView.getHolder();
		holder.addCallback(this);
		// 为了可以播放视频或者使用Camera预览，我们需要指定其Buffer类型
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// 下面开始实例化MediaPlayer对象
		player = new MediaPlayer();
		player.setOnCompletionListener(this);
		player.setOnErrorListener(this);
		player.setOnInfoListener(this);
		player.setOnPreparedListener(this);
		player.setOnSeekCompleteListener(this);
		player.setOnVideoSizeChangedListener(this);
		// 然后指定需要播放文件的路径，初始化MediaPlayer
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
		// 然后，我们取得当前Display对象
		currDisplay = this.getWindowManager().getDefaultDisplay();
		
		mHandler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	        	System.out.println("执行线程+++++++++++++++++++++++++++++++");
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
		// 当Surface尺寸等参数改变时触发
		Log.v("Surface Change:::", "surfaceChanged called");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// 当退出视频的时候触发
		Log.v("Surface Destory:::", "surfaceDestroyed called");
	}

	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		// 当video大小改变时触发
		// 这个方法在设置player的source后至少触发一次
		Log.v("Video Size Change", "onVideoSizeChanged called");
	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {
		// seek操作完成时触发
		Log.v("Seek Completion", "onSeekComplete called");
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// 当prepare完成后，该方法触发，在这里我们播放视频
		isPrepared.set(true);
		mProgress.setEnabled(true);
		mProgress.setMax(mp.getDuration());
		moveMax.setText(getTimeString(mp.getDuration()));
		Log.v(" onPrepared:::", "onPrepared called");
		// 首先取得video的宽和高
		vWidth = player.getVideoWidth();
		vHeight = player.getVideoHeight();

		if (vWidth > currDisplay.getWidth()
				|| vHeight > currDisplay.getHeight()) {
			// 如果video的宽或者高超出了当前屏幕的大小，则要进行缩放
			float wRatio = (float) vWidth / (float) currDisplay.getWidth();
			float hRatio = (float) vHeight / (float) currDisplay.getHeight();
			// 选择大的一个进行缩放
			float ratio = Math.max(wRatio, hRatio);
			vWidth = (int) Math.ceil((float) vWidth / ratio);
			vHeight = (int) Math.ceil((float) vHeight / ratio);
			// 设置surfaceView的布局参数
			surfaceView.setLayoutParams(new LinearLayout.LayoutParams(vWidth,vHeight));
			// 然后开始播放视频
			System.out.println("播放11111111111111");
			player.start();
			mHandler.sendEmptyMessage(2);
			System.out.println("播放11111111111111");
		}
	}
	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		// 当一些特定信息出现或者警告时触发
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
		// 当MediaPlayer播放完成后触发
		Log.v("Play onCompletion:::", "onComletion called");
		if(player.isPlaying()){
			player.stop();
		}
		// this.finish();
	}

	// 得到视频的播放列表
	private List<Map<String, String>> gtPlayList() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map1 = new HashMap<String, String>();
		Map<String, String> map2 = new HashMap<String, String>();
		Map<String, String> map3 = new HashMap<String, String>();
		map1.put("name", "视频1");
		map2.put("name", "视频2");
		map3.put("name", "视频3");
		list.add(map1);
		list.add(map2);
		list.add(map3);
		return list;
	}

	// 通过毫秒得到视频的分钟数和秒数
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

		items.add(new ListItem(R.drawable.publicity,"海南生态园领导视察","2014-06-20",R.drawable.pictureplay));
		items.add(new ListItem(R.drawable.publicity,"海南生态园领导视察","2014-06-21",R.drawable.pictureplay));
		items.add(new ListItem(R.drawable.publicity,"海南生态园领导视察","2014-06-22",R.drawable.pictureplay));
		items.add(new ListItem(R.drawable.publicity,"海南生态园领导视察","2014-06-23",R.drawable.pictureplay));
		items.add(new ListItem(R.drawable.publicity,"海南生态园领导视察","2014-06-24",R.drawable.pictureplay));
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