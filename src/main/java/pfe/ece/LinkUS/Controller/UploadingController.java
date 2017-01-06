package pfe.ece.LinkUS.Controller;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pfe.ece.LinkUS.Model.Album;
import pfe.ece.LinkUS.Model.IdRight;
import pfe.ece.LinkUS.Model.Moment;
import pfe.ece.LinkUS.Model.Right;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.AlbumRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.UserRepository;
import pfe.ece.LinkUS.Repository.TokenMySQLRepo.NotificationTokenRepository;
import pfe.ece.LinkUS.ServerService.AmazonS3Service;
import pfe.ece.LinkUS.ServerService.NotificationService;
import pfe.ece.LinkUS.Service.AlbumService;
import pfe.ece.LinkUS.Service.TokenService.AccessTokenService;
import pfe.ece.LinkUS.Service.UserService;
import javax.imageio.ImageIO;
import javax.sound.midi.SysexMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

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

    @RequestMapping(value = "/uploadFiles", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> uploadingFiles(@RequestBody Moment moment,@RequestParam("notificationToPeopleWithReadRightOnAlbum") String notificationToPeopleWithReadRightOnAlbum) throws IOException {

        // On fetche grâce au access token service
        String userId = accessTokenService.getUserIdOftheAuthentifiedUser();

        // On fetche les albums dont l'utilisateur est l'owned
        List<Album> albumsOwnedByCurrentAuthentifiedUser = albumService.getAlbumsOwned(userId);

        // On fetche uniquement le premier album
        // (mode freemium 1 utilisateur peut seulement consulter un album parmi les albums sur lequels il est owner)
        Album first_album = albumsOwnedByCurrentAuthentifiedUser.get(0);

        // On fetche l'id de cet album
        String albumId = first_album.getId();

        System.out.println(notificationTokenRepository);

        /** ----------------------UPLOAD IMAGE*/
        //PARTIE AMAZON

        /*AmazonS3Service amazonService = new AmazonS3Service();
=======
        AmazonS3Service amazonService = new AmazonS3Service();
>>>>>>> evolution2

        //On genere un nom unique du fichier dans AmazonS3
        String fileS3Name = amazonService.generatefileS3Name(moment.getName());

        //On upload l'image dans AmazonS3
<<<<<<< HEAD
        amazonService.uploadFileByte(moment.getImgByte(),fileS3Name,"image*//*");*/

        // PARTIE LOCALE
        String fileS3Name = moment.getName();
        File directory = new File("./images");
        if (!directory.isDirectory()) {
            directory.mkdir();
        }

        FileOutputStream fos = new FileOutputStream("./images/" + fileS3Name);
        fos.write(moment.getImgByte());
        fos.close();

        //**
        /** ----------------------STOCKAGE DANS LA BASE DE DONNEE*/
        //Ajout de donnée au moment avant de la stocker
        moment.setName(fileS3Name);
        //moment.setUrl("https://s3.amazonaws.com/"+AmazonS3Service.bucketName+"/"+fileS3Name);
        moment.setUrl("http://"+Inet4Address.getLocalHost().getHostAddress() + ":9999/images?name=" + fileS3Name);

        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while(e.hasMoreElements())
        {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements())
            {
                InetAddress i = (InetAddress) ee.nextElement();
                System.out.println(i.getHostAddress());
            }
            System.out.println("---");
        }

        moment.setPublishDate(new Date()); //Faudra plus utiliser Date car deprecated... new Date() gives you a Date object initialized with the current date / time.
        //On détruit l'image car elle vient de la stocker sur le cloud donc inutile de la stocker
        moment.setImgByte(null);

        //Ajout de la photo dans la base de donnée
        AlbumService albumService = new AlbumService(albumRepository);
        albumService.addPhoto(moment,userId,albumId);

        if(notificationToPeopleWithReadRightOnAlbum.equals("true")) { // TODO @Vincent implementer ca dans le controleur et appeler le controleurNotification ici

            /**------------------------ NOTIFICATION*/
            UserService userService=new UserService(userRepository);
            System.out.println("2"+notificationTokenRepository);
            NotificationService notificationService = new NotificationService(userService,notificationTokenRepository);
            //Retrouver l'album contenant le moment envoyé
            Album album = albumService.findAlbumById(albumId);

            //On récupère l'objet des droit lecture de l'album
            IdRight albumReadRights = album.getSpecificIdRight(Right.LECTURE.name());
            //On recupere la liste des utilisateurs de cet objet
            ArrayList<String> listUserIdListWithReadRight = albumReadRights.getUserIdList();

            //FAKE car la fonction retourne [2,3] donc on fake un [2]
            //  listUserIdListWithReadRight=new ArrayList<String>();
            //listUserIdListWithReadRight.add("2");

            //On dit que y a que l'utilisateur 2 qui recevra le truc (on va d'abord retrouver son tokenNotification, puis on va pouvoir lui envoyer une notif
            //listUserIdListWithReadRight.add("2");
            //Pn récupere les token de ces utilisateurs dans une liste
            //Liste qui a pour but de lister les token des utilisateurs ayant le droit de lecture
            System.out.println("4"+notificationTokenRepository);
            ArrayList<String> tokenUserListWithReadRight = notificationService.getTokenUserListFromIdUserList(listUserIdListWithReadRight);

            //On demande a FireBase d envoyer une notification a ces personnes (FireBase va utiliser les Token pour envoyer la notif car chaque token correspond a une appli installé sur un device.)
            notificationService.sendMomentWithTokenNotification(tokenUserListWithReadRight, moment);
        }

    return new ResponseEntity<String>("Finished to upload files",HttpStatus.OK);
    }
}

