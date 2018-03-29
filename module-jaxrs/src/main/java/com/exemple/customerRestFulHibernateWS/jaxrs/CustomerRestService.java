package com.exemple.customerRestFulHibernateWS.jaxrs;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exemple.customerRestFulHibernateWS.data.model.Customer;
import com.exemple.customerRestFulHibernateWS.data.model.Order;
import com.exemple.customerRestFulHibernateWS.data.model.Product;
import com.exemple.customerRestFulHibernateWS.services.CustomerService;

/*La classe CustomerRestService est l'implementation d'un service. Elle permet de faire l'interfacage entre le client et la couche metier. 
 *La couche metier sera accessible via l'interface CustomerService du package com.exemple.karaf.services. Pour cette raison, il est necessaire de declarer 
 *une instance de cette interface en tant que membre de la classe CustomerRestService*/
public class CustomerRestService 
{
    private static final Logger LOG = LoggerFactory.getLogger(CustomerService.class);
    
    private CustomerService customerService;
    
    public CustomerService getCustomerService() 
    {
         return customerService;
    }

    public void setCustomerService(CustomerService customerService) 
    {
        this.customerService = customerService;
    }
    
    
    /**
     * Cette methode recupere un client a partir de son idCustomer. 
     * Elle est mappee a la requete HTTP GET : "http://localhost:8181/cxf/olivier/customer/{customerId}". La valeur
     * de {customerId} sera passee en parametre a la methode par utilisation de l'annotation @PathParam.
     * <p/>
     * La methode retournera un objet de la classe Customer par creation d'une reponse HTTP. Cet objet sera transforme en XML par JAXB.
     * <p/>
     * Exemple d'appel : http://localhost:8181/cxf/olivier/customer/2
     */
    @GET
    @Path("/customer/{id}/")
    @Produces("application/xml")
    public Customer getCustomer(@PathParam("id") long customerId)
    {
        LOG.info(getClass().getSimpleName() + "[getCustomer({})]", customerId);
        
        return customerService.getCustomerById(customerId);
    }
    
    
    /**
     * Cette methode met a jour un client a partir d'informations transmises en parametre dans un objet JSON. 
     * Elle est mappee a la requete HTTP PUT http://localhost:8181/cxf/olivier/customer/update. 
     * On peut ainsi envoyer la representation XML d'un objet Customer.
     * La representation XML sera obtenue par transformation d'un Customer par JAXB.
     * <p/>
     * Cette methode met a jour un objet Customer dans notre map locale puis utilise la classe Response pour construire 
     * une reponse HP appropriee : soit OK si la mise a jour a ete effectuee avec succes (Traduction du statut HTTP 200/OK) 
     * ou NON MODIFIE si la mise a jour de l'objet Customer a echoue (Traduction du statut HTTP 304/Not Modified).  
     * <p/>
     * Exemple d'appel : http://localhost:8181/cxf/olivier/customer/update
     * avec pour parametre JSON : {"Customer":{"customerId":2, "nom":"Croche","prenom": "Sarah"}}
     */
    @PUT
    @Path("/customer/update")
    @Consumes({"application/xml", "application/json" })
    public Response updateCustomer(Customer customer)
    {
        LOG.info(getClass().getSimpleName() + "[updateCustomer({})]", customer.getIdCustomer());
        
        final Customer c = customerService.getCustomerById(customer.getIdCustomer());
        Response r;
        if(c != null)
        {
            c.setNom(customer.getNom());
            customerService.updateCustomer(c);
            r = Response.ok().build();
         } 
         else 
         {
             r = Response.notModified().build();
         }

         return r;
    }
    
    
    
