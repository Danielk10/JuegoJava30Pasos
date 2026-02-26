package com.diamon.ptc.policy;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import com.diamon.ptc.databinding.ActivityPolicyBinding;

public class PolicyActivity extends AppCompatActivity {

    private ActivityPolicyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPolicyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Política de Privacidad");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setupWebView();
    }

    private void setupWebView() {
        WebSettings settings = binding.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        binding.webView.setWebViewClient(new WebViewClient());
        binding.webView.loadUrl("https://cpiccompiler.blogspot.com/");
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}
