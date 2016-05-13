package entity;

/**
 * Created by Vu Anh Vinh on 16/2/2016.
 */
import java.util.Calendar;


public class Task {
    private String taskName;
    private Calendar actualDeadline;
    private Calendar personalDeadline;

    public int getRankPoint() {
        return rankPoint;
    }

    public void setRankPoint(int rankPoint) {
        this.rankPoint = rankPoint;
    }

    private int rankPoint;

    public long getReminderFrequency() {
        return reminderFrequency;
    }

    public void setReminderFrequency(long reminderFrequency) {
        this.reminderFrequency = reminderFrequency;
    }

    private long reminderFrequency;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    private long categoryId;


    public Calendar getPersonalDeadline() {
        return personalDeadline;
    }

    public void setPersonalDeadline(Calendar personalDeadline) {
        this.personalDeadline = personalDeadline;
    }

    private Category category;
    private boolean enableReminder;
    private Calendar endTime;


    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public Task() {}

    public Task(String taskName, Calendar deadline, Calendar personalDeadline,
                Category category) {
        super();
        this.personalDeadline = personalDeadline;
        this.taskName = taskName;
        this.actualDeadline = deadline;
        this.category = category;
    }

    /**
     * @return the task's name
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * @param taskName the task's name to set
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * @return the deadline
     */
    public Calendar getActualDeadline() {
        return actualDeadline;
    }

    /**
     * @param deadline the deadline to set
     */
    public void setActualDeadline(Calendar deadline) {
        this.actualDeadline = deadline;
    }

    /**
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(Category category) {
        this.category = category;
    }


    /**
     * @return the enableReminder
     */
    public boolean isEnableReminder() {
        return enableReminder;
    }

    /**
     * @param enableReminder the enableReminder to set
     */
    public void setEnableReminder(boolean enableReminder) {
        this.enableReminder = enableReminder;
    }


    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        Task t = (Task) obj;
        return (this.taskName == t.taskName && this.category == t.category);
    }
}
