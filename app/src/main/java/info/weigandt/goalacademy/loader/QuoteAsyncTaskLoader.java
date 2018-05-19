package info.weigandt.goalacademy.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import info.weigandt.goalacademy.classes.Config;
import info.weigandt.goalacademy.data.QuotesContract;
import timber.log.Timber;

public class QuoteAsyncTaskLoader extends AsyncTaskLoader<Cursor> {
    private Context mContext;
    public Cursor cursorCache;   // keep the cursor here - already retrieved from Content Provider

    public QuoteAsyncTaskLoader(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        // Think of this as AsyncTask onPreExecute() method,
        //  you can start a progress bar and at the end call forceLoad();
        if (cursorCache != null) {
            // To skip loadInBackground call
            deliverResult(cursorCache);
        } else {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(Cursor cursor) {    // Quick delivery if cached
        cursorCache = cursor;
        super.deliverResult(cursor);
    }

    @Override
    public Cursor loadInBackground() {
        // Think of this as AsyncTask doInBackground() method,
        //  here you will actually initiate Network call,
        //  or any work that need to be done on background
        Uri uri = QuotesContract.QuotesEntry.CONTENT_URI;
        try {
            return mContext.getContentResolver().query(
                    uri,
                    null,
                    null,
                    null,
                    Config.LIMIT_QUOTES_STRING);
        } catch (Exception e) {
            Timber.e(Config.QUOTES_ERROR, e.getMessage());
            return null;
        }
    }
}
