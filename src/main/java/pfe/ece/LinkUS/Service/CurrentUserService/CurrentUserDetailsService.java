package pfe.ece.LinkUS.Service.CurrentUserService;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Model.CurrentUser;
import pfe.ece.LinkUS.Model.User;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.UserRepository;
import pfe.ece.LinkUS.Service.UserEntityService.UserService;


/**
 * Created by Vignesh on 11/5/2016.
 */
@Service
public class CurrentUserDetailsService implements UserDetailsService{

    private static final Logger LOGGER = Logger.getLogger(CurrentUserDetailsService.class);
    private final UserService userService;
    private final UserRepository userRepository;


    @Autowired
    public CurrentUserDetailsService(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public CurrentUser loadUserByUsername(String email) throws UsernameNotFoundException {

        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        User user = userService.getUserByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException(String.format("User with email=%s was not found",email)));

        return new CurrentUser(user,user.isEnabled(),accountNonExpired,credentialsNonExpired,accountNonLocked);
    }
}
