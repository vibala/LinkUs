package pfe.ece.LinkUS.Service;

import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.UserAlarmRepository;

import java.util.logging.Logger;

/**
 * Created by DamnAug on 20/01/2017.
 */
@Service
public class UserAlarmService {

    Logger LOGGER = Logger.getLogger("LinkUS.Controller.UserAlarmService");

    UserAlarmRepository userAlarmRepository;

    public UserAlarmService(UserAlarmRepository userAlarmRepository) {
        this.userAlarmRepository = userAlarmRepository;
    }

    public boolean addAlarm() {


        return false;

    }

    public boolean removeAlarm() {

        return false;
    }

    public boolean modifyAlarm() {

        return false;
    }
}
