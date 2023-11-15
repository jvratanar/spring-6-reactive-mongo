package guru.springframework.reactivemongo.web.fn;

import guru.springframework.reactivemongo.domain.Customer;
import guru.springframework.reactivemongo.model.BeerDTO;
import guru.springframework.reactivemongo.model.CustomerDTO;
import guru.springframework.reactivemongo.services.BeerServiceImplTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class CustomerEndpointTest {
    @Autowired
    WebTestClient webTestClient;

    public static Customer getTestCustomer() {
        return Customer.builder()
                .customerName("Customer Test")
                .build();
    }

    @Test
    void testPatchCustomerNotFound() {
        this.webTestClient.patch().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .body(Mono.just(getTestCustomer()), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteCustomerNotFound() {
        this.webTestClient.delete().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(999)
    void testDeleteCustomer() {
        CustomerDTO dto = getSavedTestCustomer();

        this.webTestClient.delete().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, dto.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(4)
    void testUpdateCustomerBadRequest() {
        Customer customer = getTestCustomer();
        customer.setCustomerName("");

        this.webTestClient.put().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 1)
                .body(Mono.just(customer), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateCustomerNotFound() {
        this.webTestClient.put().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .body(Mono.just(getTestCustomer()), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(3)
    void testUpdateCustomer() {
        CustomerDTO testCustomer = getSavedTestCustomer();
        testCustomer.setCustomerName("New Customer Name");

        this.webTestClient.put().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, testCustomer.getId())
                .body(Mono.just(testCustomer), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testCreateCustomerBadRequest() {
        Customer customer = getTestCustomer();
        customer.setCustomerName("");

        this.webTestClient.post().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .body(Mono.just(customer), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testCreateCustomer() {
        this.webTestClient.post().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .body(Mono.just(getTestCustomer()), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("location");
    }

    @Test
    void testGetByIdNotFound() {
        this.webTestClient.get().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(2)
    void testGetById() {
        CustomerDTO dto = getSavedTestCustomer();

        this.webTestClient.get().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, dto.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody(CustomerDTO.class);
    }

    @Test
    @Order(1)
    void testListCustomers() {
        this.webTestClient.get().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody().jsonPath("$.size()").value(greaterThan(1));
    }

    public CustomerDTO getSavedTestCustomer() {
        FluxExchangeResult<CustomerDTO> customerDTOFluxExchangeResult = webTestClient.post()
                .uri(CustomerRouterConfig.CUSTOMER_PATH)
                .body(Mono.just(getCustomerDto()), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .returnResult(CustomerDTO.class);

        List<String> location = customerDTOFluxExchangeResult.getResponseHeaders().get("Location");

        return webTestClient.get().uri(location.get(0))
                .exchange().returnResult(CustomerDTO.class).getResponseBody().blockFirst();
    }

    public static CustomerDTO getCustomerDto() {
        return CustomerDTO.builder()
                .customerName("Test Customer")
                .build();
    }
}
