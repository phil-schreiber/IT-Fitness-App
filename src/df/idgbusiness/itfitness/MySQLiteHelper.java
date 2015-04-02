package df.idgbusiness.itfitness;

import android.content.Context;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;

public class MySQLiteHelper extends SQLiteOpenHelper{
	 // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;
 
    // Database Name
    private static final String DATABASE_NAME = "gamesManager.db";
 
    // table names
    private static final String TABLE_TOPICS ="topics";
    private static final String TABLE_QUESTIONS ="questions";  
    private static final String TABLE_ANSWERS ="answers";
    private static final String TABLE_REPLYS ="replys";
    private static final String TABLE_SESSIONS ="sessions";
    
    // Topics Table Columns names
    private static final String TOPIC_ID = "id";
    private static final String TOPIC_TITLE = "title";
    private static final String TOPIC_TEXT = "text";
    private static final String TOPIC_RELEASE ="release";

    // Questions Table Columns names
    private static final String QUESTIONS_ID ="id";
    private static final String QUESTIONS_TEXT ="text";
    private static final String QUESTIONS_MODE ="mode";
    private static final String QUESTIONS_ANSWERS ="answers";
    private static final String QUESTIONS_GAMEID="gameid";
    private static final String QUESTIONS_TOPIC="topic";
    private static final String QUESTIONS_SORTING="sorting";
    private static final String QUESTIONS_TSTAMP = "tstamp";
    // Answers Table Columns names
    private static final String ANSWERS_ID = "id";
    private static final String ANSWERS_PARENT = "parent";
    private static final String ANSWERS_TEXT = "text";
    private static final String ANSWERS_TRUTHVAL = "truthval";
    private static final String ANSWERS_GAMEID ="gameid";
    private static final String ANSWERS_TOPIC="topic";
    // Reply Table Columns names
    private static final String REPLYS_ID= "id";
    private static final String REPLYS_ANSWER = "answer";
    private static final String REPLYS_TRUTHVAL ="truthval";
    private static final String REPLYS_QUESTION ="question";
    private static final String REPLYS_SESSION = "session";
    //Session Table Columns name
    private static final String SESSIONS_ID ="id";
    private static final String SESSIONS_GAMEID ="gameid";
    private static final String SESSIONS_TOPIC ="topic";
    private static final String SESSIONS_CRTIME ="crtime";
    private static final String SESSIONS_TSTAMP ="tstamp";
    
    
    private static Context currentContext;
    
    
    
 
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        currentContext=context;
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TOPICS_TABLE = "CREATE TABLE " + TABLE_TOPICS + "("
                + TOPIC_ID + " INTEGER PRIMARY KEY," + TOPIC_TITLE + " TEXT,"
                + TOPIC_TEXT + " TEXT,"+TOPIC_RELEASE+" TEXT)";
        db.execSQL(CREATE_TOPICS_TABLE);
        
        String CREATE_QUESTIONS_TABLE ="CREATE TABLE " +  TABLE_QUESTIONS + "(" 
        		+ QUESTIONS_ID + " INTEGER PRIMARY KEY,"+ QUESTIONS_TEXT + " TEXT,"+ QUESTIONS_MODE + " INTEGER,"
        		+ QUESTIONS_ANSWERS + " INTEGER," + QUESTIONS_GAMEID +" INTEGER," + QUESTIONS_TOPIC + " INTEGER," 
        		+ QUESTIONS_SORTING + " INTEGER," + QUESTIONS_TSTAMP + " INTEGER)";
        db.execSQL(CREATE_QUESTIONS_TABLE);
        
        String CREATE_ANSWERS_TABLE="CREATE TABLE " + TABLE_ANSWERS + "("
        		+ ANSWERS_ID + " INTEGER PRIMARY KEY," + ANSWERS_PARENT + " INTEGER,"
        		+ANSWERS_TEXT + " TEXT,"+ ANSWERS_TRUTHVAL + " INTEGER,"+ANSWERS_GAMEID+" INTEGER, "+ANSWERS_TOPIC+" INTEGER)";
        db.execSQL(CREATE_ANSWERS_TABLE);
        
