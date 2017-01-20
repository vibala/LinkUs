package pfe.ece.LinkUS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.AlbumRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.FriendGroupRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.SubscriptionRepository;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.UserRepository;
import pfe.ece.LinkUS.Service.FriendGroupService;
import pfe.ece.LinkUS.Service.TokenService.AccessTokenService;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by DamnAug on 14/10/2016.
 */
@RestController
@RequestMapping("/friendGroup")
public class FriendGroupController {

    private Logger LOGGER = Logger.getLogger("LinkUS.Controller.FriendGroupController");

    @Autowired
    UserRepository usersRepository;
    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    FriendGroupRepository friendGroupRepository;
    @Autowired
    SubscriptionRepository subscriptionRepository;
    @Autowired
    private AccessTokenService accessTokenService;

    @RequestMapping(value = "/addUser", params = {"friendGroupId", "userId"}, method = RequestMethod.POST)
    public ResponseEntity addUserToFriendGroup(@RequestBody String friendGroupId,
                                               @RequestBody String userId) {

        FriendGroupService friendGroupService = new FriendGroupService(friendGroupRepository);
        if(friendGroupService.addUserToFriendGroup(friendGroupId, userId)){
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/removeUser", params = {"friendGroupId", "userId"}, method = RequestMethod.POST)
    public ResponseEntity removeUserToFriendGroup(@RequestBody String friendGroupId,
                                                  @RequestBody String userId) {

        FriendGroupService friendGroupService = new FriendGroupService(friendGroupRepository);
        if(friendGroupService.removeUserToFriendGroup(friendGroupId, userId)){
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/add", params = {"name", "userIdList"}, method = RequestMethod.POST)
    public ResponseEntity addFilledFriendGroup(@RequestBody String name,
                                               @RequestBody List<String> userIdList) {

        String ownerId = accessTokenService.getUserIdOftheAuthentifiedUser();

        FriendGroupService friendGroupService = new FriendGroupService(friendGroupRepository);
        if(friendGroupService.addFilledFriendGroup(name, ownerId, userIdList)){
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.CONFLICT);

    }
    @RequestMapping(value = "/add", params = {"name"}, method = RequestMethod.POST)
    public ResponseEntity addFriendGroup(@RequestBody String name) {

        String ownerId = accessTokenService.getUserIdOftheAuthentifiedUser();

        FriendGroupService friendGroupService = new FriendGroupService(friendGroupRepository);
        if(friendGroupService.addFilledFriendGroup(name, ownerId, null)){
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.CONFLICT);

    }

}
