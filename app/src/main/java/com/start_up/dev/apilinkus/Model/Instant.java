package com.start_up.dev.apilinkus.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Date;



/**
 * Created by DamnAug on 14/10/2016.
 */
public class Instant {

    private String id;
    private String name;
    private String url;
    private ArrayList<Comment> commentList = new ArrayList<>();
    private ArrayList<Description> descriptionsList = new ArrayList<>();
    private Date publishDate;
    private ArrayList<IdRight> idRight = new ArrayList<>();
    private ArrayList<String> userIdDescriptionAvailable = new ArrayList<>();
    private byte[] imgByte;

    public Instant(){

    }

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

    public byte[] getImgByte() {
        return imgByte;
    }

    public void setImgByte(byte[] imgByte) {
        this.imgByte = imgByte;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(ArrayList<Comment> commentList) {
        this.commentList = commentList;
    }

    public ArrayList<Description> getDescriptionsList() {
        return descriptionsList;
    }

    public void setDescriptionsList(ArrayList<Description> descriptionsList) {
        this.descriptionsList = descriptionsList;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public ArrayList<IdRight> getIdRight() {
        return idRight;
    }

    public void setIdRight(ArrayList<IdRight> idRight) {
        this.idRight = idRight;
    }

    public ArrayList<String> getUserIdDescriptionAvailable() {
        return userIdDescriptionAvailable;
    }

    public void setUserIdDescriptionAvailable(ArrayList<String> userIdDescriptionAvailable) {
        this.userIdDescriptionAvailable = userIdDescriptionAvailable;
    }

    public boolean addUserIdDescriptionAvailable(String id) {
        if(!userIdDescriptionAvailable.contains(id)) {
            return false;
        } else {
            userIdDescriptionAvailable.add(id);
            return true;
        }
    }
}
