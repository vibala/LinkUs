package pfe.ece.LinkUS.Service.TokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Model.VerificationToken;
import pfe.ece.LinkUS.Repository.TokenMySQLRepo.VerificationTokenRepository;

/**
 * Created by Vignesh on 12/10/2016.
 */
@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public void createVerificationToken(String token, String username) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUsername(username);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public VerificationToken getVerificationToken(String token) {

        VerificationToken verificationToken = null;

        try{
            verificationToken = verificationTokenRepository.findByToken(token);
        }catch(Exception e){
            return null;
        }

        return verificationToken;
    }

    @Override
    public void deleteVerificationToken(String verificationToken) {
        if(verificationToken != null){
            VerificationToken verificationTokenEntity = verificationTokenRepository.findByToken(verificationToken);
            verificationTokenRepository.delete(verificationTokenEntity);
        }
    }

}
