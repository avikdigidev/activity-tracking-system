package com.ue.prakash.config;

import com.ue.prakash.pojo.entity.ActivityTracker;
import org.springframework.batch.item.ItemProcessor;

public class ActivityTrackerProcessor implements ItemProcessor<ActivityTracker, ActivityTracker> {
    @Override
    public ActivityTracker process(ActivityTracker activityTracker) throws Exception {
        return activityTracker;
    }
}
