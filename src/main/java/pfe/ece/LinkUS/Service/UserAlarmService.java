package pfe.ece.LinkUS.Service;

import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Model.UserAlarm;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.UserAlarmRepository;

import java.util.logging.Logger;

/**
 * Created by DamnAug on 20/01/2017.
 */
@Service
public class UserAlarmService {

    Logger LOGGER = Logger.getLogger("LinkUS.Service.UserAlarmService");

    UserAlarmRepository userAlarmRepository;

    public UserAlarmService(UserAlarmRepository userAlarmRepository) {
        this.userAlarmRepository = userAlarmRepository;
    }

    public boolean addAlarm(UserAlarm userAlarm) {

        if(userAlarm.getId() != null &&
                userAlarmRepository.findOne(userAlarm.getId()) == null) {
            userAlarmRepository.save(userAlarm);
            return true;
        }
        return false;
    }

    public boolean removeAlarm(UserAlarm userAlarm) {
        if(userAlarm.getId() != null) {
            userAlarmRepository.delete(userAlarm.getId());
            return true;
        }
        return false;
    }

    public boolean modifyAlarm(UserAlarm userAlarm) {

        if(userAlarm.getId() != null &&
                userAlarmRepository.findOne(userAlarm.getId()) != null) {
            userAlarmRepository.save(userAlarm);
            return true;
        }
        return false;
    }
}
