
package com.ue.prakash.repository;

import com.ue.prakash.pojo.dto.TwoDayActivityDb;
import com.ue.prakash.pojo.dto.response.MonthlyActivity;
import com.ue.prakash.pojo.entity.ActivityTracker;
import com.ue.prakash.utils.ActivityTrackerConstants;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ActivityTrackerRepository extends CrudRepository<ActivityTracker, Long> {

    @Query(ActivityTrackerConstants.ACTIVITY_STATS_BY_ACTIVITY_DATE_QUERY)
    List<MonthlyActivity> getActivityStatsByActivityDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(ActivityTrackerConstants.ACTIVITY_STATS_BY_ACTIVITY_DATE_AND_NAME_QUERY)
    List<TwoDayActivityDb> getActivityStatsByActivityNameAndDate(@Param("startDate") Date startDate);
}
