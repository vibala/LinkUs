package com.start_up.dev.apilinkus.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vignesh on 1/13/2017.
 */

public class MomentTestModel implements Serializable {

    private ArrayList<AlbumTestModel> listOfInstants;
    private String momentName;

    public MomentTestModel(String momentName){
        this.momentName = momentName;
    }

    public MomentTestModel(String momentName,ArrayList<AlbumTestModel> listOfMoments){
        this.momentName = momentName;
        this.listOfInstants = listOfMoments;
    }

    public ArrayList<AlbumTestModel> getListOfInstants() {
        return listOfInstants;
    }

    public void setListOfInstants(ArrayList<AlbumTestModel> listOfInstants) {
        this.listOfInstants = listOfInstants;
    }

    public String getMomentName() {
        return momentName;
    }

    public void setMomentName(String momentName) {
        this.momentName = momentName;
    }
}
