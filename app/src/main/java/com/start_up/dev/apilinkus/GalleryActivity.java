package com.start_up.dev.apilinkus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.start_up.dev.apilinkus.Adapter.GalleryAdapter;
import com.start_up.dev.apilinkus.Adapter.GalleryFolderAdapter;
import com.start_up.dev.apilinkus.Adapter.RecyclerViewFolderItem;
import com.start_up.dev.apilinkus.Adapter.RecyclerViewItem;
import com.start_up.dev.apilinkus.Listener.RecyclerViewGalleryClickListener;
import com.start_up.dev.apilinkus.Listener.RecyclerViewGalleryFolderClickListener;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Huong on 12/12/2016.
 */

public class GalleryActivity extends AppCompatActivity implements RecyclerViewGalleryFolderClickListener,RecyclerViewGalleryClickListener {
    private static final String TAG = GalleryActivity.class.getSimpleName();
    public static String current_directory;
    private ProgressBar mProgressBar;
    private ArrayList<RecyclerViewItem> currentList=new ArrayList<>();
    private RecyclerView currentRecyclerView ;
    private GalleryAdapter currentAdapter ;
    private ArrayList<RecyclerViewItem> selectedList=new ArrayList<>();
    private RecyclerView selectedRecyclerView ;
    private GalleryAdapter selectedAdapter ;
    private ArrayList<RecyclerViewFolderItem> folderList=new ArrayList<>();
    private RecyclerView folderRecyclerView ;
    private GalleryFolderAdapter folderAdapter ;
    private String ExternalStorageDirectoryPath;



    //Je sais pas si ca clear vraiment mais on post sur internet dit que oui http://stackoverflow.com/questions/19287346/clear-a-bitmap-from-arraytlist-using-recycle-not-working-android
    private void clearCurrentList(){
        if(currentList!=null) {
            currentList.clear();
        }

        if(currentAdapter!=null)
            currentAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_folder);
        ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();

        mProgressBar = (ProgressBar) findViewById(R.id.gallery_progressBar);

        mProgressBar.setVisibility(View.VISIBLE);

        initViews(savedInstanceState);

