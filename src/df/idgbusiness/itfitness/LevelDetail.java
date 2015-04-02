package df.idgbusiness.itfitness;

import java.util.List;




import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;


@SuppressLint("NewApi")
public class LevelDetail extends Activity {
	public static final String PREFS_NAME = "MyPrefsFile";
	public static Context context;
	public int sdk;
	@Override
	public void onCreate(Bundle savedInstanceState){
		int sdk = android.os.Build.VERSION.SDK_INT;
		  
		overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
		super.onCreate(savedInstanceState);
		context = getApplication();
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
		String topicTitle = extras.getString("topicTitle");
		BoldCustomTextView ll = (BoldCustomTextView) findViewById(R.id.levelsTopic);
	 	ll.setText(topicTitle);
		
		MySQLiteHelper db=new MySQLiteHelper(this);		
		List numberOfGames=db.getNumberOfGames(topicId);
		setLevels(numberOfGames,topicId,topicTitle);
		
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
	public void setLevels(List<numberOfGames> numberOfGames, int topicid, String topicTitle){
		
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.levels);
	 	ll.removeAllViews();
		
		int level=1;
		for(numberOfGames numberOfGame : numberOfGames){
    		Button myButton = new CustomButtonView(this);
			 	myButton.setText("»  Schwierigkeitsgrad "+level);
			 	 if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				     
				     myButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_bottom));
				 } else {
				     
					 myButton.setBackground(getResources().getDrawable(R.drawable.border_bottom));
				 }
				 
				 myButton.setGravity(3);
				 myButton.setTextSize(20);
				 LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				 
				 buttonParams.gravity=3;
				 
				 
				 				 
				 myButton.setLayoutParams(buttonParams);
			 	 
				 LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				 ll.addView(myButton, lp);
				 myButton.setOnClickListener(getOnClickStartLevel(myButton, level, topicid, numberOfGame.getGameid(),topicTitle));
				 level++;  		
    	}
	}
	
	public  int convertDpToPixel(float dp, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px =  dp * (metrics.densityDpi / 160f);
	    int pxResult=Math.round(px);
	    return pxResult;
	}

	View.OnClickListener getOnClickStartLevel(final Button button, final int level, final int topicid, final int gameid, final String topictitle)  {
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
	      	intent.putExtra("topictitle", topictitle);
	          startActivity(intent);   
	      }
	  };
	}
	
	public void goBack(View arg){
		finish();
	}
}