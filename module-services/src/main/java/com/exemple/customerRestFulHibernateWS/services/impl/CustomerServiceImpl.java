package com.exemple.customerRestFulHibernateWS.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exemple.customerRestFulHibernateWS.data.CustomerDao;
import com.exemple.customerRestFulHibernateWS.data.model.Customer;
import com.exemple.customerRestFulHibernateWS.data.model.Order;
import com.exemple.customerRestFulHibernateWS.data.model.Product;
import com.exemple.customerRestFulHibernateWS.services.CustomerService;

public class CustomerServiceImpl implements CustomerService {

	private CustomerDao customerDao;
    private static final Logger LOG = LoggerFactory.getLogger(CustomerDao.class);
    
    public Customer getCustomerById(long customerId)
    {
        LOG.info(getClass().getSimpleName() + "[getCustomerById({})]", customerId);
        return customerDao.findCustomer(customerId);
    }
    
    
    public Customer addCustomer(Customer customer)
    {
        LOG.info(getClass().getSimpleName() + "[addCustomer({})]", customer.toString());
        return customerDao.saveCustomer(customer);
    }

    
    public Customer updateCustomer(Customer customer)
    {
        LOG.info(getClass().getSimpleName() + "[updateCustomer({})]", customer.toString());
        return customerDao.saveExistingCustomer(customer);
    }
    
    
    public Customer addOrderToCustomer(Customer customer, Order order)
    {
        LOG.info(getClass().getSimpleName() + "[addOrderToCustomer({}, {})]", customer.toString(), order.toString());
        
        /*Si on souhaite inserer un nouveau produit dans la table product quand on insere une commande (c'est un exemple!!!)
         * on utilise les trois lignes ci-dessous et on modifie la relation 
         * @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
         * en
         * @ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER) dans la classe Order.
         * De cette maniere, grace à CascadeType.MERGE qui est inclus dans CascadeType.ALL, on inserera dans la table "order", 
         * dans la table "order_detail" et dans la table "product"
        order.setCustomer(customer);
        customer.addOrder(order);
        return customerDao.saveExistingCustomer(customer);
        */
        
        /*
         * Comme on ne souhaite pas inserer de nouveaux produits dans la table "product" quand on insere une commande,
         * il nous faut recuperer les entite "Product" correspondant aux donnees fournies en parametres.
         * Afin de ne pas inserer de nouveau produit dans la table product, on recupere les entites de product existantes  
         */
        List<Product> listProducts = new ArrayList<Product>();
        
        for(Product product: order.getProducts())
        {
            Product p = customerDao.findProduct(product.getIdProduct());
            if(p != null)
            {
                listProducts.add(p);
            } 
            else 
            {
                //Le produit n'existe pas dans la base de donnees
                return null;
            }
        }
        order.setProducts(listProducts);
        
        order.setCustomer(customer);
        customer.addOrder(order);
        return customerDao.saveExistingCustomer(customer);
    }
    
    public void removeCustomer(long customerId)
    {
        LOG.info(getClass().getSimpleName() + "[removeCustomer({})]", customerId);

        customerDao.deleteCustomer(customerId);
    }
    

    public Order getCustomerOrder(long customerId, long orderId)
    {
        LOG.info(getClass().getSimpleName() + "[getCustomerOrder({}, {})]", customerId, orderId);
        
        Customer customer = customerDao.findCustomer(customerId);

        for(Order order: customer.getOrders())
        {
            if(order.getIdOrder() == orderId)
            {
                return order;
            }
        }
        return null;
    }
    
    
    public List<Order> getCustomerOrders(long customerId)
    {
        LOG.info(getClass().getSimpleName() + "[getCustomerOrders({})]", customerId);
        
        List<Order> commandes = new ArrayList<Order>();
        
        Customer customer = customerDao.findCustomer(customerId);
        /*JPA retourne l'ensemble des enregistrements qui sont lies entre eux dans les differentes tables. On a quelque chose de ce genre :
         *     idCustomer, nom, prenom, customer_id, idOrder, idOrder, customer_id, description, order_id, product_id, product_id, description
            1;"Rozier";"Olivier";1;1;1;1;"Cmd 1";1;3;3;"Ecran"
            1;"Rozier";"Olivier";1;5;5;1;"Cmd 5";5;2;2;"Souris"
            1;"Rozier";"Olivier";1;6;6;1;"Cmd 6";6;1;1;"Clavier"
            1;"Rozier";"Olivier";1;1;1;1;"Cmd 1";1;1;1;"Clavier"
            1;"Rozier";"Olivier";1;1;1;1;"Cmd 1";1;2;2;"Souris"
            Quand on demande a JPA les commandes liees au client, il voit 5 commandes mais il s'agit en fait de cinq lignes de commandes.
            Il est donc necessaire de recuperer une seule fois chaque commande afin que JAXB puisse creer un fichier XML sans noeud duplique.
        */
        for(Order order: customer.getOrders())
        {
            //Recuperation des orders differents
            if(!commandes.contains(order))
            {
                commandes.add(order);
            }
        }
        
        return commandes;
    }
    

    public Product getProduct(long customerId, long orderId, long productId)
    {
        LOG.info(getClass().getSimpleName() + "[getProduct({}, {}, {})]", customerId, orderId, productId);
        
        Order order = this.getCustomerOrder(customerId, orderId);
        
        for(Product product: order.getProducts())
        {
            if(product.getIdProduct() == productId)
            {
                return product;
            }
        }
        
        return null;
    }
    
    
    public List<Product> getProducts(long customerId, long orderId)
    {
        Order order = this.getCustomerOrder(customerId, orderId);
        return order.getProducts();
    }
    
    
    public void setCustomerDao( final CustomerDao customerDao) 
    {
        this.customerDao = customerDao;
    }    

}
