package controller;

import java.util.ArrayList;

import db.DatabaseHelper;
import entity.Task;

/**
 * Created by Vu Anh Vinh on 9/3/2016.
 * This controller help calculate the final net point to display on UI. The net point
 */
public class PointCalculationController {
    private static PointCalculationController firstInstance = null;

    public static PointCalculationController getInstance() {
        if(firstInstance == null)
        {
            firstInstance = new PointCalculationController();
        }
        return firstInstance;
    }

    /**
     *
     * @param currentTime get Tasks finished within a week to calculate their total score
     * @return
     */
    public int getWeeklyScore(long currentTime, DatabaseHelper db) {
        try{
            ArrayList<Task> tasks = db.getTasksByTime(currentTime);
            int[] ranks = new int[tasks.size()];
            //FinishTaskController ftcInstance = FinishTaskController.getInstance();
            for(int i = 0; i<tasks.size();i++) {
                int rankPoint = tasks.get(i).getRankPoint();
                ranks[i] = rankPoint;
            }
            int result = calculate(ranks);
            db.updateWeeklyScore(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @param
     * @return total point of user
     */
    public int getTotalScore(DatabaseHelper db){
        int result = 0;
        ArrayList<Integer> weeklyScores = db.getWeeklyScore();
        if(!weeklyScores.isEmpty()) {
            for(int score: weeklyScores)
            {
                result += score;
            }
        }
        return result;
    }

    public int calculate(int[] a){
        int netpoint = 0;
        int streak = 1;
        int startPoint = 0;
        int startStreakRank = 0;
        for(int i = 0; i < a.length; i++ ) {
            int rank = a[i];
            switch (rank) {
                case 1:
                {
                    if(i==0)
                    {
                        netpoint = 25;
                        startPoint = 25;
                    }
                    else {
                        int prevRank = a[i-1];
                        switch(prevRank) {
                            case 1:
                            {
                                streak++;
                                int streakLimit = 2;
                                if (startStreakRank == 0 || startStreakRank == 4) streakLimit = 3;
                                if(streak <= streakLimit){ netpoint += startPoint;
                                } else if (startPoint > 15) {
                                    startPoint -= 2;
                                    netpoint += startPoint;
                                } else { netpoint += startPoint;
                                }
                                break;
                            }
                            case 2:
                            {
                                startStreakRank = 2;
                                startPoint = 19;
                                streak = 1;
                                netpoint += 19;
                                break;
                            }
                            case 3:
                            {
                                startStreakRank = 3;
                                startPoint = 21;
                                streak = 1;
                                netpoint += 21;
                                break;
                            }
                            case 4:
                            {
                                startStreakRank = 4;
                                startPoint = 25;
                                streak = 1;
                                netpoint += 25;
                                break;
                            }
                            default:
                        }
                    }
                    System.out.println(startPoint);
                    break;
                }
                case 2:
                {
                    if(i==0)
                    {
                        netpoint = 5;
                        startPoint = 5;
                    }
                    else {
                        int prevRank = a[i-1];
                        switch (prevRank) {
                            case 1:
                            {
                                startStreakRank = 1;
                                startPoint = -3;
                                streak = 1;
                                netpoint += -3;
                                break;
                            }
                            case 2:
                            {
                                int streakLimit = 2;
                                if( startStreakRank == 0 || startStreakRank == 1 || startStreakRank == 4 ) streakLimit = 3;
                                streak++;
                                if(streak <= streakLimit){ netpoint += startPoint;
                                } else if (startPoint > -5) {
                                    startPoint -= 2;
                                    netpoint += startPoint;
                                } else { netpoint += startPoint;
                                }
                                break;
                            }
                            case 3:
                            {
                                startStreakRank = 3;
                                startPoint = 3;
                                streak = 1;
                                netpoint += 3;
                                break;
                            }
                            case 4:
                            {
                                startStreakRank = 4;
                                startPoint = 25;
                                streak = 1;
                                netpoint += 25;
                                break;
                            }
                            default:
                        }
                    }
                    System.out.println(startPoint);
                    break;
                }
                case 3:
                {
                    if (i == 0)
                    {
                        netpoint = -25;
                        startPoint = -25;
                    }
                    else {
                        int prevRank = a[i-1];
                        switch(prevRank) {
                            case 1:
                            {
                                startStreakRank = 1;
                                startPoint = -31;
                                streak = 1;
                                netpoint += -31;
                                break;
                            }
                            case 2:
                            {
                                startStreakRank = 2;
                                startPoint = -29;
                                streak = 1;
                                netpoint += -29;
                                break;
                            }
                            case 3:
                            {
                                int streakLimit = 1;
                                if(startStreakRank == 4) streakLimit = 4;
                                streak++;
                                if(streak <= streakLimit ) { netpoint += startPoint;
                                    if (streak == 4) startPoint = -25;
                                } else if(startPoint > -35) {
                                    startPoint -= 2;
                                    netpoint += startPoint;
                                } else { netpoint += startPoint;
                                }
                                break;
                            }
                            case 4:
                            {
                                startStreakRank	= 4;
                                startPoint = 0;
                                streak = 1;
                                netpoint += 0;
                                break;
                            }
                            default:
                        }
                    }
                    System.out.println(startPoint);
                    break;
                }
                case 4:
                {
                    if (i == 0)
                    {
                        netpoint = -35;
                        startPoint = -35;
                    }
                    else {
                        int prevRank = a[i-1];
                        switch (prevRank) {
                            case 1:
                            {
                                startPoint = -41;
                                streak = 1;
                                netpoint += -41;
                                break;
                            }
                            case 2:
                            {
                                startPoint = -39;
                                streak = 1;
                                netpoint += -39;
                                break;
                            }
                            case 3:
                            {
                                startPoint = -37;
                                streak = 1;
                                netpoint += -37;
                                break;
                            }
                            case 4:
                            {
                                streak++;
                                if(startPoint > -45)
                                {
                                    startPoint -= 2;
                                    netpoint += startPoint;
                                } else { netpoint += startPoint;
                                }
                                break;
                            }
                            default:
                        }
                    }
                    System.out.println(startPoint);
                    break;
                }
                default:
                {
                    System.out.println("Invalid rank");
                    return 0;
                }
            }
        }
        return netpoint;
    }

}
