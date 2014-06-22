package com.example.hellodear;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.travelsky.movie.R;
import com.travelsky.utils.ItemArrayAdapter;
import com.travelsky.utils.ListItem;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity implements OnCompletionListener,
		OnErrorListener, OnInfoListener, OnPreparedListener,OnSeekCompleteListener,
		OnVideoSizeChangedListener,SurfaceHolder.Callback {
	private Display currDisplay;
	private SurfaceView surfaceView;
	private SurfaceHolder holder;
	private MediaPlayer player;
	private int vWidth, vHeight;
	private String dataPath;
	private Intent floatingwindow = null;
	private String TAG = "android.intent.action.MainActivity";
	MyBroadcastReceiver mr;
	private Boolean playOrPause = true;

	 private boolean serviceStartOrstop = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);

		// this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏显示 
		surfaceView = (SurfaceView) this.findViewById(R.id.video_surface);
		ListView listView = (ListView) this.findViewById(R.id.lv);

//		SimpleAdapter adapter = new SimpleAdapter(this, gtPlayList(),
//				R.layout.sd_list, new String[] { "name" },
//				new int[] { R.id.mp4 });
//		listView.setAdapter(adapter);
		
		//新加的自定义Adapter
		List<ListItem> items = getListItems();

//		ItemArrayAdapter adapter = new ItemArrayAdapter(MainActivity.this, R.layout.window);

