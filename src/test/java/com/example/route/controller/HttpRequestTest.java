package com.example.route.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HttpRequestTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void error() throws Exception {
        mockMvc.perform(get("/error"))
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>Something went wrong!</h2>"));
    }

    @Test
    public void route_EmptyOrigin() throws Exception {
        mockMvc.perform(get("/routing/ / ITA"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Country cannot be blank")));
    }

    @Test
    public void route_InvalidOrigin() throws Exception {
        mockMvc.perform(get("/routing/A/ITA"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Country A not found")));
    }

    @Test
    public void route_EmptyDestination() throws Exception {
        mockMvc.perform(get("/routing/CZE/ "))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Country cannot be blank")));
    }

    @Test
    public void route_InvalidDestination() throws Exception {
        mockMvc.perform(get("/routing/CZE/B"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Country B not found")));
    }

    @Test
    public void route_NoBorders() throws Exception {
        mockMvc.perform(get("/routing/ABW/CZE"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Country ABW has no borders")));
    }

    @Test
    public void route_NoRoute() throws Exception {
        mockMvc.perform(get("/routing/USA/CZE"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("There is no land route between country USA and country CZE")));
    }

    @Test
    public void route_OriginEqualsDestination() throws Exception {
        mockMvc.perform(get("/routing/CZE/CZE"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json((List.of("CZE").toString())));
    }

    @Test
    public void route_OriginDifferentThanDestination() throws Exception {
        mockMvc.perform(get("/routing/CZE/ITA"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json((List.of("CZE", "AUT", "ITA").toString())));
    }
}