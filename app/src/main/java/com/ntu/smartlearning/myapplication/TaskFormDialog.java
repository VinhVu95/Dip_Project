package com.ntu.smartlearning.myapplication;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import controller.AddTaskController;
import db.DatabaseHelper;
import entity.Category;

/**
 * Created by Vu Anh Vinh on 18/2/2016.
 */
public class TaskFormDialog extends DialogFragment {
        private AddTaskController addTaskController;
        private DatabaseHelper db;
        private View rootView;
        private int year;
        private int month;
        private int day;
        private int hour;
        private int minute11;
        //saved data chosen by user
        Calendar actualDeadline = Calendar.getInstance();
        Calendar personalDeadline = Calendar.getInstance();
        HashMap<String,Category> map;
        EditText et;
        Spinner dropdown;

        //Intent to send to BroadcastReceiver
        private PendingIntent pendingIntent;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            getDialog().setTitle("New Task");

            addTaskController = AddTaskController.getInstance();

            rootView = inflater.inflate(R.layout.addtask_fragment,container,false);

            db = DatabaseHelper.getInstance(MainActivity.getActivity());

            et = (EditText) rootView.findViewById(R.id.editText);

            //select category
            selectCategory();

            //back to main button
            backToMain();

            // current date and time
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy - MM - dd ");
            SimpleDateFormat tdformat = new SimpleDateFormat("hh:mm");
            String strDate = mdformat.format(calendar.getTime());
            String strTime=tdformat.format(calendar.getTime());


            year=calendar.get(Calendar.YEAR);
            month=calendar.get(Calendar.MONTH);
            day=calendar.get(Calendar.DAY_OF_MONTH);
            hour=calendar.get(Calendar.HOUR_OF_DAY);
            minute11=calendar.get(Calendar.MINUTE);

            //set default value for deadlines
            actualDeadline.set(year,month,day,hour,minute11);
            personalDeadline.set(year,month,day,hour,minute11);

            final TextView txttime=(TextView)rootView.findViewById(R.id.txttime);
            final TextView txtdate=(TextView)rootView.findViewById(R.id.txtdate);
            final TextView txttime2=(TextView)rootView.findViewById(R.id.txttime2);
            final TextView txtdate2=(TextView)rootView.findViewById(R.id.txtdate2);

            //display date and time for deadlines
            displayDate(strDate, txtdate);
            displayTime(strTime, txttime);
            displayDate(strDate, txtdate2);
            displayTime(strTime, txttime2);


            txttime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.getActivity().getApplicationContext(), "CLICKED", Toast.LENGTH_LONG).show();

                    TimePickerDialog timepick = new TimePickerDialog(MainActivity.getActivity(), new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourofDay, int minute) {
                            String hourString;
                            if (hourofDay < 10)
                                hourString = "0" + hourofDay;
                            else
                                hourString = "" + hourofDay;
                            actualDeadline.set(Calendar.HOUR_OF_DAY,hourofDay);

                            String minuteSting;
                            if (minute < 10)
                                minuteSting = "0" + minute;
                            else
                                minuteSting = "" + minute;
                            actualDeadline.set(Calendar.MINUTE,minute);

                            txttime.setText(hourString + ":" + minuteSting);
                        }

                    }, hour, minute11, true
                    );
                    timepick.setTitle("Select time");
                    timepick.show();
                }
            });


            txtdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datepick = new DatePickerDialog(MainActivity.getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            int realMonthOfYear = monthOfYear + 1;
                            txtdate.setText(year + "-" + realMonthOfYear + "-" + dayOfMonth);
                            actualDeadline.set(Calendar.YEAR, year);
                            actualDeadline.set(Calendar.MONTH,monthOfYear);
                            actualDeadline.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        }
                    }, year, month, day);
                    datepick.setTitle("Select date");
                    datepick.show();

                }
            });

            txttime2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.getActivity().getApplicationContext(), "CLICKED", Toast.LENGTH_LONG).show();

                    TimePickerDialog timepick = new TimePickerDialog(MainActivity.getActivity(), new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourofDay, int minute) {
                            String hourString;
                            if (hourofDay < 10)
                                hourString = "0" + hourofDay;
                            else
                                hourString = "" + hourofDay;
                            personalDeadline.set(Calendar.HOUR_OF_DAY, hourofDay);

                            String minuteSting;
                            if (minute < 10)
                                minuteSting = "0" + minute;
                            else
                                minuteSting = "" + minute;
                            personalDeadline.set(Calendar.MINUTE, minute);

                            txttime2.setText(hourString + ":" + minuteSting);
                        }

                    }, hour, minute11, true
                    );
                    timepick.setTitle("Select time");
                    timepick.show();
                }
            });

            txtdate2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datepick = new DatePickerDialog(MainActivity.getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            int realMonthOfYear = monthOfYear + 1;
                            txtdate2.setText(year + "-" + realMonthOfYear + "-" + dayOfMonth);
                            personalDeadline.set(Calendar.YEAR, year);
                            personalDeadline.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            personalDeadline.set(Calendar.MONTH, monthOfYear);
                        }
                    }, year, month, day);
                    datepick.setTitle("Select date");
                    datepick.show();

                }
            });

            submit(actualDeadline,personalDeadline);

            return rootView;
        }

        private void displayDate(String num, TextView textView) {
            //TextView textView = (TextView) rootView.findViewById(R.id.txtdate);
            textView.setText(num);

        }
        private void displayTime(String num, TextView textView) {
            //TextView textView = (TextView) rootView.findViewById(R.id.txttime);
            textView.setText(num);

        }

        private void selectCategory() {

            dropdown = (Spinner) rootView.findViewById(R.id.category_spinner);
             map = new HashMap<String,Category>();

            ArrayList<Category> allCat = db.getAllCategory();
            String[] itemsString = new String[allCat.size()];
            for(int i = 0;i<allCat.size();i++)
            {
                itemsString[i] = allCat.get(i).getCatName();
                map.put(itemsString[i], allCat.get(i));
                //System.out.println(itemsString[i]);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.getActivity(),
                    android.R.layout.simple_spinner_dropdown_item, itemsString);
            dropdown.setAdapter(adapter);

        }

        private void backToMain() {
            ImageButton btnback = (ImageButton) rootView.findViewById(R.id.btnback);
            btnback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //back to main activity
                    onStop();
                }
            });
        }

        private void submit(final Calendar deadline, final Calendar personalDeadline) {
            Button submitBtn = (Button) rootView.findViewById(R.id.submitbutton);
            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //create a Task
                    if(!et.getText().toString().equals("")){

                        addTaskController.createTask(et.getText().toString(), deadline, personalDeadline,
                                map.get(dropdown.getSelectedItem().toString()), false, 0, db);
                        // set alarm
                        setAlarm(personalDeadline);
                        Intent intent = new Intent(MainActivity.getActivity(),MainActivity.class);
                        MainActivity.getActivity().finish();
                        startActivity(intent);
                    } else {
                       Toast.makeText(MainActivity.getActivity().getApplicationContext(), "Please key in task name", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        private void setAlarm(Calendar cal) {
            Intent alarmIntent = new Intent(MainActivity.getActivity(),AlarmReceiver.class);
            Bundle bundle = new Bundle();
            bundle.putString("MESSAGE",et.getText().toString());
            alarmIntent.putExtras(bundle);
            pendingIntent = PendingIntent.getBroadcast(MainActivity.getActivity(),0,alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager manager = (AlarmManager) MainActivity.getActivity().getSystemService(Context.ALARM_SERVICE);
            manager.setExact(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pendingIntent);
        }
}
