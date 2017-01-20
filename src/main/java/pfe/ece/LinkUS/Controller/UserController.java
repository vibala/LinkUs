package pfe.ece.LinkUS.Controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfe.ece.LinkUS.Exception.UnauthorizedInformationException;
import pfe.ece.LinkUS.Model.User;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.AlbumRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.FriendGroupRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.SubscriptionRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.UserRepository;
import pfe.ece.LinkUS.Service.TokenService.AccessTokenService;
import pfe.ece.LinkUS.Service.UserService;

import java.util.List;

/**
 * Created by DamnAug on 12/10/2016.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    FriendGroupRepository friendGroupRepository;
    @Autowired
    SubscriptionRepository subscriptionRepository;
    @Autowired
    private AccessTokenService accessTokenService;

    private static final Logger LOGGER = Logger.getLogger(UserController.class);

    @RequestMapping("/")
    public String getMyProfile() {
        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        UserService userService = new UserService(userRepository);
        User user = userService.findUserById(userId);

        userService.checkData(user);

        return user.toString();
    }

//    @RequestMapping(params = {"name"})
//    public String findUserByName(@RequestParam("name") String name) {
//        UserService userService = new UserService(userRepository);
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
//        AlbumService albumService = new AlbumService(albumRepository);
//        UserService userService = new UserService(userRepository);
//
//        return userService.findUserById(albumService.getAlbumOwnerId(albumId));
//    }
//
//    @RequestMapping(params = {"albumId", "right"})
//    public String findUserByAlbumId(@RequestParam(value = "albumId") String albumId,
//                                    @RequestParam(value = "right") String right) {
//
//        FriendGroupService friendGroupService = new FriendGroupService(friendGroupRepository);
//        AlbumService albumService = new AlbumService(albumRepository);
//        UserService userService = new UserService(userRepository);
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
    @RequestMapping(value = "/friendRequest", params = {"friendId"}, method = RequestMethod.POST)
    public ResponseEntity friendRequest(@RequestBody String friendId) {

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        UserService userService = new UserService(userRepository);
        if(userService.friendRequest(userId, friendId)) {
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
    @RequestMapping(value = "/friendRequestDecision", params = {"friendId", "decision"}, method = RequestMethod.POST)
    public ResponseEntity friendRequestDecision(@RequestBody String friendId,  Boolean decision) {

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        UserService userService = new UserService(userRepository);
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

    @RequestMapping(value = "/removeFriend", params = {"friendId"}, method = RequestMethod.POST)
    public ResponseEntity friendRequestDecision(@RequestParam(value = "friendId") String friendId) {

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        UserService userService = new UserService(userRepository);
        if(userService.removeFriend(userId, friendId)) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @RequestMapping("/getFriends")
    public String getFriends(){
        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();
        UserService userService = new UserService(userRepository);
        List<User> userList = userService.findFriends(userId);
        userService.checkData(userList);
        return userList.toString();
    }

    @RequestMapping("/getGroupFriends")
    public String getGroupFriends(){
        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        UserService userService = new UserService(userRepository);
        return userService.findFriendGroupsOwned(friendGroupRepository, userId).toString();
    }

    @RequestMapping(value = "/getFriend", params = {"friendId"})
    public String getFriend(@RequestParam("friendId") String friendId){

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();
        UserService userService = new UserService(userRepository);

        User friend = userService.findFriend(userId, friendId);
        userService.checkData(friend);

        return friend.toString();
    }

    @RequestMapping(value = "/searchFriend", params = {"text"})
    public String searchFriend(@RequestParam("text") String text){

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        UserService userService = new UserService(userRepository);
        List<User> userList = userService.searchUserByPartialFirstnameOrLastname(text);
        userService.checkData(userList);
        return userList.toString();
    }

}
