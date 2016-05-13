package controller;

import com.ntu.smartlearning.myapplication.MainActivity;

import java.util.Observable;

/**
 * Created by Vu Anh Vinh on 6/4/2016.
 */
public class UpdateScoreController extends Observable {
    private static UpdateScoreController firstInstance = null;
    private String score;

    public static UpdateScoreController getInstance() {
        if(firstInstance == null)
        {
            firstInstance = new UpdateScoreController();
        }
        return firstInstance;
    }

    /*
     * get data from subject
     */
    public String getScore(){
        return score;
    }

    /**
     *  set data then notify change for observers
     * @param score
     */
    public void setScore(String score) {
        this.score = score;
        setChanged();
        notifyObservers();
    }
}
