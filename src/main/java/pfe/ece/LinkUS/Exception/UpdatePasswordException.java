package pfe.ece.LinkUS.Exception;

/**
 * Created by Vignesh on 1/4/2017.
 */
public class UpdatePasswordException extends Exception {
    public UpdatePasswordException(String subject,String message){
        super(subject,new Throwable(message));
    }
}
