package pfe.ece.LinkUS.Service.CurrentUserService;


import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Model.CurrentUser;
import pfe.ece.LinkUS.Model.Role;

/**
 * Created by Vignesh on 11/6/2016.
 */
@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    @Override
    public boolean canAccessUser(CurrentUser currentUser, String userid) {
        return currentUser!= null &&
                (currentUser.getRole() == Role.ADMIN || (currentUser.getId().equals(userid)));
    }
}
