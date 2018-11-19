package io.spring.RestServiceTutorial.payroll.repository;

import io.spring.RestServiceTutorial.payroll.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
