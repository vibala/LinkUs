package pfe.ece.LinkUS.Controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pfe.ece.LinkUS.Exception.AlbumNotFoundException;
import pfe.ece.LinkUS.Model.Album;
import pfe.ece.LinkUS.Model.User;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.AlbumRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.FriendGroupRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.SubscriptionRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.UserRepository;
import pfe.ece.LinkUS.Service.AlbumService;
import pfe.ece.LinkUS.Service.FriendGroupService;
import pfe.ece.LinkUS.Service.UserService;

import java.util.ArrayList;
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

    private static final Logger LOGGER = Logger.getLogger(UserController.class);

    @RequestMapping("/")
    public String userDefaultCall() {
        return "Not implemented yet.";
    }

    @RequestMapping(params = {"id"})
    public String findUserById(@RequestParam("id") String id) {
        AlbumService albumService = new AlbumService(albumRepository);
        Album album = albumService.findAlbumById(id);


        return album.toString();
    }

    @RequestMapping(params = {"name"})
    public String findUserByName(@RequestParam("name") String name) {
        UserService userService = new UserService(userRepository);

        List<User> userList = userService.findUsersByName(name);

        if(userList == null || userList.isEmpty()) {
            throw  new AlbumNotFoundException(name);
        } else {
            return userList.toString();
        }
    }

    @RequestMapping(value = "/albumOwner", params = {"albumId"})
    public User findOwnerUserByAlbumId(@RequestParam("albumId") String albumId) {
        AlbumService albumService = new AlbumService(albumRepository);
        UserService userService = new UserService(userRepository);

        return userService.findUserById(albumService.getAlbumOwnerId(albumId));
    }

    @RequestMapping(params = {"albumId", "right"})
    public String findUserByAlbumId(@RequestParam(value = "albumId") String albumId,
                                    @RequestParam(value = "right") String right) {

        FriendGroupService friendGroupService = new FriendGroupService(friendGroupRepository);
        AlbumService albumService = new AlbumService(albumRepository);
        UserService userService = new UserService(userRepository);
        List<User> userList = new ArrayList<>();

        if(right == null ||"".equals(right)) {
            right = "lecture";
        }

        // Get userIds in album repo
        List<String> userIdList = albumService.findUserIdsByAlbum(albumId, right);
        // Get the groups in which the user is.
        List<String> groupIdList = albumService.findGroupIdsByAlbum(albumId, right);
        // Get the userIds of the groups
        userIdList.addAll(friendGroupService.findUserIdsByFriendGroup(groupIdList));


        // Search for users
        userList.addAll(userService.findUsersByIds(userIdList));

        if(userList == null || userList.isEmpty()) {
            throw  new AlbumNotFoundException(albumId);
        } else {
            return userList.toString();
        }
    }
}
