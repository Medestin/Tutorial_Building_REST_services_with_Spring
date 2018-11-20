package io.spring.RestServiceTutorial.payroll.assembler;

import io.spring.RestServiceTutorial.payroll.controller.OrderController;
import io.spring.RestServiceTutorial.payroll.order.Order;
import io.spring.RestServiceTutorial.payroll.order.Status;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class OrderResourceAssembler implements ResourceAssembler<Order, Resource<Order>> {
    @Override
    public Resource<Order> toResource(Order order) {

        Resource<Order> resource = new Resource<>(order,
                linkTo(methodOn(OrderController.class).getOrderById(order.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).getOrders()).withRel("orders"));

        if(order.getStatus() == Status.IN_PROGRESS){
            resource.add(linkTo(methodOn(OrderController.class).complete(order.getId())).withRel("complete"));
            resource.add(linkTo(methodOn(OrderController.class).cancel(order.getId())).withRel("cancel"));
        }
        return resource;
    }
}