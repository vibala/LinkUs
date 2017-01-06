package pfe.ece.LinkUS.Exception;

import org.springframework.security.authentication.BadCredentialsException;

/**
 * Created by Vignesh on 1/2/2017.
 */
public class FirebaseTokenInvalidException extends BadCredentialsException {

        public FirebaseTokenInvalidException(String msg) {
            super(msg);
        }

}
