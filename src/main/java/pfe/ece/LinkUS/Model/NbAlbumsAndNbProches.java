package pfe.ece.LinkUS.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by DamnAug on 26/01/2017.
 */
public class NbAlbumsAndNbProches {

    private int nbProches;
    private int nbAlbumsOwned;

    public NbAlbumsAndNbProches() {
    }

    public NbAlbumsAndNbProches(int nbProches, int nbAlbumsOwned) {
        this.nbProches = nbProches;
        this.nbAlbumsOwned = nbAlbumsOwned;
    }

    @Override
    public String toString() {
        String str = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            str = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public int getNbProches() {
        return nbProches;
    }

    public void setNbProches(int nbProches) {
        this.nbProches = nbProches;
    }

    public int getNbAlbumsOwned() {
        return nbAlbumsOwned;
    }

    public void setNbAlbumsOwned(int nbAlbumsOwned) {
        this.nbAlbumsOwned = nbAlbumsOwned;
    }
}
