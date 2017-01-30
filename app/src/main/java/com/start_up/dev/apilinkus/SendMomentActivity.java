package com.start_up.dev.apilinkus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Dimension;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.start_up.dev.apilinkus.API.APIGetListFriend_Observer;
import com.start_up.dev.apilinkus.API.APIGetListGroupFriend_Observer;
import com.start_up.dev.apilinkus.API.APILinkUS;
import com.start_up.dev.apilinkus.API.APIPostMoment_Observer;
import com.start_up.dev.apilinkus.Adapter.ImageAndTextListAdapter;
import com.start_up.dev.apilinkus.Adapter.MiniCarouselAdapter;
import com.start_up.dev.apilinkus.Adapter.RecyclerViewItem;
import com.start_up.dev.apilinkus.Listener.RecyclerViewCircleClickListener;
import com.start_up.dev.apilinkus.Listener.RecyclerViewGalleryClickListener;
import com.start_up.dev.apilinkus.Model.Authentification;
import com.start_up.dev.apilinkus.Model.IdRight;
import com.start_up.dev.apilinkus.Model.Instant;
import com.start_up.dev.apilinkus.Model.KeyValue;
import com.start_up.dev.apilinkus.Model.Moment;
import com.start_up.dev.apilinkus.Tool.LockableScrollView;
import com.start_up.dev.apilinkus.Tool.MatrixCoordinates;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Huong on 12/12/2016.
 */

public class SendMomentActivity extends AppCompatActivity implements APIGetListGroupFriend_Observer,APIGetListFriend_Observer, RecyclerViewCircleClickListener,RecyclerViewGalleryClickListener,APIPostMoment_Observer {
    private static final String TAG = SendMomentActivity.class.getSimpleName();
    private ArrayList<RecyclerViewItem> friendList=new ArrayList<>();
    private RecyclerView friendRecyclerView ;
    private ImageAndTextListAdapter friendAdapter ;
    private ArrayList<RecyclerViewItem> imageList=new ArrayList<>();
    private RecyclerView imageRecyclerView ;
    private MiniCarouselAdapter imageAdapter ;
    private ArrayList<RecyclerViewItem> friendOrGroupSelected=new ArrayList<>();
    private ImageView imgDisplayed;
    private APILinkUS api=new APILinkUS();
    private APIPostMoment_Observer activity = this;
    private Context mContext = this;


    private void clearDataFragment(){

        imageList.clear();
        friendList.clear();
        friendOrGroupSelected.clear();

        friendAdapter.notifyDataSetChanged();
        imageAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_moment);

        System.out.println("CREATE");
        initViews(savedInstanceState);


    }

public boolean landscape;
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        System.out.println("onConfigurationChanged");
        widthScreen = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
        heightScreen = getApplicationContext().getResources().getDisplayMetrics().heightPixels;

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            HEIGHT_IMG_DISPLAYED=(heightScreen/3.0f)*2.0f;

            landscape=true;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            HEIGHT_IMG_DISPLAYED=heightScreen/2.0f;

            landscape=false;
        }

        imgDisplayed.getLayoutParams().height = (int)HEIGHT_IMG_DISPLAYED;
        //On actualise la taille du bitmap
        setImageDisplay(uriDisplayed);

    }
    EditText descriptionText;
    //ScrollView scrollview;
