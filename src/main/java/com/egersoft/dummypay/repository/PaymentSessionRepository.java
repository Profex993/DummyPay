package com.egersoft.dummypay.repository;

import com.egersoft.dummypay.model.PaymentSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentSessionRepository extends JpaRepository<PaymentSession, Long> {

}
