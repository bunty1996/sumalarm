package com.level_sense.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.level_sense.app.model.ProfileUserModel;
import com.level_sense.app.R;
import com.level_sense.app.RetroFit.BaseRequest;
import com.level_sense.app.RetroFit.RequestReceiver;
import com.level_sense.app.Session.SessionParam;
import com.level_sense.app.Utility.Functions;
import com.level_sense.app.Utility.OnItemClickAdapter;
import com.level_sense.app.model.CountryModel;
import com.level_sense.app.model.CountryParentModel;
import com.level_sense.app.model.ProfileModel;
import com.level_sense.app.model.StateListModel;
import com.level_sense.app.model.StateParentModel;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonalInfoFragment extends Fragment {
    @BindView(R.id.tVFirstName)
    EditText tVFirstName;
    @BindView(R.id.tVLastName)
    EditText tVLastName;
    @BindView(R.id.tVEmail)
    EditText tVEmail;
    @BindView(R.id.tVAddress)
    EditText tVAddress;
    @BindView(R.id.tVCity)
    EditText tVCity;
    @BindView(R.id.tVpostalCode)
    EditText tVpostalCode;
    @BindView(R.id.tVcountry)
    TextView tVcountry;
    @BindView(R.id.tVState)
    TextView tVState;
    @BindView(R.id.tvTimeZone)
    TextView tvTimeZone;
    @BindView(R.id.submit_btn)
    Button submitBtn;
    @BindView(R.id.custom_pb)
    AVLoadingIndicatorView customPb;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.message_tv)
    TextView messageTv;
    @BindView(R.id.loader_view_ll)
    LinearLayout loaderViewLl;
    ArrayList<CountryModel> countryModelArrayList = new ArrayList<>();
    ArrayList<StateListModel> stateModelArrayList = new ArrayList<>();
    String stateId;
    String stateName;
    ProfileUserModel profileModel;
    String countryId;
    String countryName;
    ArrayList<String> timeZoneList = new ArrayList<>();

    public PersonalInfoFragment() {

    }

    //TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PersonalInfoFragment newInstance() {
        PersonalInfoFragment fragment = new PersonalInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_persional_info, container, false);
        ButterKnife.bind(this, view);
        timeZoneList.clear();
        callApiGetUser();
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

    private void callApiGetUser() {

        BaseRequest baseRequest = new BaseRequest(getActivity(), this);
        baseRequest.setLoaderView(customPb);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {

                SessionParam.setPrefData(getActivity(), ProfileModel.class.getName(), fullResponse);

                setData();
                callApiGetCountry(false);
                callApiGetTimeZone(false);

                if (tVEmail.getText().toString().trim().length() > 0) {
                    tVEmail.setEnabled(false);
                }

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }

        });

        baseRequest.callAPIGET(1, new HashMap<String, String>(), getResources().getString(R.string.api_get_user));

    }

    private void callApiGetCountry(boolean isFromClick) {

        BaseRequest baseRequest = new BaseRequest(getActivity(), this);
        baseRequest.setLoaderView(customPb);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
//                Log.e("fullResponse",fullResponse);
//                Log.e("fullResponse_dataObject",dataObject.toString());
//
//                Log.e("list",countryModelArrayList.size()+"");
//                for (CountryModel countryModel1 : countryModelArrayList) {
//                    Log.e("fullResponse11",countryModel1.getName()+"");
//
//                    if (countryModel1.getName().equalsIgnoreCase(profileModel.getCountry())) {
                Gson gson = new Gson();
                CountryParentModel countryModel = gson.fromJson(fullResponse, CountryParentModel.class);
                countryModelArrayList = countryModel.getCountryModelArrayList();

//                        countryName = countryModel1.getName();
//                        countryId = "" + countryModel1.getId();
//
//                        break;

              //     }

                //}

                if (isFromClick) {

                    ArrayList<String> list = new ArrayList<>();

                    for (int i = 0; i < countryModelArrayList.size(); i++) {

                        list.add(countryModelArrayList.get(i).getName());

                    }

                    Functions.showListSelection(getActivity(), R.string.select_country, new OnItemClickAdapter() {
                        @Override
                        public void onClick(int i, int position, String item) {

                            countryId = "" + countryModelArrayList.get(position).getId();
                            countryName = "" + countryModelArrayList.get(position).getName();
                            tVcountry.setText(countryModelArrayList.get(position).getName());
                            callApiGetState(countryId, false);

                        }

                    }, list);

                }

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

        //baseRequest.callAPIGET(1, new HashMap<String, String>(), getResources().getString(R.string.api_get_country));
        baseRequest.callAPIPost(1, object,getActivity().getResources().getString(R.string.api_get_country));

    }

        private void callApiGetState(String countryId, boolean isFromClick) {
        BaseRequest baseRequest = new BaseRequest(getActivity(), this);
        baseRequest.setLoaderView(customPb);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                Log.e( "onSuccess: ",fullResponse+"/////"+dataObject );
                Gson gson = new Gson();
                StateParentModel countryModel = gson.fromJson(fullResponse, StateParentModel.class);
                stateModelArrayList = countryModel.getStateList();

                if (isFromClick) {

                    ArrayList<String> slist = new ArrayList<>();

                    for (int i = 0; i < stateModelArrayList.size(); i++) {

                        slist.add(stateModelArrayList.get(i).getName());

                    }

                    Functions.showListSelection(getActivity(), R.string.select_state, new OnItemClickAdapter() {
                        @Override
                        public void onClick(int i, int position, String item) {
                            stateId = "" + stateModelArrayList.get(position).getId();
                            stateName = "" + stateModelArrayList.get(position).getName();
                            tVState.setText(stateName);
                        }
                    }, slist);

                }

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }
        });
        JsonObject object = new JsonObject();
        object.addProperty("countryId", Integer.parseInt(countryId));
      /*object = Functions.getInstance().getJsonObject(
         "countryId", countryId
          );*/
            Log.e("callApiGetState: ",object.toString() );
        baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_get_state));
    }

    private void callApiGetTimeZone(boolean value) {

        BaseRequest baseRequest = new BaseRequest(getActivity(), this);
        baseRequest.setLoaderView(customPb);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {

                try {

                    ArrayList<String> objectList = new ArrayList<>();
                    objectList.clear();
                    JSONObject object = new JSONObject(fullResponse);
                    JSONObject jsonObject = object.optJSONObject("timezoneList");

                    Iterator<String> iterator = jsonObject.keys();

                    while (iterator.hasNext()) {
                        //String str = iterator.next();
                        objectList.add(iterator.next());

                    }

                    timeZoneList.clear();

                    for (int i = 0; i < objectList.size(); i++) {

                        JSONObject object1 = jsonObject.optJSONObject(objectList.get(i));
                        Iterator<String> iterator1 = object1.keys();

                        while (iterator1.hasNext()) {

                            //String str = iterator.next();
                            timeZoneList.add(iterator1.next());

                        }

                    }

                    if (value) {

                        Functions.showListSelection(getActivity(), R.string.select_timezone, new OnItemClickAdapter() {
                            @Override
                            public void onClick(int i, int position, String item) {
                                tvTimeZone.setText(timeZoneList.get(position));
                            }
                        }, timeZoneList);
                    }

                } catch (JSONException e) {

                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }
        });

        //baseRequest.callAPIGET(1, new HashMap<String, String>(), getResources().getString(R.string.api_time_zone));
        baseRequest.callAPIGETWithBaseUrl(1, new HashMap<String, String>(), getResources().getString(R.string.api_time_zone));

    }

    private void setData() {
        ProfileUserModel profileUserModel = SessionParam.getProfileData(getActivity()).getUser();
        profileModel = profileUserModel;
        tVFirstName.setText(profileUserModel.getFirstName());
        tVLastName.setText(profileUserModel.getLastName());
        tVEmail.setText(profileUserModel.getEmail());
        tVAddress.setText(profileUserModel.getAddress());
        tVCity.setText(profileUserModel.getCity());
        tVpostalCode.setText(profileUserModel.getZipcode());
        tVCity.setText(profileUserModel.getCity());
        tVcountry.setText(profileUserModel.getCountry());
        tVState.setText(profileUserModel.getState());
        tvTimeZone.setText(profileUserModel.getTimezone());
    }

    @OnClick({R.id.tVcountry, R.id.tVState, R.id.tvTimeZone, R.id.submit_btn})
    public void onViewClicked(View view) {

        switch (view.getId()) {

            case R.id.tVcountry:
                    hideKeyboard(getActivity());
                if (countryModelArrayList != null && countryModelArrayList.size() > 0) {
                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < countryModelArrayList.size(); i++) {
                        list.add(countryModelArrayList.get(i).getName());
                    }
                    Functions.showListSelection(getActivity(), R.string.select_country, new OnItemClickAdapter() {
                        @Override
                        public void onClick(int i, int position, String item) {
                            //E/flags: {"success":true,"countryList":[{"name":"United States","code":"US","dialCode":"+1","id":1,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/us.png"},{"name":"Canada","code":"CA","dialCode":"+1","id":2,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/ca.png"},{"name":"Mexico","code":"MX","dialCode":"+52","id":3,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/mx.png"},{"name":"American Samoa","code":"AS","dialCode":"+1684","id":4,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/as.png"},{"name":"Guam","code":"GU","dialCode":"+1671","id":5,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/gu.png"},{"name":"Northern Mariana Islands","code":"MP","dialCode":"+1670","id":6,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/mp.png"},{"name":"Puerto Rico","code":"PR","dialCode":"+1787","id":7,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/pr.png"},{"name":"Virgin Islands (US)","code":"VI","dialCode":"+1340","id":8,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/vi.png"},{"name":"Australia","code":"AU","dialCode":"+61","id":9,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/au.png"},{"name":"New Zealand","code":"NZ","dialCode":"+64","id":10,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/nz.png"},{"name":"Argentina","code":"AR","dialCode":"+54","id":11,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/ar.png"},{"name":"Austria","code":"AT","dialCode":"+43","id":12,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/at.png"},{"name":"Belgium","code":"BE","dialCode":"+32","id":13,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/be.png"},{"name":"Brazil","code":"BR","dialCode":"+55","id":14,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/br.png"},{"name":"Chile","code":"CL","dialCode":"+56","id":15,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/cl.png"},{"name":"Colombia","code":"CO","dialCode":"+57","id":16,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/co.png"},{"name":"Denmark","code":"DK","dialCode":"+45","id":17,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/dk.png"},{"name":"Ecuador","code":"EC","dialCode":"+593","id":18,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/ec.png"},{"name":"Finland","code":"FI","dialCode":"+358","id":19,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/fi.png"},{"name":"France","code":"FR","dialCode":"+33","id":20,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/fr.png"},{"name":"Germany","code":"DE","dialCode":"+49","id":21,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/de.png"},{"name":"India","code":"IN","dialCode":"+91","id":22,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/in.png"},{"name":"Israel","code":"IL","dialCode":"+972","id":23,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/il.png"},{"name":"Italy","code":"IT","dialCode":"+39","id":24,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/it.png"},{"name":"Norway","code":"NO","dialCode":"+47","id":25,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/no.png"},{"name":"Poland","code":"PL","dialCode":"+48","id":26,"flag":"https://dash.level-sense.com/Level-Sense-API/web/bundles/dbadmin/app/img/flags/pl.png"},{"name":"Russia","code":"RU","dialCode":"+70","id":27,"flag":"https://
                            countryId = "" + countryModelArrayList.get(position).getId();
                            countryName = "" + countryModelArrayList.get(position).getName();
                            tVcountry.setText(countryModelArrayList.get(position).getName());
                            callApiGetState(countryId, false);

                        }
                    }, list);
                } else {
                    callApiGetCountry(true);
                }
                break;

            case R.id.tVState:
              hideKeyboard(getActivity());
                if (countryName != null && countryName.length() > 0) {

                    if (stateModelArrayList != null && stateModelArrayList.size() > 0) {
                        ArrayList<String> slist = new ArrayList<>();
                        for (int i = 0; i < stateModelArrayList.size(); i++) {
                            slist.add(stateModelArrayList.get(i).getName());
                        }

                        Functions.showListSelection(getActivity(), R.string.select_state, new OnItemClickAdapter() {
                            @Override
                            public void onClick(int i, int position, String item) {
                                stateId = "" + stateModelArrayList.get(position).getId();
                                stateName = "" + item;
                                tVState.setText(stateName);
                            }
                        }, slist);
                    } else {
                        callApiGetState(countryId, true);
                    }
                } else {
                    Functions.Alert(getActivity(), "Please select country first.", Functions.AlertType.Error);

                }
                break;

            //todo for show timezone data
            case R.id.tvTimeZone:
                hideKeyboard(getActivity());
                if (timeZoneList != null && timeZoneList.size() > 0) {

                    Functions.showListSelection(getActivity(), R.string.select_timezone, new OnItemClickAdapter() {
                        @Override
                        public void onClick(int i, int position, String item) {

                            tvTimeZone.setText(timeZoneList.get(position));

                        }
                    }, timeZoneList);

                } else {

                    callApiGetTimeZone(true);

                }

                break;

            case R.id.submit_btn:

                if (isValidate()) {
                    callApiEditUser();
                }
        }

    }

    private void callApiEditUser() {

        BaseRequest baseRequest = new BaseRequest(getActivity());
        baseRequest.setRunInBackground(false);
        baseRequest.setContainer(customPb, null);
        //baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                Functions.Alert(getActivity(), getString(R.string.update_profile_message), Functions.AlertType.Success);
            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

                Functions.Alert(getActivity(), message, Functions.AlertType.Error);
            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }
        });
        JsonObject object = null;
        object = Functions.getInstance().getJsonObject(
                "email", tVEmail.getText().toString().trim(),
                "firstName", tVFirstName.getText().toString().trim(),
                "lastName", tVLastName.getText().toString().trim(),
                "state", stateName,
                "city", tVCity.getText().toString().trim(),
                "address", tVAddress.getText().toString().trim(),
                "zipcode", tVpostalCode.getText().toString().trim(),
                "country", tVcountry.getText().toString().trim(),
                //todo for add timezone
                "timezone", tvTimeZone.getText().toString().trim()
        );

        baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_edit_user));
    }

    private boolean isValidate() {
        if (tVEmail.getText().toString().trim().length() <= 0) {
            Functions.Alert(getActivity(), "Please enter email.", Functions.AlertType.Error);
            return false;
        } else if (tVFirstName.getText().toString().trim().length() <= 0) {
            Functions.Alert(getActivity(), "Please enter first name.", Functions.AlertType.Error);
            return false;

        } else if (tVLastName.getText().toString().trim().length() <= 0) {
            Functions.Alert(getActivity(), "Please enter last name.", Functions.AlertType.Error);
            return false;

        } else if (tVAddress.getText().toString().trim().length() <= 0) {
            Functions.Alert(getActivity(), "Please enter address.", Functions.AlertType.Error);
            return false;

        } else if (tVCity.getText().toString().trim().length() <= 0) {
            Functions.Alert(getActivity(), "Please enter city.", Functions.AlertType.Error);
            return false;

        } else if (tVpostalCode.getText().toString().trim().length() <= 0) {
            Functions.Alert(getActivity(), "Please enter postal code.", Functions.AlertType.Error);
            return false;

        } else if (tVcountry.getText().toString().trim().length() <= 0) {
            Functions.Alert(getActivity(), "Please enter country.", Functions.AlertType.Error);
            return false;
        } else if (tVState.getText().toString().trim().length() <= 0) {
            Functions.Alert(getActivity(), "Please enter State.", Functions.AlertType.Error);
            return false;
        } else return true;
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}


