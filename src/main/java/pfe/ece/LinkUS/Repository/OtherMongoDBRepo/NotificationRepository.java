package pfe.ece.LinkUS.Repository.OtherMongoDBRepo;

import org.springframework.data.mongodb.repository.MongoRepository;
import pfe.ece.LinkUS.Model.Notification;

import java.util.List;

/**
 * Created by DamnAug on 24/01/2017.
 */
public interface NotificationRepository extends MongoRepository<Notification, String> {

    List<Notification> findByIdAndUserId(String id, String userId);
}
