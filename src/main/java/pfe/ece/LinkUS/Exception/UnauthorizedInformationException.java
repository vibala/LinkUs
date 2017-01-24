package pfe.ece.LinkUS.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by DamnAug on 16/01/2017.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedInformationException extends RuntimeException {

    public UnauthorizedInformationException() {
        super("Unauthorized exception requested.");
    }
}
