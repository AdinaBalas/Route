package com.example.route.controller;

import com.example.route.AbstractTest;
import com.example.route.exception.ServiceException;
import com.example.route.service.RoutingService;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

public class RoutingControllerTest extends AbstractTest {

    private RoutingController routingController;

    @Mock
    private RoutingService routingService;

    @BeforeMethod
    public void setUp() {
        routingController = new RoutingController();
        ReflectionTestUtils.setField(routingController, "routingService", routingService);
    }

    @Test
    public void testGetBorderCrossings() throws ServiceException {
        // Setup
        List<String> expectedBorderCrossings = List.of("A", "B");
        doReturn(expectedBorderCrossings).when(routingService).getBorderCrossings("A", "B");

        // Test
        ResponseEntity<List<String>> borderCrossings = routingController.getBorderCrossings("A", "B");

        // Verify
        Assert.assertEquals(borderCrossings.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(borderCrossings.getBody(), expectedBorderCrossings);
    }

    @Test
    public void testGetBorderCrossings_Exception() throws ServiceException {
        // Setup
        doThrow(new ServiceException("Country cannot be blank")).when(routingService).getBorderCrossings("", "");

        // Test
        ResponseEntity<List<String>> borderCrossings = routingController.getBorderCrossings("", "");

        // Verify
        Assert.assertEquals(borderCrossings.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assert.assertEquals(borderCrossings.getBody(), Collections.singletonList("Country cannot be blank"));
    }
}