package info.weigandt.goalacademy.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PullQuoteBroadcastReceiver extends BroadcastReceiver {
    private BroadcastReceiverListener mListener;
    public static final String LOCAL_ACTION =
            "info.weigandt.goalacademy.intent_service.ALL_DONE";
    public interface BroadcastReceiverListener {
        void onBroadcastReceived(String quoteText, String quoteAuthor);
    }

    // FYI: The emtpy constr. is only needed for static receivers defined in the manifest
    public PullQuoteBroadcastReceiver() {
    }

    public PullQuoteBroadcastReceiver(BroadcastReceiverListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String quoteText = intent.getStringExtra(PullQuoteIntentService.INTENT_QUOTE_TEXT);
        String quoteAuthor = intent.getStringExtra(PullQuoteIntentService.INTENT_QUOTE_AUTHOR);
        if (mListener != null) {
            mListener.onBroadcastReceived(quoteText, quoteAuthor);
        }
    }
}
