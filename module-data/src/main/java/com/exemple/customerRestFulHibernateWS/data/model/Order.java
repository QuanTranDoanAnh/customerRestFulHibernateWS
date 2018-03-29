package com.exemple.customerRestFulHibernateWS.data.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * La classe OrderBean est un objet JAVA contenant des methodes get et set et est aussi utilisee pour les commandes retournees par  
 * la classe CustomerServiceImpl.
 * <p/>
 * En ajoutant l'annotation @XmlRootElement, nous offrons la possibilite a JAXB de transformer cet objet en document XML et inversement.
 * <p/>
 * La representation XML pour un OrderBean est la suivante :
 * <Order>
 * <idOrder>223</idOrder>
 * <description>Order 223</description>
 * </Order>
 */

@XmlRootElement(name = "Order")
//@XmlAccessorType(XmlAccessType.FIELD) est obligatoire car le binding par defaut de JAXB est XmlAccessType.PUBLIC_MEMBER. 
//Donc, dans ce cas, JAXB tentera de binder tous les champs publics et les champs et proprietes annotees. Il tenterait donc de binder le champ "products"
//et la methode "getProducts()". En utilisant @XmlAccessorType(XmlAccessType.FIELD), le binding ne se fait qu'une fois.
@XmlAccessorType(XmlAccessType.FIELD) 
@Entity
@Table(name="orders")
public class Order
{
    @Id 
    @Column(name = "idOrder", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idOrder;
    
    private String description;
    

    //Chaque commande doit avoir un client associe donc optional=false
    @ManyToOne(optional=false)
    @JoinColumn(name="customer_id", referencedColumnName="idcustomer", nullable=false)
    private Customer customer;
    
    //On utilise une table nommee "order_detail" pour modeliser l'association entre la table "order" et la table "product".
    //L'annotation @JoinTable est utilisee pour specifier une table de la base de donnees qui associe "order_id" avec "product_id".
    //L'entite qui specifie le @JoinTable est le proprietaire de la relation. Dans notre cas, l'entite Order est le proprietaire de la relation
    //avec l'entite Product.
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name="order_detail", 
                joinColumns=@JoinColumn(name="order_id", referencedColumnName="idOrder"), 
                inverseJoinColumns=@JoinColumn(name="product_id", referencedColumnName="idProduct")
    )
    private List<Product> products;
    
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) 
    {
        this.customer = customer;
    }
    
    public long getIdOrder() 
    {
        return idOrder;
    }
    
    public void setIdOrder(long idOrder) 
    {
        this.idOrder = idOrder;
    }
    
    public String getDescription() 
    {
        return description;
    }
    
    public void setDescription(String d) 
    {
        this.description = d;
    }
    
    @XmlTransient //Permet d'eviter de faire une boucle infinie a la generation du XML
    public List<Product> getProducts() 
    {
        return products;
    }

    public void setProducts(List<Product> products) 
    {
        this.products = products;
    }
    
    @Override
    public String toString() 
    {
      return getClass().getSimpleName() + "[idOrder=" + idOrder + ", desription=" + description + "]";
    }
}
