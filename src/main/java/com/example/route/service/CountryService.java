package com.example.route.service;

import com.example.route.exception.ServiceException;
import com.example.route.model.Country;

import java.util.List;

public interface CountryService {

    List<Country> getCountries() throws ServiceException;
}
