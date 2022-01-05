package com.stuart.mycrm.service;

import java.util.List;

import com.stuart.mycrm.dto.CustomerDTO;

public interface CustomerService {

	List<CustomerDTO> getCustomers();

	void saveCustomer(CustomerDTO customer);

	CustomerDTO getCustomer(final int theId);

	void deleteCustomer(final int theId);

	List<CustomerDTO> searchCustomers(final String searchName);
	
}
