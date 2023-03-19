package ma.enset.jpaenset.repositories;

import ma.enset.jpaenset.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //Componnent de la couche DAO
public interface UserRepository extends JpaRepository<User, String>{
    User findByUserName(String userName);

}
