package df.denkfabrik.itfitness;

public class Session{
	int id;
	int gameid;
	int topic;
	int crtime;
	int tstamp;
	
	public Session(){
		
	}
	
	public Session(int id, int gameid, int topic, int crtime, int tstamp){
		this.id=id;
		this.gameid=gameid;
		this.topic=topic;
		this.crtime=crtime;
		this.tstamp=tstamp;
	}
	
	public Session(int gameid, int topic, int crtime, int tstamp){
		this.gameid=gameid;
		this.topic=topic;
		this.crtime=crtime;
		this.tstamp=tstamp;
	}
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int id){
		this.id=id;
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
	
	public int getCrtime(){
		return this.crtime;
	}
	
	public void setCrtime(int crtime){
		this.crtime=crtime;
	}
	
	public void setTstamp(int tstamp){
		this.tstamp=tstamp;
	}
	
	public int getTstamp(){
		return this.tstamp;
	}
} 