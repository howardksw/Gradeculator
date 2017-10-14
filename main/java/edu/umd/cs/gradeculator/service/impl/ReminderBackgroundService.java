package edu.umd.cs.gradeculator.service.impl;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

import edu.umd.cs.gradeculator.CategoryActivity;
import edu.umd.cs.gradeculator.CourseDirActivity;
import edu.umd.cs.gradeculator.DependencyFactory;
import edu.umd.cs.gradeculator.MainActivity;
import edu.umd.cs.gradeculator.R;
import edu.umd.cs.gradeculator.model.Course;
import edu.umd.cs.gradeculator.service.CourseService;

/**
 * Created by weng2 on 4/28/2017.
 */

public class ReminderBackgroundService extends IntentService {


    private static  int MY_NOTIFICATION_ID = 1;
    private static int REMINDER_REQUEST = 2;
    private static int MAIN_REQUEST = 3;
    private static AlarmManager alarmManager;
    private static PendingIntent alarmPendingIntent;

    public ReminderBackgroundService(){
        super("reminder");

    }
    public static Intent newIntent(Context context){
        return new Intent(context, ReminderBackgroundService.class);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        CourseService courseService = DependencyFactory.getCourseService(getApplicationContext());
        ArrayList<Course> courses = courseService.getAllCourses();
        int notificationId = 0;
        for(Course ele: courses){
            if(ele.soFarGrade()>-1 && (ele.soFarGrade() < ele.getDesire_grade()))
            {
               notificationId++;

               String courseTitle = ele.getTitle();


               //Intent restartMainIntent = MainActivity.newIntent(this);
                Intent restartCourseDirIntent = CourseDirActivity.newIntent(this,ele.getId());
               PendingIntent mContentIntent = PendingIntent.getActivity(getApplicationContext(), MAIN_REQUEST,restartCourseDirIntent,PendingIntent.FLAG_UPDATE_CURRENT);

               Notification.Builder notificationBuilder = new Notification.Builder(getApplicationContext())
                       .setTicker(getResources().getString(R.string.reminder_notification))
                       .setSmallIcon(android.R.drawable.ic_menu_agenda)
                       .setContentTitle(getResources().getString(R.string.reminder_notification))
                       .setContentText(courseTitle + " " +
                               getResources().getString(R.string.reminder_notification_content))
                       .setContentIntent(mContentIntent)
                       .setAutoCancel(true);

               Notification notification = notificationBuilder.build();


               NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                       .getSystemService(Context.NOTIFICATION_SERVICE);

               notificationManager.notify(notificationId,notification);





           }
        }


        for(int x=0; x <10;x++){

        }


    }

    public static void setReminderAlarm(Context context, int intervalInMinutes){

        long interval =  (intervalInMinutes*60000) / 60;

        Intent reminderIntent = ReminderBackgroundService.newIntent(context);
        alarmPendingIntent = PendingIntent.getService(context,REMINDER_REQUEST,reminderIntent,PendingIntent.FLAG_UPDATE_CURRENT);



        alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + interval,
                interval,alarmPendingIntent);

    }
    public static void cancelReminderAlarm(Context context){
        if (alarmManager!= null) {
            alarmManager.cancel(alarmPendingIntent);
        }
    }














}
