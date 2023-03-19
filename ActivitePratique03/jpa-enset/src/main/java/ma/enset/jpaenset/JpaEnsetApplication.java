package ma.enset.jpaenset;

import ma.enset.jpaenset.entities.Role;
import ma.enset.jpaenset.entities.User;
import ma.enset.jpaenset.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.Stream;

@SpringBootApplication
public class JpaEnsetApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaEnsetApplication.class, args);
    }

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

            /*Role role1=new Role();
            role1.setRoleName("ADMIN");
            role1.setDescription("Administrateur");
            userService.addNewRole(role1);

            Role role2=new Role();
            role2.setRoleName("STUDENT");
            role2.setDescription("Etudiant");
            userService.addNewRole(role2);*/

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
}
