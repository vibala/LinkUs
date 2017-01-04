package pfe.ece.LinkUS.Service.CurrentUserService;


import pfe.ece.LinkUS.Model.CurrentUser;

/**
 * Created by Vignesh on 11/6/2016.
 */
public interface CurrentUserService {
    boolean canAccessUser(CurrentUser currentUser, String userid);
}
