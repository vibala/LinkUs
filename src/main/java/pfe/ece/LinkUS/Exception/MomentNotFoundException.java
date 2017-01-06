package pfe.ece.LinkUS.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by DamnAug on 05/01/2017.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class MomentNotFoundException extends RuntimeException{

    public MomentNotFoundException(String id) {
        super("Couldn't fidn matching moment with id " + id);
    }
}
