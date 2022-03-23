
package com.userexperior.prakash.repository;

import com.userexperior.prakash.pojo.dto.response.MonthlyActivity;
import com.userexperior.prakash.pojo.entity.ActivityTracker;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ActivityTrackerRepository extends CrudRepository<ActivityTracker, Long> {



    @Query("select distinct new com.userexperior.prakash.pojo.dto.response.MonthlyActivity(a.activityName,COUNT(*) as total) from ActivityTracker a where a.activityDate >=  :endDate and  a.activityDate <= :startDate GROUP BY activity_name ORDER BY total DESC")
    List<MonthlyActivity> getActivityStatsByActivityDate(
            @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
