package info.weigandt.goalacademy.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.google.gson.Gson;

import info.weigandt.goalacademy.R;
import info.weigandt.goalacademy.activities.MainActivity;
import info.weigandt.goalacademy.classes.Constants;
import timber.log.Timber;

/**
 * Implementation of App Widget functionality. This class is also a broadcast receiver.
 */
public class GoalAcademyWidgetProvider extends AppWidgetProvider {

    private WidgetData mWidgetData;
    private String mSerializedWidgetData;

    /**
     *  Triggers on widget click also!
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     */
    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // TODO get the data from SharedPreferences, if not provided by broadcast to onReceive!
        if (mWidgetData == null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            mSerializedWidgetData = sharedPreferences.getString(Constants.GOAL_ACADEMY_PREFS_WIDGET_DATA, null);
            /*
            if(value != null)
            {
                //Gson gson = new Gson();
                //mWidgetData = gson.fromJson(value, WidgetData.class);
                mSerializedWidgetData = value;
            }
            */
        }

        // TODO .............................................................................

        // #0: Prepare the intent to send to the GoalAcademyWidgetRemoteViewsService
        Intent intent = new Intent(context, GoalAcademyWidgetRemoteViewsService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        // #1: Provide the data needed for the List in the intent
        intent.putExtra(Constants.SERIALIZED_WIDGET_DATA, mSerializedWidgetData);
        // set data is not used here. Use it to provide a data location (uri, file, etc...)
        // intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.goal_adacemy_app_widget);
        views.setRemoteAdapter(R.id.widgetListView, intent);
        //views.setEmptyView(R.id.widget_stack_view, R.id.tv_empty_view);   // TODO add later "day off today"

        appWidgetManager.updateAppWidget(appWidgetId, views);   // TODO is this acutally sending the intent`?

        // TODO also add a view asking to update the data. "Update needed, pls launch Goal Academy / click here"

        //views.setTextViewText(R.id.tv..., widgetText);

        // Creating an intent to launch MainActivity when clicked
        Intent intent2 = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent2, 0);

        // Set click handler to launch pending intent
        views.setOnClickPendingIntent(R.id.ll_widget_goal_academy, pendingIntent);  // TODO add listener to items also later

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * This is triggered each interval set in android:updatePeriodMillis="XXX"
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mSerializedWidgetData = intent.getStringExtra(Constants.SERIALIZED_WIDGET_DATA);
        if (mSerializedWidgetData != null) {
            Gson gson = new Gson();
            mWidgetData = gson.fromJson(mSerializedWidgetData, WidgetData.class);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            sharedPreferences.edit().putString(Constants.GOAL_ACADEMY_PREFS_WIDGET_DATA,
                    mSerializedWidgetData).apply();
        }
        else {
            Timber.w("Intent without SERIALIZED_WIDGET_DATA. Cannot update Widget correctly.");
        }

        // TODO needed? don't think so...
        /*
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = mgr.getAppWidgetIds(new ComponentName(context, BakingAppWidgetProvider.class));
        mgr.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_stack_view);
        */
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

