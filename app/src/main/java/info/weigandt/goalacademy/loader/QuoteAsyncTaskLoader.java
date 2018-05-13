package info.weigandt.goalacademy.loader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import info.weigandt.goalacademy.data.Quote;

public class QuoteAsyncTaskLoader extends AsyncTaskLoader<Quote> {
    public boolean hasResult = false;

    public QuoteAsyncTaskLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    public Quote loadInBackground() {
        return null;
    }


    @Override
    protected void onStartLoading() {
        //Think of this as AsyncTask onPreExecute() method,you can start your progress bar,and at the end call forceLoad();
        forceLoad();
    }
}
