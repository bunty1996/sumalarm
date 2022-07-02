
package com.level_sense.app.model;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class ContactModel {
@SerializedName("contactList")
ArrayList<ContactListModel> contactListModelArrayList=new ArrayList<>();

    public ArrayList<ContactListModel> getContactListModelArrayList() {
        return contactListModelArrayList;
    }

    public void setContactListModelArrayList(ArrayList<ContactListModel> contactListModelArrayList) {
        this.contactListModelArrayList = contactListModelArrayList;
    }
}
