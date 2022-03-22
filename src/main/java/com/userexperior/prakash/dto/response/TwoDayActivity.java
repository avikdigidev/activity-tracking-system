package com.userexperior.prakash.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class TwoDayActivity {
    @JsonProperty("activity_name")
    private Enum activityName;
    @JsonProperty("yesterday_occurrences")
    private Long yesterdayOccurrence;
    @JsonProperty("activity_name")
    private Enum status;
    @JsonProperty("today_occurrences")
    private Long todayOccurrence;
}
