package pfe.ece.LinkUS.ServerService;

import org.springframework.beans.factory.annotation.Autowired;
import pfe.ece.LinkUS.Model.Notification;
import pfe.ece.LinkUS.Model.NotificationFriendRequest;
import pfe.ece.LinkUS.Model.NotificationMoment;
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
public class NotificationServerService {
    /**
     * FireBase est le serveur qui va nous permettre de gérer les notifications
     */
    private static final String urlServerFireBase = "https://fcm.googleapis.com/fcm/send";
    private static final String keyServerFireBaseSmartphone="AAAAqKqxT3k:APA91bHQmHhapf24Zg1sdaCiqUSSi-wp_WbATkmLeCOa-qeQmFOG9ltZ9OwZvVwy94gKXvyWSRLKfHHhG95VQfWiebgXSh1JC_47cCdA9XnDxCg49V18CsEY1UiB2cUQufqGTvWqaceyKwKQbO1Zub6mHrsrJJJevQ";
    private static final String keyServerFireBaseTablet="AAAAw0EBF_o:APA91bFPh1ZH3dTfjtQYu5lQPA-Rjt_xXMobVu9SfJnnsNdJQhKdRqNHxawqvlSDcfhRVkk_qZ3Wn1WIEdq2AW8bX4VZTPyo8xQo-IB55-zyZveUOe1_BanSv94dtvoOdrYqsqjIaqs5";
    @Autowired
    UserService userservice;
    @Autowired
    NotificationTokenRepository notificationTokenRepository;

    public NotificationServerService(UserService userservice, NotificationTokenRepository notificationTokenRepository){
        this.userservice=userservice;
        this.notificationTokenRepository=notificationTokenRepository;
    }

    public void sendNotificationFriendRequest(NotificationFriendRequest notificationFriendRequest, String token) throws IOException {

        URL url = new URL(urlServerFireBase);

        String json = "{\"data\":"
                +"\"id\":\""+notificationFriendRequest.getId()+"\","
                +"\"fromFriendId\":\""+notificationFriendRequest.getFromFriendId()+"\","
                +"\"type\": \""+notificationFriendRequest.getType()+"\"},"
                +"\"description\": \""+notificationFriendRequest.getMessage()+" \","
                +"\"to\" : \""+token+"\"}";

        sendNotificationRequestToFirebase(url,keyServerFireBaseSmartphone,json);
        //TODO UNIMPLEMENTED sendToFirebase(url,token,notificationMoment,keyServerFireBaseTablet,title);
    }

    public void sendNotificationMoment(NotificationMoment notificationMoment,String token) throws IOException {

        URL url = new URL(urlServerFireBase);
        String title ="Vous avez reçu un nouveau moment";

        String json = "{\"data\": {\"albumId\": \""+notificationMoment.getAlbumId()+"\","
                +"\"userId\":\""+notificationMoment.getUserId()+"\","
                +"\"momentId\": \""+notificationMoment.getMomentId()+"\","
                +"\"id\":\""+notificationMoment.getId()+"\","
                +"\"title\": \""+title+" \","
                +"\"type\": \""+notificationMoment.getType()+"\"},"
                +"\"to\" : \""+token+"\"}";

        sendNotificationRequestToFirebase(url,keyServerFireBaseSmartphone,json);
        sendNotificationRequestToFirebase(url,keyServerFireBaseTablet,json);

    }

    private void sendNotificationRequestToFirebase( URL url,String keyServerFireBase,String json) throws IOException {

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
        InputStream input;
        try {
            // read the response
            input = new BufferedInputStream(conn.getInputStream());
            input.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        conn.disconnect();

    }
}
