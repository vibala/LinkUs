package pfe.ece.LinkUS.Service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Model.*;
import pfe.ece.LinkUS.Model.Enum.NotificationType;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.NotificationRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.UserRepository;
import pfe.ece.LinkUS.Repository.TokenMySQLRepo.NotificationTokenRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DamnAug on 24/01/2017.
 */
@Service
public class NotificationService {
    @Autowired
    NotificationTokenRepository notificationTokenRepository;

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

    public String getTokenByUserId(String userId){

        //Structure de la table NotificationsTokens dans la BD : ID;USERNAME;TOKEN
        //Recupération du token notif de chaque utilisateur à partir de l'username
        NotificationTokenServiceImpl notificationTokenService = new NotificationTokenServiceImpl(notificationTokenRepository);
        //username correspond a l'id dans la BD SQL
        return notificationTokenService.getNotifcationTokenByUsername(userId);

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
        createNotification(notificationMoment,userId,type);
        notificationMoment.setAlbumId(albumId);
        notificationMoment.setMomentId(momentId);
        return notificationMoment;
    }

    public NotificationFriendRequest createSaveNotificationFriendRequest(User user,User fromUser, NotificationType type) {
        NotificationFriendRequest notificationFriendRequest = createNotificationFriendRequest(user.getId(),type,fromUser.getId());
        if(!addNotification(notificationFriendRequest)) {
            modifyNotification(notificationFriendRequest);
        } else {
            user.getNotificationList().add(notificationFriendRequest.getId());
        }
        return notificationFriendRequest;
    }

    public NotificationFriendRequest createNotificationFriendRequest(String userId, NotificationType type,String fromUserId) {
        NotificationFriendRequest notificationFriendRequest = new NotificationFriendRequest();
        createNotification(notificationFriendRequest,userId,type);
        notificationFriendRequest.setFromFriendId(fromUserId);

        return notificationFriendRequest;
    }
    public void createNotification(Notification notification,String userId,NotificationType type) {
        notification.setUserId(userId);
        notification.setType(type);
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
