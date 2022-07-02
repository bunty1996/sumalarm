package com.level_sense.app.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;
import com.level_sense.app.R;
import com.level_sense.app.RetroFit.BaseRequest;
import com.level_sense.app.RetroFit.RequestReceiver;
import com.level_sense.app.Utility.ExpandableRecyclerView;
import com.level_sense.app.Utility.Functions;
import com.level_sense.app.Utility.OnItemClickAdapter;
import com.level_sense.app.adapter.AlertOptionsAdapter;
import com.level_sense.app.model.AlertOptionModel;
import com.level_sense.app.model.CellProviderModel;
import com.level_sense.app.model.ContactListModel;
import com.level_sense.app.model.CountryModel;
import com.level_sense.app.model.CountryParentModel;
import com.level_sense.app.model.ServiceParentModel;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpandableAdapter extends ExpandableRecyclerView.Adapter<ExpandableAdapter.ChildViewHolder, ExpandableRecyclerView.SimpleGroupViewHolder, ContactListModel, String> {

    ArrayList<ContactListModel> list = new ArrayList<>();
    NotificationItemListener notificationItemListener;
    Context mContext;

    public interface NotificationItemListener {

        void onDeleteClick(ContactListModel contactListModel);

        void onSaveClick(ContactListModel contactListModel);

        void onSendTestClick(ContactListModel contactListModel);
        void onSendSmsTestClick(ContactListModel contactListModel, String dialCode);

        void onSendEmailTestClick(ContactListModel contactListModel);

        void onSendVoiceTestClick(ContactListModel contactListModel, String dialCode);

    }

    public ExpandableAdapter(Context mContext, View custom_pb, NotificationItemListener notificationItemListener) {

        this.mContext = mContext;
        this.custom_pb = custom_pb;
        this.notificationItemListener = notificationItemListener;

    }

    @Override
    public int getGroupItemCount() {
        return list.size() - 1;
    }

    @Override
    public int getChildItemCount(int i) {
        return 1;
    }

    @Override
    public String getGroupItem(int i) {
        return list.get(i).getFirstName() + " " + list.get(i).getLastName();
    }

    @Override
    public ContactListModel getChildItem(int group, int child) {
        return list.get(group);
    }

    @Override
    protected ExpandableRecyclerView.SimpleGroupViewHolder onCreateGroupViewHolder(ViewGroup parent) {
        return new ExpandableRecyclerView.SimpleGroupViewHolder(parent.getContext());
    }

    @Override
    protected ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_list_child, parent, false);
        return new ChildViewHolder(rootView);
    }

    @Override
    public void onBindGroupViewHolder(ExpandableRecyclerView.SimpleGroupViewHolder holder, int group,int position) {
        super.onBindGroupViewHolder(holder, group,position);

        collapse(group);
        holder.setText(getGroupItem(group));
    }

    @Override
    public void onBindChildViewHolder(final ChildViewHolder holder, final int group, final int position) {
        super.onBindChildViewHolder(holder, group, position);

        holder.tVFirstName.setText(getChildItem(group, position).getFirstName());
        holder.tVFirstName.setTag(group);

        holder.tVFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus && position < list.size()) {

                    final int position = Integer.parseInt(v.getTag().toString());
                    final EditText Caption = (EditText) v;

                    if (position == 0) {

                        return;

                    }

                    Log.e("lisddsds", list.size() + "//" + position);
                    ContactListModel cmodel = list.get(position);
                    cmodel.setFirstName(Caption.getText().toString());
                    list.set(position, cmodel);

                }

            }

        });

       /*if(getChildItem(group, position).getServicePrivederName() != null && !getChildItem(group, position).getServicePrivederName().equalsIgnoreCase("") && !getChildItem(group, position).getServicePrivederName().equalsIgnoreCase("null")) {
            holder.tVlableserviceProvider.setText(getChildItem(group, position).getServicePrivederName());
        }*/

        holder.tVEmail.setText(getChildItem(group, position).getEmail());
        Log.e("status",""+getChildItem(group, position).getDefaultStatus());

        if (getChildItem(group, position).getDefaultStatus()!=null && getChildItem(group, position).getDefaultStatus()) {
          holder.tVEmail.setEnabled(false);
        } else {
           holder.tVEmail.setEnabled(true);
        }

        holder.tVEmail.setTag(group);
        holder.tVEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && position < list.size()) {
                    final int position = Integer.parseInt(v.getTag().toString());
                    final EditText Caption = (EditText) v;
                    if (position == 0) {
                        return;
                    }
                    ContactListModel cmodel = list.get(position);
                    cmodel.setEmail(Caption.getText().toString());
                    list.set(position, cmodel);

                }
            }
        });

        holder.tVLastName.setText(getChildItem(group, position).getLastName());
        holder.tVLastName.setTag(group);
        holder.tVLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && position < list.size()) {
                    final int position = Integer.parseInt(v.getTag().toString());
                    final EditText Caption = (EditText) v;
                    if (position == 0) {
                        return;
                    }
                    ContactListModel cmodel = list.get(position);
                    cmodel.setLastName(Caption.getText().toString());
                    list.set(position, cmodel);

                }
            }
        });

        holder.tVcellPhone.setText(getChildItem(group, position).getMobile());

        holder.tVcellPhone.setTag(group);
        if (getChildItem(group, position).getMobileCode() != null && !getChildItem(group, position).getMobileCode().trim().equalsIgnoreCase("") && !getChildItem(group, position).getMobileCode().equalsIgnoreCase("null")) {
            //holder.tvMobileCountry.setText(getChildItem(group, position).getMobileCode());
            //Log.e("cntrycd", Integer.valueOf(getChildItem(group, position).getMobileCode()) + "//" + getChildItem(group, position).getMobileCode());

            holder.notiCodePicker.setCountryForPhoneCode(Integer.valueOf(getChildItem(group, position).getMobileCode()));

        }

        if (getChildItem(group, position).getCountryId() != null && !getChildItem(group, position).getCountryId().trim().equalsIgnoreCase("") && !getChildItem(group, position).getCountryId().equalsIgnoreCase("null")) {
            //holder.tvMobileCountry.setText(getChildItem(group, position).getMobileCode());
            //Log.e("cntrycd12", Integer.valueOf(getChildItem(group, position).getCountryId()) + "//" + getChildItem(group, position).getCountryId());
            //holder.notiCodePicker.setCountryForPhoneCode(Integer.valueOf(getChildItem(group, position).getMobileCode()));

            holder.notiCodePicker.setCountryForNameCode(getChildItem(group, position).getCountryId());
        }

       holder. emailCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(holder.emailCheckbox.isChecked()){
                 holder.   email_btn.setEnabled(true);
                 holder.   email_btn.setBackground(mContext.getResources().getDrawable(R.drawable.blue_button_selector));

                }
                else{
                holder.    email_btn.setEnabled(false);
              holder.  email_btn.setBackground(mContext.getResources().getDrawable(R.drawable.gray_button_selector));
                }
            }
        });
       holder. textCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(holder.textCheckbox.isChecked()){
                  holder.  sms_btn.setEnabled(true);
                 holder.   sms_btn.setBackground(mContext.getResources().getDrawable(R.drawable.blue_button_selector));

                }
                else{
                 holder.   sms_btn.setEnabled(false);
                holder.    sms_btn.setBackground(mContext.getResources().getDrawable(R.drawable.gray_button_selector));
                }
            }
        });
        if(holder.textCheckbox.isChecked()){
           holder. sms_btn.setEnabled(true);
           holder. sms_btn.setBackground(mContext.getResources().getDrawable(R.drawable.blue_button_selector));
        }
        else{
         holder. sms_btn.setEnabled(false);
         holder.sms_btn.setBackground(mContext.getResources().getDrawable(R.drawable.gray_button_selector));
        }
        if(holder.emailCheckbox.isChecked()){
         holder.   email_btn.setEnabled(true);
         holder.   email_btn.setBackground(mContext.getResources().getDrawable(R.drawable.blue_button_selector));
        }
        else{
          holder.  email_btn.setEnabled(false);
         holder.   email_btn.setBackground(mContext.getResources().getDrawable(R.drawable.gray_button_selector));
        }
        holder.tVcellPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && position < list.size()) {
                    final int position = Integer.parseInt(v.getTag().toString());
                    final EditText Caption = (EditText) v;
                    if (position == 0) {
                        return;
                    }
                    ContactListModel cmodel = list.get(position);

                    cmodel.setMobile(Caption.getText().toString());
                    list.set(position, cmodel);
                }
            }
        });

        holder.cBEnabled.setChecked(getChildItem(group, position).getEnableStatus());
        holder.cBEnabled.setTag(group);

        holder.cBEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                final int position = Integer.parseInt(compoundButton.getTag().toString());
                final CheckBox Caption = (CheckBox) compoundButton;
                if (position == 0) {
                    return;
                }
                if (position < list.size()) {
                    ContactListModel cmodel = list.get(position);
                    cmodel.setEnableStatus(Caption.isChecked());
                    list.set(position, cmodel);
                }
            }

        });

        Log.e("serviceprovideradp", getChildItem(group, position).getServicePrivederName() + "//");
        holder.tVserviceProvider.setText(getChildItem(group, position).getServicePrivederName());
        holder.tVserviceProvider.setVisibility(View.VISIBLE);

        holder.emailLinear.setVisibility(View.VISIBLE);

        if (getChildItem(group, position).getEmailActive() && getChildItem(group, position).getSmsActive()) {

            holder.tVType.setText(mContext.getString(R.string.email) + "," + mContext.getString(R.string.text_message));
            holder.tvLblEmail.setVisibility(View.VISIBLE);
            holder.tVEmail.setVisibility(View.VISIBLE);
            holder.tvLblcellPhone.setVisibility(View.VISIBLE);
            holder.tVcellPhone.setVisibility(View.VISIBLE);
            holder.mobileLinear.setVisibility(View.VISIBLE);
            //todo for service provider
            holder.tVserviceProvider.setVisibility(View.VISIBLE);
            holder.tVlableserviceProvider.setVisibility(View.VISIBLE);
            holder.tVserviceProviderLinear.setVisibility(View.VISIBLE);

        }
        else if (getChildItem(group, position).getEmailActive()) {

            holder.tVType.setText(mContext.getString(R.string.email));
            holder.tvLblEmail.setVisibility(View.VISIBLE);
            holder.tVEmail.setVisibility(View.VISIBLE);
            holder.tvLblcellPhone.setVisibility(View.GONE);
          //  holder.tVcellPhone.setVisibility(View.GONE);
          //  holder.mobileLinear.setVisibility(View.GONE);
            //todo for service provider
            holder.tVserviceProvider.setVisibility(View.GONE);
            holder.tVlableserviceProvider.setVisibility(View.GONE);
            holder.tVserviceProviderLinear.setVisibility(View.GONE);

        } else if (getChildItem(group, position).getVoiceActive()) {

            holder.tVType.setText(mContext.getString(R.string.voice));
            holder.tvLblcellPhone.setVisibility(View.VISIBLE);
           // holder.tvLblEmail.setVisibility(View.GONE);
            holder.tVcellPhone.setVisibility(View.VISIBLE);

            holder.mobileLinear.setVisibility(View.VISIBLE);
           // holder.tVEmail.setVisibility(View.GONE);
            //holder.tVserviceProvider.setVisibility(View.GONE);
            //holder.tVlableserviceProvider.setVisibility(View.GONE);

        } else {

            holder.tVType.setText(mContext.getString(R.string.text_message));
            holder.tvLblcellPhone.setVisibility(View.VISIBLE);
           // holder.tvLblEmail.setVisibility(View.GONE);
            holder.tVcellPhone.setVisibility(View.VISIBLE);

            holder.mobileLinear.setVisibility(View.VISIBLE);
          //  holder.tVEmail.setVisibility(View.GONE);

            //todo for service provider
            holder.tVserviceProvider.setVisibility(View.VISIBLE);
            holder.tVlableserviceProvider.setVisibility(View.VISIBLE);
            holder.tVserviceProviderLinear.setVisibility(View.VISIBLE);

        }

        if (list.get(group).getMobile().equalsIgnoreCase("") || list.get(group).getMobile().equalsIgnoreCase("null")) {

          //  holder.sms_btn.setVisibility(View.GONE);
            holder.textCheckbox.setChecked(false);

        } else {

            holder.sms_btn.setVisibility(View.VISIBLE);
            holder.textCheckbox.setChecked(list.get(group).getSmsActive());
        }

        if (list.get(group).getEmail().equalsIgnoreCase("") || list.get(group).getEmail().equalsIgnoreCase("null")) {

          //  holder.email_btn.setVisibility(View.GONE);
            holder.emailCheckbox.setChecked(false);

        } else {

            holder.email_btn.setVisibility(View.VISIBLE);
            //holder.emailCheckbox.setChecked(true);
            holder.emailCheckbox.setChecked(list.get(group).getEmailActive());

        }

        if (list.get(group).getMobile().equalsIgnoreCase("") || list.get(group).getMobile().equalsIgnoreCase("null")) {

            holder.voice_btn.setVisibility(View.GONE);
            holder.voiceCheckbox.setChecked(false);

        } else {

            holder.voice_btn.setVisibility(View.VISIBLE);
            //holder.voiceCheckbox.setChecked(true);
            holder.voiceCheckbox.setChecked(list.get(group).getVoiceActive());

        }

        holder.tVType.setTag(group);
        holder.tVserviceProvider.setTag(group);

        holder.notiCodePicker.setTag(group);

     /* holder.tvMobileCountry.setTag(group);
        holder.tvMobileCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countryModels.size() <= 0)
                    callApiGetCountryList(holder.tvMobileCountry, Integer.parseInt(v.getTag().toString()));
                else {
                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < countryModels.size(); i++) {
                        list.add(countryModels.get(i).getName());
                    }
                    Functions.showListSelection(mContext, R.string.select_country, new OnItemClickAdapter() {
                        @Override
                        public void onClick(int i, int position, String item) {
                            holder.tvMobileCountry.setText(countryModels.get(position).getDialCode());
                            //ExpandableAdapter.this.list.get(Integer.parseInt(v.getTag().toString())).setMobile(countryModels.get(position).getDialCode());
                        }
                    }, list);
                }
            }
        });*/

        holder.tVserviceProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (serviceModelArrayList.size() <= 0)
                    callApiGetServiceProvider(holder.tVserviceProvider, Integer.parseInt(view.getTag().toString()));
                else {
                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < serviceModelArrayList.size(); i++) {
                        list.add(serviceModelArrayList.get(i).getProvider());
                    }
                    Functions.showListSelection(mContext, R.string.select_service_provider, new OnItemClickAdapter() {
                        @Override
                        public void onClick(int i, int position, String item) {
                            holder.tVserviceProvider.setText(item);
                            ExpandableAdapter.this.list.get(Integer.parseInt(view.getTag().toString())).setCellProvider(serviceModelArrayList.get(position).getProviderCode());
                        }
                    }, list);
                }
            }
        });

        holder.tVType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                callApiGetCountryList(holder.tVType,position);

                RecyclerView customAlertRecycler;
                TextView customAlertCancel;
                TextView customAlertSave;
                ArrayList<AlertOptionModel> optionModels = new ArrayList<>();
                String selectedItems = "";
                Log.e("ok", "oko");
                optionModels.clear();

                for (int i = 0; i < 2; i++) {

                    AlertOptionModel model = new AlertOptionModel();
                   /*if(i==0)
                        model.setName(mContext.getResources().getString(R.string.voice_small));
                    else*/
                    ContactListModel cmodel = list.get(group);

                    if (i == 0) {

                        model.setName(mContext.getResources().getString(R.string.text_message));
                        model.setSelected(cmodel.getSmsActive());

                    } else if (i == 1) {

                        model.setName(mContext.getResources().getString(R.string.email));
                        model.setSelected(cmodel.getEmailActive());

                    }

                    optionModels.add(model);

                }

                Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.custom_alert);
                customAlertRecycler = (RecyclerView) dialog.findViewById(R.id.customAlertRecycler);
                customAlertCancel = (TextView) dialog.findViewById(R.id.customAlertCancel);
                customAlertSave = (TextView) dialog.findViewById(R.id.customAlertSave);
                customAlertRecycler.setLayoutManager(new LinearLayoutManager(mContext));
                AlertOptionsAdapter adapter = new AlertOptionsAdapter(mContext, optionModels);
                customAlertRecycler.setAdapter(adapter);

                adapter.setOnOptionClick(new AlertOptionsAdapter.OnOptionClick() {
                    @Override
                    public void onOptionClick(int pos) {

                        int value = 0;

                        if (list.get(group).getEmailActive()) {
                            value = value + 2;
                        }

                        if (list.get(group).getSmsActive()) {
                            value = value + 1;
                        }

                        if (optionModels.get(pos).isSelected()) {

                            optionModels.get(pos).setSelected(false);
                            ContactListModel cmodel = list.get(group);
                            cmodel.setEmailActive(cmodel.getEmailActive());
                            cmodel.setVoiceActive(cmodel.getVoiceActive());
                            cmodel.setSmsActive(cmodel.getSmsActive());
                            Log.e("selctd", cmodel.getEmailActive() + "//" + cmodel.getVoiceActive() + "//" + cmodel.getSmsActive());

                         /*if(pos==0){
                                cmodel.setVoiceActive(false);
                                holder.tVserviceProvider.setVisibility(View.GONE);
                                holder.tVlableserviceProvider.setVisibility(View.GONE);
                            }else*/

                            if (pos == 0) {

                                if (value != 0) {
                                    value = value - 1;
                                }

                                cmodel.setSmsActive(false);
                                holder.tvLblcellPhone.setVisibility(View.GONE);
                            //    holder.tVcellPhone.setVisibility(View.GONE);
                             //   holder.mobileLinear.setVisibility(View.GONE);

                            } else {

                                if (value >= 2) {
                                    value = value - 2;
                                }
                                cmodel.setEmailActive(false);
                              //  holder.tvLblEmail.setVisibility(View.GONE);
                              //  holder.tVEmail.setVisibility(View.GONE);

                            }

                            Log.e("selctd1", cmodel.getEmailActive() + "//" + cmodel.getVoiceActive() + "//" + cmodel.getSmsActive());

                            list.set(group, cmodel);

                        } else {

                            optionModels.get(pos).setSelected(true);
                            ContactListModel cmodel = list.get(group);
                            cmodel.setEmailActive(cmodel.getEmailActive());
                            cmodel.setVoiceActive(cmodel.getVoiceActive());
                            cmodel.setSmsActive(cmodel.getSmsActive());

                            Log.e("selctdels", cmodel.getEmailActive() + "//" + cmodel.getVoiceActive() + "//" + cmodel.getSmsActive());

                           /*if(pos==0){
                                cmodel.setVoiceActive(true);
                                holder.tVserviceProvider.setVisibility(View.VISIBLE);
                                holder.tVlableserviceProvider.setVisibility(View.VISIBLE);
                            }else*/

                            if (pos == 0) {
                                Log.e("val1", value + "//");
                                value = value + 1;
                                Log.e("val12", value + "//");
                                cmodel.setSmsActive(true);
                                holder.tvLblcellPhone.setVisibility(View.VISIBLE);
                                holder.mobileLinear.setVisibility(View.VISIBLE);
                                holder.tVcellPhone.setVisibility(View.VISIBLE);
                            } else {
                                value = value + 2;
                                cmodel.setEmailActive(true);
                                holder.tvLblEmail.setVisibility(View.VISIBLE);
                                holder.tVEmail.setVisibility(View.VISIBLE);
                            }

                            list.set(group, cmodel);
                            Log.e("selctdels1", cmodel.getEmailActive() + "//" + cmodel.getVoiceActive() + "//" + cmodel.getSmsActive());
                        }

                        Log.e("expnot", value + "//");
                        if (value == 3) {

                            holder.tVType.setText(mContext.getString(R.string.text_message) + "," + mContext.getString(R.string.email));
                            holder.textCheckbox.setChecked(true);
                            holder.emailCheckbox.setChecked(true);

                        } else if (value == 2) {

                            holder.tVType.setText(mContext.getString(R.string.email));
                            holder.textCheckbox.setChecked(false);
                            holder.emailCheckbox.setChecked(true);
                        } else if (value == 1) {

                            holder.tVType.setText(mContext.getString(R.string.text_message));
                            holder.textCheckbox.setChecked(true);
                            holder.emailCheckbox.setChecked(false);

                        } else if (value == 0) {
                            holder.tVType.setText("");
                            holder.textCheckbox.setChecked(false);
                            holder.emailCheckbox.setChecked(false);
                        }

                        adapter.notifyDataSetChanged();

                    }

                });

                customAlertSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.cancel();
                        //holder.tVType.setText(seletedType);
                        ContactListModel contactListModel = new ContactListModel();

                        contactListModel.setEnableStatus(holder.cBEnabled.isChecked());

                        contactListModel.setFirstName(holder.tVFirstName.getText().toString().trim());
                        contactListModel.setLastName(holder.tVLastName.getText().toString().trim());
                        contactListModel.setEmail(holder.tVEmail.getText().toString().trim());
                        contactListModel.setMobile(holder.tVcellPhone.getText().toString().trim());
                        Log.e("updatedcntrymb", contactListModel.getMobile() + "//");
                        contactListModel.setId(list.get(group).getId());
                        //contactListModel.setEmailActive(list.get(group).getEmailActive());
                        contactListModel.setEmailActive(holder.emailCheckbox.isChecked());
                        contactListModel.setCellProvider(list.get(group).getCellProvider());
                        contactListModel.setVoiceActive(list.get(group).getVoiceActive());
                        //contactListModel.setSmsActive(list.get(group).getSmsActive());
                        contactListModel.setSmsActive(holder.textCheckbox.isChecked());
                        //contactListModel.setMobileCode(holder.tvMobileCountry.getText().toString().trim());
                        contactListModel.setMobileCode(holder.notiCodePicker.getSelectedCountryCode());
                        contactListModel.setCountryId(holder.notiCodePicker.getSelectedCountryNameCode());

                        if (isValidate(holder, contactListModel)) {
                            notificationItemListener.onSaveClick(contactListModel);
                            list.set(group, contactListModel);
                        }

                    }

                });

                customAlertCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        //holder.tVType.setText(seletedType);

                    }
                });
                dialog.show();
            }
        });

        holder.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ContactListModel contactListModel = new ContactListModel();
               /*if(holder.cBEnabled.isChecked()){
                    contactListModel.setEnableStatus(holder.cBEnabled.isChecked());
                }else{
                    contactListModel.setEnableStatus(holder.cBEnabled.isChecked());
                }*/

                contactListModel.setEnableStatus(holder.cBEnabled.isChecked());
                contactListModel.setFirstName(holder.tVFirstName.getText().toString().trim());
                contactListModel.setLastName(holder.tVLastName.getText().toString().trim());
                contactListModel.setEmail(holder.tVEmail.getText().toString().trim());
                contactListModel.setMobile(holder.tVcellPhone.getText().toString().trim());
                Log.e("updatedcntrymb", contactListModel.getMobile() + "//");
                contactListModel.setId(list.get(group).getId());
                //contactListModel.setEmailActive(list.get(group).getEmailActive());
                contactListModel.setEmailActive(holder.emailCheckbox.isChecked());
                contactListModel.setCellProvider(list.get(group).getCellProvider());
                contactListModel.setVoiceActive(list.get(group).getVoiceActive());
                //contactListModel.setSmsActive(list.get(group).getSmsActive());
                contactListModel.setSmsActive(holder.textCheckbox.isChecked());
                //contactListModel.setMobileCode(holder.tvMobileCountry.getText().toString().trim());
                contactListModel.setMobileCode(holder.notiCodePicker.getSelectedCountryCode());
                contactListModel.setCountryId(holder.notiCodePicker.getSelectedCountryNameCode());

                if (isValidate(holder, contactListModel)) {
                    notificationItemListener.onSaveClick(contactListModel);
                    list.set(group, contactListModel);
                }

            }

        });

        holder.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ContactListModel contactListModel = list.get(group);
                notificationItemListener.onSendTestClick(contactListModel);

            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                notificationItemListener.onDeleteClick(list.get(group));

            }
        });

        holder.email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notificationItemListener.onSendEmailTestClick(list.get(group));

            }
        });

        holder.sms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("list.get", list.get(group)+"" );
                //notificationItemListener.onSendSmsTestClick(list.get(group), holder.tvMobileCountry.getText().toString().trim());
                notificationItemListener.onSendSmsTestClick(list.get(group), holder.notiCodePicker.getSelectedCountryCodeWithPlus());

            }
        });

        holder.voice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //notificationItemListener.onSendVoiceTestClick(list.get(group), holder.tvMobileCountry.getText().toString().trim());
                notificationItemListener.onSendVoiceTestClick(list.get(group), holder.notiCodePicker.getSelectedCountryCodeWithPlus());

            }
        });

    }

    @Override
    public int getChildItemViewType(int i, int i1) {
        return 1;
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tVFirstName)
        EditText tVFirstName;
        @BindView(R.id.tVLastName)
        EditText tVLastName;
        @BindView(R.id.tVcellPhone)
        EditText tVcellPhone;
        @BindView(R.id.tVserviceProvider)
        TextView tVserviceProvider;
        @BindView(R.id.tVlableserviceProvider)
        TextView tVlableserviceProvider;
        @BindView(R.id.tVType)
        TextView tVType;
        @BindView(R.id.cBEnabled)
        CheckBox cBEnabled;
        @BindView(R.id.save_btn)
        Button saveBtn;
        @BindView(R.id.delete_btn)
        Button deleteBtn;
        @BindView(R.id.submit_btn)
        Button submitBtn;
        @BindView(R.id.email_btn)
        Button email_btn;
        @BindView(R.id.sms_btn)
        Button sms_btn;
        @BindView(R.id.voice_btn)
        Button voice_btn;
        @BindView(R.id.tVEmail)
        EditText tVEmail;
        @BindView(R.id.tvLblEmail)
        TextView tvLblEmail;
        @BindView(R.id.tvLblcellPhone)
        TextView tvLblcellPhone;
        @BindView(R.id.emailLinear)
        LinearLayout emailLinear;

        @BindView(R.id.tVserviceProviderLinear)
        LinearLayout tVserviceProviderLinear;
        @BindView(R.id.mobileLinear)
        LinearLayout mobileLinear;

       /*@BindView(R.id.tvMobileCountry)
        TextView tvMobileCountry;*/

        @BindView(R.id.notiCodePicker)
        CountryCodePicker notiCodePicker;
        @BindView(R.id.emailCheckbox)
        CheckBox emailCheckbox;
        @BindView(R.id.voiceCheckbox)
        CheckBox voiceCheckbox;
        @BindView(R.id.textCheckbox)
        CheckBox textCheckbox;

        public ChildViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.e("ontouch", "adptouch");
                    //if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //hide virtual keyboard
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    //return true;
                    //}
                    return false;
                }

            });

        }

    }

    public void setList(ArrayList<ContactListModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private boolean isValidate(ChildViewHolder childViewHolder, ContactListModel contactListModel) {

        if (childViewHolder.tVFirstName.getText().toString().trim().length() <= 0) {
            Functions.Alert((Activity) mContext, "Please enter first name.", Functions.AlertType.Error);
            return false;
        } else if (childViewHolder.tVLastName.getText().toString().trim().length() <= 0) {
            Functions.Alert((Activity) mContext, "Please enter last name.", Functions.AlertType.Error);
            return false;

        } else if (contactListModel.getEmailActive() && childViewHolder.tVEmail.getText().toString().trim().length() <= 0) {
            Functions.Alert((Activity) mContext, "Please enter email.", Functions.AlertType.Error);
            return false;

        } else if (contactListModel.getEmailActive() && !Functions.isValidEmaillId(childViewHolder.tVEmail.getText().toString().trim())) {
            Functions.Alert((Activity) mContext, "Please enter valid email.", Functions.AlertType.Error);
            return false;

        } else if (contactListModel.getSmsActive() && childViewHolder.tVcellPhone.getText().toString().trim().length() <= 0) {

            if (childViewHolder.tVcellPhone.getText().toString().trim().length() <= 0) {
                Functions.Alert((Activity) mContext, "Please enter mobile no.", Functions.AlertType.Error);
                return false;
            }
            if (childViewHolder.tVserviceProvider.getText().toString().trim().equals("------")) {
                Functions.Alert((Activity) mContext, "Please select service provider.", Functions.AlertType.Error);
                return false;
            }

            return false;

        } else if (contactListModel.getSmsActive() && childViewHolder.tVcellPhone.getText().toString().trim().length() > 0) {

            if (childViewHolder.tVserviceProvider.getText().toString().trim().equals("------")) {
                Functions.Alert((Activity) mContext, "Please select service provider.", Functions.AlertType.Error);
                return false;
            } else
                return true;

        } else {
            return true;
        }
    }

    View custom_pb;
    ArrayList<CellProviderModel> serviceModelArrayList = new ArrayList<>();

    private void callApiGetServiceProvider(TextView textView, final int positionr) {

        BaseRequest baseRequest = new BaseRequest(mContext);
        baseRequest.setLoaderView(custom_pb);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                Gson gson = new Gson();
                ServiceParentModel serviceParentModel = gson.fromJson(fullResponse, ServiceParentModel.class);
                serviceModelArrayList = serviceParentModel.getServiceModelArrayList();
                ArrayList<String> list = new ArrayList<>();

                for (int i = 0; i < serviceModelArrayList.size(); i++) {
                    list.add(serviceModelArrayList.get(i).getProvider());
                }

                Functions.showListSelection(mContext, R.string.select_service_provider, new OnItemClickAdapter() {
                    @Override
                    public void onClick(int i, int position, String item) {
                        textView.setText(item);
                        ExpandableAdapter.this.list.get(positionr).setCellProvider(serviceModelArrayList.get(position).getProviderCode());
                    }
                }, list);

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }

        });

        baseRequest.callAPIGET(1, new HashMap<String, String>(), mContext.getResources().getString(R.string.api_getCellProviderList));

    }

    ArrayList<CountryModel> countryModels = new ArrayList<>();

    private void callApiGetCountryList(TextView textView, final int pos) {
        countryModels.clear();
        BaseRequest baseRequest = new BaseRequest(mContext);
        baseRequest.setLoaderView(custom_pb);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {

                Log.e("dataa_abc",dataObject.toString());
                Gson gson = new Gson();
                CountryParentModel country = gson.fromJson(fullResponse, CountryParentModel.class);

                countryModels = country.getCountryModelArrayList();
                ArrayList<String> list1 = new ArrayList<>();

                for (int i = 0; i < countryModels.size(); i++) {
                    list1.add(countryModels.get(i).getName());
                }

                Functions.showListSelection(mContext, R.string.select_service_provider, new OnItemClickAdapter() {
                    @Override
                    public void onClick(int i, int position, String item) {

                        textView.setText(countryModels.get(position).getDialCode());
                        Log.e("mobile", list.get(pos).getMobile() + "//");
                        //ExpandableAdapter.this.list.get(pos).setMobile(countryModels.get(position).getDialCode()+);
                        //String str = "";
                      /*if(list.get(pos).getMobile().contains("+")) {
                        }else{
                           //ExpandableAdapter.this.list.get(pos).setMobile(countryModels.get(position).getDialCode()+list.get(pos).getMobile());
                        }*/
                    }
                }, list1);

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }

        });

        JsonObject object = null;
        object = Functions.getInstance().getJsonObject("incudeFlag","1");

        Log.e("object_data",object.toString());
        //baseRequest.callAPIGET(1, new HashMap<String, String>(), mContext.getResources().getString(R.string.api_get_country));
       // baseRequest.callAPIGET(1, new HashMap<String, String>(), mContext.getResources().getString(R.string.api_get_country));
        baseRequest.callAPIPost(1, object,mContext.getResources().getString(R.string.api_get_country));
    }

}