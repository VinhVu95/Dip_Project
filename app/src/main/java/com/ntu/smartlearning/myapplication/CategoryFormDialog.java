package com.ntu.smartlearning.myapplication;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import controller.CategoryController;
import db.DatabaseHelper;
import entity.Category;

/**
 * Created by Vu Anh Vinh on 9/3/2016.
 */
public class CategoryFormDialog extends DialogFragment {
    private DatabaseHelper db;
    private CategoryController categoryController;
    private View rootView;
    HashMap<String,Category> map;
    private Spinner dropdown;
    EditText et;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        categoryController = CategoryController.getInstance();

        db = DatabaseHelper.getInstance(MainActivity.getActivity());

        getDialog().setTitle("New Category");

        rootView = inflater.inflate(R.layout.addcategory_fragment, container, false);

        et = (EditText) rootView.findViewById(R.id.editText);

        selectParentCategory();

        backToMain();

        submit();
        return rootView;
    }

    private void backToMain(){
        ImageButton btnback = (ImageButton) rootView.findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //back to main activity
                onStop();
            }
        });
    }

    public void selectParentCategory() {
        dropdown = (Spinner) rootView.findViewById(R.id.parent_category_spinner);
        map = new HashMap<String,Category>();

        ArrayList<Category> allCat = db.getAllCategory();
        ArrayList<String> itemsString = new ArrayList<String>();
        for(int i = 0;i<allCat.size();i++)
        {
            Category c = allCat.get(i);
            if(c.getParentId() == 0)
            {
                itemsString.add(c.getCatName());
                map.put(c.getCatName(),c);
            }
        }
        itemsString.add("No parent");
        String[] itemArr = new String[itemsString.size()];
        itemArr = itemsString.toArray(itemArr);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.getActivity(),
                android.R.layout.simple_spinner_dropdown_item, itemArr);
        dropdown.setAdapter(adapter);

    }

    private void submit(){
        Button submitBtn = (Button) rootView.findViewById(R.id.submitbutton);
        submitBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a Category
                if(!et.getText().toString().equals("")) {
                    if(dropdown.getSelectedItem().toString().equals("No parent"))
                    {
                        categoryController.createCategory(et.getText().toString(),null,db);
                    } else {
                       categoryController.createCategory(et.getText().toString(),
                               map.get(dropdown.getSelectedItem().toString()),db);
                    }
                    Intent intent = new Intent(MainActivity.getActivity(),MainActivity.class);
                    MainActivity.getActivity().finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.getActivity().getApplicationContext(),"Please key in Category name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
