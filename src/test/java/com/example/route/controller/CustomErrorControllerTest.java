package com.example.route.controller;

import com.example.route.AbstractTest;
import jakarta.servlet.http.HttpServletRequest;
import org.mockito.Mock;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CustomErrorControllerTest extends AbstractTest {

    private CustomErrorController customErrorController;

    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeMethod
    public void setUp() {
        customErrorController = new CustomErrorController();
    }

    @Test
    public void testError() {
        // Test
        String result = customErrorController.error(httpServletRequest);

        // Verify
        Assert.assertEquals(result, "<h1>Something went wrong!</h2>");
    }
}