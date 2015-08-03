package models;

import android.provider.BaseColumns;

public final class TipsContract {
	
	public TipsContract(){
		
	}
	
	public static abstract class TipEntry implements BaseColumns{
		public static final String TABLE_NAME = "tips";
//		public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_IMG = "imgUrl";
	}
}
