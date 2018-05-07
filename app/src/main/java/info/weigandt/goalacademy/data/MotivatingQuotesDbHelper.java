package info.weigandt.goalacademy.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MotivatingQuotesDbHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "motivatingQuotesDb.db";
    private static final int VERSION = 0;

    // Constructor
    MotivatingQuotesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * Called when the database gets created for the first time
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create motivatingQuotes table
        final String CREATE_TABLE = "CREATE TABLE "  + MotivatingQuotesContract.MotivatingQuotesEntry.TABLE_NAME + " (" +
                MotivatingQuotesContract.MotivatingQuotesEntry._ID                + " INTEGER PRIMARY KEY, " +
                MotivatingQuotesContract.MotivatingQuotesEntry.COLUMN_QUOTE_ID + " TEXT NOT NULL, " +
                MotivatingQuotesContract.MotivatingQuotesEntry.COLUMN_TITLE    + " TEXT NOT NULL, " +
                MotivatingQuotesContract.MotivatingQuotesEntry.COLUMN_QUOTE_OBJECT    + " TEXT NOT NULL);";
        db.execSQL(CREATE_TABLE);
    }

    /**
     * This method discards the old table of data and calls onCreate to recreate a new one
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MotivatingQuotesContract.MotivatingQuotesEntry.TABLE_NAME);
        onCreate(db);
    }

}
