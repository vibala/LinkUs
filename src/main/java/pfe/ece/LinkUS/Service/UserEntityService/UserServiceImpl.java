package pfe.ece.LinkUS.Service.UserEntityService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Exception.EmailExistsException;
import pfe.ece.LinkUS.Model.User;
import pfe.ece.LinkUS.Model.UserCreateForm;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.UserRepository;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by Vignesh on 10/25/2016.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /*public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }*/

    @Override
    public Optional<User> getUserById(String id) {
        return Optional.ofNullable(userRepository.findOne(id));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll(new Sort("email"));
    }

    private boolean emailExist(String email){
        Optional<User> user = userRepository.findOneByEmail(email);

        if(!user.isPresent()){
            return false;
        }

        return true;
    }


    @Override
    public User registerNewUserAccount(UserCreateForm form) throws EmailExistsException {

        if(emailExist(form.getEmail())){
            throw new EmailExistsException("There is an account with that email address");
        }

        User user = new User();
        user.setEmail(form.getEmail());
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setPasswordHash(new BCryptPasswordEncoder().encode(form.getPassword()));
        user.setSexe(form.getSexe());
        user.setDateofBirth(form.getDateofBirth());
        user.setRole(form.getRole());
        return userRepository.save(user);
    }

    @Override
    public void saveRegisteredUser(User user) {
        Optional<User> optionalRefreshUser = userRepository.findOneByEmail(user.getEmail());
        User refreshedUser = optionalRefreshUser.get();
        if(optionalRefreshUser.isPresent() && user != null){
            refreshedUser.setEnabled(true);
            userRepository.save(refreshedUser);
        }
    }



}
