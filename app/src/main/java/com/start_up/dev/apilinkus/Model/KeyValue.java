package com.start_up.dev.apilinkus.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

/**
 * Created by Vignesh on 1/18/2017.
 * Generic class for configUser, idRight, etc.
 *
 *      Like a key/value class
 *      ----------------------
 * Created by DamnAug on 23/10/2016.

 */

public class KeyValue implements Serializable {

    private String id;
    private String type; // description, wikipedia, localisation,
    private String content;

    public KeyValue() {
    }

    public KeyValue(String type, String content) {
        this.type = type;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyValue keyValue = (KeyValue) o;

        if (!type.equals(keyValue.type)) return false;
        return content.equals(keyValue.content);

    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + content.hashCode();
        return result;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
