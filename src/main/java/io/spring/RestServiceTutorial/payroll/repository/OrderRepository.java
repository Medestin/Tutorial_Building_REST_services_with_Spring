package io.spring.RestServiceTutorial.payroll.repository;

import io.spring.RestServiceTutorial.payroll.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
