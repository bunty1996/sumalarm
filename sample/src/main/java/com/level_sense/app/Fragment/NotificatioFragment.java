package com.level_sense.app.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
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
import com.level_sense.app.model.ContactModel;
import com.level_sense.app.model.CountryModel;
import com.level_sense.app.model.CountryParentModel;
import com.level_sense.app.model.ServiceParentModel;
import com.level_sense.app.slideup.SlideUp;
import com.level_sense.app.slideup.SlideUpBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static com.level_sense.app.Utility.Functions.noData;

public class NotificatioFragment extends Fragment {

    @BindView(R.id.notification_list)
    ExpandableRecyclerView expandableRecyclerView;
    @BindView(R.id.btnAddContact)
    Button btnAddContact;
    @BindView(R.id.btnAddContact2)
    Button btnAddContact2;
    @BindView(R.id.tVcellPhone)
    EditText tVcellPhone;
    @BindView(R.id.mobileLinear)
    LinearLayout mobileLinear;
    @BindView(R.id.tVserviceProvider)
    TextView tVserviceProvider;
    @BindView(R.id.tVlableserviceProvider)
    TextView tVlableserviceProvider;
    @BindView(R.id.tVType)
    TextView tVType;
    @BindView(R.id.cBEnabled)
    CheckBox cBEnabled;
    @BindView(R.id.emailCheckbox)
    CheckBox emailCheckbox;
    @BindView(R.id.textCheckbox)
    CheckBox textCheckbox;
    @BindView(R.id.tVFirstName)
    EditText tVFirstName;
    @BindView(R.id.tVLastName)
    EditText tVLastName;
    @BindView(R.id.hidden_panel)
    LinearLayout hiddenPanel;
    @BindView(R.id.parent_rl)
    RelativeLayout parentRl;
    @BindView(R.id.save_btn)
    Button saveBtn;
    @BindView(R.id.delete_btn)
    Button deleteBtn;
    @BindView(R.id.email_btn)
    Button email_btn;
    @BindView(R.id.sms_btn)
    Button sms_btn;
    @BindView(R.id.main_panel)
    LinearLayout main_panel;
    @BindView(R.id.tVserviceProviderLinear)
    LinearLayout tVserviceProviderLinear;
    @BindView(R.id.custom_pb)
    View custom_pb;
    @BindView(R.id.tVEmail)
    EditText tVEmail;
    @BindView(R.id.tvLblEmail)
    TextView tvLblEmail;
    @BindView(R.id.tvLblcellPhone)
    TextView tvLblcellPhone;
    @BindView(R.id.TvNoData)
    TextView TvNoData;
    @BindView(R.id.notiAddScroll)
    ScrollView notiAddScroll;
    @BindView(R.id.tVserviceProviderHint)
    TextView tVserviceProviderHint;

    /*@BindView(R.id.tvMobileCountry)
      TextView tvMobileCountry;*/
    @BindView(R.id.notiCodePicker)
    CountryCodePicker notiCodePicker;

    @BindView(R.id.notiCodePickerText)
    TextView notiCodePickerText;

    ArrayList<ContactListModel> contactListModelArrayList = new ArrayList<>();
    ExpandableAdapter expandableAdapter;
    ArrayList<String> typeItem = new ArrayList<>();
    ArrayList<AlertOptionModel> optionModels = new ArrayList<>();
    private boolean isPanelShown;
    String seletedType;
    String voiceActive;
    ViewGroup container;

    private int email = 1, textMessage = 1;

    public NotificatioFragment() {
    }

