package com.start_up.dev.apilinkus.TimeLine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.start_up.dev.apilinkus.Model.Instant;
import com.start_up.dev.apilinkus.Model.KeyValue;


import java.util.ArrayList;

/**
 * Created by Huong on 26/01/2017.
 */

public class TLViewMoment {

    private String id;
    private String name;
    private String albumName;
    private String albumId;
    private ArrayList<Instant> instantList = new ArrayList();
    private ArrayList<KeyValue> descriptionsList = new ArrayList<>();
    private boolean news = true;
    private Instant mainInstant;

    public TLViewMoment(String id) {
        this.id=id;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
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

    public ArrayList<Instant> getInstantList() {
        return instantList;
    }

    public void setInstantList(ArrayList<Instant> instantList) {
        this.instantList = instantList;
    }

    public boolean isNews() {
        return news;
    }

    public void setNews(boolean news) {
        this.news = news;
    }

    public Instant getMainInstant() {
        return mainInstant;
    }

    public void setMainInstant(Instant mainInstant) {
        this.mainInstant = mainInstant;
    }

    public ArrayList<KeyValue> getDescriptionsList() {
        return descriptionsList;
    }

    public void setDescriptionsList(ArrayList<KeyValue> descriptionsList) {
        this.descriptionsList = descriptionsList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TLViewMoment moment = (TLViewMoment) o;

        return instantList.equals(moment.instantList);

    }

    @Override
    public int hashCode() {
        return instantList.hashCode();
    }
}
