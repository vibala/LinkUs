package pfe.ece.LinkUS.Repository.OtherMongoDBRepo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import pfe.ece.LinkUS.Model.Album;

import java.util.List;

/**
 * Created by DamnAug on 14/10/2016.
 */
public interface AlbumRepository extends MongoRepository<Album, String> {


    /**
     * Find every album owned by a user
     * @param ownerId
     * @return
     */
    List<Album> findByOwnerId(String ownerId);


    /**
     * Find a album owned with a specific name
     * @param ownerId
     * @param name
     * @return
     */
    Album findOneByOwnerIdAndNameIgnoreCase(String ownerId, String name);

    /**
     * Find one album by name, ignoring case
     * @param name
     * @return album
     */
    Album findByNameIgnoreCase(String name);

    /**
     * Find every album of the current location
     * @param placeName
     * @return
     */
    List<Album> findByPlaceNameIgnoreCase(String placeName);

    /**
     * Find every albums of the current country
     * @param countryName
     * @return
     */
    List<Album> findByCountryNameIgnoreCase(String countryName);


    /**
     * Find every album having group ids and right
     * @param idList
     * @param right
     * @return
     */
    @Query(value = "{idRight:{$elemMatch:{right:?1, userIdList:{$in:?0}}}}")
    List<Album> findAlbumByGroupIdRight(List<String> idList, String right);

    /**
     * Find every album having user id and right
     * @param id
     * @param right
     * @return
     */
    @Query(value = "{idRight:{$elemMatch:{right:?1, userIdList:?0}}}")
    List<Album> findAlbumByUserIdRight(String id, String right);
}