package com.example.route;

import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;

public class AbstractTest {

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
}
