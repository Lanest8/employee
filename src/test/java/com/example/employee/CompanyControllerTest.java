package com.example.employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CompanyController controller;

    @BeforeEach
    public void setup() {
        controller.clear();
    }

    @Test
    void should_return_company_list_when_get_company() throws Exception {
        Company expect = controller.create(new Company(null, "oocl1", "珠海"));
        controller.create(new Company(null, "oocl2", "珠海"));
        MockHttpServletRequestBuilder request = get("/companies")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(expect.id()))
                .andExpect(jsonPath("$[0].name").value(expect.name()))
                .andExpect(jsonPath("$[0].address").value(expect.address()));
    }


    @Test
    void should_return_company_when_get_company_with_id_exist() throws Exception {
        Company company = new Company(null, "oocl", "珠海");
        Company expect = controller.create(company);
        MockHttpServletRequestBuilder request = get("/companies/" + expect.id())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expect.id()))
                .andExpect(jsonPath("$.name").value("oocl"))
                .andExpect(jsonPath("$.address").value("珠海"));
    }

    @Test
    void should_return_company_when_get_company_with_limit() throws Exception {
        controller.create(new Company(null, "oocl1", "珠海"));
        controller.create(new Company(null, "oocl2", "珠海"));
        controller.create(new Company(null, "oocl3", "珠海"));
        controller.create(new Company(null, "oocl4", "珠海"));
        controller.create(new Company(null, "oocl5", "珠海"));
        controller.create(new Company(null, "oocl6", "珠海"));

        MockHttpServletRequestBuilder request = get("/companies" + "?page=1&size=5")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));

    }

    @Test
    void should_return_create_company_when_post() throws Exception {
        String requestBody = """
                {
                    "name":"oocl",
                    "address":"珠海"
                }
                """;
        MockHttpServletRequestBuilder request = post("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("oocl"))
                .andExpect(jsonPath("$.address").value("珠海"));
    }

    @Test
    void should_return_company_when_update_an_employee() throws Exception {
        Company company = controller.create(new Company(null, "oocl1", "珠海"));
        int id = company.id();

        String updateRequestBody = """
                {
                    "name": "oocl2",
                    "address": "香港"
                }
                """;

        MockHttpServletRequestBuilder request = put("/companies/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequestBody);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("oocl2"))
                .andExpect(jsonPath("$.address").value("香港"));
    }

    @Test
    void should_return_employee_when_delete_an_employee() throws Exception {
        Company company = controller.create(new Company(null, "oocl1", "珠海"));
        int id = company.id();

        MockHttpServletRequestBuilder request = delete("/companies/" + id)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

}
