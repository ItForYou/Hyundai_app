package co.kr.hyundai_app;

import kr.co.parser.HtmlParser;


import co.kr.network.NetActivity;

import com.google.android.gcm.GCMRegistrar;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;

import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;

import android.view.Menu;

import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;

import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends NetActivity {
	String regId;
	String SENDER_ID;
	Context mContext;
	String url;
	
	final int requestCode=0;
	private boolean net_chk;
	public static Activity main_act;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		net_chk=networkCheck();//��Ʈ��ũ ����üũ
		main_act=this;
		//GCM�˸� �޼��� ���
		url=this.getString(R.string.json_setup).toString();
		mContext=this;
		GCMRegistrar.checkDevice(this);//����̽��� �۵��� �Ǵ��� �� �Ǵ��� üũ
		GCMRegistrar.checkManifest(this);//�޴��佺Ʈ�� ���Ѽ����� �Ǿ��ִ��� üũ
		regId=GCMRegistrar.getRegistrationId(this);//GCM����Ű��
		SENDER_ID="879391621165";//������Ʈ ��ȣ
		TelephonyManager tpm=(TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
		final String myNumber=tpm.getLine1Number();
		
		//GCM ���̵� ���� �޴����� ������ ������
		HtmlParser hp=new HtmlParser(url+"?regid="+regId+"&phone="+myNumber);
		String content = hp.singleHtmlParser();
		final String jsonContent=hp.singleJsonParser(content);
		Log.d("json",jsonContent);
		
		SharedPreferences settings=PreferenceManager.getDefaultSharedPreferences(this);
		String mb_id=settings.getString("ss_mb_id","").toString();
		
		if(net_chk){
			boolean isLogin=true;
			
			if(mb_id.equals("")){
				isLogin=false;
			}
			if(jsonContent.equals("not")){
				isLogin=false;
			}
			
			if(isLogin==false){
				Intent intent = new Intent(this,LoginActivity.class);
				startActivity(intent);
				finish();
			   
			}else{
				animationStart();
			}
			if (regId.equals("")) {
				//Log.d("gcmOk","ok");
				GCMRegistrar.register(this, SENDER_ID);
			} else {
				Log.v("send", "Already registered");
			}
		}else{
			Toast.makeText(this,"���ͳݻ��°� ��ȣ���� �ʽ��ϴ�.",Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	public void onResume(){ 
		super.onResume();
		
	}
	public void onPause(){
		super.onPause();
	}
	public void onDestroy () {
		super.onDestroy();
		try {
			//������ �� �ٽ� ����Ű�� ���ǰ��� �ʱ�ȭ
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			// TODO: handle exception
		}
        
	}
	
	private void animationStart(){
		final Animation an=AnimationUtils.loadAnimation(this, R.anim.alpha);
		an.setDuration(3000);
		//�ִϸ��̼� �̺�Ʈ
		an.setAnimationListener(new AnimationListener() {
			//�ִϸ��̼� �������� �� �̺�Ʈ
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			//�ִϸ��̼� �ϴ� ���߿� �̺�Ʈ
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub 
				
			}
			//�ִϸ��̼� ������ �� �̺�Ʈ
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,MenuActivity.class);
				MainActivity.this.startActivity(intent);
				MainActivity.this.overridePendingTransition(R.anim.alpha,R.anim.hold);
				MainActivity.this.finish();
			}
		});
		
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(an);
		set.setFillEnabled(true);
		set.setFillAfter(true);
		ImageView iv=(ImageView)findViewById(R.id.mainImage);
		iv.setAnimation(set);
	}
}
