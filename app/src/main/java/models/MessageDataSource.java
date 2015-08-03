package models;

import java.util.ArrayList;

import models.MessagesContract.MessageEntry;
import models.MessagesContract.MessageEntry;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class MessageDataSource {
	
	private SQLiteDatabase database;
	private MessagesReaderDbHelper dbHelper;
	private String[] allColumns = {MessageEntry._ID, MessageEntry.COLUMN_NAME_MESSAGE, 
			MessageEntry.COLUMN_NAME_ISMINE, MessageEntry.COLUMN_NAME_STATUS_OK, MessageEntry.COLUMN_NAME_DATE};
	
	public MessageDataSource(Context context){
		dbHelper = new MessagesReaderDbHelper(context);
	}
	
	public void open() throws SQLException{
		if(database == null){
			database = dbHelper.getWritableDatabase();
		}
	}
	
	public void close(){
		if (database != null){
			database.close();
		}
	}
	
	public long insertIntoTable(Message message){		
		ContentValues values = new ContentValues();
		values.put(MessageEntry._ID, message.getId());
		values.put(MessageEntry.COLUMN_NAME_MESSAGE, message.getMessage());
		values.put(MessageEntry.COLUMN_NAME_ISMINE, (message.isMine())? 1 : 0);
		values.put(MessageEntry.COLUMN_NAME_STATUS_OK, message.statusOK ? 1 : 0);
		values.put(MessageEntry.COLUMN_NAME_DATE, message.getDate());
		
		return database.insert(MessageEntry.TABLE_NAME, null, values);
		
	}
	
	public ArrayList<Message> getAllMessages() {
	    ArrayList<Message> reports = new ArrayList<Message>();

	    Cursor cursor = database.query(MessageEntry.TABLE_NAME,
	        allColumns, null, null, null, null, MessageEntry.COLUMN_NAME_DATE+" ASC");

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Message report = cursorToReport(cursor);
	      reports.add(report);
	      cursor.moveToNext();
	    }
	    
	    cursor.close();
	    return reports;
	  }
	private Message cursorToReport(Cursor cursor) {
		boolean isMine = false;
		int isM = cursor.getInt(2);
		if (isM == 1)
			isMine = true;
		
	    Message message = new Message(cursor.getInt(0), cursor.getString(1), isMine,
	    		cursor.getInt(3));
	    message.statusOK = true;
	    
	    return message;
	  }
}
