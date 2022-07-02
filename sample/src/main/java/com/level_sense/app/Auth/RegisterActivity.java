package com.level_sense.app.Auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.level_sense.app.R;
import com.level_sense.app.RetroFit.BaseRequest;
import com.level_sense.app.RetroFit.RequestReceiver;
import com.level_sense.app.Session.SessionParam;
import com.level_sense.app.Utility.Functions;
import com.tapadoo.alerter.OnHideAlertListener;
import org.json.JSONException;
import org.json.JSONObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.email_edt)
    EditText emailEdt;
    @BindView(R.id.password_edt)
    EditText passwordEdt;
    @BindView(R.id.custom_pb)
    View custom_pb;
    @BindView(R.id.first_name_edt)
    EditText firstNameEdt;
    @BindView(R.id.last_name_edt)
    EditText lastNameEdt;
    String email, password;
    @BindView(R.id.password_eye_icon)
    ImageView passwordEyeIcon;

    @BindView(R.id.toolbar_common)
    Toolbar toolbar_common;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.toolbarBackImg)
    ImageView toolbarBackImg;

    boolean ispassword_visible = false;
    private String forgotUrl = "https://www.level-sense.com/account/login?resetpassword=%22%22";
    private String registerUrl = "https://www.level-sense.com/account/register";
    private String value = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_icon);
        getSupportActionBar().setTitle(R.string.register);*/

        toolbar_title.setText(R.string.register);
        toolbarBackImg.setImageResource(R.drawable.ic_back_icon);
        toolbarBackImg.setOnClickListener(this);
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle action bar item clicks here. The action bar will
        //automatically handle clicks on the Home/Up button, so long
        //as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.register_btn, R.id.password_eye_icon, R.id.login_btn})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.toolbarBackImg:
                onBackPressed();
                break;

            case R.id.register_btn:

                if (isValidate())
                    CallRegisterPI();

                break;

            case R.id.login_btn:

                finish();

                break;

            case R.id.password_eye_icon:

                if (ispassword_visible) {

                    ispassword_visible = false;
                    PasswordVisible(true);
                    passwordEyeIcon.setBackground(getResources().getDrawable(R.drawable.ic_eye_unselected));
                    passwordEdt.setSelection(passwordEdt.getText().length());

                } else {

                    PasswordVisible(false);
                    ispassword_visible = true;
                    passwordEyeIcon.setBackground(getResources().getDrawable(R.drawable.ic_eye_blue));
                    passwordEdt.setSelection(passwordEdt.getText().length());

                }

                break;

        }

    }

    private boolean isValidate() {

        email = emailEdt.getText().toString().trim();
        password = passwordEdt.getText().toString().trim();

        if (firstNameEdt.getText().toString().trim().length() <= 0) {
            Functions.Alert(RegisterActivity.this, "Please enter first name.", Functions.AlertType.Error);
            return false;
        } else if (lastNameEdt.getText().toString().trim().length() <= 0) {
            Functions.Alert(RegisterActivity.this, "Please enter last name.", Functions.AlertType.Error);
            return false;
        } else if (email.length() <= 0) {
            Functions.Alert(RegisterActivity.this, "Please enter email.", Functions.AlertType.Error);
            return false;
        } else if (!isValidEmail(email)) {
            Functions.Alert(RegisterActivity.this, "Please enter valid email.", Functions.AlertType.Error);
            return false;
        } else if (password.length() <= 0) {
            Functions.Alert(RegisterActivity.this, "Please enter password.", Functions.AlertType.Error);
            return false;
        } else if (password.length() < 8) {
            Functions.Alert(RegisterActivity.this, "Password length must be 8 character.", Functions.AlertType.Error);
            return false;
        } else return true;

    }

    public final static boolean isValidEmail(CharSequence target) {

        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());

    }

    public void CallRegisterPI() {

        BaseRequest baseRequest = new BaseRequest(this);
        baseRequest.setRunInBackground(false);
        baseRequest.setContainer(custom_pb, null);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {

                try {

                    JSONObject jsonObject = new JSONObject(fullResponse);
                    SessionParam.setSaveSessionKey(RegisterActivity.this, jsonObject.getString("sessionKey"));

                } catch (JSONException e) {

                }

                CallLoginAPI(email, password);
                Functions.Alert(RegisterActivity.this, "You Are Successfully Registered. Please Login. This will also be the same E-Mail and Password to use on the Website.", Functions.AlertType.Success, new OnHideAlertListener() {
                    @Override
                    public void onHide() {

                    }

                });

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

                Functions.Alert(RegisterActivity.this, message, Functions.AlertType.Error);

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }

        });

        JsonObject object = null;
        object = Functions.getInstance().getJsonObject("email", email, "password", password, "firstName", firstNameEdt.getText().toString().trim(), "lastName", lastNameEdt.getText().toString().trim());
        Log.e("registerreq", object.toString() + "//");
        baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_registerUser_new));

    }

    public void CallLoginAPI(String email, String password) {

        BaseRequest baseRequest = new BaseRequest(this);
        baseRequest.setRunInBackground(false);
        baseRequest.setContainer(custom_pb, null);
        //baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {

                try {

                    JSONObject jsonObject = new JSONObject(fullResponse);
                    SessionParam.setSaveSessionKey(RegisterActivity.this, jsonObject.getString("sessionKey"));

                } catch (JSONException e) {

                }

                //startActivity(DashBoardActivity.getIntent(RegisterActivity.this));
                Intent intent=new Intent(RegisterActivity.this,DashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

                Functions.Alert(RegisterActivity.this, message, Functions.AlertType.Error);
            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }
        });
        JsonObject object = null;
        object = Functions.getInstance().getJsonObject(
                "email", email,
                "password", password


        );

        baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_login));

    }

    public void PasswordVisible(boolean password_visible) {
        if (password_visible) {
            passwordEdt.setTransformationMethod(new PasswordTransformationMethod());
            passwordEdt.setSelection(passwordEdt.length());
        } else {
            passwordEdt.setTransformationMethod(null);
            passwordEdt.setSelection(passwordEdt.length());
        }
    }

}
