package info.weigandt.goalacademy.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * The main purpose of RemoteViewsService is to return a RemoteViewsFactory object
 *  which further handles the task of filling the widget with appropriate data.
 *  As a service, it needs to be registered in the manifest.
 */
public class GoalAcademyWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GoalAcademyWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}