    /**
     * Cette methode ajoute une commande a un client dont l'identifiant est fourni dans l'URL. La commande est fournie en parametre dans un objet JSON.
     * Cette methode est mappee a une requete HTTP PUT de la forme http://localhost:8181/cxf/olivier/customer/{customerId}/order. 
     * On peut ainsi envoyer la representation XML d'un objet Customer.
     * La representation XML sera obtenue par transformation d'un Customer par JAXB.
     * <p/>
     * Cette methode ajoute une commande à un Customer dans notre map locale puis utilise la classe Response pour construire 
     * une reponse HP appropriee : soit OK si la mise a jour a ete effectuee avec succes (Traduction du statut HTTP 200/OK) 
     * ou NON MODIFIE si la mise a jour de l'objet Customer a echoue (Traduction du statut HTTP 304/Not Modified).  
     * <p/>
     * Exemple d'appel : http://localhost:8181/cxf/olivier/customer/1/order
     * avec pour parametre JSON : {"Order":{"description":Nouvelle commande, "products": [{"idProduct":1,"description":"Clavier"},{"idProduct":2,"description":"Souris"}]}}
     */
    @PUT
    @Path("/customer/{id}/order")
    @Consumes({"application/xml", "application/json" })
    public Response addOrderToCustomer(@PathParam("id") long customerId, Order order)
    {
        LOG.info(getClass().getSimpleName() + "[addOrderToCustomer({}, {})]", customerId, order.toString());
        
        final Customer c = customerService.getCustomerById(customerId);
        Response r;
        if(c != null)
        {
            if(customerService.addOrderToCustomer(c, order) != null)
            {
                r = Response.ok().build();
            }
            else
            {
                r = Response.notModified().build();
            }
        } 
        else 
        {
            r = Response.notModified().build();
        }
        return r;
    }
    
    
    
    /**
     * Cette methode insere un client fourni en parametre dans un objet JSON.
     * Elle est mappe a la requete HTTP POST http://localhost:8181/cxf/olivier/customer/add
     * <p/>
     * Apres que cette methode a ajoute le client dans la map local, elle utilisera la classe Response pour construire la reponse HTTP 
     * en retournant a la fois l'objet Customer insere et le statut HTTP 200/OK. Ceci permet de recuperer l'ID du nouvel object Customer. 
     *<p/>
     * Exemple d'appel : http://localhost:8181/cxf/olivier/customer/add
     * avec pour parametre JSON : {"Customer":{"nom":"Niplancheharepasser","prenom": "Jennifer"}}
     */
    @POST
    @Path("/customer/add")
    @Consumes({"application/xml", "application/json" })
    public Response addCustomer(Customer customer)
    {
        LOG.info(getClass().getSimpleName() + "[addCustomer({})]", customer.toString());
        
        customerService.addCustomer(customer);
        
        return Response.ok().type("application/xml").entity(customer).build();
    }
    
    
    
    /**
     * Cette methode permet de supprimer un client dont le nom est fourni en parametre.
     * Elle est mappee a une requete HTTP DELETE du type : "http://localhost:8181/cxf/olivier/customer/{customerId}".
     * La valeur pour {id} sera passee en tant que parametre en utilisant l'annotation @PathParam.
     * <p/>
     * Cette methode utilise la classe Response pour creer une reponse HTTP : soit le statut HTTP 200/OK si l'objet Customer 
     * a ete correctement supprime de la map local, soit le statut HTTP 304/Not Modified si la suppression a echoue.
     * <p/>
     * Exemple d'appel : http://localhost:8181/cxf/olivier/customer/5
     */
    @DELETE
    @Path("/customer/{id}/")
    public Response deleteCustomer(@PathParam("id") long customerId)
    {   
        LOG.info(getClass().getSimpleName() + "[deleteCustomer({})]", customerId);
        
        final Customer customer = customerService.getCustomerById(customerId);
        
        Response r = null;
        if (customer != null) 
        {
            customerService.removeCustomer(customerId);
            r = Response.ok().build();
        } 
        else 
        {
            r = Response.notModified().build();
        }

        return r;
    }
    
    
    /**
     * Cette methode permet de recuperer les commandes d'un client dont l'identifiant est fourni dans la requete.
     * Elle est mappee a une requete HTTP GET du type : "http://localhost:8181/cxf/olivier/customer/{customerId}/orders".
     * La valeur pour {id} sera passee en tant que parametre en utilisant l'annotation @PathParam.
     * <p/>
     * La methode retournera une liste d'objet de la classe Order par creation d'une reponse HTTP. Cet objet sera transforme en XML par JAXB.
     * <p/>
     * Exemple d'appel : http://localhost:8181/cxf/olivier/customer/1/orders
     */
    @GET
    @Path("/customer/{customerId}/orders/")
    @Produces("application/json")
    public List<Order> getOrder(@PathParam("customerId") long customerId)
    {
        LOG.info(getClass().getSimpleName() + "[getOrder({})]", customerId);
        
        return customerService.getCustomerOrders(customerId);
    }
    
    
    
