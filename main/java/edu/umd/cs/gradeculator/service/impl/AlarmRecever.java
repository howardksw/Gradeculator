package edu.umd.cs.gradeculator.service.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import edu.umd.cs.gradeculator.model.Work;

/**
 * Created by weng2 on 5/3/2017.
 */

public class AlarmRecever extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
         if(intent !=null){
             Bundle bundle = intent.getExtras();
             //int id = bundle.getInt("id");
             String title =bundle.getString("title");
             int days = bundle.getInt("days");
             Work.Category category= (Work.Category) bundle.getSerializable("category");
             String courseId = bundle.getString("courseId");

             Intent myIntent = new Intent(context,NotificationService.class);
             myIntent.putExtra("title",title);
             myIntent.putExtra("days",days);
             myIntent.putExtra("category",category);
             myIntent.putExtra("courseId",courseId);

             context.startService(myIntent);



         }
    }
}
