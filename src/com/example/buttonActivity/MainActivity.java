package com.example.buttonActivity;



import com.example.hellodear.R;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity{
	
	private EditText tvUsername = null;
	private EditText tvPassWord = null;
	private Button btLogin = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		tvUsername = (EditText)super.findViewById(R.id.username);
		tvPassWord = (EditText)super.findViewById(R.id.password);
		btLogin = (Button)super.findViewById(R.id.button1);
		btLogin.setOnClickListener(new LoginOnClickListener());
				
				
				
//				new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				String username = tvUsername.getText().toString();
//				String password = tvPassWord.getText().toString();
//				String info = "�û�����"+ username + "@@@@����:"+ password;
//				
//				// ��һ����������ǰ�������Ļ���������getApplicationContext()��this 
//				// �ڶ���������Ҫ��ʾ���ַ�����Ҳ����R.string���ַ���ID 
//				// ��������������ʾ��ʱ�䳤�̡�ToastĬ�ϵ�������LENGTH_LONG(��)��LENGTH_SHORT(��)��Ҳ����ʹ�ú�����2000ms
//				Toast.makeText(getApplicationContext(), info,Toast.LENGTH_SHORT).show();				
//			}
//		});	
	}	
	
	 private class LoginOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			String username = tvUsername.getText().toString();
			String password = tvPassWord.getText().toString();
			String info = "�û�����"+ username + "@@@@����:"+ password;
			
			// ��һ����������ǰ�������Ļ���������getApplicationContext()��this 
			// �ڶ���������Ҫ��ʾ���ַ�����Ҳ����R.string���ַ���ID 
			// ��������������ʾ��ʱ�䳤�̡�ToastĬ�ϵ�������LENGTH_LONG(��)��LENGTH_SHORT(��)��Ҳ����ʹ�ú�����2000ms
			Toast.makeText(getApplicationContext(), info,Toast.LENGTH_SHORT).show();
			
		}
		
	}
	
}
