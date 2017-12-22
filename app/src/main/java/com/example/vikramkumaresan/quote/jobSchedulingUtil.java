package com.example.vikramkumaresan.quote;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

public class jobSchedulingUtil {
    @TargetApi(Build.VERSION_CODES.M)
    public static void scheduleJob(Context context, long nextCall){
        ComponentName serviceComp = new ComponentName(context,updateTriggerService.class);

        JobInfo.Builder builder = new JobInfo.Builder(0,serviceComp);
        builder.setMinimumLatency(nextCall);
        builder.setOverrideDeadline(nextCall+3000);

        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());

    }
}
