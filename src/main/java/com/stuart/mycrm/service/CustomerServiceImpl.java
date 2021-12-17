package com.stuart.mycrm.service;

import java.util.ArrayList;
import java.util.List;

import com.stuart.mycrm.record.CustomerRecord;
import com.stuart.mycrm.utilities.EntityToRecordConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stuart.mycrm.dao.CustomerDAO;
import com.stuart.mycrm.entity.Customer;

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
		return Converter.convertList(customerDAO.getCustomers());
	}

	@Override
	@Transactional
	public void saveCustomer(final CustomerRecord customer) {

		customerDAO.saveCustomer(Converter.convertOne(customer));
	}

	@Override
	@Transactional
	public CustomerRecord getCustomer(final int id) {
		return Converter.convertOne(customerDAO.getCustomer(id));
	}

	@Override
	@Transactional
	public void deleteCustomer(final int id) {
		customerDAO.deleteCustomer(id);
	}

	@Override
	@Transactional
	public List<CustomerRecord> searchCustomers(final String searchName) {

		return Converter.convertList(customerDAO.searchCustomers(searchName));
	}

	private static class Converter {
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
	}
}






