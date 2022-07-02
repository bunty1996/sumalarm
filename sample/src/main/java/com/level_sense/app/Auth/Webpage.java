package com.level_sense.app.Auth;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.level_sense.app.R;

public class Webpage extends AppCompatActivity {
private WebView webPage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webpage);

        webPage=(WebView) findViewById(R.id.webPage);
    webPage.getSettings().setJavaScriptEnabled(true);
    webPage.setWebChromeClient(new WebChromeClient());

    webPage.loadUrl("https://www.level-sense.com/account/register");

    }
}
