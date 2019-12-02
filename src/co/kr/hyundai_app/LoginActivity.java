package co.kr.hyundai_app;

import kr.co.parser.HtmlParser;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import co.kr.network.NetActivity;

public class LoginActivity extends NetActivity {
	Button btn1,btn2;
	EditText edt1,edt2;
	Context con;
	public static Activity lo_act2;
	final String mb_join_php="http://www.현대물류.kr/bbs/mobile_mb_join.php";
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.join_check);
		btn1=(Button)findViewById(R.id.join_btn1);
		btn2=(Button)findViewById(R.id.join_btn2);
		edt1=(EditText)findViewById(R.id.editText1);
		edt2=(EditText)findViewById(R.id.editText2);
		con=this;
		final Activity lo_act=this;
		lo_act2=this;
		TelephonyManager tpm=(TelephonyManager)con.getSystemService(Context.TELEPHONY_SERVICE);
		final String myNumber=tpm.getLine1Number();
		btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(edt1.getText().toString().equals("")){
					Toast.makeText(con,"아이디를 입력하십시오",Toast.LENGTH_SHORT).show();
				}else if(edt2.getText().toString().equals("")){
					Toast.makeText(con,"비밀번호를 입력하십시오",Toast.LENGTH_SHORT).show();
				}else{
					
					HtmlParser hp=new HtmlParser(getResources().getString(R.string.login)+"?mb_id="+edt1.getText().toString()+"&mb_password="+edt2.getText().toString()+"&phone="+myNumber);
					String content = hp.singleHtmlParser();
					hp.loginJsonParser(content, con,lo_act);
				}
			}
		});
		 btn2.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent =new Intent(con,WebActivity.class);
					final String joinUrl=mb_join_php+"?mb_mobile_re="+myNumber;
					intent.putExtra("url",joinUrl);
					//MainActivity.this.startActivityForResult(intent, requestCode);
					con.startActivity(intent);
					finish();
					
				}
			});
	}
}
