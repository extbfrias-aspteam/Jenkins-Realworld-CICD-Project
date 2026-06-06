package com.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest {

    @Test
    public void testGetStatusReturnsOK() {
        App app = new App("qa", "1");
        assertEquals("Status should be OK", "OK", app.getStatus());
    }

    @Test
    public void testBannerContainsEnvironment() {
        App app = new App("staging", "42");
        String banner = app.getBanner();
        assertTrue("Banner should contain environment", banner.contains("staging"));
        assertTrue("Banner should contain build number", banner.contains("42"));
    }

    @Test
    public void testBannerContainsStatus() {
        App app = new App("production", "99");
        String banner = app.getBanner();
        assertTrue("Banner should contain OK status", banner.contains("OK"));
    }
}
