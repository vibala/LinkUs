package pfe.ece.LinkUS.Service;

import pfe.ece.LinkUS.Model.NotificationToken;

/**
 * Created by Vignesh on 12/15/2016.
 */
public interface NotificationTokenService {
    String getNotifcationTokenByUsername(String username);
    void registerNotificationTokenEntity(NotificationToken notificationToken) throws Exception;
}
