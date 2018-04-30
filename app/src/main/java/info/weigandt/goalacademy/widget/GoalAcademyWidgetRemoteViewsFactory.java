package info.weigandt.goalacademy.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import info.weigandt.goalacademy.R;

/**
 * RemoteViewsFactory serves the purpose of an adapter in the widget’s context.
 * An adapter is used to connect the collection items
 *  (for example, ListView items or GridView items) with the data set.
 */
public class GoalAcademyWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;

    public GoalAcademyWidgetRemoteViewsFactory(Context applicationContext, Intent intent)
    {
        mContext = applicationContext;
    }

    @Override
    public void onCreate()
    {

    }

    @Override
    public void onDatatSetChanged()
    {

    }

    @Override
    public void onDestroy() {
       // close cursor etc...
    }

    @Override
    public int getCount() {
        return 2; // TODO enter number of list items here
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.collection_widget_list_item);
        rv.setTextViewText(R.id.widgetItemTaskNameLabel, mCursor.getString(1));

        return rv;
    }
    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        //return mCursor.moveToPosition(position) ? mCursor.getLong(0) : position;
        return 1; // TODO enter correct value
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}

