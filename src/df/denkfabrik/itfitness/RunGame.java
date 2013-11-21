package df.denkfabrik.itfitness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


@SuppressLint("NewApi")
public class RunGame extends Activity{
	private int CURRENT_QUESTION = 1;
	private int CURRENT_LEVEL=0;
	private int CURRENT_TOPIC=0;
	private int LAST_QUESTION_ID=0;
	private int GAMEID=0;
	private long lastInsertedSession=0;
	private int MAX_QUESTIONS=0;
	public static final String PREFS_NAME = "MyPrefsFile";/*I love copy and paste*/
	public int sdk;
	Map<Integer, Boolean> answerItemsTruthvals = new HashMap<Integer,Boolean>();
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		int sdk = android.os.Build.VERSION.SDK_INT;
		overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
		Bundle extras = getIntent().getExtras();
		super.onCreate(savedInstanceState);		              
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);		
		setContentView(R.layout.game);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		LinearLayout titleImg=(LinearLayout) findViewById(R.id.headerbutton);
		titleImg.setOnClickListener(homeButton());
		
		CURRENT_LEVEL=extras.getInt("level");
		CURRENT_TOPIC=extras.getInt("topic");
		GAMEID=extras.getInt("gameid");
		
		MySQLiteHelper db=new MySQLiteHelper(this);
		
		MAX_QUESTIONS=db.getQuestionMax(CURRENT_TOPIC, GAMEID);
		
		
		
		
		
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
	public void onPause() {
		super.onPause();
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	      SharedPreferences.Editor editor = settings.edit();
	      editor.putInt("currentQuestion", CURRENT_QUESTION);
	      editor.putInt("currentLevel", CURRENT_LEVEL);
	      editor.putInt("gameid", GAMEID);
	      editor.putInt("currentTopic", CURRENT_TOPIC);
	      
	      editor.putLong("session", lastInsertedSession);

	      // Commit the edits!
	      editor.commit();
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	      SharedPreferences.Editor editor = settings.edit();
	      editor.putInt("currentQuestion", CURRENT_QUESTION);
	      editor.putInt("currentLevel", CURRENT_LEVEL);
	      editor.putInt("gameid", GAMEID);
	      editor.putInt("currentTopic", CURRENT_TOPIC);
	      editor.putLong("session", lastInsertedSession);

	      // Commit the edits!
	      editor.commit();
		
	}
	
	
	
	@Override
	public void onResume()
	    {  // After a pause OR at startup
		super.onResume();
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);		
	    CURRENT_QUESTION = settings.getInt("currentQuestion", CURRENT_QUESTION);
	    CURRENT_LEVEL=settings.getInt("currentLevel", CURRENT_LEVEL);
	    GAMEID=settings.getInt("gameid", GAMEID);
	    CURRENT_TOPIC=settings.getInt("currentTopic", CURRENT_TOPIC);
	    lastInsertedSession=settings.getLong("session", lastInsertedSession);
	    
	    if(CURRENT_QUESTION != 1){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Runde unterbrochen");
		alert.setMessage("Wollen Sie die Runde fortsetzen oder neu beginnen?");
		alert.setPositiveButton("Fortsetzen", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			  
			    runGame();
			  }
			});

			alert.setNegativeButton("Neu beginnen", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
				  CURRENT_QUESTION=1;
				  lastInsertedSession=0;
				  runGame();
			  }
			});

			alert.show();
	    }else{
	    	runGame();
	    }
	}
	
	
	
	public void runGame(){
		MySQLiteHelper db = new MySQLiteHelper(this);
		LinearLayout ll = (LinearLayout)findViewById(R.id.qcontainer);
		LinearLayout llq=(LinearLayout)findViewById(R.id.awrapper);
		
		
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		int children=llq.getChildCount();
		if(children ==1){
        	ViewGroup usedradiogroup= (ViewGroup)llq.getChildAt(0);        	
        	for(int i=0; i<usedradiogroup.getChildCount(); i++){
        		RadioButton item=(RadioButton)usedradiogroup.getChildAt(i);
        		boolean checker=item.isChecked();
        		int itemid=item.getId();
        		boolean truthVal=answerItemsTruthvals.get(itemid);
        		if(checker){
        		Reply reply=new Reply();
        		reply.setAnswer(itemid);
        		reply.setQuestion(LAST_QUESTION_ID);
        		reply.setSession((int)lastInsertedSession);
        		reply.setTruthval(truthVal);
        		db.addReply(reply);
        		}
        	}	
        	
        	
        }else if(children>1){
        	for(int i=0; i<children; i++){
        		CheckBox item=(CheckBox)llq.getChildAt(i);
        		boolean checker=item.isChecked();
        		int itemid=item.getId();
        		boolean truthVal=answerItemsTruthvals.get(itemid);
        		if(checker){
        			
        			Reply reply=new Reply();
        			reply.setAnswer(itemid);
        			reply.setQuestion(LAST_QUESTION_ID);
        			reply.setSession((int)lastInsertedSession);
        			reply.setTruthval(truthVal);
        			db.addReply(reply);
        		}
        	}
        }
		
		
		llq.removeAllViews();    	
		llq.invalidate();
		llq.requestLayout();
		/*ll.invalidate();
    	ll.requestLayout();*/
		
    	answerItemsTruthvals.clear();
		
		
		
		Question firstQuestion =db.getQuestion(CURRENT_TOPIC,GAMEID,CURRENT_QUESTION);
		
		
		int gameid=firstQuestion.getGameid();
		if(CURRENT_QUESTION==1){
			LAST_QUESTION_ID=firstQuestion.getID();

			Session session=new Session();
			long tstamp =System.currentTimeMillis();
			long minutesLong = TimeUnit.MILLISECONDS.toMinutes(tstamp);
			int minutes=(int)minutesLong;
			session.setCrtime(minutes);
			session.setGameid(gameid);
			session.setTstamp(0);
			session.setTopic(CURRENT_TOPIC);
			lastInsertedSession=db.addSession(session);
			
			
		}
		if(CURRENT_QUESTION <= MAX_QUESTIONS){
		String qText=firstQuestion.getText();
		
		
				
		TextView tv1=(TextView)findViewById(R.id.qtitle);		
		tv1.setText("Frage: "+CURRENT_QUESTION);
		
		TextView tv2=(TextView)findViewById(R.id.qtext);
		
		tv2.setText(qText);
		/*Animation mAnimation;
		mAnimation = new TranslateAnimation(2,(float)-1.0, 0, 0, 0,0,0,0);
        mAnimation.setDuration(1000);
        tv2.setAnimation(mAnimation);*/

		RadioGroup radiogroup=new RadioGroup(this);

		List<Answer> answers=db.getQuestionAnswers(firstQuestion.getID());
		
		
        
		
		 
		boolean oddeven=false;
        for (Answer answer : answers) {        	
        	if(firstQuestion.getMode()==0){
        	CheckBox ch = new CustomCheckboxView(this);        	
        	ch.setText(answer.getText());
        	ch.setTextColor(getResources().getColor(R.color.black));
        	ch.setId(answer.getID());
        	
        	
        	/*if((answers.size()%2==0) && oddeven){
        		ch.setBackgroundColor(getResources().getColor(R.color.lightgreyblue));
        	}else if((answers.size()%2!=0) && !oddeven){
        		ch.setBackgroundColor(getResources().getColor(R.color.lightgreyblue));
        	}*/
        	
        	 if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			     
			     ch.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_boxes));
			 } else {
			     
				 ch.setBackground(getResources().getDrawable(R.drawable.border_boxes));
			 }
        	 ch.setButtonDrawable(R.drawable.custom_boxes);
        	if(oddeven){
        		oddeven=false;
        	}else{
        		oddeven=true;
        	}
        	answerItemsTruthvals.put(answer.getID(),answer.getTruthval());
            llq.addView(ch,lp);
        	}else{
        	RadioButton ch=new CustomRadioView(this);
        	ch.setOnCheckedChangeListener(new OnCheckedChangeListener()
        	{
        	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        	    {
        	        if ( isChecked )
        	        {
        	        	buttonView.setTextColor(getResources().getColor(R.color.cw));
        	        }else{
        	        	buttonView.setTextColor(getResources().getColor(R.color.black));
        	        }

        	    }
        	});
        	String answerText=answer.getText();
        	ch.setTextColor(getResources().getColor(R.color.black));
        	ch.setText(answerText);  
        	ch.setId(answer.getID());
        	LayoutParams btParams=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        	
        	ch.setLayoutParams(btParams);
        	
        	/*if((answers.size()%2==0) && oddeven){
        		ch.setBackgroundColor(getResources().getColor(R.color.lightgreyblue));
        	}else if((answers.size()%2!=0) && !oddeven){
        		ch.setBackgroundColor(getResources().getColor(R.color.lightgreyblue));
        	}*/
        	if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			     
			     ch.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_boxes));
			 } else {
			     
				 ch.setBackground(getResources().getDrawable(R.drawable.border_boxes));
			 }
        	
        	ch.setButtonDrawable(R.drawable.custom_boxes);
        	if(oddeven){
        		oddeven=false;
        	}else{
        		oddeven=true;
        	}
        	radiogroup.addView(ch,lp);
        	
        	answerItemsTruthvals.put(answer.getID(),answer.getTruthval());
        	
        	}
            
        }
       
        if(firstQuestion.getMode()==1){
        	llq.addView(radiogroup);
        }
        LAST_QUESTION_ID=firstQuestion.getID();
		}else{
			
			
			 llq.removeAllViews();
			 llq.invalidate();
			CURRENT_QUESTION=1;
			endGame((int)lastInsertedSession);
		}
		
	}
	
	
	public void nextQ(View arg){		
		CURRENT_QUESTION=CURRENT_QUESTION+1;
		LinearLayout mainView=(LinearLayout)findViewById(R.id.mainWrapper);
		
		mainView.postInvalidate();
		mainView.requestLayout();
		
		runGame();
	}
	
	private void endGame(int session){
		MySQLiteHelper db=new MySQLiteHelper(this);
		long tstamp =System.currentTimeMillis();
		long minutesLong = TimeUnit.MILLISECONDS.toMinutes(tstamp);
		int minutes=(int)minutesLong;
		db.addSessionEndTime(minutes,session);
		Intent intent = new Intent(getBaseContext(), EndGame.class);
    	
    	intent.putExtra("session",session);
    	intent.putExtra("gameid",GAMEID);
    	intent.putExtra("topic",CURRENT_TOPIC);
    	intent.putExtra("level", CURRENT_LEVEL);
    	
        startActivity(intent);   
	}
	
	 public void goBack(View arg){
			finish();
		}
	
}