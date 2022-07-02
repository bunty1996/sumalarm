package com.level_sense.app.Auth;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.level_sense.app.R;
import com.level_sense.app.RetroFit.BaseRequest;
import com.level_sense.app.RetroFit.RequestReceiver;
import com.level_sense.app.Session.SessionParam;
import com.level_sense.app.Utility.Functions;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotResetPass extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.forgotPasswordEdit)
    EditText forgotPasswordEdit;
    @BindView(R.id.forgotResetPasswordSubmit)
    Button forgotResetPasswordSubmit;
    @BindView(R.id.custom_pb)
    View custom_pb;

    @BindView(R.id.toolbar_common)
    Toolbar toolbar_common;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.toolbarBackImg)
    ImageView toolbarBackImg;

    @BindView(R.id.forgotPasswordOtp)
    EditText forgotPasswordOtp;
    @BindView(R.id.confirmPasswordEdit)
    EditText confirmPasswordEdit;

    @BindView(R.id.new_password_eye_icon)
    ImageView new_password_eye_icon;

    @BindView(R.id.confirm_password_eye_icon)
    ImageView confirm_password_eye_icon;

    boolean ispassword_visible = false;

    private String email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.forgot_reset_pass);
        ButterKnife.bind(this);

        if (getIntent() != null) {

            email = getIntent().getStringExtra("email");
            //otp = getIntent().getStringExtra("otp");

        }

        toolbar_title.setText(R.string.reset_pass);
        toolbarBackImg.setImageResource(R.drawable.ic_back_icon);
        toolbarBackImg.setOnClickListener(this);
        forgotResetPasswordSubmit.setOnClickListener(this);
        new_password_eye_icon.setOnClickListener(this);
        confirm_password_eye_icon.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.new_password_eye_icon:
                showNewHidePass();
                break;

            case R.id.confirm_password_eye_icon:
                showConfirmHidePass();
                break;

            case R.id.toolbarBackImg:
                onBackPressed();
                break;

            case R.id.forgotResetPasswordSubmit:
                if (forgotPasswordOtp.getText().toString().trim().length() == 0) {
                    Functions.Alert(ForgotResetPass.this, "Please enter OTP.", Functions.AlertType.Error);
                    return;
                }

                if (forgotPasswordEdit.getText().toString().trim().length() == 0) {
                    Functions.Alert(ForgotResetPass.this, "Please enter new password", Functions.AlertType.Error);
                    return;
                }

                if (confirmPasswordEdit.getText().toString().trim().length() == 0) {
                    Functions.Alert(ForgotResetPass.this, "Please enter confirm password", Functions.AlertType.Error);
                    return;
                }

                if (!forgotPasswordEdit.getText().toString().trim().equalsIgnoreCase(confirmPasswordEdit.getText().toString().trim())) {
                    Functions.Alert(ForgotResetPass.this, "New Password is not matched with confirm password", Functions.AlertType.Error);
                    return;
                }

                resetPass();
                break;

        }

    }

    private void showConfirmHidePass() {

        if (ispassword_visible) {

            ConfirmPasswordVisible(true);
            ispassword_visible = false;
            confirm_password_eye_icon.setBackground(getResources().getDrawable(R.drawable.ic_eye_unselected));
            confirmPasswordEdit.setSelection(confirmPasswordEdit.getText().length());

        } else {

            ConfirmPasswordVisible(false);
            ispassword_visible = true;
            confirm_password_eye_icon.setBackground(getResources().getDrawable(R.drawable.ic_eye_blue));
            confirmPasswordEdit.setSelection(confirmPasswordEdit.getText().length());

        }

    }

    private void showNewHidePass() {

        if (ispassword_visible) {

            NewPasswordVisible(true);
            ispassword_visible = false;
            new_password_eye_icon.setBackground(getResources().getDrawable(R.drawable.ic_eye_unselected));
            forgotPasswordEdit.setSelection(forgotPasswordEdit.getText().length());

        } else {

            NewPasswordVisible(false);
            ispassword_visible = true;
            new_password_eye_icon.setBackground(getResources().getDrawable(R.drawable.ic_eye_blue));
            forgotPasswordEdit.setSelection(forgotPasswordEdit.getText().length());

        }

    }

    public void ConfirmPasswordVisible(boolean password_visible) {

        if (password_visible) {

            confirmPasswordEdit.setTransformationMethod(new PasswordTransformationMethod());
            confirmPasswordEdit.setSelection(confirmPasswordEdit.length());
            confirmPasswordEdit.setSelection(confirmPasswordEdit.length());

        } else {

            confirmPasswordEdit.setTransformationMethod(null);

        }

    }

    public void NewPasswordVisible(boolean password_visible) {

        if (password_visible) {

            forgotPasswordEdit.setTransformationMethod(new PasswordTransformationMethod());
            forgotPasswordEdit.setSelection(forgotPasswordEdit.length());
            forgotPasswordEdit.setSelection(forgotPasswordEdit.length());

        } else {

            forgotPasswordEdit.setTransformationMethod(null);

        }

    }

    private void resetPass() {

        BaseRequest baseRequest = new BaseRequest(this);
        baseRequest.setRunInBackground(false);
        baseRequest.setContainer(custom_pb, null);
        //baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {

                try {

                    Log.e("resetres", fullResponse + "//");

                    //{"success":true,"sessionKey":"20191119062406000000TQ7ba1"}
                    JSONObject jsonObject = new JSONObject(fullResponse);

                    //SessionParam.setSaveSessionKey(ForgotResetPass.this, jsonObject.getString("sessionKey"));
                    SessionParam.setSaveSessionKey(ForgotResetPass.this, "");

                } catch (JSONException e) {

                }

                Intent login = new Intent(ForgotResetPass.this, LoginActivity.class);
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);

                //startActivity(LoginActivity.getIntent(ForgotResetPass.this));
                //finish();

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

                Functions.Alert(ForgotResetPass.this, message, Functions.AlertType.Error);

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }

        });

        /*"email": "test@mailinator.com", (This fieldis options, if session key exiting header)
          "otp": "303398",
          "password": "Welcome123"*/

        JsonObject object = null;
        object = Functions.getInstance().getJsonObject("email", email, "otp", forgotPasswordOtp.getText().toString().trim(), "password", forgotPasswordEdit.getText().toString().trim());
        Log.e("forgoteml", object.toString());
        baseRequest.callAPIPost(1, object, getResources().getString(R.string.resetPassword));

    }

}
