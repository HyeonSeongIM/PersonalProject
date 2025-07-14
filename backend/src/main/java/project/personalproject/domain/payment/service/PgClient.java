package project.personalproject.domain.payment.service;

import project.personalproject.domain.payment.dto.request.PgRequest;
import project.personalproject.domain.payment.dto.response.PgResponse;

public interface PgClient {
    // 결제 요청
    PgResponse requestPayment(PgRequest pgRequest);
}
