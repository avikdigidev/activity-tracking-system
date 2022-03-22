
package com.userexperior.prakash.repository;

import com.userexperior.prakash.entity.ActivityTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityTrackerRepository extends JpaRepository<ActivityTracker, Long> {


    @Query(nativeQuery = true, value = "select a.id,a.activity_name,a.date from activity_tracker a where date between ?1 and ?2")
    List<ActivityTracker> getActivityStats(String startDate, String endDate);


}
