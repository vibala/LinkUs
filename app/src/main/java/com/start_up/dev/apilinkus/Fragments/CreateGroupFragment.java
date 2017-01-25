package com.start_up.dev.apilinkus.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.start_up.dev.apilinkus.API.APIGetListFriend_Observer;
import com.start_up.dev.apilinkus.API.APIGetSearchListUser_Observer;
import com.start_up.dev.apilinkus.API.APILinkUS;
import com.start_up.dev.apilinkus.API.APIPostCreateGroupFriend_Observer;
import com.start_up.dev.apilinkus.Adapter.CreateGroupAdapter;
import com.start_up.dev.apilinkus.Adapter.ImageAndTextListAdapter;
import com.start_up.dev.apilinkus.Adapter.RecyclerViewItem;
import com.start_up.dev.apilinkus.Listener.RecyclerViewCircleClickListener;
import com.start_up.dev.apilinkus.Model.FriendGroup;
import com.start_up.dev.apilinkus.R;
import com.start_up.dev.apilinkus.Tool.GridSpacingItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Huong on 17/01/2017.
 */

public class CreateGroupFragment extends Fragment implements APIPostCreateGroupFriend_Observer,APIGetListFriend_Observer,APIGetSearchListUser_Observer, RecyclerViewCircleClickListener,SearchView.OnQueryTextListener  {


    @Override
    public void postCreateGroupFriend_NotifyWhenGetFinish(Boolean result) {
        if(result){
            Toast.makeText(getActivity(),"Groupe créé",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(),"Une erreur de connexion s'est produite ou le groupe existe déjà",Toast.LENGTH_SHORT).show();
        }
    }

    public interface onCreateGroupInteraction{
        public void createGroupFragmentOnButtonCreateGroup();
    }

 /*   private void prepareFriendTestModels() {


        RecyclerViewItem a = new RecyclerViewItem("my_friend", "1","Maroaaon5", "https://scontent-cdg2-1.xx.fbcdn.net/v/t1.0-9/15590012_1315905178482946_1583832953346816464_n.jpg?oh=8d20327cf060dc640b40d9c4c3c7d838&oe=59122F99");
        friendList.add(a);

        a = new RecyclerViewItem("my_friend","2","Suazfzagar Ray",  "https://scontent-cdg2-1.xx.fbcdn.net/v/t1.0-9/13331054_10209337159285692_1032067942344766374_n.jpg?oh=6b53c9ddfba81a637fc22ff684814bfa&oe=5914BADA");
        friendList.add(a);

        a = new RecyclerViewItem("my_friend", "3","Bon Jazfovi", "https://scontent-cdg2-1.xx.fbcdn.net/v/t1.0-9/15109626_662131003967305_5228921117221664410_n.jpg?oh=4c90a007be4208180953469612e657fa&oe=58DA6FC1");
        friendList.add(a);

        a = new RecyclerViewItem("my_friend", "4","The Corrs", "https://scontent-cdg2-1.xx.fbcdn.net/v/t1.0-9/15109626_662131003967305_5228921117221664410_n.jpg?oh=4c90a007be4208180953469612e657fa&oe=58DA6FC1");
        friendList.add(a);

        a = new RecyclerViewItem("my_friend", "5","The Cranberries", "https://scontent-cdg2-1.xx.fbcdn.net/v/t1.0-9/15109626_662131003967305_5228921117221664410_n.jpg?oh=4c90a007be4208180953469612e657fa&oe=58DA6FC1");
        friendList.add(a);

        a = new RecyclerViewItem("my_friend", "6","Westlife", "https://scontent-cdg2-1.xx.fbcdn.net/v/t1.0-9/15109626_662131003967305_5228921117221664410_n.jpg?oh=4c90a007be4208180953469612e657fa&oe=58DA6FC1");
        friendList.add(a);

        a = new RecyclerViewItem("my_friend", "7","Black Eyed Peas", "https://scontent-cdg2-1.xx.fbcdn.net/v/t1.0-9/15109626_662131003967305_5228921117221664410_n.jpg?oh=4c90a007be4208180953469612e657fa&oe=58DA6FC1");
        friendList.add(a);

        a = new RecyclerViewItem("my_friend", "8","VivaLaVida", "https://scontent-cdg2-1.xx.fbcdn.net/v/t1.0-9/15109626_662131003967305_5228921117221664410_n.jpg?oh=4c90a007be4208180953469612e657fa&oe=58DA6FC1");
        friendList.add(a);

        a = new RecyclerViewItem("my_friend", "9", "The Cardigans","https://scontent-cdg2-1.xx.fbcdn.net/v/t1.0-9/15109626_662131003967305_5228921117221664410_n.jpg?oh=4c90a007be4208180953469612e657fa&oe=58DA6FC1");
        friendList.add(a);

        a = new RecyclerViewItem("my_friend","10", "Pussycat Dolls", "https://scontent-cdg2-1.xx.fbcdn.net/v/t1.0-9/15109626_662131003967305_5228921117221664410_n.jpg?oh=4c90a007be4208180953469612e657fa&oe=58DA6FC1");
        friendList.add(a);

    }*/

