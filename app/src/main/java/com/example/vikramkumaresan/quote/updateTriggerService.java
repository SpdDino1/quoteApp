package com.example.vikramkumaresan.quote;

import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class updateTriggerService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        Thread thread = new Thread(){
            @Override
            public void run() {
                Intent intent = new Intent(updateTriggerService.this,NewAppWidget.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

                int[] ids = AppWidgetManager.getInstance(updateTriggerService.this).getAppWidgetIds(new ComponentName(getApplication(),NewAppWidget.class));
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
                sendBroadcast(intent);
            }
        };
        thread.run();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }


}
