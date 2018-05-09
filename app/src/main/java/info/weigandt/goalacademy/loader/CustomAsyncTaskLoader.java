package info.weigandt.goalacademy.loader;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

public class CustomAsyncTaskLoader extends AsyncTaskLoader<Cursor> {

    public CustomAsyncTaskLoader(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    public Cursor loadInBackground() {
        return null;
    }
    @Override
    protected void onStartLoading() {
        //Think of this as AsyncTask onPreExecute() method,you can start your progress bar,and at the end call forceLoad();
        forceLoad();
    }
}
