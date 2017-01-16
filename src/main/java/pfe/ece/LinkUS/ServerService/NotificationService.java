package pfe.ece.LinkUS.ServerService;

import org.springframework.beans.factory.annotation.Autowired;
import pfe.ece.LinkUS.Model.Moment;
import pfe.ece.LinkUS.Repository.TokenMySQLRepo.NotificationTokenRepository;
import pfe.ece.LinkUS.Service.NotificationTokenServiceImpl;
import pfe.ece.LinkUS.Service.UserService;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Huong on 11/12/2016.
 */
public class NotificationService {
    /**
     * FireBase est le serveur qui va nous permettre de gérer les notifications
     */
    private static final String urlServerFireBase = "https://fcm.googleapis.com/fcm/send";
    private static final String keyServerFireBase="AAAAqKqxT3k:APA91bHQmHhapf24Zg1sdaCiqUSSi-wp_WbATkmLeCOa-qeQmFOG9ltZ9OwZvVwy94gKXvyWSRLKfHHhG95VQfWiebgXSh1JC_47cCdA9XnDxCg49V18CsEY1UiB2cUQufqGTvWqaceyKwKQbO1Zub6mHrsrJJJevQ";
    @Autowired
    UserService userservice;
    @Autowired
    NotificationTokenRepository notificationTokenRepository;

    public NotificationService(UserService userservice,NotificationTokenRepository notificationTokenRepository){
        this.userservice=userservice;
        this.notificationTokenRepository=notificationTokenRepository;
    }

    public ArrayList<String> getTokenUserListFromIdUserList(ArrayList<String>  usersIdInList){
        ArrayList<String> tokenUserList=new ArrayList<String>();

        // TODO Recuperation du TokenNotification depuis l'identifiant


        //TEMPORAIRE A SUPPRIMER QUAND IMPLEMENTATION VIGNESH FINI
            for(String userIdInList:usersIdInList){

                /*Structure de la table NotificationsTokens dans la BD : ID;USERNAME;TOKEN */
                String username = userservice.findUserById(userIdInList).getId();
                /*Recupération du token notif de chaque utilisateur à partir de l'username*/
                NotificationTokenServiceImpl notificationTokenService=new NotificationTokenServiceImpl(notificationTokenRepository);
                String notification_token = notificationTokenService.getNotifcationTokenByUsername(username);
                /*Ajout des tokens dans la liste tokenUserList*/
                tokenUserList.add(notification_token);
            }
        //-----

        return tokenUserList;
    }
    public void sendMomentWithTokenNotification(ArrayList<String> listUserTokenNotification, Moment moment) throws IOException {
        for(String token : listUserTokenNotification) {
            /**EXEMPLE
             *
             * Content-Type:application/json
             * Authorization:key=AAAAqKqxT3k:APA91bHQmHhapf24Zg1sdaCiqUSSi-wp_WbATkmLeCOa-qeQmFOG9ltZ9OwZvVwy94gKXvyWSRLKfHHhG95VQfWiebgXSh1JC_47cCdA9XnDxCg49V18CsEY1UiB2cUQufqGTvWqaceyKwKQbO1Zub6mHrsrJJJevQ
             *
             * {
             "to": "c4jGkjlpZR8:APA91bGLnUHESRso2e1aYFT77-2NebDBf3r-eII1uvRL965VGWIJohmAfdr_VjqD-5NJ2T9TYWM8kdaPIWvziA1jXmQpd-MReZHO0eycgJvIhUwqTRYn8ElWFXcc5dL2aSqUho37mWtB"
             ,
             "data": {
             "title": "title-data",
             "description": "description-data"
             }
             ,
             "notification": {
             "title": "title-notification",
             "body": "description-notification"
             }
             }
             *
             */
            String json = "{\"notification\":{\"title\":\"title-notification\",\"body\":\"description-notification\"},\"data\":{\"description\":\"description-data\",\"title\":\"title-data\"},\"to\":\""+token+"\"}";

            URL url = new URL(urlServerFireBase);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json;");
            conn.setRequestProperty("Authorization", "key="+keyServerFireBase);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();

            // read the response
            InputStream input = new BufferedInputStream(conn.getInputStream());
            //String result = IOUtils.toString(input);

            input.close();
            conn.disconnect();
        }
    }
}
