package pfe.ece.LinkUS.Repository.TokenMySQLRepo;

import org.springframework.data.repository.CrudRepository;
import pfe.ece.LinkUS.Model.VerificationToken;

import javax.transaction.Transactional;

/**
 * Created by Vignesh on 12/9/2016.
 */
@Transactional
public interface VerificationTokenRepository extends CrudRepository<VerificationToken,Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByUsername(String username);
}
