package controller;

import db.DatabaseHelper;
import entity.Category;

/**
 * Created by Vu Anh Vinh on 17/2/2016.
 */
public class CategoryController {
    private static CategoryController firstInstance;

    public static CategoryController getInstance(){
        if(firstInstance == null)
        {
            firstInstance = new CategoryController();
        }
        return firstInstance;
    }
    /**
     *
     * @param catName name of category
     * description of category
     * @param parentCategory direct parent (one level above)
     * @return
     */
    public boolean createCategory (String catName,
                                   Category parentCategory, DatabaseHelper db ) {
        try {
            Category cat = new Category(catName,
                    parentCategory );
            if(parentCategory != null) {
                cat.setParentId(parentCategory.getId());
            }
            db.createCategory(cat);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Can not create object");
            return false;
        }
    }

}
