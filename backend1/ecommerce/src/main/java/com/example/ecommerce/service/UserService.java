package com.example.ecommerce.service;


import com.example.ecommerce.dto.request.CustomerRequest;
import com.example.ecommerce.dto.request.LoginRequest;
import com.example.ecommerce.dto.response.CustomerResponse;
import com.example.ecommerce.exception.DataNotFoundException;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.security.CustomCustomerDetails;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new CustomCustomerDetails(customer);
    }

    @Transactional
    public CustomerResponse registerUser(CustomerRequest customerRequest) {
        if (!StringUtils.hasText(customerRequest.getUsername())) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (!StringUtils.hasText(customerRequest.getEmail())) {
            throw new IllegalArgumentException("Email is required.");
        }
        if (!StringUtils.hasText(customerRequest.getPassword())) {
            throw new IllegalArgumentException("Password is required.");
        }

        if (customerRepository.findByUsername(customerRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (customerRepository.findByEmail(customerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }


        Customer customer = new Customer();
        customer.setUsername(customerRequest.getUsername());
        customer.setEmail(customerRequest.getEmail());
        customer.setPassword(passwordEncoder.encode(customerRequest.getPassword()));
        customer.setRole(customerRequest.getRole() == null ? "CUSTOMER" : customerRequest.getRole());
                Customer savedCustomer = customerRepository.save(customer);

        return convertToResponse(savedCustomer);
    }

    @Transactional
    public CustomerResponse loginUser(LoginRequest loginRequest) {
        Optional<Customer> customerOptional = customerRepository.findByUsername(loginRequest.getUsername());
        if (customerOptional.isEmpty()) {
           throw new DataNotFoundException("User not found with username: " + loginRequest.getUsername());

        }
        Customer customer = customerOptional.get();


        if (!passwordEncoder.matches(loginRequest.getPassword(), customer.getPassword())) {
            throw new RuntimeException("Invalid password");
        }



        CustomerResponse response = convertToResponse(customer);

        return response;
    }

    @Transactional
    public CustomerResponse updateUser(CustomerRequest customerRequest, String username) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("User not found with username " + username));


        if (customerRequest.getUsername() != null) customer.setUsername(customerRequest.getUsername());
        if (customerRequest.getEmail() != null) customer.setEmail(customerRequest.getEmail());
        if (customerRequest.getPassword() != null) customer.setPassword(passwordEncoder.encode(customerRequest.getPassword()));  // Encrypt the password
        if (customerRequest.getRole() != null) {
            customer.setRole(customerRequest.getRole());
        }



        Customer updatedCustomer = customerRepository.save(customer);
        return convertToResponse(updatedCustomer);
    }

    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream()
                .map(this::convertToResponse)
                .toList();
    }

    public List<CustomerResponse> deleteUser(String username) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("User not found with username " + username));
        customerRepository.delete(customer);
        return findAll();
    }

    public CustomerResponse findByUsername(String username) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("User not found with username " + username));
        return convertToResponse(customer);
    }



    private CustomerResponse convertToResponse(Customer customer) {
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setUsername(customer.getUsername());
        customerResponse.setEmail(customer.getEmail());
        customerResponse.setRole(customer.getRole());
        customerResponse.setCreatedAt(customer.getCreatedAt());
        customerResponse.setUpdatedAt(customer.getUpdatedAt());
        return customerResponse;
    }
}
