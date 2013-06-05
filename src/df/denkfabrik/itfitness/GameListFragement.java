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



import android.support.v4.app.Fragment;

import android.app.Activity;
import android.content.Context;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class GameListFragement extends Fragment {

  private OnItemSelectedListener listener;
  public Context context;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
	    
	  
    View view = inflater.inflate(R.layout.games_list,container, false);
    
    context = getActivity().getApplication();
    
    MySQLiteHelper db = new MySQLiteHelper(context);
    List<Topic> topicList=db.getAllTopics();
    
	 for (Topic tp : topicList) {
		 Button myButton = new Button(context);
		 myButton.setText(tp.getTitle());
		 myButton.setTextColor(getResources().getColor(R.color.black));
		 LinearLayout ll = (LinearLayout) view.findViewById(R.id.gamesButtons);
		
		 LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		 ll.addView(myButton, lp);
		 myButton.setOnClickListener(getOnClickStartGame(myButton, tp.getID()));
	 }
	
    return view;
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
 

  
  
 
  
  
  
  
} 