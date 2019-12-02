package co.kr.hyundai_app;



import android.app.AlertDialog;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import co.kr.network.NetActivity;

public class PopupActivity extends NetActivity{
	String url;
	String title;
	String msg;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup_activity);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		url=getIntent().getStringExtra("url");
		title=getIntent().getStringExtra("title");
		msg=getIntent().getStringExtra("msg");
		new AlertDialog.Builder(this)
    	.setTitle(title)
    	.setMessage(msg)
    	.setCancelable(false)
    	.setInverseBackgroundForced(false)
    	.setPositiveButton(android.R.string.ok,
    			new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//result.confirm();
						moveWebIntent(url);
						finish();
					}
				}
    	)
		.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//result.cancel();
						finish();
					}
				}
		)
		.create()
		.show();
		
	}
	public void moveWebIntent(final String url){
		final Intent intent = new Intent(this, MainActivity.class);
		
		Context con=PopupActivity.this;
		con.startActivity(intent);
		
		final NotificationManager manager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancelAll();
	}
}
