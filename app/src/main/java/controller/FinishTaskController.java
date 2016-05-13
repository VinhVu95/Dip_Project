package controller;

/**
 * Created by Vu Anh Vinh on 17/2/2016.
 */
import java.text.SimpleDateFormat;
import java.util.Calendar;

import db.DatabaseHelper;
import entity.Task;

public class FinishTaskController {
    private static FinishTaskController firstInstance = null;

    public static FinishTaskController getInstance(){
        if(firstInstance == null)
        {
            firstInstance = new FinishTaskController();
        }
        return  firstInstance;
    }
    /**
     *
     * task to finish
     * @return wasted time not doing the task
     */
    public int finishTask(long taskId,DatabaseHelper db){
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(cal.getTime());
            db.updateTaskFinish(taskId, cal.getTimeInMillis());
            Task updateTask = db.getTaskById(taskId);
            //Task information
            /*SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            System.out.println(format.format(updateTask.getEndTime().getTime()));
            System.out.println(updateTask.getTaskName());
            System.out.println(format.format(updateTask.getPersonalDeadline().getTime()));*/
            int rank = getRankPoint(updateTask);
            db.updateTaskRank(taskId,rank);
            System.out.println("Rank: " + Integer.toString(rank));

            return rank;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @param task needed to calculate procastinated time
     * @return wasted time
     */
    public long getProcastinatedTime(Task task) {
        long pTime = 0;
        try {
            Calendar endTime = task.getEndTime();
            Calendar personalTime = task.getPersonalDeadline();
            if(endTime.compareTo(personalTime) <= 0)
                return pTime;
            else
                return endTime.getTimeInMillis()-personalTime.getTimeInMillis();
        } catch (NullPointerException ex)
        {
            ex.printStackTrace();
            System.out.println("Neither endTime or personalTime has not been set");
            return 0;
        }
    }

    /**
     * TODO: get the percentage of procastination time over (deadline-personal deadline)and decide the category
     */
    public int getRankPoint(Task task) {
        long pTime = getProcastinatedTime(task);
        float percentage =  (((float) pTime)/
                (task.getActualDeadline().getTimeInMillis()-task.getPersonalDeadline().getTimeInMillis())) * 100 ;
        System.out.println("percentage = " + percentage);
        if (percentage <25) {
            task.setRankPoint(1);
        } else if (percentage < 75) {
            task.setRankPoint(2);
        } else if (percentage <100) {
            task.setRankPoint(3);
        } else {
            task.setRankPoint(4);
        }

        return task.getRankPoint();
    }
}

