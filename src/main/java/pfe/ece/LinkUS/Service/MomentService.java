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

    public void add(Album album, Moment moment) {

        // On cherche si le moment existe, si non: on l'ajoute
        if(!album.getMoments().contains(moment)) {
            LOGGER.info("Adding moment: " + moment);
            album.getMoments().add(moment);
        }
    }

    /**
     *
     * @param album
     * @param momentId
     * @return
     *      moment or null
     */
    public Moment find(Album album, String momentId) {
        for (Moment moment: album.getMoments()) {
            if(moment.getId().equals(momentId)){
                return moment;
            }
        }
        return null;
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
