package pfe.ece.LinkUS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pfe.ece.LinkUS.Model.UserAlarm;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.UserAlarmRepository;
import pfe.ece.LinkUS.Service.TokenService.AccessTokenService;
import pfe.ece.LinkUS.Service.UserAlarmService;

/**
 * Created by DamnAug on 20/01/2017.
 */
@Controller
@RequestMapping("/alarm")
public class UserAlarmController {


    @Autowired
    AccessTokenService accessTokenService;
    @Autowired
    UserAlarmRepository userAlarmRepository;

    @RequestMapping("/add")
    public ResponseEntity addAlarm() {

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        UserAlarmService userAlarmService = new UserAlarmService(userAlarmRepository);
        userAlarmService.addAlarm();

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping("/remove")
    public ResponseEntity removeAlarm() {

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        UserAlarmService userAlarmService = new UserAlarmService(userAlarmRepository);
        userAlarmService.removeAlarm();


        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping("/modify")
    public ResponseEntity modifyAlarm() {

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        UserAlarmService userAlarmService = new UserAlarmService(userAlarmRepository);
        userAlarmService.modifyAlarm();

        return new ResponseEntity(HttpStatus.OK);
    }
}
