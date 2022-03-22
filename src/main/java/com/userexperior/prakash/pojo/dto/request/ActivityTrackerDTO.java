package com.userexperior.prakash.pojo.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ActivityTrackerDTO implements Serializable {
    @JsonFormat(shape = JsonFormat.Shape.ANY)
    @JsonProperty("unique_id")
    private Long uniqueId;
    @JsonProperty("activities")
    private List<Activity> activities;

    public void setUnique_id(String uniqueId) {
        this.uniqueId = Long.getLong(uniqueId);
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
}
