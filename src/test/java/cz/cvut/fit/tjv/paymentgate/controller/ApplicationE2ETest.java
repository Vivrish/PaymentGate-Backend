package cz.cvut.fit.tjv.paymentgate.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Parent class for all E2E tests. Is used for initializing test data
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
abstract public class ApplicationE2ETest {
    @Autowired
    protected MockMvc mockMvc;

    @Test
    protected void init() throws Exception {
        String billyJson = """
                {
                "name": "billy",
                "surname": "bones",
                "email": "billybones@gmail.com",
                "login": "bill",
                  "password": "ifWefy678"

                }""";
        String johnnyJson = """
                {
                "name": "johnny",
                "surname": "jones",
                "email": "johnyjones@gmail.com",
                "login": "john",
                  "password": "ifWefy678"

                }""";
        String bankCardOne = """
                {
                  "cardNumber": 1236,
                  "cvv": 123,
                  "expirationDate": "2011-11-11T12:30:15.0000",
                  "currency": "CZK",
                  "balance": 300.28,
                  "active": true
                }""";
        String bankCardOther = """
                {
                  "currency": "CZK",
                  "cardNumber": 1234,
                  "balance": 1000,
                  "expirationDate": "2021-11-11",
                  "preferred": true,
                  "active": true,
                  "cvv": 321
                }""";
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/user/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(billyJson)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.login", Matchers.is("bill")));
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/user/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(johnnyJson)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.login", Matchers.is("john")));
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/card/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bankCardOne)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cardNumber", Matchers.is(1236)));
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/card/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bankCardOther)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cardNumber", Matchers.is(1234)));
    }
    }

