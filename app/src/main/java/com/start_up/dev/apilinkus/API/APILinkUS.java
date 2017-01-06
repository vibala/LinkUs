package com.start_up.dev.apilinkus.API;

import android.content.Context;

import com.start_up.dev.apilinkus.Model.Instant;
import com.start_up.dev.apilinkus.Model.Moment;

/**
 * Created by Huong on 06/11/2016.
 */

public class APILinkUS {

    private Context mContext;
    public static String BASE_URL;
    //private String userId="1";

    public APILinkUS(Context mContext) {
        this.mContext=mContext;
        this.BASE_URL="http://192.168.43.128:9999";
    }
    public APILinkUS() {
    }

    public String setIPServer(String ipAddress){
        this.BASE_URL=ipAddress;
        return BASE_URL;
    }
    /**
     *
     * @param m
     * @param notificationToPeopleWithReadRightOnAlbum true pour envoyer une notification aux utilisateur ayant des droits de lecture sur l'album
     * @return
     */
    public Moment addMomentToMyAlbum(Moment m, String notificationToPeopleWithReadRightOnAlbum){

        //String query="/uploadFiles?userId="+userId+"&albumId="+albumId+"&notificationToPeopleWithReadRightOnAlbum="+notificationToPeopleWithReadRightOnAlbum;
        String query="/uploadFiles?notificationToPeopleWithReadRightOnAlbum="+notificationToPeopleWithReadRightOnAlbum;

        String urlrequestAPI = BASE_URL + query;
        System.out.println("aaaaaaaa"+urlrequestAPI);
        APIPostMoment apiPostStudent = new APIPostMoment(m);
        System.out.println("bbbbbbbbb"+urlrequestAPI);
        apiPostStudent.execute(urlrequestAPI);
        System.out.println("vvvvvvvv"+urlrequestAPI);
        return m;
    }

    public void getAlbumsOwned(APIGetAlbumsOwned_Observer activity){
        //String query="/album/Owned?userId="+userId;
        String query="/album/owned";

        String urlrequestAPI = BASE_URL + query;
        APIGetAlbumsOwned apiGetAlbum = new APIGetAlbumsOwned(activity);
        apiGetAlbum.execute(urlrequestAPI);
    }
    public void getAlbumsFilter(APIGetAlbumsFilterRight_Observer activity, String right){
        String query="/album/right?right="+right;

        String urlrequestAPI = BASE_URL + query;
        APIGetAlbumsFilterRight apiGetAlbum = new APIGetAlbumsFilterRight(activity);
        apiGetAlbum.execute(urlrequestAPI);
    }
    public void sendTokenNotification(){

        //String query="/notification/token?userId="+userId;
        String query="/notification/token";
        String urlrequestAPI = APILinkUS.BASE_URL + query;
        APIPostTokenNotification apiPostToken = new APIPostTokenNotification();
        apiPostToken.execute(urlrequestAPI);
    }

    public void addFriendByMail(String album_read_mail_friend){

        String query="/album/setwith?email="+album_read_mail_friend;

        String urlrequestAPI = BASE_URL + query;
        APIPostAddFriendByMail apiPostStudent = new APIPostAddFriendByMail(album_read_mail_friend);
        apiPostStudent.execute(urlrequestAPI);

    }

}
