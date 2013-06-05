package df.denkfabrik.itfitness;

import java.util.List;




import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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


public class LevelDetail extends Activity {
	public static final String PREFS_NAME = "MyPrefsFile";
	@Override
	public void onCreate(Bundle savedInstanceState){
		overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
		super.onCreate(savedInstanceState);		              
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.games_levels);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		LinearLayout titleImg=(LinearLayout) findViewById(R.id.headerbutton);
		titleImg.setOnClickListener(homeButton());
		
		
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
		      finish();
		      return;
		    }
		
		Bundle extras = getIntent().getExtras();
		int topicId=extras.getInt("topicId");
		MySQLiteHelper db=new MySQLiteHelper(this);		
		List numberOfGames=db.getNumberOfGames(topicId);
		setLevels(numberOfGames,topicId);
		
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
	public void setLevels(List<numberOfGames> numberOfGames, int topicid){
		
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.levels);
	 	ll.removeAllViews();
		
		int level=1;
		for(numberOfGames numberOfGame : numberOfGames){
    		Button myButton = new Button(this);
			 	myButton.setText("Level "+level);
			 	
			 	 
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
	
}