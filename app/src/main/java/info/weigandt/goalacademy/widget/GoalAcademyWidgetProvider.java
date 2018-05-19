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

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

import info.weigandt.goalacademy.BuildConfig;
import info.weigandt.goalacademy.R;
import info.weigandt.goalacademy.activities.MainActivity;
import info.weigandt.goalacademy.classes.Config;
import info.weigandt.goalacademy.classes.Constants;
import info.weigandt.goalacademy.classes.GoalHelper;
import timber.log.Timber;

/**
 * Implementation of App Widget functionality. This class is also a broadcast receiver.
 */
public class GoalAcademyWidgetProvider extends AppWidgetProvider {

    private String mSerializedWidgetData;
    public static ArrayList<WidgetListItem> sWidgetListItems;
    private WidgetData mWidgetData;

    /**
     * The logic to handle the Widget update.
     *  Triggers on widget click also! What????
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     */
    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        sWidgetListItems = new ArrayList<>();
        Gson gson = new Gson();

        boolean isOutdated = false;
        // getting the data from SharedPreferences, if not provided by broadcast to onReceive!
        if (mSerializedWidgetData == null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            try {
                mSerializedWidgetData = sharedPreferences.getString(Constants.GOAL_ACADEMY_PREFS_WIDGET_DATA, null);
                mWidgetData = gson.fromJson(mSerializedWidgetData, WidgetData.class);
            } catch (Exception e) {
                Timber.e(Config.ERROR_READING_SP, e.toString());
            }
            // Check if outdated
            if (mWidgetData != null && mWidgetData.expiryDate != null)
            {
                LocalDate expiryDate = GoalHelper.convertFromIsoDate(mWidgetData.expiryDate);
                if (LocalDate.now().isAfter(expiryDate)) {
                    isOutdated = true;
                }
            }
        }
        else
        {
            mWidgetData = gson.fromJson(mSerializedWidgetData, WidgetData.class);
        }
        if (!isOutdated) {
            // prepare the data for the Factory
            int day = GoalHelper.getDayInWeek(LocalDate.now());
            if (mWidgetData.criticalEvents != null) {
                List<String> criticalGoals = mWidgetData.criticalEvents.get(day);
                for (String goalName : criticalGoals) {
                    sWidgetListItems.add(new WidgetListItem(goalName, Config.CRITICAL_GOAL_WIDGET_TEXT));
                }
            }
            if (mWidgetData.normalEvents != null) {
                List<String> normalGoals = mWidgetData.normalEvents.get(day);
                for (String goalName : normalGoals) {
                    sWidgetListItems.add(new WidgetListItem(goalName, Config.NORMAL_GOAL_WIDGET_TEXT));
                }
            }
        }

        Intent startRemoteViewsServiceIntent = new Intent(context, GoalAcademyWidgetRemoteViewsService.class);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.goal_adacemy_app_widget);

        // Set a template for the fillIntents of the individual ListItems
        //  as defined in onGetItem of the Factory class
        final Intent startMainActivityIntent = new Intent(context, MainActivity.class);

        final PendingIntent onClickPendingIntent =
                PendingIntent.getActivity(context, 0, startMainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widgetListView, onClickPendingIntent);

        views.setRemoteAdapter(R.id.widgetListView, startRemoteViewsServiceIntent);
        views.setEmptyView(R.id.widgetListView, R.id.tv_widget_list_empty);
        if (isOutdated) {
            views.setTextViewText(R.id.tv_widget_list_empty, context.getResources().getString(R.string.widget_outdated_text));
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);   // this will trigger the Service/Factory only first creation
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widgetListView);

        // Creating an intent to launch MainActivity when clicked
        Intent intent2 = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent2, 0);

        // Set click handler to launch pending intent
        views.setOnClickPendingIntent(R.id.ll_widget_goal_academy, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * This is triggered each interval set in android:updatePeriodMillis="XXX"
     * Also is executed after onReceive, if initiated by an Intent
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
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    /**
     * Catches the intent from MainActivity. Saves the provided data to SP.
     * Next method to be called: onUpdate
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        mSerializedWidgetData = intent.getStringExtra(Constants.SERIALIZED_WIDGET_DATA);
        // Storing data in Shared Preferences
        if (mSerializedWidgetData != null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            sharedPreferences.edit().putString(Constants.GOAL_ACADEMY_PREFS_WIDGET_DATA,
                    mSerializedWidgetData).apply();
        }
        else {
            if (BuildConfig.DEBUG) {
                Timber.w(Config.WIDGET_ERROR);
            }
        }
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