        mProgressBar.setVisibility(View.GONE);
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putSerializable("selectedList", selectedList);
        savedInstanceState.putSerializable("currentList", currentList);
        savedInstanceState.putSerializable("folderList", folderList);
        savedInstanceState.putString("current_directory", current_directory);
        // etc.
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
        setGalleryDisplay(current_directory);
    }
    private void initViews(Bundle savedInstanceState){
        clearCurrentList();

        current_directory=ExternalStorageDirectoryPath+"/Pictures/Messenger/";
        currentAdapter = new GalleryAdapter(this,currentList,"current");
        if (savedInstanceState == null)
            folderList = getDirectoryWithImages(ExternalStorageDirectoryPath);
        else
            folderList = (ArrayList<RecyclerViewFolderItem>) savedInstanceState.getSerializable("folderList");

        folderAdapter = new GalleryFolderAdapter(this,folderList);
        selectedAdapter = new GalleryAdapter(this,selectedList,"selected");

        if (savedInstanceState != null) {
            selectedList = (ArrayList<RecyclerViewItem>) savedInstanceState.getSerializable("selectedList");
            currentList = (ArrayList<RecyclerViewItem>) savedInstanceState.getSerializable("currentList");
            current_directory = savedInstanceState.getString("current_directory");
            System.out.println(selectedAdapter);
            System.out.println(selectedList);
            selectedAdapter.setGridData(selectedList);
            currentAdapter.setGridData(currentList);
            folderAdapter.setGridData(folderList);
        }
        currentRecyclerView = (RecyclerView) findViewById(R.id.gallery_current_recycler_view);
        Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        if( display.getRotation()== Surface.ROTATION_0)
            currentRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        else
            currentRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),4));
        currentRecyclerView.setItemAnimator(new DefaultItemAnimator());
        currentRecyclerView.setAdapter(currentAdapter);

        folderRecyclerView = (RecyclerView) findViewById(R.id.gallery_list_folder);
        folderRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1, GridLayoutManager.HORIZONTAL, false));
        folderRecyclerView.setItemAnimator(new DefaultItemAnimator());
        folderRecyclerView.setAdapter(folderAdapter);

        selectedRecyclerView = (RecyclerView) findViewById(R.id.gallery_selected_list_recycler_view);
        selectedRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1, GridLayoutManager.HORIZONTAL, false));
        selectedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        selectedRecyclerView.setAdapter(selectedAdapter);

        setGalleryDisplay(current_directory);

        //-----------BUTTON - ACTION --- Send selected photos to Server -> Server side : upload then notification to readers
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.send_gallery);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(needRequirementBeforeContinue()) return;
                Intent intent = new Intent(GalleryActivity.this, SendMomentActivity.class);
                intent.putExtra("selectedList",selectedList);
                intent.putExtra("albumId",getIntent().getStringExtra("albumId"));
                clearCurrentList();

                //Start details activity
                startActivity(intent);
            }
        });
    }

    public boolean needRequirementBeforeContinue() {
        if (selectedList == null | selectedList.size() == 0){
            Toast.makeText(getApplicationContext(), "Vous devez selectionner au moins 1 photo", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
    private ArrayList<RecyclerViewFolderItem> getDirectoryWithImages(String path) {
        ArrayList<RecyclerViewFolderItem> folderListLocal=new ArrayList<>();
        File targetDirector = new File(path);
        File[] files_list = targetDirector.listFiles();
        if(files_list != null){
            for (File file : files_list) {
                if (file.isDirectory())
                    folderListLocal.addAll(getDirectoryWithImages(file.getAbsolutePath()));
                else {
                    String absolutePath = file.getAbsolutePath();
                    String suffix = absolutePath.substring(absolutePath.lastIndexOf('.') + 1).toLowerCase();
                    if (!file.isDirectory())
                        if (suffix.length() == 0 | suffix.equals("svg"))
                            continue;

                    String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
                    if (mime == null)
                        continue;
                    if (mime.contains("image")) {
                        String folderName = absolutePath.substring(0, absolutePath.lastIndexOf('/') + 1);
                        String[] names=folderName.split("/");
                        //on enleve les dossiers caché et les petits ss dossier. si la permiere lettre du dossier n'est pas une maj on considere que ce dossier ne contient pas des photos
                        if (!folderName.contains(".") && Character.isUpperCase((names.length>1?names[names.length-1]:names[0]).charAt(0))) {
                            boolean exist = false;
                            ////System.out.println("Ajout du dossier: " + folderName);
                            for (RecyclerViewFolderItem item : folderListLocal)
                                if (item.getPath().equals(folderName)) {
                                    exist = true;
                                    break;
                                }
                            if (!exist)
                                folderListLocal.add(new RecyclerViewFolderItem((absolutePath.length() > 1 ? absolutePath.substring(0, absolutePath.lastIndexOf('/') + 1) : "/")));

                        }
                    }
                }
            }
        }

        return folderListLocal;
    }

    private void setGalleryDisplay(String directory){
        clearCurrentList();
        File fileTest = new File(directory);
        if (fileTest.exists() && fileTest.isDirectory()) {
            current_directory=directory;
            String targetPath = directory;
            //System.out.println(targetPath);
            try {
                File targetDirector = new File(targetPath);
                File[] files_list = targetDirector.listFiles();
                for (File file : files_list) {
                    if(!file.isDirectory()) {
                        boolean exist = false;
                        String absPath=file.getAbsolutePath();
                        for (RecyclerViewItem item : selectedList) {
                            if (item.getId().equals(absPath)) {
                                exist = true;
                                break;
                            }
                        }
                        currentList.add(new RecyclerViewItem(absPath, absPath.substring(absPath.lastIndexOf('/') +1), file, exist));
                    }
                }
                currentAdapter.setGridData(currentList);
                Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_SHORT).show();
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "Le chemin n'est pas valide.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Le dossier n'existe pas.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void recyclerViewGalleryListClicked(View viewCheckBox,  String path) {
        RecyclerViewItem item=null;
        for(RecyclerViewItem item_tmp : currentList){
            if(item_tmp.getId().equals(path))
                item=item_tmp;
        }
        if(item!=null){
            //System.out.println("name "+item.getName());
            if(((CheckBox)viewCheckBox).isChecked()){
                item.setChecked(true);
                selectedList.add(item);
                System.out.println("add "+item.getId());
            }
            else{
                item.setChecked(false);
                for (RecyclerViewItem i : selectedList) {
                    if (i.getId().equals(path)) {
                        i.setChecked(false);
                        //l'item de la selected list et current list ne soit pas toujours les meme a cause des cahngement de directory
                        item.setChecked(false);
                        selectedList.remove(i);
                        //System.out.println("remove "+i.getName());
                        break;
                    }
                }
                //System.out.println("remove "+item.getName());

            }
            currentAdapter.setGridData(currentList);
        }
        else if(!((CheckBox)viewCheckBox).isChecked()) {
            for (RecyclerViewItem i : selectedList) {
                if (i.getId().equals(path)) {
                    i.setChecked(false);
                    selectedList.remove(i);
                    //System.out.println("remove "+i.getName());
                    break;
                }
            }

        }


        selectedRecyclerView.scrollToPosition(0);
        selectedAdapter.setGridData(selectedList);


    }

    @Override
    public void recyclerViewFolderListClicked(View v, String path) {
        //System.out.println(path);
        setGalleryDisplay(path);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        clearCurrentList();

        finish();
    }




    private void clearFolderList(){
        if(folderList!=null) {
            folderList.clear();
        }

        if(folderAdapter!=null)
            folderAdapter.notifyDataSetChanged();

    }

}
