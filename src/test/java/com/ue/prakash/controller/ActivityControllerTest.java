package com.ue.prakash.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ue.prakash.pojo.dto.response.ActivityReportResponse;
import com.ue.prakash.service.ActivityTrackerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class ActivityControllerTest {

    private MockMvc mockMvc;
    @InjectMocks
    ActivityController activityController;

    @Mock
    ActivityTrackerService activityTrackerService;

    @Before
    public void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(activityController).build();
    }

    @Test
    public void getActivityReportTest() throws Exception {
        ActivityReportResponse results = new ActivityReportResponse();
        when(activityTrackerService.getActivityReport()).thenReturn(results);
        mockMvc.perform(get("/report")).andExpect(status().isOk());
    }

    public String convertToJson(final Object obj) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        final String jsonContent = mapper.writeValueAsString(obj);
        return jsonContent;
    }
}