// These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    Matrix bkpMatrix = new Matrix();

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    String savedItemClicked;
    private LockableScrollView scrollview;
    private void initViews(Bundle savedInstanceState){


        api.getListFriend(this);
        api.getListGroupFriend(this);
        //Intent
        Intent intent =getIntent();
         imageList=(ArrayList<RecyclerViewItem>) intent.getSerializableExtra("selectedList");


        friendAdapter = new ImageAndTextListAdapter(getApplicationContext(),friendList,this);
        friendRecyclerView = (RecyclerView) findViewById(R.id.send_moment_current_chosen_friend_list);
        friendRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1, GridLayoutManager.HORIZONTAL, false));
        friendRecyclerView.setItemAnimator(new DefaultItemAnimator());
        friendRecyclerView.setAdapter(friendAdapter);

        imageAdapter = new MiniCarouselAdapter(this,imageList,"file");
        imageRecyclerView = (RecyclerView) findViewById(R.id.send_moment_pictures);
        imageRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1, GridLayoutManager.HORIZONTAL, false));
        imageRecyclerView.setItemAnimator(new DefaultItemAnimator());
        imageRecyclerView.setAdapter(imageAdapter);

        scrollview=(LockableScrollView)  findViewById(R.id.send_moment_scroll_image_displayed);
        imgDisplayed=(ImageView) findViewById(R.id.send_moment_image_displayed);
        widthScreen = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
        heightScreen = getApplicationContext().getResources().getDisplayMetrics().heightPixels;
        /*scrollview=(ScrollView) findViewById(R.id.send_moment_scroll_image_displayed);
        scrollview.getLayoutParams().height= height/2;*/
        //ON SUPPOSE QU IL EST EN PORTRAIT au départ... TODO
        if (widthScreen>heightScreen) {
            HEIGHT_IMG_DISPLAYED=heightScreen/3.0f*2.0f;
            landscape=true;
        } else {
            HEIGHT_IMG_DISPLAYED=heightScreen/2.0f;
            landscape=false;
        }
        imgDisplayed.getLayoutParams().height = (int)HEIGHT_IMG_DISPLAYED;
        imgDisplayed.setBackgroundColor(Color.BLACK);


        imgDisplayed.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                ImageView img = (ImageView) v;
                dumpEvent(event);
                // Handle touch events here...
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        savedMatrix.set(matrix);
                        start.set(event.getX(), event.getY());
                        Log.d(TAG, "mode=DRAG");
                        mode = DRAG;
                        scrollview.setScrollingEnabled(false);
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        oldDist = spacing(event);
                        Log.d(TAG, "oldDist=" + oldDist);
                        if (oldDist > 10f) {
                            savedMatrix.set(matrix);
                            midPoint(mid, event);
                            mode = ZOOM;
                            Log.d(TAG, "mode=ZOOM");
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        scrollview.setScrollingEnabled(true);
                    case MotionEvent.ACTION_POINTER_UP:
                        scrollview.setScrollingEnabled(true);
                        mode = NONE;
                        Log.d(TAG, "mode=NONE");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            // ...
                            bkpMatrix.set(matrix);

                            matrix.set(savedMatrix);
                            float translateX=event.getX() - start.x;
                            float translateY= event.getY() - start.y;

                           /* System.out.println("widthScreen "+widthScreen);
                            System.out.println("translateX "+translateX);

                            System.out.println("heightScreen "+heightScreen);
                            System.out.println("translateY "+translateY);*/
                            if(vertical)
                                matrix.postTranslate(0,translateY);
                            else
                                matrix.postTranslate(translateX,0);
                            float matrixX=MatrixCoordinates.getXValueFromMatrix(matrix);
                            float matrixY=MatrixCoordinates.getYValueFromMatrix(matrix);
                            MatrixCoordinates.logImageViewMatrixInfos(matrix,img);

                            if(vertical) {
                                if(matrixY<(HEIGHT_IMG_DISPLAYED-bitmapDisplayed.getHeight())||matrixY>0){
                                    matrix.set(bkpMatrix);
                                }
                            }
                            else{
                            if(matrixX<(widthScreen-bitmapDisplayed.getWidth()) || matrixX>0){
                                matrix.set(bkpMatrix);
                            }
                            }

                        } else if (mode == ZOOM) {
                           /* float newDist = spacing(event);
                            Log.d(TAG, "newDist=" + newDist);
                            if (newDist > 10f) {
                                bkpMatrix.set(matrix);
                                matrix.set(savedMatrix);
                                float scale = newDist / oldDist;

                                matrix.postScale(scale, scale, mid.x, mid.y);

                                float matrixWidth=MatrixCoordinates.getWidthFromMatrix(matrix,img);
                                float matrixHeight=MatrixCoordinates.getHeightFromMatrix(matrix,img);
                                if(matrixWidth<200 || matrixWidth>widthScreen*2.0 || matrixHeight<200 || matrixHeight>heightScreen*2.0){
                                    matrix.set(bkpMatrix);
                                }
                            }*/
                        }
                        break;
                }

                img.setImageMatrix(matrix);
                return true;
        }
        });
        System.out.println(imageList.get(0).getId());
        //OnSaveState

        setImageDisplay(imageList.get(0).getId());
        System.out.println(uriDisplayed);
        descriptionText=(EditText) findViewById(R.id.send_moment_description);
        descriptionText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    //Clear focus here from edittext
                    descriptionText.setCursorVisible(false);
                }
                return false;
            }
        });
        descriptionText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                descriptionText.setCursorVisible(true);

                return false;
            }
        });
        //-----------BUTTON - ACTION --- Send selected photos to Server -> Server side : upload then notification to readers
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.send_gallery);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //On verifie que l'utilisateur a renseigné correctement le formulaire
                if(needRequirementBeforeContinue())
                    return;

                String timeZone = Calendar.getInstance().getTimeZone().getID();

                ArrayList<Instant> listInstant=new ArrayList<Instant>();
                for( RecyclerViewItem item: imageList) {
                    File file = item.getFile();
                    Instant instant = new Instant();
                    instant.setName(file.getName());

                    instant.setPublishDate(new Date());
                    instant.setTimeZone(timeZone);

                    System.out.println(instant.getName());
  /*                  FileInputStream fis = null;
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
                        for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                            bos.write(buf, 0, readNum); //no doubt here is 0
                            //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                            System.out.println("read " + readNum + " bytes,");
                        }
                    } catch (IOException ex) {
                        ex.getMessage();
                    }
                    byte[] imgByte = bos.toByteArray();
*/

                    Bitmap bmp = getBitmapResizeMaxFromFile(file.getAbsolutePath());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] imgByte = stream.toByteArray();
                    instant.setImgByte(imgByte);
                    ArrayList<IdRight> idRights = new ArrayList<>();
                    IdRight idRight = new IdRight("LECTURE");
                    ArrayList<String> groupIdList = new ArrayList<>();
                    ArrayList<String> userIdList = new ArrayList<>();
                    for (RecyclerViewItem friendOrGroup : friendOrGroupSelected) {
                        if (friendOrGroup.getType().equals("my_group_friend"))
                            groupIdList.add(friendOrGroup.getId());
                        if (friendOrGroup.getType().equals("my_friend"))
                            userIdList.add(friendOrGroup.getId());
                    }

                    idRight.setGroupIdList(groupIdList);
                    idRight.setUserIdList(userIdList);
                    idRights.add(idRight);
                    instant.setIdRight(idRights);

                    listInstant.add(instant);
                }
                    Moment moment=new Moment();
                    moment.setName("Appli Android: Titre_Moment_A_DEFINIR");
                    ArrayList<KeyValue> descriptionsList=new ArrayList<KeyValue>();
                    descriptionsList.add(new KeyValue("Description",descriptionText.getText().toString()));
                    moment.setDescriptionsList(descriptionsList);

                    moment.setInstantList(listInstant);

                    moment.setTimeZone(timeZone);
                    moment.setPublishDate(new Date());

                    System.out.println("Moment to string " + moment.toString());
                    Log.d(TAG,"List instant size " + listInstant.size());
                    if(getIntent() != null){
                        new APILinkUS().addMomentToMyAlbum(moment,getIntent().getStringExtra("albumId"),"true",activity,mContext);
                    }

                }

        });
    }
    private void dumpEvent(MotionEvent event) {
        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
                "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(
                    action >> MotionEvent.ACTION_POINTER_INDEX_SHIFT);
            sb.append(")");
        }
        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }
        sb.append("]");
        Log.d(TAG, sb.toString());
    }

    /** Determine the space between the first two fingers */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    /** Calculate the mid point of the first two fingers */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
    int origin_x ;
    int origin_y;
    public boolean needRequirementBeforeContinue() {
        if (friendOrGroupSelected == null | friendOrGroupSelected.size() == 0){
            Toast.makeText(getApplicationContext(), "Vous devez selectionner au moins 1 destinataire", Toast.LENGTH_SHORT).show();
            return true;
    }
        return false;
    }

    float widthScreen;
    float heightScreen;
    @Override
    public void recyclerViewCircleListClicked(View view, String id) {
        RecyclerViewItem item=null;
        for(RecyclerViewItem item_tmp : friendList){
            if(item_tmp.getId().equals(id))
                item=item_tmp;
        }
        if(item!=null) {
            if (((CheckBox) view).isChecked()) {
                item.setChecked(true);
                friendOrGroupSelected.add(item);
                System.out.println("add " + item.getName());
            } else {
                item.setChecked(false);
                for (RecyclerViewItem i : friendOrGroupSelected) {
                    if (i.getId().equals(id)) {
                        i.setChecked(false);
                        //l'item de la selected list et current list ne soit pas toujours les meme a cause des cahngement de directory
                        item.setChecked(false);
                        friendOrGroupSelected.remove(i);
                        System.out.println("remove "+i.getName());
                        break;
                    }
                }
            }
        }

    }
    int a=0;
    private BitmapFactory.Options options = new BitmapFactory.Options();
    //bitmap et uri Doivent toujours s'actualiser par l'intermediaire de setImageDisplay
    private Bitmap bitmapDisplayed;
    private String uriDisplayed;
    @Override
    public void recyclerViewGalleryListClicked(View v, String uri) {
        /* Ajuster a la taille
            android:scaleType="centerCrop"
            android:adjustViewBounds ="true"
         */
        //scrollview.fullScroll(scrollview.FOCUS_DOWN);

        setImageDisplay(uri);
    }
    //Plus grand ca fait outofMemory dans l'upload...
    private final float MAX_WIDTH_UPLOAD=1024.0f;
    private final float MAX_HEIGHT_UPLOAD=1024.0f;
    public Bitmap getBitmapResizeMaxFromFile(String pathFile){
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = 2;
        Bitmap bmp = BitmapFactory.decodeFile(pathFile, options);

        float widthImage = ((float) bmp.getWidth());
        float heightImage = ((float) bmp.getHeight());

        float ratio;
        ratio = ((float) bmp.getHeight()) / ((float) bmp.getWidth());

        //Normalement pas besoin car options.inSampleSize = 2; precedemment
        if(widthImage>=MAX_WIDTH_UPLOAD){
            float ratioReduc= ((float)widthImage)/MAX_WIDTH_UPLOAD;
            widthImage= (int)MAX_WIDTH_UPLOAD;
            heightImage=(int) (heightImage/ratioReduc);
        }
        if(heightImage>=MAX_HEIGHT_UPLOAD){
            float ratioReduc= ((float)heightImage)/MAX_HEIGHT_UPLOAD;
            heightImage= (int)MAX_HEIGHT_UPLOAD;
            widthImage=(int) (widthImage/ratioReduc);
        }

            bmp = scaleBitmap(bmp, (int) widthImage, (int) heightImage);

        return bmp;
    }
    public void setImageDisplay(String uri) {
        uriDisplayed = uri;
        if (bitmapDisplayed != null) {
            bitmapDisplayed.recycle();
            bitmapDisplayed = null;
        }

        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = 2;
        System.out.println(uriDisplayed);
        bitmapDisplayed = BitmapFactory.decodeFile(uriDisplayed, options);

        float widthImage = ((float) bitmapDisplayed.getWidth());
        float heightImage = ((float) bitmapDisplayed.getHeight());

        float ratio;
        ratio = ((float) bitmapDisplayed.getHeight()) / ((float) bitmapDisplayed.getWidth());

        if(!landscape) {System.out.println("PORTrAIt ");
            if (widthImage < heightImage) {
                bitmapDisplayed = scaleBitmap(bitmapDisplayed, (int) widthScreen, (int) (widthScreen * ratio));
                widthImage = ((float) bitmapDisplayed.getWidth());
                heightImage = ((float) bitmapDisplayed.getHeight());
                matrix.set(matrixVertical);
                matrix.postTranslate(0, (HEIGHT_IMG_DISPLAYED / 2 - heightImage / 2));
                //On reset la position de la matrice de ImageView
                vertical = true;
            } else {
                bitmapDisplayed = scaleBitmap(bitmapDisplayed, (int) (HEIGHT_IMG_DISPLAYED / ratio), (int) (HEIGHT_IMG_DISPLAYED));
                widthImage = ((float) bitmapDisplayed.getWidth());
                heightImage = ((float) bitmapDisplayed.getHeight());
                //On reset la position de la matrice de ImageView
                matrix.set(matrixHorizontal);
                matrix.postTranslate((widthScreen / 2 - widthImage / 2), 0);
                vertical = false;
            }
        }else{
            System.out.println("LANDSCAPE ");
            if(widthImage<heightImage) {
                bitmapDisplayed = scaleBitmap(bitmapDisplayed, (int) widthScreen/2, (int) (widthScreen/2.0 * ratio));
                widthImage = ((float) bitmapDisplayed.getWidth());
                heightImage = ((float) bitmapDisplayed.getHeight());
                matrix.set(matrixVertical);
                matrix.postTranslate(widthScreen/2/2,(HEIGHT_IMG_DISPLAYED/2-heightImage/2));
                //On reset la position de la matrice de ImageView
                vertical =true;
            }else{
                bitmapDisplayed = scaleBitmap(bitmapDisplayed, (int) widthScreen, (int) (widthScreen * ratio));
                widthImage = ((float) bitmapDisplayed.getWidth());
                heightImage = ((float) bitmapDisplayed.getHeight());
                //On reset la position de la matrice de ImageView
                matrix.set(matrixHorizontal);
                matrix.postTranslate(0, (HEIGHT_IMG_DISPLAYED / 2 - heightImage / 2));
                //On set a true car on veut laisser le scroll vertical
                vertical =true;
            }
        }
        imgDisplayed.setImageMatrix(matrix);
        imgDisplayed.setImageBitmap(bitmapDisplayed);
        scrollview.setScrollingEnabled(true);
        scrollview.fullScroll(View.FOCUS_UP);
    }


    private float HEIGHT_IMG_DISPLAYED;
    public boolean vertical =true;
