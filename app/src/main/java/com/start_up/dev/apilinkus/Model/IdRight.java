package com.start_up.dev.apilinkus.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

/**
 * Created by DamnAug on 22/10/2016.
 */
public class IdRight {

    private String id;
    private String right;
    private ArrayList<String> userIdList = new ArrayList<>();
    private ArrayList<String> groupIdList = new ArrayList<>();

    @Override
    public String toString() {
        String str = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            str = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRight() {
        return right;
    }

    public ArrayList<String> getUserIdList() {
        return userIdList;
    }

    public ArrayList<String> getGroupIdList() {
        return groupIdList;
    }
}
