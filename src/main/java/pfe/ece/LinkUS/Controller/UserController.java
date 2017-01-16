package pfe.ece.LinkUS.Controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pfe.ece.LinkUS.Exception.UnauthorizedInformationException;
import pfe.ece.LinkUS.Model.User;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.AlbumRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.FriendGroupRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.SubscriptionRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.UserRepository;
import pfe.ece.LinkUS.Service.TokenService.AccessTokenService;
import pfe.ece.LinkUS.Service.UserService;

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
//        List<User> userList = userService.findUsersByName(name);
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
    public void friendRequest(@RequestParam(value = "friendId") String friendId) {

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        UserService userService = new UserService(userRepository);
        userService.friendRequest(userId, friendId);
    }

    /**
     * Accept or decline fiend request
     *
     * @param friendId
     * @param decision
     */
    @RequestMapping(value = "/friend", params = {"friendId", "decision"}, method = RequestMethod.POST)
    public void friendRequestDecision(@RequestParam(value = "friendId") String friendId, @RequestParam(value = "decision") Boolean decision) {

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        UserService userService = new UserService(userRepository);
        if(decision) {
            userService.acceptFriend(userId, friendId);
        } else {
            userService.refuseFriend(userId, friendId);
        }
    }

    @RequestMapping("/getFriends")
    public String getFriends(){
        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();
        UserService userService = new UserService(userRepository);
        return userService.findFriends(userId).toString();
    }

    @RequestMapping("/getGroupFriends")
    public String getGroupFriends(){
        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();
        UserService userService = new UserService(userRepository);
        userService.setFriendGroupRepository(friendGroupRepository);

        return userService.findFriendGroups(userId).toString();
    }

    @RequestMapping(value = "/getFriend", params = {"friendId"})
    public String getFriend(@RequestParam("friendId") String friendId){

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();
        UserService userService = new UserService(userRepository);
        User user = userService.findUserById(userId);

        if (user.getFriendList().contains(friendId)) {
            return userService.findUserById(friendId).toString();
        } else {
            throw new UnauthorizedInformationException();
        }
    }

    @RequestMapping(value = "/searchFriend", params = {"text"})
    public String searchFriend(@RequestParam("text") String text){

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        UserService userService = new UserService(userRepository);
        return userService.searchUserByPartialFirstnameOrLastname(text).toString();
    }

}
