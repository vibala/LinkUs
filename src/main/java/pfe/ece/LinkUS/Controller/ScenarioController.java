package pfe.ece.LinkUS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pfe.ece.LinkUS.Exception.EmailExistsException;
import pfe.ece.LinkUS.Model.Enum.Right;
import pfe.ece.LinkUS.Model.User;
import pfe.ece.LinkUS.Service.AlbumService;
import pfe.ece.LinkUS.Service.FriendGroupService;
import pfe.ece.LinkUS.Service.SubscriptionService;
import pfe.ece.LinkUS.Service.TokenService.AccessTokenService;
import pfe.ece.LinkUS.Service.UserService;

import java.io.IOException;
import java.net.Inet4Address;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DamnAug on 19/01/2017.
 */
@RestController
@RequestMapping(value = "/scenario")
public class ScenarioController {

    @Autowired
    private AccessTokenService accessTokenService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private UserService userService;
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private FriendGroupService friendGroupService;

    @RequestMapping(value = "/filledAlbum")
    public ResponseEntity createFilledAlbum() {

        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        // Creation album
        String albumId = albumService.createSaveAlbum(userId, "Trip to India");

        // Creation moments
        String momentIdTajMahl = albumService.createMomentSaveToAlbum(albumId, "Visite du palais TajMahl");
        String momentIdTajPerse = albumService.createMomentSaveToAlbum(albumId, "Visite du désert de Perse");

        // Creation instants
        albumService.createInstantSaveToAlbumMoment(albumId, momentIdTajMahl, "Couché de soleil");
        albumService.createInstantSaveToAlbumMoment(albumId, momentIdTajPerse, "Aube");

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity createUsers() throws EmailExistsException, IOException, ParseException {

        List<String> userList = new ArrayList<>();
        userList.add("UserA");
        userList.add("UserB");
        userList.add("UserC");
        userList.add("UserD");

        for(String name:userList) {
            String email = name + "@yopmail.com";

            if(userService.checkUserByEmail(email)) {
                User user = userService.findUserByEmail(email);
                // Supprimer subscriptions by user
                subscriptionService.deleteUserSubscriptions(user.getId());
                // Supprimer friendGroupOwned by user
                friendGroupService.deleteFriendGroupByOwnerId(user.getId());
                // Supprimer user
                userService.removeUser(user);
            }
            userService.createFakeUser(subscriptionService, name);
        }

        // USERS
        String idA = userService.findUserByEmail(userList.get(0)+"@yopmail.com").getId();
        String idB = userService.findUserByEmail(userList.get(1)+"@yopmail.com").getId();
        String idC = userService.findUserByEmail(userList.get(2)+"@yopmail.com").getId();
        String idD = userService.findUserByEmail(userList.get(3)+"@yopmail.com").getId();

        // FRIENDS
        userService.addFakeFriend(idA, idB);
        userService.addFakeFriend(idA, idC);
        userService.addFakeFriend(idA, idD);

        userService.addFakeFriend(idB, idA);
        userService.addFakeFriend(idB, idD);

        userService.addFakeFriend(idC, idA);

        userService.addFakeFriend(idD, idA);
        userService.addFakeFriend(idD, idB);

        String url = "http://" + Inet4Address.getLocalHost().getHostAddress() + ":9999/images?name=";
        // ALBUMS
        // USER 1
        // ALBUM 1
        String albumId = albumService.createSaveAlbum(idA, "Album 1");
        Path path = Paths.get("./images/" + albumId);
        //if directory exists?
        createDirectory(path);

        List<String> listFG = new ArrayList<String>();
        listFG.add(idA);
        listFG.add(idB);
        listFG.add(idC);
        albumService.addGroupFriendToAlbum(userService, friendGroupService, idA,
                friendGroupService.addFilledFriendGroup("FG IdA 1", idA, listFG),
                albumId, Right.LECTURE.name());
        listFG = new ArrayList<String>();
        listFG.add(idA);
        listFG.add(idC);
        albumService.addGroupFriendToAlbum(userService,friendGroupService, idA,
                friendGroupService.addFilledFriendGroup("FG IdA 2", idA, listFG),
                albumId, Right.LECTURE.name());
        listFG = new ArrayList<String>();
        listFG.add(idA);
        listFG.add(idB);
        albumService.addGroupFriendToAlbum(userService,friendGroupService, idA,
                friendGroupService.addFilledFriendGroup("FG IdA 3", idA, listFG),
                albumId, Right.LECTURE.name());
        String moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite du palais TajMahl");
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Couché de soleil", url+moment1+"_image"+1+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 1);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dizel", url+moment1+"_image"+2+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 2);

        moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite du désert de Perse1");
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Couché de solei12l", url+moment1+"_image"+1+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 1);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dize32l", url+moment1+"_image"+2+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 2);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Couché de sole42il", url+moment1+"_image"+3+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 3);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dizel77", url+moment1+"_image"+4+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 4);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Couché de 42soleil", url+moment1+"_image"+5+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 5);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Di86zel", url+moment1+"_image"+6+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 6);

        // Doit etre placé après l'ajout des isntants, sinon ca n'est pas comptabilisé
        albumService.addFriendToAlbumAndAllMomentInstants(userService, idA, idB, albumId, Right.LECTURE.name());
        albumService.addFriendToAlbumAndAllMomentInstants(userService, idA, idC, albumId, Right.LECTURE.name());

        // ALBUM 2
        albumId = albumService.createSaveAlbum(idA, "Album 2");
        path = Paths.get("./images/" + albumId);
        //if directory exists?
        createDirectory(path);

        moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite du azepalais TajMahl");
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Couché de solei12l", url+moment1+"_image"+1+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 1);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dize32l", url+moment1+"_image"+2+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 2);
        moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite du ztpalais TajMahl");
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Couché de solei12l", url+moment1+"_image"+3+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 3);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dize32l", url+moment1+"_image"+2+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 2);
        moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite du zrteypalais TajMahl");
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Couché de solei12l", url+moment1+"_image"+4+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 4);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dize32l", url+moment1+"_image"+5+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 5);
        moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite du fghpalais TajMahl");
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Couché de solei12l", url+moment1+"_image"+1+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 1);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dize32l", url+moment1+"_image"+6+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 6);
        moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite duqs palais TajMahl");
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Couché de solei12l", url+moment1+"_image"+5+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 5);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dize32l", url+moment1+"_image"+6+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 6);
        moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite du paytkjhtglais TajMahl");
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Couché de solei12l", url+moment1+"_image"+3+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 3);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dize32l", url+moment1+"_image"+6+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 6);


        // Doit etre placé après l'ajout des isntants, sinon ca n'est pas comptabilisé
        albumService.addFriendToAlbumAndAllMomentInstants(userService, idA, idB, albumId, Right.LECTURE.name());
        albumService.addFriendToAlbumAndAllMomentInstants(userService, idA, idC, albumId, Right.LECTURE.name());

        // USER 2
        // ALBUM 1
        albumId = albumService.createSaveAlbum(idB, "Album 1");
        path = Paths.get("./images/" + albumId);
        //if directory exists?
        createDirectory(path);
        moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite du palais TajMahl");
        listFG = new ArrayList<String>();
        listFG.add(idA);
        listFG.add(idB);
        listFG.add(idD);
        albumService.addGroupFriendToAlbum(userService,friendGroupService, idB,
                friendGroupService.addFilledFriendGroup("FG IdB 1", idB, listFG),
                albumId, Right.LECTURE.name());
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Couché de soleil", url+moment1+"_image"+2+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 2);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dizerzel", url+moment1+"_image"+4+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 4);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Cougrhtjché de soleil", url+moment1+"_image"+5+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 5);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Coussdché de soleil", url+moment1+"_image"+6+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 6);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dizegggl", url+moment1+"_image"+1+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 1);

        moment1 = albumService.createMomentSaveToAlbum(albumId, "Laggoon");
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Couché de soleil", url+moment1+"_image"+3+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 3);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dizerzel", url+moment1+"_image"+4+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 4);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Cougrhtjché de soleil", url+moment1+"_image"+1+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 1);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Coussdché de soleil", url+moment1+"_image"+6+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 6);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dizegggl", url+moment1+"_image"+2+".jpg&albumId="+albumId);
        albumService.saveFakePhoto(albumId, moment1, 2);

        return new ResponseEntity(HttpStatus.OK);
    }

    public void createDirectory(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                System.out.println("Directory is created!");
            } catch (IOException e) {
                //fail to create directory

                System.out.println("Failed to create directory!");
                e.printStackTrace();

            }
        }
    }
}