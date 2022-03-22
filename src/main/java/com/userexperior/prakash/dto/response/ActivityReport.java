package com.userexperior.prakash.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityReport implements Serializable {

    @JsonProperty("activity_statistics_for_month")
    private MonthlyActivity monthlyActivity;

    @JsonProperty("activity_statistics_yesterday_vs_today")
    private TwoDayActivity twoDayActivity;
}
