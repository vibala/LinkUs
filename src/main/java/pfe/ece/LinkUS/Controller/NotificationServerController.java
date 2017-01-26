package pfe.ece.LinkUS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfe.ece.LinkUS.Model.NotificationToken;
import pfe.ece.LinkUS.Service.NotificationTokenService;
import pfe.ece.LinkUS.Service.TokenService.AccessTokenService;

/**
 * Created by Huong on 11/12/2016.
 */

@RestController
@RequestMapping("/notification")
public class NotificationServerController {

    private NotificationTokenService notificationTokenService;
    private AccessTokenService accessTokenService;

    @Autowired
    public NotificationServerController(NotificationTokenService notificationTokenService, AccessTokenService accessTokenService) {
        this.notificationTokenService = notificationTokenService;
        this.accessTokenService=accessTokenService;
    }

    @RequestMapping("/")
    public String unused() {
        return "Not implemented yet.";
    }

    /* Persistence des tokens */
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<String> sendNotificationToken(@RequestBody String notification_token) {
        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();
        notification_token=notification_token.replace("\"","");
        NotificationToken token = new NotificationToken(userId,notification_token);

        try {
            notificationTokenService.registerNotificationTokenEntity(token);
        } catch (Exception e) {
            return new ResponseEntity<String>("msg.Fail : Error creating the token : " + e.toString(), HttpStatus.OK);
        }
        return new ResponseEntity<String>("msg.Success : Token successfully created", HttpStatus.OK);
    }
}
