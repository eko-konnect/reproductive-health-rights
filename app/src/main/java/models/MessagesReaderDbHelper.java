package models;

import models.MessagesContract.MessageEntry;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MessagesReaderDbHelper extends SQLiteOpenHelper {
	
	public static final String DATABASE_NAME = "Messages.db";
	public static final int DATABASE_VERSION = 2;
	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_ENTRIES = 
			"CREATE TABLE " + MessageEntry.TABLE_NAME + " (" +
		    MessageEntry._ID + " INTEGER PRIMARY KEY," +
		    MessageEntry.COLUMN_NAME_MESSAGE + TEXT_TYPE + COMMA_SEP +
		    MessageEntry.COLUMN_NAME_ISMINE + " INTEGER," +
		    MessageEntry.COLUMN_NAME_STATUS_OK + " INTEGER," +
		    MessageEntry.COLUMN_NAME_DATE + " INTEGER" + ");";
	private static final String SQL_DELETE_ENTRIES =
		    "DROP TABLE IF EXISTS " + MessageEntry.TABLE_NAME;
	
	public MessagesReaderDbHelper(Context context) {
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
