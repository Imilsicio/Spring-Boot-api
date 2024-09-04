package com.First;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.First.FirstSteps.Customer;
import com.First.FirstSteps.CustomerRepository;

@SpringBootApplication
@RestController
@RequestMapping("api/v1/customers")
public class Main {

    private final CustomerRepository customerRepository;

    public Main(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    // @GetMapping("/khawir")
    // public String greet(){
    // return "Hello";
    // }

    @GetMapping
    public List<Customer> getCustomer() {
        return customerRepository.findAll();
    }

    record NewCustomerRequest(
            String name,
            String email,
            Integer age) {

    }

    @PostMapping
    public void addCustomer(@RequestBody NewCustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setAge(request.age());
        customerRepository.save(customer);

    }

    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable("customerId") Integer id) {
        customerRepository.deleteById(id);
    }

    @PutMapping("{customerId}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("customerId") Integer id,
            @RequestBody NewCustomerRequest request) {

        // Verifica se o cliente existe no banco de dados
        Optional<Customer> optionalCustomer = customerRepository.findById(id);

        if (optionalCustomer.isPresent()) {
            // Atualiza as informações do cliente
            Customer customer = optionalCustomer.get();
            customer.setName(request.name());
            customer.setEmail(request.email());
            customer.setAge(request.age());

            // Salva as mudanças no banco de dados
            customerRepository.save(customer);

            return ResponseEntity.ok(customer);
        } else {
            // Retorna uma resposta 404 caso o cliente não seja encontrado
            return ResponseEntity.notFound().build();
        }
    }

}
