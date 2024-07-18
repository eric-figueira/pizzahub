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
import pizzahub.api.entities.menuitem.MenuItem;
import pizzahub.api.entities.order.data.UpdateOrderParameters;
import pizzahub.api.entities.order.data.UpdateOrderPartialParameters;
import pizzahub.api.mappers.MenuItemMapper;
import pizzahub.api.mappers.OrderMapper;
import pizzahub.api.repositories.CustomerRepository;
import pizzahub.api.repositories.MenuItemRepository;
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

    @Autowired
    private MenuItemRepository menuItemRepository;

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
        Order order = this.repository.findByNumber(orderNumber)
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
        if (body.paymentMethod() != null) order.setPaymentMethod(body.paymentMethod());
        if (body.shippingTax() != null) order.setShippingTax(body.shippingTax());
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

        current.setNumber(body.number());
        current.setPaymentMethod(body.paymentMethod());
        current.setShippingTax(body.shippingTax());
        current.setCost(body.cost());

        Customer customer = this.customerRepository
            .findById(body.customerId())
            .orElseThrow(() -> new EntityNotFoundException(
                "Failed to retrieve customer informed by ID"
            ));
        current.setCustomer(customer);
        current.setOrderStatus(body.orderStatus());

        Order updated = this.repository.save(current);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully updated order",
                OrderMapper.modelToResponse(updated)
            ));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response> updatePartial(
        @PathVariable("id") Long id,
        @RequestBody @Valid UpdateOrderPartialParameters body
    ) {
        Order current = this.repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch order with specified number"));

        if (body.number() != null) current.setNumber(body.number());
        if (body.paymentMethod() != null) current.setPaymentMethod(body.paymentMethod());
        if (body.shippingTax() != null) current.setShippingTax(body.shippingTax());
        if (body.cost() != null) current.setCost(body.cost());

        if (body.customerId() != null) {
            Customer customer = this.customerRepository
                .findById(body.customerId())
                .orElseThrow(() -> new EntityNotFoundException(
                    "Failed to retrieve customer informed by ID"
                ));
            current.setCustomer(customer);
        }
        if (body.orderStatus() != null) current.setOrderStatus(body.orderStatus());

        Order updated = this.repository.save(current);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully updated order",
                OrderMapper.modelToResponse(updated)
            ));
    }

    @PostMapping("{orderNumber}/menuitem/{menuItemId}")
    public ResponseEntity<Response> addMenuItem(
        @PathVariable("orderNumber") Short orderNumber,
        @PathVariable("menuItemId") Long menuItemId
    ) {
        Order order = this.repository.findByNumber(orderNumber)
            .orElseThrow(() -> new EntityNotFoundException("Could not retrieve order with specified number in order to add the menu item"));

        MenuItem menuItem = this.menuItemRepository.findById(menuItemId)
            .orElseThrow(() -> new EntityNotFoundException("Could not retrieve menu item with specified id in order to add it to the order"));

        order.getMenuItems().add(menuItem);
        Order updated = this.repository.save(order);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully add menu item to order",
                OrderMapper.modelToResponse(updated)
            ));
    }

    @DeleteMapping("{orderNumber}/menuitem/{menuItemId}")
    public ResponseEntity<Response> removeMenuItem(
        @PathVariable("orderNumber") Short orderNumber,
        @PathVariable("menuItemId") Long menuItemId
    ) {
        Order order = this.repository.findByNumber(orderNumber)
            .orElseThrow(() -> new EntityNotFoundException("Could not retrieve order with specified number in order to remove the menu item"));

        MenuItem menuItem = this.menuItemRepository.findById(menuItemId)
            .orElseThrow(() -> new EntityNotFoundException("Could not retrieve menu item with specified id in order to remove it from the order"));

        order.getMenuItems().remove(menuItem);
        Order updated = this.repository.save(order);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully removed menu item from menu item",
                OrderMapper.modelToResponse(updated)
            ));
    }

    @GetMapping("{orderNumber}/status")
    public ResponseEntity<Response> fetchOrderStatus(@PathVariable("orderNumber") Short orderNumber) {
        Order order = this.repository.findByNumber(orderNumber)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch order with specified number"));

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully fetched order status",
                order.getOrderStatus()));
    }

    @PatchMapping("{orderNumber}/status/{status}")
    public ResponseEntity<Response> changeOrderStatus(
        @PathVariable("orderNumber") Short orderNumber,
        @PathVariable("status") String status
    ) {
        Order order = this.repository.findByNumber(orderNumber)
            .orElseThrow(() -> new EntityNotFoundException("Could not fetch order with specified number in order to change its status"));

        OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());
        order.setOrderStatus(newStatus);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(
                "Successfully fetched order status",
                null));
    }
}
