package com.stuart.mycrm.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stuart.mycrm.entity.Customer;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public CustomerDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Customer> getCustomers() {
        Session session = sessionFactory.getCurrentSession();
        Query<Customer> query = session.createQuery("from Customer order by lastName asc", Customer.class);
        return query.getResultList();
    }

    @Override
    public void saveCustomer(Customer customer) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(customer);
    }

    @Override
    public Customer getCustomer(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Customer.class, id);
    }

    @Override
    public void deleteCustomer(int id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("delete from Customer where id=:customerId");
        query.setParameter("customerId", id);
        query.executeUpdate();
    }

    @Override
    public List<Customer> searchCustomers(String searchName) {
        Session session = sessionFactory.getCurrentSession();
        Query<Customer> query;
        if (searchNameIsValid(searchName)) {
            query = session.createQuery(
                    "from Customer " +
                            "where lower(firstName) like :searchName " +
                            "or lower(lastName) like :searchName " +
                            "order by lastName",
                    Customer.class);
            query.setParameter("searchName", "%" + searchName.toLowerCase() + "%");

        } else {
            query = getAllCustomersSortedByLastName(session);
        }
        return query.getResultList();

    }

    private Query<Customer> getAllCustomersSortedByLastName(Session session) {
        return session.createQuery("from Customer order by lastName", Customer.class);
    }

    private boolean searchNameIsValid(String theSearchName) {
        return theSearchName != null && !theSearchName.isEmpty() && !theSearchName.isBlank();
    }
}
