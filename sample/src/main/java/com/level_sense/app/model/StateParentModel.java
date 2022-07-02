
package com.level_sense.app.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class StateParentModel  implements Serializable{
    @SerializedName("stateList")
    ArrayList<StateListModel> countryModelArrayList = new ArrayList<>();

    public ArrayList<StateListModel> getStateList() {
        return countryModelArrayList;
    }

}
