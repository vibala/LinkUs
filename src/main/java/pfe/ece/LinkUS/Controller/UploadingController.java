package pfe.ece.LinkUS.Controller;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pfe.ece.LinkUS.Model.*;
import pfe.ece.LinkUS.Model.Enum.Right;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.AlbumRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.NotificationRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.UserRepository;
import pfe.ece.LinkUS.Repository.TokenMySQLRepo.NotificationTokenRepository;
import pfe.ece.LinkUS.ServerService.NotificationServerService;
import pfe.ece.LinkUS.Service.AlbumService;
import pfe.ece.LinkUS.Service.InstantService;
import pfe.ece.LinkUS.Service.NotificationService;
import pfe.ece.LinkUS.Service.TokenService.AccessTokenService;
import pfe.ece.LinkUS.Service.UserService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Huong on 08/12/2016.
 */

@Controller
public class UploadingController {

    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    NotificationTokenRepository notificationTokenRepository;
    @Autowired
    AccessTokenService accessTokenService;
    @Autowired
    AlbumService albumService;
    @Autowired
    NotificationRepository notificationRepository;

    @RequestMapping(value = "/uploadFiles", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> uploadingFiles(@RequestBody Moment moment,
                                                 @RequestParam("albumId") String albumId,
                                                 @RequestParam("notificationToPeopleWithReadRightOnAlbum") String notificationToPeopleWithReadRightOnAlbum) throws IOException {

        Logger LOGGER = Logger.getLogger("LinkUS.Controller.AlbumController");

        AlbumService albumService = new AlbumService(albumRepository);
        FileOutputStream fos;

        // On fetche grâce au access token service
        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        // On fetche les albums dont l'utilisateur a les droits WRITE
        List<Album> albumsOfByCurrentAuthentifiedUser = albumService.findAlbumByUserIdRight(userId, Right.WRITE.name());

        // Verify the right of the user to upload photos on this albumId
        if(!albumService.checkAlbumIdInAlbums(albumId, albumsOfByCurrentAuthentifiedUser)) {
            return new ResponseEntity<String>(
                    "You have no right to upload photos on that album: " + albumId, HttpStatus.FORBIDDEN);
        }


        /** ----------------------UPLOAD IMAGE----------*/
        //PARTIE AMAZON

        /*AmazonS3Service amazonService = new AmazonS3Service();

        //On genere un nom unique du fichier dans AmazonS3
        String fileS3Name = amazonService.generatefileS3Name(instant.getName());

        //On upload l'image dans AmazonS3
        amazonService.uploadFileByte(instant.getImgByte(),fileS3Name,"image*//*");*/

        // PARTIE LOCALE
     /*   File directory = new File("./images");
        if (!directory.exists()) {
            directory.mkdir();
        }
        directory = new File("./images/" + userId);
        if (!directory.exists()) {
            directory.mkdir();
        }
        directory = new File("./images/" + userId + "/" + albumId);
        if (!directory.exists()) {
            directory.mkdir();
        }*/
        // PARTIE LOCALE
        Path path = Paths.get("./images");
        if (!Files.exists(path)) { try {Files.createDirectories(path); System.out.println("Directory is created!"); } catch (IOException e) {  System.out.println("Failed to create directory!");e.printStackTrace();}}
        path = Paths.get("./images/" + userId);
        if (!Files.exists(path)) { try {Files.createDirectories(path); System.out.println("Directory is created!"); } catch (IOException e) {  System.out.println("Failed to create directory!");e.printStackTrace();}}
        path = Paths.get("./images/" + userId + "/" + albumId);
        if (!Files.exists(path)) { try {Files.createDirectories(path); System.out.println("Directory is created!"); } catch (IOException e) {  System.out.println("Failed to create directory!");e.printStackTrace();}}


        for (Instant instant: moment.getInstantList()) {

            //generer un nom unique (hypothese que la combinaison suivante est unique dans le dossier userId/albumId

            String fileS3Name = new Date().getTime()+ RandomStringUtils.randomAlphanumeric(10)+instant.getName();

            fos = new FileOutputStream("./images/" + userId + "/" + albumId + "/" + fileS3Name);
            fos.write(instant.getImgByte());
            fos.close();


            //**

            /** ----------------------STOCKAGE DANS LA BASE DE DONNEE--------*/
            //Ajout de donnée au instant avant de la stocker
            instant.setName(fileS3Name);
            //LOGGER.info("Sauvegarde à l'url: " +"https://s3.amazonaws.com/"+AmazonS3Service.bucketName+"/"+fileS3Name);
            //instant.setUrl("https://s3.amazonaws.com/"+AmazonS3Service.bucketName+"/"+fileS3Name);

            LOGGER.info("Sauvegarde à l'url: " + "http://" + Inet4Address.getLocalHost().getHostAddress() + ":9999/images?name=" + fileS3Name + "&albumId=" + albumId);
            instant.setUrl("http://"+Inet4Address.getLocalHost().getHostAddress() + ":9999/images?name=" + fileS3Name + "&albumId=" + albumId);

            instant.setPublishDate(new Date()); //Faudra plus utiliser Date car deprecated... new Date() gives you a Date object initialized with the current date / time.
            //On détruit l'image car elle vient de la stocker sur le cloud donc inutile de la stocker sur mongoDb
            instant.setImgByte(null);

            // Ajout du user à tous les droits de l'instant
            InstantService instantService = new InstantService();
            instantService.addUserToInstantAllIdRight(instant, userId);
        }
        // Ajout du nouveau moment a la BDD
        albumService.addSaveMoment(moment, albumId);

        /** ------------------ NOTIFICATION PAR MOMENT ------------*/
        if(notificationToPeopleWithReadRightOnAlbum.equals("true")) { // TODO @Vincent implementer ca dans le controleur et appeler le controleurNotification ici

            /**------------------------ NOTIFICATION--------------*/
            UserService userService = new UserService(userRepository);

            NotificationServerService notificationServerService = new NotificationServerService(userService,notificationTokenRepository);
            //Retrouver l'album contenant l'instant envoyé (avec moment et instants ajoutés
            Album album = albumService.findAlbumById(albumId);

            //On récupère l'objet des droit lecture de l'album
            IdRight albumReadRights = album.getSpecificIdRight(Right.LECTURE.name());
            //On recupere la liste des utilisateursId de cet objet
            ArrayList<String> listUserIdListWithReadRight = albumReadRights.getUserIdList();

            //On filtre les user ayant les notifications enabled
            userService.checkNotificationEnabledString(listUserIdListWithReadRight);

            //On dit que y a que l'utilisateur 2 qui recevra le truc (on va d'abord retrouver son tokenNotification, puis on va pouvoir lui envoyer une notif
            //Pn récupere les token de ces utilisateurs dans une liste
            //Liste qui a pour but de lister les token des utilisateurs ayant le droit de lecture
            ArrayList<String> tokenUserListWithReadRight = notificationServerService.getTokenUserListFromIdUserList(listUserIdListWithReadRight);

            NotificationService notificationService = new NotificationService(notificationRepository);

            Notification notification = notificationService.createSaveNotification(userId, albumId, moment.getId(), moment);

            //On demande a FireBase d envoyer une notification a ces personnes (FireBase va utiliser les Token pour envoyer la notif car chaque token correspond a une appli installé sur un device.)
            notificationServerService.sendObjectWithTokenNotification(tokenUserListWithReadRight, notification);
        }
        return new ResponseEntity<String>("Finished to upload files",HttpStatus.OK);
    }


}

