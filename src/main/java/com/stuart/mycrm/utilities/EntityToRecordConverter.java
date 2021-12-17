package com.stuart.mycrm.utilities;

import com.stuart.mycrm.entity.Customer;
import com.stuart.mycrm.record.CustomerRecord;

import java.util.ArrayList;
import java.util.List;

public class EntityToRecordConverter {

    private EntityToRecordConverter() {
        // Private to enforce non-instantiation
    }

    public static CustomerRecord convertOne(Customer customer) {
        return new CustomerRecord(customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail());

    }

    public static List<CustomerRecord> convertList(List<Customer> customers) {
        List<CustomerRecord> records = new ArrayList<>(customers.size());
        for (Customer customer : customers) {
            records.add(convertOne(customer));
        }
        return records;
    }

    public static Customer convertOne(CustomerRecord customer) {
        return new Customer(customer.id(),
                customer.firstName(),
                customer.lastName(),
                customer.email());
    }

    public static List<Customer> convertRecordList(List<CustomerRecord> records) {
        List<Customer> customers = new ArrayList<>(records.size());
        for (CustomerRecord theRecord : records) {
            customers.add(convertOne(theRecord));
        }
        return customers;
    }

}