        String CREATE_REPLYS_TABLE="CREATE TABLE " + TABLE_REPLYS + "("
        		+ REPLYS_ID + " INTEGER PRIMARY KEY," + REPLYS_ANSWER + " INTEGER," + REPLYS_TRUTHVAL + " INTEGER,"
        		+ REPLYS_QUESTION + " INTEGER," + REPLYS_SESSION + " INTEGER)";
        db.execSQL(CREATE_REPLYS_TABLE);
        
        String CREATE_SESSIONS_TABLE = "CREATE TABLE " + TABLE_SESSIONS + "("
        		+ SESSIONS_ID + " INTEGER PRIMARY KEY," + SESSIONS_GAMEID + " INTEGER,"+SESSIONS_TOPIC+" INTEGER ," + SESSIONS_CRTIME + " INTEGER,"
        		+ SESSIONS_TSTAMP + " INTEGER)";
        db.execSQL(CREATE_SESSIONS_TABLE);
        
        
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPICS);
 
        // Create tables again
        onCreate(db);
    }
    
 // Adding new Topic
    public long addTopic(Topic topic) {
    	long lastInsertId=0;
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	values.put(TOPIC_TITLE, topic.getTitle());     
    	values.put(TOPIC_TEXT, topic.getText());
    	values.put(TOPIC_RELEASE, topic.getRelease());
    	
    	// Inserting Row
    	lastInsertId=db.insert(TABLE_TOPICS, null, values);
    	db.close(); // Closing database connection
    	return lastInsertId;
    }
    // Adding new Question
public long addQuestion(Question question) {
	long lastInsertId=0;
    SQLiteDatabase db = this.getWritableDatabase();
 
    ContentValues values = new ContentValues();
    values.put(QUESTIONS_TEXT, question.getText());     
    values.put(QUESTIONS_MODE, question.getMode());    
    values.put(QUESTIONS_ANSWERS, question.getAnswers());
    values.put(QUESTIONS_GAMEID, question.getGameid());
    values.put(QUESTIONS_TOPIC, question.getTopic());
    values.put(QUESTIONS_SORTING, question.getSorting());
    values.put(QUESTIONS_TSTAMP, question.getTstamp());
    
 
    // Inserting Row
    lastInsertId=db.insert(TABLE_QUESTIONS, null, values);
    db.close(); // Closing database connection
    return lastInsertId;
}
//Adding new Answer
public void addAnswer(Answer answer){
	SQLiteDatabase db=this.getWritableDatabase();
	ContentValues values= new ContentValues();
	values.put(ANSWERS_PARENT, answer.getParent());
	values.put(ANSWERS_TRUTHVAL, answer.getTruthval());
	values.put(ANSWERS_TEXT, answer.getText());
	
	db.insert(TABLE_ANSWERS, null, values);
	db.close();
	
}

public void addReply(Reply reply){
	SQLiteDatabase db=this.getWritableDatabase();
	ContentValues values= new ContentValues();
	
	values.put(REPLYS_ANSWER, reply.getAnswer());
	values.put(REPLYS_TRUTHVAL, reply.getTruthval());
	values.put(REPLYS_QUESTION, reply.getQuestion());
	values.put(REPLYS_SESSION, reply.getSession());
	
	db.insert(TABLE_REPLYS, null, values);
	db.close();
}

public long addSession(Session session){
	long lastInsertId=0;
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues values= new ContentValues();
	
	values.put(SESSIONS_CRTIME, session.getCrtime());
	values.put(SESSIONS_GAMEID, session.getGameid());
	values.put(SESSIONS_TSTAMP, session.getTstamp());
	values.put(SESSIONS_TOPIC, session.getTopic());
	 lastInsertId=db.insert(TABLE_SESSIONS, null, values);
	db.close();
	return lastInsertId;
}

