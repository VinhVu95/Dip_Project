package com.ntu.smartlearning.myapplication;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import controller.PointCalculationController;
import controller.UpdateScoreController;
import db.DatabaseHelper;

/**
 * Created by Vu Anh Vinh on 25/3/2016.
 */
public class RankCalculationService extends Service {

    public static RankCalculationService firstInstance;
    private static long i = 0;
    private DatabaseHelper dbHelper;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * Used to name the worker thread, important only for debugging.
     */


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);
        task();
        if(firstInstance == null){
            firstInstance = this;
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    protected void task() {
        //Get database helper
        dbHelper = DatabaseHelper.getInstance(MainActivity.getActivity());
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //Log.d("service",String.valueOf(i++));
                Calendar cal = Calendar.getInstance();
                cal.setTime(cal.getTime());
                PointCalculationController pccInstance = PointCalculationController.getInstance();
                int weeklyScore = pccInstance.getWeeklyScore(cal.getTimeInMillis(), dbHelper);
                int totalScore = pccInstance.getTotalScore(dbHelper);
                Log.d("Weekly Score: ", String.valueOf(weeklyScore));
                Log.d("Total Score so far: ", String.valueOf(totalScore));
                UpdateScoreController.getInstance().setScore(Integer.toString(totalScore));

            }
        };
        Timer timer = new Timer("ASd");
        timer.schedule(timerTask,0,2*1000);
    }
}
