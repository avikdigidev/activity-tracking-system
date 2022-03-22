package com.userexperior.prakash.pojo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class TwoDayActivity implements Serializable {
    private static final long serialVersionUID = 1905982041950251207L;
    @JsonProperty("activity_name")
    private String activityName;
    @JsonProperty("yesterday_occurrences")
    private Long yesterdayOccurrence;
    @JsonProperty("activity_name")
    private String status;
    @JsonProperty("today_occurrences")
    private Long todayOccurrence;
}
