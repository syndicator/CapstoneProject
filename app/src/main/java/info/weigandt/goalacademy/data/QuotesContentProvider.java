package info.weigandt.goalacademy.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import timber.log.Timber;

import static info.weigandt.goalacademy.data.QuotesContract.QuotesEntry.COLUMN_LINK;
import static info.weigandt.goalacademy.data.QuotesContract.QuotesEntry.TABLE_NAME;

public class QuotesContentProvider extends ContentProvider {

    private QuotesDbHelper mQuotesDbHelper;
    private static final int QUOTES = 100;
    private static final int QUOTE_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Define a static buildUriMatcher method that associates URI's with their int match

    /**
     * Initialize a new matcher object without any matches,
     * then use .addURI(String authority, String path, int match) to add matches
     */
    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(QuotesContract.AUTHORITY, QuotesContract.PATH_QUOTES, QUOTES);
        uriMatcher.addURI(QuotesContract.AUTHORITY, QuotesContract.PATH_QUOTES + "/#", QUOTE_WITH_ID);
        return uriMatcher;
    }

    /**
     * Initialized the Provider
     */
    @Override
    public boolean onCreate() {
        Context context = getContext();
        mQuotesDbHelper = new QuotesDbHelper(context);
        return true;
    }

    /**
     * Source: ud851
     */
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mQuotesDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case QUOTES:
                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(QuotesContract.QuotesEntry.CONTENT_URI, id);
                    Timber.e("returnUri is: " + returnUri.toString());
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    /**
     * Source: ud851
     */
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mQuotesDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case QUOTES:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case QUOTE_WITH_ID:
                // Get the ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                String mSelection = COLUMN_LINK + "=?";
                String[] mSelectionArgs = new String[]{String.valueOf(id)};
                retCursor = db.query(TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /**
     * see ud851
     */
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mQuotesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted items
        int quotesDeleted; // Starts with 0

        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case QUOTE_WITH_ID:
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                quotesDeleted = db.delete(TABLE_NAME, COLUMN_LINK + "=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (quotesDeleted != 0) {
            // A favorite was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of items deleted
        return quotesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(@NonNull Uri uri) {

        throw new UnsupportedOperationException("Not yet implemented");
    }
}