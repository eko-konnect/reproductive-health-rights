package models;

import android.provider.BaseColumns;

public final class MessagesContract {
	
	public MessagesContract(){
		
	}
	
	public static abstract class MessageEntry implements BaseColumns{
		public static final String TABLE_NAME = "messages";
//		public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_MESSAGE = "message";
        public static final String COLUMN_NAME_ISMINE = "isMine";
        public static final String COLUMN_NAME_STATUS_OK = "statusOK";
        public static final String COLUMN_NAME_DATE = "date";
	}
}
