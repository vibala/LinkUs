package pfe.ece.LinkUS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfe.ece.LinkUS.Exception.AlbumNotFoundException;
import pfe.ece.LinkUS.Model.Album;
import pfe.ece.LinkUS.Model.Enum.Right;
import pfe.ece.LinkUS.Model.FriendGroup;
import pfe.ece.LinkUS.Model.PreviewAlbum;
import pfe.ece.LinkUS.Service.*;
import pfe.ece.LinkUS.Service.TokenService.AccessTokenService;

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
    private FriendGroupService friendGroupService;
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private AccessTokenService accessTokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private IdRightService idRightService;


    @RequestMapping(value= "/update", method= RequestMethod.POST)
    public ResponseEntity updateAlbum(@RequestBody Album album) {

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();
        if(idRightService.checkUserInIdRight(idRightService.findByRight(album, Right.ADMIN.name()), userId)) {
            albumService.checkUpdateAlbum(album);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value= "/create", method= RequestMethod.POST)
    public ResponseEntity createAlbum(@RequestBody Album album) {

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();
        System.out.println("UserId " + userId );
        album.setOwnerId(userId);
        albumService.setMainImageUrlToAlbumAndMoments(album,true);
        if(idRightService.checkUserInIdRight(idRightService.findByRight(album, Right.ADMIN.name()), userId)) {
            albumService.checkSaveAlbum(album);
            System.out.println("okok");
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/right", produces = "application/json")
    public String findAlbumsByUserId(@RequestParam(value = "right") String right, @RequestParam(value = "news") boolean news) {

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
            return albumService.checkData(subscriptionService, albumList, news, userId).toString();
        }
    }

    @RequestMapping(value = "/preview", produces = "application/json", method= RequestMethod.GET)
    public String findPreviewAlbumsByUserId(@RequestParam(value = "right") String right, @RequestParam(value = "news") boolean news) {

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

        List<PreviewAlbum> previewAlbumList = albumService.setPreviewAlbums(albumList);

        return previewAlbumList.toString();
    }


    @RequestMapping(value = "/owned")
    public String findAlbumsOwnedByUser(@RequestParam(value = "news") boolean news) {
        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();
        return albumService.checkData(subscriptionService, albumService.getAlbumsOwned(userId), news, userId).toString();
    }

    @RequestMapping(value = "/setwith",
            params = {"friendId", "albumId", "right"},
            method = RequestMethod.POST)
    public ResponseEntity addFriendToAlbumWithSpecificRight(
            @RequestParam("friendId") String friendId,
            @RequestParam("albumId") String albumId,
            @RequestParam("right") String right){

//        friendId = friendId.replace("\"","");
//        albumId = albumId.replace("\"","");
//        right = right.replace("\"","");

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        if(albumService.addFriendToAlbum(userService, userId, friendId, albumId, right)) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/shareWithFriendGroup",
            params = {"friendGroupId", "albumId", "right"},
            method = RequestMethod.POST)
    public ResponseEntity addFriendGroupToAlbumWithSpecificRight(
            @RequestParam("friendGroupId") String friendGroupId,
            @RequestParam("albumId") String albumId,
            @RequestParam("right") String right){

//        friendGroup = friendGroup.replace("\"","");
//        albumId = albumId.replace("\"","");
//        right = right.replace("\"","");

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        if(albumService.addGroupFriendToAlbum(userService, friendGroupService, userId, friendGroupId, albumId, right)) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/searchAlbum", params = {"albumId", "news"},method = RequestMethod.GET)
    public String getAlbumById(@RequestParam(value = "albumId") String albumId,
                               @RequestParam(value = "news") boolean news){

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();
        Album album = albumService.findAlbumById(albumId);

        // Ajout de l'album à une liste pour pouvoir utiliser la méthode de vérification des données
        List<Album> albumList = new ArrayList<>();
        albumList.add(album);

        return albumService.checkData(subscriptionService, albumList, news, userId).get(0).toString();
    }
    @RequestMapping(value = "/momentUrls", params = {"albumId", "momentId"})
    public String getUrlsFromMoment(@RequestParam(value = "albumId") String albumId,
                                    @RequestParam(value = "momentId") String momentId){

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        return albumService.getUrlsFromMoment(userId, albumId, momentId).toString();
    }

    @RequestMapping(value = "/findMoments", params = {"albumId"})
    public String findMomentsInAlbum(@RequestParam("albumId") String albumId, @RequestBody List<String> momentIdList) {

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();
        return albumService.findMomentsCheckRightInAlbum(albumId, userId, momentIdList).toString();
    }
}