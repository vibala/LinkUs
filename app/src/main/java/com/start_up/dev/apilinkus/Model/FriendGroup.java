package com.start_up.dev.apilinkus.Model;

/**
 * Created by Huong on 19/01/2017.
 */


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DamnAug on 14/10/2016.
 */
public class FriendGroup implements Serializable{

    private String id;
    private String ownerId;
    private String name;
    private List<String> members = new ArrayList<>();

    public FriendGroup(String name, List<String> members) {
        this.name = name;
        this.members = members;
    }

    public String toString(){
        String str = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            str = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public List<String> getMembers() {
        return members;
    }


    /**
     * Check on ownerId & name
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FriendGroup that = (FriendGroup) o;

        if (!ownerId.equals(that.ownerId)) return false;
        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        int result = ownerId.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}