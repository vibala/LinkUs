package pfe.ece.LinkUS.Service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Model.Moment;
import pfe.ece.LinkUS.Model.Notification;
import pfe.ece.LinkUS.Model.NotificationMoment;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.NotificationRepository;

/**
 * Created by DamnAug on 24/01/2017.
 */
 @Service
public class NotificationService {

    Logger LOGGER = Logger.getLogger("LinkUS.Service.NotificationService");

    NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification findNotification(String id) {
        return notificationRepository.findOne(id);
    }
    public NotificationMoment createSaveNotificationMoment(String userId, String albumId, String momentId) {
        NotificationMoment notificationMoment = createNotificationMoment(userId, albumId, momentId);
        if(!addNotification(notificationMoment)) {
            modifyNotification(notificationMoment);
        }
        return notificationMoment;
    }

    public NotificationMoment createNotificationMoment(String userId, String albumId, String momentId) {
        NotificationMoment notificationMoment = new NotificationMoment();
        notificationMoment.setUserId(userId);
        notificationMoment.setAlbumId(albumId);
        notificationMoment.setMomentId(momentId);
        notificationMoment.setType("Moment");
        return notificationMoment;
    }

    public boolean addNotification(Notification notification) {

        if(notification.getId() != null &&
                notificationRepository.findOne(notification.getId()) == null) {
            notificationRepository.save(notification);
            return true;
        }
        return false;
    }

    public boolean removeNotification(Notification notification) {
        if(notification.getId() != null) {
            notificationRepository.delete(notification.getId());
            return true;
        }
        return false;
    }

    public boolean modifyNotification(Notification notification) {

        if(notification.getId() != null &&
                notificationRepository.findOne(notification.getId()) != null) {
            notificationRepository.save(notification);
            return true;
        }
        return false;
    }

}
