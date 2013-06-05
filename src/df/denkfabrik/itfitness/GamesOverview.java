package df.denkfabrik.itfitness;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Hex;
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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.telephony.TelephonyManager;
import android.util.Log;


public class GamesOverview extends Activity {
	public static final String PREFS_NAME = "MyPrefsFile";/*I love copy and paste*/
	public LinearLayout ll;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
		super.onCreate(savedInstanceState);		              
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.game_menu);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		LinearLayout titleImg=(LinearLayout) findViewById(R.id.headerbutton);
		titleImg.setOnClickListener(homeButton());
		
		MySQLiteHelper db = new MySQLiteHelper(this);
		List<Topic> topicList=db.getAllTopics();
		 for (Topic tp : topicList) {
			 Button myButton = new Button(this);
			 myButton.setText(tp.getTitle());

			 ll = (LinearLayout)findViewById(R.id.Buttons);
			 LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			 ll.addView(myButton, lp);
			 myButton.setOnClickListener(getOnClickStartGame(myButton, tp.getID()));
		 }
	}
	
	
	
	View.OnClickListener homeButton(){
		return new View.OnClickListener() {
	        public void onClick(View v) {
	        	Intent intent = new Intent(getBaseContext(), MainActivity.class);
	    		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    		startActivity(intent);
	        }
		};
	}
	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
	}
	View.OnClickListener getOnClickStartGame(final Button button, final int topicid)  {
		MySQLiteHelper db=new MySQLiteHelper(this);		
		final List numberOfGames=db.getNumberOfGames(topicid);
	    return new View.OnClickListener() {
	        public void onClick(View v) {
	        		        	
	        	
	        	createLevelButtons(button,numberOfGames,topicid);
	        }
	    };
	}
	public void createLevelButtons(final Button button, List<numberOfGames> numberOfGames, final int topicid){
		TextView title=(TextView)findViewById(R.id.topicTitle);
		title.setText(getText(R.string.levels));
		button.setVisibility(View.GONE);
		int level=1;
		for(numberOfGames numberOfGame : numberOfGames){
    		Button myButton = new Button(this);
			 	myButton.setText("Level "+level);
			 	LinearLayout ll = (LinearLayout)findViewById(R.id.Buttons);
				 LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				 ll.addView(myButton, lp);
				 myButton.setOnClickListener(getOnClickStartLevel(myButton, level, topicid, numberOfGame.getGameid()));
				 level++;  		
    	}
	}
	
	View.OnClickListener getOnClickStartLevel(final Button button, final int level, final int topicid, final int gameid)  {
		  return new View.OnClickListener() {
	        public void onClick(View v) {
	        	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	  	      	SharedPreferences.Editor editor = settings.edit();
	  	      	editor.clear();	  	      	
	  	      	editor.commit();
	  	      	
	        	Intent intent = new Intent(getBaseContext(), RunGame.class);
	        	intent.putExtra("topic",topicid);
	        	intent.putExtra("level",level);
	        	intent.putExtra("gameid",gameid);	        	
	            startActivity(intent);   
	        }
	    };
	}
	
	public void doUpdate(View view){
		
		final Handler mHandler = new Handler();
		if(isOnline()){			
			new Thread(){				
				@Override
				public void run(){
					
					getAndWriteData();
					mHandler.post(new Runnable() {
						@Override
						public void run(){
							
							
						}
					});
				}
		}.start();
		}
		
	}
	
	
	public void getAndWriteData(){
		int lastTopic=1;
		MySQLiteHelper db=new MySQLiteHelper(this);
		lastTopic=db.getLastTopic();
		
		
		
		try{
			HttpPost httppost=new HttpPost("http://itfitness-gcm.denkfabrik-entwicklung.de/web/app_dev.php/gcm/update/");
			HttpClient httpclient=new DefaultHttpClient();
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    	nameValuePairs.add(new BasicNameValuePair("topic", ""+lastTopic));
	    	
	    	HttpEntity postEntity = new UrlEncodedFormEntity(nameValuePairs);
        	httppost.setEntity(postEntity);
	    	HttpResponse response = httpclient.execute(httppost);        	
	        HttpEntity entity = response.getEntity();
	        String result = EntityUtils.toString(entity);
	        
	        if (entity != null){
	        	try {
					JSONObject json= new JSONObject(result);
					 writeData(json);
	        	}catch(JSONException e){
	        		
	        	}
	        }
	       
		}catch(ClientProtocolException e){
			
		}catch(IOException e){
			
		}
		
	}
	
	public void writeData(JSONObject jsonDataComplete){
		JSONObject jsonData=null;
		JSONObject topicData=null;
		MySQLiteHelper db=new MySQLiteHelper(this);
		int topicRelease=db.getLastTopic();
		topicRelease++;
		 try {
    	    // Getting Array of Questions
			 jsonData = jsonDataComplete.getJSONObject("questions");
			 topicData=jsonDataComplete.getJSONObject("topic");
    	} catch (JSONException e) {
    	    e.printStackTrace();
    	}
		int curId=0;
		int topicId=0;
		
			try{
				String answerInsertString="INSERT INTO answers SELECT ";		 		
				long lastInsertId=0;
				long lastInsertTopicLong=0;
				int lastInsertTopic=0;
				
				lastInsertTopicLong=db.addTopic(new Topic(topicData.getString("text"),topicData.getString("id"),topicRelease));
				if (lastInsertTopicLong < 0 || lastInsertTopicLong > 9999999) {
			        throw new IllegalArgumentException
			            (lastInsertTopicLong + " cannot be cast to int without changing its value.");
			    }else{
			    	lastInsertTopic=(int)lastInsertTopicLong;
			    }
				for (int i=0; i<jsonData.length(); i++){
				JSONObject sth=jsonData.getJSONObject(""+i+"");
				Iterator qIterator=sth.keys();
				
				
				while (qIterator.hasNext()){
					 String key = qIterator.next().toString();
			         JSONObject j = sth.getJSONObject(key);
			         JSONObject answers=j.getJSONObject("answers");
			         	        
			         
					if(curId != j.getInt("qid")){
						String qText=j.getString("qtext");
						int qMode=j.getInt("qmode");
						int qAnswers=j.getInt("qanswers");
						int gameid=j.getInt("gameid");
						int topic=lastInsertTopic;
						int sorting=j.getInt("qsort");
						long tstamp =System.currentTimeMillis();
						long minutesLong = TimeUnit.MILLISECONDS.toMinutes(tstamp);
						int minutes=(int)minutesLong;
						curId=j.getInt("qid");

						lastInsertId=db.addQuestion(new Question(qText,qMode,qAnswers,gameid,topic,sorting,minutes));
						
						 if (lastInsertId < 0 || lastInsertId > 9999999) {
						        throw new IllegalArgumentException
						            (lastInsertId + " cannot be cast to int without changing its value.");
						    }else{
						    	lastInsertId=(int)lastInsertId;
						    }
						     
					}
					Iterator aIterator=answers.keys();
					
					while(aIterator.hasNext()){
						String akey = aIterator.next().toString();
						JSONObject answer=answers.getJSONObject(akey);
   					String atext=answer.getString("atext");
						int truthvalRaw=answer.getInt("truthval");
						
						int gameid=j.getInt("gameid");
						int topic=lastInsertTopic;
						int aparent=answer.getInt("aparentid");
						answerInsertString =answerInsertString +" null AS id,"+lastInsertId+" AS parent,'"+ atext+"' AS text,'"+ truthvalRaw+"' AS truthval,"+gameid+" AS gameid,"+topic+" AS topic UNION SELECT";
						/*db.addAnswer(new Answer(atext,aparent,truthval));*/
					}
					
				}
				
				}

				
				answerInsertString=answerInsertString.substring(0, answerInsertString.length() - 12);	    				
				db.addAnswersBulk(answerInsertString);
				
			}catch(JSONException e){
				Log.d("Shite"," "+e);
			}	
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