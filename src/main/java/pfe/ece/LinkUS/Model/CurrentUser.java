package pfe.ece.LinkUS.Model;

import org.springframework.security.core.authority.AuthorityUtils;


/**
 * Created by Vignesh on 11/5/2016.
 */
public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private User user;


    public CurrentUser(User user,boolean enabled,
                       boolean accountNonExpired, boolean credentialsNonExpired,
                       boolean accountNonLocked){
        super(user.getEmail(),user.getPasswordHash(),enabled,accountNonExpired,credentialsNonExpired,accountNonLocked,AuthorityUtils.createAuthorityList(user.getRole().toString()));
        this.user = user;
    }

    public CurrentUser(User user){
        super(user.getEmail(),user.getPasswordHash(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getId() {
        return user.getId();
    }

    public Role getRole() {
        return user.getRole();
    }

    @Override
    public String toString() {
        return "CurrentUser{" +
                "user=" + user +
                "} " + super.toString();
    }
}
