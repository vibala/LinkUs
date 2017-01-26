package pfe.ece.LinkUS.Repository.OtherMongoDBRepo;

import org.springframework.data.mongodb.repository.MongoRepository;
import pfe.ece.LinkUS.Model.UserAlarm;

/**
 * Created by DamnAug on 20/01/2017.
 */
public interface UserAlarmRepository extends MongoRepository<UserAlarm, String> {



}
