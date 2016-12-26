package pfe.ece.LinkUS.Model.Validator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pfe.ece.LinkUS.Model.UserCreateForm;
import pfe.ece.LinkUS.Service.UserEntityService.UserService;


/**
 * Created by Vignesh on 11/6/2016.
 */
@Component
public class UserCreateFormValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserCreateFormValidator(UserService userService) {
        this.userService = userService;
    }
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UserCreateForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserCreateForm form = (UserCreateForm) target;
        validateEmail(errors,form);
        validatePassword(errors,form);
    }

    public void validatePassword(Errors errors, UserCreateForm form){
        if (!form.getPassword().equals(form.getPasswordRepeated())) {
            errors.reject("password.no_match", "Passwords do not match");
        }
    }

    public void validateEmail(Errors errors, UserCreateForm form){
        if (userService.getUserByEmail(form.getEmail()).isPresent()) {
            errors.reject("email.exists", "User with this email already exists");
        }
    }
}
