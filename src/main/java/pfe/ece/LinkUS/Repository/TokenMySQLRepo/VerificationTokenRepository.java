package pfe.ece.LinkUS.Repository.TokenMySQLRepo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pfe.ece.LinkUS.Model.VerificationToken;
import javax.transaction.Transactional;

/**
 * Created by Vignesh on 12/9/2016.
 */
@Transactional
public interface VerificationTokenRepository extends CrudRepository<VerificationToken,Long> {

    @Query("SELECT t FROM VerificationToken t WHERE t.token = ?1")
    VerificationToken findByToken(String token);

    @Query("SELECT t FROM VerificationToken t WHERE t.username = ?1")
    VerificationToken findByUsername(String username);
}
