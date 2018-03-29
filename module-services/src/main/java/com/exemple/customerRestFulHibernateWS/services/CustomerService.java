package com.exemple.customerRestFulHibernateWS.services;

import java.util.List;

import com.exemple.customerRestFulHibernateWS.data.model.Customer;
import com.exemple.customerRestFulHibernateWS.data.model.Order;
import com.exemple.customerRestFulHibernateWS.data.model.Product;

public interface CustomerService {
	Customer getCustomerById(long customerId);
    Customer addCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    Customer addOrderToCustomer(Customer customer, Order order);
    void removeCustomer(long customerId);
    Order getCustomerOrder(long customerId, long orderId);
    List<Order> getCustomerOrders(long customerId);
    Product getProduct(long customerId, long orderId, long productId);
    List<Product> getProducts(long customerId, long orderId);
}
