package df.denkfabrik.itfitness;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

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

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.ViewGroup.LayoutParams;




public class ShowStatistics extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
		super.onCreate(savedInstanceState);		              
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.statistics);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		ImageView titleImg=(ImageView) findViewById(R.id.header);
		titleImg.setOnClickListener(homeButton());
		final Handler mHandler = new Handler();
		if(isOnline()){
			new Thread(new Runnable(){
					@Override
					public void run(){
				    	final HashMap benchmark=getBenchmark();				    	
						mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                            	Iterator<Map.Entry<Integer, HashMap<Integer,String>>> entries =benchmark.entrySet().iterator();
                            	while(entries.hasNext()){
                            		Map.Entry<Integer, HashMap<Integer,String>> entry = entries.next();
                            		int topic=entry.getKey();                            		                                    
                                    Iterator<Map.Entry<Integer, String>> gameEntries=entry.getValue().entrySet().iterator();
                                    while(gameEntries.hasNext()){
                                    	Map.Entry<Integer,String> gameEntry = gameEntries.next();
                                    	String composeId=""+topic+gameEntry.getKey();
                		            	int benchViewId=Integer.parseInt(composeId);
                		            	TextView benchView=(TextView) findViewById(benchViewId);
                		            	if(benchView != null){
                		            	benchView.setText(gameEntry.getValue());
                		            	}
                                    }
                            	}
                            	                             
                            }
                        });
					}
					}).start();
		}
		
		
		LinearLayout statisticsWrap=(LinearLayout) findViewById(R.id.statisticsWrap);
		int paddingLeft=getResources().getDimensionPixelSize(R.dimen.standardPaddingLeft);
		int paddingTop=getResources().getDimensionPixelSize(R.dimen.standardPaddingTop);
		int paddingBottom=getResources().getDimensionPixelSize(R.dimen.standardPaddingBottom);
		int paddingRight=getResources().getDimensionPixelSize(R.dimen.standardPaddingRight);
		int paddingTopSmall=getResources().getDimensionPixelSize(R.dimen.smallPaddingTop);		
		int paddingBottomSmall=getResources().getDimensionPixelSize(R.dimen.smallPaddingBottom);
		int paddingRightSmall=getResources().getDimensionPixelSize(R.dimen.smallPaddingRight);
		
		MySQLiteHelper db=new MySQLiteHelper(this);
		List<Topic> topicList=db.getAllTopics();
		String[] numbers = new String[] {};
		int counter=0;
		
		
		for(Topic topic : topicList){
			
			int topicId=topic.getID();
			
			RelativeLayout topicWrap= new RelativeLayout(this);
			RelativeLayout.LayoutParams v=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			
			topicWrap.setLayoutParams(v);
			
			
			TextView topicTitle=new TextView(this);
			topicTitle.setText(topic.getTitle());
			topicTitle.setBackgroundColor(getResources().getColor(R.color.orange));
			
			topicTitle.setPadding(paddingLeft,paddingTop,paddingRight,paddingBottom);
			topicTitle.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			topicTitle.setTextColor(getResources().getColor(R.color.white));
			topicTitle.setTextSize(20);
			
			String titleId="101"+topicId;
			String legendLeftId="102"+topicId;
			int lastId=Integer.parseInt(legendLeftId);
			
			topicTitle.setId(Integer.parseInt(titleId));
			topicWrap.addView(topicTitle);
			
			RelativeLayout.LayoutParams r=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			r.addRule(RelativeLayout.BELOW,Integer.parseInt(titleId));
			TextView legendLeft=new TextView(this);
			legendLeft.setText(getResources().getString(R.string.legendLeft));
			legendLeft.setTextColor(getResources().getColor(R.color.grey));
			legendLeft.setPadding(paddingLeft,0,0,0);
			legendLeft.setLayoutParams(r);			
			legendLeft.setId(Integer.parseInt(legendLeftId));
			
			
			
			topicWrap.addView(legendLeft);
			
			TextView legendRight=new TextView(this);
			RelativeLayout.LayoutParams s=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			
			s.addRule(RelativeLayout.BELOW,Integer.parseInt(titleId));
			s.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);	
			
			legendRight.setText(getResources().getString(R.string.legendRight));
			legendRight.setTextColor(getResources().getColor(R.color.grey));
			legendRight.setLayoutParams(s);
			topicWrap.addView(legendRight);			
			
			List<numberOfGames> numberOfGames= db.getNumberOfGames(topicId);
			List<Session> sessions=db.getLastSessions(topicId);
			SparseArray<Result> results=new SparseArray<Result>();
			
			for(Session session : sessions){				
				Result result=db.getResult((int)session.getId(),(int)session.getGameid(),(int) session.getTopic());
				results.put(session.getGameid(),result);
				
			}
			int levelCounter=1;
			for (numberOfGames numberOfGame : numberOfGames){
				TextView topicGameResult=new TextView(this);
				if(results.get(numberOfGame.getGameid()) != null){
					
					Result resultItem=results.get(numberOfGame.getGameid());
					topicGameResult.setText("Level "+levelCounter+": "+resultItem.getProcent()+"%");	
				}else{
					topicGameResult.setText("Level "+levelCounter+": noch keins");
				}
				RelativeLayout.LayoutParams p=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				p.addRule(RelativeLayout.BELOW,lastId);
				topicGameResult.setLayoutParams(p);
				
				topicGameResult.setTextColor(getResources().getColor(R.color.grey));
				
				topicGameResult.setPadding(paddingLeft,paddingTopSmall,paddingBottomSmall,paddingRightSmall);
				topicWrap.addView(topicGameResult);
				
				RelativeLayout.LayoutParams q=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);								
				TextView benchmark=new TextView(this);								
				benchmark.setTextColor(getResources().getColor(R.color.grey));
				String composeID= ""+topicId+""+numberOfGame.getGameid();
				int id=Integer.parseInt(composeID);
				benchmark.setId(id);
				q.setMargins(0, 5, 0, 0);
				q.addRule(RelativeLayout.BELOW,lastId);
				q.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				benchmark.setText("nicht verfügbar");
				benchmark.setLayoutParams(q);
				topicWrap.addView(benchmark);
				levelCounter++;
				lastId=id;
			}
			
			statisticsWrap.addView(topicWrap);
			
		}
		
				
		
		
		
	}
	
	public HashMap getBenchmark(){
		HashMap<Integer, HashMap<Integer,String>> benchmark= new HashMap<Integer,HashMap<Integer,String>>();
		 try {
		HttpPost httppost = new HttpPost("http://itfitness-gcm.denkfabrik-entwicklung.de/web/gcm/bench/");
    	
    	 HttpClient httpclient=new DefaultHttpClient();
        // Execute HTTP Post Request
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity);
        
        if (entity != null){
        	try {
				JSONObject json= new JSONObject(result);
				Iterator dings = json.keys();
				
				while( dings.hasNext() ){
		            String topicId = (String)dings.next();
		            int topic=Integer.parseInt(topicId);
		            JSONObject benchmarksJSON= json.getJSONObject(topicId);
		            Iterator games=benchmarksJSON.keys();
		            HashMap<Integer,String> gameBenches=new HashMap<Integer,String>();
		            while(games.hasNext()){		           	 
		            	String gameId=(String)games.next();
		            	int game=Integer.parseInt(gameId);		            	
		            	String bench= benchmarksJSON.getString(gameId);		            	
		            	gameBenches.put(game, bench);
		            }
		            
		            benchmark.put(topic,gameBenches);     
		        }
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
		 } catch (ClientProtocolException e) {
	            // TODO Auto-generated catch block
		 } catch (IOException e) {
	            // TODO Auto-generated catch block
	   }
		 return benchmark;
	}
	
	public boolean isOnline() {
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
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
	
}