package com.exemple.customerRestFulHibernateWS.data;

import com.exemple.customerRestFulHibernateWS.data.model.Customer;
import com.exemple.customerRestFulHibernateWS.data.model.Order;
import com.exemple.customerRestFulHibernateWS.data.model.Product;

public interface CustomerDao {
	Customer findCustomer(final long customerId);
    Customer saveExistingCustomer(final Customer customer);
    Customer saveCustomer(final Customer customer);
    void deleteCustomer(final long customerId);
    Order findOrder(final long orderId);
    Product findProduct(final long productId);
}
