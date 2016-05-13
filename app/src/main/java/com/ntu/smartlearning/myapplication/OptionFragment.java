package com.ntu.smartlearning.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Vu Anh Vinh on 8/3/2016.
 */
public class OptionFragment extends DialogFragment {

    private String[] option_array = new String[] {"Task","Category"};
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose an option")
                .setItems(option_array, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0)
                        {
                            DialogFragment taskFormDialog = new TaskFormDialog();
                            taskFormDialog.show(getFragmentManager(),"new task");
                        }
                        else
                        {
                            Toast.makeText(MainActivity.getActivity(),"new category",Toast.LENGTH_SHORT).show();
                            DialogFragment categoryFormDialog = new CategoryFormDialog();
                            categoryFormDialog.show(getFragmentManager(),"new category");
                        }
                    }
                });
        return builder.create();
    }
}
