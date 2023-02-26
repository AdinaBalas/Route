package com.example.route.service;

import com.example.route.exception.ServiceException;
import com.example.route.model.Country;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    @Value("${resource.url}")
    private String URL;

    private RestTemplate restTemplate;

    public CountryServiceImpl() {
        initRestTemplate();
    }

    @Override
    public List<Country> getCountries() throws ServiceException {
        try {
            ResponseEntity<List<Country>> responseEntity =
                    restTemplate.exchange(
                            URL,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<>() {
                            }
                    );

            return responseEntity.getBody();
        } catch (Exception e) {
            throw new ServiceException("Exception getting countries", e);
        }
    }

    public void initRestTemplate() {
        restTemplate = new RestTemplate();

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
    }
}
