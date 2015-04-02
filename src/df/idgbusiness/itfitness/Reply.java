package df.idgbusiness.itfitness;

public class Reply{
	int id;
	int answer;
	boolean truthval;
	int question;
	int session;
	
	public Reply(){
		
	}
	
	public Reply(int id, int answer, boolean truthval, int question, int session){
		this.id=id;
		this.answer=answer;
		this.truthval=truthval;
		this.question=question;
		this.session=session;
	}
	
	public Reply(int answer, boolean truthval, int question, int session){
		this.answer=answer;
		this.truthval=truthval;
		this.question=question;
		this.session=session;
	}
	
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int id){
		this.id=id;
	}
	
	public int getAnswer(){
		return this.answer;
	}
	
	public void setAnswer(int answer){
		this.answer=answer;
	}
	
	public boolean getTruthval(){
		return this.truthval;
	}
	
	public void setTruthval(boolean truthval){
		this.truthval=truthval;
	}
	
	public int getQuestion(){
		return this.question;
	}
	
	public void setQuestion(int question){
		this.question=question;
	}
	
	public int getSession(){
		return this.session;
	}
	
	public void setSession(int session){
		this.session=session;
	}
	
}