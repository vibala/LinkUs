package pfe.ece.LinkUS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pfe.ece.LinkUS.Model.Album;
import pfe.ece.LinkUS.Model.Moment;
import pfe.ece.LinkUS.Model.NotificationFriendRequest;
import pfe.ece.LinkUS.Model.NotificationMoment;
import pfe.ece.LinkUS.Service.AlbumService;
import pfe.ece.LinkUS.Service.MomentService;
import pfe.ece.LinkUS.Service.NotificationService;
import pfe.ece.LinkUS.Service.TokenService.AccessTokenService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DamnAug on 24/01/2017.
 */
@RequestMapping("/userNotification")
public class NotificationController {


    @Autowired
    NotificationService notificationService;
    @Autowired
    AlbumService albumService;
    @Autowired
    AccessTokenService accessTokenService;

    @RequestMapping("/moment")
    public String getNotificationMoment(@RequestParam("notificationId") String notificationId) {

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        NotificationMoment notificationMoment =
                (NotificationMoment) notificationService.findNotification(notificationId);
        if(notificationMoment != null) {

            MomentService momentService = new MomentService();

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

    @RequestMapping("/friendRequest")
    public String getNotificationFriend(@RequestParam("notificationId") String notificationId) {

        NotificationFriendRequest notificationFriendRequest =
                (NotificationFriendRequest)notificationService.findNotification(notificationId);
        if(notificationFriendRequest != null) {



        }
        return null;
    }
}
