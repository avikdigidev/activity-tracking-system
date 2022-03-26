package com.ue.prakash.controller;

import com.ue.prakash.exception.ActivityTrackerException;
import com.ue.prakash.exception.NoDataFoundException;
import com.ue.prakash.exception.response.ResponseMessages;
import com.ue.prakash.pojo.dto.response.ActivityReportResponse;
import com.ue.prakash.service.ActivityTrackerService;
import com.ue.prakash.utils.ActivityTrackerConstants;
import com.ue.prakash.utils.HttpResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class ActivityController {
    private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    private ActivityTrackerService activityTrackerService;


    @GetMapping(value = "${report.url}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActivityReportResponse> getActivityReport() throws NoDataFoundException {

        ActivityReportResponse activityReportResponse = null;
        try {
            activityReportResponse = activityTrackerService.getActivityReport();
        } catch (NoDataFoundException e) {
            /*
            In applications where the accepted practice is to log an Exception and then rethrow it,
            you end up with miles-long logs that contain multiple instances of the same exception.
            In multithreaded applications debugging this type of log can be particularly hellish
            because messages from other threads will be interwoven with the repetitions of the logged-and-thrown Exception.
            Instead, exceptions should be either logged or rethrown, not both.
            */
            //logger.error(Arrays.asList(e.getStackTrace()).toString());
            throw new NoDataFoundException(e.getMessage());
        }catch (Exception e) {
            throw new ActivityTrackerException(e.getMessage(),ActivityTrackerConstants.ACTIVITY_TRACKER_ERROR_CODE, ActivityTrackerConstants.ACTIVITY_TRACKER_ERROR_MESSAGE);
        }


        logger.info("Inside class-name:[{}] method-name:[{}] type:[{}] msg:[{}]", "ActivityController",
                "getActivityReport", "GET", "Request served");
        return HttpResponseUtils.getResponse(HttpStatus.OK, ResponseMessages.OK.getCustomErrorMessage(),
                activityReportResponse);
    }

}
