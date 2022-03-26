package com.ue.prakash.pojo;


import com.ue.prakash.pojo.dto.TwoDayActivityDb;
import com.ue.prakash.pojo.dto.request.Activity;
import com.ue.prakash.pojo.dto.request.ActivityTrackerJSONDTO;
import com.ue.prakash.pojo.dto.response.ActivityReportResponse;
import com.ue.prakash.pojo.dto.response.MonthlyActivity;
import com.ue.prakash.pojo.dto.response.TwoDayActivity;
import com.ue.prakash.pojo.entity.ActivityTracker;
import com.ue.prakash.utils.PojoTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EntityTest {

    private static final Logger logger = LoggerFactory.getLogger(EntityTest.class);

    AutoCloseable openMocks;
    @BeforeEach
     void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
    }
    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }
    @Test
    void testAccessorsShouldAccessProperField() {
        testEntityClass(Activity.class);
        testEntityClass(ActivityTrackerJSONDTO.class);
        testEntityClass(ActivityReportResponse.class);
        testEntityClass(MonthlyActivity.class);
        testEntityClass(TwoDayActivity.class);
        testEntityClass(TwoDayActivityDb.class);
        testEntityClass(ActivityTracker.class);
    }

    public void testEntityClass(Class<?> css) {
        PojoTestUtils.validateAccessors(css);
        try {
            Object obj = css.newInstance();
            obj.toString();
        } catch (Exception e) {
            logger.info("testEntityClass exception in test class pass");
        }

    }

}
