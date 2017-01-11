package pfe.ece.LinkUS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pfe.ece.LinkUS.Model.*;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.AlbumRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.FriendGroupRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.SubscriptionRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.UserRepository;
import pfe.ece.LinkUS.Service.AlbumService;
import pfe.ece.LinkUS.Service.FriendGroupService;
import pfe.ece.LinkUS.Service.SubscriptionService;
import pfe.ece.LinkUS.Service.TokenService.AccessTokenService;
import pfe.ece.LinkUS.Service.UserEntityService.UserServiceImpl;

import java.util.ArrayList;
import java.util.Date;
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
    private TokenStore tokenStore;
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

    @RequestMapping(value = "/right",produces = "application/json")
    public String findAlbumByUserId(@RequestParam(value = "right") String right) {

        FriendGroupService friendGroupService = new FriendGroupService(friendGroupRepository);
        AlbumService albumService = new AlbumService(albumRepository);
        List<Album> albumList = new ArrayList<>();

        if(right == null ||"".equals(right)) {
            right = Right.LECTURE.name();
        }
        // Get the current authentified user id
        String userId = gettingMyUserId();

        //Get the groups in which the user is.
        List<FriendGroup> groupList = friendGroupService.getFriendGroupById(userId);

        // Search for users
        albumList.addAll(albumService.findAlbumByUserIdRight(userId, right));
        // Search for group where the user is
        albumList.addAll(albumService.findAlbumByGroupIdRight(groupList, right));

        if(albumList == null || albumList.isEmpty()) {
            //throw  new AlbumNotFoundException(userId);
            return "ablumnotfoundexception";
        } else {
            System.out.println("Aablum to string " + albumList.toString());
            return albumList.toString();
            // TODO Building return checkDataAutorization(albumList, userId).toString();
        }

    }

    private String gettingMyUserId(){
       try{
            String user_id = accessTokenService.getUserIdOftheAuthentifiedUser();
            return user_id;
       }catch(UsernameNotFoundException e){
            return "UserId is not found because username is not found";
       }
    }


    @RequestMapping(value = "/owned")
    public String findAlbumsOwnedByUser() {
        String userId = gettingMyUserId();
        AlbumService albumService = new AlbumService(albumRepository);
        return albumService.getAlbumsOwned(userId).toString();
    }

    @RequestMapping(value = "/setwith", method = RequestMethod.POST)
    public void addFriendToAlbumWithSpecificRight(
            @RequestParam("friendId") String friendId,
            @RequestParam("albumId") String albumId,
            @RequestParam("right") String right){

        String userId = gettingMyUserId();

        AlbumService albumService = new AlbumService(albumRepository);
        albumService.addFriendToAlbum(userId, friendId, albumId, right);
    }

    private List<Album> checkDataAutorization(List<Album> albumList, String userId) {

        SubscriptionService subscriptionService = new SubscriptionService(subscriptionRepository);
        Subscription subscription = subscriptionService.findSubscription(userId);


        if (subscription.getDateFin().compareTo(new Date()) < 0 ||
                subscription.getDateDebut().compareTo(new Date()) > 0) {
            //Pas d'abo valid

            if(subscription.getDateFin().compareTo(new Date()) < 0) {
                LOGGER.info("User id " + subscription.getUserId() +
                        " subscription expired ("+subscription.getDateFin().toString()+")");
            }
            if(subscription.getDateDebut().compareTo(new Date()) > 0) {
                LOGGER.severe("User id " + subscription.getUserId() +
                        " subscription is not valid yet.("+subscription.getDateDebut().toString()+")");
            }

            // gerer le cas des descriptions free
            if (subscription.getFree() > 0) {
                // Decrement
                subscription.setDescriptionFree(subscription.getFree() - 1);
                LOGGER.info("User id " + subscription.getUserId() +
                        " has "+ subscription.getFree() +" free description left");
                // Save the object to update his value
               // subscriptionService.update(subscription);

            } else {
                LOGGER.info("User id " + subscription.getUserId() + " has no free description");
                removePhotosDescriptionToAlbums(albumList);
            }
        } else {
            LOGGER.info("User id " + subscription.getUserId() + " subscription is valid");
        }
        return albumList;
    }

    private void removePhotosDescriptionToAlbums(List<Album> albumList) {
        for (Album album: albumList) {
            for(Moment moment: album.getMoments()) {
                for(Instant instant: moment.getInstantList()) {
                    instant.setDescriptionsList(null);
                }
            }
        }
    }
}
