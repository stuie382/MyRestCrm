package com.stuart.mycrm.rest;

import com.stuart.mycrm.dto.CustomerDTO;
import com.stuart.mycrm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

    private static final int ID_ZERO = 0;

    // autowired  CustomerService
    private final CustomerService customerService;

    @Autowired
    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public List<CustomerDTO> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("/customers/{customerId}")
    public CustomerDTO getCustomer(@PathVariable int customerId) {
        CustomerDTO customer = customerService.getCustomer(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer id not found - " + customerId);
        }
        return customer;
    }

    @PostMapping("/customers")
    public CustomerDTO addCustomer(@RequestBody CustomerDTO recordToAdd) {
        // Just in case the pass an id in JSON, set id to 0
        // this is force a save of new item, else it's an update
        recordToAdd.setId(ID_ZERO);
        customerService.saveCustomer(recordToAdd);
        return recordToAdd;
    }

    @PutMapping("/customers")
    public CustomerDTO updateCustomer(@RequestBody CustomerDTO theCustomer) {
        customerService.saveCustomer(theCustomer);
        return theCustomer;

    }

    @DeleteMapping("/customers/{customerId}")
    public String deleteCustomer(@PathVariable int customerId) {
        CustomerDTO tempCustomer = customerService.getCustomer(customerId);
        if (tempCustomer == null) {
            throw new CustomerNotFoundException("Customer id not found - " + customerId);
        }
        customerService.deleteCustomer(customerId);
        return "Deleted customer id - " + customerId;
    }

}


















