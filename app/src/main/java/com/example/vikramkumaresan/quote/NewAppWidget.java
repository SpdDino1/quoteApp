package com.example.vikramkumaresan.quote;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewAppWidget extends AppWidgetProvider {


    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,final int appWidgetId) {

        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, "http://quote-scraper.herokuapp.com/getQuote", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String quote = '"'+response.getString("quote")+'"'+" "+response.getString("author");

                            views.setTextViewText(R.id.quote, quote);
                            appWidgetManager.updateAppWidget(appWidgetId, views);

                            jobSchedulingUtil.scheduleJob(context,calcNextCallTime());

                        } catch (JSONException e) {
                            Log.d("test","JSONException");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test","Error = "+error.toString());
                        jobSchedulingUtil.scheduleJob(context,300000);
                    }
                }
        );
        queue.add(req);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //BOOT_COMPLETE or WidgetUpdate => Update widget!
        onUpdate(context,AppWidgetManager.getInstance(context),intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS));
    }

   @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private static int calcNextCallTime(){
        SimpleDateFormat HR = new SimpleDateFormat("HH");
        SimpleDateFormat MIN = new SimpleDateFormat("mm");
        int hr = Integer.parseInt(HR.format(Calendar.getInstance().getTime()));
        int min = Integer.parseInt(MIN.format(Calendar.getInstance().getTime()));

        int nextHr,nextMin =(60-min)*60000;

        if(hr>=12){
            nextHr = ((24 - hr)+5) *3600000; //millis
        }
        else{
            if(hr>=6){
                nextHr =(23-(hr-6))*3600000;
            }
            else{
                nextHr = (5 - hr)*3600000;
            }
        }

        return (nextHr+nextMin);
    }

    @Override
    public void onEnabled(Context context){
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

