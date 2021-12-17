package com.stuart.mycrm.service;

import java.util.List;

import com.stuart.mycrm.entity.Customer;
import com.stuart.mycrm.record.CustomerRecord;

public interface CustomerService {

	List<CustomerRecord> getCustomers();

	void saveCustomer(final CustomerRecord customer);

	CustomerRecord getCustomer(final int theId);

	void deleteCustomer(final int theId);

	List<CustomerRecord> searchCustomers(final String searchName);
	
}
