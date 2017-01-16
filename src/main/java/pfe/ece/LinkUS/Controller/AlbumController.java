package pfe.ece.LinkUS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pfe.ece.LinkUS.Exception.AlbumNotFoundException;
import pfe.ece.LinkUS.Exception.UserNotFoundException;
import pfe.ece.LinkUS.Model.*;
import pfe.ece.LinkUS.Model.Enum.Right;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.AlbumRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.FriendGroupRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.SubscriptionRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.UserRepository;
import pfe.ece.LinkUS.Service.AlbumService;
import pfe.ece.LinkUS.Service.FriendGroupService;
import pfe.ece.LinkUS.Service.TokenService.AccessTokenService;
import pfe.ece.LinkUS.Service.UserEntityService.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by DamnAug on 14/10/2016.
 */

@RestController
@RequestMapping("/album")
public class AlbumController {

    private Logger LOGGER = Logger.getLogger("LinkUS.Controller.AlbumController");
    @Autowired
    UserRepository usersRepository;
    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    FriendGroupRepository friendGroupRepository;
    @Autowired
    SubscriptionRepository subscriptionRepository;
    @Autowired
    private AccessTokenService accessTokenService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private AlbumService albumService;


    @RequestMapping("/")
    public String albumDefaultCall() {
        return "Not implemented yet.";
    }

    @RequestMapping(value = "/right", produces = "application/json")
    public String findAlbumByUserId(@RequestParam(value = "right") String right, @RequestParam(value = "news") boolean news) {

        FriendGroupService friendGroupService = new FriendGroupService(friendGroupRepository);
        AlbumService albumService = new AlbumService(albumRepository);
        albumService.setSubscriptionRepository(subscriptionRepository);
        List<Album> albumList = new ArrayList<>();

        if(right == null ||"".equals(right)) {
            right = Right.LECTURE.name();
        }
        // Get the current authentified user id
        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        //Get the groups in which the user is.
        List<FriendGroup> groupList = friendGroupService.findFriendGroupByUserId(userId);

        // Search for users
        albumList.addAll(albumService.findAlbumByUserIdRight(userId, right));
        // Search for group where the user is
        albumList.addAll(albumService.findAlbumByGroupIdRight(groupList, right));

        if(albumList == null || albumList.isEmpty()) {
            throw  new AlbumNotFoundException(userId);
        } else {
            return albumService.checkData(albumList, news, userId).toString();
        }
    }

    @RequestMapping(value = "/owned")
    public String findAlbumsOwnedByUser(@RequestParam(value = "news") boolean news) {
        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();
        AlbumService albumService = new AlbumService(albumRepository);
        albumService.setSubscriptionRepository(subscriptionRepository);
        return albumService.checkData(albumService.getAlbumsOwned(userId), news, userId).toString();
    }

    @RequestMapping(value = "/setwith", method = RequestMethod.POST)
    public void addFriendToAlbumWithSpecificRight(
            @RequestParam("friendId") String friendId,
            @RequestParam("albumId") String albumId,
            @RequestParam("right") String right){

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        AlbumService albumService = new AlbumService(albumRepository);
        albumService.addFriendToAlbum(userId, friendId, albumId, right);
    }
}
