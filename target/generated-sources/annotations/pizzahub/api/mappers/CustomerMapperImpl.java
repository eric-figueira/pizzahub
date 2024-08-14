package pizzahub.api.mappers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pizzahub.api.entities.user.customer.Customer;
import pizzahub.api.entities.user.customer.data.CustomerResponse;
import pizzahub.api.entities.user.customer.data.SaveCustomerParameters;
import pizzahub.api.entities.user.customer.data.SaveDeliveryCustomerParameters;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-14T09:08:31-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19.0.2 (Azul Systems, Inc.)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public Customer fromCreateParametersToEntity(SaveCustomerParameters parameters) {
        if ( parameters == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setFullname( parameters.fullname() );
        customer.setCpf( parameters.cpf() );

        return customer;
    }

    @Override
    public Customer fromCreateDeliveryParametersToEntity(SaveDeliveryCustomerParameters parameters) {
        if ( parameters == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setFullname( parameters.fullname() );
        customer.setCpf( parameters.cpf() );
        customer.setEmail( parameters.email() );
        customer.setPassword( parameters.password() );
        customer.setCep( parameters.cep() );
        customer.setComplement( parameters.complement() );
        customer.setAddressNumber( parameters.addressNumber() );

        return customer;
    }

    @Override
    public CustomerResponse fromEntityToResponse(Customer entity) {
        if ( entity == null ) {
            return null;
        }

        String fullname = null;
        String cpf = null;
        String email = null;
        String cep = null;
        String streetName = null;
        String neighborhood = null;
        String city = null;
        String uf = null;
        String complement = null;
        Short addressNumber = null;

        fullname = entity.getFullname();
        cpf = entity.getCpf();
        email = entity.getEmail();
        cep = entity.getCep();
        streetName = entity.getStreetName();
        neighborhood = entity.getNeighborhood();
        city = entity.getCity();
        uf = entity.getUf();
        complement = entity.getComplement();
        addressNumber = entity.getAddressNumber();

        Integer numberOfOrders = null;
        List<Short> ordersNumbers = null;

        CustomerResponse customerResponse = new CustomerResponse( fullname, cpf, email, cep, streetName, neighborhood, city, uf, complement, addressNumber, numberOfOrders, ordersNumbers );

        return customerResponse;
    }

    @Override
    public List<CustomerResponse> fromEntityListToResponseList(List<Customer> list) {
        if ( list == null ) {
            return null;
        }

        List<CustomerResponse> list1 = new ArrayList<CustomerResponse>( list.size() );
        for ( Customer customer : list ) {
            list1.add( fromEntityToResponse( customer ) );
        }

        return list1;
    }

    @Override
    public void updateCustomer(Customer target, SaveCustomerParameters parameters) {
        if ( parameters == null ) {
            return;
        }

        target.setFullname( parameters.fullname() );
        target.setCpf( parameters.cpf() );
    }

    @Override
    public void updateCustomer(Customer target, SaveDeliveryCustomerParameters parameters) {
        if ( parameters == null ) {
            return;
        }

        target.setFullname( parameters.fullname() );
        target.setCpf( parameters.cpf() );
        target.setEmail( parameters.email() );
        target.setPassword( parameters.password() );
        target.setCep( parameters.cep() );
        target.setComplement( parameters.complement() );
        target.setAddressNumber( parameters.addressNumber() );
    }
}
