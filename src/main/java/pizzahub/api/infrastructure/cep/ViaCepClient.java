package pizzahub.api.infrastructure.cep;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "https://api.postmon.com.br/v1/cep/", name = "postmon")
public interface ViaCepClient {
    @GetMapping("{cep}")
    Address fetchAddressByCep(@PathVariable("cep") String cep);
}
