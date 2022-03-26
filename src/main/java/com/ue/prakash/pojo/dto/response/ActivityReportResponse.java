package com.ue.prakash.pojo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ActivityReportResponse implements Serializable {
    private static final long serialVersionUID = 1905122041950251207L;

    @JsonProperty("activity_statistics_for_month")
    private List<MonthlyActivity> monthlyActivity;

    @JsonProperty("activity_statistics_yesterday_vs_today")
    private List<TwoDayActivity> twoDayActivity;
}
