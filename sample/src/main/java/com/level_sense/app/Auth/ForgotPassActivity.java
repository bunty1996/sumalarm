package com.level_sense.app.Auth;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class ForgotPassActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.forgotEmailLinear)
    LinearLayout forgotEmailLinear;
    @BindView(R.id.forgotPasswordEmail)
    EditText forgotPasswordEmail;
    /* @BindView(R.id.forgotPasswordOtp)
     EditText forgotPasswordOtp;*/
    @BindView(R.id.otpText)
    TextView otpText;
    @BindView(R.id.forgotPasswordSubmit)
    Button forgotPasswordSubmit;
    @BindView(R.id.custom_pb)
    View custom_pb;

    @BindView(R.id.toolbar_common)
    Toolbar toolbar_common;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.toolbarBackImg)
    ImageView toolbarBackImg;


    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pass);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        toolbar_title.setText(R.string.forgot_pass);
        toolbarBackImg.setImageResource(R.drawable.ic_back_icon);
        toolbarBackImg.setOnClickListener(this);
        forgotPasswordSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.toolbarBackImg:
                onBackPressed();
                break;

            case R.id.forgotPasswordSubmit:

//                if (forgotPasswordOtp.getVisibility() == View.GONE) {

                if (forgotPasswordEmail.getText().toString().trim().length() == 0) {

                    Functions.Alert(ForgotPassActivity.this, "Please enter email.", Functions.AlertType.Error);
                    return;

                } else if (!forgotPasswordEmail.getText().toString().trim().matches(emailPattern)) {

                    Functions.Alert(ForgotPassActivity.this, "Please enter the correct email.", Functions.AlertType.Error);
                    return;

                }

                sendOtp(forgotPasswordEmail.getText().toString().trim());

              /*  } else {

                    if (forgotPasswordOtp.getText().toString().trim().length() == 0) {
                        Functions.Alert(ForgotPassActivity.this, "Please enter OTP.", Functions.AlertType.Error);
                        return;

                    }

                    Intent forgotReset = new Intent(ForgotPassActivity.this, ForgotResetPass.class);
                    forgotReset.putExtra("email", forgotPasswordEmail.getText().toString().trim());
                    forgotReset.putExtra("otp", forgotPasswordOtp.getText().toString().trim());

                    startActivity(forgotReset);

                }*/

                break;

        }

    }

    private void sendOtp(String email) {

        BaseRequest baseRequest = new BaseRequest(this);
        baseRequest.setRunInBackground(false);
        baseRequest.setContainer(custom_pb, null);
        //baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {

                try {

                    Log.e("forgotres", fullResponse + "//");

                    JSONObject jsonObject = new JSONObject(fullResponse);

                    if (jsonObject.optBoolean("success")) {
                     /*   otpText.setVisibility(View.VISIBLE);
                        forgotPasswordOtp.setVisibility(View.VISIBLE);
                        forgotPasswordEmail.setVisibility(View.GONE);*/
                        SessionParam.setSaveSessionKey(ForgotPassActivity.this, jsonObject.getString("sessionKey"));
                        Intent forgotReset = new Intent(ForgotPassActivity.this, ForgotResetPass.class);
                        forgotReset.putExtra("email", forgotPasswordEmail.getText().toString().trim());
                        //forgotReset.putExtra("otp", forgotPasswordOtp.getText().toString().trim());

                        startActivity(forgotReset);
                    } else {
                        otpText.setVisibility(View.GONE);
//                        forgotPasswordOtp.setVisibility(View.GONE);
                        forgotPasswordEmail.setVisibility(View.VISIBLE);
                        Functions.Alert(ForgotPassActivity.this, jsonObject.optString("message"), Functions.AlertType.Error);

                    }

                } catch (JSONException e) {

                }

              /*startActivity(DashBoardActivity.getIntent(ForgotResetPass.this));
                finish();*/

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

                Functions.Alert(ForgotPassActivity.this, message, Functions.AlertType.Error);

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }

        });

        JsonObject object = null;
        object = Functions.getInstance().getJsonObject("email", email);
        Log.e("forgoteml", object.toString());
        baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_sendOtp));

    }

   /*private void setPass(String email){
        BaseRequest baseRequest = new BaseRequest(this);
        baseRequest.setRunInBackground(false);
        baseRequest.setContainer(custom_pb, null);
      //baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver(){
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject){
                try {
                    Log.e("forgotres", fullResponse + "//");
                    JSONObject jsonObject = new JSONObject(fullResponse);
                *//*SessionParam.setSaveSessionKey(ForgotPassActivity.this, jsonObject.getString("sessionKey"));*//*
                } catch (JSONException e){
                }
                startActivity(DashBoardActivity.getIntent(ForgotPassActivity.this));
                finish();
            }
            @Override
            public void onFailure(int requestCode, String errorCode, String message) {
                Functions.Alert(ForgotPassActivity.this, message, Functions.AlertType.Error);
            }
            @Override
            public void onNetworkFailure(int requestCode, String message) {
            }
        });
         *//*"email": "test@mailinator.com", (This fieldis options, if session key exiting header)
           "otp": "303398",
           "password": "Welcome123"*//*
        JsonObject object = null;
        object = Functions.getInstance().getJsonObject("email", email, otp, "", "password", "");
        Log.e("forgoteml", object.toString());
        baseRequest.callAPIPost(1, object, getResources().getString(R.string.resetPassword)); }*/
}
