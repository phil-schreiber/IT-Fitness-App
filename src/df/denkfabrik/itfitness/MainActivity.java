package df.denkfabrik.itfitness;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import java.io.InputStream;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.IntentFilter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;

import android.util.Log;
import static df.denkfabrik.itfitness.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static df.denkfabrik.itfitness.CommonUtilities.EXTRA_MESSAGE;
import static df.denkfabrik.itfitness.CommonUtilities.EXTRA_COUNT;
import static df.denkfabrik.itfitness.GCMIntentService.count;
/*import com.testflightapp.lib.TestFlight;*/

public class MainActivity extends Activity {
	private static final String TAG_SURVEYS = "questions";
	private static final String TAG_TOPIC = "topic";
	private static final String TAG_ID = "surveytitle";
	private File DB_FULL_PATH;
	private static final String SENDER_ID="590463838489";
	
	
    
    
	 static JSONObject jObj = null;
	 Context context;
	 ContextWrapper mContext;
	 
	 TextView lblMessage;
	 LinearLayout lblMessageWrap;
	// Asyntask
	    AsyncTask<Void, Void, Void> mRegisterTask;
	     
	    // Alert dialog manager
	    AlertDialogManager alert = new AlertDialogManager();
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		JSONObject jsonData=null;
		JSONObject topicData=null;
		lblMessage = (TextView) findViewById(R.id.updateCounter);
		lblMessageWrap= (LinearLayout) findViewById(R.id.updateCounterWrapper);
		/*TestFlight.takeOff(getApplication(), "6a41ad1d-91bd-4174-9d18-f9e0f1cd3478");*/
		
        registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));
        
        DB_FULL_PATH =getBaseContext().getDatabasePath("gamesManager.db");
        String outFileName =DB_FULL_PATH.getPath() ;
        
		
		File dbFile = new File(outFileName);
	    
	    	
	    	
	    	
	    	if(! dbFile.exists()){
	    		MySQLiteHelper db = new MySQLiteHelper(this);		
	    		JSONObject jsonDataComplete=getJson();
	    		
	    		 try {
	 	    	    // Getting Array of Questions
	    			 jsonData = jsonDataComplete.getJSONObject(TAG_SURVEYS);
	    			 topicData=jsonDataComplete.getJSONObject(TAG_TOPIC);
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
	    				int topicRelease=1;
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
	    				
	    			}
	    			
	    			
	    		
	    		
	    		/**
	         * CRUD Operations
	         * */
	        // Inserting Contacts
	    		
	        //db.addQuestion(new Question("Runde 1", "12"));
	        
	    	}else{
	    		/**TODO Check for new JSON Documents**/
	    	}
	    	
	    	RegisterWithGCM();
	}
	@Override
	public void onResume(){
		super.onResume();
		lblMessage.setText(""+count);
		if(count==0){
	        lblMessage.setVisibility(View.INVISIBLE);
	        lblMessageWrap.setVisibility(View.INVISIBLE);
			}else{
				lblMessage.setVisibility(View.VISIBLE);
		        lblMessageWrap.setVisibility(View.VISIBLE);
			} 
		
	}
	
	private void RegisterWithGCM()
	{           
		
	    GCMRegistrar.checkDevice(this);
	    GCMRegistrar.checkManifest(this);
	    final String regId = GCMRegistrar.getRegistrationId(this);
	    if (regId.equals("")) {
	      GCMRegistrar.register(this, SENDER_ID); // Note: get the sender id from configuration.
	    } else {
	         
	      Log.v("Registration", "Already registered, regId: " + regId);
	    }
	}
	
	
	protected boolean checkDB(ContextWrapper context, String dbName){
		File dbFile=context.getDatabasePath(dbName);
	    return dbFile.exists();
		
	}
	
	protected  JSONObject getJson(){		
		
		/*JSONObject questions = null;
		AssetManager assetManager  = getAssets();
		 InputStream input;
		 String returnData="";
		 
	     try {
        	   input = assetManager.open("questions.json");

        	          int size = input.available();
        	          byte[] buffer = new byte[size];
        	          input.read(buffer);
        	          input.close();

        	          // byte buffer into a string
        	          returnData = new String(buffer);
        	          
        	          
        	  } catch (IOException e) {
        	   // TODO Auto-generated catch block
        	   e.printStackTrace();
        	  }
	     
	     try{
	    	 
	     jObj = new JSONObject(returnData);
	     Log.d("DREEEECKCKCKCKCKCKCK",""+jObj);
	     }catch(JSONException e){
	    	   e.printStackTrace();
	     }*/
		AssetManager assetManager  = getAssets();
		InputStream input =null;
		JSONObject json = null;
		String returnData="";
		try {
			
		      
			input = assetManager.open("questions.json");

	          int size = input.available();
	          byte[] buffer = new byte[size];
	          input.read(buffer);
	          input.close();
	          

	          // byte buffer into a string
	          returnData = new String(buffer);
	          if(returnData.substring(0,1)!="{"){
	        	  returnData=returnData.substring(1);
	        	  json=new JSONObject(returnData);
	          }else{
	        	  json=new JSONObject(returnData);
	          }
	          
		   
		    }
		    catch (IOException e) {
		      Log.e(getClass().getSimpleName(), "Exception loading file", e);
		    }
		    catch (JSONException e) {
		      Log.e(getClass().getSimpleName(), "Exception parsing file", e);
		    }
	     
	     
	     
		return json;
		
		
	}
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}*/
	
	public void gamesOverview(View arg){
		if(count>0){
			count=0;
			
            lblMessage.setVisibility(View.INVISIBLE);
            lblMessageWrap.setVisibility(View.INVISIBLE);
            GCMIntentService gcm=new GCMIntentService();
            gcm.clearNotification(this);
		}
		
		Intent intent = new Intent(getBaseContext(), GamesOverviewFragment.class);		
		startActivity(intent); 
        
	}
	
	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
	}
	
	public void ResumeGame(View arg){
		final int topicid=1;
		final int level=1;
		final int gameid=1;
		Intent intent = new Intent(getBaseContext(), RunGame.class);
		intent.putExtra("topic",(int)topicid);
    	intent.putExtra("level",(int)level);
    	intent.putExtra("gameid",(int)gameid);	      	      
        startActivity(intent);  
	}
	
	
	
	public void ShowStatistics(View arg){
		Intent intent= new Intent(getBaseContext(), ShowStatistics.class);
		startActivity(intent);
	}
	
	public void AboutGame(View arg){
		Intent intent= new Intent(getBaseContext(), AboutGame.class);
		startActivity(intent);
	}
	
	
	
	/**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            
            
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());
             
            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */
             
            // Showing received message
            if(count>0){
            lblMessage.setText(""+count);
            lblMessage.setVisibility(View.VISIBLE);
            lblMessageWrap.setVisibility(View.VISIBLE);
            }
            Toast.makeText(getApplicationContext(), "Update: " + newMessage, Toast.LENGTH_LONG).show();
             
            // Releasing wake lock
            WakeLocker.release();
        }
    };
    
    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }
    
    

}


