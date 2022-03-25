package com.ue.prakash.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.ue.prakash.exception.NoDataFoundException;
import com.ue.prakash.exception.response.ResponseMessages;
import com.ue.prakash.pojo.dto.TwoDayActivityDb;
import com.ue.prakash.pojo.dto.request.Activity;
import com.ue.prakash.pojo.dto.request.ActivityTrackerJSONDTO;
import com.ue.prakash.pojo.dto.response.ActivityReportResponse;
import com.ue.prakash.pojo.dto.response.MonthlyActivity;
import com.ue.prakash.pojo.dto.response.TwoDayActivity;
import com.ue.prakash.pojo.entity.ActivityTracker;
import com.ue.prakash.repository.ActivityTrackerRepository;
import com.ue.prakash.service.ActivityTrackerService;
import com.ue.prakash.utils.ActivityTrackerConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ActivityTrackerServiceImpl implements ActivityTrackerService {

    @Autowired
    ActivityTrackerRepository activityTrackerRepository;
    @Value("${file.path}")
    private String filePath;

    private final String[] activityNames = {ActivityTrackerConstants.DOUBLE_TAP, ActivityTrackerConstants.SINGLE_TAP, ActivityTrackerConstants.CRASH, ActivityTrackerConstants.ANR};

    @Override
    public ActivityReportResponse getActivityReport() throws Exception {
        ActivityReportResponse activityReportResponse = new ActivityReportResponse();
        List<ActivityTrackerJSONDTO> activityTrackerJSONDTOList = readJSONFiles();
        saveValidDataToDB(activityTrackerJSONDTOList);
        activityReportResponse.setMonthlyActivity(getMonthlyStats());
        List<TwoDayActivity> as = getTodayVsYesterdayStats();
        activityReportResponse.setTwoDayActivity(as);
        return activityReportResponse;
    }

    public List<TwoDayActivity> getTodayVsYesterdayStats() {
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
            setYesterdayOccurrences(doubleTapActivity, singleTapActivity, crashActivity, anrActivity, yesterdayActivity);
        }
        for (TwoDayActivityDb todayActivity : todayActivityList) {
            setTodayOccurrences(doubleTapActivity, singleTapActivity, crashActivity, anrActivity, todayActivity);
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

    public void setTodayOccurrences(TwoDayActivity doubleTapActivity, TwoDayActivity singleTapActivity, TwoDayActivity crashActivity, TwoDayActivity anrActivity, TwoDayActivityDb todayActivity) {
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

    public void setYesterdayOccurrences(TwoDayActivity doubleTapActivity, TwoDayActivity singleTapActivity, TwoDayActivity crashActivity, TwoDayActivity anrActivity, TwoDayActivityDb yesterdayActivity) {
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

    public void setActivityStatus(TwoDayActivity activity) {
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

    public List<MonthlyActivity> getMonthlyStats() {
        Date today = new Date(System.currentTimeMillis());
        int numDays = -30;
        Date lastMonthDate = getDateFromToday(today, numDays);

        return activityTrackerRepository.getActivityStatsByActivityDate(today, lastMonthDate);
    }

    public Date getDateFromToday(Date today, int numDays) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, numDays);
        return new Date(cal.getTime().getTime());

    }

    public void saveValidDataToDB(List<ActivityTrackerJSONDTO> activityTrackerJSONDTOList) {
        List<ActivityTracker> activityTrackers = new ArrayList<>();
        for (ActivityTrackerJSONDTO activityDto : activityTrackerJSONDTOList) {
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

    public List<ActivityTrackerJSONDTO> readJSONFiles() throws NoDataFoundException {
        List<ActivityTrackerJSONDTO> activityTrackerJSONDTOList = new ArrayList<>();
        Gson gson = new GsonBuilder().setLongSerializationPolicy(LongSerializationPolicy.STRING).create();
        Path folder = Paths.get(filePath);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder)) {

            for (Path entry : stream) {
                String file = folder + "\\" + entry.getFileName().toString();
                ActivityTrackerJSONDTO activityDto = null;
                activityDto = gson.fromJson(new FileReader(ResourceUtils.getFile(file)), ActivityTrackerJSONDTO.class);
                removeInvalidActivities(activityDto);
                activityTrackerJSONDTOList.add(activityDto);
            }
        } catch (IOException e) {
            throw new NoDataFoundException(ResponseMessages.NOT_FOUND.getCustomErrorMessage());

        }

        return activityTrackerJSONDTOList;
    }

    public void removeInvalidActivities(ActivityTrackerJSONDTO activityDto) {
        List<Activity> activities = activityDto.getActivities();
        activities = activities.stream().filter(activity -> Arrays.asList(activityNames).contains(activity.getName())).collect(Collectors.toList());
        activityDto.setActivities(activities);
    }
}
