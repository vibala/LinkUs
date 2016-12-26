package pfe.ece.LinkUS.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by DamnAug on 22/11/2016.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class SubscriptionTypeNotFoundException extends RuntimeException {

    public SubscriptionTypeNotFoundException(String id) {
        super("Could not find any subscription type for id: '" + id + "'.");
    }
}