    private View circleView;
    private APIPostCreateGroupFriend_Observer fragment;

    onCreateGroupInteraction mCallback;

    private ArrayList<RecyclerViewItem> friendList=new ArrayList<RecyclerViewItem>();
    private ArrayList<RecyclerViewItem> userList=new ArrayList<RecyclerViewItem>();
    private ArrayList<RecyclerViewItem> selectedList=new ArrayList<RecyclerViewItem>();
    private RecyclerView friends_recyclerView;
    private RecyclerView selectedList_recyclerView;
    private CreateGroupAdapter friend_adapter;
    private ImageAndTextListAdapter selectedList_friend_adapter;
    private SearchView searchView;
    private APILinkUS api;
    private EditText groupName ;

    /**
     * Converting dp to pixel
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        api=new APILinkUS();
        circleView = inflater.inflate(R.layout.activity_create_group,container,false);
        fragment=this;

        if(savedInstanceState!=null) {
            selectedList = (ArrayList<RecyclerViewItem>) savedInstanceState.getSerializable("selectedList");
            friendList = (ArrayList<RecyclerViewItem>) savedInstanceState.getSerializable("friendList");
            copyArrayList(userList,friendList);
        }
        else {
            api.getListFriend(this);
        }
        selectedList_recyclerView = (RecyclerView) circleView.findViewById(R.id.selected_list_recycler_view);
        selectedList_friend_adapter = new ImageAndTextListAdapter(getContext(),selectedList,this);
        selectedList_recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1, GridLayoutManager.HORIZONTAL, false));
        selectedList_recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(1)));
        selectedList_recyclerView.setItemAnimator(new DefaultItemAnimator());
        selectedList_recyclerView.setAdapter(selectedList_friend_adapter);

        friends_recyclerView = (RecyclerView) circleView.findViewById(R.id.friend_recycler_view);
        friend_adapter = new CreateGroupAdapter(getContext(),friendList,this);
        friends_recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        friends_recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(1)));
        friends_recyclerView.setItemAnimator(new DefaultItemAnimator());
        friends_recyclerView.setAdapter(friend_adapter);

        groupName = (EditText) circleView.findViewById(R.id.group_name);
        searchView = (SearchView) circleView.findViewById(R.id.simpleSearchView);
        searchView.setOnQueryTextListener(this);

        Button buttonCreateGroup=(Button) circleView.findViewById(R.id.circle_fragment_create_group);
        buttonCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> listId=new ArrayList<String>();
                String nameGroup = "";
                for (RecyclerViewItem item : selectedList) {
                    listId.add(item.getId());
                    if(groupName.getText().toString().equals("")) {
                        nameGroup += item.getName() + ", ";
                    }
                }
                if(nameGroup.equals("")) {
                    nameGroup = groupName.getText().toString();
                }
                api.createGroup(fragment,new FriendGroup(nameGroup,listId));
                //Il faut le renvoyer sur une page.
                mCallback.createGroupFragmentOnButtonCreateGroup();
            }
        });


        return circleView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("selectedList",  selectedList);
        savedInstanceState.putSerializable("friendList",  friendList);
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            mCallback=(onCreateGroupInteraction) context;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    String currentQuery="";
    @Override
    public boolean onQueryTextChange(String query) {
        currentQuery=query;
        searchListUser(currentQuery);

        friends_recyclerView.scrollToPosition(0);

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
            //On affiche les coché qui sont pas forcement des amis et tous les amis
            copyArrayList(userList,selectedList);
            for(RecyclerViewItem item : friendList) {
                if (!findFriendInUserList(item)) {
                    userList.add(item);
                }
            }
            friend_adapter.setGridData(userList);
        }
    }
    public boolean findFriendInUserList(RecyclerViewItem friend){
        for (RecyclerViewItem user : userList) {
            if (friend.getId().equals(user.getId())){
                return true;
            }
        }
        return false;
    }
    private static List<RecyclerViewItem> filter(List<RecyclerViewItem> models, String query) {
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



    @Override
    public void recyclerViewCircleListClicked(View view,  String id) {
        RecyclerViewItem item=null;
        for(RecyclerViewItem item_tmp : userList){
            if(item_tmp.getId().equals(id))
                item=item_tmp;
        }
        if(item!=null) {
            System.out.println("name " + item.getName());
            if (((CheckBox) view).isChecked()) {
                item.setChecked(true);
                selectedList.add(item);
                System.out.println("add " + item.getName());
            } else {
                item.setChecked(false);
                for (RecyclerViewItem i : selectedList) {
                    if (i.getId().equals(id)) {
                        i.setChecked(false);
                        //l'item de la selected list et current list ne soit pas toujours les meme a cause des cahngement de directory
                        item.setChecked(false);
                        selectedList.remove(i);
                        //System.out.println("remove "+i.getName());
                        break;
                    }
                }
            }
            friend_adapter.setGridData(filter(userList, currentQuery));
        }      else if(!((CheckBox)view).isChecked()) {
            for (RecyclerViewItem i : selectedList) {
                if (i.getId().equals(id)) {
                    i.setChecked(false);
                    selectedList.remove(i);
                    //System.out.println("remove "+i.getName());
                    break;
                }
            }

        }
        selectedList_recyclerView.scrollToPosition(selectedList.size()-1);
        //selectedList_recyclerView.scrollToPosition(0);
        selectedList_friend_adapter.setGridData(selectedList);

    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

//GET DES FRIEND EXISTANT

    @Override
    public void getListFriend_GetResponse(JSONArray responseArray) {
        try {
            System.out.println(responseArray);
            for(int i=0;i<responseArray.length();i++){
                JSONObject response= responseArray.optJSONObject(i);
                RecyclerViewItem item = new RecyclerViewItem("my_friend", response.getString("id"),response.getString("lastName")+" "+response.getString("firstName"), response.getString("profilImgUrl"));
                friendList.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getListFriend_NotifyWhenGetFinish(Integer result) {
        copyArrayList(userList,friendList);
        if (result == 1) {
            friend_adapter.setGridData(filter(userList, currentQuery));
        } else {
            Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
        }
    }
    //GET DE LA RECHERCHE SUR LES AMIS


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

                    //On regarde s'il faut le check ou non
                    item.setChecked(false);
                    for (RecyclerViewItem itemSelectedList : selectedList) {
                        if (itemSelectedList.getId().equals(item.getId())) {
                            item.setChecked(true);
                            break;
                        }
                    }

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

    public boolean findInFriendList(JSONObject friend_response) throws JSONException {
        //On regarde s'il n'a pas deja été listé avec un appel pendingList
        for (RecyclerViewItem friend : friendList) {
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
