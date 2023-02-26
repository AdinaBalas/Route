package com.example.route.service;

import com.example.route.exception.ServiceException;

import java.util.List;

public interface RoutingService {

    List<String> getBorderCrossings(String origin, String destination) throws ServiceException;
}
