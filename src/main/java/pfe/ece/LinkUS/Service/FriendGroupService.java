package pfe.ece.LinkUS.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Model.FriendGroup;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.FriendGroupRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by DamnAug on 14/10/2016.
 */
@Service
public class FriendGroupService {

    Logger LOGGER = Logger.getLogger("LinkUS.Controller.FriendGroupService");
    @Autowired
    FriendGroupRepository friendGroupRepository;

    public FriendGroupService(FriendGroupRepository friendGroupRepository) {
        this.friendGroupRepository = friendGroupRepository;
    }

    public List<FriendGroup> findFriendGroupByIds(List<String> idList) {
        List<FriendGroup> friendGroupList = new ArrayList<>();

        for (String id: idList) {
            FriendGroup friendGroup = friendGroupRepository.findOne(id);
            if (friendGroup != null) {
                    friendGroupList.add(friendGroup);
            }
        }
        return friendGroupList;
    }

    /**
     * Find friend group by UserId (every friendGroup where the user is)
     */
    public List<FriendGroup> findFriendGroupByUserId(String id) {
        return friendGroupRepository.findFriendGroupByMembers(id);
    }

    public List<String> findUserIdsByFriendGroup(List<String> groupIdList) {
        List<String> userIdList = new ArrayList<>();

        for (String groupId: groupIdList) {
            FriendGroup friendGroup = friendGroupRepository.findOne(groupId);
            userIdList.addAll(friendGroup.getMembers());
        }
        return userIdList;
    }

    private void save(FriendGroup friendGroup) {
        // Set to null not to erase another object with the same Id (new object)
        friendGroup.setId(null);
        LOGGER.info("Saving new friendGroup" + friendGroup.toString());
        friendGroupRepository.save(friendGroup);
    }

    private void update(FriendGroup friendGroup) {
        LOGGER.info("Updating friendGroup" + friendGroup.toString());
        friendGroupRepository.save(friendGroup);
    }

    private void delete(FriendGroup friendGroup) {
        LOGGER.info("Deleting friendGroup" + friendGroup.toString());
        friendGroupRepository.delete(friendGroup);
    }
}
