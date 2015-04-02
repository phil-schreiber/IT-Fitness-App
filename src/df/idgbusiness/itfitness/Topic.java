package df.idgbusiness.itfitness;

public class Topic {
	 
    //private variables
    int id;
    String text;
    String title;
    int release;
 
    // Empty constructor
    public Topic(){
 
    }
    // constructor
    public Topic(int id, String title, String text, int release){
        this.id = id;
        this.text = text;
        this.title =title;
        this.release=release;
    }
    // constructor
    public Topic(String text, String title, int release){
    	
        this.text = text;
        this.title= title;
        this.release=release;
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
    
    public String getTitle(){
        return this.title;
    }
 

    public void setTitle(String title){
        this.title = title;
    }
    
    public int getRelease(){
    	return this.release;    	
    }
    
    public void setRelease(int release){
    	this.release=release;
    }
}