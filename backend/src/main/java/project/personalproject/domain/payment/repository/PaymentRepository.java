package project.personalproject.domain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.personalproject.domain.payment.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
