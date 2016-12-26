package com.start_up.dev.apilinkus;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.start_up.dev.apilinkus.API.APILinkUS;
import com.start_up.dev.apilinkus.Model.Moment;

import java.io.ByteArrayOutputStream;


public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "Notification-Main";

    private Moment moment;
    private static final int PICK_IMAGE = 1;
    private ImageView imgView;
    private Bitmap bitmap;
    private APILinkUS api;
    private int albumRelativeTargeted=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api=new APILinkUS(BaseActivity.this);
        api.sendTokenNotification();
        setContentView(R.layout.activity_basic);
        setTitle("");
        /**********************Configuration*************************************/
        /*
        //[AUTOMATIQUE] Initialisation du Token Notification -> Se fait tout seul grace a MyFirebaseInstanceIdService.
        n'est appelé que si le token n'a pas changé.
        // Si tu veux savoir le token tu peux l'afficher avec les lignes suivantes en contactant le server FireBase
        String token = FirebaseInstanceId.getInstance().getToken();
        System.out.println(token);
        Log.d(TAG, "Token: " + token);*/

        /***********************Récupération des elements graphiques***************/
        imgView=(ImageView)  findViewById(R.id.imgView);
        //Initialisation de la toolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /************************Button Section***************************************/
        // --------- BUTTON - INTENT------ Vers le GridView (album)
        Button buttonGoAlbum =(Button) findViewById(R.id.intent_album_owned);

        buttonGoAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("aaaaaaaaaaaaaaaaaaa");
                Intent intent = new Intent(BaseActivity.this, AlbumActivity.class);
                //Start details activity
                startActivity(intent);
            }
        });

        Button buttonGoAlbumRead =(Button) findViewById(R.id.intent_album_read);
        buttonGoAlbumRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("bbbbbbbbbbbbbbbbbbbbbb");
                Intent intent = new Intent(BaseActivity.this, AlbumReadActivity.class);
                //Start details activity
                startActivity(intent);
            }
        });

        //-----------BUTTON - ACTION --- Send selected moment to Server -> Server side : upload then notification to readers
        FloatingActionButton buttonGoGallery = (FloatingActionButton) findViewById(R.id.intent_gallery_multiple_send);
        buttonGoGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BaseActivity.this, GalleryActivity.class);
                //Start details activity
                startActivity(intent);
            }
        });


        //-----------BUTTON - ACTION --- Send selected moment to Server -> Server side : upload then notification to readers
        FloatingActionButton buttonSend = (FloatingActionButton) findViewById(R.id.fab);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (moment==null) {
                    Snackbar.make(findViewById(R.id.main_snackbar), "No image selected",
                            Snackbar.LENGTH_SHORT)
                            .show();
                }else{
                    new APILinkUS().addMomentToMyAlbum(moment,"true");

                Snackbar.make(findViewById(R.id.main_snackbar), "Image sent for uploading",
                        Snackbar.LENGTH_SHORT)
                        .show();          }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * Permet de définir le menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present. R.menu.main <=> set le menu sur le fichier menu/main.xml
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.nav_gallery:
                try {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(
                            Intent.createChooser(intent, "Select Picture"),
                            PICK_IMAGE);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "merde",
                            Toast.LENGTH_LONG).show();
                    Log.e(e.getClass().getName(), e.getMessage(), e);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);
        // When an Image is picked
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
                && null != dataIntent) {
            Uri selectedImageUri = dataIntent.getData();
            String filePath = null;

            try {
                // OI FILE Manager
                String filemanagerstring = selectedImageUri.getPath();

                // MEDIA GALLERY
                String selectedImagePath = getPath(selectedImageUri);

                if (selectedImagePath != null) {
                    filePath = selectedImagePath;
                } else if (filemanagerstring != null) {
                    filePath = filemanagerstring;
                } else {
                    Toast.makeText(getApplicationContext(), "Unknown path",
                            Toast.LENGTH_LONG).show();
                    Log.e("Bitmap", "Unknown path");
                }

                String[] parse = filePath.split("/");
                String nameAndExtension = parse[parse.length-1];

                if (filePath != null && nameAndExtension!=null) {//image.jpg
                    moment=new Moment();
                    moment.setName(nameAndExtension);
                    decodeFile(filePath);
                } else {
                    bitmap = null;
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Internal error",
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }

    }
    private String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    private void decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, bos);
        moment.setImgByte(bos.toByteArray());
        imgView.setImageBitmap(bitmap);

    }


}
