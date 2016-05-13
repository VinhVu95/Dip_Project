package com.ntu.smartlearning.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

/**
 * Created by Vu Anh Vinh on 27/2/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // The task need to carry out at this time
        Bundle extras = intent.getExtras();
        String message = extras.getString("MESSAGE");
        String display = String.format("Your personal deadline for the task %s",message);
        System.out.println (message);
        Toast.makeText(context,display, Toast.LENGTH_SHORT).show();
        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(2000);
    }
}
