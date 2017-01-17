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
import pfe.ece.LinkUS.Model.Enum.Right;
import pfe.ece.LinkUS.Model.Enum.SubscriptionTypeEnum;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.AlbumRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.SubscriptionRepository;
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

    @Autowired
    SubscriptionRepository subscriptionRepository;

    ApplicationContext ctx;
    MongoOperations operations;

    public AlbumService(AlbumRepository albumRepository) {
        ctx = new AnnotationConfigApplicationContext(MyMongoConfig.class);
        operations = (MongoOperations) ctx.getBean("mongoTemplate");
        this.albumRepository = albumRepository;
    }

    public void setSubscriptionRepository(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
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
        addAllIdRightToAlbum(album);

        // Ajout de l'owner dans chaque droit
        addUserToAlbumAllIdRight(album, userId);

        // Ajout d'un moment par défaut
        MomentService momentService = new MomentService();
        momentService.addMomentToAlbum(album, momentService.newDefaultMoment());

        LOGGER.info("createAlbumForEachNewRegisterUser - fin de création d'un album");
        save(album);
    }

    /**
     * Ajoute tous les idRight (vide) à l'album (album + instants
     * @param album
     */
    public void addAllIdRightToAlbum(Album album) {
        IdRightService idRightService = new IdRightService();
        //add IdRight to the album
        for(Right right: Right.values()) {
            IdRight idRight = new IdRight(right.name());
            idRightService.addIdRightToAlbum(album, idRight);
        }
        // add IdRight to all instants of the album
        for(Moment moment: album.getMoments()) {
            idRightService.addAllIdRightToAllInstant(moment);
        }
    }

    public boolean addFriendToAlbum(String userId, String friendId, String albumId, String right) {

        // Récupération album
        Album album = findAlbumById(albumId);

        // Récupération du user
        UserService userService = new UserService(userRepository);
        User user = userService.findUserById(userId);

        // Si l'ami est bien dans la liste d'amis
        if(userService.checkFriend(user, friendId)) {

            // Ajout du friend au right
            addUserToAlbumIdRight(album, friendId, right);
            return true;
        }
        return false;
    }

    public boolean  addUserToAlbumIdRight(Album album, String userId, String right) {
        IdRightService idRightService = new IdRightService();
        return idRightService.addUserToIdRight(idRightService.findByRight(album, right), userId);
    }

    public List<Album> checkData(List<Album> albumList, boolean news, String userId) {
        checkDataAutorization(albumList, userId);
        checkDataRight(albumList, userId);
        checkDataNews(albumList, news, userId);
        return albumList;
    }

    public List<Album> checkDataAutorization(List<Album> albumList, String userId) {

        SubscriptionService subscriptionService = new SubscriptionService(subscriptionRepository);
        Subscription subscription = subscriptionService.findSubscription(userId, SubscriptionTypeEnum.DESCRIPTION.name());

        if ((subscription.getDateFin() != null && subscription.getDateFin().compareTo(new Date()) < 0) ||
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

            // gerer le cas des descriptions/friend free
            if (subscription.getFree() > 0) {
                // Decrement
                subscription.setFree(subscription.getFree() - 1);
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

    public List<Album> checkDataRight(List<Album> albumList, String userId) {

        MomentService momentService = new MomentService();
        for(Album album: albumList) {
            momentService.checkAllMomentDataRight(album, userId);
        }
        return albumList;
    }

    public List<Album> checkDataNews(List<Album> albumList, boolean news, String userId) {

        MomentService momentService = new MomentService();
        for(Album album: albumList) {
            momentService.checkAllMomentDataNews(album, news, userId);
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


    /**
     * Add user to all idRight in the album
     *
     * @param album
     * @param userId
     */
    public boolean addUserToAlbumAllIdRight(Album album, String userId){

        boolean bool = true;

        for(IdRight idRight: album.getIdRight()) {
            IdRightService idRightService = new IdRightService();
            if(!idRightService.addUserToIdRight(idRight, userId)) {
                bool = false;
            }
        }
        return true;
    }

    /*
    *
    *
    * INSTANT
    *
    *
    */

    public boolean addSaveInstant(Instant instant, String albumId, String momentId){

        // Récupération album
        Album album = findAlbumById(albumId);

        // Récupération moment
        MomentService momentService = new MomentService();
        Moment momentFound = momentService.findMomentInAlbum(album, momentId);

        if(album != null && momentFound != null && instant != null) {
            // Ajout instant
            InstantService instantService = new InstantService();
            if(instantService.addInstantToMoment(momentFound, instant)) {

                // Sauvegarde
                albumRepository.save(album);

                return true;
            }
        }
        return false;
    }

    /*
    *
    *
    * MOMENT
    *
    *
    */

    public boolean addSaveMoment(Moment moment, String albumId) {

        // Récupération album
        Album album = albumRepository.findOne(albumId);

        if(album != null && moment != null) {
            // Ajout moment
            MomentService momentService = new MomentService();
            momentService.addMomentToAlbum(album, moment);

            // Sauvegarde
            albumRepository.save(album);

            return true;
        }
        return false;
    }
}
