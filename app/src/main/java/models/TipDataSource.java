package models;

import java.util.ArrayList;

import models.TipsContract.TipEntry;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TipDataSource {
	
	private SQLiteDatabase database;
	private TipsReaderDbHelper dbHelper;
	private String[] allColumns = {TipEntry._ID, TipEntry.COLUMN_NAME_TITLE, 
			TipEntry.COLUMN_NAME_CONTENT, TipEntry.COLUMN_NAME_AUTHOR, TipEntry.COLUMN_NAME_DATE, TipEntry.COLUMN_NAME_IMG};
	
	public TipDataSource(Context context){
		dbHelper = new TipsReaderDbHelper(context);
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

	
	public long insertIntoTable(Tip tip){		
		ContentValues values = new ContentValues();
		values.put(TipEntry._ID, tip.getId());
		values.put(TipEntry.COLUMN_NAME_TITLE, tip.getTitle());
		values.put(TipEntry.COLUMN_NAME_CONTENT, tip.getContent());
		values.put(TipEntry.COLUMN_NAME_AUTHOR, tip.getAuthor());
		values.put(TipEntry.COLUMN_NAME_DATE, tip.getDate());
		values.put(TipEntry.COLUMN_NAME_IMG, tip.getImgUrl());
		
		return database.insert(TipEntry.TABLE_NAME, null, values);
		
	}
	
	public ArrayList<Tip> getAllTips() {
	    ArrayList<Tip> reports = new ArrayList<Tip>();

	    Cursor cursor = database.query(TipEntry.TABLE_NAME,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Tip report = cursorToReport(cursor);
	      reports.add(report);
	      cursor.moveToNext();
	    }
	    
	    cursor.close();
	    return reports;
	  }
	private Tip cursorToReport(Cursor cursor) {
		return new Tip(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5));
	  }
}
