package co.kr.hyundai_app;







import co.kr.network.*;
import co.kr.webview.setup.WebViewSetup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.Uri;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
public class WebActivity extends NetActivity{
	WebView webview;
	String url;
	ListView lv,lv2;
	LinearLayout ll,ll2; 
	private boolean net_chk;
	//private ProgressDialog progDial;
	WebSettings set;
	Context mContext;
	WebViewSetup ws;
	LinearLayout ad_ll,my_ll;
	private ValueCallback<Uri> uploadMessage = null;
	private final static int FILECHOOSER_RESULTCODE = 1;
	private int geturl;
	private int ss_mb_state,ss_mb_level;
	private String ss_login_ok;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_activity);
		SharedPreferences settings=PreferenceManager.getDefaultSharedPreferences(this);
		ad_ll=(LinearLayout)findViewById(R.id.adminView);
		my_ll=(LinearLayout)findViewById(R.id.myView);
		ss_mb_state=settings.getInt("ss_mb_state",0);
		ss_mb_level=settings.getInt("ss_mb_level",0);
		ss_login_ok=settings.getString("ss_login_ok","");
		Log.d("TAG",my_ll.getVisibility()+"");
		if(ss_mb_level==10){
			ad_ll.setVisibility(View.VISIBLE);
		}
		if(ss_mb_level!=10){
			try {
				my_ll.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				// TODO: handle exception
				//Log.d("TAG",e.toString());
			}
			
			//adminView.setVisibility(View.GONE);
		}
		mContext=this;
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		net_chk=networkCheck();//네트워크 상태체크
		
		webview=(WebView)findViewById(R.id.webView1);
		set =webview.getSettings();
		Intent intent=getIntent();
		networkSetting(set);
		//웹뷰 세팅 초기화 상태
		ws=new WebViewSetup(webview,mContext,set);
		//웹뷰 설정 메서드
		ws.webviewSetup();
		//웹뷰 크롬과 똑같은 상태로 설정
		ws.WebChromeSetup();
		final Activity activity=this;
		//자바스크립트나 url을 설정하는 메서드
		ws.webViewClientSetup(activity,intent);
		
		webview.setWebChromeClient(new WebCClient());
		
		//GCM알림 기능
		
		//Log.d("url",url);
		if(net_chk){
			//webview.loadUrl(url);
		}
		//아이디 저장
		PreferenceManager.getDefaultSharedPreferences(this);
		
	}
	
	public boolean onKeyDown(int keyCode,KeyEvent event){
		//웹뷰에서 뒤로가기 눌렀을 때 뒤로가기 또는 어플끄기 설정
		if(keyCode==KeyEvent.KEYCODE_BACK){
			net_chk=networkCheck();
			if(!net_chk){
				Toast.makeText(this,"네트워크가 불안정하여 종료합니다.", Toast.LENGTH_SHORT).show();
				finish();
				return false;
			}else{
				try {
					WebBackForwardList history=this.webview.copyBackForwardList();
					
					
					if(history.getCurrentIndex()<2){
						this.finish();
						return false;
					}else{
						webview.goBack();
						return false;
						
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.d("error",e.toString());
				}
				
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	public void onDestroy () {
		super.onDestroy();
		try {
			//종료한 후 다시 어플키면 세션값은 초기화
			ws.deleteCatsh();
			//GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public void onPause(){
		super.onPause();
		CookieSyncManager.getInstance().stopSync();
	}
	public void onResume(){
		super.onResume();
		CookieSyncManager.getInstance().startSync();
	}
	public void cargoWrite(final View v){
		geturl=R.string.cargo_write;
		loginCheck();
	}
	public void cargoSearch(final View v){
		geturl=R.string.cargo_search;
		loginCheck();
	}
	public void cargoList(final View v){
		geturl=R.string.cargo_list;
		loginCheck();
	}
	public void allCargoList(final View v){
		geturl=R.string.cargo_list;
		if(ss_mb_level!=10){
			Toast.makeText(this,"관리자만 보실 수 있습니다.",Toast.LENGTH_SHORT).show();
		}else{
			loginCheck();
		}
	}
	public void customList(final View v){
		geturl=R.string.custom_list;
		loginCheck();
	}
	public void appFinish(final View v){
		try {
			MenuActivity.menuAct.finish();
			finish();
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
		}
		
	}
	//웹에서 파일 첨부하는 기능
	class WebCClient extends WebChromeClient {
	    @Override
	    public boolean onJsAlert(WebView view, String url, String message,
	            JsResult result) {
	        Toast.makeText(WebActivity.this, message, Toast.LENGTH_SHORT)
	                .show();
	        result.confirm();
	        return true;
	    }
	    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			openFileChooser(uploadMsg, "");
		}
	    // For Android 3.0+
	    public void openFileChooser(ValueCallback<Uri> uploadMsg,String acceptType) 
	    {
	        
	        Toast.makeText(WebActivity.this, acceptType, 0).show();
	        uploadMessage = uploadMsg;
	        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
	        i.addCategory(Intent.CATEGORY_OPENABLE);
	        i.setType("image/*");
	        WebActivity.this.startActivityForResult(
	                Intent.createChooser(i, "File Browser"),
	                FILECHOOSER_RESULTCODE);

	    }
	    public void openFileChooser(ValueCallback<Uri> uploadMsg,
				String acceptType, String capture) {
			openFileChooser(uploadMsg, "");
		}	    
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == FILECHOOSER_RESULTCODE && uploadMessage != null) {
			Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
			uploadMessage.onReceiveValue(result);
			uploadMessage = null;
		}
	}
	public void loginCheck(){
		
		if(ss_mb_state!=0){
			Toast.makeText(this,"승인되지 않은 회원입니다. 관리자에게 문의주시길 바랍니다.",Toast.LENGTH_SHORT).show();
		}else if(ss_login_ok.equals("")){
			Toast.makeText(this,"회원가입후 이용하실 수 있습니다.",Toast.LENGTH_SHORT).show();
		}else{
			String url=getResources().getString(geturl);
			webview.loadUrl(url);
		}
		
	}
	    
}