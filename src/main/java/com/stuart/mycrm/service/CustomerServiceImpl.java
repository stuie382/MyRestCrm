package com.stuart.mycrm.service;

import com.stuart.mycrm.dao.CustomerDAO;
import com.stuart.mycrm.entity.Customer;
import com.stuart.mycrm.record.CustomerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    /**
     * Autowired customer DAO
     */
    private final CustomerDAO customerDAO;

    @Autowired
    public CustomerServiceImpl(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    @Transactional
    public List<CustomerRecord> getCustomers() {
        List<Customer> customers = customerDAO.getCustomers();
        if (customers != null) {
            return Converter.makeRecords(customers);
        }
        return null;
    }

    @Override
    @Transactional
    public void saveCustomer(final CustomerRecord customer) {

        customerDAO.saveCustomer(Converter.makeEntity(customer));
    }

    @Override
    @Transactional
    public CustomerRecord getCustomer(final int id) {
        Customer customer = customerDAO.getCustomer(id);
        if (customer != null) {
            return Converter.makeRecord(customer);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteCustomer(final int id) {
        customerDAO.deleteCustomer(id);
    }

    @Override
    @Transactional
    public List<CustomerRecord> searchCustomers(final String searchName) {

        return Converter.makeRecords(customerDAO.searchCustomers(searchName));
    }

    private static class Converter {
        public static CustomerRecord makeRecord(Customer customer) {
            return new CustomerRecord(customer.getId(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getEmail());

        }

        public static List<CustomerRecord> makeRecords(List<Customer> customers) {
            List<CustomerRecord> records = new ArrayList<>(customers.size());
            for (Customer customer : customers) {
                records.add(makeRecord(customer));
            }
            return records;
        }

        public static Customer makeEntity(CustomerRecord customer) {
            return new Customer(customer.id(),
                    customer.firstName(),
                    customer.lastName(),
                    customer.email());
        }
    }
}
