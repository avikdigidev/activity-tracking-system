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
    /*name explicitly kept just like mentioned in JSON, else GSON was unable to read the values*/
    private Long unique_id;
    @JsonProperty("activities")
    private List<Activity> activities;

    public void setUniqueId(String unique_id) {
        this.unique_id = Long.getLong(unique_id);
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
}
