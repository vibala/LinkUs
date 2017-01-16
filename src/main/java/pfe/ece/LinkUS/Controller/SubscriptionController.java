package pfe.ece.LinkUS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pfe.ece.LinkUS.Model.Subscription;
import pfe.ece.LinkUS.Model.SubscriptionType;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.*;
import pfe.ece.LinkUS.Service.SubscriptionService;
import pfe.ece.LinkUS.Service.SubscriptionTypeService;

import java.util.logging.Logger;

/**
 * Created by DamnAug on 15/11/2016.
 */
@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    Logger LOGGER = Logger.getLogger("LinkUS.Controller.SubscriptionController");
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


    @RequestMapping(params = {"id", "type"})
    public Subscription findSubscriptionByIdAndType(@RequestParam("id") String id, @RequestParam("type") String type) {

        // Call service function
        SubscriptionService subscriptionService = new SubscriptionService(subscriptionRepository);
        return subscriptionService.findSubscription(id, type);
    }

    @RequestMapping(method = RequestMethod.POST,
            value = "/add",
            consumes = "application/json")
    public void addSubscription(@RequestBody Subscription subscription){

        // Call service function
        SubscriptionService subscriptionService = new SubscriptionService(subscriptionRepository);
        subscriptionService.addSubscription(subscription);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public void deleteSubscription(@RequestBody Subscription subscription) {
        SubscriptionService subscriptionService = new SubscriptionService(subscriptionRepository);

        subscriptionService.deleteSubscription(subscription);
    }

    @RequestMapping(params = {"userId", "subscriptionTypeId"})
    public void updateSubscription(@RequestParam("userId") String userId,
                                   @RequestParam("subscriptionTypeId") String subscriptionTypeId) {

        // Get the subscription type
        SubscriptionTypeService subscriptionTypeService = new SubscriptionTypeService(subscriptionTypeRepository);
        SubscriptionType subscriptionType = subscriptionTypeService.findSubscriptionTypeById(subscriptionTypeId);

        // Get the subscription of the user (using subscription Type)
        SubscriptionService subscriptionService = new SubscriptionService(subscriptionRepository);
        subscriptionService.updateSubscription(userId, subscriptionType);
    }
}