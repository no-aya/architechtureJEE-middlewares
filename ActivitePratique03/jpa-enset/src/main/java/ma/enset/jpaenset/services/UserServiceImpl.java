package ma.enset.jpaenset.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import ma.enset.jpaenset.entities.Role;
import ma.enset.jpaenset.entities.User;
import ma.enset.jpaenset.repositories.RoleRepository;
import ma.enset.jpaenset.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Override
    public User addNewUser(User user) {
        user.setUserId(UUID.randomUUID().toString()); //Génerer un identifiant chaine de caractères
        //TODO:Hashage du mot de passe
        return userRepository.save(user);
    }

    @Override
    public Role addNewRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public User findUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public Role findRoleByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    public void addRoleToUser(String userName, String roleName) {
        User foundUser = findUserByUserName(userName);
        Role foundRole = findRoleByRoleName(roleName);
        if (foundUser.getRoles() != null) {
            //Pour garder la cohérence entre les deux tables
            foundUser.getRoles().add(foundRole);
            foundRole.getUsers().add(foundUser);
        }
        //userRepository.save(foundUser);
    }

    @Override
    public User authenticate(String userName, String password) {
        User user = userRepository.findByUserName(userName);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        throw new RuntimeException("User not found or wrong password!");
    }
}
