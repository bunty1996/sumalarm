
package com.level_sense.app.model;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class CountryParentModel {
    @SerializedName("countryList")
    ArrayList<CountryModel> countryModelArrayList = new ArrayList<>();

    public ArrayList<CountryModel> getCountryModelArrayList() {
        return countryModelArrayList;
    }

    public void setCountryModelArrayList(ArrayList<CountryModel> countryModelArrayList) {
        this.countryModelArrayList = countryModelArrayList;
    }
}
