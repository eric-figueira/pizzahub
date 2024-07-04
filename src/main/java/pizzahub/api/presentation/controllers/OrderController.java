package pizzahub.api.presentation.controllers;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import pizzahub.api.entities.order.data.UpdateOrderParameters;
import pizzahub.api.mappers.OrderMapper;
import pizzahub.api.repositories.CustomerRepository;
import pizzahub.api.repositories.OrderRepository;

import pizzahub.api.entities.order.Order;
import pizzahub.api.entities.order.OrderStatus;
import pizzahub.api.entities.order.data.CreateOrderParameters;
import pizzahub.api.entities.order.data.OrderResponse;
import pizzahub.api.entities.user.customer.Customer;

import pizzahub.api.presentation.Response;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderRepository repository;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public ResponseEntity<Response> fetchOrders(
            @RequestParam(value = "page", defaultValue = "1") short page,
            @RequestParam(value = "perPage", defaultValue = "30") short perPage,
            @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
            @RequestParam(value = "order", defaultValue = "asc") String order
    ) {
        List<Order> all = this.repository.findAll();

        // pagination
        short start = (short) ((page - 1) * perPage);

        double numberOfGroups = (double) all.size() / perPage;
        short lastGroupNumber = (short) Math.ceil(numberOfGroups);

        short end = page == lastGroupNumber ? (short) all.size() : (short) (page * perPage);

        if (start >= all.size()) {
            throw new IllegalArgumentException(
                "The pagination parameters are invalid. Please ensure that 'page' is smaller than the total data size"
            );
        }

        List<Order> paginated = all.subList(start, end);

        // ordination
        switch (orderBy) {
            case "number":
                if (order.equalsIgnoreCase("asc"))
                    paginated.sort(Comparator.comparing(Order::getNumber));
                else
                    paginated.sort(Comparator.comparing(Order::getNumber).reversed());
                break;

            case "cost":
                if (order.equalsIgnoreCase("asc"))
                    paginated.sort(Comparator.comparing(Order::getCost));
                else
                    paginated.sort(Comparator.comparing(Order::getCost).reversed());
                break;

            case "shippingTax":
                if (order.equalsIgnoreCase("asc"))
                    paginated.sort(Comparator.comparing(Order::getShippingTax));
                else
                    paginated.sort(Comparator.comparing(Order::getShippingTax).reversed());
                break;

            case "orderDate":
                if (order.equalsIgnoreCase("asc"))
                    paginated.sort(Comparator.comparing(Order::getOrderDate));
                else
                    paginated.sort(Comparator.comparing(Order::getOrderDate).reversed());
                break;

            case "orderTime":
                if (order.equalsIgnoreCase("asc"))
                    paginated.sort(Comparator.comparing(Order::getOrderTime));
                else
                    paginated.sort(Comparator.comparing(Order::getOrderTime).reversed());

            default:
                break;
        }

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully fetched all orders",
                paginated
            ));
    }

    @GetMapping("/{number}")
    public ResponseEntity<Response> fetchByNumber(@PathVariable("number") Short orderNumber) {
        Order order = this.repository.findByOrderNumber(orderNumber)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch order with specified number"));

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully fetched order with specified number",
                OrderMapper.modelToResponse(order)));
    }

    @PostMapping()
    public ResponseEntity<Response> create(@RequestBody @Valid CreateOrderParameters body) {
        Order order = new Order();
        order.setNumber(body.number());
        order.setPaymentMethod(body.paymentMethod());
        order.setShippingTax(body.shippingTax());
        order.setCost(body.cost());

        Customer customer = this.customerRepository
                            .findById(body.customerId())
                            .orElseThrow(() -> new EntityNotFoundException(
                                "Failed to retrieve customer informed by ID"
                            ));
        order.setCustomer(customer);

        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(new Date());
        order.setOrderTime(LocalTime.now());

        Order created = this.repository.save(order);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully created new order",
                OrderMapper.modelToResponse(created)
            ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable("id") Long orderId) {
        Order exists = this.repository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch order with specified number"));

        this.repository.deleteById(orderId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response("Successfully deleted order with specified number", null));
    }

    @PutMapping("/{id}")
     public ResponseEntity<Response> update(
         @PathVariable("id") Long id,
         @RequestBody @Valid UpdateOrderParameters body
    ) {
        Order current = this.repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch order with specified number"));

        // to implement
    }
}
