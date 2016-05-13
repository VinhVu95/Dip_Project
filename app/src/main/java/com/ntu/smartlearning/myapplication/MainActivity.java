package com.ntu.smartlearning.myapplication;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import controller.PointCalculationController;
import controller.UpdateScoreController;
import db.DatabaseHelper;
import entity.Category;

public class MainActivity extends AppCompatActivity implements Observer {

    private DatabaseHelper db;
    private ArrayList<Category> mainList;
    //private Calendar cal;
    private LinearLayout mLinearListView;
    boolean isFirstViewClick = false;
    boolean isSecondViewClick = false;
    private static MainActivity activity;
    private static View savedView;
    private static Intent cServiceIntent;

    public static MainActivity getActivity() {
        return activity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = DatabaseHelper.getInstance(getApplicationContext());
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UpdateScoreController.getInstance().addObserver(this);

        //Get score
        new LoadContent().execute();

        if (RankCalculationService.firstInstance == null) {
            cServiceIntent = new Intent(this, RankCalculationService.class);
            this.startService(cServiceIntent);
        }

        //new task
        create();

        mLinearListView = (LinearLayout) findViewById(R.id.linear_ListView);

        //Make array list one is for mainlist
        mainList = db.getDisplayContent();
        //System.out.println(mainList.size());

        //Add data into first row
        for (int i = 0; i < mainList.size(); i++) {

            LayoutInflater inflater = null;
            inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View mLinearView = inflater.inflate(R.layout.row_first, null);

            final TextView mainCatName = (TextView) mLinearView.findViewById(R.id.textViewName);
            final RelativeLayout mLinearFirstArrow = (RelativeLayout) mLinearView.findViewById(R.id.linearFirst);
            final ImageView mImageArrowFirst = (ImageView) mLinearView.findViewById(R.id.imageFirstArrow);
            final LinearLayout mLinearScrollSecond = (LinearLayout) mLinearView.findViewById(R.id.linear_scroll);

            //checkes if menu is already opened or not
            if (isFirstViewClick == false) {
                mLinearScrollSecond.setVisibility(View.GONE);
                mImageArrowFirst.setBackgroundResource(R.drawable.plus_sign);
            } else {
                mLinearScrollSecond.setVisibility(View.VISIBLE);
                mImageArrowFirst.setBackgroundResource(R.drawable.plus_sign);
            }
            //Handles onclick effect on list item
            mainCatName.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (isFirstViewClick == false) {
                        isFirstViewClick = true;
                        mImageArrowFirst.setBackgroundResource(R.drawable.plus_sign);
                        mLinearScrollSecond.setVisibility(View.VISIBLE);

                    } else {
                        isFirstViewClick = false;
                        mImageArrowFirst.setBackgroundResource(R.drawable.plus_sign);
                        mLinearScrollSecond.setVisibility(View.GONE);
                    }
                    return false;
                }
            });

            final String name = mainList.get(i).getCatName();
            mainCatName.setText(name);

            //Add data into second row
            for (int j = 0; j < mainList.get(i).getChildrenCategory().size(); j++) {
                LayoutInflater inflater2 = null;
                inflater2 = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mLinearView2 = inflater2.inflate(R.layout.row_second, null);

                TextView mSubCatName = (TextView) mLinearView2.findViewById(R.id.textViewTitle);
                final RelativeLayout mLinearSecondArrow = (RelativeLayout) mLinearView2.findViewById(R.id.linearSecond);
                final ImageView mImageArrowSecond = (ImageView) mLinearView2.findViewById(R.id.imageSecondArrow);
                final LinearLayout mLinearScrollThird = (LinearLayout) mLinearView2.findViewById(R.id.linear_scroll_third);

                //checkes if menu is already opened or not
                if (isSecondViewClick == false) {
                    mLinearScrollThird.setVisibility(View.GONE);
                    mImageArrowSecond.setBackgroundResource(R.drawable.plus_sign);
                } else {
                    mLinearScrollThird.setVisibility(View.VISIBLE);
                    mImageArrowSecond.setBackgroundResource(R.drawable.plus_sign);
                }
                //Handles onclick effect on list item
                mSubCatName.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (isSecondViewClick == false) {
                            isSecondViewClick = true;
                            mImageArrowSecond.setBackgroundResource(R.drawable.plus_sign);
                            mLinearScrollThird.setVisibility(View.VISIBLE);

                        } else {
                            isSecondViewClick = false;
                            mImageArrowSecond.setBackgroundResource(R.drawable.plus_sign);
                            mLinearScrollThird.setVisibility(View.GONE);
                        }
                        return false;
                    }
                });

                final String subCatName = mainList.get(i).getChildrenCategory().get(j).getCatName();
                mSubCatName.setText(subCatName);

                //Add tasks in subcategories
                for (int k = 0; k < mainList.get(i).getChildrenCategory().get(j).getTasks().size(); k++) {
                    LayoutInflater inflater3 = null;
                    inflater3 = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View mLinearView3 = inflater3.inflate(R.layout.row_third, null);

                    TextView mTaskName = (TextView) mLinearView3.findViewById(R.id.textViewItemName);
                    TextView mDeadline = (TextView) mLinearView3.findViewById(R.id.textViewItemDate);
                    final String taskName = mainList.get(i).getChildrenCategory().get(j).getTasks().get(k).getTaskName();
                    final Date taskDeadline = mainList.get(i).getChildrenCategory().get(j).getTasks().get(k).getActualDeadline().getTime();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String dateString = format.format(taskDeadline);
                    mTaskName.setText(taskName);
                    mDeadline.setText(dateString);
                    mLinearScrollThird.addView(mLinearView3);

                    //click on Finish Button

                    finishTask(mLinearView3, mainList.get(i).getChildrenCategory().get(j).getTasks().get(k).getId());
                }
                mLinearScrollSecond.addView(mLinearView2);
            }

            for (int j = 0; j < mainList.get(i).getTasks().size(); j++) {
                LayoutInflater inflater4 = null;
                inflater4 = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mLinearView4 = inflater4.inflate(R.layout.row_third, null);

                TextView mTaskName = (TextView) mLinearView4.findViewById(R.id.textViewItemName);
                TextView mDeadline = (TextView) mLinearView4.findViewById(R.id.textViewItemDate);
                final String taskName = mainList.get(i).getTasks().get(j).getTaskName();
                final Date taskDeadline = mainList.get(i).getTasks().get(j).getActualDeadline().getTime();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String dateString = format.format(taskDeadline);
                mTaskName.setText(taskName);
                mDeadline.setText(dateString);
                mLinearScrollSecond.addView(mLinearView4);

                //click on Finish Button

                finishTask(mLinearView4, mainList.get(i).getTasks().get(j).getId());
            }
            mLinearListView.addView(mLinearView);
        }

    }

    @Override
    public void update(Observable observable, Object data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RelativeLayout toolBar = (RelativeLayout) findViewById(R.id.profile);
                TextView scoreText = (TextView) toolBar.findViewById(R.id.score);
                TextView type = (TextView) toolBar.findViewById(R.id.type);
                int score = Integer.parseInt(UpdateScoreController.getInstance().getScore());
                scoreText.setText(Integer.toString(score));
                if (score <= -76)
                    type.setText("Paragon of Procrastination");
                else if (score <= -51)
                    type.setText("Major Procrastinator");
                else if (score <= -26)
                    type.setText("Minor Procrastinator");
                else if (score <= -1)
                    type.setText("Amateur Procrastinator");
                else if (score <= 24)
                    type.setText("Persevering Pupil");
                else if (score <= 49)
                    type.setText("The learned One");
                else if (score <= 74)
                    type.setText("Exemplar Scholar");
                else
                    type.setText("Paragon of Intellectuals");

            }
        });

    }

    public static View getDeletedRow() {
        return savedView;
    }

    private void create() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment option = new OptionFragment();
                option.show(getFragmentManager(), "what you want to do?");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void finishTask(final View taskRow, final long taskId) {
        Button fBut = (Button) taskRow.findViewById(R.id.finishButton);
        fBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedView = taskRow;
                DialogFragment newConfirmDialog = ConfirmDialog.newInstance(R.string.finish_title, taskId);
                newConfirmDialog.show(getFragmentManager(), "confirm");
            }
        });
    }

    private class LoadContent extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            int total_score = PointCalculationController.getInstance().getTotalScore(db);
            return Integer.toString(total_score);
        }

        @Override
        protected void onPostExecute(String s) {
            RelativeLayout toolBar = (RelativeLayout) findViewById(R.id.profile);
            TextView score = (TextView) toolBar.findViewById(R.id.score);
            score.setText(s);
            int scr = Integer.parseInt(s);
            TextView type = (TextView) toolBar.findViewById(R.id.type);
            if (scr <= -76)
                type.setText("Paragon of Procrastination");
            else if (scr <= -51)
                type.setText("Major Procrastinator");
            else if (scr <= -26)
                type.setText("Minor Procrastinator");
            else if (scr <= -1)
                type.setText("Amateur Procrastinator");
            else if (scr <= 24)
                type.setText("Persevering Pupil");
            else if (scr <= 49)
                type.setText("The learned One");
            else if (scr <= 74)
                type.setText("Exemplar Scholar");
            else
                type.setText("Paragon of Intellectuals");

        }
    }

}
