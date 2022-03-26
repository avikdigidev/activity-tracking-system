package com.ue.prakash.pojo.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ActivityTrackerJSONDTO implements Serializable {
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @JsonProperty("unique_id")
    /*name explicitly kept just like mentioned in JSON, else GSON was unable to read the values*/
    private Long unique_id;
    @JsonProperty("activities")
    private List<Activity> activities;

    //from string to long
    public void setUniqueId(String unique_id) {
        this.unique_id = Long.getLong(unique_id);
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
}
