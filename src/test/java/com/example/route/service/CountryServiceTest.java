package com.example.route.service;

import com.example.route.AbstractTest;
import com.example.route.exception.ServiceException;
import com.example.route.model.Country;
import org.mockito.Mock;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class CountryServiceTest extends AbstractTest {

    private CountryService countryService;
    @Mock
    private RestTemplate restTemplate;

    private String URL = "https://raw.githubusercontent.com/mledoze/countries/master/countries.json";

    @Test(expectedExceptions = ServiceException.class, expectedExceptionsMessageRegExp = "Exception getting countries")
    public void testGetCountries_Exception() throws ServiceException {
        // Set
        countryService = new CountryServiceImpl();
        ReflectionTestUtils.setField(countryService, "restTemplate", restTemplate);

        doThrow(new RestClientException("Exception")).when(restTemplate).exchange(
                URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        // Test
        countryService.getCountries();
    }

    @Test
    public void testGetCountries() throws ServiceException {
        // Set
        countryService = mock(CountryService.class);
        List<String> expectedCountries = Arrays.asList("CZE", "AUT", "ITA");
        doReturn(expectedCountries).when(countryService).getCountries();

        // Test
        List<Country> countries = countryService.getCountries();

        // Verify
        Assert.assertEquals(countries, expectedCountries);
    }
}