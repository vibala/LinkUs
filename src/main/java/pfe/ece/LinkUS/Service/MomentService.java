package pfe.ece.LinkUS.Service;

import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Model.Album;
import pfe.ece.LinkUS.Model.Moment;

import java.util.logging.Logger;

/**
 * Created by DamnAug on 06/01/2017.
 */
@Service
public class MomentService {

    Logger LOGGER = Logger.getLogger("LinkUS.Controller.MomentService");

    public Moment newDefaultMoment() {
        LOGGER.info("Creating new default moment.");
        Moment moment = new Moment();
        moment.setId("0");
        moment.setName("Default");
        return moment;
    }

    public void delete(Album album, Moment moment) {
        delete(album, moment.getId());
    }

    public void delete(Album album, String momentId) {

        Moment foundMoment = null;
        for(Moment moment: album.getMoments()) {
            if(moment.getId().equals(momentId)) {
                foundMoment = moment;
            }
        }
        if(foundMoment != null) {
            album.getMoments().remove(foundMoment);
        }

        // Si il n'y a plus de moments on en rajoute 1 par défaut
        if(album.getMoments().isEmpty()) {
            album.getMoments().add(newDefaultMoment());
        }
    }
}
