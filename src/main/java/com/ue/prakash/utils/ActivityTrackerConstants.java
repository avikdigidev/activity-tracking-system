package com.ue.prakash.utils;


public class ActivityTrackerConstants {

    private ActivityTrackerConstants() {

    }
    public static final String ACTIVITY_TRACKER_TABLE = "activity_tracker";

    public static final String ACTIVITY_STATS_BY_ACTIVITY_DATE_QUERY = "select distinct new com.ue.prakash.pojo.dto.response.MonthlyActivity(a.activityName,COUNT(*) as total) from ActivityTracker a where a.activityDate >=  :endDate and  a.activityDate <= :startDate GROUP BY activity_name ORDER BY total DESC";
    public static final String ACTIVITY_STATS_BY_ACTIVITY_DATE_AND_NAME_QUERY = "select distinct new com.ue.prakash.pojo.dto.TwoDayActivityDb(a.activityName, COUNT(*) as total) from ActivityTracker a where a.activityDate = :startDate GROUP BY activity_name";

}