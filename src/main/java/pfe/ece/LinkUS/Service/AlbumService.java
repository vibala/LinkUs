package pfe.ece.LinkUS.Service;

import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Config.MyMongoConfig;
import pfe.ece.LinkUS.Exception.AlbumNotFoundException;
import pfe.ece.LinkUS.Exception.OwnerAlbumNotFoundException;
import pfe.ece.LinkUS.Model.Album;
import pfe.ece.LinkUS.Model.FriendGroup;
import pfe.ece.LinkUS.Model.IdRight;
import pfe.ece.LinkUS.Model.Moment;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.AlbumRepository;

import java.util.ArrayList;
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

    // Added by Vignesh
    public void save_album(Album album) {
       save(album);
    }

    // Added By Vignesh
    public void update_album(Album album) {
        LOGGER.info("Updating album" + album.toString());
        albumRepository.save(album);
    }

    private void save(Album album) {
        // Set to null not to erase another object with the same Id (new object)
        LOGGER.info("Saving new album" + album.toString());
        albumRepository.save(album);
    }

    private void update(Album album) {
        LOGGER.info("Updating album" + album.toString());
        albumRepository.save(album);
    }




    private void delete(Album album) {
        LOGGER.info("Deleting album" + album.toString());
        albumRepository.delete(album);
    }


    //Il faudra plus tard AUTOMATISER la détection du userid pour ne plus le mettre dans la requete-> eviter que n'importe qui modifie n'importe quoi
    public void addPhoto(Moment moment, String userId, String albumId){
        final Query query = new Query(new Criteria().andOperator(
                Criteria.where("ownerId").is(userId),
                Criteria.where("_id").is(albumId)
        ));
        /*Query query = new Query();
        query.addCriteria(Criteria.where("ownerId").is(userId));
        query.addCriteria( Criteria.where("_id").is(albumId));
        */
        final Update update = new Update().addToSet("moments", moment);

        final WriteResult wr = operations.updateFirst(query, update, "album");
    }
}
