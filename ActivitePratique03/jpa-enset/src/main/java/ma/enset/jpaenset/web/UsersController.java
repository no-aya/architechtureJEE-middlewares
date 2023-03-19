package ma.enset.jpaenset.web;

import lombok.AllArgsConstructor;
import ma.enset.jpaenset.entities.User;
import ma.enset.jpaenset.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {
    @Autowired
    private UserService userService;

    @GetMapping("/users/{username}")
    public User user(@PathVariable String username){
        User user=userService.findUserByUserName(username);
        return user;
    }
}
