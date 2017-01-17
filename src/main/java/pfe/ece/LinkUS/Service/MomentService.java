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

    public boolean addMomentToAlbum(Album album, Moment moment) {

        // On cherche si le moment existe, si non: on l'ajoute
        if(!album.getMoments().contains(moment)) {
            LOGGER.info("Adding moment: " + moment + " to album: " + album.getName());
            album.getMoments().add(moment);
            return true;
        }
        return false;
    }

    /**
     *
     * @param album
     * @param momentId
     * @return
     *      moment or null
     */
    public Moment findMomentInAlbum(Album album, String momentId) {
        for (Moment moment: album.getMoments()) {
            if(moment.getId().equals(momentId)){
                return moment;
            }
        }
        return null;
    }

    public boolean deleteMomentFromAlbum(Album album, Moment moment) {
        return deleteMomentFromAlbum(album, moment.getId());
    }

    public boolean deleteMomentFromAlbum(Album album, String momentId) {

        boolean bool = false;
        Moment foundMoment = null;
        for(Moment moment: album.getMoments()) {
            if(moment.getId().equals(momentId)) {
                foundMoment = moment;
            }
        }
        if(foundMoment != null) {
            album.getMoments().remove(foundMoment);
            bool = true;
        }

        // Si il n'y a plus de moments on en rajoute 1 par défaut
        if(album.getMoments().isEmpty()) {
            LOGGER.info("Removing moment: " + momentId + " from album: " + album.getName());
            album.getMoments().add(newDefaultMoment());
        }
        return bool;
    }

    public void checkAllMomentDataRight(Album album, String userId) {

        InstantService instantService = new InstantService();
        for(Moment moment: album.getMoments()) {

            // Check if instant are available for the user
            instantService.checkAllInstantDataRight(moment, userId);

            if (moment.getInstantList().isEmpty()) {
                deleteMomentFromAlbum(album, moment);
            }
        }
    }

    public void checkAllMomentDataNews(Album album, boolean news, String userId) {

        for(Moment moment: album.getMoments()) {
            if (moment.isNews() != news) {
                deleteMomentFromAlbum(album, moment);
            }
        }
    }
}
