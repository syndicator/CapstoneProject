package info.weigandt.goalacademy.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class QuotesContract {
    public static final String AUTHORITY = "info.weigandt.goalacademy";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_QUOTES = "quotes";

    public static final class QuotesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_QUOTES).build();
        // Task table name
        public static final String TABLE_NAME = "quotes";

        // Column names
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_AUTHOR = "author";
    }
}
