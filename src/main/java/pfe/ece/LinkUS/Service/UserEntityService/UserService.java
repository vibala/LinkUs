package pfe.ece.LinkUS.Service.UserEntityService;


import pfe.ece.LinkUS.Exception.EmailExistsException;
import pfe.ece.LinkUS.Model.User;
import pfe.ece.LinkUS.Model.UserCreateForm;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by Vignesh on 10/25/2016.
 */
// In service layer, where the business logic should be implemented, we'd need something to retrieve the User by id, email,
// list all the users and create a new one
public interface UserService {

    Optional<User> getUserById(String id);
    Optional<User> getUserByEmail(String email);
    Collection<User> getAllUsers();
    User registerNewUserAccount(UserCreateForm form) throws EmailExistsException;
    void saveRegisteredUser(User user);
    void saveNewPassword(User user,String new_password);
}
