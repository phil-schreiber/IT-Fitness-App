package df.denkfabrik.itfitness;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.commons.codec.binary.Hex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;




public class EndGame extends Activity{
	private int RESULT_SESSION=0;
	private int GAMEID=0;
	private int TOPIC_ID=0;
	private int RESULT_PERCENT=0;
	private int LEVEL=0;
	private int ACHIEVED_POINTS=0;
	private String TOPIC="";
	private int MAX_POINTS=0;
	
	
	protected void onCreate(Bundle savedInstanceState){
		overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
		Bundle extras = getIntent().getExtras();
		super.onCreate(savedInstanceState);		              
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.endgame);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		ImageView titleImg=(ImageView) findViewById(R.id.header);
		titleImg.setOnClickListener(homeButton());
		RESULT_SESSION=extras.getInt("session");
		GAMEID=extras.getInt("gameid");
		TOPIC_ID=extras.getInt("topic");
		LEVEL=extras.getInt("level");
		showResult();
	
	}
	View.OnClickListener homeButton(){
		return new View.OnClickListener() {
	        public void onClick(View v) {
	        	Intent intent = new Intent(getApplicationContext(), MainActivity.class);
	    		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    		startActivity(intent);
	        }
		};
	}
	
	protected void showResult(){
		MySQLiteHelper db=new MySQLiteHelper(this);
		Result result=db.getResult(RESULT_SESSION,GAMEID,TOPIC_ID);
		Topic topic=db.getTopic(TOPIC_ID);
		TOPIC=topic.getTitle();
		TextView resultTitle=(TextView)findViewById(R.id.resultTitle);
		resultTitle.setText(getText(R.string.resultTitle)+" "+TOPIC+" - Level "+LEVEL);
		ACHIEVED_POINTS=result.getAchievedpoints();
		MAX_POINTS=result.getMaxpoints();
		TextView pointsView=(TextView)findViewById(R.id.result);
		pointsView.setText("Erreichte Punkte:\r\n "+ACHIEVED_POINTS+" von "+MAX_POINTS+"");
		TextView percentView=(TextView)findViewById(R.id.percent);
		RESULT_PERCENT=result.getProcent();
		percentView.setText("In Prozent:\r\n"+RESULT_PERCENT+" %");
		
		if(isOnline()){
			new Thread(new Runnable() {
		        public void run() {
		        	uploadResult(ACHIEVED_POINTS,MAX_POINTS);
		        }
		    }).start();
			
		}
		
	}
	
	public void uploadResult(int achievedPoints, int maxPoints){
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String regId=telephonyManager.getDeviceId();
		String anonymousId="";
		byte[] bytesOfregId=null;
		try {
			bytesOfregId = regId.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			MessageDigest digester=MessageDigest.getInstance("MD5");
			byte[] thedigest = digester.digest(bytesOfregId);
			
			 anonymousId = new String(Hex.encodeHex(thedigest));
			
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
		}
		
		
		 try {
	        	HttpPost httppost = new HttpPost("http://itfitness-gcm.denkfabrik-entwicklung.de/web/gcm/upload/");
	        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	        	nameValuePairs.add(new BasicNameValuePair("regId", anonymousId));
	        	nameValuePairs.add(new BasicNameValuePair("gameId", ""+GAMEID+""));
	        	nameValuePairs.add(new BasicNameValuePair("topicId", ""+TOPIC_ID+""));
	        	nameValuePairs.add(new BasicNameValuePair("achievedPoints",""+achievedPoints+""));
	        	nameValuePairs.add(new BasicNameValuePair("maxPoints",""+maxPoints+""));
	        	
	        	
	        	HttpEntity postEntity = new UrlEncodedFormEntity(nameValuePairs);
	        	httppost.setEntity(postEntity);
	        	
	        	 HttpClient httpclient=new DefaultHttpClient();
	            // Execute HTTP Post Request
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity entity = response.getEntity();
	            String result = EntityUtils.toString(entity);
	            Log.d("UPLOADRESULT",""+result);
	            
	            
	        } catch (ClientProtocolException e) {
	            // TODO Auto-generated catch block
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	        }
	}
	
	public void shareIt(View arg){
		
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		sharingIntent.setType("text/plain");
		String shareBody = getString(R.string.subject)+": "+RESULT_PERCENT+"% in "+TOPIC+" -  Level "+LEVEL;
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "IT Fitnes - Ergebnis");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		
		startActivityForResult(Intent.createChooser(sharingIntent, "Share via"),0);
		
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    if (requestCode == 0) {
	         if (resultCode == RESULT_CANCELED) {
	        	 showResult();
	        }
	    }
	}
	
	public void allNew(View arg){
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	public boolean isOnline() {
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
}