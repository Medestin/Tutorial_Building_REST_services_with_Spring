package io.spring.RestServiceTutorial.payroll.controller;

import io.spring.RestServiceTutorial.payroll.assembler.EmployeeResourceAssembler;
import io.spring.RestServiceTutorial.payroll.employee.Employee;
import io.spring.RestServiceTutorial.payroll.exception.EmployeeNotFoundException;
import io.spring.RestServiceTutorial.payroll.repository.EmployeeRepository;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class EmployeeController {
    private final EmployeeRepository repository;
    private final EmployeeResourceAssembler assembler;

    public EmployeeController(EmployeeRepository repository, EmployeeResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/employees")
    public Resources<Resource<Employee>> getEmployees() {
        List<Resource<Employee>> employees = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(employees, linkTo(methodOn(EmployeeController.class).getEmployees()).withSelfRel());
    }

    @GetMapping("/employees/{id}")
    public Resource<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));

        return assembler.toResource(employee);
    }

    @PostMapping("/employees")
    public ResponseEntity addEmployee(@RequestBody Employee employee) {
        repository.save(employee);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/employees/{id}")
    public Resource<Employee> replaceEmployee(@RequestBody Employee employee, @PathVariable Long id) {
        Employee newEmployee = repository.findById(id)
                .map(e -> {
                    e.setName(employee.getName());
                    e.setRole(employee.getRole());
                    return repository.save(employee);
                }).orElseGet(() -> {
                            employee.setId(id);
                            return repository.save(employee);
                        }
                );
        return assembler.toResource(newEmployee);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity deleteEmployee(@PathVariable Long id){
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
