package info.weigandt.goalacademy.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import info.weigandt.goalacademy.data.PostRetrofitQuoteCallListener;
import info.weigandt.goalacademy.data.Quote;
import info.weigandt.goalacademy.data.QuotesContract;
import info.weigandt.goalacademy.data.QuotesController;
import timber.log.Timber;

public class PullQuoteIntentService extends IntentService implements PostRetrofitQuoteCallListener {
    public static final String INTENT_QUOTE_TEXT = "intent_quote_text";
    public static final String INTENT_QUOTE_AUTHOR = "intent_quote_author";

    public PullQuoteIntentService() {
        super("PullQuoteIntentService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        // #0 Do the work
        performQuoteLoadingFromApi();
        // Response will be handled in OnPostApiCall (Async loading of quote with Retrofit / OkHttp 3)
        // -> So the service can chill a bit more meanwhile
        // Still it's useful to be in a worker thread for writing to the Content Provider afterwards
    }

    /**
     * Listener-method to be executed from QuotesController after Retrofit request is finished
     */
    @Override
    public void onPostApiCall(Quote quote) {
        if (quote != null) {
            String quoteText = quote.getQuoteText();
            String quoteAuthor = quote.getQuoteAuthor();

            // #1 Send broadcast on completion
            sendBroadcastNow(quoteText, quoteAuthor);

            // #2 Write quote to Content Provider
            writeQuoteToContentProvider(quoteText, quoteAuthor);
        }
        else
        {
            // TODO error handling
        }
    }

    private void writeQuoteToContentProvider(String quoteText, String quoteAuthor) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(QuotesContract.QuotesEntry.COLUMN_LINK, "Unique URL");    // TODO completely remove later from db model also
        contentValues.put(QuotesContract.QuotesEntry.COLUMN_TEXT, quoteText);
        contentValues.put(QuotesContract.QuotesEntry.COLUMN_AUTHOR, quoteAuthor);
        // Insert the content values via a ContentResolver
        Uri uri = getContentResolver().insert(QuotesContract.QuotesEntry.CONTENT_URI, contentValues);
        if (uri != null) {
            Toast.makeText(getBaseContext(), "Added to Content Provider", Toast.LENGTH_LONG).show();
            Timber.e("added to content resolver");
        }
    }

    private void sendBroadcastNow(String quoteText, String quoteAuthor) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(PullQuoteBroadcastReceiver.LOCAL_ACTION);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(INTENT_QUOTE_TEXT, quoteText);
        broadcastIntent.putExtra(INTENT_QUOTE_AUTHOR, quoteAuthor);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        localBroadcastManager.sendBroadcast(broadcastIntent);
    }

    // region API call
    /**
     * Start the async task using Retrofit.
     * "this" is passed as listener to the controller, so that MainActivity
     *  can be called when request is finished
     */
    private void performQuoteLoadingFromApi() {
        // QuotesController will callback with onPostApiCall(Quote quote)
        QuotesController mQuotesController = new QuotesController(this);
        mQuotesController.startLoadingQuote();
    }
    // endregion API call
}
