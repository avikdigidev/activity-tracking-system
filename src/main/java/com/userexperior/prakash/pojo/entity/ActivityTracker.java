package com.userexperior.prakash.pojo.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "activity_tracker")
public class ActivityTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "activity_name",length = 10,nullable = false)
    private String activityName;

    @Column(name = "start_time",nullable = false)
    private Long startTime;

    @Column(name = "activity_duration",nullable = false)
    private int activityDuration;

    @Column(name = "date")
    private Date date;
}
