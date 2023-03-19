# Use case JPA, Hibernate Spring Data, Many To Many | Users-Roles

Dans cette partie, nous allons voir comment utiliser JPA, Hibernate et Spring Data pour implémenter un système de gestion des utilisateurs et des rôles.
Puis nous allons merger vers MySQL et voir comment utiliser Spring Data JPA.

## 1. Préparation du projet
Dans IntelliJ, créer un nouveau projet Spring Boot avec les dépendances suivantes:
- Spring Web
- Spring Data JPA
- H2 Database
- Lombok

## 2. Création des entités
Dans le package `com.enset.entities`, créer les entités suivantes:
- `Role.java`
- `User.java`

## 3. Création des repositories
Dans le package `com.enset.repositories`, créer les repositories suivants:
- `RoleRepository.java`
- `UserRepository.java`

## 4. Création des services
Dans le package `com.enset.services`, créer les services suivants:
- `UserService.java`

## 5. Création des controllers
Dans le package `com.enset.controllers`, créer les controllers suivants:
- `UserController.java`

## 6. Création des données de test
Dans le package `com.enset.jpaenset`, accéder à la classe `JpaEnsetApplication` et ajouter le code suivant:
```java
@Bean
    CommandLineRunner start(UserService userService){
        return args -> {
            User user=new User();
            user.setUserName("aya_ait");
            user.setPassword("azertyu123");
            userService.addNewUser(user);


            User user2=new User();
            user2.setUserName("admin");
            user2.setPassword("azertyu123");
            userService.addNewUser(user2);

            Stream.of("ADMIN","STUDENT","USER").forEach(roleName->{
                Role role=new Role();
                role.setRoleName(roleName);
                role.setDescription("Description de "+roleName);
                userService.addNewRole(role);
            });

            userService.addRoleToUser("admin","ADMIN");
            userService.addRoleToUser("admin","USER");

            userService.addRoleToUser("aya_ait","STUDENT");
            userService.addRoleToUser("aya_ait","USER");


            try{
                User connectedUser = userService.authenticate("admin", "azertyu123");
                System.out.println("Bienvenue "+connectedUser.getUserName());
                System.out.println("Vous avez les roles suivants:");
                connectedUser.getRoles().forEach(r->{
                            System.out.println(r.getRoleName()+" : "+r.getDescription());
                        });
            }catch (Exception e){
                System.out.println("Erreur d'authentification");
            }
        };
    }
```

## 7. Tester le projet
Lancer le projet et vérifier que les données sont bien insérées dans la base de données H2.

## 8. Migration vers MySQL
Dans le fichier `application.properties`, ajouter les propriétés suivantes:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/users_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=
server.port=8083
spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
```
Ajouter la dépendance suivante:
- MySQL Driver
dans le fichier `pom.xml`:
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.32</version>
</dependency>
```

## 9. Tester le projet
Lancer le projet et vérifier que les données sont bien insérées dans la base de données MySQL.

Pour accéder au donner depuis un lien URL, ajouter le code suivant dans la classe `UserController`:
```java
@Autowired
private UserService userService;

@GetMapping("/users/{username}")
public User user(@PathVariable String username){
   User user=userService.findUserByUserName(username);
   return user;
}
```

Comme ça on peut accéder aux données de l'utilisateur `admin` depuis l'URL suivante:
http://localhost:8083/users/admin


