package co.kr.network;


import android.app.Activity;

import android.content.Context;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.webkit.WebSettings;


public class NetActivity extends Activity{
	Context mContext;
	//private ProgressDialog progDial;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//end if
		
	}
	
	public boolean networkCheck(){
		ConnectivityManager manager = 
			     (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo lte_4g = manager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
		boolean blte_4g=false;
		if(lte_4g!=null)
			blte_4g=lte_4g.isConnected();
		if(mobile.isConnected()||wifi.isConnected()||blte_4g)
			return true;
		else{
			//Toast.makeText(this,"네트워크를 확인하십시오",Toast.LENGTH_SHORT).show();
			return false;
			
		}

	}
	
	public void networkSetting(WebSettings set){
		/*set.setJavaScriptEnabled(true);
		set.setBuiltInZoomControls(true);
		set.setLoadWithOverviewMode(true);
		set.setUseWideViewPort(true);
		set.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);*/
	}
	/*public void webProgress(WebView webview,final Activity activity){
		
		webview.setWebViewClient(new WebViewClient(){
			@Override
			public void onLoadResource(WebView view, String url) {
				// TODO Auto-generated method stub
				if(progDial==null){
					progDial=new ProgressDialog(activity);
					progDial.setMessage("로딩중...");
					progDial.show();
				}
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				if(progDial.isShowing()){
					progDial.dismiss();
					progDial=null;
				}
			}
			public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) { 
				Toast.makeText(activity,"로딩오류",Toast.LENGTH_SHORT).show();
				progDial.dismiss();
				progDial=null;
			} 

		});
	}*/
	
}
