package pfe.ece.LinkUS.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Exception.SubscriptionTypeNotFoundException;
import pfe.ece.LinkUS.Model.SubscriptionType;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.SubscriptionTypeRepository;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by DamnAug on 22/11/2016.
 */
@Service
public class SubscriptionTypeService {

    Logger LOGGER = Logger.getLogger("LinkUS.Service.SubscriptionTypeService");
    @Autowired
    SubscriptionTypeRepository subscriptionTypeRepository;

    public SubscriptionTypeService(SubscriptionTypeRepository subscriptionTypeRepository) {
        this.subscriptionTypeRepository = subscriptionTypeRepository;
    }

    public List<SubscriptionType> findAllSubscriptionType() {
        return subscriptionTypeRepository.findAll();
    }

    public SubscriptionType findSubscriptionTypeById(String id) {
        SubscriptionType subscriptionType = subscriptionTypeRepository.findOne(id);
        if (subscriptionType == null) {
            throw new SubscriptionTypeNotFoundException(id);
        }
        return subscriptionType;
    }

    public boolean addSubscriptionType(SubscriptionType subscriptionType) {
        boolean matching = findMatchingSubscriptionType(subscriptionType);

        if(matching) {
            // Existing object
            LOGGER.info("Existing subscriptionType id: " + subscriptionType.getId() +
                    ": Type: " + subscriptionType.getType() +
                    "Duree: " + subscriptionType.getLength()+subscriptionType.getUnit());
        } else {
            // Adding the object
            save(subscriptionType);
            LOGGER.info("SubscriptionType id: " + subscriptionType.getId() +
                    ": Type: " + subscriptionType.getType() +
                    "Duree: " + subscriptionType.getLength()+subscriptionType.getUnit());
        }
        return matching;
    }

    /**
     * Take the id object to findMomentInAlbum it, then update the new one
     * @param subscriptionTypeNew
     */
    public void updateSubscriptionType(SubscriptionType subscriptionTypeNew) {

        SubscriptionType subscriptionTypeOld = subscriptionTypeRepository.findOne(subscriptionTypeNew.getId());

        if (subscriptionTypeOld == null) {
            throw new SubscriptionTypeNotFoundException(subscriptionTypeNew.getId());
        }

        update(subscriptionTypeNew);
    }

    public boolean findMatchingSubscriptionType(SubscriptionType subscriptionType) {

        return subscriptionTypeRepository.findSubscriptionTypeByTypeAndLengthAndUnit(
                subscriptionType.getType(), subscriptionType.getLength(), subscriptionType.getUnit()) != null;
    }

    public void deleteSubscriptionType(SubscriptionType subscriptionType) {
        if(findMatchingSubscriptionType(subscriptionType)) {
            delete(subscriptionType);
        } else {
            LOGGER.warning("No subscription (id: " + subscriptionType.getId() + ") matching to deleteMomentFromAlbum.");
        }
    }

    private void save(SubscriptionType subscriptionType) {
        // Set to null not to erase another object with the same Id (new object)
        subscriptionType.setId(null);
        LOGGER.info("Saving new subscriptionType" + subscriptionType.toString());
        subscriptionTypeRepository.save(subscriptionType);
    }

    private void update(SubscriptionType subscriptionType) {
        LOGGER.info("Updating subscriptionType" + subscriptionType.toString());
        subscriptionTypeRepository.save(subscriptionType);
    }

    private void delete(SubscriptionType subscriptionType) {
        LOGGER.info("Deleting subscriptionType" + subscriptionType.toString());
        subscriptionTypeRepository.delete(subscriptionType);
    }

}
