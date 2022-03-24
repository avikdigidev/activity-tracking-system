package com.userexperior.prakash.service;

import com.userexperior.prakash.pojo.dto.response.ActivityReport;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface ActivityTrackerService {
    ActivityReport getActivityReport() throws IOException, Exception;
}
