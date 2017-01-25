package com.start_up.dev.apilinkus.Adapter;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Huong on 17/01/2017.
 */
//Doit etre serializable car on le serialise dans des onSaveInstance
public class RecyclerViewItem implements Serializable {

    private String id;
    private String name;
    private String url;
    //Le type est util avec CircleAdapter pour changer les boutons selon le type search,group,friend
    //Le type est aussi utilisé pour différencier group d'ami et ami dans SendMoment
    private String type;
    private File file;
    private boolean checked=false;

    public RecyclerViewItem(String type,String id, String name, String url){
        this.name = name;
        this.id = id;
        this.url = url;
        this.type = type;
    }
    public RecyclerViewItem(String id, String name,File file,boolean checked){
        this.name = name;
        this.id = id;
        this.file = file;
        this.checked = checked;
    }

    public RecyclerViewItem(String id, String name, String url){
        this.name = name;
        this.id = id;
        this.url = url;
        this.type = type;
    }
    public RecyclerViewItem(String name,File file,boolean checked){
        this.name = name;
        this.id = id;
        this.file = file;
        this.checked = checked;
    }

    public File getFile() {
        return file;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }


}