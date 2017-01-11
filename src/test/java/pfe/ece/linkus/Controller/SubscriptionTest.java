package pfe.ece.linkUS.Controller;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import pfe.ece.LinkUS.Controller.SubscriptionController;
import pfe.ece.LinkUS.Model.Subscription;

/**
 * Created by DamnAug on 23/11/2016.
 */
@Ignore
public class SubscriptionTest {

    @Test
    public void findSubscriptionByIdTest_OK() {

        String subId = "99999";
        String subType = "description";
        String subUserId = "19999";

        SubscriptionController subscriptionController = new SubscriptionController();

        subscriptionController.addSubscription(new Subscription(subId, subType, subUserId));

        Subscription subscription = subscriptionController.findSubscriptionById(subId);

        Assert.assertNotNull(subscription);
        Assert.assertEquals(subscription.getType(), subType);
        Assert.assertEquals(subscription.getId(), subId);
        Assert.assertEquals(subscription.getUserId(), subUserId);
    }

    @Test
    public void findSubscriptionByIdTest_KO() {

        SubscriptionController subscriptionController = new SubscriptionController();

        Assert.assertNull(subscriptionController.findSubscriptionById("notAId"));
    }
}
