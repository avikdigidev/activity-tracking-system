package com.ue.prakash.pojo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MonthlyActivity implements Serializable {
    private static final long serialVersionUID = 1905182041950251207L;
    @JsonProperty("activity_name")
    private String activityName;
    @JsonProperty("occurrences")
    private Long activityOccurrence;


}
