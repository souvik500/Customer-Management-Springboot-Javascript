package com.example.Customer_Details_Demo.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.Customer_Details_Demo.entity.Customer;
import com.example.Customer_Details_Demo.services.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private RestTemplate restTemplate;

	@PostMapping
	public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
		Customer newCustomer = customerService.createCustomer(customer);
		return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
		Customer updatedCustomer = customerService.updateCustomer(id, customer);
		return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllCustomers(@RequestParam(required = false) String search,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String sortDir) {

		Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

		Page<Customer> customers = customerService.getAllCustomers(search, pageable);

		Map<String, Object> response = new HashMap<>();
		response.put("customers", customers.getContent());
		response.put("currentPage", customers.getNumber());
		response.put("totalItems", customers.getTotalElements());
		response.put("totalPages", customers.getTotalPages());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
		Customer customer = customerService.getCustomerById(id);
		return new ResponseEntity<>(customer, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> deleteCustomer(@PathVariable Long id) {
		customerService.deleteCustomer(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping("/sync")
	public ResponseEntity<String> syncCustomers() {
		try {
			// This is a placeholder for the actual API call
			// In a real implementation, you would use the authentication API first
			// to get the token, then use that token to call the customer list API

			// For demonstration purposes, we'll simulate the API response
			String authUrl = "https://qa.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";
			String customerListUrl = "https://qa.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list";

			// This would be the actual implementation
			/*
			 * // First, authenticate to get the token Map<String, String> authRequest = new
			 * HashMap<>(); authRequest.put("login_id", "test@sunbasedata.com");
			 * authRequest.put("password", "Test@123");
			 * 
			 * ResponseEntity<Map> authResponse = restTemplate.postForEntity(authUrl,
			 * authRequest, Map.class); String token = (String)
			 * authResponse.getBody().get("token");
			 * 
			 * // Then, use the token to get the customer list HttpHeaders headers = new
			 * HttpHeaders(); headers.set("Authorization", "Bearer " + token);
			 * HttpEntity<String> entity = new HttpEntity<>(headers);
			 * 
			 * ResponseEntity<Customer[]> response = restTemplate.exchange( customerListUrl,
			 * HttpMethod.GET, entity, Customer[].class );
			 * 
			 * Customer[] externalCustomers = response.getBody();
			 */

			// For demonstration, we'll create some dummy customers
			Customer[] externalCustomers = {
					new Customer(null, "uuid1", "John", "Doe", "Main St", "Apt 1", "New York", "NY", "john@example.com",
							"1234567890"),
					new Customer(null, "uuid2", "Jane", "Smith", "Second St", "Apt 2", "Los Angeles", "CA",
							"jane@example.com", "0987654321") };

			customerService.syncCustomers(Arrays.asList(externalCustomers));

			return new ResponseEntity<>("Customers synced successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Failed to sync customers: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
