package project.personalproject.domain.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.personalproject.domain.payment.dto.request.CreatePaymentCommand;
import project.personalproject.domain.payment.service.PaymentService;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/payment")
public class ApiV1PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody CreatePaymentCommand command) {
        Map<String, Object> body = Map.of("orderId", command.orderId());
        return ResponseEntity.status(201).body(body);
    }
}
