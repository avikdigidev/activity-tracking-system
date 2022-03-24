package com.ue.prakash.controller;

import com.ue.prakash.pojo.dto.response.ActivityReport;
import com.ue.prakash.service.ActivityTrackerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ActivityController {
    private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    private ActivityTrackerService activityTrackerService;


    @GetMapping("/report")
    //TODO add api responses for error code and message like previous service
    public ActivityReport getActivityReport() {

        ActivityReport activityReport = null;
        try {
            activityReport = activityTrackerService.getActivityReport();
        } catch (Exception e) {
            e.printStackTrace();
        }


        logger.info("Inside class-name:[{}] method-name:[{}] type:[{}] msg:[{}]", "ActivityController",
                "getActivityReport", "GET", "Request served");
        return activityReport;
    }

}
