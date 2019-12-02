package co.kr.hyundai_app;

import kr.co.parser.HtmlParser;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import android.widget.Toast;
import co.kr.network.NetActivity;

public class MenuActivity extends NetActivity{
	String mb_id=null; 
	boolean isBool=false;
	public int ss_mb_level,ss_mb_state,ss_mb_num,login_error;
	public String ss_mb_id,ss_login_ok="";
	public static Activity menuAct;
	@SuppressWarnings("unused")
	private boolean net_chk;
	@SuppressWarnings("unused")
	private static Context mContext;
	@SuppressWarnings("unused")
	private SharedPreferences mPref;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_activity);
		SharedPreferences settings=PreferenceManager.getDefaultSharedPreferences(this);
		mb_id=settings.getString("ss_mb_id","").toString();
		HtmlParser hp2=new HtmlParser(getResources().getString(R.string.login)+"?mb_id="+mb_id);
		String content2 = hp2.singleHtmlParser();
		Log.d("TAG",content2);
		hp2.loginJsonParser(content2,this);
		
		mContext=this;
		menuAct=this;
		mPref=PreferenceManager.getDefaultSharedPreferences(this);
		
		ss_mb_state=settings.getInt("ss_mb_state",0);
		ss_mb_level=settings.getInt("ss_mb_level",0);
		ss_login_ok=settings.getString("ss_login_ok","ok");
	}
	public void cargoWrite(final View v){
		if(ss_mb_state!=0){
			Toast.makeText(this,"승인되지 않은 회원입니다. 관리자에게 문의주시길 바랍니다.",Toast.LENGTH_SHORT).show();
		}else{
			Intent intent = new Intent(this,WebActivity.class);
			intent.putExtra("url",getResources().getString(R.string.cargo_write));
			startActivity(intent);
		}
	}
	public void cargoList(final View v){
		if(ss_mb_state!=0){
			Toast.makeText(this,"승인되지 않은 회원입니다. 관리자에게 문의주시길 바랍니다.",Toast.LENGTH_SHORT).show();
		}else{
			Intent intent = new Intent(this,WebActivity.class);
			intent.putExtra("url",getResources().getString(R.string.cargo_list));
			startActivity(intent);
		}
	}
	public void allCargoList(final View v){
		if(ss_mb_level!=10){
			Toast.makeText(this,"관리자만 보실 수 있습니다.",Toast.LENGTH_SHORT).show();
		}else{
			Intent intent = new Intent(this,WebActivity.class);
			intent.putExtra("url",getResources().getString(R.string.cargo_list));
			startActivity(intent);
		}
	}
	public void customList(final View v){
			Intent intent = new Intent(this,WebActivity.class);
			intent.putExtra("url",getResources().getString(R.string.custom_list));
			startActivity(intent);
	}
	public boolean onKeyDown(int keyCode,KeyEvent event){
		if(!isBool){
			switch(keyCode){
			case KeyEvent.KEYCODE_BACK:
				Toast.makeText(this,"뒤로가기 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
				isBool=true;
				//mHandler.sendEmptyMessageDelayed(0, 2000);
				return false;
			default:
				
				break;
			}
		}else{
			
			finish();
			
		}
		return false;
	}
	
}
