package df.denkfabrik.itfitness;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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



import android.support.v4.app.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class GameListFragement extends Fragment {
	public HashMap<String,String> newData=new HashMap();
  private OnItemSelectedListener listener;
  public LinearLayout ll;
  public static Context context;
  public ProgressDialog pd;
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
	    
	
    View view = inflater.inflate(R.layout.games_list,container, false);
    
    context = getActivity().getApplication();
    pd =  new ProgressDialog(getActivity());
    
    MySQLiteHelper db = new MySQLiteHelper(context);
    List<Topic> topicList=db.getAllTopics();
    ll = (LinearLayout) view.findViewById(R.id.gamesButtons);
    LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	 for (Topic tp : topicList) {
		 FrameLayout buttonFrame=new FrameLayout(context);
		 
		 FrameLayout.LayoutParams iconParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);	            
		 iconParams.gravity=3;		 
		 iconParams.setMargins(convertDpToPixel(10,context),convertDpToPixel(10,context), 0, 0);
		 LinearLayout icon=new LinearLayout(context);
		 icon.setBackgroundResource(R.drawable.icon_goon);
		 icon.setLayoutParams(iconParams);
		 
		 Button myButton = new Button(context);
		 myButton.setText(tp.getTitle());
		 myButton.setTextColor(getResources().getColor(R.color.black));
		 
		
		 buttonFrame.addView(myButton,lp);
		 buttonFrame.addView(icon,iconParams);
		 ll.addView(buttonFrame, lp);
		 myButton.setOnClickListener(getOnClickStartGame(myButton, tp.getID()));
	 }
	 
	 FrameLayout buttonFrame=new FrameLayout(context);
	 
	 FrameLayout.LayoutParams iconParams2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);	            
	 iconParams2.gravity=3;		 
	 iconParams2.setMargins(convertDpToPixel(10,context),convertDpToPixel(10,context), 0, 0);
	 LinearLayout icon2=new LinearLayout(context);
	 icon2.setBackgroundResource(R.drawable.icon_download);
	 icon2.setLayoutParams(iconParams2); 
	Button updateButton=new Button(context);
	LinearLayout updateButtonWrap=(LinearLayout) view.findViewById(R.id.updateButtonWrap);
	updateButton.setText(getResources().getString(R.string.update));
	updateButton.setTextColor(getResources().getColor(R.color.black));
	buttonFrame.addView(updateButton,lp);
	buttonFrame.addView(icon2,iconParams2);
	
	updateButtonWrap.addView(buttonFrame);
	updateButton.setOnClickListener(triggerUpdate(updateButton));
	
    return view;
  }
  
  public  int convertDpToPixel(float dp, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px =  dp * (metrics.densityDpi / 160f);
	    int pxResult=Math.round(px);
	    return pxResult;
	}
  View.OnClickListener triggerUpdate(final Button button){
	  return new View.OnClickListener() {
	        public void onClick(View v) {
	        	
	        	
	        	pd.setTitle("Loading");
	        	pd.setMessage("Wait while loading...");
	        	pd.show();
	        	// To dismiss the dialog
	        	
	        	doUpdate();
	        	
	        }
	  };
  }
  
  View.OnClickListener getOnClickStartGame(final Button button, final int topicid)  {
	  
		MySQLiteHelper db=new MySQLiteHelper(context);		
		final List numberOfGames=db.getNumberOfGames(topicid);
	    return new View.OnClickListener() {
	        public void onClick(View v) {
	        		        	
	        	
	        	listener.OnItemSelected(numberOfGames,topicid);
	        }
	    };
	}
  
  
  public interface OnItemSelectedListener {
      public void OnItemSelected(List<numberOfGames> numberOfGames, int topicId);
  }
  

  
  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (activity instanceof OnItemSelectedListener) {
      listener = (OnItemSelectedListener) activity;
    } else {
      throw new ClassCastException(activity.toString()
          + " must implemenet MyListFragment.OnItemSelectedListener");
    }
  }
 

  
  public void doUpdate(){
		
		final Handler mHandler = new Handler();
		if(isOnline()){			
			new Thread(){				
				@Override
				public void run(){
					
					newData=getAndWriteData();
					mHandler.post(new Runnable() {
						
						@Override
						public void run(){
							if(newData.get("topicid") != null){
								FrameLayout buttonFrame=new FrameLayout(context);
								 
								 FrameLayout.LayoutParams iconParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);	            
								 iconParams.gravity=3;		 
								 iconParams.setMargins(convertDpToPixel(10,context),convertDpToPixel(10,context), 0, 0);
								 LinearLayout icon=new LinearLayout(context);
								 icon.setBackgroundResource(R.drawable.icon_goon);
								 icon.setLayoutParams(iconParams);
								 
							LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
							Button myButton = new Button(context);
							myButton.setText(newData.get("topictitle"));
							myButton.setTextColor(getResources().getColor(R.color.black));
							buttonFrame.addView(myButton,lp);
							buttonFrame.addView(icon,iconParams);
							ll.addView(buttonFrame, lp);
							myButton.setOnClickListener(getOnClickStartGame(myButton, Integer.parseInt(newData.get("topicid"))));
							}
							pd.dismiss();
						}
					});
				}
		}.start();
		}
	}

	
	
	public HashMap<String,String> getAndWriteData(){
		int lastTopic=1;
		HashMap<String,String> returnData=new HashMap();
		MySQLiteHelper db=new MySQLiteHelper(context);
		lastTopic=db.getLastTopic();
		
		
		
		try{
			HttpPost httppost=new HttpPost("http://itfitness-gcm.denkfabrik-entwicklung.de/web/gcm/update/");
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
					returnData=writeData(json);
	        	}catch(JSONException e){
	        		
	        	}
	        }
	       
		}catch(ClientProtocolException e){
			
		}catch(IOException e){
			
		}
		return returnData;
	}
	
	public HashMap<String,String> writeData(JSONObject jsonDataComplete){
		HashMap<String,String> writtenData=new HashMap();
		JSONObject jsonData=null;
		JSONObject topicData=null;
		MySQLiteHelper db=new MySQLiteHelper(context);
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
				writtenData.put("topicid",""+lastInsertTopic);
				writtenData.put("topictitle",topicData.getString("id"));
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
			
			return writtenData;
	}
	
	public boolean isOnline() {
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
 
	
  
  
} 