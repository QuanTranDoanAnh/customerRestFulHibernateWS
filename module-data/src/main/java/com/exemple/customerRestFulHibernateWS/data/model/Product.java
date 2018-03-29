package com.exemple.customerRestFulHibernateWS.data.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * La classe Product est un objet JAVA contenant des methodes get et set
 * <p/>
 * En ajoutant l'annotation @XmlRootElement, nous offrons la possibilite a JAXB
 * de transformer cet objet en document XML et inversement.
 * <p/>
 * La representation XML d'un Product ressemblera a ceci :
 * 
 * <Product> <idProduct>10010</idProduct> <description>produit 1</description>
 * </Product>
 */

@XmlRootElement(name = "Product")
@XmlType(propOrder = { "idProduct", "description" }) // Fixe l'ordre des champs dans le fichier XML
@Entity
@Table(name = "products")
public class Product {
	@Id
	@Column(name = "idProduct", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idProduct;

	@Column(name = "description", length = 200)
	private String description;

	@ManyToMany(mappedBy = "products", fetch = FetchType.EAGER)
	private List<Order> orderList;

	public long getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(long idProduct) {
		this.idProduct = idProduct;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String d) {
		this.description = d;
	}

	@XmlTransient // Permet d'eviter de faire une boucle infinie a la generation du XML
	public List<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}
}
