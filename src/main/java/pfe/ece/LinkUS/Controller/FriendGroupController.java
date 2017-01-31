package pfe.ece.LinkUS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pfe.ece.LinkUS.Model.FriendGroup;
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

    @RequestMapping(value = "/addUser", params = {"userId"}, method = RequestMethod.POST)
    public ResponseEntity addUserToFriendGroup(@RequestBody FriendGroup friendGroup,@RequestParam String userId) {
        if(friendGroup != null && userId != null && !userId.equals("")) {
            FriendGroupService friendGroupService = new FriendGroupService(friendGroupRepository);
            if(friendGroupService.addUserToFriendGroup(friendGroup.getId(), userId)){
                return new ResponseEntity(HttpStatus.OK);
            }
        }
        return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/removeUser", params = {"userId"}, method = RequestMethod.POST)
    public ResponseEntity removeUserToFriendGroup(@RequestBody FriendGroup friendGroup,@RequestParam String userId) {
        if(friendGroup != null && userId != null && !userId.equals("")) {
            FriendGroupService friendGroupService = new FriendGroupService(friendGroupRepository);
            if (friendGroupService.removeUserToFriendGroup(friendGroup.getId(), userId)) {
                return new ResponseEntity(HttpStatus.OK);
            }
        }
        return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/addFilled",  method = RequestMethod.POST)
    public ResponseEntity addFilledFriendGroup(@RequestBody FriendGroup friendGroup) {

        String name=friendGroup.getName();
        List<String> userIdList=friendGroup.getMembers();
        String ownerId = accessTokenService.getUserIdOftheAuthentifiedUser();

        FriendGroupService friendGroupService = new FriendGroupService(friendGroupRepository);
        if(friendGroupService.addFilledFriendGroup(name, ownerId, userIdList) != null){
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.CONFLICT);

    }

    @RequestMapping(value = "/addEmpty", method = RequestMethod.POST)
    public ResponseEntity addFriendGroup(@RequestBody String name) {
        name=name.replace("\"","");
        String ownerId = accessTokenService.getUserIdOftheAuthentifiedUser();

        FriendGroupService friendGroupService = new FriendGroupService(friendGroupRepository);
        if(friendGroupService.addFilledFriendGroup(name, ownerId, null) != null){
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/removeByName",method = RequestMethod.POST)
    public ResponseEntity removeFriendGroupByName(@RequestBody String name) {
        name=name.replace("\"","");
        String ownerId = accessTokenService.getUserIdOftheAuthentifiedUser();

        FriendGroupService friendGroupService = new FriendGroupService(friendGroupRepository);
        if(friendGroupService.deleteFriendGroup(name, ownerId)){
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/remove",method = RequestMethod.POST)
    public ResponseEntity removeFriendGroupById(@RequestBody String id) {
        id=id.replace("\"","");
        String ownerId = accessTokenService.getUserIdOftheAuthentifiedUser();

        FriendGroupService friendGroupService = new FriendGroupService(friendGroupRepository);
        if(friendGroupService.deleteFriendGroup(id)){
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.CONFLICT);
    }
}