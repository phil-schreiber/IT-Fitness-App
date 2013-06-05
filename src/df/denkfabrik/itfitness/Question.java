package df.denkfabrik.itfitness;
public class Question {
 
    //private variables
    int id;
    String text;
    int mode;
    int answers;
    int gameid;
    int topic;
    int sorting;
    int tstamp;
 
    // Empty constructor
    public Question(){
 
    }
    // constructor
    public Question(int id, String text, int mode, int answers, int gameid){
        this.id = id;
        this.text = text;
        this.mode = mode;
        this.answers = answers;
        this.gameid=gameid;
        
    }
    // constructor
    public Question(String text, int mode, int answers, int gameid, int topic, int sorting, int tstamp){
    	
        this.text = text;
        this.mode = mode;
        this.answers = answers;
        this.gameid = gameid;
        this.topic = topic;
        this.sorting = sorting;
        this.tstamp = tstamp;
    }
 


    public int getID(){
        return this.id;
    }
 

    public void setID(int id){
        this.id = id;
    }
 

    public String getText(){
        return this.text;
    }
 

    public void setText(String text){
        this.text = text;
    }
 
    public int getMode(){
    	return  this.mode;
    }
    
    public void setMode(int mode){
    	this.mode = mode;
    }
    
    public int getAnswers(){
    	return this.answers;
    }
    
    public void setAnswers(int answers){
    	this.answers=answers;
    }
    
    public int getGameid(){
    	return this.gameid;
    }
    
    public void setGameid(int gameid){
    	this.gameid=gameid;
    }
    
    public int getTopic(){
    	return this.topic;
    }
    
    public void setTopic(int topic){
    	this.topic=topic;
    }
    
    public int getSorting(){
    	return this.sorting;
    }
    
    public void setSorting(int sorting){
    	this.sorting=sorting;
    }
    
    public int getTstamp(){ 
    	return this.tstamp;
    }
    
    public void setTstamp(int tstamp){
    	this.tstamp=tstamp;
    }
    
}