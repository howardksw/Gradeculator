package edu.umd.cs.gradeculator.service.impl;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import edu.umd.cs.gradeculator.CategoryActivity;
import edu.umd.cs.gradeculator.DependencyFactory;
import edu.umd.cs.gradeculator.MainActivity;
import edu.umd.cs.gradeculator.R;
import edu.umd.cs.gradeculator.model.Work;
import edu.umd.cs.gradeculator.service.CourseService;

/**
 * Created by weng2 on 5/3/2017.
 */

public class NotificationService extends Service {

    private NotificationManager notificationManager;
    private static int MAIN_REQUEST = 3;
    private static  int MY_NOTIFICATION_ID = 4;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        CourseService courseService = DependencyFactory.getCourseService(getApplicationContext());

        if(intent !=null) {
            Bundle bundle = intent.getExtras();
            String title = bundle.getString("title");
            int days = bundle.getInt("days");
            Work.Category category= (Work.Category) bundle.getSerializable("category");
            String courseId = bundle.getString("courseId");

            String courseTitle = courseService.getCourseById(courseId).getTitle();

            notificationManager = (NotificationManager) this.getApplicationContext()
                    .getSystemService(
                            this.getApplicationContext().NOTIFICATION_SERVICE);

            //Intent restartMainIntent = MainActivity.newIntent(this);

            Intent restartCategoryIntent = CategoryActivity.newIntent(this,courseId,category);

            PendingIntent mContentIntent = PendingIntent.getActivity(getApplicationContext(), MAIN_REQUEST,restartCategoryIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            String daysString;
            if (days == 0) {
                daysString = "today" + "!";
            }else if (days == 1){

                daysString = "in "+days  + " day!";
            }else {
                daysString = "in "+days  + " days!";
            }


            Notification.Builder notificationBuilder = new Notification.Builder(getApplicationContext())
                        .setTicker(getResources().getString(R.string.reminder_notification))
                        .setSmallIcon(android.R.drawable.ic_menu_compass)
                        .setContentTitle(getResources().getString(R.string.reminder_notification))
                        .setContentText(title +" of "+ courseTitle + " is due " + daysString)
                        .setContentIntent(mContentIntent)
                        .setAutoCancel(true);


            Notification notification = notificationBuilder.build();


            NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(MY_NOTIFICATION_ID,notification);



        }
    }
}
