package com.level_sense.app.Auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.level_sense.app.RetroFit.BaseRequest;
import com.level_sense.app.RetroFit.RequestReceiver;
import com.level_sense.app.Session.SessionParam;
import com.level_sense.app.Utility.Functions;
import com.level_sense.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.email_edt)
    EditText emailEdt;
    @BindView(R.id.password_edt)
    EditText passwordEdt;
    @BindView(R.id.custom_pb)
    View custom_pb;
    @BindView(R.id.forgotPassword)
    TextView forgotPassword;
    @BindView(R.id.password_eye_icon)
    ImageView passwordEyeIcon;
    @BindView(R.id.rememberCheck)
    CheckBox rememberCheck;
    boolean ispassword_visible = false;
    String email, password;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //startActivity(new Intent(this, DemoActivity.class));
        //finish();
        Log.e("checbx", SessionParam.getPrefData(LoginActivity.this, SessionParam.checkbox) + "//");

        if (SessionParam.getPrefData(LoginActivity.this, SessionParam.checkbox) != null && SessionParam.getPrefData(LoginActivity.this, SessionParam.checkbox).equalsIgnoreCase("1")) {

            rememberCheck.setChecked(true);
            passwordEdt.setText(SessionParam.getPrefData(LoginActivity.this, SessionParam.savePass));
            emailEdt.setText(SessionParam.getPrefData(LoginActivity.this, SessionParam.saveEmail));

        }

    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    public void PasswordVisible(boolean password_visible) {

        if (password_visible) {
            passwordEdt.setTransformationMethod(new PasswordTransformationMethod());
            passwordEdt.setSelection(passwordEdt.length());
            passwordEdt.setSelection(passwordEdt.length());
        } else {
            passwordEdt.setTransformationMethod(null);
        }
    }

    @OnClick({R.id.login_btn, R.id.register_btn, R.id.password_eye_icon, R.id.forgotPassword})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.login_btn:

                if (isValidate())
                    CallLoginAPI(email, password);

                break;

            case R.id.register_btn:

               /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.level-sense.com/account/register"));
                startActivity(browserIntent);*/

                Intent reg = new Intent(LoginActivity.this, RegisterActivity.class);
                reg.putExtra("data", "2");
                startActivity(reg);

//             startActivity(RegisterActivity.getIntent(this));

                break;

            //todo for forgot password
            case R.id.password_eye_icon:

                showHidePass();

                break;

            case R.id.forgotPassword:

              /*Intent forgot = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.level-sense.com/account/login?resetpassword=%22%22"));
                startActivity(forgot);*/

              /*Intent forgot = new Intent(LoginActivity.this, RegisterActivity.class);
                forgot.putExtra("data", "1");
                startActivity(forgot);*/
                //showHidePass();



                startActivity( new Intent(LoginActivity.this,ForgotPassActivity.class));

                break;

        }

    }

    private void showHidePass() {
        if (ispassword_visible) {

            PasswordVisible(true);
            ispassword_visible = false;
            passwordEyeIcon.setBackground(getResources().getDrawable(R.drawable.ic_eye_unselected));
            passwordEdt.setSelection(passwordEdt.getText().length());

        } else {

            PasswordVisible(false);
            ispassword_visible = true;
            passwordEyeIcon.setBackground(getResources().getDrawable(R.drawable.ic_eye_blue));
            passwordEdt.setSelection(passwordEdt.getText().length());

        }
    }

    private boolean isValidate() {

        email = emailEdt.getText().toString().trim();
        password = passwordEdt.getText().toString().trim();

        if (email.length() <= 0) {

            Functions.Alert(LoginActivity.this, "Please enter email.", Functions.AlertType.Error);
            return false;

        } else if (password.length() <= 0) {

            Functions.Alert(LoginActivity.this, "Please enter password.", Functions.AlertType.Error);

            return false;

        } else return true;

    }

    public void CallLoginAPI(String email, String password) {

        Log.e("chx", rememberCheck.isChecked() + "//");

        if (rememberCheck.isChecked()) {
            SessionParam.saveCheckBoxStatus(LoginActivity.this, "1");
            SessionParam.saveEmail(LoginActivity.this, email);
            SessionParam.savePass(LoginActivity.this, password);
        } else {
            SessionParam.saveCheckBoxStatus(LoginActivity.this, "0");
        }

        BaseRequest baseRequest = new BaseRequest(this);
        baseRequest.setRunInBackground(false);
        baseRequest.setContainer(custom_pb, null);
        //baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {

                try {

                    JSONObject jsonObject = new JSONObject(fullResponse);
                    SessionParam.setSaveSessionKey(LoginActivity.this, jsonObject.getString("sessionKey"));

                } catch (JSONException e) {

                }

                startActivity(DashBoardActivity.getIntent(LoginActivity.this));
                 finish();

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

                Functions.Alert(LoginActivity.this, message, Functions.AlertType.Error);

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }
        });


        JsonObject object = null;
        object = Functions.getInstance().getJsonObject("email", email, "password", password);

        baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_login));

    }

}
