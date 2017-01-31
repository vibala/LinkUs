package pfe.ece.LinkUS.Controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfe.ece.LinkUS.Model.*;
import pfe.ece.LinkUS.Model.Enum.NotificationType;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.AlbumRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.FriendGroupRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.SubscriptionRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.UserRepository;
import pfe.ece.LinkUS.Repository.TokenMySQLRepo.NotificationTokenRepository;
import pfe.ece.LinkUS.ServerService.NotificationServerService;
import pfe.ece.LinkUS.Service.AlbumService;
import pfe.ece.LinkUS.Service.FriendGroupService;
import pfe.ece.LinkUS.Service.NotificationService;
import pfe.ece.LinkUS.Service.TokenService.AccessTokenService;
import pfe.ece.LinkUS.Service.UserService;

import java.io.IOException;
import java.util.List;

/**
 * Created by DamnAug on 12/10/2016.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    AlbumService albumService;
    @Autowired
    FriendGroupRepository friendGroupRepository;
    @Autowired
    SubscriptionRepository subscriptionRepository;
    @Autowired
    AccessTokenService accessTokenService;

    @Autowired
    NotificationTokenRepository notificationTokenRepository;

    @Autowired
    NotificationService notificationService;

    private static final Logger LOGGER = Logger.getLogger(UserController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getMyProfile() {
        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        User user = userService.findUserById(userId);

        userService.checkData(user);

        return user.toString();
    }


//    @RequestMapping(params = {"name"})
//    public String findUserByName(@RequestParam("name") String name) {
//
//
//        List<User> userList = userService.findUsersByLastName(name);
//
//        if(userList == null || userList.isEmpty()) {
//            throw new AlbumNotFoundException(name);
//        } else {
//            return userList.toString();
//        }
//    }
//
//    @RequestMapping(value = "/albumOwner", params = {"albumId"})
//    public User findOwnerUserByAlbumId(@RequestParam("albumId") String albumId) {
//
//
//        return userService.findUserById(albumService.getAlbumOwnerId(albumId));
//    }
//
//    @RequestMapping(params = {"albumId", "right"})
//    public String findUserByAlbumId(@RequestParam(value = "albumId") String albumId,
//                                    @RequestParam(value = "right") String right) {
//
//        FriendGroupService friendGroupService = new FriendGroupService(friendGroupRepository);
//
//        List<User> userList = new ArrayList<>();
//
//        if(right == null ||"".equals(right)) {
//            right = Right.LECTURE.name();
//        }
//
//        // Get userIds in album repo
//        List<String> userIdList = albumService.findUserIdsByAlbum(albumId, right);
//        // Get the groups in which the user is.
//        List<String> groupIdList = albumService.findGroupIdsByAlbum(albumId, right);
//        // Get the userIds of the groups
//        userIdList.addAll(friendGroupService.findUserIdsByFriendGroup(groupIdList));
//
//
//        // Search for users
//        userList.addAll(userService.findUsersByIds(userIdList));
//
//        if(userList == null || userList.isEmpty()) {
//            throw  new AlbumNotFoundException(albumId);
//        } else {
//            return userList.toString();
//        }
//    }

    /**
     * Create friend request
     *
     * @param friendId
     */
    @RequestMapping(value = "/friendRequest", method = RequestMethod.POST)
    public ResponseEntity friendRequest(@RequestBody String friendId) {
        friendId = friendId.replace("\"","");
        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        if(userService.friendRequest(userId, friendId)) {
            NotificationServerService notificationServerService = new NotificationServerService(userService,notificationTokenRepository);

            User friend = userService.findUserById(friendId);
            User fromUser = userService.findUserById(userId);
            NotificationFriendRequest notificationFriendRequest = notificationService.createSaveNotificationFriendRequest(friend,fromUser, NotificationType.FRIEND_REQUEST);
            //On demande a FireBase d envoyer une notificationMoment a ces personnes (FireBase va utiliser les Token pour envoyer la notif car chaque token correspond a une appli installé sur un device.)
            try {
                notificationServerService.sendNotificationFriendRequest(notificationFriendRequest,notificationService.getTokenByUserId(friendId));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.CONFLICT);
    }

    /**
     * Accept or decline fiend request
     *
     * @param friendId
     * @param decision
     */
    @RequestMapping(value = "/friendRequestDecision", params = {"decision"}, method = RequestMethod.POST)
    public ResponseEntity friendRequestDecision(@RequestBody String friendId,  @RequestParam("decision") boolean decision) {
        friendId = friendId.replace("\"","");

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        if(decision) {
            if(userService.acceptFriend(userId, friendId)) {
                return new ResponseEntity(HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.CONFLICT);
            }
        } else {
            if(userService.refuseFriend(userId, friendId)){
                return new ResponseEntity(HttpStatus.OK);
            }
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/removeFriend", method = RequestMethod.POST)
    public ResponseEntity removeFriend(@RequestBody String friendId) {
        friendId = friendId.replace("\"","");

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        if(userService.removeFriend(userId, friendId)) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @RequestMapping("/getFriends")
    public String getFriends(){
        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();
        List<User> userList = userService.findFriends(userId);
        userService.checkData(userList);
        return userList.toString();
    }

    @RequestMapping("/getGroupFriends")
    public String getGroupFriends(){
        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        return userService.findFriendGroupsOwned(friendGroupRepository, userId).toString();
    }

    @RequestMapping(value = "/getFriend", params = {"friendId"})
    public String getFriend(@RequestParam("friendId") String friendId){

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        User friend = userService.findFriend(userId, friendId);
        userService.checkData(friend);

        return friend.toString();
    }

    @RequestMapping(value = "/searchUser", params = {"text"})
    public String searchUser(@RequestParam("text") String text){

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        List<User> userList = userService.searchUserByPartialFirstnameOrLastname(userId, text);
        userService.checkData(userList);
        return userList.toString();
    }

    @RequestMapping("/getPendingFriends")
    public String getPendingFriends(){
        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();
        List<User> userList = userService.findPendingFriends(userId);
        userService.checkData(userList);
        return userList.toString();
    }

    @RequestMapping("/getRequestPendingFriends")
    public String getRequestPendingFriends(){
        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();
        List<User> userList = userService.findRequestPendingFriends(userId);
        userService.checkData(userList);
        return userList.toString();
    }

    @RequestMapping("/getProchesAndAlbums")
    public String getProchesAndAlbums() {
        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        User user = userService.findUserById(userId);

        return new NbAlbumsAndNbProches(user.getFriendList().size(), albumService.getAlbumsOwned(userId).size()).toString();
    }

    @RequestMapping(value = "/changeFullname", params = {"lastName","firstName"}, method = RequestMethod.POST)
    public ResponseEntity<String> changeFullName(@RequestParam("lastName") String lastName, @RequestParam("firstName") String firstName){
        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        if(!userService.modifyUserLastNameAndFirstName(userId, firstName, lastName)) {
            return new ResponseEntity<String>("Full name not updated", HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<String>("Full name updated", HttpStatus.OK);
    }

    @RequestMapping(value = "/changeUsername", params = {"email"}, method = RequestMethod.POST)
    public ResponseEntity<String> changeEmailUsername(@RequestParam("email") String email){
        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();


        if(!userService.modifyUserEmail(userId, email)){
            return new ResponseEntity<String>("Username name not updated", HttpStatus.NOT_MODIFIED);
        }

        return new ResponseEntity<String>("Username updated", HttpStatus.OK);
    }


}
