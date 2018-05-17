package info.weigandt.goalacademy.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import info.weigandt.goalacademy.classes.Config;

public class QuotesDbHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = Config.QUOTES_DB_NAME;
    private static final int VERSION = 1;

    // Constructor
    QuotesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * Called when the database gets created for the first time
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create quotes table
        final String CREATE_TABLE = "CREATE TABLE "  + QuotesContract.QuotesEntry.TABLE_NAME + " (" +
                QuotesContract.QuotesEntry._ID                + " INTEGER PRIMARY KEY, " +
                QuotesContract.QuotesEntry.COLUMN_LINK + " TEXT NOT NULL, " +
                QuotesContract.QuotesEntry.COLUMN_TEXT + " TEXT NOT NULL, " +
                QuotesContract.QuotesEntry.COLUMN_AUTHOR + " TEXT NOT NULL);";
        db.execSQL(CREATE_TABLE);
    }

    /**
     * This method discards the old table of data and calls onCreate to recreate a new one
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuotesContract.QuotesEntry.TABLE_NAME);
        onCreate(db);
    }

}
