package com.start_up.dev.apilinkus.API;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.start_up.dev.apilinkus.Model.Album;
import com.start_up.dev.apilinkus.Model.FriendGroup;
import com.start_up.dev.apilinkus.Model.Moment;
import com.start_up.dev.apilinkus.Model.Subscription;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Huong on 06/11/2016.
 */

public class APILinkUS {

    private Context mContext;
    private final String TAG = APILinkUS.class.getSimpleName();
    public static String BASE_URL="http://192.168.137.77:9999";
    //private String userId="1";

    //ARRETER DE TOUCHER LES CONSTRUCTEURS 
    public APILinkUS(Context mContext) {
        this.mContext=mContext;
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
    public Moment addMomentToMyAlbum(Moment m, String albumId, String notificationToPeopleWithReadRightOnAlbum,APIPostMoment_Observer observer,Context mContext){

        String query="/uploadFiles?albumId="+albumId+"&notificationToPeopleWithReadRightOnAlbum="+notificationToPeopleWithReadRightOnAlbum;

        String urlrequestAPI = BASE_URL + query;
        APIPostMoment apiPostStudent = new APIPostMoment(m,observer,mContext);
        apiPostStudent.execute(urlrequestAPI);
        return m;
    }

    public void getPreviewAlbumsOwned(APIGetAlbumsOwned_Observer activity, Activity parent_activity){
        String query="/album/preview?right=ADMIN&news=true";

        String urlrequestAPI = BASE_URL + query;
        APIGetAlbumsOwned apiGetAlbum = new APIGetAlbumsOwned(activity,parent_activity);
        apiGetAlbum.execute(urlrequestAPI);
    }

    public void logout(){
        String query="/oauth/revoke-token";

        String urlrequestAPI = BASE_URL + query;
        APIGetRevokeToken revokeToken = new APIGetRevokeToken();
        revokeToken.execute();
    }

    public void getAlbumByAlbumId(APIGetAlbumByAlbumId_Observer activity, String albumId,Context context){
        String query="/album/searchAlbum?albumId="+albumId+"&news=true";

        String urlrequestAPI = BASE_URL + query;
        APIGetAlbumByAlbumId apiGetAlbumByAlbumId = new APIGetAlbumByAlbumId(activity,context);
        apiGetAlbumByAlbumId.execute(urlrequestAPI);
    }


    public void getAlbumsFilter(APIGetAlbumsFilterRight_Observer activity, String right){
        String query="/album/preview?right="+right+"&news=true";

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
    public void sendFriendRequest(APIPostOneString_Observer obs,String idFriendUser){

        String query="/user/friendRequest";
        String urlrequestAPI = BASE_URL + query;
        new APIPostOneString(idFriendUser,obs,"sendFriendRequest").execute(urlrequestAPI);

    }
    public void createGroup(APIPostCreateGroupFriend_Observer obs,FriendGroup group){

        String query="/friendGroup/addFilled";

        String urlrequestAPI = BASE_URL + query;
        new APIPostCreateGroupFriend(obs,group).execute(urlrequestAPI);

    }

    public void removeFriend(APIPostOneString_Observer obs,String idFriendUser){

        String query="/user/removeFriend";

        String urlrequestAPI = BASE_URL + query;
        new APIPostOneString(idFriendUser,obs,"removeFriend").execute(urlrequestAPI);

    }
    public void removeGroupFriend(APIPostOneString_Observer obs,String idGroupFriend){
        String query="/friendGroup/remove";

        String urlrequestAPI = BASE_URL + query;
        new APIPostOneString(idGroupFriend,obs,"removeGroupFriend").execute(urlrequestAPI);
    }

    public void getSearchListUser(APIGetSearchListUser_Observer fragment, String text){
        String query="/user/searchUser?text="+text;

        String urlrequestAPI = BASE_URL + query;
        new APIGetSearchListUser( fragment).execute(urlrequestAPI);
    }

    public void getListGroupFriend(APIGetListGroupFriend_Observer fragment){
        String query="/user/getGroupFriends";

        String urlrequestAPI = BASE_URL + query;
        new APIGetListGroupFriend(fragment).execute(urlrequestAPI);
    }
    public void getListFriend(APIGetListFriend_Observer fragment){
        String query="/user/getFriends";

        String urlrequestAPI = BASE_URL + query;
        new APIGetListFriend(fragment).execute(urlrequestAPI);
    }
    public void getRequestPendingListFriend(APIGetRequestPendingListFriend_Observer fragment){
        String query="/user/getRequestPendingFriends";

        String urlrequestAPI = BASE_URL + query;
        new APIGetPendingListFriend(fragment).execute(urlrequestAPI);
    }

    public void getUserProfileDetails(APIGetUserProfileDetails_Observer activityObserver, Context mContext){

        String query="/user/";
        String urlrequestAPI = BASE_URL + query;
        APIGetUserProfileDetails apiGetUserProfileDetails = new APIGetUserProfileDetails(activityObserver,mContext);
        apiGetUserProfileDetails.execute(urlrequestAPI);
    }

    public void getNbofFriendsAndAlbumOwned(APIGetUserNbFriendsAndNbOwnedAlbums_Observer activityObserver){

        String query="/user/getProchesAndAlbums";
        String urlrequestAPI = BASE_URL + query;
        APIGetUserNbFriendsAndNbOwnedAlbums apiGetUserProfileDetails = new APIGetUserNbFriendsAndNbOwnedAlbums(activityObserver);
        apiGetUserProfileDetails.execute(urlrequestAPI);
    }

    public void changeUsernameWhichisEquivalentToTheUserEmail(String username){
        String query = "/user/changeUsername?email=" + username;
        String urlrequestAPI = BASE_URL + query;
        APIPostChangeUsername apiPostChangeUsername = new APIPostChangeUsername();
        apiPostChangeUsername.execute(urlrequestAPI);
    }

    public void changeUserFullName(String lastName, String firstName){
        String query = "/user/changeFullname?lastName="+lastName+"&firstName="+firstName;
        String urlrequestAPI = BASE_URL + query;
        APIPostChangeFullName apiPostChangeFullName = new APIPostChangeFullName();
        apiPostChangeFullName.execute(urlrequestAPI);

    }

    public void getSubscriptionList(APIGetSubscriptionList_Observer activity){
        String query = "/subscription/getsubliste";
        String urlrequestAPI = BASE_URL + query;
        APIGetSubscriptionList apiGetSubscriptionList = new APIGetSubscriptionList(activity);
        apiGetSubscriptionList.execute(urlrequestAPI);
    }

    public String createNewAlbum(Album album){
        String query = "/album/create";
        String urlrequestAPI = BASE_URL + query;
        APIPostCreateAlbum apiPostCreateAlbum = new APIPostCreateAlbum(album);
        String result = "";
        try {
            result = apiPostCreateAlbum.execute(urlrequestAPI).get().toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void shareAlbumWithFriend(String friendId, String albumId, String rigth,APIPostShareAlbumWith_Observer observer){
        String query = "/album/setwith?albumId="+albumId+"&right="+rigth+"&friendId="+friendId;
        String urlrequestAPI = BASE_URL + query;
        APIPostShareAlbumWith apiPostShareAlbumWithFriend = new APIPostShareAlbumWith(observer);
        apiPostShareAlbumWithFriend.execute(urlrequestAPI);
    }

    public void shareAlbumWithGroupFriend(String friendGroupId, String albumId, String rigth,APIPostShareAlbumWith_Observer observer){
        String query = "/album/shareWithFriendGroup?albumId="+albumId+"&right="+rigth+"&friendGroupId="+friendGroupId;
        String urlrequestAPI = BASE_URL + query;
        APIPostShareAlbumWith apiPostShareAlbumWithGroupFriend = new APIPostShareAlbumWith(observer);
        apiPostShareAlbumWithGroupFriend.execute(urlrequestAPI);
    }

    public void updateSubscription(Subscription subscription, String subscriptionTypeId){
        String query = "/subscription/update?subscriptionTypeId="+ subscriptionTypeId;
        String urlrequestAPI = BASE_URL + query;
        APIPostAddSubscription apiPostAddSubscription = new APIPostAddSubscription(subscription);
        apiPostAddSubscription.execute(urlrequestAPI);
    }

    public void findMomentsInAlbum(String albumId, List<String> momentIdList, APIPostMomentsInAlbum_Observer observer){
        String query = "/findMoments?albumId="+albumId;
        String urlrequestAPI = BASE_URL + query;
        APIPostMomentsInAlbum apiPostMomentsInAlbum = new APIPostMomentsInAlbum(observer,momentIdList);
        apiPostMomentsInAlbum.execute(urlrequestAPI);
    }

    public void findFriendProfileByIdFromPendingFriends(String friendId,APIGetFriendProfileByIdFromPendingFriends_Observer observer){
        String query = "/user/getFriendProfileByIdFromPendingFriends?friendId="+friendId;
        String urlrequestAPI = BASE_URL + query;
        APIGetFriendProfileByIdFromPendingFriends apiGetFriendProfileByIdFromPendingFriends = new APIGetFriendProfileByIdFromPendingFriends(observer);
        apiGetFriendProfileByIdFromPendingFriends.execute(urlrequestAPI);
    }

    public void postFriendRequestDecision(String friendId,boolean decision,APIPostFriendRequestDecision_Observer observer){
        String query = "/friendRequestDecision?friendId="+friendId+"&decision="+decision;
        String urlrequestAPI = BASE_URL + query;
        APIPostFriendRequestDecision apiPostFriendRequestDecision = new APIPostFriendRequestDecision(observer);
        apiPostFriendRequestDecision.execute(urlrequestAPI,friendId);
    }
}


/** post multiple String, je sais pas encore comment gérer les multiple body avec different type, pas besoin pour l'instant
 * MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
 requestBody.add("message_id", "msgid");
 requestBody.add("message", "qwerty");
 requestBody.add("client_id", "111");
 requestBody.add("secret_key", "222");

 HttpEntity formEntity = new HttpEntity<MultiValueMap<String, String>>(requestBody, headers);

 Sur le serveur faudra recuperer MultiValueMap<String, String> requestBody et faire requestBody.get"a")
 */