package io.spring.RestServiceTutorial.payroll.controller;

import io.spring.RestServiceTutorial.payroll.exception.EmployeeNotFoundException;
import io.spring.RestServiceTutorial.payroll.employee.Employee;
import io.spring.RestServiceTutorial.payroll.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {
    private final EmployeeRepository repository;

    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return repository.findAll();
    }

    @PostMapping("/employees")
    public ResponseEntity addEmployee(@RequestBody Employee employee) {
        repository.save(employee);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/employees/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @PutMapping("/employees/{id}")
    public Employee replaceEmployee(@RequestBody Employee employee, @PathVariable Long id) {
        return repository.findById(id)
                .map(currentEmployee -> {
                    currentEmployee.setName(employee.getName());
                    currentEmployee.setRole(employee.getRole());
                    return repository.save(employee);
                }).orElseGet(() -> {
                            employee.setId(id);
                            return repository.save(employee);
                        }
                );
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity deleteEmployee(@PathVariable Long id){
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
