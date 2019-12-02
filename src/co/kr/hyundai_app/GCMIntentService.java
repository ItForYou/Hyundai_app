package co.kr.hyundai_app;


import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import com.google.android.gcm.GCMBaseIntentService;


@SuppressLint("Wakelock")
public class GCMIntentService extends GCMBaseIntentService {
    private static final String tag = "GCMIntentService";
    private static final String PROJECT_ID = "879391621165";
    public static Notification sNotification;
   
    String url;

    //구글 api 페이지 주소 [https://code.google.com/apis/console/#project:긴 번호]


   //#project: 이후의 숫자가 위의 PROJECT_ID 값에 해당한다
    

    //public 기본 생성자를 무조건 만들어야 한다.

    WakeLock wl;
    public GCMIntentService(){ this(PROJECT_ID); }

   

    public GCMIntentService(String project_id) { super(project_id); }


 

    /** 푸시로 받은 메시지 */
    @Override
    protected void onMessage(Context context, Intent intent) {
       
    	final String jsonMsg=intent.getExtras().getString("msg");
    	
    	try {
			final JSONObject json = new JSONObject(jsonMsg);
			final String title = json.getString("title");
			final String msg = json.getString("msg");
			url= json.getString("url");
			if(wl==null){
				PowerManager pm=(PowerManager)getSystemService(Context.POWER_SERVICE);
				wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"wakeLock");
				wl.acquire();
				
			}
			showNotification(context, title, msg, url);
			if(wl!=null){
				if(wl.isHeld()){
					wl.release();
					wl=null;
					Intent pintent = new Intent(context,PopupActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					pintent.putExtra("url",url);
					pintent.putExtra("msg",msg);
					pintent.putExtra("title",title);
					startActivity(pintent);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("error", e.toString());
		}
    	
    }
    @SuppressWarnings("deprecation")
	public void showNotification(Context context,String title,String msg,String url){
    	final NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    	final Intent intent = new Intent(context, MainActivity.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	intent.putExtra("url",url);
    	Log.d("url",url+"|||");
    	final PendingIntent contentIntent = PendingIntent.getActivity(context, (int)System.currentTimeMillis(), intent, 0);
    	sNotification = new Notification(R.drawable.icon, msg, System.currentTimeMillis());
    	sNotification.defaults = Notification.DEFAULT_VIBRATE;
		sNotification.defaults |= Notification.DEFAULT_SOUND;
		sNotification.vibrate = new long[] {100, 250, 100, 500};
		sNotification.flags = Notification.FLAG_AUTO_CANCEL;
		sNotification.tickerText = msg;
    	sNotification.setLatestEventInfo(context, title, msg, contentIntent);
    	manager.notify(0, sNotification);
    }

    


    /**에러 발생시*/
    protected void on_error(Context context, String errorId) {
        Log.d(tag, "on_error. errorId : "+errorId);
    }

    
  

    /**단말에서 GCM 서비스 등록 했을 때 등록 id를 받는다*/
    @Override
    protected void onRegistered(Context context, String regId) {
        Log.d(tag, "onRegistered. regId : "+regId);
    }





    /**단말에서 GCM 서비스 등록 해지를 하면 해지된 등록 id를 받는다*/
    @Override
    protected void onUnregistered(Context context, String regId) {
        Log.d(tag, "onUnregistered. regId : "+regId);
    }



	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub
	}
}

