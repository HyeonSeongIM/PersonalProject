package project.personalproject.domain.payment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.personalproject.domain.payment.entity.Payment;
import project.personalproject.domain.payment.service.SettlementService;

@Service
@RequiredArgsConstructor
@Transactional
public class SettlementServiceImpl implements SettlementService {
    @Override
    public void settle(Payment payment) {

    }
}
