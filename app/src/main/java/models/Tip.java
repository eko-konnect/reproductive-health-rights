package models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Tip {
	private long id;
	private String title;
	@SerializedName("details")
	private String content;
	private String author;
	private String date;
	@SerializedName("imgUrl")
	private String imgUrl;
		
	public Tip(long id, String title, String content, String author, String date, String imgUrl) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.setAuthor(author);
		this.date = date;
		this.imgUrl = imgUrl;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public static Tip fromJson(JSONObject json) throws JSONException {	
			Tip tip = new Tip(json.getLong("id"),
					json.getString("title"),
					json.getString("details"),
					json.getString("author"),
					json.getString("date"),
					json.getString("imgUrl")); 
			
		return tip;
	}
	
	

}
