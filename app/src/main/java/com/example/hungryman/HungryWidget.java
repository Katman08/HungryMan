package com.example.hungryman;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;


@RequiresApi(api = Build.VERSION_CODES.O)
public class HungryWidget extends AppWidgetProvider {
    static RemoteViews views;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        views = new RemoteViews(context.getPackageName(), R.layout.hungry_widget);

        GetData getData = new GetData();
        getData.execute(views, appWidgetManager, appWidgetId);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

}