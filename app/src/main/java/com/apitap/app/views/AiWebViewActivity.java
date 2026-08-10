package com.apitap.app.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.apitap.app.R;

public class AiWebViewActivity extends Activity {

    private static final int REQUEST_AI_PERMISSIONS = 1001;
    private static final int FILE_CHOOSER_REQUEST = 1002;

    private WebView webView;
    private ValueCallback<Uri[]> filePathCallback;

    private final String AI_URL = "https://ai.atapai.com/embed";

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ai_webview);

        webView = findViewById(R.id.aiWebView);
        ImageButton btnCloseAi = findViewById(R.id.btnCloseAi);

        btnCloseAi.setOnClickListener(v -> finish());

        View aiRoot = findViewById(R.id.aiRoot);

        aiRoot.setOnApplyWindowInsetsListener((view, insets) -> {

            int topInset = insets.getInsets(
                    WindowInsets.Type.statusBars()
            ).top;

            int rightInset = insets.getInsets(
                    WindowInsets.Type.systemBars()
            ).right;

            btnCloseAi.setTranslationY(topInset + 8);
            btnCloseAi.setTranslationX(-rightInset);

            return insets;
        });

        setupWebView();

        requestRequiredPermissions();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        webView.setWebContentsDebuggingEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view,
                    WebResourceRequest request) {

                Uri uri = request.getUrl();

                // Keep navigation inside ATAP AI.
                if ("https".equals(uri.getScheme())
                        && "ai.atapai.com".equals(uri.getHost())) {

                    return false;
                }

                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {

                Log.d(
                        "AI_WEBVIEW_CONSOLE",
                        consoleMessage.message()
                                + " -- line "
                                + consoleMessage.lineNumber()
                                + " -- "
                                + consoleMessage.sourceId()
                );

                return true;
            }

            @Override
            public void onPermissionRequest(final PermissionRequest request) {

                Log.d("AI_WEBVIEW", "onPermissionRequest called");

                for (String resource : request.getResources()) {
                    Log.d("AI_WEBVIEW", "Requested resource: " + resource);
                }

                runOnUiThread(() -> {

                    String[] resources = request.getResources();

                    if (ContextCompat.checkSelfPermission(
                            AiWebViewActivity.this,
                            Manifest.permission.RECORD_AUDIO
                    ) != PackageManager.PERMISSION_GRANTED) {

                        Log.d("AI_WEBVIEW", "RECORD_AUDIO not granted");
                        request.deny();
                        return;
                    }

                    if (ContextCompat.checkSelfPermission(
                            AiWebViewActivity.this,
                            Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED) {

                        Log.d("AI_WEBVIEW", "CAMERA not granted");
                        request.deny();
                        return;
                    }

                    Log.d("AI_WEBVIEW", "Granting WebView permissions");

                    request.grant(resources);
                });
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(
                    String origin,
                    GeolocationPermissions.Callback callback) {

                callback.invoke(origin, true, false);
            }

            @Override
            public boolean onShowFileChooser(
                    WebView webView,
                    ValueCallback<Uri[]> filePathCallback,
                    FileChooserParams fileChooserParams) {

                AiWebViewActivity.this.filePathCallback = filePathCallback;

                Intent intent = fileChooserParams.createIntent();

                try {
                    startActivityForResult(intent, FILE_CHOOSER_REQUEST);
                } catch (Exception e) {
                    AiWebViewActivity.this.filePathCallback = null;
                    return false;
                }

                return true;
            }
        });
    }

    private void requestRequiredPermissions() {

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            webView.loadUrl(AI_URL);
            return;
        }

        String[] permissions = {
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        boolean needsPermission = false;

        for (String permission : permissions) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
            ) != PackageManager.PERMISSION_GRANTED) {

                needsPermission = true;
                break;
            }
        }

        if (needsPermission) {

            ActivityCompat.requestPermissions(
                    this,
                    permissions,
                    REQUEST_AI_PERMISSIONS
            );

        } else {

            webView.loadUrl(AI_URL);
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_CHOOSER_REQUEST) {

            if (filePathCallback == null) {
                return;
            }

            Uri[] results = null;

            if (resultCode == RESULT_OK && data != null) {

                if (data.getData() != null) {
                    results = new Uri[]{
                            data.getData()
                    };
                }
            }

            filePathCallback.onReceiveValue(results);
            filePathCallback = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == REQUEST_AI_PERMISSIONS) {
            webView.loadUrl(AI_URL);
        }
    }

    @Override
    public void onBackPressed() {

        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}