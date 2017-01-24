package pfe.ece.LinkUS.Service;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Exception.SubscriptionNotFoundException;
import pfe.ece.LinkUS.Model.Enum.SubscriptionTypeEnum;
import pfe.ece.LinkUS.Model.Subscription;
import pfe.ece.LinkUS.Model.SubscriptionType;
import pfe.ece.LinkUS.Model.User;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.SubscriptionRepository;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by DamnAug on 15/11/2016.
 */
@Service
public class SubscriptionService {

    Logger LOGGER = Logger.getLogger("LinkUS.Service.SubscriptionService");
    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Value("${subscription.description.free}")
    private int descriptionFree;

    @Value("${subscription.friend.free}")
    private int friendFree;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public Subscription findSubscription(String id, String type) {
        Subscription subscription = subscriptionRepository.findByUserIdAndType(id, type);
        if (subscription == null) {
            throw new SubscriptionNotFoundException(id);
        }
        return subscription;
    }

    public void addUserToAllSubscription(User user) {

        // On crée des subscriptions par défaut pour le user
        for(SubscriptionTypeEnum subscriptionTypeEnum: SubscriptionTypeEnum.values()) {

            addSubscription(new Subscription(subscriptionTypeEnum.name(), user.getId(), new Date()));
        }
    }

    /**
     * Fonctionne pas je pense
     *
     * @param subscription
     * @return
     */
    public boolean addSubscription(Subscription subscription) {
        boolean matching = findMatchingSubscription(subscription);

        if(matching) {
            //updateSubscription()
            // Existing object
            LOGGER.info("Subscription existing for user id " + subscription.getUserId() +
                    ": Type: " + subscription.getType() +
                    " (" + subscription.getDateDebut() +
                    " -> " + subscription.getDateFin() + ").");
        } else {
            // Initialisation des free (j'ai pas trouvé d'autres solution pour gérer chaque cas)
            if(subscription.getType().equals(SubscriptionTypeEnum.DESCRIPTION.name())) {
                subscription.setFree(descriptionFree);
            }
            if(subscription.getType().equals(SubscriptionTypeEnum.FRIEND.name())) {
                subscription.setFree(friendFree);
            }

            // Adding the object
            save(subscription);
            LOGGER.info("New subcription for user id " + subscription.getUserId() +
                    ": Type: " + subscription.getType() +
                    " (" + subscription.getDateDebut() +
                    " -> " + subscription.getDateFin() + ").");
        }
        return matching;
    }

    public boolean findMatchingSubscription(Subscription subscription) {

        return subscriptionRepository.findSubscriptionByTypeAndUserId(
                subscription.getType(), subscription.getUserId()) != null;
    }

    public void updateSubscription(String userId, SubscriptionType subscriptionType) {

        Subscription subscription = findSubscription(userId, subscriptionType.getType());

        // Update subscription dates
        updateSubscriptionDates(subscription, subscriptionType);

        // Update
        update(subscription);
    }

    private void updateSubscriptionDates(Subscription subscription, SubscriptionType subscriptionType) {
        Date date;
        String infoBegin;

        // Si périmé
        if (subscription.getDateFin().compareTo(new Date()) < 0) {
            date = new Date();
            subscription.setDateDebut(date);
            infoBegin = subscription.getDateDebut().toString();
        } else { // sinon
            date = subscription.getDateFin();
            infoBegin = subscription.getDateDebut().toString() + "(unchanged)";
        }
        if ("month".equals(subscriptionType.getUnit())) {
            subscription.setDateFin(
                    DateUtils.addMonths(date,
                            Integer.parseInt(subscriptionType.getLength())));
        }
        if ("year".equals(subscriptionType.getUnit())) {
            subscription.setDateFin(
                    DateUtils.addYears(date,
                            Integer.parseInt(subscriptionType.getLength())));
        }
        LOGGER.info("Update of userId " + subscription.getUserId() + " subscription '" + subscription.getType() +
                "'. (" + infoBegin + " -> " + subscription.getDateFin() + ").");
    }

    public boolean deleteSubscription(Subscription subscription) {

        if(findMatchingSubscription(subscription)) {
            delete(subscription);
            return true;
        } else {
            LOGGER.warning("No subscription matching to deleteMomentFromAlbum.");
            return false;
        }
    }

    public boolean deleteUserSubscriptions(String userId) {

        List<Subscription> subscriptionList = subscriptionRepository.findByUserId(userId);
        if(subscriptionList != null && !subscriptionList.isEmpty()) {
            for(Subscription subscription: subscriptionList) {
                deleteSubscription(subscription);
            }
            return true;
        }
        return false;
    }

    private void save(Subscription subscription) {
        // Set to null not to erase another object with the same Id (new object)
        subscription.setId(null);
        LOGGER.info("Saving new subscription" + subscription.toString() + ".");
        subscriptionRepository.save(subscription);
    }

    private void update(Subscription subscription) {
        LOGGER.info("Updating subscription" + subscription.toString() + ".");
        subscriptionRepository.save(subscription);
    }

    private void delete(Subscription subscription) {
        LOGGER.info("Deleting subscription" + subscription.toString() + ".");
        subscriptionRepository.delete(subscription);
    }
}