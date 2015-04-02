package df.idgbusiness.itfitness;

public class numberOfGames{
	int number;
	int gameid;
	
	public numberOfGames(){
		
	}
	
	public numberOfGames(int number, int gameid){
		this.number=number;
		this.gameid=gameid;
	}
	
	public void setNumer(int number){
		this.number=number;
	}
	public int getNumber(){
		return this.number;
	}
	
	public void setGameid(int gameid){
		this.gameid=gameid;
	}
	
	public int getGameid(){
		return this.gameid;
	}
	
	
	
}