package controller;

/**
 * Created by Vu Anh Vinh on 17/2/2016.
 */
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;

import db.DatabaseHelper;
import entity.Category;
import entity.Task;


public class AddTaskController {
    private static AddTaskController firstInstance = null;

    public static AddTaskController getInstance(){
        if(firstInstance == null)
        {
            firstInstance = new AddTaskController();
        }
        return firstInstance;
    }
    /**
     *
     * @param hasReminder pass option of user whether to create reminder
     * details of the task
     * @return
     */
    public boolean createTask(String taskName, Calendar deadline, Calendar personalDeadline,
                              Category category, boolean hasReminder , long frequency, DatabaseHelper db) {
        try {
            Task createdTask = new Task(taskName,deadline,personalDeadline,category);

            //category.getTasks().add(createdTask);
            createdTask.setEnableReminder(hasReminder);
            if(hasReminder) {
                createdTask.setReminderFrequency(frequency);
            }
            createdTask.setCategoryId(category.getId());
            db.createTask(createdTask);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Can not create object");
            return false;
        }
        return true;
    }
}
