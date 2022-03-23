package com.userexperior.prakash.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.userexperior.prakash.pojo.dto.request.Activity;
import com.userexperior.prakash.pojo.dto.request.ActivityTrackerDTO;
import com.userexperior.prakash.pojo.dto.response.ActivityReport;
import com.userexperior.prakash.pojo.dto.response.MonthlyActivity;
import com.userexperior.prakash.pojo.entity.ActivityTracker;
import com.userexperior.prakash.repository.ActivityTrackerRepository;
import com.userexperior.prakash.service.ActivityTrackerService;
import com.userexperior.prakash.utils.ActivityTrackerConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ActivityTrackerServiceImpl implements ActivityTrackerService {

    @Autowired
    ActivityTrackerRepository activityTrackerRepository;
    @Value("${file.path}")
    private String filePath;

    private String[] activityNames = {ActivityTrackerConstants.DOUBLE_TAP, ActivityTrackerConstants.SINGLE_TAP, ActivityTrackerConstants.CRASH, ActivityTrackerConstants.ANR};

    @Override
    public ActivityReport getActivityReport() throws IOException {
        ActivityReport activityReport = new ActivityReport();
        List<ActivityTrackerDTO> activityTrackerDTOList = readJSONFiles();
        saveValidDataToDB(activityTrackerDTOList);
        activityTrackerDTOList = null; //no use of this list so explicitly setting it to null for GC
        List<MonthlyActivity> ls = getMonthlyStats();
        System.out.println(ls);
        activityReport.setMonthlyActivity(ls);

        return activityReport;
    }

    private List<MonthlyActivity> getMonthlyStats() {
        List<MonthlyActivity> monthlyActivityStats = new ArrayList<>();
        Date today = new Date(System.currentTimeMillis());
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, -30);
        Date lastMonthDate = new Date(cal.getTime().getTime());
        List<ActivityTracker> list = activityTrackerRepository.getActivityStats(today.toString(), lastMonthDate.toString());
        List<ActivityTracker> list2 = activityTrackerRepository.getActivityStatsByActivityDate(today, lastMonthDate);
        System.out.println("*****************************************");
        System.out.println(list);
        System.out.println(list2);
        System.out.println("*****************************************");
        int countA = 0, countB = 0, countC = 0, countD = 0;
        for (ActivityTracker activityTracker : list) {
            MonthlyActivity monthlyActivity = new MonthlyActivity();
            monthlyActivityStats.add(monthlyActivity);
        }
        return monthlyActivityStats;
    }

    private void saveValidDataToDB(List<ActivityTrackerDTO> activityTrackerDTOList) {
        List<ActivityTracker> activityTrackers = new ArrayList<>();
        for (ActivityTrackerDTO activityDto : activityTrackerDTOList) {
            for (Activity activity : activityDto.getActivities()) {
                ActivityTracker activityTracker = new ActivityTracker();
                activityTracker.setUniqueId(activityDto.getUnique_id());
                activityTracker.setActivityName(activity.getName());
                activityTracker.setStartTime(activity.getTime());
                activityTracker.setActivityDuration(activity.getDuration());
                activityTracker.setActivityDate(new Date(activity.getTime()));
                activityTrackers.add(activityTracker);
            }
        }
        activityTrackerRepository.saveAll(activityTrackers);
    }

    private List<ActivityTrackerDTO> readJSONFiles() throws IOException {
        List<ActivityTrackerDTO> activityTrackerDTOList = new ArrayList<>();
        Gson gson = new GsonBuilder().setLongSerializationPolicy(LongSerializationPolicy.STRING).create();
        Path folder = Paths.get(filePath);
        DirectoryStream<Path> stream = Files.newDirectoryStream(folder);
        for (Path entry : stream) {
            String file = folder + "\\" + entry.getFileName().toString();
            ActivityTrackerDTO activityDto = gson.fromJson(new FileReader(ResourceUtils.getFile(file)), ActivityTrackerDTO.class);
            removeInvalidActivities(activityDto);
            activityTrackerDTOList.add(activityDto);
        }
        return activityTrackerDTOList;
    }

    private void removeInvalidActivities(ActivityTrackerDTO activityDto) {
        List<Activity> activities = activityDto.getActivities();
        activities = activities.stream().filter(activity -> Arrays.asList(activityNames).contains(activity.getName())).collect(Collectors.toList());
        activityDto.setActivities(activities);
    }
}
