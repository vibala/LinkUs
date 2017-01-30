package com.start_up.dev.apilinkus.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.start_up.dev.apilinkus.API.APIGetListFriend_Observer;
import com.start_up.dev.apilinkus.API.APIGetListGroupFriend_Observer;
import com.start_up.dev.apilinkus.API.APIGetRequestPendingListFriend_Observer;
import com.start_up.dev.apilinkus.API.APIGetSearchListUser_Observer;
import com.start_up.dev.apilinkus.API.APILinkUS;
import com.start_up.dev.apilinkus.API.APIPostOneString_Observer;
import com.start_up.dev.apilinkus.Adapter.CircleAdapter;
import com.start_up.dev.apilinkus.Adapter.RecyclerViewItem;
import com.start_up.dev.apilinkus.Listener.RecyclerViewCircleClickListener;
import com.start_up.dev.apilinkus.R;
import com.start_up.dev.apilinkus.Tool.GridSpacingItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by Huong on 17/01/2017.
 */

public class CircleFragment extends Fragment implements APIGetRequestPendingListFriend_Observer,APIPostOneString_Observer,APIGetListFriend_Observer,APIGetListGroupFriend_Observer,APIGetSearchListUser_Observer, RecyclerViewCircleClickListener,SearchView.OnQueryTextListener  {

//GET DES GROUP EXISTANT
    @Override
    public void postOneString_NotifyWhenGetFinish(Boolean result, String typePost,String id) {
        if(result){
            switch (typePost){
                case "removeFriend":

                    for (Iterator<RecyclerViewItem> it = friendList.iterator(); it.hasNext(); ) {
                        RecyclerViewItem item = it.next();
                        if(item.getId().equals(id)) {
                            it.remove();
                            //On update la vue
                            searchListUser(currentQuery);
                            searchInGroupFriendList(currentQuery);
                        }
                    }

                    Toast.makeText(getActivity(),"La personne a été enlevé de votre liste d'ami",Toast.LENGTH_SHORT).show();

                    break;
                case "removeGroupFriend":

                    for (Iterator<RecyclerViewItem> it = group_friendList.iterator(); it.hasNext(); ) {
                        RecyclerViewItem item = it.next();
                        if(item.getId().equals(id)) {
                            it.remove();
                            //On update la vue
                            searchListUser(currentQuery);
                            searchInGroupFriendList(currentQuery);
                        }
                    }

                    Toast.makeText(getActivity(),"Le groupe a été supprimé",Toast.LENGTH_SHORT).show();
                    break;
                case "sendFriendRequest":
                    //On se casse pas la tete on recréé toute la friendList
                    //getPendingListFriend fait un clear de la liste d'ami puis appel les amis
                    //L'appel de l'api update la vue car un moment ca arrive a friend_adapter.setGridData(filter(friendList, currentQuery)); suivi d'un searchListUser(currentQuery); searchInGroupFriendList(currentQuery);
                    api.getRequestPendingListFriend(this);
                    Toast.makeText(getActivity(),"Votre invitation a été envoyée",Toast.LENGTH_SHORT).show();
                    break;
                default:
            }

        }else{
            switch (typePost){
                case "removeFriend":

                    Toast.makeText(getActivity(),"Une erreur s'est produite, nous n'avons pas réussi retirer cette personne de votre liste d'ami ",Toast.LENGTH_SHORT).show();
                    break;
                case "removeGroupFriend":


                    Toast.makeText(getActivity(),"Une erreur s'est produite, le groupe n'a pas pu être supprimé",Toast.LENGTH_SHORT).show();
                    break;
                case "sendFriendRequest":


                    Toast.makeText(getActivity(),"Une erreur s'est produite, votre invitation n'a pa pu être envoyé",Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
        }
    }


    @Override
    public void getRequestPendingListFriend_GetResponse(JSONArray responseArray) {
        friendList.clear();
        try {
            System.out.println(responseArray);
            for(int i=0;i<responseArray.length();i++){
                JSONObject response= responseArray.optJSONObject(i);
                RecyclerViewItem item = new RecyclerViewItem("my_friend_request_pending", response.getString("id"),response.getString("lastName")+" "+response.getString("firstName"), response.getString("profilImgUrl"));
                friendList.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getRequestPendingListFriend_NotifyWhenGetFinish(Integer result) {
        if (result == 1) {
            friend_adapter.setGridData(filter(friendList, currentQuery));
            //Une fois qu'on a les pending on prend les autres car le bouton pending par dessus bouton add
            api.getListFriend(this);
        } else {
            Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getListFriend_GetResponse(JSONArray responseArray) {
        try {
            System.out.println(responseArray);
            for(int i=0;i<responseArray.length();i++){
                JSONObject friend_response= responseArray.optJSONObject(i);
                //S'il n'existe pas (s'il n'a pas été créé dans l'appel de la pendingList), on créé une vue pour lui et on l'ajoute
                if(!findInFriendList(friend_response)) {
                    RecyclerViewItem item = new RecyclerViewItem("my_friend", friend_response.getString("id"),friend_response.getString("lastName")+" "+friend_response.getString("firstName"), friend_response.getString("profilImgUrl"));
                    friendList.add(item);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getListFriend_NotifyWhenGetFinish(Integer result) {
        copyArrayList(userList,friendList);
        if (result == 1) {
            //On filtre dans la liste de tous nos amis, La fonction filter fait une copie
            friend_adapter.setGridData(filter(userList, currentQuery));
            //On update la vue avec la query car quand on appuye sur un bouton il faut repeupler le search
            searchListUser(currentQuery);
            searchInGroupFriendList(currentQuery);
        } else {
            Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void getSearchListUser_GetResponse(JSONArray responseArray) {
        copyArrayList(userList,friendList);
        try {
            System.out.println(responseArray);
            for(int i=0;i<responseArray.length();i++){
                JSONObject friend_response= responseArray.optJSONObject(i);
                //S'il n'existe pas, on créé une vue pour lui et on l'ajoute
                System.out.println("friend_response "+friend_response);
                if(!findInFriendList(friend_response)) {
                    RecyclerViewItem item = new RecyclerViewItem("search_friend", friend_response.getString("id"),friend_response.getString("lastName")+" "+friend_response.getString("firstName"), friend_response.getString("profilImgUrl"));
                    userList.add(item);
                    System.out.println("userList "+userList.toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getSearchListUser_NotifyWhenGetFinish(Integer result) {
        if (result == 1) {
            friend_adapter.setGridData(filter(userList, currentQuery));

        } else {
            Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void getListGroupFriend_GetResponse(JSONArray responseArray) {
        group_friendList.clear();
        try {
            System.out.println(responseArray);
            for(int i=0;i<responseArray.length();i++){
                JSONObject response= responseArray.optJSONObject(i);
                RecyclerViewItem item = new RecyclerViewItem("my_group_friend", response.getString("id"),response.getString("name"), response.getString("groupImgUrl"));
                group_friendList.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getListGroupFriend_NotifyWhenGetFinish(Integer result) {
        if (result == 1) {
            group_friend_adapter.setGridData(group_friendList);
        } else {
            Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
        }
    }

    public interface onCircleInteraction{
     public void circleFragmentOnButtonCreateGroup();
    }
    /**
     * Adding few AlbumTestModels for testing
     */
    private void prepareFriendTestModels() {

        RecyclerViewItem a = new RecyclerViewItem("my_friend","Maroaaon5", "1", "https://scontent-cdg2-1.xx.fbcdn.net/v/t1.0-9/15590012_1315905178482946_1583832953346816464_n.jpg?oh=8d20327cf060dc640b40d9c4c3c7d838&oe=59122F99");
        friendList.add(a);

        a = new RecyclerViewItem("my_friend", "2", "Suazfzagar Ray","https://scontent-cdg2-1.xx.fbcdn.net/v/t1.0-9/13331054_10209337159285692_1032067942344766374_n.jpg?oh=6b53c9ddfba81a637fc22ff684814bfa&oe=5914BADA");
        friendList.add(a);
     /*   a = new RecyclerViewItem("my_friend","3", "Bon Jazfovi", "https://scontent-cdg2-1.xx.fbcdn.net/v/t1.0-9/15109626_662131003967305_5228921117221664410_n.jpg?oh=4c90a007be4208180953469612e657fa&oe=58DA6FC1");
        friendList.add(a);
        a = new RecyclerViewItem("my_friend", "4","The Corrs", "https://scontent-cdg2-1.xx.fbcdn.net/v/t1.0-9/15109626_662131003967305_5228921117221664410_n.jpg?oh=4c90a007be4208180953469612e657fa&oe=58DA6FC1");
        friendList.add(a);
        a = new RecyclerViewItem("my_friend", "5", "The Cranberries","https://scontent-cdg2-1.xx.fbcdn.net/v/t1.0-9/15109626_662131003967305_5228921117221664410_n.jpg?oh=4c90a007be4208180953469612e657fa&oe=58DA6FC1");
        friendList.add(a);

*/
        friend_adapter.setGridData(friendList);
    }

    private void prepareGroupFriendTestModels() {

        RecyclerViewItem a = new RecyclerViewItem("my_group_friend","13","my_group1",  "https://scontent-cdg2-1.xx.fbcdn.net/v/t1.0-9/15590012_1315905178482946_1583832953346816464_n.jpg?oh=8d20327cf060dc640b40d9c4c3c7d838&oe=59122F99");
        group_friendList.add(a);

      /*  a = new RecyclerViewItem("my_group_friend", "8","my_group2", "https://scontent-cdg2-1.xx.fbcdn.net/v/t1.0-9/13331054_10209337159285692_1032067942344766374_n.jpg?oh=6b53c9ddfba81a637fc22ff684814bfa&oe=5914BADA");
        group_friendList.add(a);

           a = new RecyclerViewItem("my_group_friend", "11","my_group3", "https://scontent-cdg2-1.xx.fbcdn.net/v/t1.0-9/15109626_662131003967305_5228921117221664410_n.jpg?oh=4c90a007be4208180953469612e657fa&oe=58DA6FC1");
        group_friendList.add(a);

        a = new RecyclerViewItem("my_group_friend", "12","my_group4", "https://scontent-cdg2-1.xx.fbcdn.net/v/t1.0-9/15109626_662131003967305_5228921117221664410_n.jpg?oh=4c90a007be4208180953469612e657fa&oe=58DA6FC1");
        group_friendList.add(a);
*/
        group_friend_adapter.setGridData(group_friendList);

    }
    private View circleView;

    onCircleInteraction mCallback;
    //Changé uniquement a la création de l'instance et a la suppression d'un ami
    private ArrayList<RecyclerViewItem> friendList=new ArrayList<RecyclerViewItem>();
    private ArrayList<RecyclerViewItem> userList=new ArrayList<RecyclerViewItem>();
    //Changé uniquement a la création de l'instance et a la suppression d'un groupe
    private ArrayList<RecyclerViewItem> group_friendList=new ArrayList<RecyclerViewItem>();
    private RecyclerView friends_recyclerView;
    private CircleAdapter friend_adapter;
    private RecyclerView group_friends_recyclerView;
    private CircleAdapter group_friend_adapter;
    private SearchView searchView;
    private APILinkUS api ;

    private void clearDataFragment(){
        friendList.clear();
        userList.clear();
        group_friendList.clear();
    }
    /**
     * Converting dp to pixel
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        api=new APILinkUS();
        circleView = inflater.inflate(R.layout.activity_searchfriend,container,false);
        clearDataFragment();
        friends_recyclerView = (RecyclerView) circleView.findViewById(R.id.friend_recycler_view);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            friends_recyclerView.getLayoutParams().height = (int)((float)metrics.heightPixels/(4));
        }
        else {
            friends_recyclerView.getLayoutParams().height = (int)((float)metrics.heightPixels/(2));
        }
        friend_adapter = new CircleAdapter(getContext(),this,friendList,this);
        friends_recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        friends_recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(1)));
        friends_recyclerView.setItemAnimator(new DefaultItemAnimator());
        friends_recyclerView.setAdapter(friend_adapter);

        group_friends_recyclerView = (RecyclerView) circleView.findViewById(R.id.group_friend_recycler_view);
        group_friend_adapter = new CircleAdapter(getContext(),this,group_friendList,this);
        group_friends_recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        group_friends_recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(1)));
        group_friends_recyclerView.setItemAnimator(new DefaultItemAnimator());
        group_friends_recyclerView.setAdapter(group_friend_adapter);




        searchView = (SearchView) circleView.findViewById(R.id.simpleSearchView);
        searchView.setOnQueryTextListener(this);

        Button buttonCreateGroup=(Button) circleView.findViewById(R.id.circle_fragment_create_group);
        buttonCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearDataFragment();
                mCallback.circleFragmentOnButtonCreateGroup();
                     }
        });
      /*  prepareFriendTestModels();
        prepareGroupFriendTestModels();
*/

        //On remplit la liste d'ami
        api.getRequestPendingListFriend(this);
        //On remplit la liste des groupes
        api.getListGroupFriend(this);

        return circleView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            mCallback=(onCircleInteraction) context;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onResume(){
        super.onResume();
        if(api==null)
            api=new APILinkUS();
        api.getListGroupFriend(this);

    }
    String currentQuery="";
    @Override
    public boolean onQueryTextChange(String query) {
        currentQuery=query.toLowerCase();
        searchListUser(currentQuery);
        searchInGroupFriendList(currentQuery);

        group_friends_recyclerView.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private void searchListUser(String query) {
        if(!query.equals("")) {
            api.getSearchListUser(this, query);
        }else{
            friend_adapter.setGridData(friendList);
        }
    }
    private void searchInGroupFriendList(String query) {
        if(!query.equals("")) {
            //on envoit une copie
            group_friend_adapter.setGridData(filter(group_friendList, currentQuery));
        }else {
            group_friend_adapter.setGridData(group_friendList);
        }
    }

    @Override
    public void recyclerViewCircleListClicked(View v, String position) {

    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private List<RecyclerViewItem> filter(List<RecyclerViewItem> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<RecyclerViewItem> filteredModelList = new ArrayList<>();
        for (RecyclerViewItem model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }


    public boolean findInFriendList(JSONObject friend_response) throws JSONException {
        //On regarde s'il n'a pas deja été listé avec un appel pendingList
        for (RecyclerViewItem friend : friendList) {
            System.out.println("friend_response.getString(\"id\") "+friend_response.getString("id"));
            System.out.println("friend.getId() "+friend.getId());
            if (friend_response.getString("id").equals(friend.getId())){
                return true;
            }
        }
        return false;
    }

    private void copyArrayList(ArrayList<RecyclerViewItem> dest, ArrayList<RecyclerViewItem> src){
        dest.clear();
        for(RecyclerViewItem x:src )
            dest.add(x);
    }

}
