package com.idempiere.vasanth.idempieremobileapp;


import android.annotation.TargetApi;

import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public WebView myWebView;
    private static final int PERMISSION_REQUEST_CODE = 1;
    public String globalurl;
    public String globalcookie;
    public static Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //cookie start
        //LOLLIPOP and higher version enter here
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        }else{ // Less than lollipop
            showToastMessage("cookie - less thanlollipop");
//            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(myWebView.getContext());
//            CookieManager cookieManager = CookieManager.getInstance();
//            cookieManager.setAcceptCookie(true);
//            cookieManager.removeSessionCookie();
//            cookieSyncManager.sync();
        }
        //cookie end


        if (savedInstanceState != null) {
            ((WebView) findViewById(R.id.webview)).restoreState(savedInstanceState);
        } else {
            myWebView = (WebView) findViewById(R.id.webview);
            WebSettings webSettings = myWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setLoadsImagesAutomatically(true);
            webSettings.setBuiltInZoomControls(false);
            webSettings.setBuiltInZoomControls(false);
            webSettings.setAppCacheEnabled(false);
            myWebView.setWebChromeClient(new WebChromeClient());
            //myWebView.loadUrl("http://45.79.139.150/m/");
            String mobilurl ="http://skpcst.com/m/";
             myWebView.loadUrl(mobilurl);
            showToastMessage(mobilurl);
            myWebView.setWebViewClient(new YourWebClient(MainActivity.this));

            handler = new Handler(getApplicationContext().getMainLooper());
        }



    }

    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public void showToastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    public void requestPermission(String url, String cookie) {
        MainActivity.this.globalurl = url;
        MainActivity.this.globalcookie = cookie;
        if (ActivityCompat.shouldShowRequestPermissionRationale
                (MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //  this.showToastMessage("Write External Storage permission is required.
            // Please allow it in App Settings.");

        } else {
            ActivityCompat.requestPermissions
                    (MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MainActivity.this ,
                                    "Permission Granted, Now you can use local storage ", Toast.LENGTH_SHORT).show();
                        }
                    });
                    DownloadManagerUsedToWrite2(this.globalurl,this.globalcookie);

                }

                else {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MainActivity.this ,
                                    "Permission Denied, You cannot use local storage ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
        }
    }

    public void DownloadManagerUsedToWrite2(String url, String cookie){
        showToastMessage("2_0");
        DownloadManager.Request dmRequest = new DownloadManager.Request(Uri.parse(url));
        showToastMessage("2_1");
        dmRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        showToastMessage("2_ 2");
        String filename = "ErpReport.pdf";
        dmRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename);
        showToastMessage("2_ 3");
        dmRequest.addRequestHeader("Cookie", cookie);
        showToastMessage("2_ 4");
        DownloadManager dm = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        showToastMessage("2_ 5");
        dm.enqueue(dmRequest);
        showToastMessage("2_ 6");
    }

}