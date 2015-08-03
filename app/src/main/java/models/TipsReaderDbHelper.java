package models;

import models.TipsContract.TipEntry;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TipsReaderDbHelper extends SQLiteOpenHelper {
	
	public static final String DATABASE_NAME = "Tips.db";
	public static final int DATABASE_VERSION = 2;
	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_ENTRIES = 
			"CREATE TABLE " + TipEntry.TABLE_NAME + " (" +
		    TipEntry._ID + " INTEGER PRIMARY KEY," +
		    TipEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
		    TipEntry.COLUMN_NAME_CONTENT + TEXT_TYPE + COMMA_SEP +
		    TipEntry.COLUMN_NAME_AUTHOR + TEXT_TYPE + COMMA_SEP +
		    TipEntry.COLUMN_NAME_IMG + TEXT_TYPE + COMMA_SEP +
		    TipEntry.COLUMN_NAME_DATE + TEXT_TYPE + ");";
	private static final String SQL_DELETE_ENTRIES =
		    "DROP TABLE IF EXISTS " + TipEntry.TABLE_NAME;
	
	public TipsReaderDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES);
		

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
	}

}
