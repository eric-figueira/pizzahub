package pizzahub.api.mappers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pizzahub.api.entities.pizzeria.Pizzeria;
import pizzahub.api.entities.pizzeria.data.PizzeriaResponse;
import pizzahub.api.entities.pizzeria.data.SavePizzeriaParameters;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-09T12:33:13-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.39.0.v20240725-1906, environment: Java 17.0.11 (Eclipse Adoptium)"
)
@Component
public class PizzeriaMapperImpl implements PizzeriaMapper {

    @Override
    public PizzeriaResponse fromEntityToResponse(Pizzeria entity) {
        if ( entity == null ) {
            return null;
        }

        Short code = null;
        String firstContact = null;
        String secondContact = null;
        String email = null;
        String cep = null;
        String streetName = null;
        String neighborhood = null;
        String city = null;
        String uf = null;
        String complement = null;
        Short addressNumber = null;
        Date createdAt = null;

        code = entity.getCode();
        firstContact = entity.getFirstContact();
        secondContact = entity.getSecondContact();
        email = entity.getEmail();
        cep = entity.getCep();
        streetName = entity.getStreetName();
        neighborhood = entity.getNeighborhood();
        city = entity.getCity();
        uf = entity.getUf();
        complement = entity.getComplement();
        addressNumber = entity.getAddressNumber();
        createdAt = entity.getCreatedAt();

        List<Long> workersIds = null;

        PizzeriaResponse pizzeriaResponse = new PizzeriaResponse( code, firstContact, secondContact, email, cep, streetName, neighborhood, city, uf, complement, addressNumber, createdAt, workersIds );

        return pizzeriaResponse;
    }

    @Override
    public Pizzeria fromSaveParametersToEntity(SavePizzeriaParameters parameters) {
        if ( parameters == null ) {
            return null;
        }

        Pizzeria pizzeria = new Pizzeria();

        pizzeria.setAddressNumber( parameters.addressNumber() );
        pizzeria.setCode( parameters.code() );
        pizzeria.setComplement( parameters.complement() );
        pizzeria.setEmail( parameters.email() );
        pizzeria.setFirstContact( parameters.firstContact() );
        pizzeria.setSecondContact( parameters.secondContact() );

        return pizzeria;
    }

    @Override
    public List<PizzeriaResponse> fromEntityListToResponseList(List<Pizzeria> list) {
        if ( list == null ) {
            return null;
        }

        List<PizzeriaResponse> list1 = new ArrayList<PizzeriaResponse>( list.size() );
        for ( Pizzeria pizzeria : list ) {
            list1.add( fromEntityToResponse( pizzeria ) );
        }

        return list1;
    }

    @Override
    public void updatePizzeria(Pizzeria target, SavePizzeriaParameters parameters) {
        if ( parameters == null ) {
            return;
        }

        target.setAddressNumber( parameters.addressNumber() );
        target.setCode( parameters.code() );
        target.setComplement( parameters.complement() );
        target.setEmail( parameters.email() );
        target.setFirstContact( parameters.firstContact() );
        target.setSecondContact( parameters.secondContact() );
    }
}
