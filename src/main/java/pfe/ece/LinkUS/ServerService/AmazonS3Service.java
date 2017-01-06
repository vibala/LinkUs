package pfe.ece.LinkUS.ServerService;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.GetSessionTokenRequest;
import com.amazonaws.services.securitytoken.model.GetSessionTokenResult;
import com.amazonaws.util.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Huong on 11/12/2016.
 */
public class AmazonS3Service {
    private AmazonS3Client s3Client;
    public static String bucketName = "linkusmediastorage";
    private String awsAccessKey="AKIAJUQP32RDZOUXQIDQ";
    private String awsSecretKey="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";

    public AmazonS3Service(){

        s3Client = initNoToken();
        /*
        Utiliser plus tard le temporary Access avec les credentials non affiché dans lecode mais stocké sur la machine.
        Les credentials doivent être stocké dans ... cf  http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html
        s3Client = initTemporaryAccess();
         */
    }

    public AmazonS3Client initNoToken(){
        AmazonS3Client client = new AmazonS3Client(new BasicAWSCredentials(awsAccessKey, awsSecretKey));
        return client;
    }


    //Fonctionne mais pour la portabilité du code on prend le normal (sinon faut que les gens crééent .aws puis mettent les credential dans le bon dossier
    public AmazonS3Client initTemporaryAccess(){
        //-----------------AMAZON INIT------------------
        AWSSecurityTokenServiceClient stsClient = new AWSSecurityTokenServiceClient(new ProfileCredentialsProvider());
        // Start a session.
        GetSessionTokenRequest getSessionTokenRequest = new GetSessionTokenRequest();
        // Following duration can be set only if temporary credentials are requested by an IAM user.
        getSessionTokenRequest.setDurationSeconds(900);

        GetSessionTokenResult sessionTokenResult = stsClient.getSessionToken(getSessionTokenRequest);
        Credentials sessionCredentials = sessionTokenResult.getCredentials();
        System.out.println("Session Credentials: " + sessionCredentials.toString());

        // Package the session credentials as a BasicSessionCredentials
        // object for an S3 client object to use.
        BasicSessionCredentials basicSessionCredentials = new BasicSessionCredentials(sessionCredentials.getAccessKeyId(),sessionCredentials.getSecretAccessKey(),sessionCredentials.getSessionToken());
        AmazonS3Client client = new AmazonS3Client(basicSessionCredentials);
        return client;
    }


    public String generatefileS3Name(String fileName){
        System.out.println(fileName);

        String randomString = UUID.randomUUID().toString();
        String uniqueTimeStamp=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String random=randomString+uniqueTimeStamp;
        String fileS3Name=random+fileName;

        /*
        $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ TODO
        Vérifier que cette url n'exite pas dans AmazonS3
         */

    return fileS3Name;
}

    /**
     * Upload from a byte array
     * @param fileByte
     * @param fileName
     * @param contentType "image/*" "application/pdf"
     * @throws IOException
     */
    public void  uploadFileByte(byte[] fileByte,String fileName,String contentType) throws IOException {

        InputStream stream = new ByteArrayInputStream(fileByte);
    try {
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(fileByte.length);
        meta.setContentType(contentType);
        s3Client.putObject(bucketName, fileName, stream, meta);
        //On rend public (commenter l'ACL pour privé)
        s3Client.setObjectAcl(bucketName, fileName, CannedAccessControlList.PublicRead);

    } catch (AmazonServiceException ase) {
        System.out.println("Error Message:    " + ase.getMessage());
        System.out.println("HTTP Status Code: " + ase.getStatusCode());
        System.out.println("AWS Error Code:   " + ase.getErrorCode());
        System.out.println("Error Type:       " + ase.getErrorType());
        System.out.println("Request ID:       " + ase.getRequestId());
    } catch (AmazonClientException ace) {
        System.out.println("Error Message: " + ace.getMessage());
    } finally {
        if (stream != null) {
            stream.close();
        }
    }
}















//Unused for the moment

    /**
     * Upload from a file saved on the server
     *Code a ajouter dans le controller
     *
     * private static final String tmpDirectory = "C:\\Users\\Huong\\Desktop\\b\\Depot_uploading";
     *
     *
     *
     * String AbsolutePathfile=tmpDirectory+"\\"+fileS3Name;

     //-----------------RECCUPERATION DE LA PHOTO en jpg
     // convert byte array back to BufferedImage
     InputStream in = new ByteArrayInputStream(moment.getImgByte());
     BufferedImage bImageFromConvert = null;
     try {
     bImageFromConvert = ImageIO.read(in);
     ImageIO.write(bImageFromConvert, "jpg", new File(AbsolutePathfile));
     } catch (IOException e) {
     e.printStackTrace();
     return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
     }
     in.close();

     * @param AbsolutePathfile
     * @param fileS3Name
     * @throws IOException
     */
    public void  uploadFileOnServer(String AbsolutePathfile,String fileS3Name) throws IOException {
    FileInputStream stream = new FileInputStream(AbsolutePathfile);
    byte[] contentLengthbyte = null;
    try {
        contentLengthbyte = IOUtils.toByteArray(stream);
    } catch (IOException e) {
        e.printStackTrace();
    }

    stream.close();
    stream = new FileInputStream(AbsolutePathfile);
    System.out.println(contentLengthbyte.length);

    Long contentLength = Long.valueOf(contentLengthbyte.length);

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(contentLength);

    try {
        //Public
        s3Client.putObject(new PutObjectRequest(bucketName, fileS3Name, stream, metadata).withCannedAcl(CannedAccessControlList.PublicRead));
        //Pas public
        //s3Client.putObject(new PutObjectRequest(bucketName, keyNameUpload, stream, metadata));

    } catch (AmazonServiceException ase) {
        System.out.println("Error Message:    " + ase.getMessage());
        System.out.println("HTTP Status Code: " + ase.getStatusCode());
        System.out.println("AWS Error Code:   " + ase.getErrorCode());
        System.out.println("Error Type:       " + ase.getErrorType());
        System.out.println("Request ID:       " + ase.getRequestId());
    } catch (AmazonClientException ace) {
        System.out.println("Error Message: " + ace.getMessage());
    } finally {
        if (stream != null) {
            stream.close();
        }
    }
}
}
