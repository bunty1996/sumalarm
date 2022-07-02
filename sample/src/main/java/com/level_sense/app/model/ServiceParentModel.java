
package com.level_sense.app.model;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class ServiceParentModel {
    @SerializedName("cellProviderList")
    ArrayList<CellProviderModel> serviceModelArrayList = new ArrayList<>();

    public ArrayList<CellProviderModel> getServiceModelArrayList() {
        return serviceModelArrayList;
    }

    public void setCountryModelArrayList(ArrayList<CellProviderModel> serviceModelArrayList) {
        this.serviceModelArrayList = serviceModelArrayList;
    }
}
