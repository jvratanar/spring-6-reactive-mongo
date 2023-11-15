package guru.springframework.reactivemongo.services;

import guru.springframework.reactivemongo.mappers.CustomerMapper;
import guru.springframework.reactivemongo.model.BeerDTO;
import guru.springframework.reactivemongo.model.CustomerDTO;
import guru.springframework.reactivemongo.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Mono<CustomerDTO> saveCustomer(Mono<CustomerDTO> customerDTO) {
        return customerDTO
                .map(this.customerMapper::customerDTOToCustomer)
                .flatMap(this.customerRepository::save)
                .map(this.customerMapper::customerToCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> saveCustomer(CustomerDTO customerDTO) {
        return null;
    }

    @Override
    public Mono<CustomerDTO> getCustomerById(String customerId) {
        return this.customerRepository.findById(customerId)
                .map(this.customerMapper::customerToCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> updateCustomer(String customerId, CustomerDTO customerDTO) {
        return this.customerRepository.findById(customerId)
                .map(customer -> {
                    customer.setCustomerName(customerDTO.getCustomerName());
                    return customer;
                })
                .flatMap(this.customerRepository::save)
                .map(this.customerMapper::customerToCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> patchCustomer(String customerId, CustomerDTO customerDTO) {
        return this.customerRepository.findById(customerId)
                .map(customer -> {
                    if (StringUtils.hasText(customerDTO.getCustomerName())) {
                        customer.setCustomerName(customerDTO.getCustomerName());
                    }
                    return customer;
                })
                .flatMap(this.customerRepository::save)
                .map(this.customerMapper::customerToCustomerDTO);
    }

    @Override
    public Mono<Void> deleteCustomerById(String customerId) {
        return this.customerRepository.deleteById(customerId);
    }

    @Override
    public Flux<CustomerDTO> listCustomers() {
        return this.customerRepository.findAll()
                .map(this.customerMapper::customerToCustomerDTO);
    }
}
