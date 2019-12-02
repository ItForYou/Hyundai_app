package co.kr.webview.setup;


import co.kr.hyundai_app.LoginActivity;
import co.kr.hyundai_app.MainActivity;
import co.kr.hyundai_app.MenuActivity;
import co.kr.hyundai_app.R;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;

import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import android.webkit.JavascriptInterface;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


@SuppressLint("SetJavaScriptEnabled")
public class WebViewSetup {
	WebView webview;
	Context con;
	private ProgressBar progDial;
	WebSettings set;
	private Handler handler = new Handler();
	@SuppressWarnings("unused")
	private SharedPreferences mPref;
	int ok;
	private String nextUrl;
	private String loginUrl;
	private String mb_id;
	private SharedPreferences settings;
	private boolean isBool=false;
	public WebViewSetup() {
		// TODO Auto-generated constructor stub
	}
	public WebViewSetup(WebView webview,Context con,WebSettings set){
		this.webview=webview;
		this.con=con;
		this.set=set;
	}
	@SuppressWarnings("deprecation")
	public void webviewSetup(){
		settings=PreferenceManager.getDefaultSharedPreferences(con);
		mb_id=settings.getString("mb_id", "").toString();
		loginUrl=con.getString(R.string.mb_login)+"?mb_id="+mb_id;
		webview.setVerticalScrollbarOverlay(true);//스크롤바 여백 생기는 현상 막기
		//캐쉬설정
		webview.clearCache(true);
		set.setCacheMode(WebSettings.LOAD_NO_CACHE);
		set.setAppCacheEnabled(true);
		set.setDomStorageEnabled(true);
		set.setAppCacheMaxSize(1028*1028*4);
		set.setSupportZoom(false); // 확대,축소 기능을 사용할 수 있도록 설정
		set.setJavaScriptCanOpenWindowsAutomatically(true);
		set.setPluginsEnabled(true);
		
		final String appCachePath = con.getApplicationContext().getCacheDir().getAbsolutePath();
		set.setAppCachePath(appCachePath);
		webview.getSettings().setAppCachePath(appCachePath);
		webview.getSettings().setAllowFileAccess(true);
		webview.getSettings().setAppCacheEnabled(false);
		webview.getSettings().setJavaScriptEnabled(true);
		
		//가로 스크롤 바 제거
		webview.setHorizontalScrollBarEnabled(false);
		webview.setWebChromeClient(new WebChromeClient());
		mPref=PreferenceManager.getDefaultSharedPreferences(con);
		
		webview.addJavascriptInterface(new ViewGone(),"gone");
		webview.addJavascriptInterface(new SaveMember(),"save");
		
		CookieSyncManager.createInstance(con);
	}
	private class ViewGone
    { 
		@SuppressWarnings("unused")
		@JavascriptInterface
		public void displays(final String msg) {
			handler.post(new Runnable() {
				public void run() {
					progDial.setVisibility(View.GONE);
				}
			});
		}
    }  
	
	private class SaveMember
    {
		@SuppressWarnings("unused")
		@JavascriptInterface
		public void memSave(final String msg) {
			handler.post(new Runnable() {
				public void run() {
					//Toast.makeText(con,msg,Toast.LENGTH_SHORT).show();				
					
					if(msg.toString()!=""){
						ok=1;
					}
				
					
					
				} 
			});
		}
    }  
	public void WebChromeSetup(){
		webview.setWebChromeClient(new WebChromeClient() {	
			@Override
		      public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
		            quotaUpdater.updateQuota(spaceNeeded * 2);
		      }//end onReachedMaxAppCacheSize Method
			@Override
			public void onCloseWindow(WebView window) {
		         super.onCloseWindow(window);
		    }
			 @Override
			    public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
				    
				    
			        new AlertDialog.Builder(con)
			            .setTitle("알림")
			            .setMessage(message)
			            .setPositiveButton(android.R.string.ok,
			                    new AlertDialog.OnClickListener() {
			                        public void onClick(DialogInterface dialog, int which) {
			                            result.confirm();
			                        }
			                    })
			            .setCancelable(false)
			            .create()
			            .show();

			        return true;
			    };
			    public boolean onJsConfirm(WebView view, String url, String message, final android.webkit.JsResult result){
			    	
			    	
			    	new AlertDialog.Builder(con)
			    	.setTitle("선택")
			    	.setMessage(message)
			    	.setPositiveButton(android.R.string.ok,
			    			new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									result.confirm();
								}
							}
			    	)
					.setNegativeButton(android.R.string.cancel,
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									result.cancel();
								}
							}
					)
					.create()
					.show();
			    	return true;
			    }
			    
		});
		//로딩 프로그래스		
	}
	public void webViewClientSetup(final Activity activity, final Intent intent){
		
		webview.setWebViewClient(new WebViewClient(){
			//페
		  public boolean shouldOverrideUrlLoading(WebView view,final String url) {
			  
			  WebBackForwardList history=webview.copyBackForwardList();
			  if(history.getCurrentIndex()<1){
				  activity.finish();
			  }
			  if(url.equals("http://www.xn--vk1b93jqsbr18c.kr/bbs/home")){
				  
				  final Intent intent=new Intent(con,MenuActivity.class);
				  con.startActivity(intent);
				  activity.finish();
				  MenuActivity.menuAct.finish();
				  
			  }
			  if(url.startsWith("tel:")){
				  final Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url)); 
	                con.startActivity(intent);
	                return true;
			  }else if (url.startsWith("sms:")) {
		        	final Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
		        	con.startActivity(intent);
		        	return true;
		       }else{  
				  view.loadUrl(nextUrl);
			  }
			  if(ok==1){
				  Intent intent = new Intent(con,MainActivity.class);
				  con.startActivity(intent);
				  activity.finish();
				  MainActivity.main_act.finish();
				  LoginActivity.lo_act2.finish();
			  }
			  return false;
	        }
			@SuppressWarnings("static-access")
			@Override
			public void onLoadResource(WebView view, String url) {
				// TODO Auto-generated method stub
				
				if(progDial==null){
					progDial=(ProgressBar)activity.findViewById(R.id.progressBar1);
					progDial.setVisibility(view.VISIBLE);
				}
			}
			@SuppressWarnings("static-access")
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				try {
					//set.setJavaScriptEnabled(true);
					final String  nextUrl=intent.getStringExtra("url").toString();
					if(!isBool){
						if(!nextUrl.equals("")){
							if(progDial.getVisibility()==view.VISIBLE){
								progDial.setVisibility(view.GONE);
								progDial=null;
								CookieSyncManager.getInstance().sync();
								isBool=true;
								webview.loadUrl(nextUrl);
							}else{
								
							}
						}else{
							
						}
					}else{
						if(progDial.getVisibility()==view.VISIBLE){
							progDial.setVisibility(view.GONE);
							progDial=null;
							CookieSyncManager.getInstance().sync();
							isBool=true;
							//webview.loadUrl(nextUrl);
						}else{
							
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					
				}
				
			}
			public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) { 
				view.loadUrl(nextUrl);
				/*Toast.makeText(activity,"네트워크 확인 후 재실행 하십시오",Toast.LENGTH_SHORT).show();
				activity.finish();
				webview.loadUrl(null);
				//progDial.dismiss();
				progDial=null;*/
				
			}
		});
		this.webview.loadUrl(loginUrl);
		this.webview.clearHistory();
		//this.webview.clearFormData();
		//this.webview.clearCache(true);
	}
	public void deleteCatsh(){
		CookieManager cookieManager = CookieManager.getInstance();    
		cookieManager.removeSessionCookie();
	}
}