public void addSessionEndTime(int endtime, int session){
	
	SQLiteDatabase db = this.getWritableDatabase();
	String strFilter = "id=" + session;
	ContentValues args = new ContentValues();
	args.put(SESSIONS_TSTAMP, endtime);
	db.update(TABLE_SESSIONS, args, strFilter, null);

	db.close();
	
}


public void addAnswersBulk(String insertQuery){
	SQLiteDatabase db = this.getWritableDatabase();
	db.execSQL(insertQuery);
	db.close();
}

public int getQuestionMax(int topic, int gameid){
	int maxQuestions=0;
	String selectQuery="SELECT count(*) FROM "+TABLE_QUESTIONS+" WHERE topic=? AND  gameid=?";
	SQLiteDatabase db=this.getWritableDatabase();
	Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(topic),String.valueOf(gameid)});
	if(cursor.moveToFirst()){
		do{
			maxQuestions=Integer.parseInt(cursor.getString(0));

		}while(cursor.moveToNext());
	}
	db.close();
	
	
	return maxQuestions;
}
public Question getQuestion(int topic,int gameid, int sorting){
	Question question=new Question();
	SQLiteDatabase db=this.getWritableDatabase();
	Cursor cursor = db.query(TABLE_QUESTIONS, new String[] { QUESTIONS_ID, QUESTIONS_TEXT, QUESTIONS_MODE, QUESTIONS_ANSWERS, QUESTIONS_GAMEID}, "topic="+topic+" AND gameid="+gameid+" AND sorting="+sorting, null, null, null, null, null);
    if (cursor.moveToFirst() && cursor.getCount() >= 1){
    	
        
        question = new Question(Integer.parseInt(cursor.getString(0)),cursor.getString(1), Integer.parseInt(cursor.getString(2)),Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)));
    	
    }
    
    db.close();
	return question;
}

public Topic getTopic(int topicid){
	Topic topic=new Topic();
	SQLiteDatabase db=this.getWritableDatabase();
	Cursor cursor = db.query(TABLE_TOPICS, new String[] { TOPIC_ID , TOPIC_TITLE , TOPIC_TEXT, TOPIC_RELEASE}, "id="+topicid+"", null, null, null, null, null);
    if (cursor.moveToFirst() && cursor.getCount() >= 1){
    	topic = new Topic(Integer.parseInt(cursor.getString(0)),cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)));
    }
    
    db.close();
	return topic;
}



public Result getResult(int session, int gameid, int topic){
	
	Result result=new Result();
	SQLiteDatabase db=this.getWritableDatabase();
	String selectQuery="SELECT question, COUNT(id),SUM(truthval) FROM "+TABLE_REPLYS+" WHERE session=? GROUP BY question";
	Cursor cursor=db.rawQuery(selectQuery, new String[]{String.valueOf(session)});
	int truthCounter=0;
	int max=1;
	if(cursor.moveToFirst()){
		do {			
		
			int qid=Integer.parseInt(cursor.getString(0));
			int answercount=Integer.parseInt(cursor.getString(1));
			int sumtruth=Integer.parseInt(cursor.getString(2));
			
			if(answercount <=sumtruth){
				truthCounter=truthCounter+sumtruth;
			}
		}while(cursor.moveToNext());
		
		result.setAchievedPoints(truthCounter);	
	}
	
	String selectQueryMax="SELECT SUM(truthval) FROM "+TABLE_ANSWERS+" WHERE gameid=? AND topic=?";
	
	cursor=db.rawQuery(selectQueryMax,new String[]{String.valueOf(gameid),String.valueOf(topic)});
	
	if(cursor.moveToFirst()){		
		do{
			if(cursor.getString(0) != null){;		
			max=Integer.parseInt(cursor.getString(0));
			}else{
			max=0;				
			}
			
		}while(cursor.moveToNext());

		result.setMaxPoints(max);
	}
	db.close();	
	if(max!=0){
	result.setProcent(Math.round((truthCounter*100)/max));
	}else{
		result.setProcent(0);
	}
	
	return result;
}
public List<Answer> getQuestionAnswers(int questionid){
	List<Answer> answerList = new ArrayList<Answer>();
	String selectQuery="SELECT * FROM "+TABLE_ANSWERS+" WHERE parent=?";
	SQLiteDatabase db=this.getWritableDatabase();
	Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(questionid)});
	if(cursor.moveToFirst()){
		do{
			Answer answer=new Answer();
			answer.setID(Integer.parseInt(cursor.getString(0)));
			answer.setText(cursor.getString(2));
			if(Integer.parseInt(cursor.getString(3))==0){
				answer.setTruthval(false);	
			}else if(Integer.parseInt(cursor.getString(3))==1){
				answer.setTruthval(true);
			}
			
			answerList.add(answer);
		}while(cursor.moveToNext());
	}
	db.close();
	return answerList;
}

