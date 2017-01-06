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

        // On ajoute l'owner dans chaque droit
        for(Right right: Right.values()) {
            IdRight idRight = new IdRight(right.name());
            idRight.getUserIdList().add(userId);
            album.getIdRight().add(idRight);
        }

        // Ajout d'un moment par défaut
        MomentService momentService = new MomentService();
        album.getMoments().add(momentService.newDefaultMoment());

        LOGGER.info("createAlbumForEachNewRegisterUser - fin de création d'un album");
        save(album);
    }

    public void addInstant(Instant instant, String albumId, String momentId){

        Album album = albumRepository.findOne(albumId);
        ArrayList<Moment> momentArrayList = new ArrayList<>();

        // on cherche le moment correspondant
        for (Moment moment: momentArrayList) {
            if(moment.getId().equals(momentId)) {
                // Si l'instant n'existe pas, on l'ajoute
                if(!moment.getInstantList().contains(instant)) {
                    moment.getInstantList().add(instant);
                    LOGGER.info("Adding instant: " + instant);
                }
            }
        }
        albumRepository.save(album);
    }

    public void addMoment(Moment moment, String albumId) {

        Album album = albumRepository.findOne(albumId);

        // On cherche si le moment existe, si non: on l'ajoute
        if(!album.getMoments().contains(moment)) {
            album.getMoments().add(moment);
            LOGGER.info("Adding moment: " + moment);
        }
        albumRepository.save(album);
    }
}