public Matrix matrixVertical=new Matrix();
public Matrix matrixHorizontal=new Matrix();
private static float MAX_WIDTH=4096.0f;
private static float MAX_HEIGHT=4096.0f;


    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {

        //Normalement pas besoin car options.inSampleSize = 2; precedemment
        if(newWidth>=MAX_WIDTH){
            float ratioReduc= ((float)newWidth)/MAX_WIDTH;
            newWidth= (int)MAX_WIDTH;
            newHeight=(int) (newHeight/ratioReduc);
        }
        if(newHeight>=MAX_HEIGHT){
            float ratioReduc= ((float)newHeight)/MAX_HEIGHT;
            newHeight= (int)MAX_HEIGHT;
            newWidth=(int) (newWidth/ratioReduc);
        }

        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

    public boolean imageScrollMode=false;
    @Override
    public void getListGroupFriend_GetResponse(JSONArray responseArray) {
        try {
            System.out.println(responseArray);
            for(int i=0;i<responseArray.length();i++){
                JSONObject response= responseArray.optJSONObject(i);
                RecyclerViewItem item = new RecyclerViewItem("my_group_friend",response.getString("id"),response.getString("name"),  response.getString("groupImgUrl"));
                friendList.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getListGroupFriend_NotifyWhenGetFinish(Integer result) {
        if (result == 1) {
            friendAdapter.setGridData(friendList);
        } else {
            Toast.makeText(getApplicationContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
        }
    }

//GET DES FRIEND EXISTANT

    public void getListFriend_GetResponse(JSONArray responseArray) {
        try {
            System.out.println(responseArray);
            for(int i=0;i<responseArray.length();i++){
                JSONObject response= responseArray.optJSONObject(i);
                RecyclerViewItem item = new RecyclerViewItem("my_friend",response.getString("id"),response.getString("lastName")+" "+response.getString("firstName"),  response.getString("profilImgUrl"));
                friendList.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getListFriend_NotifyWhenGetFinish(Integer result) {

        if (result == 1) {
            friendAdapter.setGridData(friendList);
        } else {
            Toast.makeText(getApplicationContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            View v = getCurrentFocus();
            if (v instanceof EditText) {
                EditText edit = ((EditText) v);
                Rect outR = new Rect();
                edit.getGlobalVisibleRect(outR);
                Boolean isKeyboardOpen = !outR.contains((int)ev.getRawX(), (int)ev.getRawY());
                System.out.print("Is Keyboard? " + isKeyboardOpen);
                if (isKeyboardOpen) {
                    System.out.print("Entro al IF");
                    edit.clearFocus();
                    InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
                }

                edit.setCursorVisible(!isKeyboardOpen);

            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void postMomentToServer_NotifyWhenGetFinish(Boolean result) {
        if(result == true){
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle b = new Bundle();
            b.putString("Uniqid","From SendMomentActivity");
            b.putString("albumId",getIntent().getStringExtra("albumId"));
            intent.putExtras(b);
            startActivity(intent);
            finish(); // call this to finish the current activity
        }else{
            Toast.makeText(this,"Failed to upload to the server ! Please retry later",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        clearDataFragment();
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);
        finish();
    }

}
