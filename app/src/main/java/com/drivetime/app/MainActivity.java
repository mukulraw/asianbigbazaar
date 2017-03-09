package com.drivetime.app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.Objects;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity {

    private WebView myWebView;
    private WebSettings webSettings;
    private ProgressBar progress;
    private ImageView image;




    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);







        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED , WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        image = (ImageView)findViewById(R.id.image);

        progress = (ProgressBar)findViewById(R.id.progress);

        onNewIntent(getIntent());

        myWebView = (WebView)findViewById(R.id.webView);

        //progress.setVisibility(View.GONE);
        if (myWebView != null) {
            myWebView.setVisibility(View.GONE);
        }

        if (myWebView != null) {
            webSettings = myWebView.getSettings();
        }

        webSettings.setJavaScriptEnabled(true);


        myWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        myWebView.getSettings().setAppCacheEnabled(true);
        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setEnableSmoothTransition(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);



        //webSettings.setAppCacheEnabled(true);
        //webSettings.setLoadWithOverviewMode(true);
        //webSettings.setUseWideViewPort(true);
        //webSettings.setDomStorageEnabled(true);
        //webSettings.setLoadsImagesAutomatically(true);
        //webSettings.setDatabaseEnabled(true);
        //webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //webSettings.setSupportZoom(true);
        //webSettings.setBuiltInZoomControls(true);
        //webSettings.setDisplayZoomControls(false);
        //myWebView.setWebViewClient(new MyWebViewClient());
        //myWebView.setWebChromeClient(new MyWebChromeClient());
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //    webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
       // }


        // webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

       // myWebView.addJavascriptInterface(new JavaScriptInterface(), "Android");

        //if (Build.VERSION.SDK_INT >= 19) {

          //  myWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        //} else {

          //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            //    myWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            //}

        //}



        //load the home page URL
        //load the home page URL



        myWebView.setWebViewClient(new MyWebViewClient());

        //Add a JavaScriptInterface, so I can make calls from the web to Java methods
        myWebView.addJavascriptInterface(new myJavaScriptInterface(), "CallToAnAndroidFunction");

        myWebView.setWebChromeClient(new MyWebChromeClient());
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext() , R.anim.fade_in);
        image.startAnimation(anim);



        myWebView.loadUrl("http://godrivo.com/");



    }


    public class myJavaScriptInterface {
        @JavascriptInterface
        public void setVisible(){
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    myWebView.setVisibility(View.VISIBLE);
                }
            });
        }

    }




    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (Objects.equals(url, "https://play.google.com/store/apps/details?id=com.drivetime.app"))
            {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.drivetime.app"));
                startActivity(intent);
            }
            else
            {
                myWebView.loadUrl(url);
            }

            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progress.setVisibility(View.GONE);
            myWebView.loadUrl("javascript:(function(){"+"document.getElementById('show').style.display ='none';"+"})()");
            //Call to a function defined on my myJavaScriptInterface
            myWebView.loadUrl("javascript: window.CallToAnAndroidFunction.setVisible()");
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        //display alert message in Web View
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            new AlertDialog.Builder(view.getContext())
                    .setMessage(message).setCancelable(true).show();
            result.confirm();
            return true;
        }


        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progress.setProgress(newProgress);

            progress.setVisibility(View.VISIBLE);










            if (newProgress > 18)
            {

                image.animate().translationY(image.getHeight()).alpha(0.0f);
                image.setVisibility(View.GONE);




                myWebView.setVisibility(View.VISIBLE);
                //progress.setVisibility(View.VISIBLE);
            }

            if (newProgress<100  && newProgress>18)
            {
                progress.setVisibility(View.VISIBLE);


            }
            if (newProgress == 100)
            {
                progress.setVisibility(View.GONE);
            }


        }

    }

    private class JavaScriptInterface {

        // Instantiate the interface and set the context
        JavaScriptInterface() {
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

        if (myWebView.canGoBack())
        {
            myWebView.goBack();
        }
        else
        {
            super.onBackPressed();
        }

    }


}