public List<Topic> getAllTopics(){
	List<Topic> topicList=new ArrayList<Topic>();
	String selectQuery="SELECT * FROM "+TABLE_TOPICS;
	SQLiteDatabase db=this.getWritableDatabase();
	Cursor cursor=db.rawQuery(selectQuery,null);
	if(cursor.moveToFirst()){
		do{
			Topic topic = new Topic();
            topic.setID(Integer.parseInt(cursor.getString(0)));
            topic.setTitle(cursor.getString(1));
            topic.setText(cursor.getString(2));
            // Adding topic to list
            topicList.add(topic);
			
		}while(cursor.moveToNext());
	}
	db.close();
	return topicList;
}

public int getLastTopic(){
	int lastTopic=1;
	String selectQuery="SELECT MAX(release) FROM "+TABLE_TOPICS;
	SQLiteDatabase db=this.getWritableDatabase();
	Cursor cursor=db.rawQuery(selectQuery,null);
	if(cursor.moveToFirst()){
		do{
			
			lastTopic=Integer.parseInt(cursor.getString(0));
            
			
		}while(cursor.moveToNext());
	}
	db.close();
	return lastTopic;
}


public List<numberOfGames> getNumberOfGames(int topicid){
	List<numberOfGames> numberOfGames=new ArrayList<numberOfGames>();
	String selectQuery="SELECT count(gameid), gameid FROM "+TABLE_QUESTIONS+" WHERE topic=? GROUP BY gameid";
	SQLiteDatabase db=this.getWritableDatabase();
	Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(topicid)});
	if(cursor.moveToFirst()){
		do{
			numberOfGames numberOfGame=new numberOfGames(Integer.parseInt(cursor.getString(0)),Integer.parseInt(cursor.getString(1)));
			numberOfGames.add(numberOfGame);

		}while(cursor.moveToNext());
	}
	db.close();
	return numberOfGames;
}

public List<Session> getLastSessions(int topicid){
	List<Session> sessions=new ArrayList<Session>();
	String selectQuery="SELECT * FROM "+TABLE_SESSIONS+ " WHERE tstamp <> 0 AND topic=? GROUP BY gameid ORDER BY topic, gameid, tstamp";
	SQLiteDatabase db=this.getWritableDatabase();
	Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(topicid)});
	if(cursor.moveToFirst()){
		do{
			Session session=new Session(Integer.parseInt(cursor.getString(0)),Integer.parseInt(cursor.getString(1)),Integer.parseInt(cursor.getString(2)),Integer.parseInt(cursor.getString(3)),Integer.parseInt(cursor.getString(4)));
			sessions.add(session);
		}while(cursor.moveToNext());
	}
	db.close();
	return sessions;
}

/**
 * Check if the database exist
 * 
 * @return true if it exists, false if it doesn't
 */
public boolean checkDataBase() {
	File DB_FULL_PATH =currentContext.getDatabasePath("gamesManager.db");
     String outFileName =DB_FULL_PATH.getPath() ;
    SQLiteDatabase checkDB = null;
    try {
        checkDB = SQLiteDatabase.openDatabase(outFileName, null,
                SQLiteDatabase.OPEN_READONLY);
        checkDB.close();
    } catch (SQLiteException e) {
        // database doesn't exist yet.
    
    }
    return checkDB != null ? true : false;
}
}
