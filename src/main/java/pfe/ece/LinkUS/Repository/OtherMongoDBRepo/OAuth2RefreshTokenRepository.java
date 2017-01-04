package pfe.ece.LinkUS.Repository.OtherMongoDBRepo;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pfe.ece.LinkUS.Model.OAuth2AuthenticationRefreshToken;

/**
 * Created by Vignesh on 12/8/2016.
 */
@Repository
public interface OAuth2RefreshTokenRepository extends MongoRepository<OAuth2AuthenticationRefreshToken,String> {

    public OAuth2AuthenticationRefreshToken findByTokenId(String tokenId);

}
