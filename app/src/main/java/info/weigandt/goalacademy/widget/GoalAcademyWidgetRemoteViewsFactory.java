package info.weigandt.goalacademy.widget;

import android.content.Context;
import android.content.Intent;
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
    // TODO initial loading of data here? also when triggered..?
    @Override
    public void onDataSetChanged()
    {
        // iterate over goals
        // if there is an entry for the current WEEK, check further:
        // is it scheduled for today? (week days set)
        // when numbers scheduled, is number reached already? if not, display it!
        // if an event is passed for today, do not display it.


    }

    @Override
    public void onDestroy() {
       // close cursor etc...
    }

    @Override
    public int getCount() {
        return 2; // TODO enter number of list items here
    }

    /**
     *
     * @param position
     * @return "RemoteViews" single list item
     */
    @Override
    public RemoteViews getViewAt(int position) {



        RemoteViews remoteView = new RemoteViews(mContext.getPackageName(), R.layout.collection_widget_list_item);
        remoteView.setTextViewText(R.id.tv_widget_goal_name, "TEST");


        // TODO retrieve data here. e.g. "goalList.getString(1))" - from Firebase or cursor or what?
        return remoteView;
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
