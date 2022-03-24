package com.userexperior.prakash.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.userexperior.prakash.pojo.dto.TwoDayActivityDb;
import com.userexperior.prakash.pojo.dto.request.Activity;
import com.userexperior.prakash.pojo.dto.request.ActivityTrackerDTO;
import com.userexperior.prakash.pojo.dto.response.ActivityReport;
import com.userexperior.prakash.pojo.dto.response.MonthlyActivity;
import com.userexperior.prakash.pojo.dto.response.TwoDayActivity;
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
    public ActivityReport getActivityReport() throws Exception {
        ActivityReport activityReport = new ActivityReport();
        List<ActivityTrackerDTO> activityTrackerDTOList = readJSONFiles();
        saveValidDataToDB(activityTrackerDTOList);
        activityTrackerDTOList = null; //no use of this list so explicitly setting it to null for GC
        activityReport.setMonthlyActivity(getMonthlyStats());
        List<TwoDayActivity> as = getTodayVsYesterdayStats();
        activityReport.setTwoDayActivity(as);
        return activityReport;
    }

    private List<TwoDayActivity> getTodayVsYesterdayStats() {
        Date today = new Date(System.currentTimeMillis());
        int numDays = -1;
        Date yesterday = getDateFromToday(today, numDays);

        List<TwoDayActivity> twoDayActivityList = new ArrayList<>();
        TwoDayActivity doubleTapActivity = new TwoDayActivity();
        doubleTapActivity.setName(activityNames[0]);
        TwoDayActivity singleTapActivity = new TwoDayActivity();
        singleTapActivity.setName(activityNames[1]);
        TwoDayActivity crashActivity = new TwoDayActivity();
        crashActivity.setName(activityNames[2]);
        TwoDayActivity anrActivity = new TwoDayActivity();
        anrActivity.setName(activityNames[3]);

        List<TwoDayActivityDb> yesterdayActivityList = activityTrackerRepository.getActivityStatsByActivityNameAndDate(yesterday);
        List<TwoDayActivityDb> todayActivityList = activityTrackerRepository.getActivityStatsByActivityNameAndDate(today);
        for (TwoDayActivityDb yesterdayActivity : yesterdayActivityList) {
            if (Objects.equals(yesterdayActivity.getActivityName(), doubleTapActivity.getName())) {
                doubleTapActivity.setYesterdayOccurrence(yesterdayActivity.getOccurrences());
            } else if (Objects.equals(yesterdayActivity.getActivityName(), singleTapActivity.getName())) {
                singleTapActivity.setYesterdayOccurrence(yesterdayActivity.getOccurrences());
            } else if (Objects.equals(yesterdayActivity.getActivityName(), crashActivity.getName())) {
                crashActivity.setYesterdayOccurrence(yesterdayActivity.getOccurrences());
            } else if (Objects.equals(yesterdayActivity.getActivityName(), anrActivity.getName())) {
                anrActivity.setYesterdayOccurrence(yesterdayActivity.getOccurrences());
            }
        }
        for (TwoDayActivityDb todayActivity : todayActivityList) {
            if (Objects.equals(todayActivity.getActivityName(), doubleTapActivity.getName())) {
                doubleTapActivity.setYesterdayOccurrence((todayActivity.getOccurrences() == null) ? 0 : todayActivity.getOccurrences());
            } else if (Objects.equals(todayActivity.getActivityName(), singleTapActivity.getName())) {
                singleTapActivity.setYesterdayOccurrence((todayActivity.getOccurrences() == null) ? 0 : todayActivity.getOccurrences());
            } else if (Objects.equals(todayActivity.getActivityName(), crashActivity.getName())) {
                crashActivity.setYesterdayOccurrence((todayActivity.getOccurrences() == null) ? 0 : todayActivity.getOccurrences());
            } else if (Objects.equals(todayActivity.getActivityName(), anrActivity.getName())) {
                anrActivity.setYesterdayOccurrence((todayActivity.getOccurrences() == null) ? 0 : todayActivity.getOccurrences());
            }
        }
        setActivityStatus(doubleTapActivity);
        setActivityStatus(singleTapActivity);
        setActivityStatus(crashActivity);
        setActivityStatus(anrActivity);
        twoDayActivityList.add(doubleTapActivity);
        twoDayActivityList.add(singleTapActivity);
        twoDayActivityList.add(crashActivity);
        twoDayActivityList.add(anrActivity);
        return twoDayActivityList;
    }

    private void setActivityStatus(TwoDayActivity activity) {
        long y = activity.getYesterdayOccurrence();
        long x = activity.getTodayOccurrence();
        if (x > y) {
            activity.setStatus(ActivityTrackerConstants.POSITIVE);
        } else if (x < y) {
            activity.setStatus(ActivityTrackerConstants.NEGATIVE);
        } else {
            activity.setStatus(ActivityTrackerConstants.UNALTERED);
        }

    }

    private List<MonthlyActivity> getMonthlyStats() {
        Date today = new Date(System.currentTimeMillis());
        int numDays = -30;
        Date lastMonthDate = getDateFromToday(today, numDays);
        List<MonthlyActivity> monthlyActivityStats = activityTrackerRepository.getActivityStatsByActivityDate(today, lastMonthDate);

        return monthlyActivityStats;
    }

    private Date getDateFromToday(Date today, int numDays) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, numDays);
        return new Date(cal.getTime().getTime());

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
