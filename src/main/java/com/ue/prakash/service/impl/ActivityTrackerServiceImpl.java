package com.ue.prakash.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.LongSerializationPolicy;
import com.ue.prakash.exception.ActivityTrackerException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ActivityTrackerServiceImpl implements ActivityTrackerService {
    private static final Logger logger = LoggerFactory.getLogger(ActivityTrackerServiceImpl.class);
    @Autowired
    ActivityTrackerRepository activityTrackerRepository;
    @Value("${file.path}")
    private String filePath;
    @Value("${base.module.target.activities}")
    private List<String> activityNames ;
    @Value("${base.module.activities.status}")
    private List<String> status;


    @Override
    public ActivityReportResponse getActivityReport() throws NoDataFoundException {
        ActivityReportResponse activityReportResponse = new ActivityReportResponse();
        List<ActivityTrackerJSONDTO> activityTrackerJSONDTOList = readJSONFiles();
        saveValidDataToDB(activityTrackerJSONDTOList);
        activityReportResponse.setMonthlyActivity(getMonthlyStats());
        List<TwoDayActivity> as = getTodayVsYesterdayStats();
        activityReportResponse.setTwoDayActivity(as);
        return activityReportResponse;
    }

    private List<TwoDayActivity> getTodayVsYesterdayStats() {
        Date today = new Date(System.currentTimeMillis());
        int numDays = -1;
        Date yesterday = getDateFromToday(today, numDays);
        logger.info("Inside class-name:[{}] method-name:[{}] msg:[{}] [{}] - [{}]", "ActivityTrackerServiceImpl",
                "getTodayVsYesterdayStats", "Get Yesterday vs Today stats for the dates : ",today,yesterday);
        List<TwoDayActivity> twoDayActivityList = new ArrayList<>();
        //using builder pattern creating objects for all the possible activities with default values

        TwoDayActivity anrActivity = TwoDayActivity.builder().name(activityNames.get(0)).yesterdayOccurrence(0L).todayOccurrence(0L).build();
        TwoDayActivity crashActivity = TwoDayActivity.builder().name(activityNames.get(1)).yesterdayOccurrence(0L).todayOccurrence(0L).build();
        TwoDayActivity doubleTapActivity = TwoDayActivity.builder().name(activityNames.get(2)).yesterdayOccurrence(0L).todayOccurrence(0L).build();
        TwoDayActivity singleTapActivity = TwoDayActivity.builder().name(activityNames.get(3)).yesterdayOccurrence(0L).todayOccurrence(0L).build();


        logger.info("Inside class-name:[{}] method-name:[{}] msg:[{}] [{}]", "ActivityTrackerServiceImpl",
                "getTodayVsYesterdayStats", "Fetching Yesterday stats for the date : ",yesterday);
        List<TwoDayActivityDb> yesterdayActivityList = activityTrackerRepository.getActivityStatsByActivityNameAndDate(yesterday);
        logger.info("Inside class-name:[{}] method-name:[{}] msg:[{}] [{}]", "ActivityTrackerServiceImpl",
                "getTodayVsYesterdayStats", "Fetching Today stats for the date : ",today);
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

    private void setTodayOccurrences(TwoDayActivity doubleTapActivity, TwoDayActivity singleTapActivity, TwoDayActivity crashActivity, TwoDayActivity anrActivity, TwoDayActivityDb todayActivity) {
        if (Objects.equals(todayActivity.getActivityName(), doubleTapActivity.getName())) {
            doubleTapActivity.setTodayOccurrence(todayActivity.getOccurrences());
        } else if (Objects.equals(todayActivity.getActivityName(), singleTapActivity.getName())) {
            singleTapActivity.setTodayOccurrence(todayActivity.getOccurrences());
        } else if (Objects.equals(todayActivity.getActivityName(), crashActivity.getName())) {
            crashActivity.setTodayOccurrence(todayActivity.getOccurrences());
        } else if (Objects.equals(todayActivity.getActivityName(), anrActivity.getName())) {
            anrActivity.setTodayOccurrence(todayActivity.getOccurrences());
        }
    }

    private void setYesterdayOccurrences(TwoDayActivity doubleTapActivity, TwoDayActivity singleTapActivity, TwoDayActivity crashActivity, TwoDayActivity anrActivity, TwoDayActivityDb yesterdayActivity) {
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

    private void setActivityStatus(TwoDayActivity activity) {
        long y = activity.getYesterdayOccurrence();
        long x = activity.getTodayOccurrence();
        if (x > y) {
            activity.setStatus(status.get(0));
        } else if (x < y) {
            activity.setStatus(status.get(1));
        } else {
            activity.setStatus(status.get(2));
        }

    }

    private List<MonthlyActivity> getMonthlyStats() {
        Date today = new Date(System.currentTimeMillis());
        int numDays = -30;
        Date lastMonthDate = getDateFromToday(today, numDays);
        logger.info("Inside class-name:[{}] method-name:[{}] msg:[{}] [{}] - [{}]", "ActivityTrackerServiceImpl",
                "getMonthlyStats", "Get monthly stats for the date range : ",today,lastMonthDate);
        return activityTrackerRepository.getActivityStatsByActivityDate(today, lastMonthDate);
    }

    private Date getDateFromToday(Date today, int numDays) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, numDays);
        return new Date(cal.getTime().getTime());

    }

    private void saveValidDataToDB(List<ActivityTrackerJSONDTO> activityTrackerJSONDTOList) {
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
        logger.info("Inside class-name:[{}] method-name:[{}] msg:[{}]", "ActivityTrackerServiceImpl",
                "saveValidDataToDB", "Successfully saved valid records to DB");
        activityTrackerRepository.saveAll(activityTrackers);
    }

    private List<ActivityTrackerJSONDTO> readJSONFiles() throws NoDataFoundException {
        List<ActivityTrackerJSONDTO> activityTrackerJSONDTOList = new ArrayList<>();
        Gson gson = new GsonBuilder().setLongSerializationPolicy(LongSerializationPolicy.STRING).create();
        Path folder = Paths.get(filePath);
        logger.info("Inside class-name:[{}] method-name:[{}] msg:[{}] > [{}]", "ActivityTrackerServiceImpl",
                "readJSONFiles", "Reading files from folder-- ",folder.toString());
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder)) {
            for (Path entry : stream) {
                String file = folder + "\\" + entry.getFileName().toString();
                ActivityTrackerJSONDTO activityDto = gson.fromJson(new FileReader(ResourceUtils.getFile(file)), ActivityTrackerJSONDTO.class);
                removeInvalidActivities(activityDto);
                logger.info("Inside class-name:[{}] method-name:[{}] msg:[{}] > [{}]", "ActivityTrackerServiceImpl",
                        "readJSONFiles", "File processed and added to list-- ",file);
                activityTrackerJSONDTOList.add(activityDto);
            }
        }  catch (JsonSyntaxException e) {
            throw new ActivityTrackerException(ResponseMessages.CONFLICT.getCustomErrorMessage());

        }catch (IOException e) {
            throw new NoDataFoundException(ResponseMessages.NOT_FOUND.getCustomErrorMessage());

        }
        return activityTrackerJSONDTOList;
    }

    private void removeInvalidActivities(ActivityTrackerJSONDTO activityDto) {
        List<Activity> activities = activityDto.getActivities();
        activities = activities.stream().filter(activity -> Arrays.asList(activityNames).contains(activity.getName())).collect(Collectors.toList());
        activityDto.setActivities(activities);
    }
}
