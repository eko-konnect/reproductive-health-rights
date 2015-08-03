package models;


//Class to model the attributes of each chat message.
public class Message {
	private long id;
	private String message;//content of the message
	private boolean isMine = true;//boolean to determine the sender of the message
	private long date;
	public boolean statusOK = true;
	
	
	public Message(String message, boolean isMine){
		this.message = message;
		this.isMine = isMine;
	}
	
	public Message(long id, String message, boolean isMine, long date) {
//		super();
		this.id = id;
		this.message = message;
		this.isMine = isMine;
		this.date = date;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMessage(){
		return message;
	}
	public void setMessage(String message){
		this.message = message;
	}
	public boolean isMine(){
		return isMine;
	}
	public void setMine(boolean isMine){
		this.isMine = isMine;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}
}
