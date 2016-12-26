package pfe.ece.LinkUS.Repository.OtherMongoDBRepo;

import org.springframework.data.mongodb.repository.MongoRepository;
import pfe.ece.LinkUS.Model.SubscriptionType;

/**
 * Created by DamnAug on 22/11/2016.
 */
public interface SubscriptionTypeRepository extends MongoRepository<SubscriptionType, String> {

    SubscriptionType findSubscriptionTypeByTypeAndLengthAndUnit(String type, String length, String unit);
}
