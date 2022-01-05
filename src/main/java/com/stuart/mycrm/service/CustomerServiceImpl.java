package com.stuart.mycrm.service;

import com.stuart.mycrm.dao.CustomerDAO;
import com.stuart.mycrm.entity.Customer;
import com.stuart.mycrm.dto.CustomerDTO;
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
    public List<CustomerDTO> getCustomers() {
        List<Customer> customers = customerDAO.getCustomers();
        if (customers != null) {
            return Converter.makeRecords(customers);
        }
        return null;
    }

    @Override
    @Transactional
    public void saveCustomer(CustomerDTO customer) {
        Customer customerEntity = Converter.makeEntity(customer);

        customerDAO.saveCustomer(customerEntity);
        customer.setId(customerEntity.getId());
    }

    @Override
    @Transactional
    public CustomerDTO getCustomer(final int id) {
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
    public List<CustomerDTO> searchCustomers(final String searchName) {

        return Converter.makeRecords(customerDAO.searchCustomers(searchName));
    }

    private static class Converter {
        public static CustomerDTO makeRecord(Customer customer) {
            return new CustomerDTO(customer.getId(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getEmail());

        }

        public static List<CustomerDTO> makeRecords(List<Customer> customers) {
            List<CustomerDTO> records = new ArrayList<>(customers.size());
            for (Customer customer : customers) {
                records.add(makeRecord(customer));
            }
            return records;
        }

        public static Customer makeEntity(CustomerDTO customer) {
            return new Customer(customer.getId(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getEmail());
        }
    }
}
