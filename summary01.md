# Quick notes on how to set up a Java service

## Initialisation du projet
Il existe 2 manière de créer un projet Spring Boot :
- Utiliser le générateur de projet Spring Initializr
- Utiliser le générateur de projet start.spring.io

### Les différents packages
Il faut créer un projet Maven avec les dépendances suivantes :
  - Spring Web
  ```xml
  <dependency>
	 <groupId>org.springframework.boot</groupId>
	 <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  ```
  - Spring Data JPA
    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    ```
  - Spring GraphQL
    ```xml
    <dependency>
        <groupId>com.graphql-java</groupId>
        <artifactId>graphql-spring-boot-starter</artifactId>
        <version>5.0.2</version>
    </dependency>
    <dependency>
	   <groupId>org.springframework.graphql</groupId>
	   <artifactId>spring-graphql-test</artifactId>
	   <scope>test</scope>
    </dependency>
    ```
  - H2 Database
    ```xml
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    ```
  - Lombok
    ```xml
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    ```
    - Spring Web
    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    ```
    - Spring Boot
    ```xml
    <dependency>
	    <groupId>org.springframework.boot</groupId>
	    <artifactId>spring-boot-starter-test</artifactId>
	    <scope>test</scope>
    </dependency>
    <dependency>
	    <groupId>org.springframework</groupId>
	    <artifactId>spring-webflux</artifactId>
	    <scope>test</scope>
    </dependency>
    ```
    - SQL Database
    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jdbc</artifactId>
    </dependency>
    ```
    - JDBC API
    ```xml
    <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.32</version>
    </dependency>
    ```
    
### Structure du projet
Notre projet va être composé de 3 packages :
- `X.entities` : contient les entités
- `X.repository` : contient les interfaces de gestion des données
- `X.service` : contient les services métiers

Puis `ressources` qui contient :
- `application.properties` : contient les propriétés de l'application
- `graphql` : contient les fichiers de configuration de GraphQL
- `static` : contient les fichiers statiques
- `templates` : contient les fichiers de templates

## application.properties
Si on veut utiliser une base de données SQL, il faut ajouter les propriétés suivantes :
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bank-db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=
server.port=8082

spring.jpa.hibernate.ddl-auto=create
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
spring.jpa.show-sql=true

#Thymeleaf cache
spring.thymeleaf.cache=false
```
> **Note** :
> NE PAS OUBLIER DE DEMARER LE SERVEUR MYSQL
> 

Si on veut utiliser une base de données H2, il faut ajouter les propriétés suivantes :
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
server.port=8082

spring.jpa.hibernate.ddl-auto=create
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
spring.jpa.show-sql=true

#Thymeleaf cache
spring.thymeleaf.cache=false
```

## Création des entités


 