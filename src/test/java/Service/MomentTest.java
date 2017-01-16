package Service;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pfe.ece.LinkUS.Model.Album;
import pfe.ece.LinkUS.Model.Moment;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.AlbumRepository;
import pfe.ece.LinkUS.Service.AlbumService;
import pfe.ece.LinkUS.Service.MomentService;

/**
 * Created by DamnAug on 06/01/2017.
 */
@Ignore
public class MomentTest {


    @Autowired
    AlbumRepository albumRepository;


    @Test
    public void deleteTest() {

        String userId = "zekfzefkjn";
        String momentId = "rzifuh";

        AlbumService albumService = new AlbumService(albumRepository);
        MomentService momentService = new MomentService();
        Album album = new Album();

        Moment moment = new Moment();
        moment.setId(momentId);
        album.getMoments().add(moment);

        Assert.assertTrue(album.getMoments().size() == 1);

        momentService.deleteMomentFromAlbum(album, momentId);

        Assert.assertTrue(album.getMoments().size() == 1);
        Assert.assertEquals(album.getMoments().get(0).getId(), "0");
        Assert.assertEquals(album.getMoments().get(0).getName(), "Default");

    }
}
