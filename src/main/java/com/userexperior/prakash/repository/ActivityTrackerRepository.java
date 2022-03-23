
package com.userexperior.prakash.repository;

import com.userexperior.prakash.pojo.entity.ActivityTracker;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ActivityTrackerRepository extends CrudRepository<ActivityTracker, Long> {


    @Query(nativeQuery = true, value = "select activity_name from activity_tracker where activity_date >= ?1 and activity_date <= ?2")
    List<ActivityTracker> getActivityStats(String startDate, String endDate);


    @Query("select a from ActivityTracker a where a.activityDate >=  :endDate and  a.activityDate <= :startDate")
    List<ActivityTracker> getActivityStatsByActivityDate(
            @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
