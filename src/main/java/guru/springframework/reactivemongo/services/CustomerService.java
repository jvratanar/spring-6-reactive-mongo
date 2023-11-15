package guru.springframework.reactivemongo.services;

import guru.springframework.reactivemongo.model.BeerDTO;
import guru.springframework.reactivemongo.model.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Mono<CustomerDTO> saveCustomer(Mono<CustomerDTO> customerDTO);
    Mono<CustomerDTO> saveCustomer(CustomerDTO customerDTO);
    Mono<CustomerDTO> getCustomerById(String customerId);
    Mono<CustomerDTO> updateCustomer(String customerId, CustomerDTO customerDTO);
    Mono<CustomerDTO> patchCustomer(String customerId, CustomerDTO customerDTO);
    Mono<Void> deleteCustomerById(String customerId);
    Flux<CustomerDTO> listCustomers();
}
