package com.userexperior.prakash.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class MonthlyActivity {
    @JsonProperty("activity_name")
    private Enum activityName;
    @JsonProperty("occurrences")
    private Long activityOccurrence;
}