    //TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NotificatioFragment newInstance() {
        NotificatioFragment fragment = new NotificatioFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);
        //hiddenPanel.setVisibility(View.GONE);
        Log.e("onCreateView: ", tVserviceProvider.getText().toString().trim());
        isPanelShown = false;
        this.container = container;
        email = 1;
        initSlider();
        saveBtn.setText("Cancel");
        saveBtn.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.red_button_selector));
        deleteBtn.setText("Add");
        deleteBtn.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.green_button_selector));
        updateUI();

        tVType.setText(getString(R.string.text_message) + "," + getString(R.string.email));

        expandableAdapter = new ExpandableAdapter(getActivity(), custom_pb, new ExpandableAdapter.NotificationItemListener() {
            @Override
            public void onDeleteClick(final ContactListModel contactListModel) {

                Functions.showDefaultTwoButonYesNo(getActivity(), getString(R.string.are_you_sure_want_to_delete)
                        , R.string.deleteContect, new Functions.OnDialogButtonClickListener() {
                            @Override
                            public void onClick(int type) {

                                if (type == 1) {

                                    CallDeleteContactAPI(contactListModel);

                                }

                                btnAddContact2.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.blue_dark));
                                btnAddContact2.setClickable(true);

                            }

                        });

            }

            @Override
            public void onSaveClick(ContactListModel contactListModel) {
                CallUpdateContactAPI(contactListModel);

            }

            @Override
            public void onSendTestClick(ContactListModel contactListModel) {
                CallSentTestAPI(contactListModel);
            }

            @Override
            public void onSendSmsTestClick(ContactListModel contactListModel, String dialCode) {
                callSendSmsTestAPI(contactListModel, dialCode);
            }

            @Override
            public void onSendEmailTestClick(ContactListModel contactListModel) {
                callSendEmailTestAPI(contactListModel);
            }

            @Override
            public void onSendVoiceTestClick(ContactListModel contactListModel, String dialCode) {
                callSendVoiceTestAPI(contactListModel, dialCode);
            }

        });
        expandableRecyclerView.setAdapter(expandableAdapter);
        expandableAdapter.setOnChildItemClickedListener(new ExpandableRecyclerView.OnChildItemClickedListener() {
            @Override
            public void onChildItemClicked(int group, int position) {
               /*btnAddContact2.setVisibility(View.GONE);
                btnAddContact2.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.text_grey));
                Toast.makeText(getActivity(),"CLICKED"+group+"//"+position,Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onParentItemClicked(int group, int position, String value) {
                if(value.equalsIgnoreCase("1")){
                    btnAddContact2.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.text_grey));
                    btnAddContact2.setClickable(false);
                }else {
                    btnAddContact2.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.blue_dark));
                    btnAddContact2.setClickable(true);
                }

            }

        });
        callApiGetServiceProvider(tVserviceProvider, false);

        notiAddScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                return false;
            }

        });

        emailCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(emailCheckbox.isChecked()){
                    email_btn.setEnabled(true);
                    email_btn.setBackground(getResources().getDrawable(R.drawable.blue_button_selector));

                }
                else{
                    email_btn.setEnabled(false);
                    email_btn.setBackground(getResources().getDrawable(R.drawable.gray_button_selector));
                }
            }
        });
        textCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(textCheckbox.isChecked()){
                    sms_btn.setEnabled(true);
                    sms_btn.setBackground(getResources().getDrawable(R.drawable.blue_button_selector));

                }
                else{
                    sms_btn.setEnabled(false);
                    sms_btn.setBackground(getResources().getDrawable(R.drawable.gray_button_selector));
                }
            }
        });
        if(textCheckbox.isChecked()){
            sms_btn.setEnabled(true);
            sms_btn.setBackground(getResources().getDrawable(R.drawable.blue_button_selector));
        }
        else{
            sms_btn.setEnabled(false);
            sms_btn.setBackground(getResources().getDrawable(R.drawable.gray_button_selector));
        }
        if(emailCheckbox.isChecked()){
            email_btn.setEnabled(true);
            email_btn.setBackground(getResources().getDrawable(R.drawable.blue_button_selector));
        }
            else{
            email_btn.setEnabled(false);
            email_btn.setBackground(getResources().getDrawable(R.drawable.gray_button_selector));
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.btnAddContact, R.id.btnAddContact2, R.id.tVserviceProviderHint,R.id.save_btn, /*R.id.tvMobileCountry,*//*R.id.notiCodePicker,*/ R.id.delete_btn, R.id.email_btn, R.id.sms_btn, R.id.tVserviceProvider, R.id.tVType})
    public void onViewClicked(View view) {

        switch (view.getId()) {

            case R.id.btnAddContact:
                slideUpDown();

                break;

            case R.id.btnAddContact2:
                slideUpDown();

                break;

            case R.id.tVserviceProviderHint:
                String url = "https://qafw82j9t590rwl4-9849846.shopifypreview.com/blogs/wifi-enabled-sump-pump-alarms/articles?preview_key=bce7ee3a852fc4cfb528aeca132ff01e";
                Intent serviceProviderIntent = new Intent(Intent.ACTION_VIEW);
                serviceProviderIntent.setData(Uri.parse(url));
                startActivity(serviceProviderIntent);
                break;

            case R.id.save_btn:

                slideUpDown();

                break;

            case R.id.email_btn:

                //todo for send test email
                if (email == 1 && tVEmail.getText().toString().trim().length() <= 0) {
                    Functions.Alert(getActivity(), "Please enter email.", Functions.AlertType.Error);
                    return;

                } else if (email == 1 && !Functions.isValidEmaillId(tVEmail.getText().toString().trim())) {
                    Functions.Alert(getActivity(), "Please enter valid email.", Functions.AlertType.Error);
                    return;
                }

                sendEmailTestAPI(tVEmail.getText().toString().trim());
                sendEmailTestAPI(tVEmail.getText().toString().trim());

                break;

            case R.id.sms_btn:

                //todo for send sms
                if (textMessage == 1 && tVcellPhone.getText().toString().trim().length() <= 0) {

                    if (tVserviceProvider.getText().toString().trim().equals("------")) {
                        Functions.Alert(getActivity(), "Please select service provider.", Functions.AlertType.Error);
                        return;
                    }

                    Functions.Alert(getActivity(), "Please enter mobile no.", Functions.AlertType.Error);
                    return;

                } else if (textMessage == 1 && tVcellPhone.getText().toString().trim().length() > 0 && tVserviceProvider.getText().toString().trim().equals("------")) {
                    //if (tVserviceProvider.getText().toString().trim().contains("-")) {
                    Functions.Alert(getActivity(), "Please select service provider.", Functions.AlertType.Error);
                    return;

                } else sendSmsTestAPI();


                break;

            case R.id.delete_btn:

                if (isValidate())
                    CallAddContactAPI(null);

                break;

            case R.id.tVType:

                customAlert();

                break;

          /*case R.id.tvMobileCountry:
             if(countryModels.size() <= 0)
                    callApiGetCountryList(tvMobileCountry);
                else {
                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < countryModels.size(); i++) {
                        list.add(countryModels.get(i).getName());
                    }
                    Functions.showListSelection(getActivity(), R.string.select_country, new OnItemClickAdapter() {
                        @Override
                        public void onClick(int i, int position, String item) {
                            tvMobileCountry.setText(countryModels.get(position).getDialCode());
                        }
                    }, list);} break;*/
          /*case R.id.notiCodePicker:
                break;*/

            case R.id.tVserviceProvider:

                if (serviceModelArrayList.size() <= 0)
                    callApiGetServiceProvider(tVserviceProvider, true);

                else {

                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < serviceModelArrayList.size(); i++) {
                        list.add(serviceModelArrayList.get(i).getProvider());
                    }

                    Functions.showListSelection(getActivity(), R.string.select_service_provider, new OnItemClickAdapter() {
                        @Override
                        public void onClick(int i, int position, String item) {
                            tVserviceProvider.setText(item);
                            serviceProvider = item;
                            serviceProviderCode = serviceModelArrayList.get(position).getProviderCode();
                        }
                    }, list);
                }

                break;

        }

    }

    ArrayList<CountryModel> countryModels = new ArrayList<>();

    private void callApiGetCountryList(TextView textView) {
        countryModels.clear();
        BaseRequest baseRequest = new BaseRequest(getActivity());
        baseRequest.setLoaderView(custom_pb);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                Gson gson = new Gson();
                CountryParentModel country = gson.fromJson(fullResponse, CountryParentModel.class);

                countryModels = country.getCountryModelArrayList();
                ArrayList<String> list1 = new ArrayList<>();

                for (int i = 0; i < countryModels.size(); i++) {

                    list1.add(countryModels.get(i).getName());

                }

                Functions.showListSelection(getActivity(), R.string.select_service_provider, new OnItemClickAdapter() {
                    @Override
                    public void onClick(int i, int position, String item) {

                        textView.setText(countryModels.get(position).getDialCode());

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

       // baseRequest.callAPIGET(1, new HashMap<String, String>(), getActivity().getResources().getString(R.string.api_get_country));
        baseRequest.callAPIPost(1, object,getActivity().getResources().getString(R.string.api_get_country));

    }

    private void customAlert() {

        Log.e("ok", "oko");
        optionModels.clear();

        for (int i = 0; i < 2; i++) {

            AlertOptionModel model = new AlertOptionModel();
           /*if(i==0)
                model.setName(getResources().getString(R.string.voice_small));
            else*/
            if (i == 0) {

                if (textMessage == 1) {
                    model.setSelected(true);
                } else
                    model.setSelected(false);
                model.setName(getResources().getString(R.string.text_message));

            } else if (i == 1) {

                model.setName(getResources().getString(R.string.email));

                if (email == 1) {
                    model.setSelected(true);
                } else
                    model.setSelected(false);

            }
            //model.setSelected(false);
            optionModels.add(model);

        }

        RecyclerView customAlertRecycler;
        TextView customAlertCancel;
        TextView customAlertSave;

        Dialog dialog = new Dialog(getActivity());
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.custom_alert, container, false);
        ButterKnife.bind(getActivity(), v);
        dialog.setContentView(v);
        customAlertRecycler = (RecyclerView) dialog.findViewById(R.id.customAlertRecycler);
        customAlertCancel = (TextView) dialog.findViewById(R.id.customAlertCancel);
        customAlertSave = (TextView) dialog.findViewById(R.id.customAlertSave);
        customAlertRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        AlertOptionsAdapter adapter = new AlertOptionsAdapter(getActivity(), optionModels);
        customAlertRecycler.setAdapter(adapter);

        adapter.setOnOptionClick(new AlertOptionsAdapter.OnOptionClick() {
            @Override
            public void onOptionClick(int pos) {

                int value = 0;

                if (textMessage == 1) {
                    value = value + 1;
                }

                if (email == 1)
                    value = value + 2;

                if (optionModels.get(pos).isSelected()) {

                    optionModels.get(pos).setSelected(false);

                  /*if(pos == 0){
                        tVlableserviceProvider.setVisibility(View.GONE);
                        tVserviceProvider.setVisibility(View.GONE);
                    }else */

                    if (pos == 0) {

                        textMessage = 0;
                        if (value > 0) {
                            value = value - 1;
                        }
                        tVcellPhone.setVisibility(View.GONE);
                        mobileLinear.setVisibility(View.GONE);
                        tvLblcellPhone.setVisibility(View.GONE);
                        //todo for service provider
                        tVlableserviceProvider.setVisibility(View.GONE);
                        tVserviceProviderLinear.setVisibility(View.GONE);
                        tVserviceProvider.setVisibility(View.GONE);

                    } else {

                        email = 0;

                        if (value >= 2) {
                            value = value - 2;
                        }

                        tvLblEmail.setVisibility(View.GONE);
                        tVEmail.setVisibility(View.GONE);

                    }

                } else {

                    optionModels.get(pos).setSelected(true);

                   /*if(pos==0){
                        tVlableserviceProvider.setVisibility(View.VISIBLE);
                        tVserviceProvider.setVisibility(View.VISIBLE);
                    }else*/

                    if (pos == 0) {

                        if (value <= 2) {
                            value = value + 1;
                        }

                        textMessage = 1;

                        tVcellPhone.setVisibility(View.VISIBLE);
                        mobileLinear.setVisibility(View.VISIBLE);

                        tvLblcellPhone.setVisibility(View.VISIBLE);

                    } else {

                        if (value < 2) {
                            value = value + 2;
                        }

                        email = 1;

                        tvLblEmail.setVisibility(View.VISIBLE);
                        tVEmail.setVisibility(View.VISIBLE);

                    }

                }

                Log.e("value", value + "//");

                if (value == 3) {

                    tVType.setText(getResources().getString(R.string.email) + " , " + getResources().getString(R.string.text_message));
                    //todo for service provider
                    tVlableserviceProvider.setVisibility(View.VISIBLE);
                    tVserviceProviderLinear.setVisibility(View.VISIBLE);
                    tVserviceProvider.setVisibility(View.VISIBLE);

                    cBEnabled.setChecked(true);
                    emailCheckbox.setChecked(true);
                    textCheckbox.setChecked(true);

                } else if (value == 1) {

                    tVType.setText(getResources().getString(R.string.text_message));
                    //todo for service provider
                    tVlableserviceProvider.setVisibility(View.VISIBLE);
                    tVserviceProviderLinear.setVisibility(View.VISIBLE);
                    tVserviceProvider.setVisibility(View.VISIBLE);

                    cBEnabled.setChecked(true);
                    emailCheckbox.setChecked(false);
                    textCheckbox.setChecked(true);

                } else if (value == 2) {

                    tVType.setText(getResources().getString(R.string.email));
                    //todo for service provider
                    tVlableserviceProvider.setVisibility(View.GONE);
                    tVserviceProviderLinear.setVisibility(View.GONE);
                    tVserviceProvider.setVisibility(View.GONE);

                    cBEnabled.setChecked(true);
                    emailCheckbox.setChecked(true);
                    textCheckbox.setChecked(false);

                } else if (value == 0) {

                    tVType.setText("");

                    cBEnabled.setChecked(false);
                    emailCheckbox.setChecked(false);
                    textCheckbox.setChecked(false);

                }

                adapter.notifyDataSetChanged();

            }

        });

        customAlertCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }

        });

        customAlertSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                if (isValidate())
                    CallAddContactAPI(null);
            }

        });

        dialog.show();

    }

    private void updateUI() {

        if (email == 1 && textMessage == 1) {

            tvLblEmail.setVisibility(View.VISIBLE);
            tVEmail.setVisibility(View.VISIBLE);
            tVcellPhone.setVisibility(View.VISIBLE);
            mobileLinear.setVisibility(View.VISIBLE);
            tvLblcellPhone.setVisibility(View.VISIBLE);
            //todo for service provider
            tVlableserviceProvider.setVisibility(View.VISIBLE);
            tVserviceProviderLinear.setVisibility(View.VISIBLE);
            tVserviceProvider.setVisibility(View.VISIBLE);

        } else if (email == 1) {

            tVcellPhone.setVisibility(View.GONE);
            mobileLinear.setVisibility(View.GONE);
            tvLblcellPhone.setVisibility(View.GONE);
            //todo for service provider
            tVlableserviceProvider.setVisibility(View.GONE);
            tVserviceProviderLinear.setVisibility(View.GONE);
            tVserviceProvider.setVisibility(View.GONE);

            tvLblEmail.setVisibility(View.VISIBLE);
            tVEmail.setVisibility(View.VISIBLE);
            tVcellPhone.setText("");

        } else if (textMessage == 1) {

            tVcellPhone.setVisibility(View.VISIBLE);
            mobileLinear.setVisibility(View.VISIBLE);
            tvLblcellPhone.setVisibility(View.VISIBLE);

            //todo for service provider
            tVlableserviceProvider.setVisibility(View.VISIBLE);
            tVserviceProviderLinear.setVisibility(View.VISIBLE);
            tVserviceProvider.setVisibility(View.VISIBLE);

            tvLblEmail.setVisibility(View.GONE);
            tVEmail.setVisibility(View.GONE);
            tVcellPhone.setText("");

        }

        tVEmail.setText("");
        tVcellPhone.setText("");
        tVcellPhone.setText("");

    }

    SlideUp slideUp;

    public void slideUpDown() {

        if (!slideUp.isVisible()) {
            btnAddContact.setVisibility(View.VISIBLE);
            btnAddContact.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.text_grey));
            btnAddContact2.setVisibility(View.GONE);
            slideUp.show();
            TvNoData.setVisibility(View.GONE);
            cBEnabled.setChecked(true);
            emailCheckbox.setChecked(true);
            textCheckbox.setChecked(true);

        } else {

            cBEnabled.setChecked(false);
            emailCheckbox.setChecked(false);
            textCheckbox.setChecked(false);
            if (contactListModelArrayList.isEmpty()) {
                TvNoData.setVisibility(View.VISIBLE);
            }
            slideUp.hide();

        }

    }

    void initSlider() {

        slideUp = new SlideUpBuilder(hiddenPanel).withListeners(new SlideUp.Listener.Events() {

            public void onSlide(float percent) {

            }

            @Override
            public void onVisibilityChanged(int visibility) {

                if (visibility == View.GONE) {

                    btnAddContact2.setVisibility(View.VISIBLE);
                }

            }

        }).withStartGravity(Gravity.BOTTOM).withLoggingEnabled(true).withGesturesEnabled(true).withStartState(SlideUp.State.HIDDEN).build();

    }

    private void callApiGetNotification() {

        BaseRequest baseRequest = new BaseRequest(getActivity(), this);
        baseRequest.setLoaderView(custom_pb);

        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                Log.e("notires", fullResponse + "//");
                contactListModelArrayList.clear();
                Gson gson = new Gson();
                ContactModel deviceParentModel = gson.fromJson(fullResponse, ContactModel.class);
                contactListModelArrayList = deviceParentModel.getContactListModelArrayList();

                for (int i = 0; i < contactListModelArrayList.size(); i++) {
                    expandableAdapter.collapse(i);
                }

                for (int i = 0; i < contactListModelArrayList.size(); i++) {

                    for (int j = 0; j < serviceModelArrayList.size(); j++) {

                        if (serviceModelArrayList.get(j).getProviderCode().equalsIgnoreCase(contactListModelArrayList.get(i).getCellProvider())) {

                            contactListModelArrayList.get(i).setServicePrivederName(serviceModelArrayList.get(j).getProvider());
                            break;

                        }

                    }

                }

                expandableAdapter.setList(contactListModelArrayList);
                noData(TvNoData, contactListModelArrayList);
                expandableAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }

        });

        baseRequest.callAPIGET(1, new HashMap<String, String>(), getResources().getString(R.string.api_getContactList));

    }

    public void sendSmsTestAPI() {

        BaseRequest baseRequest = new BaseRequest(getActivity(), this);
        baseRequest.setRunInBackground(false);
        baseRequest.setContainer(custom_pb, null);
        //baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                //callApiGetNotification();
                try {

                    Log.e("sendSmsTestAPI",fullResponse);
                    JSONObject object = new JSONObject(fullResponse);

                    if (object.has("message")) {
                        Toast.makeText(getActivity(), object.optString("message"), Toast.LENGTH_SHORT).show();

                        Log.e("sendSmsTestAPI",object.optString("message"));
                    }

                } catch (JSONException e) {

                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

                Log.e("sndsmserror", message + "//");
                Functions.Alert(getActivity(), message, Functions.AlertType.Error);

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }

        });

        JsonObject object = null;
        object = Functions.getInstance().getJsonObject("mobile", tVcellPhone.getText().toString().trim(), "dialCode", notiCodePicker.getSelectedCountryCodeWithPlus(), "cellProvider", serviceProviderCode);

        // object = Functions.getInstance().getJsonObject("mobile", dialCode + contactListModel.getMobile()),"dialCode",dialCode ;
        Log.e("testsmsparam", object.toString() + "//");
        baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_twillio_sms));

    }

    public void callSendSmsTestAPI(ContactListModel contactListModel, String dialCode) {

        BaseRequest baseRequest = new BaseRequest(getActivity(), this);
        baseRequest.setRunInBackground(false);
        baseRequest.setContainer(custom_pb, null);
        //baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {

                //callApiGetNotification();

                try {

                    JSONObject object = new JSONObject(fullResponse);

                    if (object.has("message")) {

                        Toast.makeText(getActivity(), object.optString("message"), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {

                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

                Log.e("sndsmserror", message + "//");
                Functions.Alert(getActivity(), message, Functions.AlertType.Error);

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }

        });

        JsonObject object = null;
        object = Functions.getInstance().getJsonObject("mobile", contactListModel.getMobile(), "dialCode", dialCode, "cellProvider", contactListModel.getServicePrivederName());
      //object = Functions.getInstance().getJsonObject("mobile", dialCode + contactListModel.getMobile()),"dialCode",dialCode ;
        Log.e("testsmsparam", object.toString() + "//");
        baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_twillio_sms));

    }

    public void sendEmailTestAPI(String email) {

        BaseRequest baseRequest = new BaseRequest(getActivity(), this);
        baseRequest.setRunInBackground(false);
        baseRequest.setContainer(custom_pb, null);
        //baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {

                try {

                    JSONObject object = new JSONObject(fullResponse);

                    if (object.has("message")) {

                        Toast.makeText(getActivity(), object.optString("message"), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {

                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

                Log.e("sendemlerror", message + "//");
                Functions.Alert(getActivity(), message, Functions.AlertType.Error);

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }

        });

        JsonObject object = null;
        object = Functions.getInstance().getJsonObject("email", email);

        baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_twillio_email));

    }

    public void callSendEmailTestAPI(ContactListModel contactListModel) {

        BaseRequest baseRequest = new BaseRequest(getActivity(), this);
        baseRequest.setRunInBackground(false);
        baseRequest.setContainer(custom_pb, null);
        //baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                //callApiGetNotification();

                try {

                    JSONObject object = new JSONObject(fullResponse);

                    if (object.has("message")) {

                        Toast.makeText(getActivity(), object.optString("message"), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {

                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

                Log.e("sendemlerror", message + "//");
                Functions.Alert(getActivity(), message, Functions.AlertType.Error);

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }
        });

        JsonObject object = null;
        object = Functions.getInstance().getJsonObject("email", contactListModel.getEmail());

        baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_twillio_email));

    }

    //todo for implement the voice call functionality currently API is pending from dipak side
    public void callSendVoiceTestAPI(ContactListModel contactListModel, String dialCode) {

        BaseRequest baseRequest = new BaseRequest(getActivity(), this);
        baseRequest.setRunInBackground(false);
        baseRequest.setContainer(custom_pb, null);
        //baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {

                //callApiGetNotification();

                try {

                    JSONObject object = new JSONObject(fullResponse);

                    if (object.has("message")) {

                        Toast.makeText(getActivity(), object.optString("message"), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

                Log.e("sendvoiceerror", message + "//");
                Functions.Alert(getActivity(), message, Functions.AlertType.Error);

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }

        });

        JsonObject object = null;
        object = Functions.getInstance().getJsonObject("mobile", dialCode + contactListModel.getMobile());
        Log.e("voicecal", object.toString() + "//");
        baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_twillio_voice));

    }

    public void CallDeleteContactAPI(ContactListModel contactListModel) {

        BaseRequest baseRequest = new BaseRequest(getActivity(), this);
        baseRequest.setRunInBackground(false);
        baseRequest.setContainer(custom_pb, null);
        //baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                callApiGetNotification();
            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {
                Log.e("deleteerror", message + "//");
                Functions.Alert(getActivity(), message, Functions.AlertType.Error);
            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }
        });

        JsonObject object = null;
        object = Functions.getInstance().getJsonObject("id", contactListModel.getId());

        baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_deleteContact));
    }

    public void CallAddContactAPI(ContactListModel contactListModel) {

        BaseRequest baseRequest = new BaseRequest(getActivity(), this);
        baseRequest.setRunInBackground(false);
        baseRequest.setContainer(custom_pb, null);
        //baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                Log.e("fullresponse", fullResponse );
                slideUpDown();
                tVEmail.setText("");
                tVcellPhone.setText("");
                tVFirstName.setText("");
                tVLastName.setText("");
                tVserviceProvider.setText("----");
                email = 1;
                tVType.setText(getString(R.string.email));
                updateUI();
                callApiGetNotification();
            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

                Log.e("addcontcterror", message + "//");
                Functions.Alert(getActivity(), message, Functions.AlertType.Error);

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }

        });
        Log.e( "dial",notiCodePicker.getSelectedCountryNameCode().toUpperCase() );
        JsonObject object = null;
        object = Functions.getInstance().getJsonObject("emailActive", email == 1 ? "1" : "0",
                "email", tVEmail.getText().toString().trim().length() > 0 ? tVEmail.getText().toString().trim() : (tVFirstName.getText().toString().trim()
                        + tVLastName.getText().toString().trim())
                , "enableStatus", cBEnabled.isChecked() ? "1" : "0"
                , "defaultStatus", "1"
                , "mobile", tVcellPhone.getText().toString().trim()
                , "firstName", tVFirstName.getText().toString().trim()
                , "dialCode", notiCodePicker.getSelectedCountryCodeWithPlus()
                , "countryId", notiCodePicker.getSelectedCountryNameCode().toUpperCase()
                , "lastName", tVLastName.getText().toString().trim()
                //,"voiceActive", type != Type.Voice ? "0" : "1"
                , "voiceActive", "0"
                , "cellProvider", "" + serviceProviderCode
                , "smsActive", textMessage != 1 ? "0" : "1");

        Log.e("addcontct", object.toString() + "//");

        if (contactListModel != null) {

            object.addProperty("id", contactListModel.getId());
            baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_editContact));

        } else {

            baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_addContact));

        }

    }

    public void CallUpdateContactAPI(ContactListModel contactListModel) {

        Log.e("updatellll", "okk");
        BaseRequest baseRequest = new BaseRequest(getActivity(), this);
        baseRequest.setRunInBackground(false);
        baseRequest.setContainer(custom_pb, null);
        //baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                Log.e("testresponse", fullResponse );
                btnAddContact2.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.blue_dark));
                btnAddContact2.setClickable(true);
                callApiGetNotification();

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

                Log.e("updatecontcerror", message + "//");
                Functions.Alert(getActivity(), message, Functions.AlertType.Error);

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }

        });

        JsonObject object = null;

        object = Functions.getInstance().getJsonObject("emailActive", contactListModel.getEmailActive() ? "1" : "0", "email", contactListModel.getEmail()
                , "enableStatus", contactListModel.getEnableStatus() ? "1" : "0"
                , "defaultStatus", "1"
                , "mobile", contactListModel.getMobile()
                , "firstName", contactListModel.getFirstName()
                , "lastName", contactListModel.getLastName()
                , "dialCode", "+"+contactListModel.getMobileCode()
                , "countryId", contactListModel.getCountryId().toUpperCase()
                , "voiceActive", contactListModel.getVoiceActive() ? "1" : "0"
                , "cellProvider", "" + contactListModel.getCellProvider()
                , "smsActive", contactListModel.getSmsActive() ? "1" : "0");
        Log.e( "dial ",contactListModel.getCountryId().toUpperCase());
        Log.e("updatecontct1", object.toString() + "//");

        if (contactListModel != null) {

            object.addProperty("id", contactListModel.getId());
            baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_editContact));

        } else {

            baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_addContact));

        }

    }

    public void CallSentTestAPI(ContactListModel contactListModel) {

        BaseRequest baseRequest = new BaseRequest(getActivity(), this);
        baseRequest.setRunInBackground(false);
        baseRequest.setContainer(custom_pb, null);
        //baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {

                Log.e("CallSentTestAPI",fullResponse);
                Functions.Alert(getActivity(), "Test Notification", Functions.AlertType.Success);

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

                Log.e("calsenderror", message + "//");
                Functions.Alert(getActivity(), message, Functions.AlertType.Error);

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }

        });

        JsonObject object = null;

        object = Functions.getInstance().getJsonObject(
                "id", contactListModel.getId(),
                "emailActive", contactListModel.getEmailActive() ? "1" : "0", "email", contactListModel.getEmail()
                , "enableStatus", contactListModel.getEnableStatus() ? "1" : "0"
                , "defaultStatus", "1"
                , "firstName", contactListModel.getFirstName()
                , "lastName", contactListModel.getLastName()
                , "voiceActive", contactListModel.getVoiceActive() ? "1" : "0"
                , "cellProvider", contactListModel.getCellProvider()
                , "smsActive", contactListModel.getSmsActive() ? "1" : "0");

        baseRequest.callAPIPost(1, object, contactListModel.getEmailActive() ? getResources().getString(R.string.api_testMail) : getString(R.string.api_testSms));

    }

    private boolean isValidate() {

        if (tVFirstName.getText().toString().trim().length() <= 0) {
            Functions.Alert(getActivity(), "Please enter first name.", Functions.AlertType.Error);
            return false;

        } else if (tVLastName.getText().toString().trim().length() <= 0) {
            Functions.Alert(getActivity(), "Please enter last name.", Functions.AlertType.Error);
            return false;

        } else if (email == 1 && tVEmail.getText().toString().trim().length() <= 0) {
            Functions.Alert(getActivity(), "Please enter email.", Functions.AlertType.Error);
            return false;

        } else if (email == 1 && !Functions.isValidEmaillId(tVEmail.getText().toString().trim())) {
            Functions.Alert(getActivity(), "Please enter valid email.", Functions.AlertType.Error);
            return false;

        } else if (textMessage == 1 && tVcellPhone.getText().toString().trim().length() <= 0) {

            /*if(tvMobileCountry.getText().toString().trim().contains("-")){
                Functions.Alert(getActivity(), "Please select country.", Functions.AlertType.Error);
                return false;
            }*/

            if (tVserviceProvider.getText().toString().trim().equals("------") ) {
                Functions.Alert(getActivity(), "Please select service provider.", Functions.AlertType.Error);
                return false;
            }

            Functions.Alert(getActivity(), "Please enter mobile no.", Functions.AlertType.Error);
            return false;

        } else if (textMessage == 1 && tVcellPhone.getText().toString().trim().length() > 0 && tVserviceProvider.getText().toString().trim().equals("------")) {

            //if(tVserviceProvider.getText().toString().trim().contains("-")) {
            Functions.Alert(getActivity(), "Please select service provider.", Functions.AlertType.Error);
            return false;
           /*}else
                return true;*/

        } else return true;

    }

    String serviceProvider = "";
    String serviceProviderCode = "";
    ArrayList<CellProviderModel> serviceModelArrayList = new ArrayList<>();

    private void callApiGetServiceProvider(TextView textView, boolean isShownDialog) {

        BaseRequest baseRequest = new BaseRequest(getActivity(), this);
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
                Log.e( "onSuccess: ",isShownDialog+"" );
                if (isShownDialog) {

                            Functions.showListSelection(getActivity(), R.string.select_service_provider, new OnItemClickAdapter() {
                            @Override
                        public void onClick(int i, int position, String item) {

                            textView.setText(item);
                            serviceProvider = item;
                            serviceProviderCode = serviceModelArrayList.get(position).getProviderCode();

                        }

                    }, list);

                } else {
                    Log.e( "onSuccess: ","working" );
                    callApiGetNotification();

                }

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {
                Log.e("servicefailur", message + "//");
            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }

        });

        baseRequest.callAPIGET(1, new HashMap<String, String>(), getResources().getString(R.string.api_getCellProviderList));

    }

}

