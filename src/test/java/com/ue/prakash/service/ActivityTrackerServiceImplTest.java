package com.ue.prakash.service;

import com.ue.prakash.service.impl.ActivityTrackerServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityTrackerServiceImplTest {

    @InjectMocks
    private ActivityTrackerServiceImpl underTest;

}
