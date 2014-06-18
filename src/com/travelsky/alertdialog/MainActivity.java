package com.travelsky.alertdialog;

import com.example.hellodear.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity{
	//����ѡ�������  
    private int selectedCityIndex = 0;  
    
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		
//		Builder builder = new AlertDialog.Builder(this);
//		
//		builder.setTitle("�Ի���");
//		builder.setIcon(R.drawable.ic_aa);
//		builder.setMessage("����һ����򵥵ĶԻ���");
//   1.
//		builder.create();
//		builder.show();
		
//		2.
//		//Ϊ��ȷ������ťע������¼�  
//		builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {  
//             @Override  
//             public void onClick(DialogInterface dialog, int which) {  
//                  // ����ʵ�������д��Ӧ���롣  
//            	 Toast.makeText(MainActivity.this, "ȷ��", Toast.LENGTH_SHORT).show();;
//            	 
//             }  
//        });  
//		
//		//ȡ��
//		builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				Toast.makeText(MainActivity.this, "ȡ��", Toast.LENGTH_SHORT).show();;
//			}
//		});
//		
//		//�鿴����
//		builder.setNeutralButton(R.string.detail, new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				Toast.makeText(MainActivity.this, "�鿴��ϸ", Toast.LENGTH_SHORT).show();;
//			}
//		});
//		builder.create();
//		builder.show();

		//3.
//		setContentView(R.layout.activity_main);
//		final String items[] = {"����","����","�Ϻ�","����"};
//		Builder builder = new AlertDialog.Builder(this);
//		builder.setTitle("��ϲ��������ĸ�����");
//		builder.setIcon(R.drawable.ic_aa);
//		builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				selectedCityIndex = which;
//				
//			}
//		}); 
//			
//		builder.setNegativeButton(R.string.sure, new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				Toast.makeText(MainActivity.this, "��ϲ���ĳ�����"+ items[selectedCityIndex], Toast.LENGTH_SHORT).show();
//				
//			}
//		});
//		
//		builder.setNeutralButton(R.string.cancle, new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				Toast.makeText(MainActivity.this, "ȡ��", Toast.LENGTH_LONG).show();
//			}
//		});
//		
//		builder.create();
//		builder.show();
		
		setContentView(R.layout.activity_main);
		final String items[] = new String[]{"����","����","ƹ����","������"};
		final boolean[] sportSeleted = new boolean[]{false,false,false,false};
		
		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("��ϲ���������Щ�˶���Ŀ��");
		builder.setIcon(R.drawable.ic_aa);
		
		builder.setMultiChoiceItems(items, sportSeleted, new DialogInterface.OnMultiChoiceClickListener() {  
	           
	           @Override  
	           public void onClick(DialogInterface dialog, int which, boolean isChecked) {  
	        	   sportSeleted[which] = isChecked;              //ѡ����Ĳ�����ٱ��浽ѡ�������  
	           }  
	       })  ;
		
		builder.setNegativeButton(R.string.sure, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				StringBuilder sb = new StringBuilder();
				for(int i=0;i<sportSeleted.length;i++){
					if(sportSeleted[i] == true){
						sb.append(items[i]+ ",");
					}
				}
				Toast.makeText(MainActivity.this, "��ϲ�����˶���"+sb, Toast.LENGTH_SHORT).show();
				
				
			}
		});
			
		builder.setNeutralButton(R.string.cancle, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(MainActivity.this, "ȡ��", Toast.LENGTH_SHORT).show();			
			}
		});
		
		builder.create();
		builder.show();
	}

}
