package project.personalproject.domain.payment.service.impl;

import org.springframework.stereotype.Component;
import project.personalproject.domain.payment.dto.request.PgRequest;
import project.personalproject.domain.payment.dto.response.PgResponse;
import project.personalproject.domain.payment.service.PgClient;

@Component
public class TossPgClient implements PgClient {

    @Override
    public PgResponse requestPayment(PgRequest request) {
        // 임시 구현 (Stub)
        return new PgResponse("TOSS_ORDER_001", "READY");
    }
}
