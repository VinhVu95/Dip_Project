package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import controller.CategoryController;
import entity.Category;
import entity.Task;

/**
 * Created by Vu Anh Vinh on 22/2/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper firstInstance = null;
    //Logcat tag
    private static final String LOG = "DatabaseHelper";

    //Database version
    private static final int DATABASE_VERSION = 1;

    //Database name
    private static final String DATABASE_NAME = "DooD Database";

    //Table name
    private static final String TABLE_TASK = "task";
    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_TOTAL_SCORE = "total_score";

    // Common Column names
    private static final String ID = "Id";

    // TASK table - Column names
    private static final String TASK_NAME = "Task_Name";
    private static final String PERSONAL_DEADLINE = "Personal_Deadline";
    private static final String ACTUAL_DEADLINE = "Actual_Deadline";
    private static final String TIME_FINISHED = "Time_Finished";
    private static final String CATEGORY_ID = "Category_Id";
    private static final String FREQUENCY = "Frequency";
    private static final String RANK = "Rank";

    // CATEGORY table - Column names
    private static final String CATEGORY_NAME = "Category_Name";
    private static final String PARENT_CATEGORY = "Parent_Category";

    //TOTAL SCORE table - Column names
    private static final String WEEKLY_SCORE = "Weekly_Score";

    // Table create Statements
    // TASK table create statement
    private static final String CREATE_TABLE_TASK = "CREATE TABLE "
            + TABLE_TASK + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TASK_NAME + " TEXT,"
            + PERSONAL_DEADLINE + " INTEGER," + ACTUAL_DEADLINE + " INTEGER,"
            + TIME_FINISHED + " INTEGER," + CATEGORY_ID + " INTEGER,"
            + FREQUENCY + " INTEGER," + RANK + " INTEGER DEFAULT 0)";

    // CATEGORY table create statement
    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE "
            + TABLE_CATEGORY + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + CATEGORY_NAME + " TEXT,"
            + PARENT_CATEGORY + " INTEGER DEFAULT 0)";

    private static final String CREATE_TABLE_TOTAL_SCORE = "CREATE TABLE "
            + TABLE_TOTAL_SCORE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + WEEKLY_SCORE + " INTEGER)";

    public static DatabaseHelper getInstance(Context context) {
        if(firstInstance == null)
        {
            firstInstance = new DatabaseHelper(context);
        }
        return firstInstance;
    }

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

         // creating required tables
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_TASK);
        db.execSQL(CREATE_TABLE_TOTAL_SCORE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);

        //create new tables

        onCreate(db);
    }

    /**
     * Creating a task
     */
    public long createTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TASK_NAME, task.getTaskName());
        values.put(PERSONAL_DEADLINE, task.getPersonalDeadline().getTimeInMillis());
        values.put(ACTUAL_DEADLINE, task.getActualDeadline().getTimeInMillis());
        values.put(CATEGORY_ID, task.getCategoryId());
        if(task.isEnableReminder())
        {
            values.put(FREQUENCY, task.getReminderFrequency());
        }

        long taskId = db.insert(TABLE_TASK, null, values);

        return taskId;
    }


    /**
     * Creating a Category
     */
    public long createCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CATEGORY_NAME,category.getCatName());
        if(category.getParentCategory() != null)
        {
            values.put(PARENT_CATEGORY,category.getParentId());
        }

        return db.insert(TABLE_CATEGORY, null, values);
    }

    /**
     * Fetching all Category
     */
    public ArrayList<Category> getAllCategory() {
        ArrayList<Category> results = new ArrayList<Category>();
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORY;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and add to list
        if (c.moveToFirst()) {
            do{
                Category cat = new Category();
                cat.setCatName(c.getString(c.getColumnIndex(CATEGORY_NAME)));
                //System.out.println(c.getString(c.getColumnIndex(CATEGORY_NAME)));
                cat.setId(c.getInt(c.getColumnIndex(ID)));
                cat.setParentId(c.getInt(c.getColumnIndex(PARENT_CATEGORY)));
                results.add(cat);
            } while (c.moveToNext());
        }
        return results;
    }

    /*
     * Fetching data
     */
    public ArrayList<Category> getDisplayContent(){
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Category> results = new ArrayList<Category>();
        String selectRootCategoryQuery = "SELECT * FROM " + TABLE_CATEGORY + " WHERE " + PARENT_CATEGORY
                + " = 0 ";

        Cursor c = db.rawQuery(selectRootCategoryQuery, null);
        if (c.moveToFirst()) {
            do{

                Category cat = new Category();
                cat.setCatName(c.getString(c.getColumnIndex(CATEGORY_NAME)));
                long parentId = c.getInt(c.getColumnIndex(ID));
                cat.setId(parentId);
                cat.setParentId(c.getInt(c.getColumnIndex(PARENT_CATEGORY)));

                //Get child category
                String selectChildrenCategoryQuery = "SELECT * FROM " + TABLE_CATEGORY + " WHERE " + PARENT_CATEGORY
                    + " = " + parentId;
                Cursor c1 = db.rawQuery(selectChildrenCategoryQuery,null);
                if (c1.moveToFirst()) {
                    do{
                        Category cCat = new Category();
                        cCat.setCatName(c1.getString(c1.getColumnIndex(CATEGORY_NAME)));
                        long childId = c1.getInt(c1.getColumnIndex(ID));
                        cCat.setId(childId);
                        cCat.setParentId(c1.getInt(c1.getColumnIndex(PARENT_CATEGORY)));
                        cat.getChildrenCategory().add(cCat);

                        //Get tasks
                        cCat.setTasks(getTasksByCategory(childId,db));
                    } while (c1.moveToNext());
                }
                results.add(cat);

                //Get tasks
                cat.setTasks(getTasksByCategory(parentId, db));
            } while (c.moveToNext());
        }

        return results;
    }
    /*
     * Get task by ID
     */
    public Task getTaskById(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TASK + " WHERE " + ID
                + " = " + id;

        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Task task = new Task();
        Calendar cal = Calendar.getInstance();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        task.setTaskName(c.getString(c.getColumnIndex(TASK_NAME)));
        task.setId(c.getInt(c.getColumnIndex(ID)));
        task.setCategoryId(c.getInt(c.getColumnIndex(CATEGORY_ID)));
        cal.setTimeInMillis(c.getLong(c.getColumnIndex(PERSONAL_DEADLINE)));
        task.setPersonalDeadline(cal);
        cal1.setTimeInMillis(c.getLong(c.getColumnIndex(ACTUAL_DEADLINE)));
        task.setActualDeadline(cal1);
        cal2.setTimeInMillis(c.getLong(c.getColumnIndex(TIME_FINISHED)));
        task.setEndTime(cal2);
        return task;
    }

    /*
     *  Update task by id
     */
    public void updateTaskFinish(long taskId,long timeFinish){
        SQLiteDatabase db = this.getWritableDatabase();

        String updateStatement = "UPDATE " + TABLE_TASK + " SET " + TIME_FINISHED
                                + " = " + timeFinish + " WHERE " + ID + " = " +taskId;
        db.execSQL(updateStatement);
    }

    /*
     * Update task rank point
     */
    public void updateTaskRank(long taskId,int rankPoint) {
        SQLiteDatabase db = this.getWritableDatabase();

        String updateStatement = "UPDATE " + TABLE_TASK + " SET " + RANK
                                + " = " + rankPoint + " WHERE " + ID + " = " +taskId;
        db.execSQL(updateStatement);
    }

    /*
     * Get task for category
     */
    private ArrayList<Task> getTasksByCategory(long id,SQLiteDatabase db){
        ArrayList<Task> results = new ArrayList<Task>();
        String selectTaskQuery = "SELECT * FROM " + TABLE_TASK + " WHERE " + CATEGORY_ID + " = "
                + id;
        Cursor c = db.rawQuery(selectTaskQuery, null);
        if(c.moveToFirst()){
            do{
                if(c.getLong(c.getColumnIndex(TIME_FINISHED)) == 0)
                {
                    Calendar cal = Calendar.getInstance();
                    Calendar cal1 = Calendar.getInstance();

                    Task task = new Task();
                    task.setTaskName(c.getString(c.getColumnIndex(TASK_NAME)));
                    task.setId(c.getInt(c.getColumnIndex(ID)));
                    task.setCategoryId(c.getInt(c.getColumnIndex(CATEGORY_ID)));
                    cal.setTimeInMillis(c.getLong(c.getColumnIndex(PERSONAL_DEADLINE)));
                    task.setPersonalDeadline(cal);
                    cal1.setTimeInMillis(c.getLong(c.getColumnIndex(ACTUAL_DEADLINE)));
                    task.setActualDeadline(cal1);
                    results.add(task);
                }

            }while (c.moveToNext());
        }
        return  results;
    }

    /**
     * Get task by time finished
     */
    public ArrayList<Task> getTasksByTime(long time) {
        ArrayList<Task> results = new ArrayList<Task>();
        SQLiteDatabase db = this.getReadableDatabase();
        long upperLimit = time - 1000*2;
        String selectTaskQuery = String.format("SELECT * FROM %s WHERE %s < %d AND %s >= %d ORDER BY %s ", TABLE_TASK,TIME_FINISHED,time,TIME_FINISHED,upperLimit,TIME_FINISHED);
        Cursor c = db.rawQuery(selectTaskQuery,null);
        if(c.moveToFirst()) {
            do{
                Calendar cal = Calendar.getInstance();
                Calendar cal1 = Calendar.getInstance();

                Task task = new Task();
                task.setTaskName(c.getString(c.getColumnIndex(TASK_NAME)));
                task.setId(c.getInt(c.getColumnIndex(ID)));
                task.setCategoryId(c.getInt(c.getColumnIndex(CATEGORY_ID)));
                cal.setTimeInMillis(c.getLong(c.getColumnIndex(PERSONAL_DEADLINE)));
                task.setPersonalDeadline(cal);
                cal1.setTimeInMillis(c.getLong(c.getColumnIndex(ACTUAL_DEADLINE)));
                task.setActualDeadline(cal1);
                task.setRankPoint(c.getInt(c.getColumnIndex(RANK)));
                results.add(task);
            } while (c.moveToNext());
        }
        return results;
    }

    /**
     * Insert record to Total Score table after 7 days periods
     */
    public void updateWeeklyScore(int weeklyScore) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(WEEKLY_SCORE,weeklyScore);
        db.insert(TABLE_TOTAL_SCORE,null,value);
    }

    /**
     * Get weekly scores
     */
    public ArrayList<Integer> getWeeklyScore() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = String.format("SELECT * FROM %s ",TABLE_TOTAL_SCORE);
        Cursor c = db.rawQuery(selectQuery,null);
        if(c.moveToFirst()) {
            do{
                result.add(c.getInt(c.getColumnIndex(WEEKLY_SCORE)));
            } while (c.moveToNext());
        }
        return result;
    }

}
