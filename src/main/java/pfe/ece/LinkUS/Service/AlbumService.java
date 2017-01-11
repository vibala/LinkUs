package pfe.ece.LinkUS.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Config.MyMongoConfig;
import pfe.ece.LinkUS.Exception.AlbumNotFoundException;
import pfe.ece.LinkUS.Exception.OwnerAlbumNotFoundException;
import pfe.ece.LinkUS.Model.*;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.AlbumRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by DamnAug on 14/10/2016.
 */
@Service
public class AlbumService {

    Logger LOGGER = Logger.getLogger("LinkUS.Controller.AlbumService");

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    UserRepository userRepository;

    ApplicationContext ctx;
    MongoOperations operations;

    public AlbumService(AlbumRepository albumRepository) {
        ctx = new AnnotationConfigApplicationContext(MyMongoConfig.class);
        operations = (MongoOperations) ctx.getBean("mongoTemplate");
        this.albumRepository = albumRepository;
    }

    public List<Album> getAlbumsOwned(String ownerId) {
        List<Album> albumList = albumRepository.findByOwnerId(ownerId);

        //parsingService.parseDBObjectToListString(albumList, );

        if(albumList == null || albumList.isEmpty()) {
            throw new OwnerAlbumNotFoundException(ownerId);
        } else {
            return albumList; // List.toString ??!!
        }

    }

    public String getAlbumOwnerId(String albumId) {

        Album album = findAlbumById(albumId);
        return album.getOwnerId();
    }

    public Album findAlbumById(String id) {
        Album album = albumRepository.findOne(id);
        System.out.println(album.toString());
        if (album == null) {
            throw  new AlbumNotFoundException(id);
        } else {
            return album;
        }
    }

    public List<Album> findAlbumByGroupIdRight(List<FriendGroup> friendGroupList, String right) {
        List<String> groupIdList = new ArrayList<>();
        for(FriendGroup fg: friendGroupList) {
            groupIdList.add(fg.getId());
        }
        return albumRepository.findAlbumByGroupIdRight(groupIdList, right);
    }

    public List<Album> findAlbumByUserIdRight(String id, String right) {

        return albumRepository.findAlbumByUserIdRight(id, right);

    }

    public List<String> findUserIdsByAlbum(String albumId, String right) {
        List<String>  userIdsList = new ArrayList<>();

        Album album = albumRepository.findOne(albumId);

        if(album != null) {
            for(IdRight idRight: album.getIdRight()) {
                if (idRight.getRight().equals(right)) {
                    userIdsList.addAll(idRight.getUserIdList());
                    break;
                }
            }
        }
        return userIdsList;
    }

    public List<String> findGroupIdsByAlbum(String albumId, String right) {
        List<String>  groupIdsList = new ArrayList<>();

        Album album = albumRepository.findOne(albumId);

        if(album != null) {
            for(IdRight idRight: album.getIdRight()) {
                if (idRight.getRight().equals(right)) {
                    groupIdsList.addAll(idRight.getGroupIdList());
                    break;
                }
            }
        }
        return groupIdsList;
    }

    public void save(Album album) {
        // Set to null not to erase another object with the same Id (new object)
        LOGGER.info("Saving new album" + album.toString());
        album.setId(null);
        albumRepository.save(album);
    }

    public void update(Album album) {
        LOGGER.info("Updating album" + album.toString());
        albumRepository.save(album);
    }

    public void delete(Album album) {
        LOGGER.info("Deleting album" + album.toString());
        albumRepository.delete(album);
    }

    /**
     * Method preparing the users' albums: new album, new moment
     * @param userId
     * @return
     */
    public void createAlbumForNewRegisteredUser(String userId){
        LOGGER.info("createAlbumForEachNewRegisterUser - debut de création d'un album");
        Album album = new Album();
        /** TODO A ENLEVER  **/
        album.setName("First album");
        album.setPlaceName("Earth");
        album.setCountryName("The milkey way");
        /**---------------**/
        album.setBeginDate(new Date());
        album.setActive(false);
        album.setOwnerId(userId);

        // Ajout de tous les idRight
        addAllIdRight(album);

        // Ajout de l'owner dans chaque droit
        IdRightService idRightService = new IdRightService();
        idRightService.addUserToAll(album, userId);

        // Ajout d'un moment par défaut
        MomentService momentService = new MomentService();
        momentService.add(album, momentService.newDefaultMoment());

        LOGGER.info("createAlbumForEachNewRegisterUser - fin de création d'un album");
        save(album);
    }

    /**
     * Ajoute tous les idRight (vide) à l'album
     * @param album
     */
    public void addAllIdRight(Album album) {
        IdRightService idRightService = new IdRightService();
        for(Right right: Right.values()) {
            IdRight idRight = new IdRight(right.name());
            idRightService.add(album, idRight);
        }
    }

    public void addFriendToAlbum(String userId, String friendId, String albumId, String right) {

        // Récupération album
        Album album = findAlbumById(albumId);

        // Récupération du user
        UserService userService = new UserService(userRepository);
        User user = userService.findUserById(userId);

        // Si l'ami est bien dans la liste
        if(userService.checkFriend(user, friendId)) {

            // Ajout du friend au right
            addUser(album, friendId, right);
        }
    }

    public void addUser(Album album, String userId, String right) {
        IdRightService idRightService = new IdRightService();
        idRightService.addUser(idRightService.findByRight(album, right), userId);
    }

    /*
    *
    *
    * INSTANT
    *
    *
    */

    public void addInstant(Instant instant, String albumId, String momentId){

        // Récupération album
        Album album = findAlbumById(albumId);

        // Récupération moment
        MomentService momentService = new MomentService();
        Moment momentFound = momentService.find(album, momentId);

        // Ajout instant
        InstantService instantService = new InstantService();
        instantService.add(momentFound, instant);

        // Sauvegarde
        albumRepository.save(album);
    }

    /*
    *
    *
    * MOMENT
    *
    *
    */

    public void addMoment(Moment moment, String albumId) {

        // Récupération album
        Album album = albumRepository.findOne(albumId);

        // Ajout moment
        MomentService momentService = new MomentService();
        momentService.add(album, moment);

        // Sauvegarde
        albumRepository.save(album);
    }


}
