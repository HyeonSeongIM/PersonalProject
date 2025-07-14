package project.personalproject.domain.payment.service;

import project.personalproject.domain.payment.entity.Payment;

public interface SettlementService {

    // 정산요청
    void settle(Payment payment);
}
