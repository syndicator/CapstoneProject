package info.weigandt.goalacademy.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import info.weigandt.goalacademy.R;
import info.weigandt.goalacademy.activities.MainActivity;

import static info.weigandt.goalacademy.widget.GoalAcademyWidgetProvider.sWidgetListItems;

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

    /**
     * Called when notifyDataSetChanged() is triggered on the remote adapter.
     * This allows a RemoteViewsFactory to respond to data changes
     * by updating any internal references.
     * THIS SEEMS TO BE CALLED WHEN notifyAppWidgetViewDataChanged is called in the provider. onCreate gets skipped.
     * So senseless when we have no way to pass in new data :(((((
     * Oh well we use static member of provider but this is so ..phhhh
     */

    @Override
    public void onDataSetChanged()
    {
        // seems to be executed after onCreate - also?
    }

    @Override
    public void onDestroy() {
       // close cursor etc...
    }

    @Override
    public int getCount() {
        if (sWidgetListItems != null) {
            return sWidgetListItems.size();
        }
        else {
            return 0;
        }
    }

    /**
     *
     * @param position
     * @return "RemoteViews" single list item
     */
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(mContext.getPackageName(), R.layout.collection_widget_list_item);
        remoteView.setTextViewText(R.id.tv_widget_goal_name, sWidgetListItems.get(position).getGoalName());
        remoteView.setTextViewText(R.id.tv_widget_info, sWidgetListItems.get(position).getInfoText());

        // Intent on click
        Intent fillInIntent = new Intent(mContext, MainActivity.class);
        fillInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        remoteView.setOnClickFillInIntent(R.id.widgetItemContainer, fillInIntent);

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
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
