package io.spring.RestServiceTutorial.payroll.configuration;

import io.spring.RestServiceTutorial.payroll.employee.Employee;
import io.spring.RestServiceTutorial.payroll.order.Order;
import io.spring.RestServiceTutorial.payroll.order.Status;
import io.spring.RestServiceTutorial.payroll.repository.EmployeeRepository;
import io.spring.RestServiceTutorial.payroll.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository, OrderRepository orderRepository){
        return args -> {
            log.info("Preloading " + employeeRepository.save(new Employee("Bilbo Baggins", "burglar")));
            log.info("Preloading " + employeeRepository.save(new Employee("Frodo Baggins", "thief")));
            orderRepository.save(new Order("MacBook Pro", Status.COMPLETED));
            orderRepository.save(new Order("iPhone", Status.IN_PROGRESS));

            orderRepository.findAll().forEach(order -> {
                log.info("Preloaded " + order);
            });
        };
    }
}
