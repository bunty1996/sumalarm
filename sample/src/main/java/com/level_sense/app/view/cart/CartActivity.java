/*
 *   The MIT License (MIT)
 *
 *   Copyright (c) 2015 Shopify Inc.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package com.level_sense.app.view.cart;

import android.app.Dialog;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LifecycleRegistryOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.level_sense.app.BuildConfig;
import com.level_sense.app.domain.model.ShopSettings;
import com.level_sense.app.view.ProgressDialogHelper;
import com.level_sense.app.view.ScreenRouter;
import com.level_sense.app.view.checkout.CheckoutViewModel;
import com.shopify.buy3.pay.PayHelper;
import com.level_sense.app.BaseApplication;
import com.level_sense.app.R;
import com.level_sense.app.domain.model.Checkout;
import butterknife.BindView;
import butterknife.ButterKnife;

public final class CartActivity extends AppCompatActivity implements LifecycleRegistryOwner, GoogleApiClient.ConnectionCallbacks {
    @BindView(R.id.root)
    View rootView;
    @BindView(R.id.cart_header)
    CartHeaderView cartHeaderView;
    @BindView(R.id.cart_list)
    CartListView cartListView;
    @BindView(R.id.toolbar)
    Toolbar toolbarView;

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private CartDetailsViewModel cartDetailsViewModel;
    private CartHeaderViewModel cartHeaderViewModel;

    private GoogleApiClient googleApiClient;
    private ProgressDialogHelper progressDialogHelper;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    public void onConnected(@Nullable final Bundle bundle) {
        ShopSettings shopSettings = ((BaseApplication) getApplication()).shopSettings().getValue();
        PayHelper.isReadyToPay(getApplicationContext(), googleApiClient, shopSettings.acceptedCardBrands, result -> {
            if (lifecycleRegistry.getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                cartDetailsViewModel.onGoogleApiClientConnectionChanged(true);
            }
        });
    }

    @Override
    public void onConnectionSuspended(final int i) {
        cartDetailsViewModel.onGoogleApiClientConnectionChanged(false);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);

        setSupportActionBar(toolbarView);
        getSupportActionBar().setTitle("Cart");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialogHelper = new ProgressDialogHelper(this);

        initViewModels();
        connectGoogleApiClient();

        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
    }

    @Override
    protected void onPause() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        super.onPause();
    }

    @Override
    protected void onStop() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {

        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);

        super.onDestroy();

        if (progressDialogHelper != null) {
            progressDialogHelper.dismiss();
            progressDialogHelper = null;
        }

        if (googleApiClient != null) {
            googleApiClient.disconnect();
            googleApiClient = null;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cartDetailsViewModel.handleMaskedWalletResponse(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(RealCartViewModel.class.getName(), cartDetailsViewModel.saveState());
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        cartDetailsViewModel.restoreState(savedInstanceState.getBundle(RealCartViewModel.class.getName()));
    }

    private void initViewModels() {

        ShopSettings shopSettings = ((BaseApplication) getApplication()).shopSettings().getValue();
        RealCartViewModel cartViewModel = ViewModelProviders.of(this, new ViewModelProvider.Factory() {
            @Override
            public <T extends ViewModel> T create(final Class<T> modelClass) {
                if (modelClass.equals(RealCartViewModel.class)) {
                    //noinspection unchecked
                    return (T) new RealCartViewModel(shopSettings);
                } else {
                    return null;
                }
            }
        }).get(RealCartViewModel.class);

        cartHeaderViewModel = cartViewModel;
        cartDetailsViewModel = cartViewModel;

        cartDetailsViewModel.webCheckoutCallback().observe(this.getLifecycle(), checkout -> {
            if (checkout != null) {

                Log.e("webchekout", "okkk");
                TextView webDescription,webCancel,webProceed;

                final Dialog dialog = new Dialog(CartActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.checkout_web);

                webDescription=(TextView) dialog.findViewById(R.id.webDescription);
                webCancel=(TextView) dialog.findViewById(R.id.webCancel);
                webProceed=(TextView) dialog.findViewById(R.id.webProceed);

                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.gravity = Gravity.CENTER_VERTICAL;
                webCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                webProceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        onWebCheckoutConfirmation(checkout);
                    }
                });


                dialog.show();

            }
        });

        cartDetailsViewModel.androidPayStartCheckoutCallback().observe(this.getLifecycle(), payCart -> {
            if (cartHeaderViewModel.googleApiClientConnectionData().getValue() == Boolean.TRUE && payCart != null) {
                PayHelper.requestMaskedWallet(googleApiClient, payCart, BuildConfig.ANDROID_PAY_PUBLIC_KEY);
            }
        });
        cartDetailsViewModel.androidPayCheckoutCallback().observe(this.getLifecycle(), confirmation -> {
            if (confirmation != null) {
                ScreenRouter.route(this, new AndroidPayConfirmationClickActionEvent(confirmation.checkoutId, confirmation.payCart,
                        confirmation.maskedWallet));
            }
        });
        cartDetailsViewModel.progressLiveData().observe(this, progress -> {
            if (progress != null) {
                if (progress.show) {
                    showProgress(progress.requestId);
                } else {
                    hideProgress(progress.requestId);
                }
            }
        });
        cartDetailsViewModel.errorErrorCallback().observe(this.getLifecycle(), error -> {
            if (error != null) {
                showError(error.requestId, error.t, error.message);
            }
        });

        cartHeaderView.bindViewModel(cartHeaderViewModel);

        CartListViewModel cartListViewModel = ViewModelProviders.of(this).get(CartListViewModel.class);
        cartListView.bindViewModel(cartListViewModel);
    }

    private void showProgress(final int requestId) {
        progressDialogHelper.show(requestId, null, getResources().getString(R.string.progress_loading), () -> {
            cartDetailsViewModel.cancelRequest(requestId);
            cartDetailsViewModel.progressLiveData().hide(requestId);
        });
    }

    private void hideProgress(final int requestId) {
        progressDialogHelper.dismiss(requestId);
    }

    private void onWebCheckoutConfirmation(final Checkout checkout) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(checkout.webUrl));
        startActivity(intent);
    }

    private void showError(final int requestId, final Throwable t, final String message) {

        if (message != null) {
            showAlertErrorMessage(message);
            return;
        }

        if (t instanceof CheckoutViewModel.ShippingRateMissingException) {
            showAlertErrorMessage(getString(R.string.checkout_shipping_select_shipping_rate));
            return;
        }

        showDefaultErrorMessage();
    }

    private void showAlertErrorMessage(final String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                })
                .show();
    }

    private void showDefaultErrorMessage() {
        Snackbar snackbar = Snackbar.make(rootView, R.string.default_error, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundResource(R.color.snackbar_error_background);
        snackbar.show();
    }

    private void connectGoogleApiClient() {
        if (PayHelper.isAndroidPayEnabledInManifest(this)) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
                            .setEnvironment(BuildConfig.ANDROID_PAY_ENVIRONMENT)
                            .setTheme(WalletConstants.THEME_DARK)
                            .build())
                    .addConnectionCallbacks(this)
                    .build();
            googleApiClient.connect();
        }
    }
}
