package com.ntu.smartlearning.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;


import controller.FinishTaskController;
import db.DatabaseHelper;

/**
 * Created by Vu Anh Vinh on 28/2/2016.
 */
public class ConfirmDialog extends DialogFragment {
    private FinishTaskController fControll;
    private DatabaseHelper db;

    public static ConfirmDialog newInstance(int title,long taskId) {
        ConfirmDialog frag = new ConfirmDialog();
        Bundle args = new Bundle();
        args.putInt("title",title);
        args.putLong("taskId", taskId);
        frag.setArguments(args);
        return frag;

    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        db = DatabaseHelper.getInstance(MainActivity.getActivity());
        fControll = FinishTaskController.getInstance();
        int title = getArguments().getInt("title");
        final long taskID = getArguments().getLong("taskId");

        AlertDialog .Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(title)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                fControll.finishTask(taskID, db);
                                View deletedView = MainActivity.getDeletedRow();
                                //System.out.println(((ViewGroup)deletedView.getParent()).toString());
                                ((ViewGroup) deletedView.getParent()).removeView(deletedView);

                            }
                        }
                )
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Dismiss if cancel
                            }
                        }
                );
        return builder.create();
    }
}
