package pfe.ece.LinkUS.Repository.OtherMongoDBRepo;

import org.springframework.data.mongodb.repository.MongoRepository;
import pfe.ece.LinkUS.Model.Notification;
import pfe.ece.LinkUS.Model.NotificationMoment;

/**
 * Created by DamnAug on 24/01/2017.
 */
public interface NotificationRepository extends MongoRepository<Notification, String> {



}
