package io.spring.RestServiceTutorial.payroll.assembler;

import io.spring.RestServiceTutorial.payroll.controller.EmployeeController;
import io.spring.RestServiceTutorial.payroll.employee.Employee;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class EmployeeResourceAssembler implements ResourceAssembler<Employee, Resource<Employee>> {
    @Override
    public Resource<Employee> toResource(Employee entity) {
        return new Resource<>(entity,
                linkTo(methodOn(EmployeeController.class).getEmployeeById(entity.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).getEmployees()).withRel("employees"));
    }
}
