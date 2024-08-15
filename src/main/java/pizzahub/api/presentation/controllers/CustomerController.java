package pizzahub.api.presentation.controllers;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pizzahub.api.presentation.Response;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import pizzahub.api.entities.user.customer.Customer;
import pizzahub.api.entities.user.customer.data.SaveCustomerParameters;
import pizzahub.api.entities.user.customer.data.SaveDeliveryCustomerParameters;
import pizzahub.api.entities.user.customer.data.UpdateCustomerPartialParameters;
import pizzahub.api.infrastructure.cep.Address;
import pizzahub.api.infrastructure.cep.ViaCepClient;
import pizzahub.api.mappers.CustomerMapper;
import pizzahub.api.repositories.CustomerRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(value = "/customers")
public class CustomerController {
    @Autowired
    private CustomerRepository repository;
    @Autowired
    private CustomerMapper mapper;
    @Autowired
    private ViaCepClient cepClient;

    @GetMapping
    public ResponseEntity<Response> fetchAll(
            @RequestParam(value = "page", defaultValue = "1") short page,
            @RequestParam(value = "perPage", defaultValue = "10") short perPage,
            @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
            @RequestParam(value = "order", defaultValue = "asc") String order) {
        List<Customer> all = this.repository.findAll();

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

        List<Customer> paginated = all.subList(start, end);

        if ("fullname".equals(orderBy)) {
            if (order.equalsIgnoreCase("asc"))
                paginated.sort(Comparator.comparing(Customer::getFullName));
            else
                paginated.sort(Comparator.comparing(Customer::getFullName).reversed());
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response(
                        "Successfully fetched all customers",
                        this.mapper.fromEntityListToResponseList(paginated)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> fetchByCPF(@PathVariable UUID id) {
        Customer customer = this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Could not fetch pizzeria with specified code"));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response("Succesfully fetched customer with specified cpf",
                        this.mapper.fromEntityToResponse(customer)));
    }

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody @Valid SaveCustomerParameters body) {
        Customer customer = this.mapper.fromCreateParametersToEntity(body);

        Customer created = this.repository.save(customer);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response(
                        "Successfully created new customer",
                        this.mapper.fromEntityToResponse(created)));
    }

    @PostMapping("/delivery")
    public ResponseEntity<Response> createDelivery(@RequestBody @Valid SaveDeliveryCustomerParameters body) {
        Customer customer = this.mapper.fromCreateDeliveryParametersToEntity(body);

        String cep = body.cep();

        Address address = cepClient.fetchAddressByCep(cep);
        customer.setCity(address.getCidade());
        customer.setNeighborhood(address.getBairro());
        customer.setUf(address.getEstado());
        customer.setStreetName(address.getLogradouro());

        Customer created = this.repository.save(customer);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response(
                        "Successfully created new customer",
                        this.mapper.fromEntityToResponse(created)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable(name = "id") UUID id) {
        Customer customer = this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Could not fetch customer with specified id in order to remove it"));

        this.repository.deleteById(customer.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response("Successfully deleted customer with specified id", null));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response> updatePartial(
            @PathVariable("id") UUID id,
            @RequestBody @Valid UpdateCustomerPartialParameters body) {
        Customer customer = this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Could not fetch customer with specified id in order to update it"));

        if (body.fullname() != null)
            customer.setFullName(body.fullname());
        if (body.email() != null)
            customer.setEmail(body.email());
        if (body.password() != null)
            customer.setPassword(body.password());

        if (body.cep() != null) {
            String cep = body.cep();

            Address address = cepClient.fetchAddressByCep(cep);

            customer.setCity(address.getCidade());
            customer.setNeighborhood(address.getBairro());
            customer.setUf(address.getEstado());
            customer.setStreetName(address.getLogradouro());
            customer.setCep(body.cep());
        }

        if (body.complement() != null)
            customer.setComplement(body.complement());
        if (body.addressNumber() != null)
            customer.setAddressNumber(body.addressNumber());

        Customer updated = this.repository.save(customer);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response(
                        "Successfully updated pizzeria",
                        this.mapper.fromEntityToResponse(updated)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(
            @PathVariable("id") UUID id,
            @RequestBody @Valid SaveCustomerParameters body) {
        Customer customer = this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Could not fetch customer with specified id in order to update it"));

        this.mapper.updateCustomer(customer, body);

        Customer updated = this.repository.save(customer);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response(
                        "Successfully updated pizzeria",
                        this.mapper.fromEntityToResponse(updated)));
    }

    @PutMapping("/delivery/{id}")
    public ResponseEntity<Response> update(
            @PathVariable("id") UUID id,
            @RequestBody @Valid SaveDeliveryCustomerParameters body) {
        Customer customer = this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Could not fetch customer with specified id in order to update it"));

        String cep = body.cep();

        Address address = cepClient.fetchAddressByCep(cep);

        customer.setCity(address.getCidade());
        customer.setNeighborhood(address.getBairro());
        customer.setUf(address.getEstado());
        customer.setStreetName(address.getLogradouro());
        customer.setCep(body.cep());

        this.mapper.updateCustomer(customer, body);

        Customer updated = this.repository.save(customer);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response(
                        "Successfully updated pizzeria",
                        this.mapper.fromEntityToResponse(updated)));
    }
}
