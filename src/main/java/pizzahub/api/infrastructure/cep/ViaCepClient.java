package pizzahub.api.infrastructure.cep;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient()
public interface ViaCepClient {
    @GetMapping("{cep}/json")
    Address fetchAddressByCep(@PathVariable("cep") String cep);
}
