package io.spring.RestServiceTutorial.payroll.controller;

import io.spring.RestServiceTutorial.payroll.assembler.OrderResourceAssembler;
import io.spring.RestServiceTutorial.payroll.exception.OrderNotFoundException;
import io.spring.RestServiceTutorial.payroll.order.Order;
import io.spring.RestServiceTutorial.payroll.order.Status;
import io.spring.RestServiceTutorial.payroll.repository.OrderRepository;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class OrderController {
    private OrderRepository repository;
    private OrderResourceAssembler assembler;

    public OrderController(OrderRepository repository, OrderResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/orders")
    public Resources<Resource<Order>> getOrders(){

        List<Resource<Order>> orders = repository.findAll().stream()
                .map(order -> assembler.toResource(order))
                .collect(Collectors.toList());

        return new Resources<>(orders,
                linkTo(methodOn(OrderController.class).getOrders()).withSelfRel());
    }

    @GetMapping("/orders/{id}")
    public Resource<Order> getOrderById(@PathVariable Long id){
        Order order = repository.findById(id).orElseThrow(() ->new OrderNotFoundException(id));

        return assembler.toResource(order);
    }

    @PostMapping("/orders")
    public ResponseEntity<Resource<Order>> addOrder(@RequestBody Order order){
        order.setStatus(Status.IN_PROGRESS);
        repository.save(order);

        return ResponseEntity.created(linkTo(methodOn(OrderController.class).getOrderById(order.getId())).toUri())
                .body(assembler.toResource(order));
    }

    @DeleteMapping("/orders/{id}/cancel")
    public ResponseEntity<ResourceSupport> cancel(@PathVariable Long id){
        Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        if(order.getStatus() == Status.IN_PROGRESS){
            order.setStatus(Status.CANCELED);
            return ResponseEntity.ok(assembler.toResource(repository.save(order)));
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed",
                        "You can't cancel an order that is in the " + order.getStatus() + " status"));
    }

    @PutMapping("/orders/{id}/complete")
    public ResponseEntity<ResourceSupport> complete(@PathVariable Long id){
        Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        if(order.getStatus() == Status.IN_PROGRESS){
            order.setStatus(Status.COMPLETED);
            return ResponseEntity.ok(assembler.toResource(repository.save(order)));
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed",
                        "You can't complete an order that is in the " + order.getStatus() + " status"));
    }
}
