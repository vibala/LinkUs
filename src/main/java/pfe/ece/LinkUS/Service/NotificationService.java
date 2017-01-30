package pfe.ece.LinkUS.Service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Model.Enum.NotificationType;
import pfe.ece.LinkUS.Model.Moment;
import pfe.ece.LinkUS.Model.Notification;
import pfe.ece.LinkUS.Model.NotificationMoment;
import pfe.ece.LinkUS.Model.User;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.NotificationRepository;

import java.util.ArrayList;
import java.util.List;

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

    public List<Notification> findNotificationsByIds(List<String> idList) {
        List<Notification> notificationList = new ArrayList<>();

        if(idList != null) {
            for(String id: idList) {
                notificationList.add(findNotification(id));
            }
        }
        return notificationList;
    }
    public Notification findNotificationById(String notifId) {
        return notificationRepository.findOne(notifId);
    }

    public NotificationMoment createSaveNotificationMoment(User user, String albumId, String momentId, NotificationType type) {

        NotificationMoment notificationMoment = createNotificationMoment(user.getId(), albumId, momentId,type);
        if(!addNotification(notificationMoment)) {
            modifyNotification(notificationMoment);
        } else {
            user.getNotificationList().add(notificationMoment.getId());
        }
        return notificationMoment;
    }

    public NotificationMoment createNotificationMoment(String userId, String albumId, String momentId,NotificationType type) {
        NotificationMoment notificationMoment = new NotificationMoment();
        notificationMoment.setUserId(userId);
        notificationMoment.setAlbumId(albumId);
        notificationMoment.setMomentId(momentId);
        notificationMoment.setType(type);
        return notificationMoment;
    }

    public boolean addNotification(Notification notification) {

        if(notification.getId() != null &&
                notificationRepository.findOne(notification.getId()) == null) {
            LOGGER.info("Creating notification: " + notification);
            notificationRepository.save(notification);
            return true;
        }
        return false;
    }

    public boolean deleteNotifications(List<Notification> notificationList, User user) {

        boolean bool = true;
        for(Notification notification: notificationList) {
            if(!deleteNotification(notification, user)) {
                bool = false;
            }
        }
        return bool;
    }

    public boolean deleteNotification(Notification notification, User user) {

        if(notification.getId() != null) {
            LOGGER.info("Deleting notification: " + notification.getId());

            if(user.getNotificationList().contains(notification.getId())) {
                user.getNotificationList().remove(notification.getId());
            }
            notificationRepository.delete(notification.getId());
            return true;
        }
        return false;
    }

    public boolean modifyNotification(Notification notification) {

        if(notification.getId() != null &&
                notificationRepository.findOne(notification.getId()) != null) {
            LOGGER.info("Modifying notification: " + notification.getId());
            notificationRepository.save(notification);
            return true;
        }
        return false;
    }

}
