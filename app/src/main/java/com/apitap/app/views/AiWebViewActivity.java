package com.apitap.app.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
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
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.apitap.app.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class AiWebViewActivity extends Activity {

    private static final int REQUEST_AI_PERMISSIONS = 1001;
    private static final int FILE_CHOOSER_REQUEST = 1002;

    private WebView webView;
    private LinearLayout aiPanel;

    private ValueCallback<Uri[]> filePathCallback;

    private final String AI_URL =
            "https://ai.atapai.com/embed";

    private boolean isExpanded = false;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        Window window = getWindow();

        window.setBackgroundDrawableResource(
                android.R.color.transparent
        );

        window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
        );
        setContentView(R.layout.activity_ai_webview);

        webView = findViewById(R.id.aiWebView);
        aiPanel = findViewById(R.id.aiPanel);

        ImageButton btnCloseAi =
                findViewById(R.id.btnCloseAi);

/*        ImageView btnExpandAi =
                findViewById(R.id.btnExpandAi);*/


        BottomSheetBehavior<LinearLayout> bottomSheet =
                BottomSheetBehavior.from(aiPanel);

        bottomSheet.setFitToContents(false);

        bottomSheet.setHalfExpandedRatio(0.55f);

        bottomSheet.setExpandedOffset(0);

        bottomSheet.setHideable(false);

        bottomSheet.setState(
                BottomSheetBehavior.STATE_HALF_EXPANDED
        );

        btnCloseAi.setOnClickListener(v -> finish());

/*        btnExpandAi.setOnClickListener(v -> {

            if (isExpanded) {
                collapseAi();
                btnExpandAi.setImageResource(R.drawable.ic_collapse);
            } else {
                expandAi();
                btnExpandAi.setImageResource(R.drawable.ic_minimize);
            }
        });*/

        setupWebView();
        setupInitialPanel();

        requestRequiredPermissions();
    }

    private void setupInitialPanel() {

        aiPanel.post(() -> {

            int screenHeight =
                    aiPanel.getRootView().getHeight();

            int halfHeight =
                    (int) (screenHeight * 0.55f);

            aiPanel.getLayoutParams().height =
                    halfHeight;

            aiPanel.requestLayout();
        });
    }

    private void expandAi() {

        isExpanded = true;

        aiPanel.getLayoutParams().height =
                LinearLayout.LayoutParams.MATCH_PARENT;

        aiPanel.requestLayout();
    }

    private void collapseAi() {

        isExpanded = false;

        aiPanel.post(() -> {

            int screenHeight =
                    aiPanel.getRootView().getHeight();

            aiPanel.getLayoutParams().height =
                    (int) (screenHeight * 0.55f);

            aiPanel.requestLayout();
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {

        WebSettings settings =
                webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);

        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);

        settings.setMediaPlaybackRequiresUserGesture(false);

        WebView.setWebContentsDebuggingEnabled(true);

        webView.setBackgroundColor(Color.WHITE);

        webView.setWebViewClient(
                new WebViewClient() {

                    @Override
                    public boolean shouldOverrideUrlLoading(
                            WebView view,
                            WebResourceRequest request) {

                        Uri uri = request.getUrl();

                        if ("https".equals(uri.getScheme())
                                && "ai.atapai.com"
                                .equals(uri.getHost())) {

                            return false;
                        }

                        return true;
                    }
                }
        );

        webView.setWebChromeClient(
                new WebChromeClient() {

                    @Override
                    public boolean onConsoleMessage(
                            ConsoleMessage consoleMessage) {

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
                    public void onPermissionRequest(
                            final PermissionRequest request) {

                        Log.d(
                                "AI_WEBVIEW",
                                "onPermissionRequest called"
                        );

                        for (String resource :
                                request.getResources()) {

                            Log.d(
                                    "AI_WEBVIEW",
                                    "Requested resource: "
                                            + resource
                            );
                        }

                        runOnUiThread(() -> {

                            String[] resources =
                                    request.getResources();

                            if (ContextCompat.checkSelfPermission(
                                    AiWebViewActivity.this,
                                    Manifest.permission.RECORD_AUDIO
                            ) != PackageManager.PERMISSION_GRANTED) {

                                request.deny();
                                return;
                            }

                            if (ContextCompat.checkSelfPermission(
                                    AiWebViewActivity.this,
                                    Manifest.permission.CAMERA
                            ) != PackageManager.PERMISSION_GRANTED) {

                                request.deny();
                                return;
                            }

                            request.grant(resources);
                        });
                    }

                    @Override
                    public void onGeolocationPermissionsShowPrompt(
                            String origin,
                            GeolocationPermissions.Callback callback) {

                        callback.invoke(
                                origin,
                                true,
                                false
                        );
                    }

                    @Override
                    public boolean onShowFileChooser(
                            WebView webView,
                            ValueCallback<Uri[]> callback,
                            FileChooserParams params) {

                        filePathCallback = callback;

                        Intent intent =
                                params.createIntent();

                        try {

                            startActivityForResult(
                                    intent,
                                    FILE_CHOOSER_REQUEST
                            );

                        } catch (Exception e) {

                            filePathCallback = null;
                            return false;
                        }

                        return true;
                    }
                }
        );
    }

    private void requestRequiredPermissions() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

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

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (requestCode == FILE_CHOOSER_REQUEST) {

            if (filePathCallback == null) {
                return;
            }

            Uri[] results = null;

            if (resultCode == RESULT_OK
                    && data != null
                    && data.getData() != null) {

                results = new Uri[]{
                        data.getData()
                };
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

        if (requestCode ==
                REQUEST_AI_PERMISSIONS) {

            webView.loadUrl(AI_URL);
        }
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    protected void onDestroy() {

        if (webView != null) {

            webView.stopLoading();
            webView.destroy();
        }

        super.onDestroy();
    }
}