package com.ue.prakash.service;

import com.ue.prakash.exception.NoDataFoundException;
import com.ue.prakash.pojo.dto.response.ActivityReportResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface ActivityTrackerService {
    ActivityReportResponse getActivityReport() throws IOException, NoDataFoundException;

    
}
