package com.start_up.dev.apilinkus.Adapter;

import java.io.Serializable;

/**
 * Created by Huong on 17/01/2017.
 */
//Doit etre serializable car on le serialise dans des onSaveInstance
public class RecyclerViewFolderItem implements Serializable{

    private String path;

    public RecyclerViewFolderItem(String path){
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}