package pizzahub.api.presentation.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import pizzahub.api.repositories.CustomerRepository;
import pizzahub.api.repositories.OrderRepository;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import pizzahub.api.entities.order.Order;
import pizzahub.api.entities.order.data.CreateOrderRequestDTO;
import pizzahub.api.entities.user.customer.Customer;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderRepository repository;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public ResponseEntity<List<Order>> fetchOrders(
            @RequestParam(value = "page", defaultValue = "1") short page,
            @RequestParam(value = "perPage", defaultValue = "30") short perPage) {
        List<Order> orders = this.repository.findAll();

        short start = (short) ((page - 1) * perPage);
        short end = (short) (page * perPage);

        if (start < orders.size() || end >= orders.size()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ArrayList<Order>());
        }

        List<Order> paginated = orders.subList(start, end);

        return ResponseEntity.ok(paginated);
    }

    @GetMapping("/{id}")
    public Optional<Order> fetchOrderById(@PathVariable("id") Long orderId) {
        return repository.findById(orderId);
    }

    // @PostMapping()
    // public ResponseEntity createOrder(@RequestBody @Valid CreateOrderRequestDTO body) {
    //     try {
    //         Order order = new Order(body);

    //         Optional<Customer> customer = this.customerRepository.findById(body.clientId());
    //         if (!customer.isPresent()) {
    //             return ResponseEntity.badRequest().build();
    //         }

    //         this.repository.save(order);
    //         return ResponseEntity.ok(order.getId());
    //     } catch (Exception error) {
    //         return ResponseEntity.unprocessableEntity().build();
    //     }
    // }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteOrder(@PathVariable("id") Long orderId) {
        this.repository.deleteById(orderId);
        return ResponseEntity.ok().build();
    }

    // @PutMapping
    // public ResponseEntity<Response> putOrder(@RequestBody @Valid UpdateOrderRequestDTO body) {
    //     //TODO: process PUT request

    //     return ResponseEntity.ok().body(new Response());
    // }
}
