package com.charan.employee_managment_system;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerHqlIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getByName_whenExists_returnsEmployee() throws Exception {
        // seeded data in Service.postConstruct includes firstName "Kalla"
        mvc.perform(get("/v1/hql/employees/find/name/Kalla"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.firstName").value("Kalla"));
    }

    @Test
    void getByEmail_whenExists_returnsEmployee() throws Exception {
        mvc.perform(get("/v1/hql/employees/find/email/charan@gmail.com"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("charan@gmail.com"));
    }

    @Test
    @Transactional
    void create_full_then_updatePhn_and_updateOtherDetails_and_deleteFlow() throws Exception {
        String createJson = """
            {
              "firstName":"IntTest",
              "lastName":"User",
              "email":"inttest+hql@example.com",
              "address":"Initial Address",
              "phn":"0001112222"
            }
            """;

        String createResp = mvc.perform(post("/v1/hql/employees/create/full")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("inttest+hql@example.com"))
            .andReturn().getResponse().getContentAsString();

        JsonNode created = objectMapper.readTree(createResp);
        long id = created.get("id").asLong();

        // Update phone only
        String phnJson = """
        {"phn":"9998887777"}
        """;
        mvc.perform(patch("/v1/hql/employees/update/phn/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(phnJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("updated successfully")));

        // Update other details (lastName + address)
        String otherJson = """
            {
              "lastName":"UpdatedLast",
              "address":"Updated Address"
            }
            """;
        mvc.perform(patch("/v1/hql/employees/update/otherdetails/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(otherJson))
            .andExpect(status().isOk());
    

        mvc.perform(delete("/v1/hql/employees/delete/inttest+hql@example.com"))
            .andExpect(status().isOk())
            .andExpect(content().string(org.hamcrest.Matchers.containsString("deleted with email")));
    }

    @Test
    void patchPhn_missingPhnKey_returnsNotFound() throws Exception {
        mvc.perform(patch("/v1/hql/employees/update/phn/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isNotFound())
            .andExpect(content().string(org.hamcrest.Matchers.containsString("You can update only PHONE NUMBER")));
    }

    @Test
    void findByName_whenNotFound_returns404() throws Exception {
        mvc.perform(get("/v1/hql/employees/find/name/NameThatDoesNotExist"))
            .andExpect(status().isNotFound())
            .andExpect(content().string(org.hamcrest.Matchers.containsString("No employee found with the first name")));
    }
}