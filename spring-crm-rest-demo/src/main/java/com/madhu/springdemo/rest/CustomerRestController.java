package com.madhu.springdemo.rest;

import com.madhu.springdemo.entity.Customer;
import com.madhu.springdemo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

    // autowire the CustomerService
    //inject dependency for customer service
    @Autowired
    private CustomerService customerService;

    // add mapping for GET /customers
    @GetMapping("/customers")
    public List<Customer> getCustomers(){
        return customerService.getCustomers();
    } //background jackson will convert POJOs to JSON

    // add mapping for GET /customers/{customerId}
    @GetMapping("/customers/{customerId}")
    public Customer getCustomer(@PathVariable int customerId){
        Customer theCustomer = customerService.getCustomer(customerId);

        if (theCustomer == null){
            throw new CustomerNotFoundException("Customer id not found - "+customerId);
        }
        //for null objects jackson returns empty body so if we don't mention if condition we don't get http message
        return theCustomer;
    }

    // add mapping for POST /customers - add a new customer
    @PostMapping("/customers")
    public Customer addCustomer(@RequestBody Customer theCustomer){

        // also, just in case they pass an id in JSON ... set id to 0
        // this is force a save of a new item ... instead of update

        theCustomer.setId(0); // zero means empty which helps to say to hibernate session that insert new customer
        customerService.saveCustomer(theCustomer); //use @RequestBody to access the request body as a pojo
        return theCustomer;

    }

    // add mapping for PUT /customers - update existing customer
    @PutMapping("/customers")
    public Customer updateCustomer(@RequestBody Customer theCustomer){
        customerService.saveCustomer(theCustomer); //since customer id is set, DAO will update customer in the database
        return theCustomer;
    }

    // add mapping for DELETE /customers/{customerId} - delete customer
    @DeleteMapping("/customers/{customerId}")
    public String deleteCustomer(@PathVariable int customerId){

        Customer tempCustomer = customerService.getCustomer(customerId);
        // throw exception if null
        if (tempCustomer == null){
            throw new CustomerNotFoundException("Customer id not found - "+customerId);
        }
        customerService.deleteCustomer(customerId);
        return "Deleted customer id - "+customerId;
    }


}
