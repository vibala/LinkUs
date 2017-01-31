package pfe.ece.LinkUS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pfe.ece.LinkUS.Model.Moment;
import pfe.ece.LinkUS.Model.Notification;
import pfe.ece.LinkUS.Model.NotificationMoment;
import pfe.ece.LinkUS.Model.User;
import pfe.ece.LinkUS.Service.AlbumService;
import pfe.ece.LinkUS.Service.NotificationService;
import pfe.ece.LinkUS.Service.TokenService.AccessTokenService;
import pfe.ece.LinkUS.Service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DamnAug on 24/01/2017.
 */
@Controller
@RequestMapping("/userNotification")
public class NotificationController {

    @Autowired
    NotificationService notificationService;
    @Autowired
    AlbumService albumService;
    @Autowired
    AccessTokenService accessTokenService;
    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String getNotification(@RequestParam("notificationId") String notificationId) {

        Notification notification = notificationService.findNotification(notificationId);

        if(notification != null) {
            return notification.toString();
        }
        return null;
    }

    @RequestMapping("/getMoment")
    public String getNotificationMoment(@RequestParam("notificationId") String notificationId) {

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        NotificationMoment notificationMoment =
                (NotificationMoment) notificationService.findNotification(notificationId);

        if(notificationMoment != null) {
            List<String> momentIdList = new ArrayList<>();
            momentIdList.add(notificationMoment.getMomentId());

            List<Moment> momentList = albumService.findMomentsCheckRightInAlbum(
                    notificationMoment.getAlbumId(), userId, momentIdList);

            if(momentList != null && !momentList.isEmpty()) {
                return momentList.get(0).toString();
            }
        }
        return null;
    }

    @RequestMapping("/delete")
    public ResponseEntity deleteNotification(@RequestParam("notificationId") String notificationId) {

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();
        User user = userService.findUserById(userId);

        Notification notification = notificationService.findNotificationById(notificationId);

        if(notificationService.deleteNotification(notification, user)) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @RequestMapping("/deleteAll")
    public ResponseEntity deleteAllNotifications() {

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();
        User user = userService.findUserById(userId);

        List<Notification> notificationsList = notificationService.findNotificationsByIds(user.getNotificationList());
        if(notificationService.deleteNotifications(notificationsList, user)) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.CONFLICT);
    }
}