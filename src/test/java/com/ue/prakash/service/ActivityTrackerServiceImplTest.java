package com.ue.prakash.service;

import com.ue.prakash.repository.ActivityTrackerRepository;
import com.ue.prakash.service.impl.ActivityTrackerServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class ActivityTrackerServiceImplTest {
    AutoCloseable openMocks;
    @InjectMocks
    private ActivityTrackerServiceImpl underTest;
    @Mock
    ActivityTrackerRepository activityTrackerRepository;
    @BeforeEach
    public void setUp() throws Exception {

        MockitoAnnotations.openMocks(this);
    }
    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }
}
