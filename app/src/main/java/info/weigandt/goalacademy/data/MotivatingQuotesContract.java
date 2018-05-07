package info.weigandt.goalacademy.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MotivatingQuotesContract {
    public static final String AUTHORITY = "info.weigandt.goalacademy";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOTIVATING_QUOTES = "motivating_quotes";

    public static final class MotivatingQuotesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOTIVATING_QUOTES).build();
        // Task table name
        public static final String TABLE_NAME = "motivating_quotes";

        // Column names
        public static final String COLUMN_QUOTE_ID = "quote_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_QUOTE_OBJECT = "quote_object";
    }
}
