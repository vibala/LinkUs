package pfe.ece.LinkUS.Repository.OtherMongoDBRepo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import pfe.ece.LinkUS.Model.User;

import java.util.List;
import java.util.Optional;

/**
 * Created by DamnAug on 12/10/2016.
 */
public interface UserRepository extends MongoRepository<User, String> {

    List<User> findByFirstNameLikeOrLastNameLike(String firstname, String lastname, Pageable pageable);
    /**
     * Find Users by lastName, ignoring case
     * @param lastName
     * @return album
     */
    List<User> findByLastNameIgnoreCase(String lastName);

    /**
     * Find Users by firstName, ignoring case
     * @param firstName
     * @return album
     */
    List<User> findByFirstNameIgnoreCase(String firstName);

    /**
     * Find Users by id, ignoring case
     * @param id
     * @return album
     */
    List<User> findById(String id);

    /**
     * Find Users by email, ignoring case
     * @param email
     * @return Optional<User>
     */
    Optional<User> findOneByEmail(String email);

}
