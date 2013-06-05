package df.denkfabrik.itfitness;

public class Answer {
	 
    //private variables
    int id;
    String text;
    int parent;
    boolean truthval;
    int gameid;
    int topic;
 
    // Empty constructor
    public Answer(){
 
    }
    // constructor
    public Answer(int id, String text, int parent, boolean truthval, int gameid, int topic){
        this.id = id;
        this.text = text;
        this.parent =parent;
        this.truthval=truthval;
        this.gameid=gameid;
        this.topic=topic;
    }
    // constructor
    public Answer(String text, int parent, boolean truthval, int gameid, int topic){
    	
        this.text = text;
        this.parent=parent;
        this.truthval=truthval;
        this.gameid=gameid;
        this.topic=topic;
    }
 


    public int getID(){
        return this.id;
    }
    
    public void setID(int id){
    	this.id=id;
    }
    
    public String getText(){
    	return this.text;
    }
    
    public void setText(String text){
    	this.text=text;
    }
    
    public int getParent(){
    	return this.parent;
    }
    
    public void setParent(int parent){
    	this.parent=parent;
    }
    
    public boolean getTruthval(){
    	return this.truthval;
    }
    
    public void setTruthval(boolean truthval){
    	this.truthval=truthval;
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
    
}