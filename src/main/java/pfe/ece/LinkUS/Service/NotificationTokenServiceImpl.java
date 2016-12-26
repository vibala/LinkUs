package pfe.ece.LinkUS.Service;

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
        System.out.println("6"+notificationTokenRepository);
    }
    @Override
    public String getNotifcationTokenByUsername(String username) {
        System.out.println("7"+notificationTokenRepository);
        if(notificationTokenRepository.findByUsername(username).getToken()!= null &&
                !notificationTokenRepository.findByUsername(username).getToken().isEmpty()){
            return notificationTokenRepository.findByUsername(username).getToken();
        }

        return null;
    }

    @Override
    public void registerNotificationTokenEntity(NotificationToken notificationToken) throws Exception {
        if(notificationToken.getToken() == null || notificationToken.getToken().isEmpty()){
            throw new Exception("Token element in the notification token entity is either null or empty");
        }

        if(notificationToken.getUsername() == null || notificationToken.getUsername().isEmpty()){
            throw new Exception("Username element in the notification token entity is either null or empty");
        }

        notificationTokenRepository.save(notificationToken);
    }

}
