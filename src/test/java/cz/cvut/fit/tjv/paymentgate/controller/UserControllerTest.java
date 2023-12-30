package cz.cvut.fit.tjv.paymentgate.controller;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


/**
 * Main E2E test. Running individual tests in arbitrary order can cause undefined false result.
 * !Tests must be started sequentially!
 * Initializing of test data is performed in parent class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserControllerTest extends ApplicationE2ETest{

    @Test
    void create() throws Exception {
        String jsonContent = """
                {
                  "name": "bob",
                  "surname": "builder",
                  "email": "bobbuilder@gmail.com",
                  "login": "bob",
                  "password": "ifWefy678"
                }""";
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.login", Matchers.is("bob")));

    }

    @Test
    void deleteByLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/login/bob/delete"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/user/login/bob"))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void updateByLogin() throws Exception {
        String newJohny = """
                {
                "name": "johannes",
                "surname": "johansen",
                "email": "notJohny@gmail.com",
                "login": "johannes",
                  "password": "ifWefy678"

                }""";
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/user/login/john/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(newJohny)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/user/login/john"))
                .andExpect(MockMvcResultMatchers.status().is(404));
        mockMvc.perform(MockMvcRequestBuilders.get("/user/login/johannes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("johannes")));
    }

    @Test
    void addBankCardByLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/user/login/johannes/add/card/1236"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.put("/user/login/bill/add/card/1234"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void choosePreferredByLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/user/login/johannes/prefer/1236"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.put("/user/login/bill/prefer/1234"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/user/login/johannes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.preferredCardNumber").value(1236));
        mockMvc.perform(MockMvcRequestBuilders.get("/user/login/bill"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.preferredCardNumber").value(1234));
    }
    @Test
    void createTransactionByLogin() throws Exception {
        String newJohny = """
                {
                "name": "johannes",
                "surname": "johansen",
                "email": "notJohny@gmail.com",
                "login": "johannes",
                  "password": "ifWefy678"

                }""";
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/user/login/john/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newJohny)
        );

        mockMvc.perform(MockMvcRequestBuilders.put("/user/login/johannes/add/card/1236"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.put("/user/login/bill/add/card/1234"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.put("/user/login/johannes/prefer/1236"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.put("/user/login/bill/prefer/1234"))
                .andExpect(MockMvcResultMatchers.status().isOk());


        String transaction = """
                {"currency": "CZK",
                "date": "2011-11-11",
                "amount": 600}""";
        mockMvc.perform(MockMvcRequestBuilders.post("/user/login/bill/create/transaction/johannes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transaction))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/card/number/1234"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(400.0));
        mockMvc.perform(MockMvcRequestBuilders.get("/card/number/1236"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(900.28));

        mockMvc.perform(MockMvcRequestBuilders.post("/user/login/bill/create/transaction/johannes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transaction))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.successful").value(false));

        mockMvc.perform(MockMvcRequestBuilders.put("/card/number/1234/deactivate"))
                .andExpect(MockMvcResultMatchers.status().isOk());
         mockMvc.perform(MockMvcRequestBuilders.get("/card/number/1234"))
                 .andExpect(MockMvcResultMatchers.status().isOk())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.active").value(false));

        mockMvc.perform(MockMvcRequestBuilders.post("/user/login/johannes/create/transaction/bill")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transaction))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.successful").value(false));

    }
}