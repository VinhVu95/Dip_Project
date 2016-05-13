package entity;

/**
 * Created by Vu Anh Vinh on 16/2/2016.
 */
import java.util.ArrayList;
import java.util.List;


public class Category {
    private String catName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    private long parentId;
    private String description;
    private Category parentCategory;
    private List<Category> childrenCategory;
    private List<Task> tasks;
    public Category(){
        childrenCategory = new ArrayList<Category>();
    }
    public Category(String catName,
                    Category parentCategory ) {
        super();
        tasks = new ArrayList<Task>();
        childrenCategory = new ArrayList<Category>();
        setCatName(catName);
        //setDescription(description);
        setParentCategory(parentCategory);
    }
    /**
     * @return the catName
     */
    public String getCatName() {
        return catName;
    }
    /**
     * @param catName the catName to set
     */
    public void setCatName(String catName) {
        this.catName = catName;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return the parentCategory
     */
    public Category getParentCategory() {
        return parentCategory;
    }
    /**
     * @param parentCategory the parentCategory to set
     */
    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }
    /**
     * @return the childrenCategory
     */
    public List<Category> getChildrenCategory() {
        return childrenCategory;
    }
    /**
     * @param childrenCategory the childrenCategory to set
     */
    public void setChildrenCategory(List<Category> childrenCategory) {
        this.childrenCategory = childrenCategory;
    }
    /**
     * @return the tasks
     */
    public List<Task> getTasks() {
        return tasks;
    }
    /**
     * @param tasks the tasks to set
     */
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        Category c = (Category) obj;
        return this.catName == c.catName;
    }


}

