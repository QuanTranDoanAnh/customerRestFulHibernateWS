package com.exemple.customerRestFulHibernateWS.data.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.osgi.service.log.LogService;

import com.exemple.customerRestFulHibernateWS.data.CustomerDao;
import com.exemple.customerRestFulHibernateWS.data.model.Customer;
import com.exemple.customerRestFulHibernateWS.data.model.Order;
import com.exemple.customerRestFulHibernateWS.data.model.Product;

public class CustomerDaoImpl implements CustomerDao
{
    //@PersistenceContext permet de recuperer l'entityManager. Le unitName est celui fourni dans le fichier persistence.xml (dans la balise <persistence-unit name="customerDb" transaction-type="JTA">)
    @PersistenceContext(unitName = "customerDb")
    private EntityManager entityManager;
    private LogService logService;

    
    public void setEntityManager( final EntityManager entityManager ) 
    {
        this.entityManager = entityManager;
    }
    
    @Override
    public Customer findCustomer(final long customerId)
    {
        logService.log(LogService.LOG_INFO, getClass().getSimpleName() + "[findCustomer(" + customerId + ")]");
        
        return entityManager.find(Customer.class, customerId);
    }
    
    
    @Override
    public Customer saveCustomer(final Customer customer)
    {
        //On rend l'entite persistante. C'est-a-dire qu'on l'ecrit en base sur le prochain commit de la transaction dans laquelle on se trouve
        logService.log(LogService.LOG_INFO, getClass().getSimpleName() + "[saveCustomer(" + customer.toString() + ")]");
        
        entityManager.persist(customer);
        return customer;
    }
    
    @Override
    public Customer saveExistingCustomer(final Customer customer)
    {
        //On attache l'entite a l'Entity Manager courant.
        logService.log(LogService.LOG_INFO, getClass().getSimpleName() + "[saveExistingCustomer(" + customer.toString() + ")]");
        
        entityManager.merge(customer);
        return customer;
    }
    
    @Override
    public void deleteCustomer(final long customerId)
    {
        //On rend l'entite non persistante. Une entite rendue non persistante sera effacee de la base sur le prochain commit de la transaction dans laquelle on se trouve
        logService.log(LogService.LOG_INFO, getClass().getSimpleName() + "[deleteCustomer(" + customerId + ")]");
        
        entityManager.remove(findCustomer(customerId));
    }
    
    @Override
    public Order findOrder(final long orderId)
    {
        logService.log(LogService.LOG_INFO, getClass().getSimpleName() + "[findOrder(" + orderId + ")]");
        
        return entityManager.find(Order.class, orderId);
    }
    
    @Override
    public Product findProduct(final long productId)
    {
        logService.log(LogService.LOG_INFO, getClass().getSimpleName() + "[findProduct(" + productId + ")]");
        
        return entityManager.find(Product.class, productId);
    }
    
    public void setLogService( final LogService logService ) 
    {
        this.logService = logService;
    }
    
}