package pfe.ece.LinkUS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pfe.ece.LinkUS.Model.Notification;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.NotificationRepository;
import pfe.ece.LinkUS.Service.NotificationService;

/**
 * Created by DamnAug on 24/01/2017.
 */
@RequestMapping("/userNotification")
public class NotificationController {


    @Autowired
    NotificationRepository notificationRepository;

    @RequestMapping("/")
    public String getNotificationObject(@RequestParam("notificationId") String notificationId) {

        NotificationService notificationService = new NotificationService(notificationRepository);

        Notification notification = notificationService.findNotification(notificationId);
        if(notification != null) {
            return notification.getObject().toString();
        }
        return null;
    }
}
