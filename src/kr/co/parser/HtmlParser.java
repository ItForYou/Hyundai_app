package kr.co.parser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.kr.hyundai_app.LoginActivity;
import co.kr.hyundai_app.MainActivity;
import co.kr.hyundai_app.MenuActivity;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class HtmlParser extends Thread{
	String addr;
	
	public HtmlParser(String address){
		addr=address;
	}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public String singleHtmlParser(){
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		URL url;
		StringBuilder html=new StringBuilder();
		try {
			url = new URL(addr);
			HttpURLConnection conn=(HttpURLConnection)url.openConnection();
			if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
				String line = "";
				if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
					BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
					for(;;){
						line=br.readLine();
						
						if(line==null){break;}
						html.append(line+"\n");
					}
					
					br.close();
					
					
				}
			}
			conn.disconnect();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return html.toString();
	}
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	public String versionHtmlParser(){
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);  
		
		URL url;
		
		StringBuilder html=new StringBuilder();
		try {
			url = new URL(addr);
			HttpURLConnection conn=(HttpURLConnection)url.openConnection();
			if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
				String line = "";
				if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
					BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
					for(;;){
						line=br.readLine();
						if(line==null){break;}
						html.append(line);
					}
					
					br.close();
				}
			}
			conn.disconnect();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return html.toString();
	}
	public String singleJsonParser(String content){
		String result="";
		try {
			JSONArray ja=new JSONArray("["+content.toString()+"]");
			for(int i=0;i<ja.length();i++){
				JSONObject jo=ja.getJSONObject(i);
				result+=jo.getString("ok").toString();
				//result+=jo.getString("content").toString();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		return result;
	}
@SuppressWarnings("unused")
public void loginJsonParser(String content,Context con){
		String result="";
		try {
			JSONArray ja=new JSONArray("["+content.toString()+"]");
			
			for(int i=0;i<ja.length();i++){
				JSONObject jo=ja.getJSONObject(i);
				SharedPreferences settings=PreferenceManager.getDefaultSharedPreferences(con);
				Editor editor=settings.edit();
				Log.d("TAG", jo.getInt("ss_mb_state")+"");
				editor.putString("mb_id", jo.getString("ss_mb_id"));
				editor.putString("ss_mb_id", jo.getString("ss_mb_id"));
				editor.putInt("ss_mb_level", jo.getInt("ss_mb_level"));
				editor.putInt("ss_mb_state", jo.getInt("ss_mb_state"));
				editor.putString("ss_login_ok", jo.getString("ss_login_ok"));
				editor.commit();
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		
		
	}
	@SuppressLint("ShowToast")
	public void loginJsonParser(String content,Context con,final Activity act){
		
		try {
			JSONArray ja=new JSONArray("["+content.toString()+"]");
			
			for(int i=0;i<ja.length();i++){
				JSONObject jo=ja.getJSONObject(i);
				
				SharedPreferences settings=PreferenceManager.getDefaultSharedPreferences(con);
			
				Editor editor=settings.edit();
				
				Log.d("TAG",jo.getString("ss_login_ok"));
				
				
				if(jo.getString("ss_login_ok").equals("not")){
					Toast.makeText(con,"로그인 실패하였습니다.",Toast.LENGTH_SHORT).show();
				}
				if(jo.getString("ss_login_ok").equals("not_state")){
					Toast.makeText(con,"승인되지 않은 회원입니다.",Toast.LENGTH_SHORT).show();
				}
				
				
				if(jo.getString("ss_login_ok").equals("ok")){
					editor.putString("mb_id", jo.getString("ss_mb_id"));
					editor.putString("ss_mb_id", jo.getString("ss_mb_id"));
					editor.putInt("ss_mb_level", jo.getInt("ss_mb_level"));
					editor.putInt("ss_mb_state", jo.getInt("ss_mb_state"));
					editor.putString("ss_login_ok", jo.getString("ss_login_ok"));
					editor.commit();
					Intent intent = new Intent(con,MainActivity.class);
					act.startActivity(intent);
					Toast.makeText(con,"로그인 성공하였습니다.",1000).show();
					act.finish();
					LoginActivity.lo_act2.finish();
				}
			
				//MainActivity.login_error=jo.getInt("error");
				//MainActivity.ss_mb_id=jo.getString("ss_mb_id").toString();
				//MainActivity.ss_login_ok=jo.getString("ss_login_ok").toString();
				//MainActivity.ss_mb_num=jo.getInt("ss_mb_num");
				//MainActivity.ss_mb_level=jo.getInt("ss_mb_level");
				//MainActivity.ss_mb_state=jo.getInt("ss_mb_state");
				
				//result+=jo.getString("content").toString();
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		
	}
}
