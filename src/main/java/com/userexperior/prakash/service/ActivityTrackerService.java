package com.userexperior.prakash.service;

import com.userexperior.prakash.pojo.dto.response.ActivityReport;
import org.springframework.stereotype.Service;

@Service
public interface ActivityTrackerService {
    ActivityReport getActivityReport();
}
