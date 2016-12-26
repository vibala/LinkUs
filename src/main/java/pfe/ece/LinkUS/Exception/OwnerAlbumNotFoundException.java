package pfe.ece.LinkUS.Exception;

/**
 * Created by DamnAug on 16/10/2016.
 */
public class OwnerAlbumNotFoundException extends RuntimeException{

    public OwnerAlbumNotFoundException(String userId) {
        super("Couldn't find albums for user '"+ userId + "'.");
    }
}
