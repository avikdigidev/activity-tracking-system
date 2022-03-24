package com.userexperior.prakash.controller;

import com.userexperior.prakash.pojo.dto.response.ActivityReport;
import com.userexperior.prakash.exception.InternalServerErrorException;
import com.userexperior.prakash.exception.ResourceNotFoundException;
import com.userexperior.prakash.service.ActivityTrackerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;

@RestController
public class ActivityController {
    private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    private ActivityTrackerService activityTrackerService;


    @GetMapping("/report")
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
