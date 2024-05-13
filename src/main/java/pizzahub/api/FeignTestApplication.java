package pizzahub.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import pizzahub.api.infrastructure.cep.Address;
import pizzahub.api.infrastructure.cep.ViaCepClient;

@SpringBootApplication
public class FeignTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeignTestApplication.class, args);
	}

    @Bean
    public CommandLineRunner run(ViaCepClient client) {
        return args -> {
            if (args.length > 0) {
                String cepToValidate = args[0];
                Address address = client.fetchAddressByCep(cepToValidate);
                System.out.println(address);
            }
        };
    }

}
