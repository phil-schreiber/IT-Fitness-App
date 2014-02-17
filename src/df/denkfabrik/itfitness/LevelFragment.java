package df.denkfabrik.itfitness;

import java.util.List;


import android.support.v4.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;


public class LevelFragment extends Fragment {
	public static final String PREFS_NAME = "MyPrefsFile";
	public Context context;
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
		context = getActivity().getApplication();
	    View view = inflater.inflate(R.layout.games_levels, container, false);
	    return view;
	  }

	public void setLevels(List<numberOfGames> numberOfGames, int topicid, String topictitle){
		BoldCustomTextView levelsTopic=(BoldCustomTextView)getView().findViewById(R.id.levelsTopic);
		levelsTopic.setText(topictitle);
		LinearLayout ll = (LinearLayout)getView().findViewById(R.id.levels);
	 	ll.removeAllViews();
		
		int level=1;
		for(numberOfGames numberOfGame : numberOfGames){
    		Button myButton = new CustomButtonView(context);
    			 
			 	myButton.setText("Level "+level);
			 	
			 	 
				 LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				 ll.addView(myButton, lp);
				 myButton.setOnClickListener(getOnClickStartLevel(myButton, level, topicid, numberOfGame.getGameid(),topictitle));
				 level++;  		
    	}
	}
	
	
	View.OnClickListener getOnClickStartLevel(final Button button, final int level, final int topicid, final int gameid, final String topictitle)  {
		  return new View.OnClickListener() {
	        public void onClick(View v) {
	        	SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
	  	      	SharedPreferences.Editor editor = settings.edit();
	  	      	editor.clear();	  	      	
	  	      	editor.commit();
	  	      	
	        	Intent intent = new Intent(context, RunGame.class);
	        	intent.putExtra("topic",topicid);
	        	intent.putExtra("level",level);
	        	intent.putExtra("gameid",gameid);	
	        	intent.putExtra("topictitle", topictitle);
	            startActivity(intent);   
	        }
	    };
	}
}
