package pfe.ece.LinkUS.Service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Model.Notification;
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
    public Notification createSaveNotification(String userId, String albumId, String momentId, Object o) {
        Notification notification = createNotification(userId, albumId, momentId, o);
        if(!addNotification(notification)) {
            modifyNotification(notification);
        }
        return notification;
    }

    public Notification createNotification(String userId, String albumId, String momentId, Object o) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setAlbumId(albumId);
        notification.setMomentId(momentId);
        notification.setType(o.getClass().toString());
        notification.setObject(o);
        return notification;
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
