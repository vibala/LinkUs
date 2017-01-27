package pfe.ece.LinkUS.Service;

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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by DamnAug on 14/10/2016.
 */
@Service
public class AlbumService {

    Logger LOGGER = Logger.getLogger("LinkUS.Service.AlbumService");

    AlbumRepository albumRepository;

    UserRepository userRepository;


    SubscriptionRepository subscriptionRepository;

    ApplicationContext ctx;
    MongoOperations operations;

    public AlbumService(AlbumRepository albumRepository) {
        ctx = new AnnotationConfigApplicationContext(MyMongoConfig.class);
        operations = (MongoOperations) ctx.getBean("mongoTemplate");
        this.albumRepository = albumRepository;
    }

    /**
     *
     * @param subscriptionRepository
     */
    public void setSubscriptionRepository(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    /**
     *
     * @param ownerId
     * @return
     */
    public List<Album> getAlbumsOwned(String ownerId) {
        List<Album> albumList = albumRepository.findByOwnerId(ownerId);

        //parsingService.parseDBObjectToListString(albumList, );

        if(albumList == null || albumList.isEmpty()) {
            throw new OwnerAlbumNotFoundException(ownerId);
        } else {
            return albumList; // List.toString ??!!
        }

    }

    /**
     * Si l'album existe deja on l'update
     * @param album
     * @return
     */
    public boolean checkUpdateAlbum(Album album) {

        update(album);
        return true;
    }

    public boolean checkSaveAlbum(Album album) {

        save(album);
        return true;
    }

    /**
     *
     * @param albumId
     * @return
     */
    public String getAlbumOwnerId(String albumId) {

        Album album = findAlbumById(albumId);
        if(album != null) {
            return album.getOwnerId();
        }
        return null;
    }

    /**
     *
     * @param id
     * @return
     */
    public Album findAlbumById(String id) {
        Album album = albumRepository.findOne(id);
        if (album == null) {
            throw  new AlbumNotFoundException(id);
        } else {
            return album;
        }
    }

    /**
     *
     * @param ownerId
     * @param name
     * @return
     */
    public Album findAlbumByOwnerIdAndName(String ownerId, String name) {
        Album album = albumRepository.findOneByOwnerIdAndNameIgnoreCase(ownerId, name);
        if (album == null) {
            throw  new AlbumNotFoundException(ownerId);
        } else {
            return album;
        }
    }

    /**
     *
     * @param ownerId
     * @param name
     * @return
     */
    public boolean checkAlbumByOwnerIdAndName(String ownerId, String name) {
        Album album = albumRepository.findOneByOwnerIdAndNameIgnoreCase(ownerId, name);
        return album != null;
    }

    /**
     *
     * @param friendGroupList
     * @param right
     * @return
     */
    public List<Album> findAlbumByGroupIdRight(List<FriendGroup> friendGroupList, String right) {
        List<String> groupIdList = new ArrayList<>();
        for(FriendGroup fg: friendGroupList) {
            groupIdList.add(fg.getId());
        }
        return albumRepository.findAlbumByGroupIdRight(groupIdList, right);
    }

    /**
     *
     * @param id
     * @param right
     * @return
     */
    public List<Album> findAlbumByUserIdRight(String id, String right) {

        return albumRepository.findAlbumByUserIdRight(id, right);

    }

    /**
     *
     * @param albumId
     * @param right
     * @return
     */
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

    /**
     *
     * @param albumId
     * @param right
     * @return
     */
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

    public void fillEmptyIds(Album album) {
        album.setRandomId();
        for(IdRight idRight: album.getIdRight()){
            idRight.setRandomId();
        }
        for(Moment moment: album.getMoments()) {
            moment.setRandomId();
            for(Instant instant: moment.getInstantList()) {
                instant.setRandomId();
                for(IdRight idRight:instant.getIdRight()) {
                    idRight.setRandomId();
                }
                for(Comment comment: instant.getCommentList()) {
                    comment.setRandomId();
                }
            }
        }
    }

    public List<String> getUrlsFromMoment(String userId, String albumId, String momentId) {
        Album album = findAlbumById(albumId);

        MomentService momentService = new MomentService();
        Moment moment = momentService.findMomentInAlbum(album, momentId);

        List<String> urls = new ArrayList<>();

        for (Instant instant: moment.getInstantList()) {
            urls.add(instant.getUrl());
        }
        return urls;
    }

    public List<PreviewAlbum> setPreviewAlbums(List<Album> albumList) {

        List<PreviewAlbum> previewAlbumList = new ArrayList<>();

        if(albumList != null) {
            for(Album album: albumList) {
                previewAlbumList.add(setPreviewAlbum(album));
            }
        }
        return previewAlbumList;
    }

    public PreviewAlbum setPreviewAlbum(Album album) {
        PreviewAlbum previewAlbum = new PreviewAlbum();
        previewAlbum.setAlbumId(album.getId());
        previewAlbum.setImgUrl(album.getImageUrl());
        previewAlbum.setAlbumName(album.getName());
        return previewAlbum;
    }
    /**
     *
     * @param album
     */
    private void save(Album album) {
        // Set to null not to erase another object with the same Id (new object)
        LOGGER.info("Saving new album" + album.toString());
        fillEmptyIds(album);
        albumRepository.save(album);
    }

    /**
     *
     * @param album
     */
    private void update(Album album) {
        LOGGER.info("Updating album" + album.toString());
        fillEmptyIds(album);
        albumRepository.save(album);
    }

    /**
     *
     * @param album
     */
    private void delete(Album album) {
        LOGGER.info("Deleting album" + album.toString());
        albumRepository.delete(album);
    }

    /**
     *
     * @param name
     * @param ownerId
     * @param countryName
     * @param placeName
     * @param beginDate
     * @param endDate
     * @return
     */
    public String createSaveAlbum(String name, String ownerId,
                                  String countryName, String placeName,
                                  Date beginDate, Date endDate) {

        if(!checkAlbumByOwnerIdAndName(ownerId, name)) {

            Album album = createCompleteAlbum(name, ownerId, countryName, placeName, beginDate, endDate);
            album.setActive(true);
            save(album);
            return album.getId();
        }
        return null;
    }

    /**
     *
     * @param ownerId
     * @param name
     * @return
     */
    public String createSaveAlbum(String ownerId, String name) {

        if(!checkAlbumByOwnerIdAndName(ownerId, name)) {

            Album album = createCompleteAlbum(name, ownerId, null, null, null, null);
            album.setActive(true);
            addUserToAlbumAllIdRight(album, ownerId);
            save(album);
            return album.getId();
        }
        return null;
    }

    /**
     * Retourne null si l'instant n'est pas ajouté, instantId sinon
     *
     * @param albumId
     * @param momentId
     * @param name
     * @return
     */
    public String createInstantSaveToAlbumMoment(String albumId, String momentId, String name) {

        InstantService instantService = new InstantService();
        Instant instant = instantService.createInstant(name);

        return createInstantPhotoSaveToAlbumMoment(albumId, momentId, instant, "");
    }

    /**
     * Retourne null si l'instant n'est pas ajouté, instantId sinon
     *
     * @param albumId
     * @param momentId
     * @param instant
     * @return
     */
    public String createInstantSaveToAlbumMoment(String albumId, String momentId, Instant instant) {

        return createInstantPhotoSaveToAlbumMoment(albumId, momentId, instant, "");
    }

    public String createInstantPhotoSaveToAlbumMoment(String albumId, String momentId, String name, String url) {

        InstantService instantService = new InstantService();
        Instant instant = instantService.createInstant(name);

        return createInstantPhotoSaveToAlbumMoment(albumId, momentId, instant, url);
    }

    public String createInstantPhotoSaveToAlbumMoment(String albumId, String momentId, Instant instant, String url) {

        Album album = findAlbumById(albumId);

        MomentService momentService = new MomentService();
        Moment moment = momentService.findMomentInAlbum(album, momentId);

        instant.setUrl(url);
        InstantService instantService = new InstantService();
        if(instantService.addInstantToMoment(moment, instant)) {
            update(album);
            return instant.getId();
        }
        return null;
    }

    public void saveFakePhoto(String userId, String albumId, String momentId, int numero) throws IOException {

        Files.copy(new File("./src/main/resources/public/images/image" + numero + ".jpeg").toPath(),
                new File("./images/" + userId + "/" + albumId + "/" + momentId + "_image" + numero + ".jpeg").toPath());
    }

    /**
     * Return null en cas  de non ajout du moment ou momentId
     *
     * @param albumId
     * @param momentName
     * @return
     */
    public String createMomentSaveToAlbum(String albumId, String momentName) {

        Album album = findAlbumById(albumId);

        MomentService momentService = new MomentService();
        Moment moment = momentService.createMoment(momentName, null);

        return createMomentSaveToAlbum(album, moment);
    }

    /**
     * Return null en cas  de non ajout du moment ou momentId
     *
     * @param album
     * @param moment
     * @return
     */
    public String createMomentSaveToAlbum(Album album, Moment moment) {

        MomentService momentService = new MomentService();

        if(momentService.addMomentToAlbum(album, moment)) {
            update(album);
            return moment.getId();
        }
        return null;
    }

    /**
     * Method permettant d'initialiser un album avec tout ou parti des
     * ses attribut mais n'écrase pas par des dnnées null
     *
     * @param name
     * @param ownerId
     * @param countryName
     * @param placeName
     * @param beginDate
     * @param endDate
     * @return
     */
    public Album createCompleteAlbum(String name, String ownerId,
                                     String countryName, String placeName,
                                     Date beginDate, Date endDate) {

        Album album = new Album();
        if(name != null) {
            album.setName(name);
        }
        if(ownerId != null) {
            album.setOwnerId(ownerId);
        }
        if(countryName != null) {
            album.setCountryName(countryName);
        }

        if(placeName != null) {
            album.setPlaceName(placeName);
        }
        if(beginDate != null) {
            album.setBeginDate(beginDate);
        }
        if(endDate != null) {
            album.setEndDate(endDate);
        }
        return album;
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

    /**
     *
     * @param userId
     * @param friendId
     * @param albumId
     * @param right
     * @return
     */
    public boolean addFriendToAlbum(UserService userService,
                                    String userId, String friendId, String albumId, String right) {

        this.userRepository = userRepository;

        // Récupération album
        Album album = findAlbumById(albumId);

        // Récupération du user
        User user = userService.findUserById(userId);

        // Si l'ami est bien dans la liste d'amis
        if(userService.checkFriend(user, friendId)) {

            // Ajout du friend au right
            if(addUserToAlbumIdRight(album, friendId, right)) {
                update(album);
                return true;
            }
        }
        return false;
    }

    public boolean addGroupFriendToAlbum(UserService userService, FriendGroupService friendGroupService,
                                         String userId, String friendGroupId, String albumId, String right) {

        // Récupération album
        Album album = findAlbumById(albumId);

        // Récupération du user
        User user = userService.findUserById(userId);

        IdRightService idRightService = new IdRightService();
        // Si le user est bien l'owner,
        //  et si le user est admin de l'album
        if(friendGroupService.findFriendGroupById(friendGroupId).getOwnerId().equals(userId) &&
                idRightService.checkUserInIdRight(idRightService.findByRight(album, Right.ADMIN.name()), userId)) {

            if(addFriendGroupToAlbumIdRight(album, friendGroupId, right)){
                update(album);
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param album
     * @param userId
     * @param right
     * @return
     */
    public boolean  addUserToAlbumIdRight(Album album, String userId, String right) {
        IdRightService idRightService = new IdRightService();
        return idRightService.addUserToIdRight(idRightService.findByRight(album, right), userId);
    }

    public boolean  addFriendGroupToAlbumIdRight(Album album, String friendGroupId, String right) {
        IdRightService idRightService = new IdRightService();
        return idRightService.addFriendGroupToIdRight(idRightService.findByRight(album, right), friendGroupId);
    }

    /**
     *
     * @param albumList
     * @param news
     * @param userId
     * @return
     */
    public List<Album> checkData(List<Album> albumList, boolean news, String userId) {
        checkDataAutorization(albumList, userId);
        checkDataRight(albumList, userId);
        checkDataNews(albumList, news, userId);
        return albumList;
    }

    public boolean checkAlbumIdInAlbums(String albumId, List<Album> albumsOfByCurrentAuthentifiedUser) {
        boolean found = false;
        for(Album albumItr: albumsOfByCurrentAuthentifiedUser) {
            if(albumItr.getId().equals(albumId)) {
                found = true;
            }
        }
        return found;
    }
    /**
     *
     * @param albumList
     * @param userId
     * @return
     */
    public List<Album> checkDataAutorization(List<Album> albumList, String userId) {

        SubscriptionService subscriptionService = new SubscriptionService(subscriptionRepository);
        Subscription subscription = subscriptionService.findSubscriptionByUserIdAndType(userId, SubscriptionTypeEnum.DESCRIPTION.name());

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

    /**
     *
     * @param albumList
     * @param userId
     * @return
     */
    public List<Album> checkDataRight(List<Album> albumList, String userId) {

        MomentService momentService = new MomentService();
        for(Album album: albumList) {
            momentService.checkAllMomentDataRight(album, userId);
        }
        return albumList;
    }

    /**
     *
     * @param albumList
     * @param news
     * @param userId
     * @return
     */
    public List<Album> checkDataNews(List<Album> albumList, boolean news, String userId) {

        MomentService momentService = new MomentService();
        for(Album album: albumList) {
            momentService.checkAllMomentDataNews(album, news, userId);
        }
        return albumList;
    }

    /**
     *
     * @param albumList
     */
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

        if(album.getIdRight().isEmpty()) {
            addAllIdRightToAlbum(album);
        }

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

    /**
     *
     * @param instant
     * @param albumId
     * @param momentId
     * @return
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

    /**
     *
     * @param moment
     * @param albumId
     * @return
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
