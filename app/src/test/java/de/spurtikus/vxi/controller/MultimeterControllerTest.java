package de.spurtikus.vxi.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
public class MultimeterControllerTest {

    MultimeterController multimeterController = new MultimeterController();

    @Test
    public void testInfo() {
        String ret = multimeterController.info("mfc", "hp1411");
        assertNotNull(ret);
    }

    @Test
    public void testInit() {
        String ret = multimeterController.init("mfc", "hp1411");
        assertNotNull(ret);
    }
}
