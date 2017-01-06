package com.start_up.dev.apilinkus;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.start_up.dev.apilinkus.API.APILinkUS;
import com.start_up.dev.apilinkus.Model.Moment;
import com.start_up.dev.apilinkus.Tool.DataAdapterGallery;
import com.start_up.dev.apilinkus.Tool.GridItem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.valueOf;

/**
 * Created by Huong on 12/12/2016.
 */

public class GalleryActivity  extends AppCompatActivity implements View.OnLongClickListener{
    private static final String TAG = GalleryActivity.class.getSimpleName();
    private String directory="/Pictures/Messenger/";
    private Toolbar toolbar;
    private ProgressBar mProgressBar;
    private ArrayList<File> files;
    private DataAdapterGallery adapter ;
    private EditText gallery_path ;
    private RecyclerView recyclerView ;
    private FloatingActionButton gallery_path_icon ;
    private ArrayList<File> selection_list=new ArrayList<>();
    private int counter=0;
    private String ExternalStorageDirectoryPath;
    //private String FEED_URL = "http://javatechig.com/?json=get_recent_posts&count=45";

    // tuto selection multiple - checkbox
    public boolean is_in_action_mode=false;
    public TextView counter_text_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();
        mProgressBar = (ProgressBar) findViewById(R.id.gallery_progressBar);

        mProgressBar.setVisibility(View.VISIBLE);

        initViews();

        mProgressBar.setVisibility(View.GONE);
    }

    private void initViews(){

        gallery_path = (EditText) findViewById(R.id.gallery_set_path);
        gallery_path_icon=(FloatingActionButton) findViewById(R.id.gallery_set_path_icon);
        gallery_path_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_in_action_mode) {
                    clearActionMode();
                    adapter.notifyDataSetChanged();
                }
                String directoryTmp = valueOf(gallery_path.getText());
                setGalleryDisplay(directoryTmp);

            }


        });


        recyclerView = (RecyclerView)findViewById(R.id.gallery_card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        setGalleryDisplay(directory);



        // tuto selection multiple - TOOLbar
        toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        counter_text_view=(TextView) findViewById(R.id.counter_text);
        counter_text_view.setVisibility(View.GONE);

        //-----------BUTTON - ACTION --- Send selected photos to Server -> Server side : upload then notification to readers
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.send_gallery);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(File file : selection_list){

                    Moment moment=new Moment();
                    moment.setName(file.getName());
                    System.out.println(moment.getName());
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    //System.out.println(file.exists() + "!!");
                    //InputStream in = resource.openStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    try {
                        for (int readNum; (readNum = fis.read(buf)) != -1;) {
                            bos.write(buf, 0, readNum); //no doubt here is 0
                            //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                            System.out.println("read " + readNum + " bytes,");
                        }
                    } catch (IOException ex) {
                        ex.getMessage();
                    }
                    byte[] imgByte = bos.toByteArray();

                    moment.setImgByte(imgByte);
                    new APILinkUS().addMomentToMyAlbum(moment,"true");
                }

                if(selection_list.isEmpty())
                    Snackbar.make(findViewById(R.id.gallery_snackbar_and_fab), "No Image Selected", Snackbar.LENGTH_SHORT).show();
                else{
                    if(is_in_action_mode) { //Condition normalement inutile. Si c'est pas vide il est forcement en action mode
                        String plural = "s";
                        if (selection_list.size() == 1)
                            plural = "";
                        Snackbar.make(findViewById(R.id.gallery_snackbar_and_fab), "Image" + plural + " sent, check your album",
                                Snackbar.LENGTH_SHORT)
                                .show();
                        clearActionMode();
                        adapter.notifyDataSetChanged();
                    }
                }

            }
        });

    }

    // tuto selection multiple - toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_activity_gallery,menu);
        return true;
    }

    private void setGalleryDisplay(String directoryTmp){

        File fileTest = new File(ExternalStorageDirectoryPath + directoryTmp);
        if (fileTest.exists() && fileTest.isDirectory()) {
            directory = directoryTmp;

        String targetPath = ExternalStorageDirectoryPath+directory;
        System.out.println(targetPath);
            try {
                File targetDirector = new File(targetPath);
                File[] files_list = targetDirector.listFiles();
                files = new ArrayList<File>();
                for (File file : files_list) {
                    if(!file.isDirectory())
                        files.add(file);
                }
                adapter = new DataAdapterGallery(GalleryActivity.this, files);
                recyclerView.setAdapter(adapter);
                recyclerView.setHasFixedSize(true);
                Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG).show();
                adapter.notifyDataSetChanged();
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "Le chemin n'est pas valide.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Le dossier n'existe pas.", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onLongClick(View v) {
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_action_mode);
        counter_text_view.setVisibility(View.VISIBLE);
        is_in_action_mode=true;
        adapter.notifyDataSetChanged();
        //add home button to toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    public void prepareSelection(View view, int position){
        if(((CheckBox)view).isChecked()){

            selection_list.add(files.get(position));
            counter=counter+1;
            updateCounter(counter);
        }
        else{
            selection_list.remove(files.get(position));
            counter=counter-1;
            updateCounter(counter);
        }
    }

    public void updateCounter(int counter){
        if(counter==0){
            counter_text_view.setText("0 item selected");
        }
        else{
            counter_text_view.setText(counter + "items selected");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.item_delete){

            DataAdapterGallery adapterGallery_bis=(DataAdapterGallery)adapter;
            System.out.println("ezfzef"+selection_list);
            adapterGallery_bis.updateAdapter(selection_list);
            clearActionMode();
        }
        else if(item.getItemId()==android.R.id.home){
            clearActionMode();
            adapter.notifyDataSetChanged();
        }
        return true;
    }

    public void clearActionMode(){
        is_in_action_mode=false;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_activity_gallery);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        counter_text_view.setVisibility(View.GONE);
        counter_text_view.setText(" 0 item selected");
        counter=0;
        selection_list.clear();
    }

    @Override
    public void onBackPressed(){
        if(is_in_action_mode){
            clearActionMode();
            adapter.notifyDataSetChanged();
        }
        else{
            super.onBackPressed();
        }
    }
}


