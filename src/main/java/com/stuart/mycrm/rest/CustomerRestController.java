package com.stuart.mycrm.rest;

import com.stuart.mycrm.entity.Customer;
import com.stuart.mycrm.record.CustomerRecord;
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
    public List<CustomerRecord> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("/customers/{customerId}")
    public CustomerRecord getCustomer(@PathVariable int customerId) {
        CustomerRecord customer = customerService.getCustomer(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer id not found - " + customerId);
        }
        return customer;
    }

    @PostMapping("/customers")
    public CustomerRecord addCustomer(@RequestBody CustomerRecord recordToAdd) {
        // Just in case the pass an id in JSON, set id to 0
        // this is force a save of new item, else it's an update
        CustomerRecord sanitisedRecord = new CustomerRecord(ID_ZERO,
                recordToAdd.firstName(),
                recordToAdd.lastName(),
                recordToAdd.email());
        customerService.saveCustomer(sanitisedRecord);
        //TODO do I want to return this without the new ID?
        return sanitisedRecord;
    }

    @PutMapping("/customers")
    public CustomerRecord updateCustomer(@RequestBody CustomerRecord theCustomer) {
        customerService.saveCustomer(theCustomer);
        return theCustomer;

    }

    @DeleteMapping("/customers/{customerId}")
    public String deleteCustomer(@PathVariable int customerId) {
        CustomerRecord tempCustomer = customerService.getCustomer(customerId);
        if (tempCustomer == null) {
            throw new CustomerNotFoundException("Customer id not found - " + customerId);
        }
        customerService.deleteCustomer(customerId);
        return "Deleted customer id - " + customerId;
    }

}


















