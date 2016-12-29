package pfe.ece.LinkUS.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pfe.ece.LinkUS.Model.User;
import pfe.ece.LinkUS.Service.CurrentUserService.CurrentUserDetailsService;
import pfe.ece.LinkUS.Service.UserEntityService.UserService;

/**
 * Created by Vignesh on 12/29/2016.
 */
public class ProperPasswordEncoder implements PasswordEncoder {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserService userService;
    private CurrentUserDetailsService userDetailsService;


    public ProperPasswordEncoder(CurrentUserDetailsService userDetailsService) {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.userDetailsService = userDetailsService;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return bCryptPasswordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        User userInfo = userDetailsService.getUserInfo();
        //System.out.println("Userinfo " + userInfo.getEmail());
        String dateofRegistration = userInfo.getDateofRegistration().toString();
        rawPassword = rawPassword + "" + dateofRegistration;
        //System.out.println("zzzzzzzzzzzzzzz ! " + rawPassword + "rsdfsdfsdf" + encodedPassword);
        return bCryptPasswordEncoder.matches(rawPassword,encodedPassword);
    }
}
