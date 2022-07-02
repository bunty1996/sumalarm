package com.level_sense.app.Auth;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.level_sense.app.Fragment.GetHardwareFragment;
import com.level_sense.app.R;
import com.level_sense.app.view.ScreenRouter;
import com.level_sense.app.view.cart.CartClickActionEvent;

public class Hardware extends AppCompatActivity implements View.OnClickListener {

    private TextView loginView;
    Fragment fragment;
    private LinearLayout hardwareShopingCart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hardware);
        loginView = (TextView) findViewById(R.id.loginView);
        hardwareShopingCart = (LinearLayout) findViewById(R.id.hardwareShopingCart);
        fragment = GetHardwareFragment.newInstance();
        setFragment(fragment);
        loginView.setOnClickListener(this);
        hardwareShopingCart.setOnClickListener(this);

    }

    void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.loginView:

                startActivity(LoginActivity.getIntent(Hardware.this));
                finish();

                break;

            case R.id.hardwareShopingCart:

                ScreenRouter.route(Hardware.this, new CartClickActionEvent());

                break;

        }

    }

}
