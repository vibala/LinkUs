package pfe.ece.LinkUS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pfe.ece.LinkUS.Model.SubscriptionType;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.*;
import pfe.ece.LinkUS.Service.SubscriptionTypeService;

import java.util.logging.Logger;

/**
 * Created by DamnAug on 23/11/2016.
 */
@RestController
@RequestMapping("/subscriptionType")
public class SubscriptionTypeController {

    Logger LOGGER = Logger.getLogger("LinkUS.Controller.SubscriptionTypeController");
    @Autowired
    UserRepository userRepository;
    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    FriendGroupRepository friendGroupRepository;
    @Autowired
    SubscriptionRepository subscriptionRepository;
    @Autowired
    SubscriptionTypeRepository subscriptionTypeRepository;

    @RequestMapping("/")
    public String userDefaultCall() {
        return "Not implemented yet.";
    }


    @RequestMapping(params = {"id"})
    public SubscriptionType findSubscriptionTypeById(@RequestParam("id") String id) {

        // Call service function
        SubscriptionTypeService subscriptionTypeService = new SubscriptionTypeService(subscriptionTypeRepository);
        return subscriptionTypeService.findSubscriptionTypeById(id);
    }

    /**
     * ADD SUBSCRIPTION
     * @param subscriptionType
     */
    @RequestMapping(method = RequestMethod.POST,
            value = "/add",
            consumes = "application/json")
    public void addSubscription(@RequestBody SubscriptionType subscriptionType){
        SubscriptionTypeService subscriptionTypeService = new SubscriptionTypeService(subscriptionTypeRepository);

        subscriptionTypeService.addSubscriptionType(subscriptionType);
    }

    /**
     * UPDATE SUBSCRIPTION
     * @param subscriptionType
     */
    @RequestMapping(method = RequestMethod.POST,
            value = "/update",
            consumes = "application/json")
    public void updateSubscription(@RequestBody SubscriptionType subscriptionType) {
        SubscriptionTypeService subscriptionTypeService = new SubscriptionTypeService(subscriptionTypeRepository);

        subscriptionTypeService.updateSubscriptionType(subscriptionType);
    }

    /**
     * DELETE SUBSCRIPTION
     * @param subscriptionType
     */
    @RequestMapping(method = RequestMethod.POST,
            value = "/delete",
            consumes = "application/json")
    public void deleteSubscriptionType(@RequestBody SubscriptionType subscriptionType) {
        SubscriptionTypeService subscriptionTypeService = new SubscriptionTypeService(subscriptionTypeRepository);

        subscriptionTypeService.deleteSubscriptionType(subscriptionType);
    }
}
