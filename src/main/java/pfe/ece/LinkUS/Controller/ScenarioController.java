package pfe.ece.LinkUS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pfe.ece.LinkUS.Config.ProperPasswordEncoder;
import pfe.ece.LinkUS.Exception.EmailExistsException;
import pfe.ece.LinkUS.Model.Enum.Role;
import pfe.ece.LinkUS.Model.User;
import pfe.ece.LinkUS.Model.UserCreateForm;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.AlbumRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.UserRepository;
import pfe.ece.LinkUS.Service.AlbumService;
import pfe.ece.LinkUS.Service.CurrentUserService.CurrentUserDetailsService;
import pfe.ece.LinkUS.Service.TokenService.AccessTokenService;
import pfe.ece.LinkUS.Service.UserEntityService.UserServiceImpl;
import pfe.ece.LinkUS.Service.UserService;

import java.util.Date;

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
    public ResponseEntity createUsers() throws EmailExistsException {

        AlbumService albumService = new AlbumService(albumRepository);
        UserService userService = new UserService(userRepository);


        // USERS

        String idA = userService.createFakeUser("UserA");
        String idB = userService.createFakeUser("UserB");
        String idC = userService.createFakeUser("UserC");
        String idD = userService.createFakeUser("UserD");

        userService.addFakeFriend(idA, idB);
        userService.addFakeFriend(idA, idC);
        userService.addFakeFriend(idA, idD);

        userService.addFakeFriend(idB, idA);
        userService.addFakeFriend(idB, idD);

        userService.addFakeFriend(idC, idA);

        userService.addFakeFriend(idD, idA);
        userService.addFakeFriend(idD, idB);


        // ALBUMS
        // USER 1
        // ALBUM 1
        String albumId = albumService.createSaveAlbum(idA, "Album 1");
        String moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite du palais TajMahl");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Couché de soleil");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Dizel");

        String moment2 = albumService.createMomentSaveToAlbum(albumId, "Visite du désert de Perse1");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Couché de solei12l");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Dize32l");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Couché de sole42il");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Dizel77");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Couché de 42soleil");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Di86zel");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Couc78hé de soleil");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Diz8756el");

        // ALBUM 2
        albumId = albumService.createSaveAlbum(idA, "Album 2");
        moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite du azepalais TajMahl");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Couché de solei12l");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Dize32l");
        moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite du ztpalais TajMahl");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Couché de solei12l");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Dize32l");
        moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite du zrteypalais TajMahl");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Couché de solei12l");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Dize32l");
        moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite du fghpalais TajMahl");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Couché de solei12l");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Dize32l");
        moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite duqs palais TajMahl");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Couché de solei12l");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Dize32l");
        moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite du paytkjhtglais TajMahl");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Couché de solei12l");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Dize32l");

        // USER 2
        // ALBUM 1
        albumId = albumService.createSaveAlbum(idB, "Album 1");
        moment1 = albumService.createMomentSaveToAlbum(albumId, "Visite du palais TajMahl");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Couché de soleil");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Dizerzel");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Cougrhtjché de soleil");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Coussdché de soleil");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Dizegggl");

        moment1 = albumService.createMomentSaveToAlbum(albumId, "Laggoon");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Couché de soleil");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Dizerzel");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Cougrhtjché de soleil");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Coussdché de soleil");
        albumService.createInstantSaveToAlbumMoment(albumId, moment1, "Dizegggl");


        return new ResponseEntity(HttpStatus.OK);
    }
}
