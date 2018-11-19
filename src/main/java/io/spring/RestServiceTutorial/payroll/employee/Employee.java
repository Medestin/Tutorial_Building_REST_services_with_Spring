package io.spring.RestServiceTutorial.payroll.employee;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Employee {

    @Id
    @NotNull
    @GeneratedValue
    private Long id;

    private String firstName;
    private String lastName;
    private String role;

    public Employee() {
    }

    public Employee(String name, String role){
        this.setName(name);
        this.role = role;
    }

    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    public void setName(String name) {
        String[] parts =name.split(" ");
        this.firstName = parts[0];
        this.lastName = parts[1];
    }
}
