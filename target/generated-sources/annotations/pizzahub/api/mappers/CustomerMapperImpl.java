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
    date = "2024-08-09T12:45:11-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.39.0.v20240725-1906, environment: Java 17.0.11 (Eclipse Adoptium)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public Customer fromCreateParametersToEntity(SaveCustomerParameters parameters) {
        if ( parameters == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setCpf( parameters.cpf() );
        customer.setFullname( parameters.fullname() );

        return customer;
    }

    @Override
    public Customer fromCreateDeliveryParametersToEntity(SaveDeliveryCustomerParameters parameters) {
        if ( parameters == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setAddressNumber( parameters.addressNumber() );
        customer.setCep( parameters.cep() );
        customer.setComplement( parameters.complement() );
        customer.setCpf( parameters.cpf() );
        customer.setEmail( parameters.email() );
        customer.setFullname( parameters.fullname() );
        customer.setPassword( parameters.password() );

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

        target.setCpf( parameters.cpf() );
        target.setFullname( parameters.fullname() );
    }

    @Override
    public void updateCustomer(Customer target, SaveDeliveryCustomerParameters parameters) {
        if ( parameters == null ) {
            return;
        }

        target.setAddressNumber( parameters.addressNumber() );
        target.setCep( parameters.cep() );
        target.setComplement( parameters.complement() );
        target.setCpf( parameters.cpf() );
        target.setEmail( parameters.email() );
        target.setFullname( parameters.fullname() );
        target.setPassword( parameters.password() );
    }
}
