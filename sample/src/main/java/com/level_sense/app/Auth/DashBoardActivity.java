package com.level_sense.app.Auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

//import com.crashlytics.android.Crashlytics;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.JsonObject;
import com.level_sense.app.Fragment.About;
import com.level_sense.app.Fragment.ArticleDetailFragment;
import com.level_sense.app.Fragment.ArticleFragment;
import com.level_sense.app.Fragment.PersonalInfoFragment;
import com.level_sense.app.R;
import com.level_sense.app.Session.SessionParam;
import com.level_sense.app.graph.ChartActivity;
import com.level_sense.app.model.ProfileModel;
import com.level_sense.app.model.ProfileUserModel;
import com.level_sense.app.Fragment.ClaimDeviceFragment;
import com.level_sense.app.Fragment.GetHardwareFragment;
import com.level_sense.app.Fragment.MyDeviceFragment;
import com.level_sense.app.Fragment.NotificatioFragment;
import com.level_sense.app.RetroFit.BaseRequest;
import com.level_sense.app.RetroFit.RequestReceiver;
import com.level_sense.app.Utility.Functions;
import com.level_sense.app.view.ScreenRouter;
import com.level_sense.app.view.cart.CartClickActionEvent;

import java.util.HashMap;

//import io.fabric.sdk.android.Fabric;

public class DashBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    View headerView;
    MenuItem menuItem;
    Fragment fragment;
    private static DrawerLayout drawer;
    public static ActionBarDrawerToggle toggle;
    public static ActionBar actionbar;
    boolean isExit = false;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseCrashlytics firebaseCrashlytics;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, DashBoardActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseCrashlytics = FirebaseCrashlytics.getInstance();


//        firebaseCrashlytics.setCrashlyticsCollectionEnabled(true);
//        Fabric.with(this,new Crashlytics());
//        Button crashButton = new Button(this);
//        crashButton.setText("Crash!");
//        crashButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                 // Force a crash
//                throw new RuntimeException("Test Crash"); // Force a crash
//            }
//        });
////
//        addContentView(crashButton, new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setVisibility(View.VISIBLE);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                throw new RuntimeException("Test Crash");
//
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        fragment = MyDeviceFragment.newInstance();
        setFragment(fragment);
        actionbar.setTitle(R.string.my_devices);

        getSupportActionBar().setTitle(R.string.my_devices);
        fragment = MyDeviceFragment.newInstance();
     /*ProfileUserModel profileUserModel=SessionParam.getProfileData(DashBoardActivity.this).getUser();
       if(profileUserModel != null)
       ((TextView) headerView.findViewById(R.id.tVuserName)).setText(profileUserModel.getFirstName() + " " + profileUserModel.getLastName());*/

        callApiGetUser();
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "name");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isExit = false;
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

            else {
            if (isExit) {
                super.onBackPressed();

            } else {
                isExit = true;
                Toast.makeText(DashBoardActivity.this, "Again press to exit app.", Toast.LENGTH_SHORT).show();
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 5000);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        menu.findItem(R.id.cart).getActionView().setOnClickListener(v -> {
            ScreenRouter.route(this, new CartClickActionEvent());
        });
        menuItem = menu.findItem(R.id.cart);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle action bar item clicks here. The action bar will
        //automatically handle clicks on the Home/Up button, so long
        //as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_devices) {
            //Handle the camera action
            menuItem.setVisible(false);
            getSupportActionBar().setTitle(R.string.my_devices);
            fragment = MyDeviceFragment.newInstance();

        } else if (id == R.id.nav_claim_device) {
            getSupportActionBar().setTitle(R.string.claim_device);
            menuItem.setVisible(false);
            fragment = ClaimDeviceFragment.newInstance();

        } else if (id == R.id.nav_article) {
             menuItem.setVisible(false);
            fragment = ArticleFragment.newInstance();
            getSupportActionBar().setTitle(R.string.article);

        } else if (id == R.id.nav_notification) {
            menuItem.setVisible(false);
            fragment = NotificatioFragment.newInstance();
            getSupportActionBar().setTitle(R.string.notification);

        } else if (id == R.id.nav_personal_information) {

            menuItem.setVisible(false);
            fragment = PersonalInfoFragment.newInstance();
            getSupportActionBar().setTitle(R.string.personal_information);

        } else if (id == R.id.nav_get_hardware) {
            menuItem.setVisible(true);
            getSupportActionBar().setTitle(R.string.collection_list_title);
            fragment = GetHardwareFragment.newInstance();

        } else if (id == R.id.nav_about) {
            menuItem.setVisible(false);
            getSupportActionBar().setTitle(R.string.about);
            fragment = About.newInstance();

        } else if (id == R.id.nav_logout) {

            Functions.showDefaultTwoButonYesNo(DashBoardActivity.this, getString(R.string.are_you_sure_want_to_logout)
                    , R.string.app_name, new Functions.OnDialogButtonClickListener() {
                        @Override
                        public void onClick(int type) {
                            if (type == 1) {
                                callApiLogout();
                                startActivity(LoginActivity.getIntent(DashBoardActivity.this));
                               PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();
                                finish();
                            }
                        }
                    });

        }

        setFragment(fragment);
         drawer= (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void callApiLogout() {

        SessionParam.setSaveSessionKey(this, "");
        BaseRequest baseRequest = new BaseRequest(DashBoardActivity.this);
        baseRequest.setRunInBackground(true);
        JsonObject object = null;
        object = Functions.getInstance().getJsonObject();

        baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_logout));

    }

    public void setFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

    }

    private void callApiGetUser() {

        BaseRequest baseRequest = new BaseRequest(this);
        baseRequest.setRunInBackground(true);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {

                SessionParam.setPrefData(DashBoardActivity.this, ProfileModel.class.getName(), fullResponse);
                ProfileUserModel profileUserModel = SessionParam.getProfileData(DashBoardActivity.this).getUser();
                ((TextView) headerView.findViewById(R.id.tVuserName)).setText(profileUserModel.getFirstName() + " " + profileUserModel.getLastName());

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

    public static void setDrawerState(boolean isEnableDrawer)
    {
        if (isEnableDrawer)
        {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toggle.setDrawerIndicatorEnabled(false);
            toggle.setHomeAsUpIndicator(R.drawable.ic_menu);
            toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (drawer.isDrawerOpen(GravityCompat.START))
                    {
                        drawer.closeDrawer(GravityCompat.START);
                    }
                    else {
                        drawer.openDrawer(GravityCompat.START);
                    }
                }
            });
            toggle.syncState();
        }
        else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);
            toggle.setHomeAsUpIndicator(R.drawable.ic_back_icon);
            toggle.syncState();


        }

    }

}
