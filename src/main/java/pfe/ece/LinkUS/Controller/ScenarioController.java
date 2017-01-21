package pfe.ece.LinkUS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pfe.ece.LinkUS.Exception.EmailExistsException;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.AlbumRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.UserRepository;
import pfe.ece.LinkUS.Service.AlbumService;
import pfe.ece.LinkUS.Service.TokenService.AccessTokenService;
import pfe.ece.LinkUS.Service.UserService;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DamnAug on 19/01/2017.
 */
@Controller
@RequestMapping(value = "/scenario")
public class ScenarioController {

    @Autowired
    private AccessTokenService accessTokenService;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/filledAlbum")
    public ResponseEntity createFilledAlbum() {

        AlbumService albumService = new AlbumService(albumRepository);

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

    @RequestMapping(value = "/users")
    public ResponseEntity createUsers() throws EmailExistsException, IOException, ParseException {

        AlbumService albumService = new AlbumService(albumRepository);
        UserService userService = new UserService(userRepository);

        List<String> userList = new ArrayList<>();
        userList.add("UserA");
        userList.add("UserB");
        userList.add("UserC");
        userList.add("UserD");

        for(String name:userList) {
            userService.removeFakeUser(name);
            userService.createFakeUser(name);
        }


        // USERS
        String idA = userService.findUserByEmail(userList.get(0)+"@yopmail.com").getId();
        String idB = userService.findUserByEmail(userList.get(1)+"@yopmail.com").getId();
        String idC = userService.findUserByEmail(userList.get(2)+"@yopmail.com").getId();
        String idD = userService.findUserByEmail(userList.get(3)+"@yopmail.com").getId();

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
        File directory = new File("./images/" + idA + "/" + albumId);
        if (!directory.exists()) {
            directory.mkdir();
        }
        String moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite du palais TajMahl");
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Couché de soleil", url+moment1+"_photo"+1+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idA, albumId, moment1, 1);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dizel", url+moment1+"_photo"+2+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idA, albumId, moment1, 2);

        moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite du désert de Perse1");
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Couché de solei12l", url+moment1+"_photo"+1+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idA, albumId, moment1, 1);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dize32l", url+moment1+"_photo"+2+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idA, albumId, moment1, 2);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Couché de sole42il", url+moment1+"_photo"+3+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idA, albumId, moment1, 3);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dizel77", url+moment1+"_photo"+4+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idA, albumId, moment1, 4);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Couché de 42soleil", url+moment1+"_photo"+5+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idA, albumId, moment1, 5);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Di86zel", url+moment1+"_photo"+6+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idA, albumId, moment1, 6);

        // ALBUM 2
        albumId = albumService.createSaveAlbum(idA, "Album 2");
        directory = new File("./images/" + idA + "/" + albumId);
        if (!directory.exists()) {
            directory.mkdir();
        }
        moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite du azepalais TajMahl");
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Couché de solei12l", url+moment1+"_photo"+1+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idA, albumId, moment1, 1);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dize32l", url+moment1+"_photo"+2+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idA, albumId, moment1, 2);
        moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite du ztpalais TajMahl");
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Couché de solei12l", url+moment1+"_photo"+3+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idA, albumId, moment1, 3);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dize32l", url+moment1+"_photo"+2+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idA, albumId, moment1, 2);
        moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite du zrteypalais TajMahl");
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Couché de solei12l", url+moment1+"_photo"+4+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idA, albumId, moment1, 4);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dize32l", url+moment1+"_photo"+5+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idA, albumId, moment1, 5);
        moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite du fghpalais TajMahl");
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Couché de solei12l", url+moment1+"_photo"+1+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idA, albumId, moment1, 1);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dize32l", url+moment1+"_photo"+6+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idA, albumId, moment1, 6);
        moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite duqs palais TajMahl");
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Couché de solei12l", url+moment1+"_photo"+5+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idA, albumId, moment1, 5);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dize32l", url+moment1+"_photo"+6+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idA, albumId, moment1, 6);
        moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite du paytkjhtglais TajMahl");
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Couché de solei12l", url+moment1+"_photo"+3+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idA, albumId, moment1, 3);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dize32l", url+moment1+"_photo"+6+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idA, albumId, moment1, 6);

        // USER 2
        // ALBUM 1
        albumId = albumService.createSaveAlbum(idB, "Album 1");
        directory = new File("./images/" + idB + "/" + albumId);
        if (!directory.exists()) {
            directory.mkdir();
        }
        moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite du palais TajMahl");
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Couché de soleil", url+moment1+"_photo"+2+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idB, albumId, moment1, 2);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dizerzel", url+moment1+"_photo"+4+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idB, albumId, moment1, 4);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Cougrhtjché de soleil", url+moment1+"_photo"+5+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idB, albumId, moment1, 5);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Coussdché de soleil", url+moment1+"_photo"+6+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idB, albumId, moment1, 6);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dizegggl", url+moment1+"_photo"+1+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idB, albumId, moment1, 1);

        moment1 = albumService.createMomentSaveToAlbum(albumId, "Laggoon");
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Couché de soleil", url+moment1+"_photo"+3+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idB, albumId, moment1, 3);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dizerzel", url+moment1+"_photo"+4+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idB, albumId, moment1, 4);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Cougrhtjché de soleil", url+moment1+"_photo"+1+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idB, albumId, moment1, 1);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Coussdché de soleil", url+moment1+"_photo"+6+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idB, albumId, moment1, 6);
        albumService.createInstantPhotoSaveToAlbumMoment(albumId, moment1, "Dizegggl", url+moment1+"_photo"+2+".jpeg&albumId="+albumId);
        albumService.saveFakePhoto(idB, albumId, moment1, 2);


        return new ResponseEntity(HttpStatus.OK);
    }
}
