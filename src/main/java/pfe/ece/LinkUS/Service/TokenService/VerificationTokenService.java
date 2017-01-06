package pfe.ece.LinkUS.Service.TokenService;


import pfe.ece.LinkUS.Model.VerificationToken;

/**
 * Created by Vignesh on 12/10/2016.
 */
public interface VerificationTokenService {

    void createVerificationToken(String token, String username, String object);

    VerificationToken getVerificationToken(String verificationToken);

    void deleteVerificationToken(String verificationToken);

    boolean existsTokenAssociatedToUsername(String username);

}
