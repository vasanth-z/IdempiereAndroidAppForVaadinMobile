package com.idempiere.vasanth.idempieremobileapp;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import static android.content.ContentValues.TAG;


public class YourWebClient extends WebViewClient {

        private static MainActivity mainActivity;
    private static String cookieSharing;
        public YourWebClient(MainActivity _context){
            this.mainActivity = _context;
        }

        public void showToastMessage_WebClient(final String toastMessage) {
            mainActivity.runOnUiThread(new Runnable() {
               public void run() {
                    Toast.makeText(mainActivity.getBaseContext(), toastMessage, Toast.LENGTH_SHORT).show();
                }
            });
            //toast(toastMessage);

        }


    @Override
    public void onPageFinished(WebView view, String url){


        String cookie = CookieManager.getInstance().getCookie(url);
        Log.d(TAG, "onPageFinished  " + cookie);
        cookieSharing = cookie;
//        CookieSyncManager syncManager = CookieSyncManager.createInstance(Main.this);
//        syncManager.sync();
//
//        CookieManager manager = new CookieManager();
//        manager
//        CookieHandler.setDefault(manager);
//
//        try {
//            URL blah = new URL(url);
//            HttpURLConnection con = (HttpURLConnection) blah.openConnection();
//            readStream(con.getInputStream()); // outputting html
//        }
//        catch (Exception e) {
//        }
//
//        CookieStore cookieJar = manager.getCookieStore();
//        List<HttpCookie> cookies = cookieJar.getCookies();
//
//        for (HttpCookie cookie: cookies) {
//            Log.d(TAG, "cookie name : "+cookie.getName().toString());
//        }
    }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            //Less than LOLLIPOP version enter here
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                if (CheckIfTheUrlIsReportUrl(url)) {
                    showToastMessage_WebClient("less than LOLLIPOP");
                    try {
                        showToastMessage_WebClient("Download started...");
                        UrlsTakesCareByDownloadManager(url);
                    } catch (Exception eee) {
                        showToastMessage_WebClient("UrlsTakesCareByDownloadManager() Exception -" + eee.getMessage());
                    }
                    return null;
                }
            }
            return super.shouldInterceptRequest(view, url);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            //LOLLIPOP and higher version enter here
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String url = request.getUrl().toString();
                if (CheckIfTheUrlIsReportUrl(url)) {
                    showToastMessage_WebClient("greater than or equal LOLLIPOP");
                    try {
                       // showToastMessage_WebClient("Download started...");
                        UrlsTakesCareByDownloadManager(url);
                    } catch (Exception eee) {
                        Log.i("INFO","vasanth2 -- " + eee.getMessage());
                        showToastMessage_WebClient("UrlsTakesCareByDownloadManager() Exception -" + eee.getMessage());
                    }
                    return null;
                }
            }
            return super.shouldInterceptRequest(view, request);
        }

        private Boolean CheckIfTheUrlIsReportUrl(String url) {
            if (url.contains("/m/APP/connector/") && url.contains("/dl"))
                return true;
            else
                return false;
        }


        private boolean checkPermission() {
            int result = ContextCompat.checkSelfPermission(mainActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }





        private void UrlsTakesCareByDownloadManager(final String url) {
            showToastMessage_WebClient("cookie manger before");

            String cookie = "";

            //LOLLIPOP and higher version enter here
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cookie = CookieManager.getInstance().getCookie(url);
            }else{ // Less than lollipop
                showToastMessage_WebClient("kitkat cookie");
                cookie = this.cookieSharing;
            }

            final String finalCookie = cookie;
            showToastMessage_WebClient("cookie manger after" + cookie);

            if (Build.VERSION.SDK_INT >= 23)   { //marshmallow and after
                showToastMessage_WebClient("* Marshmallow *");
                if (checkPermission())   {
                    // Code for above or equal 23 API Oriented Device. Your Permission granted already .Do next code
                    showToastMessage_WebClient("* write permission yes in Marshmallow *");
                    MainActivity.handler.post(new Runnable() {
                                                  @Override
                                                  public void run() {
                                                      mainActivity.DownloadManagerUsedToWrite2(url, finalCookie);
                                                  }
                                              }
                    );
                } else {
                    // Code for permission
                    showToastMessage_WebClient("* write permission NO in Marshmallow *");
                    MainActivity.handler.post(new Runnable() {
                                                  @Override
                                                  public void run() {
                                                      mainActivity.requestPermission(url,finalCookie);
                                                  }
                                              });

                }
            } else  {
                // less than marshmallow - 23 API Oriented Device. Do next code
                showToastMessage_WebClient("* Less than Marshmallow *");

                MainActivity.handler.post(new Runnable() {
                                              @Override
                                              public void run() {
                                                  mainActivity.DownloadManagerUsedToWrite2(url, finalCookie);
                                              }
                                          }
                );

            }
        }
    }