//		listView.setAdapter(adapter);
		
		
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					//判断是不是正在播放中
					if (player.isPlaying() == true) {
						//重置播放器  然后可以重新塞如资源路径 调用prepare()方法
						player.reset();
					}
					player.setAudioStreamType(AudioManager.STREAM_MUSIC);
					player.setDisplay(holder);
					System.out.println(holder + "@@@@@@@");
					System.out.println(player + "@@@@@@@@@");
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
					player.prepare();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				player.start();
			}
		});

		// 给SurfaceView添加CallBack监听
		holder = surfaceView.getHolder();
		holder.addCallback(this);
		// 为了可以播放视频或者使用Camera预览，我们需要指定其Buffer类型
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		System.out.println("aaaaaaaaaaaaaaaaaaaaaa+ **************");
		
		// 下面开始实例化MediaPlayer对象
		player = new MediaPlayer();
		player.setOnCompletionListener(this);
		player.setOnErrorListener(this);
		player.setOnInfoListener(this);
		player.setOnPreparedListener(this);
		player.setOnSeekCompleteListener(this);
		player.setOnVideoSizeChangedListener(this);
		System.out.println(holder + "------------");
		System.out.println(player + "-------------");

		// 然后指定需要播放文件的路径，初始化MediaPlayer
		// String dataPath =
		// Environment.getExternalStorageDirectory().getPath()+"/Test_Movie.m4v";
		dataPath = "http://116.211.111.250/share.php?method=Share.download&cqid=1acc93049fd469acbe349ff025131a51&dt=53.e3050a26cec2eb1422c3ae24ecf9f4b2&e=1403320899&fhash=eae8797988d66421a32798437ae6674e537c29f8&fname=MOV008.mp4&fsize=15497693&nid=14029320224978335&scid=53&st=9983b999fac628e93cb7ce14ff4d3122&xqid=198731395";		
		try {
			player.setDataSource(dataPath);
			Log.v("Next:::", "Next called");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 然后，我们取得当前Display对象
		currDisplay = this.getWindowManager().getDefaultDisplay();

		floatingwindow = new Intent();
		floatingwindow.setClass(MainActivity.this, FloatingService.class);
//		floatingwindow.putExtra("vWidth", vWidth);
//		floatingwindow.putExtra("vHeight", vHeight);
		mr = new MyBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(TAG);
		this.registerReceiver(mr, intentFilter);
//		Intent intent = getIntent();
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// 当Surface尺寸等参数改变时触发
		Log.v("Surface Change:::", "surfaceChanged called");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.v("Surface Created:::", "surfaceCreated called");
		// 当SurfaceView中的Surface被创建的时候被调用
		// 在这里我们指定MediaPlayer在当前的Surface中进行播放
		player.setDisplay(holder);
		// 在指定了MediaPlayer播放的容器后，我们就可以使用prepare或者prepareAsync来准备播放了
		try {
			player.prepareAsync();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}

	}

	// 当退出视频页面的时候执行的方法
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.v("Surface Destory:::", "surfaceDestroyed called");
	}

	@Override
	public void onVideoSizeChanged(MediaPlayer arg0, int arg1, int arg2) {
		// 当video大小改变时触发
		// 这个方法在设置player的source后至少触发一次
		Log.v("Video Size Change", "onVideoSizeChanged called");

	}

	@Override
	public void onSeekComplete(MediaPlayer arg0) {
		// seek操作完成时触发
		Log.v("Seek Completion", "onSeekComplete called");

	}

	@Override
	public void onPrepared(MediaPlayer player) {
		// 当prepare完成后，该方法触发，在这里我们播放视频

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
			surfaceView.setLayoutParams(new LinearLayout.LayoutParams(vWidth,
					vHeight));

			// 然后开始播放视频

			player.start();
			//player.pause();
			
		}
	}

	@Override
	public boolean onInfo(MediaPlayer player, int whatInfo, int extra) {
		// 当一些特定信息出现或者警告时触发
		switch (whatInfo) {
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
	public boolean onError(MediaPlayer player, int whatError, int extra) {
		Log.v("Play Error:::", "onError called");
		switch (whatError) {
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

	
	//当MediaPlayer播放结束后执行这个方法
	@Override
	public void onCompletion(MediaPlayer player) {
		// 当MediaPlayer播放完成后触发
		Log.v("Play onCompletion:::", "onComletion called");
		player.stop();
		// this.finish();
	}
	
	
	//得到视频的播放列表
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

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_UP) {
			return super.onTouchEvent(event);
		} // else ACTION_UP
		
		if(!serviceStartOrstop){
			System.out.println("on TouchEvent start --------------------");
			int position = this.player.getCurrentPosition();
			//视频当前播放的长度 单位毫秒
			floatingwindow.putExtra("position", position);
			//视频总的长度  单位毫秒
			floatingwindow.putExtra("max", player.getDuration());
			floatingwindow.putExtra("visable", true); //
			floatingwindow.putExtra("vWidth", vWidth);
			floatingwindow.putExtra("vHeight", vHeight);
			floatingwindow.putExtra("playOrPause", playOrPause);
			System.out.println(playOrPause + "???????????????????????????????????");
			startService(floatingwindow);
			serviceStartOrstop = true;
		}else{
			System.out.println("on TouchEvent end --------------------");
			stopService(floatingwindow);
			serviceStartOrstop = false;
		}
		return true;
	}

	public class MyBroadcastReceiver extends BroadcastReceiver { // 自定义广播接受者
		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			System.out.println(action + "&&&&&&&&&&&&&&&&&&&&&");
			System.out.println(TAG+ "&&&&&&&&&&&&&&&&&&&&&&&");
			if (action.equals(TAG)) {
				String flag = intent.getStringExtra("flag");
				System.out.println(flag + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				if (flag.equals("play")) {
					if (!player.isPlaying()){
						player.start();							
						System.out.println("开始播放视频");
					}
					playOrPause = false;
				} else if (flag.equals("pause")) {
					if (player.isPlaying()){
						player.pause(); // 暂停
						
						System.out.println("暂停视频");
					}
					playOrPause = true;
				} else {
					MainActivity.this.finish();
				}
			}
		}
	}
	
	private List<ListItem> getListItems(){
		List<ListItem> items = new ArrayList<ListItem>();

		items.add(new ListItem(R.drawable.publicity,"海南生态园1","2014-06-22",R.drawable.pictureplay));
		items.add(new ListItem(R.drawable.publicity,"海南生态园2","2014-06-22",R.drawable.pictureplay));
		items.add(new ListItem(R.drawable.publicity,"海南生态园3","2014-06-22",R.drawable.pictureplay));
		items.add(new ListItem(R.drawable.publicity,"海南生态园4","2014-06-22",R.drawable.pictureplay));
		items.add(new ListItem(R.drawable.publicity,"海南生态园5","2014-06-22",R.drawable.pictureplay));
		return items;
	}
}

// @Override
// protected void onCreate(Bundle savedInstanceState) {
// super.onCreate(savedInstanceState);
// setContentView(R.layout.activity_main);
//
// // if (savedInstanceState == null) {
// // getSupportFragmentManager().beginTransaction()
// // .add(R.id.container, new PlaceholderFragment())
// // .commit();
// // }
//
// // new Handler().post((new Runnable() {
// // @Override
// // public void run() {
// Log.i("aa", "aa");
// Uri uri = Uri.parse("http://v.youku.com/v_show/id_XNzI3MTU3OTM2.html");
// VideoView videoView = (VideoView)this.findViewById(R.id.video_view);
// videoView.setMediaController(new MediaController(this));
// videoView.setVideoURI(uri);
// //videoView.start();
// videoView.requestFocus();
// Log.i("bb", "bb");
//
// // }
// // }));
// }
//
//
//
//
//
//
// //
// // @Override
// // public boolean onCreateOptionsMenu(Menu menu) {
// //
// // // Inflate the menu; this adds items to the action bar if it is present.
// // getMenuInflater().inflate(R.menu.main, menu);
// // return true;
// // }
// //
// // @Override
// // public boolean onOptionsItemSelected(MenuItem item) {
// // // Handle action bar item clicks here. The action bar will
// // // automatically handle clicks on the Home/Up button, so long
// // // as you specify a parent activity in AndroidManifest.xml.
// // int id = item.getItemId();
// // if (id == R.id.action_settings) {
// // return true;
// // }
// // return super.onOptionsItemSelected(item);
// // }
// //
// // /**
// // * A placeholder fragment containing a simple view.
// // */
// // public static class PlaceholderFragment extends Fragment {
// //
// // public PlaceholderFragment() {
// // }
// //
// // @Override
// // public View onCreateView(LayoutInflater inflater, ViewGroup container,
// // Bundle savedInstanceState) {
// // View rootView = inflater.inflate(R.layout.fragment_main, container,
// false);
// // return rootView;
// // }
// // }
//
// }
