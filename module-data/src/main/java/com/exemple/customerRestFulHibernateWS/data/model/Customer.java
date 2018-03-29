package com.exemple.customerRestFulHibernateWS.data.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * La classe Customer est un objet JAVA contenant des methodes get et set
 * <p/>
 * En ajoutant l'annotation @XmlRootElement, nous offrons la possibilite a JAXB de transformer cet objet en document XML et inversement.
 * <p/>
 * La representation XML d'un customer ressemblera a ceci :
 * <Customer>
 * <idCustomer>123</idCustomer>
 * <prenom>Olivier</prenom>
 * <nom>Rozier</nom>
 * </Customer> 
 */
@XmlRootElement(name = "Customer")
@XmlType(propOrder={"idCustomer", "prenom", "nom"}) //Fixe l'ordre des champs dans le fichier XML
@Entity
@Table(name="customers")
public class Customer 
{
     @Id //specifie la cle primaire
     @Column(name = "idCustomer", nullable = false)
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private long idCustomer;
    
     @Column(name = "prenom", length = 50)
     private String prenom;
     
     @Column(name = "nom", length = 50)
     private String nom;
    
     //FetchType.LAZY indique a la JPA runtime qu'on souhaite reporter le chargement des champs au moment de l'acces
     //Ceci est completement transparent : les donnees sont chargees depuis la base de donnees dans les objets discretement lorsqu'on tente de lire
     //les champs pour la premiere fois.
     //FetchType.EAGER indique que quel que soit le moment ou on recupere une entite depuis une requete ou depuis l'entity manager, on a la garantie
     //que tous les champs sont renseignes.
    
    @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="customer")
    private List<Order> orders;
    
    public long getIdCustomer() 
    {
         return idCustomer;
    }

    public void setIdCustomer(long idCustomer) 
    {
        this.idCustomer = idCustomer;
    }

    public String getNom() 
    {
        return nom;
    }

    public void setNom(String nom) 
    {
        this.nom = nom;
    }
    
    public String getPrenom() 
    {
        return prenom;
    }

    public void setPrenom(String prenom) 
    {
        this.prenom = prenom;
    }
    
    @XmlTransient //Permet d'eviter de faire une boucle infinie a la generation du XML
    public List<Order> getOrders() 
    {
        return orders;
    }

    public void setOrders(List<Order> orders) 
    {
        this.orders = orders;
    }
    
    public void addOrder(Order order) 
    {
        this.orders.add(order);
    }
    
    @Override
    public String toString() 
    {
      return getClass().getSimpleName() + "[idCustomer=" + idCustomer + ", nom=" + nom + ", prenom=" + prenom + "]";
    }
}
