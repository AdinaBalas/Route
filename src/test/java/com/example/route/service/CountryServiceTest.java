package com.example.route.service;

import com.example.route.AbstractTest;
import com.example.route.exception.ServiceException;
import com.example.route.model.Country;
import org.mockito.Mock;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class CountryServiceTest extends AbstractTest {

    private static final String URL = "https://raw.githubusercontent.com/mledoze/countries/master/countries.json";

    private CountryService countryService;
    @Mock
    private RestTemplate restTemplate;

    @BeforeMethod
    public void setUp() {
        countryService = new CountryServiceImpl();
        ReflectionTestUtils.setField(countryService, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(countryService, "URL", URL);
    }

    @Test(expectedExceptions = ServiceException.class, expectedExceptionsMessageRegExp = "Exception getting countries")
    public void testGetCountries_Exception() throws ServiceException {
        // Set
        doThrow(new RestClientException("Exception")).when(restTemplate).exchange(
                eq(URL),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class)
        );

        // Test
        countryService.getCountries();
    }

    @Test
    public void testGetCountries() throws ServiceException {
        // Set
        List<String> expectedCountries = Arrays.asList("CZE", "AUT", "ITA");
        ResponseEntity responseEntity = ResponseEntity.ok(expectedCountries);

        doReturn(responseEntity).when(restTemplate).exchange(
                eq(URL),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class)
        );

        // Test
        List<Country> countries = countryService.getCountries();

        // Verify
        assertEquals(countries, expectedCountries);
    }
}