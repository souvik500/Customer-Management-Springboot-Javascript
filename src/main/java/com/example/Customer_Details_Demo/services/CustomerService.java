package com.example.Customer_Details_Demo.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.Customer_Details_Demo.entity.Customer;
import com.example.Customer_Details_Demo.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	public Customer createCustomer(Customer customer) {
		if (customer.getUuid() == null) {
			customer.setUuid(UUID.randomUUID().toString());
		}
		return customerRepository.save(customer);
	}

	public Customer updateCustomer(Long id, Customer customerDetails) {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

		customer.setFirstName(customerDetails.getFirstName());
		customer.setLastName(customerDetails.getLastName());
		customer.setStreet(customerDetails.getStreet());
		customer.setAddress(customerDetails.getAddress());
		customer.setCity(customerDetails.getCity());
		customer.setState(customerDetails.getState());
		customer.setEmail(customerDetails.getEmail());
		customer.setPhone(customerDetails.getPhone());

		return customerRepository.save(customer);
	}

	public Page<Customer> getAllCustomers(String search, Pageable pageable) {
		if (search != null && !search.isEmpty()) {
			return customerRepository.findAllWithSearch(search, pageable);
		}
		return customerRepository.findAll(pageable);
	}

	public Customer getCustomerById(Long id) {
		return customerRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
	}

	public void deleteCustomer(Long id) {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
		customerRepository.delete(customer);
	}

	public Customer syncCustomer(Customer externalCustomer) {
		Optional<Customer> existingCustomer = customerRepository.findByUuid(externalCustomer.getUuid());

		if (existingCustomer.isPresent()) {
			Customer customer = existingCustomer.get();
			customer.setFirstName(externalCustomer.getFirstName());
			customer.setLastName(externalCustomer.getLastName());
			customer.setStreet(externalCustomer.getStreet());
			customer.setAddress(externalCustomer.getAddress());
			customer.setCity(externalCustomer.getCity());
			customer.setState(externalCustomer.getState());
			customer.setEmail(externalCustomer.getEmail());
			customer.setPhone(externalCustomer.getPhone());
			return customerRepository.save(customer);
		} else {
			return customerRepository.save(externalCustomer);
		}
	}

	public void syncCustomers(List<Customer> externalCustomers) {
		for (Customer customer : externalCustomers) {
			syncCustomer(customer);
		}
	}
}
