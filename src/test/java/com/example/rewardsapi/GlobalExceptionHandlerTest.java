package com.example.rewardsapi;

import com.example.rewardsapi.controller.RewardController;
import com.example.rewardsapi.exception.GlobalExceptionHandler;
import com.example.rewardsapi.model.Transaction;
import com.example.rewardsapi.service.RewardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RewardController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RewardService rewardService;

    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void handleIllegalArgument_onNegativeAmount() throws Exception {
        Transaction in = new Transaction("C1", -50, LocalDate.of(2026, 1, 1));
        when(rewardService.saveTransaction(any(Transaction.class)))
                .thenThrow(new IllegalArgumentException("Transaction amount cannot be negative"));

        String json = mapper.writeValueAsString(in);

        mvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Transaction amount cannot be negative"));
    }

    @Test
    void handleBadRequestBody_onMalformedJson() throws Exception {
        String malformedJson = "{ invalid json }";

        mvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Malformed JSON request"));
    }

    @Test
    void handleValidation_onMissingRequiredField() throws Exception {
        // missing customerId, amount, date in JSON
        String json = "{}";

        mvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void response_containsTimestamp() throws Exception {
        Transaction in = new Transaction("C1", -10, LocalDate.of(2026, 1, 1));
        when(rewardService.saveTransaction(any(Transaction.class)))
                .thenThrow(new IllegalArgumentException("Test error"));

        String json = mapper.writeValueAsString(in);

        mvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists());
    }
}