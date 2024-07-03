package pizzahub.api.presentation.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
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
import pizzahub.api.entities.order.OrderStatus;
import pizzahub.api.entities.order.data.CreateOrderRequestDTO;
import pizzahub.api.entities.order.data.OrderResponseDTO;
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
            @RequestParam(value = "order", defaultValue = "asc") String order,
            @RequestParam(value = "groupBy", defaultValue = "") String groupBy
    ) {
        List<Order> all = this.repository.findAll();

        // pagination
        short start = (short) ((page - 1) * perPage);
        short end = 1;

        double numberOfGroups = (double) all.size() / perPage;
        short lastGroupNumber = (short) Math.ceil(numberOfGroups);

        if (page == lastGroupNumber) {
            // pagination refers to last page
            end = (short) all.size();
        } else {
            end = (short) (page * perPage);
        }

        if (start >= all.size()) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new Response(
                    "The pagination parameters are invalid. Please ensure that 'page' is smaller than the total data size",
                    null
                ));
        }

        List<Order> paginated = all.subList(start, end);

        // ordenation
        switch (orderBy) {
            case "number":
                if (order.equalsIgnoreCase("asc"))
                    paginated.sort(Comparator.comparing(Order::getNumber));
                else
                    paginated.sort(Comparator.comparing(Order::getNumber).reversed());
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
                paginated.stream()
                    .map(item -> item.convertToResponseDTO())
                    .collect(Collectors.toList())
            ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> fetchOrderById(@PathVariable("id") Long orderId) {
        Optional<Order> orderOptional = this.repository.findById(orderId);

        if (orderOptional.isPresent()) {
            Order menuItem = orderOptional.get();

            OrderResponseDTO response = menuItem.convertToResponseDTO();

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response("Successfully fetched order with specified id", response));
        }
        else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Response("Could not fetch order with specified ID", null));
        }
    }

    @PostMapping()
    public ResponseEntity<Response> createOrder(@RequestBody @Valid CreateOrderRequestDTO body) {
        LocalDate current = LocalDate.now();

        try {
            Order order = new Order(body);

            try {
                Customer customer = this.customerRepository
                                    .findById(body.customerId())
                                    .orElseThrow(() -> new EntityNotFoundException());
                order.setCustomer(customer);
            } catch (EntityNotFoundException error) {
                return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new Response("Failed to retrieve customer informed by ID", null));
            }

            order.setOrderStatus(OrderStatus.NOT_INITIATED);

            //order.setOrderDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            order.setOrderTime(LocalTime.now());

            Order savedOrder = this.repository.save(order);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response(
                    "Successfully created new order",
                    savedOrder.convertToResponseDTO()
                ));

        } catch (Exception error) {
            return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new Response("Failed to create order", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteOrder(@PathVariable("id") Long orderId) {
        return this.repository.findById(orderId)
            .map(order -> {
                this.repository.deleteById(orderId);

                return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new Response(
                        "Successfully deleted order with specified id",
                        null));
            })
            .orElseGet(() -> ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Response(
                    "Could not retrieve order with specified id in order to remove it",
                    null)));
    }

    // @PutMapping
    // public ResponseEntity<Response> putOrder(@RequestBody @Valid UpdateOrderRequestDTO body) {
    //     //TODO: process PUT request

    //     return ResponseEntity.ok().body(new Response());
    // }
}
