package com.ue.prakash.pojo.entity;

import com.ue.prakash.utils.ActivityTrackerConstants;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = ActivityTrackerConstants.ACTIVITY_TRACKER_TABLE)
@ToString
public class ActivityTracker {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "unique_id", length = 10, nullable = false)
    private Long uniqueId;

    @Column(name = "activity_name", length = 10, nullable = false)
    private String activityName;

    @Column(name = "start_time", nullable = false)
    private Long startTime;

    @Column(name = "activity_duration", nullable = false)
    private int activityDuration;

    @Column(name = "activity_date")
    private Date activityDate;
}
