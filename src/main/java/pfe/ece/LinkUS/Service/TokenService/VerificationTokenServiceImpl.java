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
    public void createVerificationToken(String token, String username,String object) {
        VerificationToken verificationToken = new VerificationToken(object);
        verificationToken.setToken(token);
        verificationToken.setUsername(username);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public VerificationToken getVerificationToken(String token) {

        VerificationToken verificationToken = null;
        System.out.println("toto");
        try{
            System.out.println("titi");
            verificationToken = verificationTokenRepository.findByToken(token);
        }catch(Exception e){
            System.out.print(e.getMessage());
            return null;
        }

        System.out.println("caca");
        return verificationToken;
    }

    @Override
    public void deleteVerificationToken(String verificationToken) {
        if(verificationToken != null){
            VerificationToken verificationTokenEntity = verificationTokenRepository.findByToken(verificationToken);
            verificationTokenRepository.delete(verificationTokenEntity);
        }
    }

    @Override
    public boolean existsTokenAssociatedToUsername(String username) {
        if(verificationTokenRepository.findByUsername(username) != null){
            return true;
        }
        return false;
    }
}
