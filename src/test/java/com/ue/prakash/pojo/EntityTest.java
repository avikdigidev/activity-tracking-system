package com.ue.prakash.pojo;


import com.ue.prakash.pojo.dto.request.Activity;
import com.ue.prakash.utils.PojoTestUtils;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityTest {

    private static Logger logger = LoggerFactory.getLogger(EntityTest.class);

    @Before("")
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testAccessorsShouldAccessProperField() {
        testEntityClass(Activity.class);


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
