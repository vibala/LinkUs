package pfe.ece.LinkUS.Service;

import org.springframework.stereotype.Service;
import pfe.ece.LinkUS.Model.Album;
import pfe.ece.LinkUS.Model.Instant;
import pfe.ece.LinkUS.Model.Moment;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by DamnAug on 06/01/2017.
 */
@Service
public class MomentService {

    Logger LOGGER = Logger.getLogger("LinkUS.Service.MomentService");

    public Moment newDefaultMoment() {
        LOGGER.info("Creating new default moment.");
        Moment moment = new Moment();
        moment.setId("0");
        moment.setName("Default");
        setMainImageUrlDefault(moment);
        return moment;
    }

    public void setMainImageUrlDefault(Moment moment) {
        moment.setMainImageUrl("http://www.reynoldsam.com/wordpress/wp-content/themes/ram/_images/nophoto.jpg");
    }
/*
    public String createMomentSaveToAlbum(Album album, String name) {

        if(album != null) {
            Moment moment = createMoment(name, null);

            // Temporaire
            Instant instant = new Instant();
            instant.setName("pipi");
            InstantService instantService = new InstantService();
            instantService.addInstantToMoment(moment, instant);
            //

            MomentService momentService = new MomentService();
            momentService.addMomentToAlbum(album, moment);

            return moment.getId();
        }
        return null;
    }*/

    public Moment createMoment(String name, String timezone, ArrayList<Instant> instantList) {

        Moment moment = new Moment();

        moment.setName(name);
        moment.setTimeZone(timezone);
        moment.setPublishDate(new Date());

        if(instantList != null && !instantList.isEmpty()) {
            moment.setInstantList(instantList);
        }
        return moment;

    }

    public boolean addMomentToAlbum(Album album, Moment moment) {

        // On cherche si le moment existe, si non: on l'ajoute
        if(!album.getMoments().contains(moment)) {
            LOGGER.info("Adding moment: " + moment.getId() + " to album: " + album.getName());
            album.getMoments().add(moment);
            return true;
        }
        return false;
    }


/*    public String saveInstantToAlbumMoment(Album album, String momentId, Instant instant) {

        MomentService momentService = new MomentService();
        Moment moment = momentService.findMomentInAlbum(album, momentId);

        return saveInstantToAlbumMoment(moment, instant);
    }*/

/*    public String saveInstantToAlbumMoment(Moment moment, Instant instant) {

        InstantService instantService = new InstantService();

        instantService.addInstantToMoment(moment, instant);

        return instant.getId();
    }*/

    /**
     *
     * @param album
     * @param momentIdList
     * @return
     */
    public  List<Moment> findMomentsInAlbum(Album album, List<String> momentIdList) {
        List<Moment> momentList = new ArrayList<>();

        for (String momentId: momentIdList) {
            Moment moment = findMomentInAlbum(album, momentId);
            if(moment != null) {
                momentList.add(moment);
            }
        }
        return momentList;
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
        boolean bool = false;
        Moment foundMoment = null;
        for(Moment momentItr: album.getMoments()) {
            if(momentItr.equals(moment)) {
                foundMoment = momentItr;
            }
        }
        if(foundMoment != null) {
            album.getMoments().remove(foundMoment);
            bool = true;
        }

        // Si il n'y a plus de moments on en rajoute 1 par défaut
        if(album.getMoments().isEmpty()) {
            LOGGER.info("Removing moment: " + foundMoment.getId() + " from album: " + album.getName());
            album.getMoments().add(newDefaultMoment());
        }
        return bool;
    }

    public void checkAllMomentDataRight(Album album, String userId) {

        InstantService instantService = new InstantService();

        Iterator<Moment> iter = album.getMoments().iterator();
        while (iter.hasNext()) {
            Moment moment = iter.next();
            // Check if instant are available for the user
            instantService.checkAllInstantDataRight(moment, userId);

            if (moment.getInstantList().isEmpty()) {
                iter.remove();
            }
        }
    }

    public void checkAllMomentDataNews(Album album, boolean news, String userId) {

        Iterator<Moment> iter = album.getMoments().iterator();
        while (iter.hasNext()) {
            Moment moment = iter.next();

            if (moment.isNews() != news) {
                iter.remove();
            }
        }
    }

    public void setMainInstantUsingCotation(Moment moment) {

        Instant bestInstant = null;
        double bestCotation = -1;

        // si il y a des instants dans le moment
        if(moment.getInstantList() != null && !moment.getInstantList().isEmpty()) {
            for(Instant instant: moment.getInstantList()) {
                if(instant.getCotation() > bestCotation) {
                    bestCotation = instant.getCotation();
                    bestInstant = instant;
                }
            }
            moment.setMainInstant(bestInstant.getId());
            // best instant contient une image on rempli le moment
            if(bestInstant.getUrl() != null && !bestInstant.getUrl().equals("")) {
                moment.setMainImageUrl(bestInstant.getUrl());
            }
            // bestInstant ne contient pas d'image et moment pas d'url alors on rempli avec une image par défaut
            if((bestInstant.getUrl() == null || bestInstant.getUrl().equals("")) &&
                    (moment.getMainImageUrl() == null || moment.getMainImageUrl().equals(""))) {
                // si l'instant principal n'a pas de photo
                setMainImageUrlDefault(moment);
            }
        } else {
            // on fait rien pour moment.getMainIntant() au risque d'écraser des données

            if(moment.getMainImageUrl() == null || moment.getMainImageUrl().equals("")) {
                setMainImageUrlDefault(moment);
            }
        }
    }

    public boolean addUserToAllMomentsInstantWithRight(List<Moment> momentList, String userId, String right) {

        InstantService instantService = new InstantService();
        boolean bool = true;

        for(Moment moment: momentList) {
            if(!instantService.addUserToAllInstantIdRight(moment, right, userId)) {
                bool = false;
            }
        }
        return bool;
    }
}