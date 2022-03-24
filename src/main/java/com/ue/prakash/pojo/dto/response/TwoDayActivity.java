package com.ue.prakash.pojo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TwoDayActivity implements Serializable {
    private static final long serialVersionUID = 1905982041950251207L;
    @JsonProperty("activity_name")
    private String name;
    @JsonProperty("yesterday_occurrences")
    private Long yesterdayOccurrence;
    @JsonProperty("today_occurrences")
    private Long todayOccurrence;
    @JsonProperty("status")
    private String status;
}
