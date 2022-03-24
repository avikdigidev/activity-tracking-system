package com.userexperior.prakash.pojo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import java.io.Serializable;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class TwoDayActivityDb implements Serializable {
    private static final long serialVersionUID = 1905982041950251207L;
    @JsonIgnore
    private String activityName;
    @JsonIgnore
    private Long occurrences;

}
