package com.ue.prakash.pojo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class TwoDayActivityDb implements Serializable {
    private static final long serialVersionUID = 1905982041950251207L;
    @JsonIgnore
    private String activityName;
    @JsonIgnore
    private Long occurrences;

}
