package pfe.ece.LinkUS.Repository.OtherMongoDBRepo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pfe.ece.LinkUS.Model.OAuth2AuthenticationAccessToken;

import java.util.List;

/**
 * Created by Vignesh on 12/8/2016.
 */
@Repository
public interface OAuth2AccessTokenRepository extends MongoRepository<OAuth2AuthenticationAccessToken,String> {

    public OAuth2AuthenticationAccessToken findByTokenId(String tokenId);

    public OAuth2AuthenticationAccessToken findByRefreshToken(String refreshToken);

    public OAuth2AuthenticationAccessToken findByAuthenticationId(String authenticationId);

    public List<OAuth2AuthenticationAccessToken> findByClientIdAndUserName(String clientId, String userName);

    public List<OAuth2AuthenticationAccessToken> findByClientId(String clientId);

}