    /**
     * Cette methode permet de recuperer une commande dont l'identifiant est fourni en parametre de meme que l'identifiant du client.
     * Elle est mappee a une requete HTTP GET du type : "http://localhost:8181/cxf/olivier/customers/{customerId}/order/{orderId}".
     * <p/>
     * La methode retournera un objet de la classe Order par creation d'une reponse HTTP. Cet objet sera transforme en XML par JAXB.
     * <p/>
     * Exemple d'appel : http://localhost:8181/cxf/olivier/customers/1/order/1
     */
    @GET
    @Path("/customer/{customerId}/order/{orderId}")
    @Produces("application/xml")
    public Order getOrder(@PathParam("customerId") long customerId, @PathParam("orderId") long orderId)
    {
        LOG.info(getClass().getSimpleName() + "[getOrder({}, {})]", customerId, orderId);
        
        return customerService.getCustomerOrder(customerId, orderId);
    }
    
    
    /**
     * Cette methode permet de recuperer un produit donne dans une commande donnee d'un client donne.
     * Les identifiants du produit, de la commande et du client sont fournis en parametres.
     * Cette methode est mappee a une requete HTTP GET du type : "http://localhost:8181/cxf/olivier/customer/{customerId}/order/{orderId}/product/{productId}".
     * <p/>
     * La methode retournera un objet de la classe Product par creation d'une reponse HTTP. Cet objet sera transforme en XML par JAXB.
     * <p/>
     * Exemple d'appel : http://localhost:8181/cxf/olivier/customer/1/order/1/product/3
     */
    @GET
    @Path("/customer/{customerId}/order/{orderId}/product/{productId}")
    @Produces("application/xml")
    public Product getProduct(@PathParam("customerId") long customerId, @PathParam("orderId") long orderId, @PathParam("productId") long productId)
    {
        LOG.info(getClass().getSimpleName() + "[getProduct({}, {}, {})]", customerId, orderId, productId);
        
        return customerService.getProduct(customerId, orderId, productId);
    }
    
    
    /**
     * Cette methode permet de recuperer tous les produits d'une commande donnee d'un client donne.
     * Les identifiants du client et de la commande seront fournis dans l'URL.
     * Cette methode est mappee a une requete HTTP GET du type : "http://localhost:8181/cxf/olivier/customer/{customerId}/order/{orderId}/products".
     * <p/>
     * La methode retournera un objet de la classe Product par creation d'une reponse HTTP. Cet objet sera transforme en XML par JAXB.
     * <p/>
     * Exemple d'appel : http://localhost:8181/cxf/olivier/customer/1/order/1/products
     */
    @GET
    @Path("/customer/{customerId}/order/{orderId}/products")
    @Produces("application/xml")
    public List<Product> getProducts(@PathParam("customerId") long customerId, @PathParam("orderId") long orderId)
    {
        LOG.info(getClass().getSimpleName() + "[getProducts({}, {})]", customerId, orderId);
        
        return customerService.getProducts(customerId, orderId);
    }
}
