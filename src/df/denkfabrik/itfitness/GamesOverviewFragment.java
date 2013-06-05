package df.denkfabrik.itfitness;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

import df.denkfabrik.itfitness.GameListFragement.OnItemSelectedListener;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GamesOverviewFragment extends FragmentActivity implements GameListFragement.OnItemSelectedListener{
	private OnItemSelectedListener listener;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
		super.onCreate(savedInstanceState);		              
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.overview_fragment);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		LinearLayout titleImg=(LinearLayout) findViewById(R.id.headerbutton);
		titleImg.setOnClickListener(homeButton());
		
		
    }

	 
	
	
    // if the wizard generated an onCreateOptionsMenu you can delete
    // it, not needed for this tutorial

  @Override
  public void OnItemSelected(List<numberOfGames> numberOfGames, int topicId) {
    LevelFragment fragment =(LevelFragment) getSupportFragmentManager().findFragmentById(R.id.detailFragment);
        if (fragment != null && fragment.isInLayout()) {
          fragment.setLevels(numberOfGames,topicId);
        } else{
        	Intent intent = new Intent(getApplicationContext(), LevelDetail.class);        		
        		intent.putExtra("topicId", topicId);
        		
        		startActivity(intent);
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
  
  public void doUpdate(){
		
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
	    ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
  

}
