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
		net_chk=networkCheck();//��Ʈ��ũ ����üũ
		
		webview=(WebView)findViewById(R.id.webView1);
		set =webview.getSettings();
		Intent intent=getIntent();
		networkSetting(set);
		//���� ���� �ʱ�ȭ ����
		ws=new WebViewSetup(webview,mContext,set);
		//���� ���� �޼���
		ws.webviewSetup();
		//���� ũ�Ұ� �Ȱ��� ���·� ����
		ws.WebChromeSetup();
		final Activity activity=this;
		//�ڹٽ�ũ��Ʈ�� url�� �����ϴ� �޼���
		ws.webViewClientSetup(activity,intent);
		
		webview.setWebChromeClient(new WebCClient());
		
		//GCM�˸� ���
		
		//Log.d("url",url);
		if(net_chk){
			//webview.loadUrl(url);
		}
		//���̵� ����
		PreferenceManager.getDefaultSharedPreferences(this);
		
	}
	
	public boolean onKeyDown(int keyCode,KeyEvent event){
		//���信�� �ڷΰ��� ������ �� �ڷΰ��� �Ǵ� ���ò��� ����
		if(keyCode==KeyEvent.KEYCODE_BACK){
			net_chk=networkCheck();
			if(!net_chk){
				Toast.makeText(this,"��Ʈ��ũ�� �Ҿ����Ͽ� �����մϴ�.", Toast.LENGTH_SHORT).show();
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
			//������ �� �ٽ� ����Ű�� ���ǰ��� �ʱ�ȭ
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
			Toast.makeText(this,"�����ڸ� ���� �� �ֽ��ϴ�.",Toast.LENGTH_SHORT).show();
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
	//������ ���� ÷���ϴ� ���
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
			Toast.makeText(this,"���ε��� ���� ȸ���Դϴ�. �����ڿ��� �����ֽñ� �ٶ��ϴ�.",Toast.LENGTH_SHORT).show();
		}else if(ss_login_ok.equals("")){
			Toast.makeText(this,"ȸ�������� �̿��Ͻ� �� �ֽ��ϴ�.",Toast.LENGTH_SHORT).show();
		}else{
			String url=getResources().getString(geturl);
			webview.loadUrl(url);
		}
		
	}
	    
}