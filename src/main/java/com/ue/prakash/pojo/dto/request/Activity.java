package com.ue.prakash.pojo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Activity implements Serializable {
    private static final long serialVersionUID = 1905182041950851207L;
    @JsonProperty("name")
    private String name;
    @JsonProperty("time")
    private Long time;
    @JsonProperty("duration")
    private int duration;

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = Long.getLong(time);
    }

    public void setDuration(String duration) {
        this.duration = Integer.parseInt(duration);
    }
}
