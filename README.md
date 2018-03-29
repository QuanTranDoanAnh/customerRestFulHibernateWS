# customerRestFulHibernateWS
customerRestFulHibernateWS in Karaf 4.1.x with H2 db
Most recent update of how to create Restful web services with Karaf 4.1.x, JPA2 and H2.
Since Karaf 4.1.x & Pax JDBC 1.1.0 there are many changes in APIs, comparing to Liquidbase Tutorial of Karaf.

+ Prerequisite:
- Karaf 4.1.5
- Maven
- Eclipse

+ How to play with it
1. Start Karaf
2. Access to the project folder, run `mvn clean install`
3. Copy the file `org.ops4j.datasource-customerDb.cfg` in the `module-data` module to the `etc` folder of Karaf 4.1.5 installation
4. In the Karaf console, run `feature:repo-add mvn:com.exemple.customerRestFulHibernateWS/module-kar/0.0.1-SNAPSHOT/xml/features` and then run `feature:install module-kar`.
5. Try to create a customer record in the db by typing `jdbc:execute customerDb "insert into customers(prenom, nom) values ('Quan', 'Tran')"`
6. Use browser to access `http://localhost:8181/cxf/olivier/customer/1` or `http://localhost:8181/cxf/olivier/customer/1/orders`

+ Attention
- Be aware of table name which duplicate "identifiers" like 'User', 'Order' etc.
