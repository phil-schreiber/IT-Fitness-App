package df.denkfabrik.itfitness;

public class Result{
	
	int maxpoints;
	int achievedpoints;
	int procent;
	
	public Result(){
		
	}
	
	public int getMaxpoints(){
		return this.maxpoints;
	}
	
	public int getAchievedpoints(){
		return this.achievedpoints;
	}
	
	public int getProcent(){
		return this.procent;
	}
	
	public void setMaxPoints(int maxpoints){
		this.maxpoints=maxpoints;
	}
	
	public void setAchievedPoints(int achievedpoints){
		this.achievedpoints=achievedpoints;
	}
	
	public void setProcent(int procent){
		this.procent=procent;
	}
}	