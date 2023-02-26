package com.example.route.service;

import com.example.route.exception.ServiceException;
import com.example.route.model.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RoutingServiceImpl implements RoutingService {

    @Autowired
    private CountryService countryService;

    @Override
    public List<String> getBorderCrossings(String origin, String destination) throws ServiceException {
        List<Country> countryList = countryService.getCountries();
        if (CollectionUtils.isEmpty(countryList)) {
            throw new ServiceException("Countries list is empty");
        }

        Map<String, Country> map = countryList.stream().collect(Collectors.toMap(Country::getCode, Function.identity()));
        Country originCountry = validateAndGetCountry(map, origin);
        Country destinationCountry = validateAndGetCountry(map, destination);
        if (originCountry.equals(destinationCountry)) {
            return Collections.singletonList(origin);
        }

        try {
            return calculateBorders(map, originCountry, destinationCountry);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Exception getting border crossings", e);
        }
    }

    private Country validateAndGetCountry(Map<String, Country> map, String countryCode) throws ServiceException {
        if (countryCode.isBlank()) {
            throw new ServiceException("Country cannot be blank");
        }

        Country country = map.get(countryCode);
        if (null == country) {
            throw new ServiceException(String.format("Country %s not found", countryCode));
        }
        if (CollectionUtils.isEmpty(country.getBorders())) {
            throw new ServiceException(String.format("Country %s has no borders", countryCode));
        }

        return country;
    }

    public List<String> calculateBorders(Map<String, Country> countries, Country origin, Country destination) throws ServiceException {
        // Create a set to keep track of visited countries
        Set<Country> visitedCountries = new HashSet<>();
        // Create a queue for the BFS algorithm
        Queue<List<String>> queue = new LinkedList<>();
        // Add the origin country to the queue as the starting point
        queue.offer(Collections.singletonList(origin.getCode()));

        // Loop until the queue is empty
        while (!queue.isEmpty()) {
            // Get the next route from the queue
            List<String> route = queue.poll();
            // Get the last country in the route
            String lastCountry = route.get(route.size() - 1);
            // Check if this is the destination country
            if (lastCountry.equals(destination.getCode())) {
                // Return the route to the destination
                return route;
            }

            // If country is not the destination country, mark the country as visited
            visitedCountries.add(countries.get(lastCountry));

            // Add all border countries to the queue as new routes
            for (String border : countries.get(lastCountry).getBorders()) {
                if (!visitedCountries.contains(countries.get(border))) {
                    // Create a new route by appending the border country to the current route
                    List<String> newRoute = new ArrayList<>(route);
                    newRoute.add(border);
                    // Add the new route to the queue
                    queue.offer(newRoute);
                }
            }
        }

        // If we reach here, there is no path from the origin to the destination
        throw new ServiceException(String.format("There is no land route between country %s and country %s", origin.getCode(), destination.getCode()));
    }
}
