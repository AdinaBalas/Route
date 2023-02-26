package com.example.route.service;

import com.example.route.AbstractTest;
import com.example.route.exception.ServiceException;
import com.example.route.model.Country;
import org.mockito.Mock;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doReturn;

public class RoutingServiceTest extends AbstractTest {

    private RoutingService routingService;

    @Mock
    private CountryService countryService;

    @BeforeMethod
    public void setUp() {
        routingService = new RoutingServiceImpl(countryService);
    }

    @Test(expectedExceptions = ServiceException.class, expectedExceptionsMessageRegExp = "Countries list is empty")
    public void testGetBorderCrossings_Null() throws ServiceException {
        // Setup
        doReturn(null).when(countryService).getCountries();

        // Test
        routingService.getBorderCrossings("A", "B");
    }

    @Test(expectedExceptions = ServiceException.class, expectedExceptionsMessageRegExp = "Countries list is empty")
    public void testGetBorderCrossings_Empty() throws ServiceException {
        // Setup
        doReturn(Collections.emptyList()).when(countryService).getCountries();

        // Test
        routingService.getBorderCrossings("A", "B");
    }

    @Test(expectedExceptions = ServiceException.class, expectedExceptionsMessageRegExp = "Country cannot be blank")
    public void testGetBorderCrossings_InvalidOrigin() throws ServiceException {
        // Setup
        List<Country> countries = Arrays.asList(
                new Country() {{
                    setCode("A");
                }}, new Country() {{
                    setCode("B");
                }});
        doReturn(countries).when(countryService).getCountries();

        // Test
        routingService.getBorderCrossings("", "");
    }

    @Test(expectedExceptions = ServiceException.class, expectedExceptionsMessageRegExp = "Country X not found")
    public void testGetBorderCrossings_OriginNotFound() throws ServiceException {
        // Setup
        List<Country> countries = Arrays.asList(
                new Country() {{
                    setCode("A");
                }}, new Country() {{
                    setCode("B");
                }});
        doReturn(countries).when(countryService).getCountries();

        // Test
        routingService.getBorderCrossings("X", "");
    }

    @Test(expectedExceptions = ServiceException.class, expectedExceptionsMessageRegExp = "Country A has no borders")
    public void testGetBorderCrossings_OriginNoBorders() throws ServiceException {
        // Setup
        List<Country> countries = Arrays.asList(
                new Country() {{
                    setCode("A");
                }}, new Country() {{
                    setCode("B");
                }});
        doReturn(countries).when(countryService).getCountries();

        // Test
        routingService.getBorderCrossings("A", "");
    }

    @Test(expectedExceptions = ServiceException.class, expectedExceptionsMessageRegExp = "Country cannot be blank")
    public void testGetBorderCrossings_InvalidDestination() throws ServiceException {
        // Setup
        List<Country> countries = Arrays.asList(
                new Country() {{
                    setCode("A");
                    setBorders(List.of("B"));
                }}, new Country() {{
                    setCode("B");
                }});
        doReturn(countries).when(countryService).getCountries();

        // Test
        routingService.getBorderCrossings("A", "");
    }

    @Test(expectedExceptions = ServiceException.class, expectedExceptionsMessageRegExp = "Country C not found")
    public void testGetBorderCrossings_DestinationNotFound() throws ServiceException {
        // Setup
        List<Country> countries = Arrays.asList(
                new Country() {{
                    setCode("A");
                    setBorders(List.of("B"));
                }}, new Country() {{
                    setCode("B");
                }});
        doReturn(countries).when(countryService).getCountries();

        // Test
        routingService.getBorderCrossings("A", "C");
    }

    @Test(expectedExceptions = ServiceException.class, expectedExceptionsMessageRegExp = "Country B has no borders")
    public void testGetBorderCrossings_DestinationNoBorders() throws ServiceException {
        // Setup
        List<Country> countries = Arrays.asList(
                new Country() {{
                    setCode("A");
                    setBorders(List.of("B"));
                }}, new Country() {{
                    setCode("B");
                }});
        doReturn(countries).when(countryService).getCountries();

        // Test
        routingService.getBorderCrossings("A", "B");
    }

    @Test
    public void testGetBorderCrossings_OriginIsDestination() throws ServiceException {
        // Setup
        List<Country> countries = Arrays.asList(
                new Country() {{
                    setCode("A");
                    setBorders(List.of("B"));
                }}, new Country() {{
                    setCode("B");
                    setBorders(List.of("C"));
                }});
        doReturn(countries).when(countryService).getCountries();

        // Test
        List<String> borderCrossings = routingService.getBorderCrossings("A", "A");

        // Verify
        Assert.assertEquals(borderCrossings, Collections.singletonList("A"));
    }


    @Test(expectedExceptions = ServiceException.class, expectedExceptionsMessageRegExp = "There is no land route between country A and country B")
    public void testGetBorderCrossings_NoRoute() throws ServiceException {
        // Setup
        List<Country> countries = Arrays.asList(
                new Country() {{
                    setCode("A");
                    setBorders(List.of("X"));
                }}, new Country() {{
                    setCode("B");
                    setBorders(List.of("Y"));
                }},
                new Country() {{
                    setCode("X");
                    setBorders(Collections.emptyList());
                }},
                new Country() {{
                    setCode("Y");
                    setBorders(Collections.emptyList());
                }});
        doReturn(countries).when(countryService).getCountries();

        // Test
        routingService.getBorderCrossings("A", "B");
    }

    @Test
    public void testGetBorderCrossings_Route() throws ServiceException {
        // Setup
        List<Country> countries = Arrays.asList(
                new Country() {{
                    setCode("A");
                    setBorders(List.of("B"));
                }}, new Country() {{
                    setCode("B");
                    setBorders(List.of("A", "C"));
                }},
                new Country() {{
                    setCode("C");
                    setBorders(List.of("B"));
                }});
        doReturn(countries).when(countryService).getCountries();

        // Test
        List<String> borderCrossings = routingService.getBorderCrossings("A", "C");

        // Verify
        Assert.assertEquals(borderCrossings, List.of("A", "B", "C"));
    }

    @Test(expectedExceptions = ServiceException.class, expectedExceptionsMessageRegExp = "Exception getting border crossings")
    public void testGetBorderCrossings_UnexpectedException() throws ServiceException {
        // Setup
        List<Country> countries = Arrays.asList(
                new Country() {{
                    setCode("A");
                    setBorders(List.of("B"));
                }}, new Country() {{
                    setCode("X");
                    setBorders(List.of("Y"));
                }});
        doReturn(countries).when(countryService).getCountries();

        // Test
        routingService.getBorderCrossings("A", "X");
    }
}