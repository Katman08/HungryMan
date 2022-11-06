package com.example.hungryman;

import android.appwidget.AppWidgetManager;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RequiresApi(api = Build.VERSION_CODES.O)
public class GetData {
    RemoteViews views;
    AppWidgetManager appWidgetManager;
    int appWidgetId;

    public void execute(RemoteViews views, AppWidgetManager appWidgetManager, int appWidgetId) {

        this.views = views;
        this.appWidgetManager = appWidgetManager;
        this.appWidgetId = appWidgetId;

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(doShit);
    }

    Runnable doShit = () -> {

        String[] names = {
                "McEwen Food Hall",
                "Billy D's Fried Chicken",
                "Lakeside Dining Hall",
                "Clohan Hall"
        };

        String[] data = new String[] {
                "Closed",
                "Closed",
                "Closed",
                "Closed"
        };

        boolean open;

        Elements openTimes;
        LocalDateTime oTime;
        String openTime = "";

        Elements closeTimes;
        LocalDateTime cTime;
        String closeTime = "";

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy ");
        String currentDate = now.format(dateFormatter);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");

        try {
            String url = "https://www.elondining.com/menu-hours/";
            Document doc = Jsoup.connect(url).get();
            Elements locations = doc.select(".location");

            for (Element location : locations) {
                open = false;

                for (int i=0; i< data.length; i++) {
                    if (location.select(".open-now-location-link").text().equals(names[i])) {

                        openTimes = location.select(".hours-row").select(".hour.open_at");
                        closeTimes = location.select(".hours-row").select(".hour.close_at");

                        for (int j=0; j<openTimes.size(); j++) {
                            oTime = LocalDateTime.parse(currentDate + openTimes.get(j).text().toUpperCase(), formatter);
                            cTime = LocalDateTime.parse(currentDate + closeTimes.get(j).text().toUpperCase(), formatter);

                            if (oTime.compareTo(cTime) > 0) cTime = cTime.plusDays(1);

                            if (now.compareTo(oTime) > 0 && now.compareTo(cTime) < 0) {
                                open = true;
                                closeTime = closeTimes.get(j).text();
                            } else if (now.compareTo(oTime) < 0){
                                openTime = openTimes.get(j).text();
                            }

                        }

                        if (open) data[i] = closeTime.equals("") ? "Open" : "Open Until " + closeTime;
                        else data[i] = openTime.equals("") ? "Closed" : "Closed Until " + openTime;
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        views.setTextViewText(R.id.loc1_name, names[0]);
        views.setTextViewText(R.id.loc1_status, data[0]);

        views.setTextViewText(R.id.loc2_name, names[1]);
        views.setTextViewText(R.id.loc2_status, data[1]);

        views.setTextViewText(R.id.loc3_name, names[2]);
        views.setTextViewText(R.id.loc3_status, data[2]);

        views.setTextViewText(R.id.loc4_name, names[3]);
        views.setTextViewText(R.id.loc4_status, data[3]);

        appWidgetManager.updateAppWidget(appWidgetId, views);

    };



}