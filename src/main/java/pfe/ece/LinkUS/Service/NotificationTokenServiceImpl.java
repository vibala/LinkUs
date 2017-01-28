package pfe.ece.LinkUS.Service;

import com.google.firebase.internal.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Model.NotificationToken;
import pfe.ece.LinkUS.Repository.TokenMySQLRepo.NotificationTokenRepository;

/**
 * Created by Vignesh on 12/15/2016.
 */
@Service
public class NotificationTokenServiceImpl implements NotificationTokenService{

    @Autowired
    NotificationTokenRepository notificationTokenRepository;

    public NotificationTokenServiceImpl (NotificationTokenRepository notificationTokenRepository){
        this.notificationTokenRepository=notificationTokenRepository;
    }

    @Override
    public String getNotifcationTokenByUsername(String username) {
        /**
         *   ONE   ADDDDDDD
         */

        String token =null;
        try {
            token = notificationTokenRepository.findByUsername(username).getToken();
        } catch(Exception e){
            e.getStackTrace();
        }
        if(token != null && !token.isEmpty()){
            return token;
        } else {
            Log.d("ERROR","token not found from user : " + username);
            return null;
        }
    }

    public void removeNotifcationTokenByUsername(String username) {

        NotificationToken notificationToken = notificationTokenRepository.findByUsername(username);
        if(notificationToken != null) {
            notificationTokenRepository.delete(notificationToken);
        }
    }

    @Override
    public void registerNotificationTokenEntity(NotificationToken notificationToken) throws Exception {
        if(notificationToken.getToken() == null || notificationToken.getToken().isEmpty()){
            throw new Exception("Token element in the notification token entity is either null or empty");
        }

        if(notificationToken.getUsername() == null || notificationToken.getUsername().isEmpty()){
            throw new Exception("Username element in the notification token entity is either null or empty");
        }
        removeNotifcationTokenByUsername(notificationToken.getUsername());
        notificationTokenRepository.save(notificationToken);
    }
}