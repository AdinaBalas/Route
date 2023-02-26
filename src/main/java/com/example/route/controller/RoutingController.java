package com.example.route.controller;

import com.example.route.service.RoutingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/routing")
public class RoutingController {

    private final RoutingService routingService;

    @Autowired
    public RoutingController(RoutingService routingService) {
        this.routingService = routingService;
    }

    @GetMapping(value = "/{origin}/{destination}", produces = "application/json")
    public ResponseEntity<List<String>> getBorderCrossings(@PathVariable(value = "origin") String origin,
                                                           @PathVariable(value = "destination") String destination) {
        try {
            return ResponseEntity.ok(routingService.getBorderCrossings(origin, destination));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonList(e.getMessage()));
        }
    }
}
