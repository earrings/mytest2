package com.travelsky.seekbar;

import com.example.hellodear.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnSeekBarChangeListener{
	private SeekBar seekbar = null;
	private TextView tvinfo01 = null;
	private TextView tvinfo02 = null;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seekbar_test);
		
		seekbar = (SeekBar)super.findViewById(R.id.seekbar);
		tvinfo01 = (TextView)super.findViewById(R.id.info01);
		tvinfo02 = (TextView)super.findViewById(R.id.info02);
		seekbar.setOnSeekBarChangeListener(this);
		
		
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		tvinfo01.setText("������ǰֵ:"+progress);  
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		tvinfo02.setText("�������ڵ���");  
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		tvinfo02.setText("����ֹͣ����");  
		
	}

}
