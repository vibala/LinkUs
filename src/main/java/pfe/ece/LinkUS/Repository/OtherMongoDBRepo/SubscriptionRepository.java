package pfe.ece.LinkUS.Repository.OtherMongoDBRepo;

import org.springframework.data.mongodb.repository.MongoRepository;
import pfe.ece.LinkUS.Model.Subscription;

/**
 * Created by DamnAug on 15/11/2016.
 */
public interface SubscriptionRepository extends MongoRepository<Subscription, String> {

   Subscription findByUserIdAndType(String userId, String type);

   Subscription findSubscriptionByTypeAndUserId(String type, String userId);



}
