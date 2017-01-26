package pfe.ece.LinkUS.Repository.OtherMongoDBRepo;

import org.springframework.data.mongodb.repository.MongoRepository;
import pfe.ece.LinkUS.Model.FriendGroup;

import java.util.List;

/**
 * Created by DamnAug on 14/10/2016.
 */
public interface FriendGroupRepository extends MongoRepository<FriendGroup, String> {

    //@Query(value = "{\"memberId\":?0}")
    List<FriendGroup> findFriendGroupByMembers(String id);
    List<FriendGroup> findFriendGroupByNameLikeIgnoreCase(String name);
    List<FriendGroup> findByOwnerId(String ownerId);
    FriendGroup findByOwnerIdAndName(String ownerId, String name);
}
