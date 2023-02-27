package com.example.route;

import com.example.route.controller.CustomErrorController;
import com.example.route.controller.RoutingController;
import com.example.route.service.CountryService;
import com.example.route.service.RoutingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.testng.Assert.assertNotNull;

@SpringBootTest
class RouteApplicationTests {

    @Autowired
    private CustomErrorController customErrorController;
    @Autowired
    private RoutingController routingController;
    @Autowired
    private CountryService countryService;
    @Autowired
    private RoutingService routingService;

    @Test
    void contextLoads() {
        assertNotNull(customErrorController);
        assertNotNull(routingController);
        assertNotNull(countryService);
        assertNotNull(routingService);
    }
}
