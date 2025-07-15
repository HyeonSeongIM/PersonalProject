package project.personalproject.domain.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import project.personalproject.domain.payment.dto.request.CreatePaymentCommand;
import project.personalproject.domain.payment.entity.Payment;
import project.personalproject.domain.payment.service.PaymentService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ApiV1PaymentController.class)
class ApiV1PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @WithMockUser
    @Test
    void 결제_생성_요청시_201_반환() throws Exception {
        // given
        CreatePaymentCommand command = new CreatePaymentCommand(123L, "ORDER123", 10000);
        Payment payment = paymentService.createPayment(command);

        when(paymentService.createPayment(any())).thenReturn(payment);

        // when & then
        mockMvc.perform(post("/api/v1/payment")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value("ORDER123"));
    }

}